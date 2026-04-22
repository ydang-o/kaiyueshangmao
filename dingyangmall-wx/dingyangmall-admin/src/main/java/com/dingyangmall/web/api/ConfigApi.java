package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置 API
 *
 * @author dingyangmall
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/public/ma/config")
public class ConfigApi {

    private final TbIntegralRuleService integralRuleService;

    /**
     * 获取服务配置
     */
    @GetMapping("/service")
    public AjaxResult getServiceConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // 获取积分规则配置
        TbIntegralRule rule = integralRuleService.list().stream().findFirst().orElse(null);
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
}
