/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.mapper;

import com.dingyangmall.system.domain.SysRoleMenu;
import java.util.List;

public interface SysRoleMenuMapper {
    public int checkMenuExistRole(Long var1);

    public int deleteRoleMenuByRoleId(Long var1);

    public int deleteRoleMenu(Long[] var1);

    public int batchRoleMenu(List<SysRoleMenu> var1);
}

