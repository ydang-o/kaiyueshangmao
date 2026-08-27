/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.weixin.constant.MyReturnCode;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/goodsspu", "/api/ma/goodsspu"})
public class GoodsSpuApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(GoodsSpuApi.class);
    private final GoodsSpuService goodsSpuService;

    @GetMapping(value={"/page"})
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu, String couponUserId) {
        if (goodsSpu == null) {
            goodsSpu = new GoodsSpu();
        }
        goodsSpu.setShelf("1");
        return AjaxResult.success(this.goodsSpuService.page1(page, goodsSpu));
    }

    @GetMapping(value={"/list"})
    public AjaxResult listAll(@RequestParam(defaultValue="500") int limit) {
        Page<GoodsSpu> page = new Page<GoodsSpu>(1L, Math.min(limit, 500));
        GoodsSpu query = new GoodsSpu();
        query.setShelf("1");
        List<GoodsSpu> list = this.goodsSpuService.page1(page, query).getRecords();
        return AjaxResult.success(list);
    }

    @GetMapping(value={"/{id}"})
    public AjaxResult getById(@PathVariable(value="id") String id) {
        GoodsSpu goodsSpu = this.goodsSpuService.getById2(id);
        if (goodsSpu == null) {
            return AjaxResult.error(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("id", goodsSpu.getId());
        result.put("name", goodsSpu.getName());
        result.put("sellPoint", goodsSpu.getSellPoint());
        result.put("description", goodsSpu.getDescription());
        result.put("picUrls", goodsSpu.getPicUrls());
        result.put("salesPrice", goodsSpu.getSalesPrice());
        result.put("marketPrice", goodsSpu.getMarketPrice());
        result.put("stock", goodsSpu.getStock());
        result.put("saleNum", goodsSpu.getSaleNum());
        result.put("integralPrice", goodsSpu.getIntegralPrice());
        result.put("goodsType", goodsSpu.getGoodsType());
        result.put("couponType", goodsSpu.getCouponType());
        result.put("shelf", goodsSpu.getShelf());
        result.put("categoryFirst", goodsSpu.getCategoryFirst());
        result.put("categorySecond", goodsSpu.getCategorySecond());
        result.put("createTime", goodsSpu.getCreateTime());
        result.put("updateTime", goodsSpu.getUpdateTime());
        String goodsType = goodsSpu.getGoodsType();
        if ("1".equals(goodsType) || "2".equals(goodsType)) {
            result.put("type", "virtual");
        } else {
            result.put("type", "physical");
        }
        return AjaxResult.success(result);
    }

    @Generated
    public GoodsSpuApi(GoodsSpuService goodsSpuService) {
        this.goodsSpuService = goodsSpuService;
    }
}

