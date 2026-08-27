/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.entity.UserAddress;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.service.UserAddressService;
import com.dingyangmall.mall.utils.MemberUtils;
import java.io.Serializable;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/weixin/api/ma/useraddress", "/api/ma/useraddress"})
public class UserAddressApi {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserAddressApi.class);
    private final UserAddressService userAddressService;
    private final UmsMemberService umsMemberService;
    private static final int MAX_ADDRESS_COUNT = 10;

    private String getCurrentUserId() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return null;
        }
        try {
            Long memberId = Long.parseLong(memberIdStr);
            UmsMember member = (UmsMember)this.umsMemberService.getById(memberId);
            if (member != null && StringUtils.isNotEmpty(member.getPhone())) {
                return member.getPhone();
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return memberIdStr;
    }

    @GetMapping(value={"/page"})
    public AjaxResult getUserAddressPage(Page page, UserAddress userAddress) {
        String userId = this.getCurrentUserId();
        if (StringUtils.isEmpty(userId)) {
            return AjaxResult.error("\u8bf7\u5148\u767b\u5f55");
        }
        userAddress.setUserId(userId);
        return AjaxResult.success(this.userAddressService.page(page, Wrappers.query(userAddress)));
    }

    @PostMapping
    public AjaxResult save(@RequestBody UserAddress userAddress) {
        long count;
        String userId = this.getCurrentUserId();
        if (StringUtils.isEmpty(userId)) {
            return AjaxResult.error("\u8bf7\u5148\u767b\u5f55");
        }
        userAddress.setUserId(userId);
        if ((userAddress.getId() == null || userAddress.getId().isEmpty()) && (count = this.userAddressService.count((Wrapper)Wrappers.lambdaQuery().eq(UserAddress::getUserId, userId))) >= 10L) {
            return AjaxResult.error("\u6536\u8d27\u5730\u5740\u6700\u591a\u4fdd\u5b58 10 \u4e2a\uff0c\u8bf7\u5148\u5220\u9664\u518d\u6dfb\u52a0");
        }
        return AjaxResult.success(this.userAddressService.saveOrUpdate(userAddress));
    }

    @DeleteMapping(value={"/{id}"})
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.userAddressService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public UserAddressApi(UserAddressService userAddressService, UmsMemberService umsMemberService) {
        this.userAddressService = userAddressService;
        this.umsMemberService = umsMemberService;
    }
}

