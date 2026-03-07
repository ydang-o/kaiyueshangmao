package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.TbIntegralRuleMapper;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 积分规则 服务实现类
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@Service
public class TbIntegralRuleServiceImpl extends ServiceImpl<TbIntegralRuleMapper, TbIntegralRule> implements TbIntegralRuleService {

    @Autowired
    private UmsMemberService umsMemberService;

    @Autowired
    private TbIntegralFlowService integralFlowService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeRegisterPoints(Long userId) {
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRegisterIntegral() == null || rule.getRegisterIntegral() <= 0) {
            return;
        }

        UmsMember member = umsMemberService.getById(userId);
        if (member != null) {
            Integer currentPoints = member.getPoints() == null ? 0 : member.getPoints();
            member.setPoints(currentPoints + rule.getRegisterIntegral());
            umsMemberService.updateById(member);

            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(userId);
            flow.setOperType(4); // 4: 注册赠送
            flow.setIntegralNum(rule.getRegisterIntegral());
            flow.setRemark("注册赠送积分");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(userId));
            integralFlowService.save(flow);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeInvitePoints(Long userId, Long inviterId) {
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRecommendIntegral() == null || rule.getRecommendIntegral() <= 0) {
            return;
        }

        UmsMember inviter = umsMemberService.getById(inviterId);
        if (inviter != null) {
            Integer currentPoints = inviter.getPoints() == null ? 0 : inviter.getPoints();
            inviter.setPoints(currentPoints + rule.getRecommendIntegral());
            umsMemberService.updateById(inviter);

            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(inviterId);
            flow.setSourceUserId(userId); // 来源是新注册的用户
            flow.setOperType(6); // 6: 推荐注册
            flow.setIntegralNum(rule.getRecommendIntegral());
            flow.setRemark("推荐注册赠送积分");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(inviterId));
            integralFlowService.save(flow);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean distributeSignInPoints(Long userId) {
        // 1. 获取规则
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getSignIntegral() == null || rule.getSignIntegral() <= 0) {
            return false;
        }

        // 2. 检查今日是否已签到
        long count = integralFlowService.count(Wrappers.<TbIntegralFlow>lambdaQuery()
                .eq(TbIntegralFlow::getUserId, userId)
                .eq(TbIntegralFlow::getOperType, 5) // 5: 每日签到
                .ge(TbIntegralFlow::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay()));
        
        if (count > 0) {
            return false; // 已签到
        }

        // 3. 发放积分
        UmsMember member = umsMemberService.getById(userId);
        if (member != null) {
            Integer currentPoints = member.getPoints() == null ? 0 : member.getPoints();
            member.setPoints(currentPoints + rule.getSignIntegral());
            umsMemberService.updateById(member);

            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(userId);
            flow.setOperType(5); // 5: 每日签到
            flow.setIntegralNum(rule.getSignIntegral());
            flow.setRemark("每日签到赠送积分");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(userId));
            integralFlowService.save(flow);
            return true;
        }
        return false;
    }
}
