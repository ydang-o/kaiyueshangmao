/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.entity.GoodsCategoryTree;
import com.dingyangmall.mall.mapper.GoodsCategoryMapper;
import com.dingyangmall.mall.service.GoodsCategoryService;
import com.dingyangmall.mall.util.TreeUtil;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GoodsCategoryServiceImpl
extends ServiceImpl<GoodsCategoryMapper, GoodsCategory>
implements GoodsCategoryService {
    @Override
    public List<GoodsCategoryTree> selectTree(GoodsCategory goodsCategory) {
        return this.getTree(this.list(Wrappers.lambdaQuery(goodsCategory)));
    }

    private List<GoodsCategoryTree> getTree(List<GoodsCategory> entitys) {
        List treeList = entitys.stream().filter(entity -> !entity.getId().equals(entity.getParentId())).sorted(Comparator.comparingInt(GoodsCategory::getSort)).map(entity -> {
            GoodsCategoryTree node = new GoodsCategoryTree();
            BeanUtil.copyProperties(entity, node);
            return node;
        }).collect(Collectors.toList());
        return TreeUtil.build(treeList, "0");
    }

    @Override
    public boolean removeById(Serializable id) {
        super.removeById(id);
        this.remove((Wrapper)Wrappers.query().lambda().eq(GoodsCategory::getParentId, id));
        return true;
    }
}

