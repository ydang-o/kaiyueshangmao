/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/mall/referral-code"})
public class ReferralCodeApi {
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private TbIntegralFlowService integralFlowService;
    @Autowired
    private TbIntegralRuleService integralRuleService;

    @GetMapping(value={"/members"})
    public AjaxResult listMembers(@RequestParam(defaultValue="1") Integer current, @RequestParam(defaultValue="20") Integer size, @RequestParam(required=false) String keyword) {
        List members = this.umsMemberService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(UmsMember::getDelFlag, "0")).and(StringUtils.isEmpty(keyword) ? null : w -> ((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)w.like(UmsMember::getPhone, keyword)).or()).like(UmsMember::getNickname, keyword)).or()).like(UmsMember::getMemberCode, keyword))).orderByDesc(UmsMember::getCreateTime));
        List allReferrals = this.integralFlowService.list((Wrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, 6));
        Map<Long, Long> inviterCountMap = allReferrals.stream().filter(f -> f.getSourceUserId() != null).collect(Collectors.groupingBy(TbIntegralFlow::getSourceUserId, Collectors.counting()));
        int total = members.size();
        int from = (current - 1) * size;
        int to = Math.min(from + size, total);
        List<UmsMember> pageData = from < total ? members.subList(from, to) : Collections.emptyList();
        ArrayList records = new ArrayList();
        for (UmsMember m : pageData) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("id", m.getId());
            row.put("nickname", m.getNickname());
            row.put("phone", m.getPhone());
            row.put("memberCode", m.getMemberCode());
            row.put("points", m.getPoints());
            row.put("level", m.getLevel());
            row.put("createTime", m.getCreateTime());
            row.put("referralCount", inviterCountMap.getOrDefault(m.getId(), 0L));
            records.add(row);
        }
        HashMap<String, Serializable> page = new HashMap<String, Serializable>();
        page.put("records", records);
        page.put("total", Integer.valueOf(total));
        page.put("current", current);
        page.put("size", size);
        return AjaxResult.success(page);
    }

    @PostMapping(value={"/gift"})
    public AjaxResult giftReferral(@RequestBody Map<String, Object> body) {
        Number targetUserIdNum = (Number)body.get("targetUserId");
        String inviteCode = (String)body.get("inviteCode");
        if (targetUserIdNum == null || inviteCode == null || inviteCode.trim().isEmpty()) {
            return AjaxResult.error("\u88ab\u63a8\u8350\u4eba\u548c\u63a8\u8350\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        }
        Long targetUserId = targetUserIdNum.longValue();
        UmsMember targetMember = (UmsMember)this.umsMemberService.getById(targetUserId);
        if (targetMember == null) {
            return AjaxResult.error("\u88ab\u63a8\u8350\u4eba\u4e0d\u5b58\u5728");
        }
        UmsMember inviter = this.umsMemberService.getByMemberCode(inviteCode.trim());
        if (inviter == null) {
            return AjaxResult.error("\u63a8\u8350\u7801\u65e0\u6548\uff0c\u672a\u627e\u5230\u5bf9\u5e94\u4f1a\u5458");
        }
        if (inviter.getId().equals(targetUserId)) {
            return AjaxResult.error("\u4e0d\u80fd\u63a8\u8350\u81ea\u5df1");
        }
        List existing = this.integralFlowService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, 6)).eq(TbIntegralFlow::getSourceUserId, inviter.getId())).eq(TbIntegralFlow::getUserId, targetUserId));
        if (existing != null && !existing.isEmpty()) {
            return AjaxResult.error("\u8be5\u63a8\u8350\u5173\u7cfb\u5df2\u5b58\u5728\uff0c\u4e0d\u53ef\u91cd\u590d\u8d60\u9001");
        }
        this.integralRuleService.distributeInvitePoints(targetUserId, inviter.getId());
        return AjaxResult.success("\u63a8\u8350\u7801\u8d60\u9001\u6210\u529f\uff0c\u79ef\u5206\u5df2\u53d1\u653e\u7ed9\u63a8\u8350\u4eba\uff1a" + inviter.getNickname());
    }

    @GetMapping(value={"/records"})
    public AjaxResult listRecords(@RequestParam(defaultValue="1") Integer current, @RequestParam(defaultValue="20") Integer size) {
        List referrals = this.integralFlowService.list((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbIntegralFlow::getOperType, 6)).orderByDesc(TbIntegralFlow::getCreateTime));
        List<Object> memberIds = new ArrayList();
        for (TbIntegralFlow f : referrals) {
            if (f.getUserId() != null) {
                memberIds.add(f.getUserId());
            }
            if (f.getSourceUserId() == null) continue;
            memberIds.add(f.getSourceUserId());
        }
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        HashMap memberMap = new HashMap();
        if (!memberIds.isEmpty()) {
            this.umsMemberService.listByIds(memberIds).forEach(m -> memberMap.put(m.getId(), m));
        }
        int total = referrals.size();
        int from = (current - 1) * size;
        int to = Math.min(from + size, total);
        List<TbIntegralFlow> pageData = from < total ? referrals.subList(from, to) : Collections.emptyList();
        ArrayList records = new ArrayList();
        for (TbIntegralFlow flow : pageData) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            UmsMember inviter = (UmsMember)memberMap.get(flow.getSourceUserId());
            UmsMember invitee = (UmsMember)memberMap.get(flow.getUserId());
            row.put("id", flow.getId());
            row.put("inviterId", flow.getSourceUserId());
            row.put("inviterName", inviter != null ? inviter.getNickname() : "-");
            row.put("inviteeId", flow.getUserId());
            row.put("inviteeName", invitee != null ? invitee.getNickname() : "-");
            row.put("rewardPoints", flow.getIntegralNum());
            row.put("remark", flow.getRemark());
            row.put("operTime", flow.getOperTime());
            records.add(row);
        }
        HashMap<String, Serializable> page = new HashMap<String, Serializable>();
        page.put("records", records);
        page.put("total", Integer.valueOf(total));
        page.put("current", current);
        page.put("size", size);
        return AjaxResult.success(page);
    }
}

