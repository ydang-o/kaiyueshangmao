/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.GoodsCategory
 *  com.dingyangmall.mall.entity.GoodsSpu
 *  com.dingyangmall.mall.entity.TbBanner
 *  com.dingyangmall.mall.service.GoodsCategoryService
 *  com.dingyangmall.mall.service.GoodsSpuService
 *  com.dingyangmall.mall.service.TbBannerService
 *  com.dingyangmall.system.domain.SysNotice
 *  com.dingyangmall.system.service.ISysNoticeService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.TbBanner;
import com.dingyangmall.mall.service.GoodsCategoryService;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.TbBannerService;
import com.dingyangmall.system.domain.SysNotice;
import com.dingyangmall.system.service.ISysNoticeService;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/ma/home"})
public class ApiMaHomeController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ApiMaHomeController.class);
    private final GoodsSpuService goodsSpuService;
    private final GoodsCategoryService goodsCategoryService;
    private final ISysNoticeService noticeService;
    private final TbBannerService bannerService;

    @GetMapping
    public AjaxResult home() {
        HashMap data = new HashMap();
        List<TbBanner> bannerList = ((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)this.bannerService.lambdaQuery().eq(TbBanner::getStatus, (Object)"1")).eq(TbBanner::getDelFlag, (Object)"0")).orderByAsc(TbBanner::getSort)).last("LIMIT 6")).list();
        log.info("[Home] banner query count: {}", (Object)(bannerList != null ? bannerList.size() : 0));
        if (bannerList == null || bannerList.isEmpty()) {
            log.info("[Home] no active banners, falling back to products");
            Page pageBanner = new Page(1L, 6L);
            List<GoodsSpu> goodsBanner = this.queryGoodsList((Page<GoodsSpu>)pageBanner, 6);
            bannerList = goodsBanner.stream().map(spu -> {
                TbBanner b = new TbBanner();
                try {
                    b.setId(Long.valueOf(Long.parseLong(spu.getId())));
                }
                catch (Exception exception) {
                    // empty catch block
                }
                b.setTitle(spu.getName());
                if (spu.getPicUrls() != null && spu.getPicUrls().length > 0) {
                    b.setPicUrl(spu.getPicUrls()[0]);
                }
                b.setLinkUrl(spu.getId());
                b.setLinkType("1");
                return b;
            }).collect(Collectors.toList());
        }
        if (bannerList != null) {
            bannerList.forEach(b -> {
                if (b.getPicUrl() != null && b.getPicUrl().startsWith("/")) {
                    log.debug("[Home] banner image raw url: {}", (Object)b.getPicUrl());
                }
            });
        }
        data.put("bannerList", bannerList);
        GoodsCategory queryCat = new GoodsCategory();
        queryCat.setEnable("1");
        List categoryTree = this.goodsCategoryService.selectTree(queryCat);
        data.put("categoryTree", categoryTree != null ? categoryTree : List.of());
        SysNotice noticeQuery = new SysNotice();
        noticeQuery.setStatus("0");
        List notices = this.noticeService.selectNoticeList(noticeQuery);
        List noticeList = notices != null ? notices.stream().map(n -> n.getNoticeTitle() != null ? n.getNoticeTitle() : n.getNoticeContent()).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList()) : List.of();
        data.put("noticeList", noticeList);
        List<GoodsSpu> goodsList = this.queryGoodsList((Page<GoodsSpu>)new Page(1L, 10L), 10);
        data.put("goodsList", goodsList);
        List<GoodsSpu> promoList = this.queryGoodsList((Page<GoodsSpu>)new Page(1L, 2L), 2);
        data.put("promoList", promoList);
        return AjaxResult.success(data);
    }

    private List<GoodsSpu> queryGoodsList(Page<GoodsSpu> page, int size) {
        GoodsSpu query = new GoodsSpu();
        query.setShelf("1");
        List list = this.goodsSpuService.page1(page, query).getRecords();
        if (list != null && !list.isEmpty()) {
            return list;
        }
        log.warn("[Home] goods_spu no record with shelf=1, fallback to all (del_flag=0). Please set shelf='1' in DB for normal display.");
        query.setShelf(null);
        List<GoodsSpu> fallback = this.goodsSpuService.page1(page, query).getRecords();
        return fallback != null ? fallback : List.of();
    }

    @Generated
    public ApiMaHomeController(GoodsSpuService goodsSpuService, GoodsCategoryService goodsCategoryService, ISysNoticeService noticeService, TbBannerService bannerService) {
        this.goodsSpuService = goodsSpuService;
        this.goodsCategoryService = goodsCategoryService;
        this.noticeService = noticeService;
        this.bannerService = bannerService;
    }
}

