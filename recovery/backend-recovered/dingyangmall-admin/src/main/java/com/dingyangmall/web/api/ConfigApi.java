/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import java.util.HashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/public/ma/config"})
public class ConfigApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ConfigApi.class);
    private final TbIntegralRuleService integralRuleService;

    @GetMapping(value={"/service"})
    public AjaxResult getServiceConfig() {
        HashMap<String, Integer> config = new HashMap<String, Integer>();
        TbIntegralRule rule = this.integralRuleService.list().stream().findFirst().orElse(null);
        if (rule != null) {
            config.put("signInPoints", rule.getSignIntegral());
            config.put("registerPoints", rule.getRegisterIntegral());
            config.put("invitePoints", rule.getRecommendIntegral());
            config.put("redPacketSwitch", rule.getRedPacketSwitch());
        } else {
            config.put("signInPoints", 0);
            config.put("registerPoints", 0);
            config.put("invitePoints", 0);
            config.put("redPacketSwitch", 0);
        }
        return AjaxResult.success(config);
    }

    @Generated
    public ConfigApi(TbIntegralRuleService integralRuleService) {
        this.integralRuleService = integralRuleService;
    }
}

