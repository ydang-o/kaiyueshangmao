/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.exception.ServiceException;
import com.dingyangmall.common.exception.user.BlackListException;
import com.dingyangmall.common.exception.user.CaptchaException;
import com.dingyangmall.common.exception.user.CaptchaExpireException;
import com.dingyangmall.common.exception.user.UserNotExistsException;
import com.dingyangmall.common.exception.user.UserPasswordNotMatchException;
import com.dingyangmall.common.utils.DateUtils;
import com.dingyangmall.common.utils.MessageUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import com.dingyangmall.framework.manager.AsyncManager;
import com.dingyangmall.framework.manager.factory.AsyncFactory;
import com.dingyangmall.framework.security.context.AuthenticationContextHolder;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.system.service.ISysConfigService;
import com.dingyangmall.system.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysConfigService configService;

    public String login(String username, String password, String code, String uuid) {
        this.validateCaptcha(username, code, uuid);
        this.loginPreCheck(username, password);
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            authentication = this.authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("user.password.not.match", new Object[0]), new Object[0]));
                throw new UserPasswordNotMatchException();
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", e.getMessage(), new Object[0]));
            throw new ServiceException(e.getMessage());
        }
        finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Success", MessageUtils.message("user.login.success", new Object[0]), new Object[0]));
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        this.recordLoginInfo(loginUser.getUserId());
        return this.tokenService.createToken(loginUser);
    }

    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = "captcha_codes:" + StringUtils.nvl(uuid, "");
            String captcha = (String)this.redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("user.jcaptcha.expire", new Object[0]), new Object[0]));
                throw new CaptchaExpireException();
            }
            this.redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("user.jcaptcha.error", new Object[0]), new Object[0]));
                throw new CaptchaException();
            }
        }
    }

    public void loginPreCheck(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("not.null", new Object[0]), new Object[0]));
            throw new UserNotExistsException();
        }
        if (password.length() < 5 || password.length() > 20) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("user.password.not.match", new Object[0]), new Object[0]));
            throw new UserPasswordNotMatchException();
        }
        if (username.length() < 2 || username.length() > 20) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("user.password.not.match", new Object[0]), new Object[0]));
            throw new UserPasswordNotMatchException();
        }
        String blackStr = this.configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Error", MessageUtils.message("login.blocked", new Object[0]), new Object[0]));
            throw new BlackListException();
        }
    }

    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        this.userService.updateUserProfile(sysUser);
    }
}

