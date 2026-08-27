/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.mapper.GoodsSpuMapper;
import com.dingyangmall.mall.service.GoodsSpuService;
import java.io.Serializable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsSpuServiceImpl
extends ServiceImpl<GoodsSpuMapper, GoodsSpu>
implements GoodsSpuService {
    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean removeById(Serializable id) {
        super.removeById(id);
        return true;
    }

    @Override
    public IPage<GoodsSpu> page1(IPage<GoodsSpu> page, GoodsSpu goodsSpu) {
        return ((GoodsSpuMapper)this.baseMapper).selectPage1(page, goodsSpu);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean save1(GoodsSpu goodsSpu) {
        ((GoodsSpuMapper)this.baseMapper).insert(goodsSpu);
        return true;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean updateById1(GoodsSpu goodsSpu) {
        ((GoodsSpuMapper)this.baseMapper).updateById(goodsSpu);
        return true;
    }

    @Override
    public GoodsSpu getById1(String id) {
        return ((GoodsSpuMapper)this.baseMapper).selectById1(id);
    }

    @Override
    public GoodsSpu getById2(String id) {
        return ((GoodsSpuMapper)this.baseMapper).selectById2(id);
    }
}

