/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.page.TableDataInfo
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.mall.entity.TbCouponInfo
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.TbCouponInfoService
 *  com.dingyangmall.mall.service.UmsMemberService
 *  lombok.Generated
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.UmsMemberService;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/mall/coupon", "/dev-api/mall/coupon"})
public class TbCouponInfoController
extends BaseController {
    private final TbCouponInfoService couponInfoService;
    private final UmsMemberService umsMemberService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:index')")
    public TableDataInfo getPage(Page page, TbCouponInfo tbCouponInfo) {
        IPage result = this.couponInfoService.page((IPage)page, (Wrapper)Wrappers.query((Object)tbCouponInfo).lambda().orderByDesc(TbCouponInfo::getCreateTime));
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("\u67e5\u8be2\u6210\u529f");
        rspData.setRows(result.getRecords());
        rspData.setTotal(result.getTotal());
        return rspData;
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:get')")
    public AjaxResult getById(@PathVariable(value="id") Long id) {
        return AjaxResult.success((Object)this.couponInfoService.getById((Serializable)id));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:add')")
    public AjaxResult save(@RequestBody TbCouponInfo tbCouponInfo) {
        return this.toAjax(this.couponInfoService.save((Object)tbCouponInfo));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:edit')")
    public AjaxResult update(@RequestBody TbCouponInfo tbCouponInfo) {
        return this.toAjax(this.couponInfoService.updateById((Object)tbCouponInfo));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:del')")
    public AjaxResult removeById(@PathVariable Long id) {
        return this.toAjax(this.couponInfoService.removeById((Serializable)id));
    }

    @PostMapping(value={"/distribute"})
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:add')")
    public AjaxResult distribute(@RequestBody Map<String, Object> body) {
        String goodsId;
        String phone = body != null && body.get("phone") != null ? body.get("phone").toString().trim() : null;
        Long userId = null;
        if (body != null && body.get("userId") != null) {
            Object u = body.get("userId");
            if (u instanceof Number) {
                userId = ((Number)u).longValue();
            } else if (u != null) {
                try {
                    userId = Long.parseLong(u.toString());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
        }
        if (userId == null && StringUtils.isNotEmpty((String)phone)) {
            UmsMember member = this.umsMemberService.getByPhone(phone);
            if (member == null) {
                return AjaxResult.error((String)"\u7528\u6237\u4e0d\u5b58\u5728\uff0c\u8bf7\u786e\u8ba4\u624b\u673a\u53f7\u5df2\u6ce8\u518c");
            }
            userId = member.getId();
        }
        if (userId == null) {
            return AjaxResult.error((String)"\u8bf7\u63d0\u4f9b\u624b\u673a\u53f7(phone)\u6216\u7528\u6237ID(userId)");
        }
        String string = goodsId = body != null && body.get("goodsId") != null ? body.get("goodsId").toString().trim() : null;
        if (StringUtils.isEmpty(goodsId)) {
            return AjaxResult.error((String)"\u8bf7\u9009\u62e9\u5546\u54c1\u5238(goodsId)");
        }
        int count = 1;
        if (body != null && body.get("count") != null) {
            try {
                count = Integer.parseInt(body.get("count").toString());
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        int validityDays = 365;
        if (body != null && body.get("validityDays") != null) {
            try {
                validityDays = Integer.parseInt(body.get("validityDays").toString());
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        try {
            List list = this.couponInfoService.distributeCoupon(userId, goodsId, count, validityDays);
            return AjaxResult.success((String)("\u53d1\u653e\u6210\u529f\uff0c\u5171 " + list.size() + " \u5f20"), (Object)list);
        }
        catch (RuntimeException e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @GetMapping(value={"/statistics"})
    @PreAuthorize(value="@ss.hasPermi('mall:coupon:index')")
    public AjaxResult statistics(@RequestParam(required=false) Long verifyDealerId) {
        LambdaQueryWrapper wrapper = (LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2);
        long totalVerified = this.couponInfoService.count((Wrapper)wrapper);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long todayVerified = this.couponInfoService.count((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2)).ge(TbCouponInfo::getVerifyTime, (Object)start)).le(TbCouponInfo::getVerifyTime, (Object)end));
        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("totalVerified", totalVerified);
        data.put("todayVerified", todayVerified);
        if (verifyDealerId != null) {
            long byDealer = this.couponInfoService.count((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbCouponInfo::getCouponStatus, (Object)2)).eq(TbCouponInfo::getVerifyDealerId, (Object)verifyDealerId));
            data.put("byDealer", byDealer);
        }
        return AjaxResult.success(data);
    }

    @Generated
    public TbCouponInfoController(TbCouponInfoService couponInfoService, UmsMemberService umsMemberService) {
        this.couponInfoService = couponInfoService;
        this.umsMemberService = umsMemberService;
    }
}

