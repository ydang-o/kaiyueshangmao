package com.dingyangmall.mall.constant;

/**
 * 商城统一返回码（C端/管理端共用）
 *
 * @author dingyangmall
 */
public enum MallReturnCode {

    /** 业务不允许（如：仅未支付可取消、仅待收货可确认收货等） */
    ERR_70001("70001", "操作不允许"),

    /** 用户不存在 / 订单提交失败等 */
    ERR_70003("70003", "用户不存在"),

    /** 订单/数据不存在 */
    ERR_70005("70005", "订单不存在"),

    /** 密码错误 */
    ERR_70007("70007", "密码错误"),
    ;

    private final String code;
    private final String msg;

    MallReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
