/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.weixin.constant.MyReturnCode;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/public/ma/goodsspu"})
public class PublicGoodsSpuApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(PublicGoodsSpuApi.class);
    private final GoodsSpuService goodsSpuService;

    @GetMapping(value={"/page"})
    public AjaxResult page(Page<GoodsSpu> page, GoodsSpu goodsSpu, String couponUserId) {
        if (goodsSpu == null) {
            goodsSpu = new GoodsSpu();
        }
        goodsSpu.setShelf("1");
        return AjaxResult.success(this.goodsSpuService.page1(page, goodsSpu));
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getById(@PathVariable(value="id") String id) {
        GoodsSpu goodsSpu = this.goodsSpuService.getById2(id);
        if (goodsSpu == null) {
            return AjaxResult.error(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
        }
        return AjaxResult.success(goodsSpu);
    }

    @Generated
    public PublicGoodsSpuApi(GoodsSpuService goodsSpuService) {
        this.goodsSpuService = goodsSpuService;
    }
}

