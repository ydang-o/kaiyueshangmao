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

    /** 单个用户最多 10 个收货地址 */
    private static final int MAX_ADDRESS_COUNT = 10;

    /**
    * 新增、修改用户收货地址（最多 10 个，设默认时会将其他地址改为非默认）
    * @param userAddress 用户收货地址
    * @return AjaxResult
    */
    @PostMapping
    public AjaxResult save(@RequestBody UserAddress userAddress){
		String userId = MemberUtils.getMemberId();
		userAddress.setUserId(userId);
		if (userAddress.getId() == null || userAddress.getId().isEmpty()) {
			long count = userAddressService.count(com.baomidou.mybatisplus.core.toolkit.Wrappers.<UserAddress>lambdaQuery().eq(UserAddress::getUserId, userId));
			if (count >= MAX_ADDRESS_COUNT) {
				return AjaxResult.error("收货地址最多保存 " + MAX_ADDRESS_COUNT + " 个，请先删除再添加");
			}
		}
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

