package com.test.demo.match.enums;

/**
 * 委托状态枚举类
 */
public enum OrderStatusEnum {
    INIT(0,"初始化"),
    TRADED(1,"已成交"),
    CANCELED(2,"已撤单"),
    PART_CANCEL(3,"部分撤单");
    private int code;
    private String msg;
    OrderStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static OrderStatusEnum getOrderStatusEnum(int code){
        for(OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()){
            if(orderStatusEnum.getCode() == code){
                return orderStatusEnum;
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
