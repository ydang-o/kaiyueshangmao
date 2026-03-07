package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.UmsMemberMapper;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.stereotype.Service;
import com.dingyangmall.common.utils.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {

    @Override
    public UmsMember getByMemberCode(String memberCode) {
        return getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getMemberCode, memberCode));
    }

    @Override
    public UmsMember getByPhone(String phone) {
        return getOne(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getPhone, phone));
    }

    @Override
    public UmsMember getOrCreateByPhone(String phone, String nickname, String avatar) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        UmsMember member = getByPhone(phone);
        if (member != null) {
            return member;
        }
        member = new UmsMember();
        member.setPhone(phone);
        member.setNickname(StringUtils.isNotEmpty(nickname) ? nickname : ("用户" + phone.substring(Math.max(0, phone.length() - 4))));
        member.setAvatar(avatar);
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setLevel(0);
        member.setDelFlag("0");
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        save(member);
        return member;
    }

    @Override
    public boolean save(UmsMember entity) {
        if (StringUtils.isEmpty(entity.getMemberCode())) {
            entity.setMemberCode(generateMemberCode());
        }
        return super.save(entity);
    }

    private String generateMemberCode() {
        // Generate 10-digit numeric code to avoid collision
        String code = RandomUtil.randomNumbers(10);
        // Ensure uniqueness
        while (count(Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getMemberCode, code)) > 0) {
            code = RandomUtil.randomNumbers(10);
        }
        return code;
    }
}
