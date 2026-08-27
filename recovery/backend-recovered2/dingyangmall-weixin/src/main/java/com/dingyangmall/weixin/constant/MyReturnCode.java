/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.constant;

/*
 * Uses 'sealed' constructs - enablewith --sealed true
 */
public enum MyReturnCode {
    ERR_60000(60000, "\u7cfb\u7edf\u9519\u8bef\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5"){}
    ,
    ERR_60001(60001, "\u767b\u5f55\u8d85\u65f6\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55"){}
    ,
    ERR_60002(60002, "session\u4e0d\u80fd\u4e3a\u7a7a"){}
    ,
    ERR_70001(70001, "\u8be5\u72b6\u6001\u8ba2\u5355\u4e0d\u5141\u8bb8\u64cd\u4f5c"){}
    ,
    ERR_70002(70002, "\u8bf7\u9009\u62e9\u4ed8\u6b3e\u65b9\u5f0f"){}
    ,
    ERR_70003(70003, "\u6ca1\u6709\u7b26\u5408\u4e0b\u5355\u6761\u4ef6\u7684\u89c4\u683c\u5546\u54c1\uff0c\u5546\u54c1\u5df2\u4e0b\u67b6\u6216\u5e93\u5b58\u4e0d\u8db3"){}
    ,
    ERR_70004(70004, "\u53ea\u6709\u672a\u652f\u4ed8\u7684\u8be6\u5355\u80fd\u53d1\u8d77\u652f\u4ed8"){}
    ,
    ERR_70005(70005, "\u65e0\u6548\u8ba2\u5355"){}
    ,
    ERR_80004(80004, "\u8be5\u5546\u54c1\u5df2\u5220\u9664"){};

    private int code;
    private String msg;

    private MyReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "MyReturnCode{code='" + this.code + "'msg='" + this.msg + "'}";
    }
}

