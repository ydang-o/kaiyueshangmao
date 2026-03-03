package com.dingyangmall.web.mapper;

import com.dingyangmall.web.entity.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WxMaUserMapper {
    WxMaUser selectByOpenid(@Param("openid") String openid);

    int upsert(@Param("u") WxMaUser user);
}

