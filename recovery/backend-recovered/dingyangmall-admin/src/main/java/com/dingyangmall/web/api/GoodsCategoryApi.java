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
@RequestMapping(value={"/weixin/api/ma/goodscategory", "/api/ma/goodscategory"})
public class GoodsCategoryApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(GoodsCategoryApi.class);
    private final GoodsCategoryService goodsCategoryService;

    @GetMapping(value={"/tree"})
    public AjaxResult goodsCategoryTree(GoodsCategory goodsCategory) {
        goodsCategory.setEnable("1");
        return AjaxResult.success(this.goodsCategoryService.selectTree(goodsCategory));
    }

    @Generated
    public GoodsCategoryApi(GoodsCategoryService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
    }
}

