/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.UserAddress
 *  com.dingyangmall.mall.service.UserAddressService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.UserAddress;
import com.dingyangmall.mall.service.UserAddressService;
import java.io.Serializable;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/useraddress"})
public class UserAddressController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(UserAddressController.class);
    private final UserAddressService userAddressService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:index')")
    public AjaxResult getUserAddressPage(Page page, UserAddress userAddress) {
        return AjaxResult.success((Object)this.userAddressService.page((IPage)page, (Wrapper)Wrappers.query((Object)userAddress)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success((Object)this.userAddressService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:add')")
    public AjaxResult save(@RequestBody UserAddress userAddress) {
        return AjaxResult.success((Object)this.userAddressService.save((Object)userAddress));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:edit')")
    public AjaxResult updateById(@RequestBody UserAddress userAddress) {
        return AjaxResult.success((Object)this.userAddressService.updateById((Object)userAddress));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success((Object)this.userAddressService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }
}

