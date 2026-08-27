/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.entity.SysRole;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.system.service.ISysMenuService;
import com.dingyangmall.system.service.ISysRoleService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SysPermissionService {
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysMenuService menuService;

    public Set<String> getRolePermission(SysUser user) {
        HashSet<String> roles = new HashSet<String>();
        if (user.isAdmin()) {
            roles.add("admin");
        } else if (user.getDealerLevel() != null) {
            roles.add("dealer");
        } else {
            roles.addAll(this.roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    public Set<String> getMenuPermission(SysUser user) {
        HashSet<String> perms = new HashSet<String>();
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else if (user.getDealerLevel() != null) {
            perms.add("*:*:*");
        } else {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                for (SysRole role : roles) {
                    Set<String> rolePerms = this.menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(this.menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}

