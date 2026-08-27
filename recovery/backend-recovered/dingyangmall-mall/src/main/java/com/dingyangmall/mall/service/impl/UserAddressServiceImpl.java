/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.UserAddress;
import com.dingyangmall.mall.mapper.UserAddressMapper;
import com.dingyangmall.mall.service.UserAddressService;
import org.springframework.stereotype.Service;

@Service
public class UserAddressServiceImpl
extends ServiceImpl<UserAddressMapper, UserAddress>
implements UserAddressService {
    @Override
    public boolean save(UserAddress entity) {
        this.setIsDefault(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(UserAddress entity) {
        this.setIsDefault(entity);
        return super.updateById(entity);
    }

    void setIsDefault(UserAddress entity) {
        if ("1".equals(entity.getIsDefault())) {
            UserAddress userAddress = new UserAddress();
            userAddress.setIsDefault("0");
            super.update(userAddress, (Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(UserAddress::getUserId, entity.getUserId())).eq(UserAddress::getIsDefault, "1"));
        }
    }
}

