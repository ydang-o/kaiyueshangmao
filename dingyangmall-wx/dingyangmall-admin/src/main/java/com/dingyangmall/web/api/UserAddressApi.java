/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.UserAddress;
import com.dingyangmall.mall.service.UserAddressService;
import com.dingyangmall.mall.utils.MemberUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户收货地址
 *
 * @author JL
 * @date 2019-09-11 14:28:59
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = { "/weixin/api/ma/useraddress", "/api/ma/useraddress" })
public class UserAddressApi {

    private final UserAddressService userAddressService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param userAddress 用户收货地址
    * @return
    */
    @GetMapping("/page")
    public AjaxResult getUserAddressPage(Page page, UserAddress userAddress) {
		userAddress.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success(userAddressService.page(page,Wrappers.query(userAddress)));
    }

    /**
    * 新增、修改用户收货地址
    * @param userAddress 用户收货地址
    * @return AjaxResult
    */
    @PostMapping
    public AjaxResult save(@RequestBody UserAddress userAddress){
		userAddress.setUserId(MemberUtils.getMemberId());
        return AjaxResult.success(userAddressService.saveOrUpdate(userAddress));
    }

    /**
    * 通过id删除用户收货地址
    * @param id
    * @return AjaxResult
    */
    @DeleteMapping("/{id}")
    public AjaxResult removeById(@PathVariable String id){
		return AjaxResult.success(userAddressService.removeById(id));
    }

}

