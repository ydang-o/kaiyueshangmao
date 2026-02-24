package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dingyangmall.mall.config.MallConfigProperties;
import com.dingyangmall.mall.dto.ExpressTrackResult;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.enums.OrderLogisticsEnum;
import com.dingyangmall.mall.service.Kuaidi100QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快递100（Api100）实时查询实现
 * 文档：https://api.kuaidi100.com/document/5f0ffb5ebc8da837cbd8aefc.html
 *
 * @author dingyangmall
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Kuaidi100QueryServiceImpl implements Kuaidi100QueryService {

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
            result.setData(new ArrayList<>());
            return result;
        }

        String key = mallConfigProperties.getKuaidi100Key();
        String customer = mallConfigProperties.getKuaidi100Customer();
        if (StrUtil.isBlank(key) || StrUtil.isBlank(customer)) {
            log.debug("未配置快递100 key/customer，仅返回本地物流信息");
            result.setData(new ArrayList<>());
            return result;
        }

        try {
            // param JSON: com, num, phone(顺丰等可选)
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("com", orderLogistics.getLogistics().toLowerCase());
            paramMap.put("num", orderLogistics.getLogisticsNo());
            paramMap.put("resultv2", "4");
            if (StrUtil.isNotBlank(orderLogistics.getTelNum()) && orderLogistics.getTelNum().length() >= 4) {
                // 顺丰需要收件人手机后4位
                String phone = orderLogistics.getTelNum();
                paramMap.put("phone", phone.substring(phone.length() - 4));
            }
            String paramStr = JSONUtil.toJsonStr(paramMap);
            String sign = DigestUtil.md5Hex(paramStr + key + customer).toUpperCase();

            Map<String, Object> form = new HashMap<>();
            form.put("customer", customer);
            form.put("sign", sign);
            form.put("param", paramStr);

            String respBody = HttpUtil.createPost(QUERY_URL)
                    .form(form)
                    .charset(StandardCharsets.UTF_8)
                    .timeout(10000)
                    .execute()
                    .body();

            JSONObject json = JSONUtil.parseObj(respBody);
            // 快递100 成功时返回 state、data 等；失败时通常有 message
            result.setState(json.getStr("state"));
            result.setStateDesc(stateDesc(json.getStr("state")));
            result.setIscheck(json.getStr("ischeck", "0"));
            result.setMessage(json.getStr("message"));
            JSONArray data = json.getJSONArray("data");
            List<ExpressTrackResult.TrackItem> list = new ArrayList<>();
            if (data != null && !data.isEmpty()) {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    ExpressTrackResult.TrackItem ti = new ExpressTrackResult.TrackItem();
                    ti.setTime(item.getStr("time"));
                    ti.setContext(item.getStr("context"));
                    ti.setLocation(item.getStr("location"));
                    list.add(ti);
                }
            }
            result.setData(list);
        } catch (Exception e) {
            log.warn("快递100查询异常 com={} num={}", orderLogistics.getLogistics(), orderLogistics.getLogisticsNo(), e);
            result.setMessage(e.getMessage() != null ? e.getMessage() : "查询异常");
            result.setData(new ArrayList<>());
        }

        return result;
    }

    private String stateDesc(String state) {
        if (state == null) return "";
        try {
            OrderLogisticsEnum e = OrderLogisticsEnum.valueOf(OrderLogisticsEnum.STATUS_PREFIX + "_" + state);
            return e != null ? e.getDesc() : state;
        } catch (Exception e) {
            return state;
        }
    }
}
