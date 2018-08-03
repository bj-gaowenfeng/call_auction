package com.test.demo.match.enums;

/**
 * 匹配结果消息枚举类
 */
public enum MatchResultMsgEnum {
    TRADED(1,"成交"),
    ADD_BUY_DEEP(2,"增加买深度"),
    ADD_SELL_DEEP(3,"增加卖深度"),
    ORDER_CANCEL_ALL(4,"全部撤单"),
    ORDER_CANCEL_PART(5,"部分撤单"),
    ORDER_CANCEL_FAIL(6,"撤单失败");

    private int code;
    private String msg;
    MatchResultMsgEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static MatchResultMsgEnum getMatchResultMsgEnum(int code){
        for(MatchResultMsgEnum matchResultMsgEnum : MatchResultMsgEnum.values()){
            if(matchResultMsgEnum.getCode() == code){
                return matchResultMsgEnum;
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
