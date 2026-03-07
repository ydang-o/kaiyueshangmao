package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.UmsMember;

public interface UmsMemberService extends IService<UmsMember> {
    
    /**
     * 根据会员码获取会员
     * @param memberCode 会员码
     * @return 会员对象
     */
    UmsMember getByMemberCode(String memberCode);

    /**
     * 根据手机号获取会员
     * @param phone 手机号
     * @return 会员对象
     */
    UmsMember getByPhone(String phone);

    /**
     * 根据手机号获取会员，不存在则创建（自动生成会员码）
     * @param phone 手机号
     * @param nickname 昵称（可选）
     * @param avatar 头像 URL（可选）
     * @return 会员对象，永不为 null
     */
    UmsMember getOrCreateByPhone(String phone, String nickname, String avatar);
}
