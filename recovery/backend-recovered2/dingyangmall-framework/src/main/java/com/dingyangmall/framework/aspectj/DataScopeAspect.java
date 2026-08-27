/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.aspectj;

import com.dingyangmall.common.annotation.DataScope;
import com.dingyangmall.common.core.domain.BaseEntity;
import com.dingyangmall.common.core.domain.entity.SysRole;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.core.text.Convert;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.security.context.PermissionContextHolder;
import java.util.ArrayList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataScopeAspect {
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";
    public static final String DATA_SCOPE = "dataScope";

    @Before(value="@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        this.clearDataScope(point);
        this.handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(JoinPoint joinPoint, DataScope controllerDataScope) {
        SysUser currentUser;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotNull(currentUser = loginUser.getUser()) && !currentUser.isAdmin()) {
            String permission = StringUtils.defaultIfEmpty(controllerDataScope.permission(), PermissionContextHolder.getContext());
            DataScopeAspect.dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(), controllerDataScope.userAlias(), permission);
        }
    }

    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String permission) {
        Object params;
        StringBuilder sqlString = new StringBuilder();
        ArrayList<String> conditions = new ArrayList<String>();
        ArrayList scopeCustomIds = new ArrayList();
        user.getRoles().forEach(role -> {
            if (DATA_SCOPE_CUSTOM.equals(role.getDataScope()) && StringUtils.containsAny(role.getPermissions(), Convert.toStrArray(permission))) {
                scopeCustomIds.add(Convert.toStr(role.getRoleId()));
            }
        });
        for (SysRole role2 : user.getRoles()) {
            String dataScope = role2.getDataScope();
            if (conditions.contains(dataScope) || !StringUtils.containsAny(role2.getPermissions(), Convert.toStrArray(permission))) continue;
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                conditions.add(dataScope);
                break;
            }
            if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                if (scopeCustomIds.size() > 1) {
                    sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id in ({}) ) ", deptAlias, String.join((CharSequence)",", scopeCustomIds)));
                } else {
                    sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias, role2.getRoleId()));
                }
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(StringUtils.format(" OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )", deptAlias, user.getDeptId(), user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.user_id = {} ", userAlias, user.getUserId()));
                } else {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
                }
            }
            conditions.add(dataScope);
        }
        if (StringUtils.isEmpty(conditions)) {
            sqlString.append(StringUtils.format(" OR {}.dept_id = 0 ", deptAlias));
        }
        if (StringUtils.isNotBlank(sqlString.toString()) && StringUtils.isNotNull(params = joinPoint.getArgs()[0]) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity)params;
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    private void clearDataScope(JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity)params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}

