package com.test.demo.match;


import com.test.demo.match.enums.OrderDirectionEnum;
import com.test.demo.match.module.OrderInfo;
import com.test.demo.match.module.TradeInfo;
import com.test.demo.match.service.MatchService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CallAuctionApplication {

	public static void main(String[] args) {


		//1. 有 1 个数量为 10， 预期价格为 10 买单，和 1 个价格为 10， 数量为 10 的卖单，最后应该输出可成交数量为 10， 价格为 10 。
		MatchService matchService1 = new MatchService();
		List<OrderInfo> orderInfoList1 = new ArrayList<OrderInfo>();
		OrderInfo  buyOrderInfo1 = new OrderInfo();
		buyOrderInfo1.setOrderDirection(OrderDirectionEnum.ORDER_BUY.getCode());
		buyOrderInfo1.setOrderPrice(new BigDecimal("10"));
		buyOrderInfo1.setOrderQuantity(new BigDecimal("10"));
		orderInfoList1.add(buyOrderInfo1);

		OrderInfo  sellOrderInfo1 = new OrderInfo();
		sellOrderInfo1.setOrderDirection(OrderDirectionEnum.ORDER_SELL.getCode());
		sellOrderInfo1.setOrderPrice(new BigDecimal("10"));
		sellOrderInfo1.setOrderQuantity(new BigDecimal("10"));
		orderInfoList1.add(sellOrderInfo1);

		TradeInfo tradeInfo1 = matchService1.matchOrderInfoList(orderInfoList1);
		System.out.println("price1:"+tradeInfo1.getActualPrice() + ",quantity1:"+tradeInfo1.getQuantity());

		//2. 有 1 个数量为 10， 预期价格为 20 买单，和 1 个价格为 10， 数量为 10 的卖单，最后应该输出可成交数量为 10， 价格 。
		MatchService matchService2 = new MatchService();
		List<OrderInfo> orderInfoList2 = new ArrayList<OrderInfo>();

		OrderInfo  sellOrderInfo2 = new OrderInfo();
		sellOrderInfo2.setOrderDirection(OrderDirectionEnum.ORDER_SELL.getCode());
		sellOrderInfo2.setOrderPrice(new BigDecimal("10"));
		sellOrderInfo2.setOrderQuantity(new BigDecimal("10"));
		orderInfoList2.add(sellOrderInfo2);

		OrderInfo  buyOrderInfo2 = new OrderInfo();
		buyOrderInfo2.setOrderDirection(OrderDirectionEnum.ORDER_BUY.getCode());
		buyOrderInfo2.setOrderPrice(new BigDecimal("20"));
		buyOrderInfo2.setOrderQuantity(new BigDecimal("10"));
		orderInfoList2.add(buyOrderInfo2);

		TradeInfo tradeInfo2 = matchService2.matchOrderInfoList(orderInfoList2);
		System.out.println("price2:"+tradeInfo2.getActualPrice() + ",quantity2:"+tradeInfo2.getQuantity());

		//3. 有 1 个数量为 10， 预期价格为 10 买单，和 1 个价格为 20， 数量为 10 的卖单，最后应该输出可成交数量为 0 。
		MatchService matchService3 = new MatchService();
		List<OrderInfo> orderInfoList3 = new ArrayList<OrderInfo>();
		OrderInfo  buyOrderInfo3 = new OrderInfo();
		buyOrderInfo3.setOrderDirection(OrderDirectionEnum.ORDER_BUY.getCode());
		buyOrderInfo3.setOrderPrice(new BigDecimal("10"));
		buyOrderInfo3.setOrderQuantity(new BigDecimal("10"));
		orderInfoList3.add(buyOrderInfo3);

		OrderInfo  sellOrderInfo3 = new OrderInfo();
		sellOrderInfo3.setOrderDirection(OrderDirectionEnum.ORDER_SELL.getCode());
		sellOrderInfo3.setOrderPrice(new BigDecimal("20"));
		sellOrderInfo3.setOrderQuantity(new BigDecimal("10"));
		orderInfoList3.add(sellOrderInfo3);

		TradeInfo tradeInfo3 = matchService3.matchOrderInfoList(orderInfoList3);
		System.out.println("price3:"+tradeInfo3.getActualPrice() + ",quantity3:"+tradeInfo3.getQuantity());

		//4. 有 1 个数量为 5， 预期价格为 10 买单，和 1 个价格为 10， 数量为 10 的卖单，最后应该输出可成交数量为 5  。
		MatchService matchService4 = new MatchService();
		List<OrderInfo> orderInfoList4 = new ArrayList<OrderInfo>();
		OrderInfo  buyOrderInfo4 = new OrderInfo();
		buyOrderInfo4.setOrderDirection(OrderDirectionEnum.ORDER_BUY.getCode());
		buyOrderInfo4.setOrderPrice(new BigDecimal("10"));
		buyOrderInfo4.setOrderQuantity(new BigDecimal("5"));
		orderInfoList4.add(buyOrderInfo4);

		OrderInfo  sellOrderInfo4 = new OrderInfo();
		sellOrderInfo4.setOrderDirection(OrderDirectionEnum.ORDER_SELL.getCode());
		sellOrderInfo4.setOrderPrice(new BigDecimal("10"));
		sellOrderInfo4.setOrderQuantity(new BigDecimal("10"));
		orderInfoList4.add(sellOrderInfo4);

		TradeInfo tradeInfo4 = matchService4.matchOrderInfoList(orderInfoList4);
		System.out.println("price4:"+tradeInfo4.getActualPrice() + ",quantity4:"+tradeInfo4.getQuantity());

	}
}
