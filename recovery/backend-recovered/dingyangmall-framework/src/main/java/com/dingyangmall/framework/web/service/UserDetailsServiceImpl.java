/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.enums.UserStatus;
import com.dingyangmall.common.exception.ServiceException;
import com.dingyangmall.common.utils.MessageUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SysPasswordService;
import com.dingyangmall.framework.web.service.SysPermissionService;
import com.dingyangmall.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl
implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = this.userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("\u767b\u5f55\u7528\u6237\uff1a{} \u4e0d\u5b58\u5728.", (Object)username);
            throw new ServiceException(MessageUtils.message("user.not.exists", new Object[0]));
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("\u767b\u5f55\u7528\u6237\uff1a{} \u5df2\u88ab\u5220\u9664.", (Object)username);
            throw new ServiceException(MessageUtils.message("user.password.delete", new Object[0]));
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("\u767b\u5f55\u7528\u6237\uff1a{} \u5df2\u88ab\u505c\u7528.", (Object)username);
            throw new ServiceException(MessageUtils.message("user.blocked", new Object[0]));
        }
        this.passwordService.validate(user);
        return this.createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, this.permissionService.getMenuPermission(user));
    }
}

