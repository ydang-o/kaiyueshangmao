/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品 API：小程序请求本接口后，后端查询数据库表 goods_spu。
 * <ul>
 *   <li>GET /page → 分页查询 goods_spu（上架且未删除）</li>
 *   <li>GET /list → 查询所有上架商品（默认最多 500 条，直接查 goods_spu）</li>
 *   <li>GET /{id} → 按 id 查 goods_spu 单条</li>
 * </ul>
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/weixin/api/ma/goodsspu", "/api/ma/goodsspu" })
public class GoodsSpuApi {

    private final GoodsSpuService goodsSpuService;

	/**
	 * 分页查询：后端查 goods_spu 表，条件 shelf=1、del_flag=0。
	 */
    @GetMapping("/page")
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu, String couponUserId) {
		if (goodsSpu == null) goodsSpu = new GoodsSpu();
		goodsSpu.setShelf(CommonConstants.YES);
        return AjaxResult.success(goodsSpuService.page1(page, goodsSpu));
    }

	/**
	 * 查询所有上架商品：后端直接查 goods_spu 表，小程序可调用此接口拿到全部商品列表。
	 * @param limit 最多返回条数，默认 500
	 */
	@GetMapping("/list")
	public AjaxResult listAll(@RequestParam(defaultValue = "500") int limit) {
		Page<GoodsSpu> page = new Page<>(1, Math.min(limit, 500));
		GoodsSpu query = new GoodsSpu();
		query.setShelf(CommonConstants.YES);
		List<GoodsSpu> list = goodsSpuService.page1(page, query).getRecords();
		return AjaxResult.success(list);
	}

    /**
     * 通过 id 查询：后端查 goods_spu 表。
     */
    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") String id){
		GoodsSpu goodsSpu = goodsSpuService.getById2(id);
		if(goodsSpu == null){
			return AjaxResult.error(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
		}
        return AjaxResult.success(goodsSpu);
    }

}

