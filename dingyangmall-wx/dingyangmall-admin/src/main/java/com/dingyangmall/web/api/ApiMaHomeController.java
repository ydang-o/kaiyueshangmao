package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.entity.GoodsCategory;
import com.dingyangmall.mall.entity.GoodsCategoryTree;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsCategoryService;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.system.domain.SysNotice;
import com.dingyangmall.system.service.ISysNoticeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小程序首页聚合接口：一次返回轮播、分类宫格、公告、猜你喜欢、推荐块，与各大购物平台首页一致。
 * 路径：GET /api/ma/home
 * 无需登录，走 /api/ma/** 链（token 可选）。
 */
@Slf4j
@RestController
@RequestMapping("/api/ma/home")
@AllArgsConstructor
public class ApiMaHomeController {

    private final GoodsSpuService goodsSpuService;
    private final GoodsCategoryService goodsCategoryService;
    private final ISysNoticeService noticeService;

    /**
     * 首页数据：轮播图、宫格分类、公告、猜你喜欢列表、推荐大图块
     */
    @GetMapping
    public AjaxResult home() {
        Map<String, Object> data = new HashMap<>();

        // 1. 轮播图：取前 6 条上架商品（无上架时回退为查所有未删除商品，便于首页有数据）
        Page<GoodsSpu> pageBanner = new Page<>(1, 6);
        List<GoodsSpu> bannerList = queryGoodsList(pageBanner, 6);
        data.put("bannerList", bannerList);

        // 2. 分类树：宫格导航用
        GoodsCategory queryCat = new GoodsCategory();
        queryCat.setEnable(CommonConstants.YES);
        List<GoodsCategoryTree> categoryTree = goodsCategoryService.selectTree(queryCat);
        data.put("categoryTree", categoryTree != null ? categoryTree : List.of());

        // 3. 公告列表（取标题或内容用于展示）
        SysNotice noticeQuery = new SysNotice();
        noticeQuery.setStatus("0");
        List<SysNotice> notices = noticeService.selectNoticeList(noticeQuery);
        List<String> noticeList = notices != null
            ? notices.stream()
                .map(n -> n.getNoticeTitle() != null ? n.getNoticeTitle() : n.getNoticeContent())
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toList())
            : List.of();
        data.put("noticeList", noticeList);

        // 4. 猜你喜欢：第一页 10 条
        List<GoodsSpu> goodsList = queryGoodsList(new Page<>(1, 10), 10);
        data.put("goodsList", goodsList);

        // 5. 推荐大图块：2 条
        List<GoodsSpu> promoList = queryGoodsList(new Page<>(1, 2), 2);
        data.put("promoList", promoList);

        return AjaxResult.success(data);
    }

    /**
     * 查商品列表：先按 shelf=1 查，若为空则回退为不按 shelf 过滤（仅 del_flag=0），并打日志，确保首页有数据可展示。
     */
    private List<GoodsSpu> queryGoodsList(Page<GoodsSpu> page, int size) {
        GoodsSpu query = new GoodsSpu();
        query.setShelf(CommonConstants.YES);
        List<GoodsSpu> list = goodsSpuService.page1(page, query).getRecords();
        if (list != null && !list.isEmpty()) {
            return list;
        }
        log.warn("[Home] goods_spu no record with shelf=1, fallback to all (del_flag=0). Please set shelf='1' in DB for normal display.");
        query.setShelf(null);
        List<GoodsSpu> fallback = goodsSpuService.page1(page, query).getRecords();
        return fallback != null ? fallback : List.of();
    }
}
