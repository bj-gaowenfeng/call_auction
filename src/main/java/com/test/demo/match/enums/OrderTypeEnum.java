package com.test.demo.match.enums;

/**
 * 市价或限价枚举类
 */
public enum OrderTypeEnum {
    LIMIT_PRICE(1,"限价交易"),
    MARKET_PRICE(2,"市价交易");

    private int code;
    private String msg;
    OrderTypeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static OrderTypeEnum getOrderTypeEnum(int code){
        for(OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()){
            if(orderTypeEnum.getCode() == code){
                return orderTypeEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
