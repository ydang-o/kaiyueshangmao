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
 * 公开接口：商品分类树，不需登录/token，不经过 weixin 模块校验。
 * 供小程序首页等场景使用，与 /weixin/api/ma/goodscategory/tree 返回一致。
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/public/ma/goodscategory")
public class PublicGoodsCategoryApi {

    private final GoodsCategoryService goodsCategoryService;

    @GetMapping("/tree")
    public AjaxResult tree(GoodsCategory goodsCategory) {
        if (goodsCategory == null) {
            goodsCategory = new GoodsCategory();
        }
        goodsCategory.setEnable(CommonConstants.YES);
        return AjaxResult.success(goodsCategoryService.selectTree(goodsCategory));
    }
}
