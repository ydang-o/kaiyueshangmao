/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.domain.entity.SysUser
 *  com.dingyangmall.common.core.domain.model.LoginBody
 *  com.dingyangmall.common.utils.SecurityUtils
 *  com.dingyangmall.framework.web.service.SysLoginService
 *  com.dingyangmall.framework.web.service.SysPermissionService
 *  com.dingyangmall.system.service.ISysMenuService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginBody;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.framework.web.service.SysLoginService;
import com.dingyangmall.framework.web.service.SysPermissionService;
import com.dingyangmall.system.service.ISysMenuService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private SysPermissionService permissionService;

    @PostMapping(value={"/login"})
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        String token = this.loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        ajax.put("token", (Object)token);
        return ajax;
    }

    @GetMapping(value={"getInfo"})
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Set roles = this.permissionService.getRolePermission(user);
        Set permissions = this.permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", (Object)user);
        ajax.put("roles", (Object)roles);
        ajax.put("permissions", (Object)permissions);
        return ajax;
    }

    @GetMapping(value={"getRouters"})
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List menus = this.menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success((Object)this.menuService.buildMenus(menus));
    }
}

