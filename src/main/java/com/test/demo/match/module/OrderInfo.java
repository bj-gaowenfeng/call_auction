package com.test.demo.match.module;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 委托下单请求参数
 */
public class OrderInfo implements Serializable{

    private Integer orderDirection;	 //委托类型 1:买入 2:卖出
    private BigDecimal orderQuantity; //委托数量
    private BigDecimal orderPrice; //委托价格

    public Integer getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(Integer orderDirection) {
        this.orderDirection = orderDirection;
    }

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(BigDecimal orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
