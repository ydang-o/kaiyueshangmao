/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.service.GoodsCategoryService;
import java.io.Serializable;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/goodscategory"})
public class GoodsCategoryController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(GoodsCategoryController.class);
    private final GoodsCategoryService goodsCategoryService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:index')")
    public AjaxResult getGoodsCategoryPage(Page page, GoodsCategory goodsCategory) {
        return AjaxResult.success(this.goodsCategoryService.page(page, Wrappers.query(goodsCategory)));
    }

    @GetMapping(value={"/tree"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:index')")
    public AjaxResult getGoodsCategoryTree() {
        return AjaxResult.success(this.goodsCategoryService.selectTree(null));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success(this.goodsCategoryService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:add')")
    public AjaxResult save(@RequestBody GoodsCategory goodsCategory) {
        return AjaxResult.success(this.goodsCategoryService.save(goodsCategory));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:edit')")
    public AjaxResult updateById(@RequestBody GoodsCategory goodsCategory) {
        if (goodsCategory.getId().equals(goodsCategory.getParentId())) {
            return AjaxResult.error("\u4e0d\u80fd\u5c06\u672c\u7ea7\u8bbe\u4e3a\u7236\u7c7b");
        }
        return AjaxResult.success(this.goodsCategoryService.updateById(goodsCategory));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodscategory:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.goodsCategoryService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public GoodsCategoryController(GoodsCategoryService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
    }
}

