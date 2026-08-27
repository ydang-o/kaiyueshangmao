/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.constant;

/*
 * Uses 'sealed' constructs - enablewith --sealed true
 */
public enum WxReturnCode {
    SUC_0("0", "\u8bf7\u6c42\u6210\u529f"){}
    ,
    ERR_1("-1", "\u7cfb\u7edf\u7e41\u5fd9\uff0c\u6b64\u65f6\u8bf7\u5f00\u53d1\u8005\u7a0d\u5019\u518d\u8bd5"){}
    ,
    ERR_10001("10001", "\u6d89\u5acc\u5e7f\u544a "){}
    ,
    ERR_20001("20001", "\u6d89\u5acc\u653f\u6cbb "){}
    ,
    ERR_20002("20002", "\u6d89\u5acc\u8272\u60c5 "){}
    ,
    ERR_20004("20004", "\u6d89\u5acc\u793e\u4f1a "){}
    ,
    ERR_20006("20006", "\u6d89\u5acc\u8fdd\u6cd5\u72af\u7f6a "){}
    ,
    ERR_20008("20008", "\u6d89\u5acc\u6b3a\u8bc8 "){}
    ,
    ERR_20013("20013", "\u6d89\u5acc\u7248\u6743 "){}
    ,
    ERR_21000("21000", "\u6d89\u5acc\u5176\u4ed6 "){}
    ,
    ERR_22000("22000", "\u6d89\u5acc\u4e92\u63a8(\u4e92\u76f8\u5ba3\u4f20) "){}
    ,
    ERR_30001("30001", "\u539f\u521b\u6821\u9a8c\u51fa\u73b0\u7cfb\u7edf\u9519\u8bef\u4e14\u7528\u6237\u9009\u62e9\u4e86\u88ab\u5224\u4e3a\u8f6c\u8f7d\u5c31\u4e0d\u7fa4\u53d1 "){}
    ,
    ERR_30002("30002", "\u539f\u521b\u6821\u9a8c\u88ab\u5224\u5b9a\u4e3a\u4e0d\u80fd\u7fa4\u53d1 "){}
    ,
    ERR_30003("30003", "\u539f\u521b\u6821\u9a8c\u88ab\u5224\u5b9a\u4e3a\u8f6c\u8f7d\u6587\u4e14\u7528\u6237\u9009\u62e9\u4e86\u88ab\u5224\u4e3a\u8f6c\u8f7d\u5c31\u4e0d\u7fa4\u53d1 "){}
    ,
    ERR_40001("40001", "\u83b7\u53d6access_token\u65f6AppSecret\u9519\u8bef\uff0c\u6216\u8005access_token\u65e0\u6548\u3002\u8bf7\u5f00\u53d1\u8005\u8ba4\u771f\u6bd4\u5bf9AppSecret\u7684\u6b63\u786e\u6027\uff0c\u6216\u67e5\u770b\u662f\u5426\u6b63\u5728\u4e3a\u6070\u5f53\u7684\u516c\u4f17\u53f7\u8c03\u7528\u63a5\u53e3"){}
    ,
    ERR_40002("40002", "\u4e0d\u5408\u6cd5\u7684\u51ed\u8bc1\u7c7b\u578b"){}
    ,
    ERR_40003("40003", "\u4e0d\u5408\u6cd5\u7684OpenID\uff0c\u8bf7\u5f00\u53d1\u8005\u786e\u8ba4OpenID\uff08\u8be5\u7528\u6237\uff09\u662f\u5426\u5df2\u5173\u6ce8\u516c\u4f17\u53f7\uff0c\u6216\u662f\u5426\u662f\u5176\u4ed6\u516c\u4f17\u53f7\u7684OpenID"){}
    ,
    ERR_40004("40004", "\u4e0d\u5408\u6cd5\u7684\u5a92\u4f53\u6587\u4ef6\u7c7b\u578b"){}
    ,
    ERR_40005("40005", "\u4e0d\u5408\u6cd5\u7684\u6587\u4ef6\u7c7b\u578b"){}
    ,
    ERR_40006("40006", "\u4e0d\u5408\u6cd5\u7684\u6587\u4ef6\u5927\u5c0f"){}
    ,
    ERR_40007("40007", "\u4e0d\u5408\u6cd5\u7684\u5a92\u4f53\u6587\u4ef6id"){}
    ,
    ERR_40008("40008", "\u4e0d\u5408\u6cd5\u7684\u6d88\u606f\u7c7b\u578b"){}
    ,
    ERR_40009("40009", "\u4e0d\u5408\u6cd5\u7684\u56fe\u7247\u6587\u4ef6\u5927\u5c0f"){}
    ,
    ERR_40010("40010", "\u4e0d\u5408\u6cd5\u7684\u8bed\u97f3\u6587\u4ef6\u5927\u5c0f"){}
    ,
    ERR_40011("40011", "\u4e0d\u5408\u6cd5\u7684\u89c6\u9891\u6587\u4ef6\u5927\u5c0f"){}
    ,
    ERR_40012("40012", "\u4e0d\u5408\u6cd5\u7684\u7f29\u7565\u56fe\u6587\u4ef6\u5927\u5c0f"){}
    ,
    ERR_40013("40013", "\u4e0d\u5408\u6cd5\u7684AppID\uff0c\u8bf7\u5f00\u53d1\u8005\u68c0\u67e5AppID\u7684\u6b63\u786e\u6027\uff0c\u907f\u514d\u5f02\u5e38\u5b57\u7b26\uff0c\u6ce8\u610f\u5927\u5c0f\u5199"){}
    ,
    ERR_40014("40014", "\u4e0d\u5408\u6cd5\u7684access_token\uff0c\u8bf7\u5f00\u53d1\u8005\u8ba4\u771f\u6bd4\u5bf9access_token\u7684\u6709\u6548\u6027\uff08\u5982\u662f\u5426\u8fc7\u671f\uff09\uff0c\u6216\u67e5\u770b\u662f\u5426\u6b63\u5728\u4e3a\u6070\u5f53\u7684\u516c\u4f17\u53f7\u8c03\u7528\u63a5\u53e3"){}
    ,
    ERR_40015("40015", "\u4e0d\u5408\u6cd5\u7684\u83dc\u5355\u7c7b\u578b"){}
    ,
    ERR_40016("40016", "\u4e0d\u5408\u6cd5\u7684\u6309\u94ae\u4e2a\u6570"){}
    ,
    ERR_40017("40017", "\u4e0d\u5408\u6cd5\u7684\u6309\u94ae\u4e2a\u6570"){}
    ,
    ERR_40018("40018", "\u4e0d\u5408\u6cd5\u7684\u6309\u94ae\u540d\u5b57\u957f\u5ea6"){}
    ,
    ERR_40019("40019", "\u4e0d\u5408\u6cd5\u7684\u6309\u94aeKEY\u957f\u5ea6"){}
    ,
    ERR_40020("40020", "\u4e0d\u5408\u6cd5\u7684\u6309\u94aeURL\u957f\u5ea6"){}
    ,
    ERR_40021("40021", "\u4e0d\u5408\u6cd5\u7684\u83dc\u5355\u7248\u672c\u53f7"){}
    ,
    ERR_40022("40022", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u7ea7\u6570"){}
    ,
    ERR_40023("40023", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u6309\u94ae\u4e2a\u6570"){}
    ,
    ERR_40024("40024", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u6309\u94ae\u7c7b\u578b"){}
    ,
    ERR_40025("40025", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u6309\u94ae\u540d\u5b57\u957f\u5ea6"){}
    ,
    ERR_40026("40026", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u6309\u94aeKEY\u957f\u5ea6"){}
    ,
    ERR_40027("40027", "\u4e0d\u5408\u6cd5\u7684\u5b50\u83dc\u5355\u6309\u94aeURL\u957f\u5ea6"){}
    ,
    ERR_40028("40028", "\u4e0d\u5408\u6cd5\u7684\u81ea\u5b9a\u4e49\u83dc\u5355\u4f7f\u7528\u7528\u6237"){}
    ,
    ERR_40029("40029", "\u4e0d\u5408\u6cd5\u7684oauth_code"){}
    ,
    ERR_40030("40030", "\u4e0d\u5408\u6cd5\u7684refresh_token"){}
    ,
    ERR_40031("40031", "\u4e0d\u5408\u6cd5\u7684openid\u5217\u8868"){}
    ,
    ERR_40032("40032", "\u4e0d\u5408\u6cd5\u7684openid\u5217\u8868\u4e2a\u6570"){}
    ,
    ERR_40033("40033", "\u4e0d\u5408\u6cd5\u7684\u8bf7\u6c42\u5b57\u7b26\uff0c\u4e0d\u80fd\u5305\u542bxxxx\u683c\u5f0f\u7684\u5b57\u7b26"){}
    ,
    ERR_40035("40035", "\u4e0d\u5408\u6cd5\u7684\u53c2\u6570"){}
    ,
    ERR_40055("40055", "\u4e0d\u5b8c\u6574\u7684url\uff0c\u524d\u9762\u8981\u52a0http://"){}
    ,
    ERR_40037("40037", "template_id\u4e0d\u6b63\u786e"){}
    ,
    ERR_40038("40038", "\u4e0d\u5408\u6cd5\u7684\u8bf7\u6c42\u683c\u5f0f"){}
    ,
    ERR_40039("40039", "\u4e0d\u5408\u6cd5\u7684URL\u957f\u5ea6"){}
    ,
    ERR_40050("40050", "\u4e0d\u5408\u6cd5\u7684\u5206\u7ec4id"){}
    ,
    ERR_40051("40051", "\u5206\u7ec4\u540d\u5b57\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40062("40062", "\u6807\u9898\u957f\u5ea6\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40097("40097", "\u53c2\u6570\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40113("40113", "\u6587\u4ef6\u540d\u79f0\u4e0d\u5408\u6cd5\uff0c\u9700\u5305\u542b\u6b63\u786e\u540e\u7f00"){}
    ,
    ERR_40117("40117", "\u5206\u7ec4\u540d\u5b57\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40118("40118", "media_id\u5927\u5c0f\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40119("40119", "button\u7c7b\u578b\u9519\u8bef"){}
    ,
    ERR_40120("40120", "button\u7c7b\u578b\u9519\u8bef"){}
    ,
    ERR_40121("40121", "\u4e0d\u5408\u6cd5\u7684media_id\u7c7b\u578b"){}
    ,
    ERR_40125("40125", "\u4e0d\u5408\u6cd5\u7684AppSecret\uff0c\u8bf7\u5f00\u53d1\u8005\u68c0\u67e5AppSecret\u7684\u6b63\u786e\u6027\uff0c\u907f\u514d\u5f02\u5e38\u5b57\u7b26\uff0c\u6ce8\u610f\u5927\u5c0f\u5199"){}
    ,
    ERR_40130("40130", "\u81f3\u5c11\u9700\u8981\u540c\u65f6\u53d1\u9001\u4e24\u4e2a\u7528\u6237"){}
    ,
    ERR_40132("40132", "\u5fae\u4fe1\u53f7\u4e0d\u5408\u6cd5"){}
    ,
    ERR_40137("40137", "\u4e0d\u652f\u6301\u7684\u56fe\u7247\u683c\u5f0f"){}
    ,
    ERR_40164("40164", "\u8c03\u7528\u63a5\u53e3\u7684IP\u5730\u5740\u4e0d\u5728\u767d\u540d\u5355\u4e2d\uff0c\u8bf7\u5728\u63a5\u53e3IP\u767d\u540d\u5355\u4e2d\u8fdb\u884c\u8bbe\u7f6e"){}
    ,
    ERR_41001("41001", "\u7f3a\u5c11access_token\u53c2\u6570"){}
    ,
    ERR_41002("41002", "\u7f3a\u5c11appid\u53c2\u6570"){}
    ,
    ERR_41003("41003", "\u7f3a\u5c11refresh_token\u53c2\u6570"){}
    ,
    ERR_41004("41004", "\u7f3a\u5c11secret\u53c2\u6570"){}
    ,
    ERR_41005("41005", "\u7f3a\u5c11\u591a\u5a92\u4f53\u6587\u4ef6\u6570\u636e"){}
    ,
    ERR_41006("41006", "\u7f3a\u5c11media_id\u53c2\u6570"){}
    ,
    ERR_41007("41007", "\u7f3a\u5c11\u5b50\u83dc\u5355\u6570\u636e"){}
    ,
    ERR_41008("41008", "\u7f3a\u5c11oauth code"){}
    ,
    ERR_41009("41009", "\u7f3a\u5c11openid"){}
    ,
    ERR_41028("41028", "form_id\u4e0d\u6b63\u786e\uff0c\u6216\u8005\u8fc7\u671f"){}
    ,
    ERR_41029("41029", "form_id\u5df2\u88ab\u4f7f\u7528"){}
    ,
    ERR_41030("41030", "page\u4e0d\u6b63\u786e"){}
    ,
    ERR_42001("42001", "access_token\u8d85\u65f6\uff0c\u8bf7\u68c0\u67e5access_token\u7684\u6709\u6548\u671f\uff0c\u8bf7\u53c2\u8003\u57fa\u7840\u652f\u6301-\u83b7\u53d6access_token\u4e2d\uff0c\u5bf9access_token\u7684\u8be6\u7ec6\u673a\u5236\u8bf4\u660e"){}
    ,
    ERR_42002("42002", "refresh_token\u8d85\u65f6"){}
    ,
    ERR_42003("42003", "oauth_code\u8d85\u65f6"){}
    ,
    ERR_43001("43001", "\u9700\u8981GET\u8bf7\u6c42"){}
    ,
    ERR_43002("43002", "\u9700\u8981POST\u8bf7\u6c42"){}
    ,
    ERR_43003("43003", "\u9700\u8981HTTPS\u8bf7\u6c42"){}
    ,
    ERR_43004("43004", "\u9700\u8981\u63a5\u6536\u8005\u5173\u6ce8"){}
    ,
    ERR_43005("43005", "\u9700\u8981\u597d\u53cb\u5173\u7cfb"){}
    ,
    ERR_44001("44001", "\u591a\u5a92\u4f53\u6587\u4ef6\u4e3a\u7a7a"){}
    ,
    ERR_44002("44002", "POST\u7684\u6570\u636e\u5305\u4e3a\u7a7a"){}
    ,
    ERR_44003("44003", "\u56fe\u6587\u6d88\u606f\u5185\u5bb9\u4e3a\u7a7a"){}
    ,
    ERR_44004("44004", "\u6587\u672c\u6d88\u606f\u5185\u5bb9\u4e3a\u7a7a"){}
    ,
    ERR_45001("45001", "\u591a\u5a92\u4f53\u6587\u4ef6\u5927\u5c0f\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45002("45002", "\u6d88\u606f\u5185\u5bb9\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45003("45003", "\u6807\u9898\u5b57\u6bb5\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45004("45004", "\u63cf\u8ff0\u5b57\u6bb5\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45005("45005", "\u94fe\u63a5\u5b57\u6bb5\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45006("45006", "\u56fe\u7247\u94fe\u63a5\u5b57\u6bb5\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45007("45007", "\u8bed\u97f3\u64ad\u653e\u65f6\u95f4\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45008("45008", "\u56fe\u6587\u6d88\u606f\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45009("45009", "\u63a5\u53e3\u8c03\u7528\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45010("45010", "\u521b\u5efa\u83dc\u5355\u4e2a\u6570\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45015("45015", "\u56de\u590d\u65f6\u95f4\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45016("45016", "\u7cfb\u7edf\u5206\u7ec4\uff0c\u4e0d\u5141\u8bb8\u4fee\u6539"){}
    ,
    ERR_45017("45017", "\u5206\u7ec4\u540d\u5b57\u8fc7\u957f"){}
    ,
    ERR_45018("45018", "\u5206\u7ec4\u6570\u91cf\u8d85\u8fc7\u4e0a\u9650"){}
    ,
    ERR_45028("45028", "\u6ca1\u6709\u7fa4\u53d1\u7684\u914d\u989d\uff0c\u914d\u989d\u5df2\u7ecf\u7528\u5b8c"){}
    ,
    ERR_45047("45047", "\u5ba2\u670d\u4e0b\u884c\u6d88\u606f\u8d85\u8fc7\u4e0a\u9650"){}
    ,
    ERR_45157("45157", "\u6807\u7b7e\u540d\u975e\u6cd5\uff0c\u8bf7\u6ce8\u610f\u4e0d\u80fd\u548c\u5176\u4ed6\u6807\u7b7e\u91cd\u540d"){}
    ,
    ERR_45158("45158", "\u6807\u7b7e\u540d\u957f\u5ea6\u8d85\u8fc730\u4e2a\u5b57\u8282"){}
    ,
    ERR_45056("45056", "\u521b\u5efa\u7684\u6807\u7b7e\u6570\u8fc7\u591a\uff0c\u8bf7\u6ce8\u610f\u4e0d\u80fd\u8d85\u8fc7100\u4e2a"){}
    ,
    ERR_45058("45058", "\u4e0d\u80fd\u4fee\u65390/1/2\u8fd9\u4e09\u4e2a\u7cfb\u7edf\u9ed8\u8ba4\u4fdd\u7559\u7684\u6807\u7b7e"){}
    ,
    ERR_45057("45057", "\u8be5\u6807\u7b7e\u4e0b\u7c89\u4e1d\u6570\u8d85\u8fc710w\uff0c\u4e0d\u5141\u8bb8\u76f4\u63a5\u5220\u9664"){}
    ,
    ERR_45059("45059", "\u6709\u7c89\u4e1d\u8eab\u4e0a\u7684\u6807\u7b7e\u6570\u5df2\u7ecf\u8d85\u8fc7\u9650\u5236"){}
    ,
    ERR_45159("45159", "\u975e\u6cd5\u7684tag_id"){}
    ,
    ERR_46001("46001", "\u4e0d\u5b58\u5728\u5a92\u4f53\u6570\u636e"){}
    ,
    ERR_46002("46002", "\u4e0d\u5b58\u5728\u7684\u83dc\u5355\u7248\u672c"){}
    ,
    ERR_46003("46003", "\u4e0d\u5b58\u5728\u7684\u83dc\u5355\u6570\u636e"){}
    ,
    ERR_46004("46004", "\u4e0d\u5b58\u5728\u7684\u7528\u6237"){}
    ,
    ERR_47001("47001", "\u89e3\u6790JSON/XML\u5185\u5bb9\u9519\u8bef"){}
    ,
    ERR_48001("48001", "api\u529f\u80fd\u672a\u6388\u6743\uff0c\u8bf7\u786e\u8ba4\u516c\u4f17\u53f7\u5df2\u83b7\u5f97\u8be5\u63a5\u53e3\uff0c\u53ef\u4ee5\u5728\u516c\u4f17\u5e73\u53f0\u5b98\u7f51-\u5f00\u53d1\u8005\u4e2d\u5fc3\u9875\u4e2d\u67e5\u770b\u63a5\u53e3\u6743\u9650"){}
    ,
    ERR_48002("48002", "\u7c89\u4e1d\u62d2\u6536\u6d88\u606f\uff08\u7c89\u4e1d\u5728\u516c\u4f17\u53f7\u9009\u9879\u4e2d\uff0c\u5173\u95ed\u4e86 \u201c \u63a5\u6536\u6d88\u606f \u201d \uff09"){}
    ,
    ERR_48004("48004", "api \u63a5\u53e3\u88ab\u5c01\u7981\uff0c\u8bf7\u767b\u5f55 admin.weixin.qq.com \u67e5\u770b\u8be6\u60c5"){}
    ,
    ERR_48005("48005", "api \u7981\u6b62\u5220\u9664\u88ab\u81ea\u52a8\u56de\u590d\u548c\u81ea\u5b9a\u4e49\u83dc\u5355\u5f15\u7528\u7684\u7d20\u6750"){}
    ,
    ERR_48006("48006", "api \u7981\u6b62\u6e05\u96f6\u8c03\u7528\u6b21\u6570\uff0c\u56e0\u4e3a\u6e05\u96f6\u6b21\u6570\u8fbe\u5230\u4e0a\u9650"){}
    ,
    ERR_48008("48008", "\u6ca1\u6709\u8be5\u7c7b\u578b\u6d88\u606f\u7684\u53d1\u9001\u6743\u9650"){}
    ,
    ERR_49003("49003", "\u4f20\u5165\u7684openid\u4e0d\u5c5e\u4e8e\u6b64AppID"){}
    ,
    ERR_50001("50001", "\u7528\u6237\u672a\u6388\u6743\u8be5api"){}
    ,
    ERR_50002("50002", "\u7528\u6237\u53d7\u9650\uff0c\u53ef\u80fd\u662f\u8fdd\u89c4\u540e\u63a5\u53e3\u88ab\u5c01\u7981"){}
    ,
    ERR_50005("50005", "\u7528\u6237\u672a\u5173\u6ce8\u516c\u4f17\u53f7"){}
    ,
    ERR_61003("61003", "\u8bf7\u786e\u8ba4\u662f\u5426\u53d6\u6d88\u6388\u6743\uff08\u7b2c\u4e09\u65b9\u5e73\u53f0\u6388\u6743\uff09"){}
    ,
    ERR_61004("61004", "\u5f53\u524dip\u672a\u5728\u767d\u540d\u5355\u4e2d\uff0c\u76f4\u63a5\u83b7\u53d6\u672c\u5730ip\u6dfb\u52a0"){}
    ,
    ERR_61005("61005", " \u7ec4\u4ef6 ticket\u5df2\u5931\u6548\uff0c\u91cd\u65b0\u63a5\u53d7\u6388\u6743url\u53cd\u9988\u7684ticket"){}
    ,
    ERR_61006("61006", "\u83b7\u53d6componentTicket\u4e3anull"){}
    ,
    ERR_61007("61007", "\u5f53\u524d\u516c\u4f17\u53f7\u6216\u8005\u5c0f\u7a0b\u5e8f\u5df2\u5728\u516c\u4f17\u5e73\u53f0\u89e3\u7ed1"){}
    ,
    ERR_61009("61009", "\u6388\u6743\u7801\u5931\u6548\uff0c\u91cd\u65b0\u6388\u6743"){}
    ,
    ERR_61451("61451", "\u53c2\u6570\u9519\u8bef(invalid parameter)"){}
    ,
    ERR_61452("61452", "\u65e0\u6548\u5ba2\u670d\u8d26\u53f7(invalid kf_account)"){}
    ,
    ERR_61453("61453", "\u5ba2\u670d\u5e10\u53f7\u5df2\u5b58\u5728(kf_account exsited)"){}
    ,
    ERR_61454("61454", "\u5ba2\u670d\u5e10\u53f7\u540d\u957f\u5ea6\u8d85\u8fc7\u9650\u5236(\u4ec5\u5141\u8bb810\u4e2a\u82f1\u6587\u5b57\u7b26\uff0c\u4e0d\u5305\u62ec@\u53ca@\u540e\u7684\u516c\u4f17\u53f7\u7684\u5fae\u4fe1\u53f7)(invalid kf_acount length)"){}
    ,
    ERR_61455("61455", "\u5ba2\u670d\u5e10\u53f7\u540d\u5305\u542b\u975e\u6cd5\u5b57\u7b26(\u4ec5\u5141\u8bb8\u82f1\u6587+\u6570\u5b57)(illegal character in kf_account)"){}
    ,
    ERR_61456("61456", "\u5ba2\u670d\u5e10\u53f7\u4e2a\u6570\u8d85\u8fc7\u9650\u5236(10\u4e2a\u5ba2\u670d\u8d26\u53f7)(kf_account count exceeded)"){}
    ,
    ERR_61457("61457", "\u65e0\u6548\u5934\u50cf\u6587\u4ef6\u7c7b\u578b(invalid file type)"){}
    ,
    ERR_61450("61450", "\u7cfb\u7edf\u9519\u8bef(system error)"){}
    ,
    ERR_61500("61500", "\u65e5\u671f\u683c\u5f0f\u9519\u8bef"){}
    ,
    ERR_61501("61501", "\u65e5\u671f\u8303\u56f4\u9519\u8bef"){}
    ,
    ERR_65400("65400", "API\u4e0d\u53ef\u7528\uff0c\u5373\u6ca1\u6709\u5f00\u901a/\u5347\u7ea7\u5230\u65b0\u7248\u5ba2\u670d\u529f\u80fd"){}
    ,
    ERR_65401("65401", "\u65e0\u6548\u5ba2\u670d\u5e10\u53f7"){}
    ,
    ERR_65403("65403", "\u5ba2\u670d\u6635\u79f0\u4e0d\u5408\u6cd5"){}
    ,
    ERR_65404("65404", "\u5ba2\u670d\u5e10\u53f7\u4e0d\u5408\u6cd5"){}
    ,
    ERR_65405("65405", "\u5e10\u53f7\u6570\u76ee\u5df2\u8fbe\u5230\u4e0a\u9650\uff0c\u4e0d\u80fd\u7ee7\u7eed\u6dfb\u52a0"){}
    ,
    ERR_65406("65406", "\u5df2\u7ecf\u5b58\u5728\u7684\u5ba2\u670d\u5e10\u53f7"){}
    ,
    ERR_65407("65407", "\u9080\u8bf7\u5bf9\u8c61\u5df2\u7ecf\u662f\u8be5\u516c\u4f17\u53f7\u5ba2\u670d"){}
    ,
    ERR_65408("65408", "\u672c\u516c\u4f17\u53f7\u5df2\u7ecf\u6709\u4e00\u4e2a\u9080\u8bf7\u7ed9\u8be5\u5fae\u4fe1"){}
    ,
    ERR_65409("65409", "\u65e0\u6548\u7684\u5fae\u4fe1\u53f7"){}
    ,
    ERR_65410("65410", "\u9080\u8bf7\u5bf9\u8c61\u7ed1\u5b9a\u516c\u4f17\u53f7\u5ba2\u670d\u6570\u8fbe\u5230\u4e0a\u9650\uff08\u76ee\u524d\u6bcf\u4e2a\u5fae\u4fe1\u53f7\u53ef\u4ee5\u7ed1\u5b9a5\u4e2a\u516c\u4f17\u53f7\u5ba2\u670d\u5e10\u53f7\uff09"){}
    ,
    ERR_65411("65411", "\u8be5\u5e10\u53f7\u5df2\u7ecf\u6709\u4e00\u4e2a\u7b49\u5f85\u786e\u8ba4\u7684\u9080\u8bf7\uff0c\u4e0d\u80fd\u91cd\u590d\u9080\u8bf7"){}
    ,
    ERR_65412("65412", "\u8be5\u5e10\u53f7\u5df2\u7ecf\u7ed1\u5b9a\u5fae\u4fe1\u53f7\uff0c\u4e0d\u80fd\u8fdb\u884c\u9080\u8bf7"){}
    ,
    ERR_99999("99999", "\u65e0\u6cd5\u83b7\u53d6\u5230\u6587\u4ef6\u540d"){}
    ,
    ERR_9001001("9001001", "POST\u6570\u636e\u53c2\u6570\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001002("9001002", "\u8fdc\u7aef\u670d\u52a1\u4e0d\u53ef\u7528"){}
    ,
    ERR_9001003("9001003", "Ticket\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001004("9001004", "\u83b7\u53d6\u6447\u5468\u8fb9\u7528\u6237\u4fe1\u606f\u5931\u8d25"){}
    ,
    ERR_9001005("9001005", "\u83b7\u53d6\u5546\u6237\u4fe1\u606f\u5931\u8d25"){}
    ,
    ERR_9001006("9001006", "\u83b7\u53d6OpenID\u5931\u8d25"){}
    ,
    ERR_9001007("9001007", "\u4e0a\u4f20\u6587\u4ef6\u7f3a\u5931"){}
    ,
    ERR_9001008("9001008", "\u4e0a\u4f20\u7d20\u6750\u7684\u6587\u4ef6\u7c7b\u578b\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001009("9001009", "\u4e0a\u4f20\u7d20\u6750\u7684\u6587\u4ef6\u5c3a\u5bf8\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001010("9001010", "\u4e0a\u4f20\u5931\u8d25"){}
    ,
    ERR_9001020("9001020", "\u5e10\u53f7\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001021("9001021", "\u5df2\u6709\u8bbe\u5907\u6fc0\u6d3b\u7387\u4f4e\u4e8e50%\uff0c\u4e0d\u80fd\u65b0\u589e\u8bbe\u5907"){}
    ,
    ERR_9001022("9001022", "\u8bbe\u5907\u7533\u8bf7\u6570\u4e0d\u5408\u6cd5\uff0c\u5fc5\u987b\u4e3a\u5927\u4e8e0\u7684\u6570\u5b57"){}
    ,
    ERR_9001023("9001023", "\u5df2\u5b58\u5728\u5ba1\u6838\u4e2d\u7684\u8bbe\u5907ID\u7533\u8bf7"){}
    ,
    ERR_9001024("9001024", "\u4e00\u6b21\u67e5\u8be2\u8bbe\u5907ID\u6570\u91cf\u4e0d\u80fd\u8d85\u8fc750"){}
    ,
    ERR_9001025("9001025", "\u8bbe\u5907ID\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001026("9001026", "\u9875\u9762ID\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001027("9001027", "\u9875\u9762\u53c2\u6570\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001028("9001028", "\u4e00\u6b21\u5220\u9664\u9875\u9762ID\u6570\u91cf\u4e0d\u80fd\u8d85\u8fc710"){}
    ,
    ERR_9001029("9001029", "\u9875\u9762\u5df2\u5e94\u7528\u5728\u8bbe\u5907\u4e2d\uff0c\u8bf7\u5148\u89e3\u9664\u5e94\u7528\u5173\u7cfb\u518d\u5220\u9664"){}
    ,
    ERR_9001030("9001030", "\u4e00\u6b21\u67e5\u8be2\u9875\u9762ID\u6570\u91cf\u4e0d\u80fd\u8d85\u8fc750"){}
    ,
    ERR_9001031("9001031", "\u65f6\u95f4\u533a\u95f4\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001032("9001032", "\u4fdd\u5b58\u8bbe\u5907\u4e0e\u9875\u9762\u7684\u7ed1\u5b9a\u5173\u7cfb\u53c2\u6570\u9519\u8bef"){}
    ,
    ERR_9001033("9001033", "\u95e8\u5e97ID\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001034("9001034", "\u8bbe\u5907\u5907\u6ce8\u4fe1\u606f\u8fc7\u957f"){}
    ,
    ERR_9001035("9001035", "\u8bbe\u5907\u7533\u8bf7\u53c2\u6570\u4e0d\u5408\u6cd5"){}
    ,
    ERR_9001036("9001036", "\u67e5\u8be2\u8d77\u59cb\u503cbegin\u4e0d\u5408\u6cd5"){};

    private String code;
    private String msg;

    private WxReturnCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsg(String code) {
        try {
            return WxReturnCode.valueOf(code).getMsg();
        }
        catch (IllegalArgumentException e) {
            return "\u672a\u5b9a\u4e49\u7684\u8fd4\u56de\u7801\uff1a" + code;
        }
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "WxReturnCode{code='" + this.code + "'msg='" + this.msg + "'}";
    }
}

