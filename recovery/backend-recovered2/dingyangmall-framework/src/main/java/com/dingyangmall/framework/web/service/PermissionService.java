/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.entity.SysRole;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.security.context.PermissionContextHolder;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service(value="ss")
public class PermissionService {
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return this.hasPermissions(loginUser.getPermissions(), permission);
    }

    public boolean lacksPermi(String permission) {
        return !this.hasPermi(permission);
    }

    public boolean hasAnyPermi(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(",")) {
            if (permission == null || !this.hasPermissions(authorities, permission)) continue;
            return true;
        }
        return false;
    }

    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (SysRole sysRole : loginUser.getUser().getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (!"admin".equals(roleKey) && !roleKey.equals(StringUtils.trim(role))) continue;
            return true;
        }
        return false;
    }

    public boolean lacksRole(String role) {
        return !this.hasRole(role);
    }

    public boolean hasAnyRoles(String roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles())) {
            return false;
        }
        for (String role : roles.split(",")) {
            if (!this.hasRole(role)) continue;
            return true;
        }
        return false;
    }

    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains("*:*:*") || permissions.contains(StringUtils.trim(permission));
    }
}

