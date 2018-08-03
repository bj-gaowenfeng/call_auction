package com.test.demo.match.enums;

/**
 * 委托方向枚举类
 */
public enum OrderDirectionEnum {
    ORDER_BUY(1,"委买"),
    ORDER_SELL(2,"委卖"),
    ORDER_Cancel(3,"撤单");

    private int code;
    private String msg;
    OrderDirectionEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static OrderDirectionEnum getOrderDirectionEnum(int code){
        for(OrderDirectionEnum orderDirectionEnum : OrderDirectionEnum.values()){
            if(orderDirectionEnum.getCode() == code){
                return orderDirectionEnum;
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
