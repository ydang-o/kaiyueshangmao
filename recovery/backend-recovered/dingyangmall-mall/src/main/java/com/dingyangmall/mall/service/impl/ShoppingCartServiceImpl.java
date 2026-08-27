/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.ShoppingCart;
import com.dingyangmall.mall.mapper.ShoppingCartMapper;
import com.dingyangmall.mall.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartServiceImpl
extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
implements ShoppingCartService {
    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean save(ShoppingCart entity) {
        ShoppingCart shoppingCart = (ShoppingCart)((ShoppingCartMapper)this.baseMapper).selectOne((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(ShoppingCart::getUserId, entity.getUserId())).eq(ShoppingCart::getSpuId, entity.getSpuId()));
        if (shoppingCart != null) {
            entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
            ((ShoppingCartMapper)this.baseMapper).deleteById(shoppingCart);
            return super.save(entity);
        }
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean updateById(ShoppingCart entity) {
        ShoppingCart shoppingCart = (ShoppingCart)((ShoppingCartMapper)this.baseMapper).selectOne((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(ShoppingCart::getUserId, entity.getUserId())).eq(ShoppingCart::getSpuId, entity.getSpuId()));
        if (shoppingCart != null && !entity.getId().equals(shoppingCart.getId())) {
            entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
            ((ShoppingCartMapper)this.baseMapper).deleteById(shoppingCart);
            return super.updateById(entity);
        }
        return super.updateById(entity);
    }

    @Override
    public IPage<ShoppingCart> page2(IPage<ShoppingCart> page, ShoppingCart shoppingCart) {
        return ((ShoppingCartMapper)this.baseMapper).selectPage2(page, shoppingCart);
    }
}

