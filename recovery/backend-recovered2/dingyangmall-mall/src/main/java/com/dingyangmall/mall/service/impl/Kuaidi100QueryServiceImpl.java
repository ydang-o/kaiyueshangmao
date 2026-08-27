/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dingyangmall.mall.config.MallConfigProperties;
import com.dingyangmall.mall.dto.ExpressTrackResult;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.enums.OrderLogisticsEnum;
import com.dingyangmall.mall.service.Kuaidi100QueryService;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Kuaidi100QueryServiceImpl
implements Kuaidi100QueryService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(Kuaidi100QueryServiceImpl.class);
    private static final String QUERY_URL = "https://poll.kuaidi100.com/poll/query.do";
    private final MallConfigProperties mallConfigProperties;

    @Override
    public ExpressTrackResult query(OrderLogistics orderLogistics) {
        ExpressTrackResult result = new ExpressTrackResult();
        result.setCom(orderLogistics.getLogistics());
        result.setNum(orderLogistics.getLogisticsNo());
        result.setState(orderLogistics.getStatus());
        result.setStateDesc(orderLogistics.getStatusDesc());
        result.setIscheck(orderLogistics.getIsCheck());
        result.setMessage(orderLogistics.getMessage());
        if (StrUtil.isBlank(orderLogistics.getLogistics()) || StrUtil.isBlank(orderLogistics.getLogisticsNo())) {
            result.setData(new ArrayList<ExpressTrackResult.TrackItem>());
            return result;
        }
        String key = this.mallConfigProperties.getKuaidi100Key();
        String customer = this.mallConfigProperties.getKuaidi100Customer();
        if (StrUtil.isBlank(key) || StrUtil.isBlank(customer)) {
            log.debug("\u672a\u914d\u7f6e\u5feb\u9012100 key/customer\uff0c\u4ec5\u8fd4\u56de\u672c\u5730\u7269\u6d41\u4fe1\u606f");
            result.setData(new ArrayList<ExpressTrackResult.TrackItem>());
            return result;
        }
        try {
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("com", orderLogistics.getLogistics().toLowerCase());
            paramMap.put("num", orderLogistics.getLogisticsNo());
            paramMap.put("resultv2", "4");
            if (StrUtil.isNotBlank(orderLogistics.getTelNum()) && orderLogistics.getTelNum().length() >= 4) {
                String phone = orderLogistics.getTelNum();
                paramMap.put("phone", phone.substring(phone.length() - 4));
            }
            String paramStr = JSONUtil.toJsonStr(paramMap);
            String sign = DigestUtil.md5Hex(paramStr + key + customer).toUpperCase();
            HashMap<String, Object> form = new HashMap<String, Object>();
            form.put("customer", customer);
            form.put("sign", sign);
            form.put("param", paramStr);
            String respBody = ((HttpRequest)HttpUtil.createPost(QUERY_URL).form(form).charset(StandardCharsets.UTF_8)).timeout(10000).execute().body();
            JSONObject json = JSONUtil.parseObj(respBody);
            result.setState(json.getStr("state"));
            result.setStateDesc(this.stateDesc(json.getStr("state")));
            result.setIscheck(json.getStr("ischeck", "0"));
            result.setMessage(json.getStr("message"));
            JSONArray data = json.getJSONArray("data");
            ArrayList<ExpressTrackResult.TrackItem> list = new ArrayList<ExpressTrackResult.TrackItem>();
            if (data != null && !data.isEmpty()) {
                for (int i = 0; i < data.size(); ++i) {
                    JSONObject item = data.getJSONObject(i);
                    ExpressTrackResult.TrackItem ti = new ExpressTrackResult.TrackItem();
                    ti.setTime(item.getStr("time"));
                    ti.setContext(item.getStr("context"));
                    ti.setLocation(item.getStr("location"));
                    list.add(ti);
                }
            }
            result.setData(list);
        }
        catch (Exception e) {
            log.warn("\u5feb\u9012100\u67e5\u8be2\u5f02\u5e38 com={} num={}", orderLogistics.getLogistics(), orderLogistics.getLogisticsNo(), e);
            result.setMessage(e.getMessage() != null ? e.getMessage() : "\u67e5\u8be2\u5f02\u5e38");
            result.setData(new ArrayList<ExpressTrackResult.TrackItem>());
        }
        return result;
    }

    private String stateDesc(String state) {
        if (state == null) {
            return "";
        }
        try {
            OrderLogisticsEnum e = OrderLogisticsEnum.valueOf(OrderLogisticsEnum.STATUS_PREFIX + "_" + state);
            return e != null ? e.getDesc() : state;
        }
        catch (Exception e) {
            return state;
        }
    }

    @Generated
    public Kuaidi100QueryServiceImpl(MallConfigProperties mallConfigProperties) {
        this.mallConfigProperties = mallConfigProperties;
    }
}

