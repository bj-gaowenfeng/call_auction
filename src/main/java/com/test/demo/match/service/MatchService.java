package com.test.demo.match.service;


import com.test.demo.match.constants.CallAuctionConstants;
import com.test.demo.match.enums.OrderDirectionEnum;
import com.test.demo.match.module.OrderInfo;
import com.test.demo.match.module.TradeInfo;
import com.test.demo.match.util.MatchUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.test.demo.match.constants.CallAuctionConstants.ZERO_BIGDECIMAL;
import static com.test.demo.match.constants.CallAuctionConstants.ZERO_INT;


/**
 * 交易撮合类
 */

public class MatchService {

    //买队列
    private ConcurrentSkipListMap<BigDecimal, List<OrderInfo>>
            latestBuyOrderListMap = new ConcurrentSkipListMap<BigDecimal, List<OrderInfo>>();

    //卖队列
    private ConcurrentSkipListMap<BigDecimal, List<OrderInfo>>
            latestSellOrderListMap = new ConcurrentSkipListMap<BigDecimal, List<OrderInfo>>();

    //最新成交价
    private BigDecimal latestTradePrice;

    /**
     * 撮合匹配
     * orderInfoList
     */
    public TradeInfo matchOrderInfoList(List<OrderInfo> orderInfoList) {
         if(CollectionUtils.isEmpty(orderInfoList)){
             System.out.println("orderInfoList is null.");
             return MatchUtils.createTradeInfo(null,BigDecimal.ZERO);
         }
         TradeInfo tradeInfo = new TradeInfo();
         tradeInfo.setQuantity(BigDecimal.ZERO);
         for(OrderInfo orderReqParam:orderInfoList){
             matchOrderReqParam(orderReqParam,tradeInfo);
         }
         return tradeInfo;
    }

    private void matchOrderReqParam(OrderInfo orderReqParam,TradeInfo tradeInfo){
        try {

            System.out.println("match new orderInfo Data start..");

            match(orderReqParam,tradeInfo);

        } catch (Throwable ta) {
            ta.printStackTrace();
        }
    }

    /**
     * 交易撮合
     */
    private void match(OrderInfo orderReqParam,TradeInfo tradeInfo) throws Throwable {
        OrderDirectionEnum orderDirectionEnum = OrderDirectionEnum.getOrderDirectionEnum(orderReqParam.getOrderDirection());
        switch (orderDirectionEnum) {
            case ORDER_BUY:
                matchBuyOrderList(orderReqParam,tradeInfo);
                break;
            case ORDER_SELL:
                matchSellOrderList(orderReqParam,tradeInfo);
                break;
        }
    }

    //买单
    private void matchBuyOrderList(OrderInfo orderInfo,TradeInfo tradeInfo) throws Throwable {
        BigDecimal orderPrice = orderInfo.getOrderPrice();

        long startTimeMill = System.currentTimeMillis();
        //获取小于等于指定价格以下的map集合
        Map<BigDecimal, List<OrderInfo>> buyMatchMap = latestSellOrderListMap.headMap(orderPrice, true);


        // 没有匹配卖单价格 直接加入委买队列
        if (MapUtils.isEmpty(buyMatchMap)) {
            List<OrderInfo> orderInfoList = latestBuyOrderListMap.get(orderPrice);
            if (orderInfoList == null) {
                orderInfoList = new ArrayList<OrderInfo>();
                latestBuyOrderListMap.put(orderPrice, orderInfoList);
            }
            orderInfoList.add(orderInfo);
            return;
        }

        // 匹配卖单价格,生成成交明细及最新成交价
        BigDecimal latestTradePriceResult = MatchUtils.generateBuyTradeInfoList(latestTradePrice, orderInfo, buyMatchMap,tradeInfo);
        if (latestTradePriceResult != null) { //产生匹配最新成交价
            latestTradePrice = latestTradePriceResult;
        }

        // 买单匹配后有剩余委托量，加入买单队列
        if (orderInfo.getOrderQuantity().compareTo(ZERO_BIGDECIMAL) > ZERO_INT) {
            BigDecimal buyOrderPrice = orderInfo.getOrderPrice();
            List<OrderInfo> buyOrderInfoList = latestBuyOrderListMap.get(buyOrderPrice);
            if (buyOrderInfoList == null) {
                buyOrderInfoList = new ArrayList<OrderInfo>();
                latestBuyOrderListMap.put(buyOrderPrice, buyOrderInfoList);
            }
            buyOrderInfoList.add(orderInfo);
        }

    }

    //卖单
    private void matchSellOrderList(OrderInfo orderInfo,TradeInfo tradeInfo) throws Throwable {
        BigDecimal orderPrice = orderInfo.getOrderPrice();
        //获取大于等于指定价格以上的map集合
        long startTimeMill = System.currentTimeMillis();
        ConcurrentNavigableMap<BigDecimal, List<OrderInfo>> sellMatchMap = latestBuyOrderListMap.tailMap(orderPrice, true);

        //1. 没有匹配买单价格 直接加入委卖队列
        if (MapUtils.isEmpty(sellMatchMap)) {
            List<OrderInfo> orderInfoList = latestSellOrderListMap.get(orderPrice);
            if (orderInfoList == null) {
                orderInfoList = new ArrayList<OrderInfo>();
                latestSellOrderListMap.put(orderPrice, orderInfoList);
            }
            orderInfoList.add(orderInfo);
            return;
        }

        // 匹配买单价格,生成成交明细及最新成交价
        BigDecimal latestTradePriceResult = MatchUtils.generateSellTradeInfoList(latestTradePrice, orderInfo, sellMatchMap, tradeInfo);
        if (latestTradePriceResult != null) { //产生匹配最新成交价
            latestTradePrice = latestTradePriceResult;
        }

        // 卖单匹配后有剩余委托量，加入卖单队列
        if (orderInfo.getOrderQuantity().compareTo(ZERO_BIGDECIMAL) > CallAuctionConstants.ZERO_INT) {
            BigDecimal sellOrderPrice = orderInfo.getOrderPrice();
            List<OrderInfo> sellOrderInfoList = latestSellOrderListMap.get(sellOrderPrice);
            if (sellOrderInfoList == null) {
                sellOrderInfoList = new ArrayList<OrderInfo>();
                latestSellOrderListMap.put(sellOrderPrice, sellOrderInfoList);
            }
            sellOrderInfoList.add(orderInfo);
        }
    }
}
