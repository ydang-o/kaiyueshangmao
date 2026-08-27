/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dingyangmall.mall.entity.UmsMember;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberMapper
extends BaseMapper<UmsMember> {
    public int updatePointsAtomic(@Param(value="memberId") Long var1, @Param(value="points") Integer var2);
}

