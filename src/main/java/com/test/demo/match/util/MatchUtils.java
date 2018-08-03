package com.test.demo.match.util;


import com.test.demo.match.module.OrderInfo;
import com.test.demo.match.module.TradeInfo;

import org.apache.commons.collections.CollectionUtils;


import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import static com.test.demo.match.constants.CallAuctionConstants.ZERO_BIGDECIMAL;
import static com.test.demo.match.constants.CallAuctionConstants.ZERO_INT;


/**
 * 匹配工具类
 */
public class MatchUtils {

    /**
     * 生成委买成交队列
     */
    public static BigDecimal generateBuyTradeInfoList(BigDecimal preLatestTradePrice, OrderInfo buyOrderInfo, Map<BigDecimal, List<OrderInfo>> buyMatchMap,
                                                      TradeInfo tradeInfo) {

        BigDecimal actualPrice = preLatestTradePrice;
        for (BigDecimal price : buyMatchMap.keySet()) {
            List<OrderInfo> orderInfoList = buyMatchMap.get(price);
            Iterator<OrderInfo> iter = orderInfoList.iterator();
            while (iter.hasNext()) {
                OrderInfo sellOrderInfo = iter.next();
                BigDecimal buyOrderQuantity = buyOrderInfo.getOrderQuantity();
                BigDecimal selltOrderQuantity = sellOrderInfo.getOrderQuantity();
                if (selltOrderQuantity.compareTo(buyOrderQuantity) > 0) {
                    buyTradeInfo(tradeInfo, actualPrice, buyOrderInfo, sellOrderInfo, 1);
                    //type 1
                    return tradeInfo.getActualPrice();
                } else if (selltOrderQuantity.compareTo(buyOrderQuantity) == 0) {
                    buyTradeInfo(tradeInfo, actualPrice, buyOrderInfo, sellOrderInfo, 0);
                    iter.remove(); //成交从队列删除
                    if (CollectionUtils.isEmpty(orderInfoList)) {
                        buyMatchMap.remove(price);
                    }
                    return tradeInfo.getActualPrice();
                } else {
                    if (selltOrderQuantity.compareTo(ZERO_BIGDECIMAL) <= ZERO_INT) { //委托量<=0
                        System.out.println("sellOrderList orderQuantity <= 0 ");
                        iter.remove(); //异常数据从队列删除
                        continue;
                    }
                    buyTradeInfo(tradeInfo, actualPrice, buyOrderInfo, sellOrderInfo, -1);
                    actualPrice = tradeInfo.getActualPrice();
                    iter.remove(); //成交从队列删除
                }
            }
            if (CollectionUtils.isEmpty(orderInfoList)) {
                buyMatchMap.remove(price);
            }
        }
        return actualPrice;
    }

    /**
     * 买单匹配
     */
    private static TradeInfo buyTradeInfo(TradeInfo tradeInfo, BigDecimal preLatestTradePrice, OrderInfo buyOrderInfo, OrderInfo sellOrderInfo, int type) {
        BigDecimal quantity = null;
        BigDecimal actualPrice = null;
        switch (type) {
            case 1:
                //卖单大于委买剩余量，成交量为委买剩余量
                quantity = buyOrderInfo.getOrderQuantity();
                actualPrice = buyOrderInfo.getOrderPrice();
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));

