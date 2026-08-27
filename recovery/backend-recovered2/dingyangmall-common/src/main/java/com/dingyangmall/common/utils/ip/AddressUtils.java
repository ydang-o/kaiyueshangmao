/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.utils.ip;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.http.HttpUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        if (IpUtils.internalIp(ip)) {
            return "\u5185\u7f51IP";
        }
        if (DingyangmallConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("\u83b7\u53d6\u5730\u7406\u4f4d\u7f6e\u5f02\u5e38 {}", (Object)ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSON.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e) {
                log.error("\u83b7\u53d6\u5730\u7406\u4f4d\u7f6e\u5f02\u5e38 {}", (Object)ip);
            }
        }
        return UNKNOWN;
    }
}

