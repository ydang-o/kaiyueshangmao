package com.dingyangmall.weixin.constant;

/**
 * Shared application error codes used by mobile-facing APIs.
 */
public enum MyReturnCode {
    ERR_70001("70001", "操作不允许"),
    ERR_70003("70003", "用户不存在"),
    ERR_70004("70004", "订单状态不允许支付"),
    ERR_70005("70005", "订单不存在"),
    ERR_80004("80004", "商品不存在");

    private final String code;
    private final String msg;

    MyReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() { return code; }
    public String getMsg() { return msg; }
}
