/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.UmsMemberMapper;
import com.dingyangmall.mall.service.UmsMemberService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class UmsMemberServiceImpl
extends ServiceImpl<UmsMemberMapper, UmsMember>
implements UmsMemberService {
    @Override
    public UmsMember getByMemberCode(String memberCode) {
        return (UmsMember)this.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getMemberCode, memberCode));
    }

    @Override
    public UmsMember getByPhone(String phone) {
        return (UmsMember)this.getOne((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getPhone, phone));
    }

    @Override
    public UmsMember getOrCreateByPhone(String phone, String nickname, String avatar) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        UmsMember member = this.getByPhone(phone);
        if (member != null) {
            return member;
        }
        member = new UmsMember();
        member.setPhone(phone);
        member.setNickname((String)(StringUtils.isNotEmpty(nickname) ? nickname : "\u7528\u6237" + phone.substring(Math.max(0, phone.length() - 4))));
        member.setAvatar(avatar);
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        this.save(member);
        return member;
    }

    @Override
    public UmsMember getOrCreateByOpenid(String openid, String nickname, String avatar) {
        if (StringUtils.isEmpty(openid)) {
            return null;
        }
        UmsMember member = this.getByMemberCode(openid);
        if (member != null) {
            return member;
        }
        member = new UmsMember();
        member.setMemberCode(openid);
        member.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : "\u5fae\u4fe1\u7528\u6237");
        member.setAvatar(avatar);
        String suffix = openid.length() >= 8 ? openid.substring(openid.length() - 8) : openid;
        String pseudoPhone = "999" + suffix;
        member.setPhone(pseudoPhone);
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        ((UmsMemberMapper)this.baseMapper).insert(member);
        return member;
    }

    @Override
    public boolean save(UmsMember entity) {
        if (StringUtils.isEmpty(entity.getMemberCode())) {
            entity.setMemberCode(this.generateMemberCode());
        }
        return super.save(entity);
    }

    private String generateMemberCode() {
        String code = RandomUtil.randomNumbers(10);
        while (this.count((Wrapper)Wrappers.lambdaQuery().eq(UmsMember::getMemberCode, code)) > 0L) {
            code = RandomUtil.randomNumbers(10);
        }
        return code;
    }
}

