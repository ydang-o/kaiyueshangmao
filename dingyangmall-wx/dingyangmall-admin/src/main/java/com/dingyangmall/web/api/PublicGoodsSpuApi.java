package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.weixin.constant.MyReturnCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公开接口：商品分页/详情，不需登录/token，不经过 weixin 模块校验。
 * 供小程序首页、分类、搜索等场景使用，与 /weixin/api/ma/goodsspu 返回一致。
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/public/ma/goodsspu")
public class PublicGoodsSpuApi {

    private final GoodsSpuService goodsSpuService;

    @GetMapping("/page")
    public AjaxResult page(Page<GoodsSpu> page, GoodsSpu goodsSpu, String couponUserId) {
        if (goodsSpu == null) {
            goodsSpu = new GoodsSpu();
        }
        goodsSpu.setShelf(CommonConstants.YES);
        return AjaxResult.success(goodsSpuService.page1(page, goodsSpu));
    }

    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") String id) {
        GoodsSpu goodsSpu = goodsSpuService.getById2(id);
        if (goodsSpu == null) {
            return AjaxResult.error(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
        }
        return AjaxResult.success(goodsSpu);
    }
}
