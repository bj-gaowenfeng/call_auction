package com.test.demo.match.module;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *成交明细对象
 */
public class TradeInfo implements Serializable{


    private static final long serialVersionUID = 1324314650346982395L;

    private BigDecimal quantity; //成交数量

    private BigDecimal actualPrice; //成交价格

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }
}
