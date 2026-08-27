/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.TbIntegralRuleMapper;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbIntegralRuleServiceImpl
extends ServiceImpl<TbIntegralRuleMapper, TbIntegralRule>
implements TbIntegralRuleService {
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TbIntegralFlowService integralFlowService;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void distributeRegisterPoints(Long userId) {
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRegisterIntegral() == null || rule.getRegisterIntegral() <= 0) {
            return;
        }
        UmsMember member = (UmsMember)this.umsMemberService.getById(userId);
        if (member != null) {
            Integer currentPoints = member.getPoints() == null ? 0 : member.getPoints();
            member.setPoints(currentPoints + rule.getRegisterIntegral());
            this.umsMemberService.updateById(member);
            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(userId);
            flow.setOperType(4);
            flow.setIntegralNum(rule.getRegisterIntegral());
            flow.setRemark("\u6ce8\u518c\u8d60\u9001\u79ef\u5206");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(userId));
            this.integralFlowService.save(flow);
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void distributeInvitePoints(Long userId, Long inviterId) {
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getRecommendIntegral() == null || rule.getRecommendIntegral() <= 0) {
            return;
        }
        long existingCount = this.integralFlowService.count((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, 6)).eq(TbIntegralFlow::getSourceUserId, userId));
        if (existingCount > 0L) {
            return;
        }
        UmsMember inviter = (UmsMember)this.umsMemberService.getById(inviterId);
        if (inviter != null) {
            Integer currentPoints = inviter.getPoints() == null ? 0 : inviter.getPoints();
            inviter.setPoints(currentPoints + rule.getRecommendIntegral());
            this.umsMemberService.updateById(inviter);
            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(inviterId);
            flow.setSourceUserId(userId);
            flow.setOperType(6);
            flow.setIntegralNum(rule.getRecommendIntegral());
            flow.setRemark("\u63a8\u8350\u6ce8\u518c\u8d60\u9001\u79ef\u5206");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(inviterId));
            this.integralFlowService.save(flow);
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean distributeSignInPoints(Long userId) {
        TbIntegralRule rule = this.list().stream().findFirst().orElse(null);
        if (rule == null || rule.getSignIntegral() == null || rule.getSignIntegral() <= 0) {
            return false;
        }
        long count = this.integralFlowService.count((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getUserId, userId)).eq(TbIntegralFlow::getOperType, 5)).ge(TbIntegralFlow::getCreateTime, LocalDateTime.now().toLocalDate().atStartOfDay()));
        if (count > 0L) {
            return false;
        }
        UmsMember member = (UmsMember)this.umsMemberService.getById(userId);
        if (member != null) {
            Integer currentPoints = member.getPoints() == null ? 0 : member.getPoints();
            member.setPoints(currentPoints + rule.getSignIntegral());
            this.umsMemberService.updateById(member);
            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(userId);
            flow.setOperType(5);
            flow.setIntegralNum(rule.getSignIntegral());
            flow.setRemark("\u6bcf\u65e5\u7b7e\u5230\u8d60\u9001\u79ef\u5206");
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(userId));
            this.integralFlowService.save(flow);
            return true;
        }
        return false;
    }
}