                //ConcurrentSkipListMap和子map：buyMatchMap 共享key,value
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));

                break;
            case 0:
                quantity = sellOrderInfo.getOrderQuantity();
                actualPrice = sellOrderInfo.getOrderPrice();
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));
                break;
            case -1:
                //卖单数量小于委买剩余量，成交量为卖单数量
                quantity = sellOrderInfo.getOrderQuantity();
                actualPrice = sellOrderInfo.getOrderPrice();
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));

                break;
        }

        if(actualPrice != null) {
            tradeInfo.setActualPrice(actualPrice);
        }

        if (tradeInfo.getQuantity() != null) {
            tradeInfo.setQuantity(tradeInfo.getQuantity().add(quantity));
        } else {
            tradeInfo.setQuantity(quantity);
        }

        return tradeInfo;
    }

    /**
     * 生成委卖成交队列
     */
    public static BigDecimal generateSellTradeInfoList(BigDecimal preLatestTradePrice, OrderInfo sellOrderInfo, ConcurrentNavigableMap<BigDecimal, List<OrderInfo>> sellMatchMap,
                                                       TradeInfo tradeInfo) {
        BigDecimal actualPrice = preLatestTradePrice;

        for (BigDecimal price : sellMatchMap.descendingKeySet()) { //倒序遍历map 价格从高到低
            List<OrderInfo> orderInfoList = sellMatchMap.get(price);
            Iterator<OrderInfo> iter = orderInfoList.iterator();
            while (iter.hasNext()) {
                BigDecimal sellOrderQuantity = sellOrderInfo.getOrderQuantity();
                OrderInfo buyOrderInfo = iter.next();
                BigDecimal buyOrderQuantity = buyOrderInfo.getOrderQuantity();
                if (buyOrderQuantity.compareTo(sellOrderQuantity) > 0) {
                    sellTradeInfo(tradeInfo, actualPrice, sellOrderInfo, buyOrderInfo, 1);
                    //type 1
                    return tradeInfo.getActualPrice();
                } else if (buyOrderQuantity.compareTo(sellOrderQuantity) == 0) {
                    sellTradeInfo(tradeInfo, actualPrice, sellOrderInfo, buyOrderInfo, 0);
                    iter.remove(); //成交从队列删除
                    if (CollectionUtils.isEmpty(orderInfoList)) {
                        sellMatchMap.remove(price);
                    }
                    return tradeInfo.getActualPrice();
                } else {
                    if (buyOrderQuantity.compareTo(ZERO_BIGDECIMAL) <= ZERO_INT) { //委托量<=0
                        System.out.println("buyOrderList orderQuantity:{} <= 0 ");
                        iter.remove(); //异常数据从队列删除
                        continue;
                    }
                    sellTradeInfo(tradeInfo, actualPrice, sellOrderInfo, buyOrderInfo, -1);
                    iter.remove(); //成交从队列删除
                    actualPrice = tradeInfo.getActualPrice();
                }
            }

            if (CollectionUtils.isEmpty(orderInfoList)) {
                sellMatchMap.remove(price);
            }
        }
        return actualPrice;
    }


    /**
     * 卖单匹配
     */
    private static TradeInfo sellTradeInfo(TradeInfo tradeInfo, BigDecimal preLatestTradePrice, OrderInfo sellOrderInfo, OrderInfo buyOrderInfo, int type) {
        BigDecimal quantity = null;
        BigDecimal actualPrice = null;
        switch (type) {
            case 1:
                //买单大于委卖剩余量，成交量为委卖剩余量
                quantity = sellOrderInfo.getOrderQuantity();
                actualPrice = sellOrderInfo.getOrderPrice();
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));

                //ConcurrentSkipListMap和子map：buyMatchMap 共享key,value
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));
                break;
            case 0:
                quantity = buyOrderInfo.getOrderQuantity();
                actualPrice = buyOrderInfo.getOrderPrice();
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));
                break;
            case -1:
                //买单数量小于委卖剩余量，成交量为买单数量
                quantity = buyOrderInfo.getOrderQuantity();
                actualPrice = buyOrderInfo.getOrderPrice();
                sellOrderInfo.setOrderQuantity(sellOrderInfo.getOrderQuantity().subtract(quantity));
                buyOrderInfo.setOrderQuantity(buyOrderInfo.getOrderQuantity().subtract(quantity));
                break;
        }
        if(actualPrice != null) {
            tradeInfo.setActualPrice(actualPrice);
        }
        if (tradeInfo.getQuantity() != null) {
            tradeInfo.setQuantity(tradeInfo.getQuantity().add(quantity));
        } else {
            tradeInfo.setQuantity(quantity);
        }

        return tradeInfo;
    }


    public static TradeInfo createTradeInfo(BigDecimal actualPrice, BigDecimal quantity) {
        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setActualPrice(actualPrice);
        tradeInfo.setQuantity(quantity);
        return tradeInfo;
    }
}
