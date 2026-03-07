package com.dingyangmall.web.mapper;

import com.dingyangmall.web.entity.WxMaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WxMaUserMapper {
    WxMaUser selectByOpenid(@Param("openid") String openid);

    WxMaUser selectById(@Param("id") String id);

    /** 按手机号查询 wx_user（同一手机可能有多条，取一条） */
    WxMaUser selectByPhone(@Param("phone") String phone);

    /** 分页查询 wx_user 列表，支持按手机号、昵称筛选 */
    List<WxMaUser> selectPage(@Param("offset") long offset, @Param("limit") long limit,
                             @Param("phone") String phone, @Param("nickname") String nickname);

    /** 分页总数 */
    long countPage(@Param("phone") String phone, @Param("nickname") String nickname);

    int upsert(@Param("u") WxMaUser user);

    int updatePhoneByOpenid(@Param("openid") String openid, @Param("phone") String phone);
}

