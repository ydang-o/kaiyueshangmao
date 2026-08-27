/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

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
        return AjaxResult.success(this.userAddressService.page(page, Wrappers.query(userAddress)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        return AjaxResult.success(this.userAddressService.getById((Serializable)((Object)id)));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:add')")
    public AjaxResult save(@RequestBody UserAddress userAddress) {
        return AjaxResult.success(this.userAddressService.save(userAddress));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:edit')")
    public AjaxResult updateById(@RequestBody UserAddress userAddress) {
        return AjaxResult.success(this.userAddressService.updateById(userAddress));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:useraddress:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.userAddressService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }
}

