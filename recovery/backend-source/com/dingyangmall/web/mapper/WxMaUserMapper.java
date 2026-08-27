/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  org.apache.ibatis.annotations.Mapper
 *  org.apache.ibatis.annotations.Param
 */
package com.dingyangmall.web.mapper;

import com.dingyangmall.web.entity.WxMaUser;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WxMaUserMapper {
    public WxMaUser selectByOpenid(@Param(value="openid") String var1);

    public WxMaUser selectById(@Param(value="id") String var1);

    public WxMaUser selectByPhone(@Param(value="phone") String var1);

    public List<WxMaUser> selectPage(@Param(value="offset") long var1, @Param(value="limit") long var3, @Param(value="phone") String var5, @Param(value="nickname") String var6);

    public long countPage(@Param(value="phone") String var1, @Param(value="nickname") String var2);

    public int upsert(@Param(value="u") WxMaUser var1);

    public int updatePhoneByOpenid(@Param(value="openid") String var1, @Param(value="phone") String var2);
}

