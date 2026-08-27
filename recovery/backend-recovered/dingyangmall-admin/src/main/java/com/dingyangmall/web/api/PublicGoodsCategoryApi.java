/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.service.GoodsCategoryService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/public/ma/goodscategory"})
public class PublicGoodsCategoryApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(PublicGoodsCategoryApi.class);
    private final GoodsCategoryService goodsCategoryService;

    @GetMapping(value={"/tree"})
    public AjaxResult tree(GoodsCategory goodsCategory) {
        if (goodsCategory == null) {
            goodsCategory = new GoodsCategory();
        }
        goodsCategory.setEnable("1");
        return AjaxResult.success(this.goodsCategoryService.selectTree(goodsCategory));
    }

    @Generated
    public PublicGoodsCategoryApi(GoodsCategoryService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
    }
}

