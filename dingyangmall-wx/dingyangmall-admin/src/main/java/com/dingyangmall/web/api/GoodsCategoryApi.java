/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 */
package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.service.GoodsCategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品类目
 *
 * @author www.dingyangmall.com
 * @date 2019-08-12 11:46:28
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/weixin/api/ma/goodscategory", "/api/ma/goodscategory" })
public class GoodsCategoryApi {

    private final GoodsCategoryService goodsCategoryService;

    /**
    * 返回树形集合
    */
    @GetMapping("/tree")
    public AjaxResult goodsCategoryTree(GoodsCategory goodsCategory) {
		goodsCategory.setEnable(CommonConstants.YES);
		return AjaxResult.success(goodsCategoryService.selectTree(goodsCategory));
    }
}

