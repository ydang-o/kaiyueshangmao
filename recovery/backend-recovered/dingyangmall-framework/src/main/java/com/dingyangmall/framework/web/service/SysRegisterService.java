/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.service;

import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.RegisterBody;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.exception.user.CaptchaException;
import com.dingyangmall.common.exception.user.CaptchaExpireException;
import com.dingyangmall.common.utils.MessageUtils;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.manager.AsyncManager;
import com.dingyangmall.framework.manager.factory.AsyncFactory;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.system.service.ISysConfigService;
import com.dingyangmall.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SmsService smsService;

    public String registerDistributor(RegisterBody registerBody) {
        Object msg = "";
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        String phonenumber = registerBody.getPhonenumber();
        String smsCode = registerBody.getSmsCode();
        String inviteCode = registerBody.getInviteCode();
        if (!StringUtils.isNotEmpty(phonenumber) || !StringUtils.isNotEmpty(smsCode)) {
            return "\u624b\u673a\u53f7\u548c\u9a8c\u8bc1\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        }
        this.smsService.validateSmsCode(phonenumber, smsCode);
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setPhonenumber(phonenumber);
        sysUser.setNickName(username);
        if (StringUtils.isEmpty(username)) {
            msg = "\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        } else if (StringUtils.isEmpty(password)) {
            msg = "\u7528\u6237\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        } else if (username.length() < 2 || username.length() > 20) {
            msg = "\u8d26\u6237\u957f\u5ea6\u5fc5\u987b\u57282\u523020\u4e2a\u5b57\u7b26\u4e4b\u95f4";
        } else if (password.length() < 5 || password.length() > 20) {
            msg = "\u5bc6\u7801\u957f\u5ea6\u5fc5\u987b\u57285\u523020\u4e2a\u5b57\u7b26\u4e4b\u95f4";
        } else if (!this.userService.checkUserNameUnique(sysUser)) {
            msg = "\u4fdd\u5b58\u7528\u6237'" + username + "'\u5931\u8d25\uff0c\u6ce8\u518c\u8d26\u53f7\u5df2\u5b58\u5728";
        } else if (!this.userService.checkPhoneUnique(sysUser)) {
            msg = "\u4fdd\u5b58\u7528\u6237'" + username + "'\u5931\u8d25\uff0c\u624b\u673a\u53f7\u7801\u5df2\u5b58\u5728";
        } else {
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            if (StringUtils.isNotEmpty(inviteCode)) {
                try {
                    Long parentId = Long.parseLong(inviteCode);
                    SysUser parent = this.userService.selectUserById(parentId);
                    if (parent == null || !Integer.valueOf(1).equals(parent.getDealerLevel())) {
                        return "\u65e0\u6548\u7684\u9080\u8bf7\u7801\uff08\u4e0a\u7ea7\u5206\u9500\u5546\u4e0d\u5b58\u5728\u6216\u975e\u4e00\u7ea7\u5206\u9500\u5546\uff09";
                    }
                    sysUser.setDealerLevel(2);
                    sysUser.setParentDistributorId(parent.getUserId());
                }
                catch (NumberFormatException e) {
                    return "\u65e0\u6548\u7684\u9080\u8bf7\u7801\u683c\u5f0f";
                }
            } else {
                sysUser.setDealerLevel(1);
            }
            int rows = this.userService.insertUser(sysUser);
            if (rows <= 0) {
                msg = "\u6ce8\u518c\u5931\u8d25,\u8bf7\u8054\u7cfb\u7cfb\u7edf\u7ba1\u7406\u4eba\u5458";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Register", MessageUtils.message("user.register.success", new Object[0]), new Object[0]));
            }
        }
        return msg;
    }

    public String register(RegisterBody registerBody) {
        Object msg = "";
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        boolean captchaEnabled = this.configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            this.validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        if (StringUtils.isEmpty(username)) {
            msg = "\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a";
        } else if (StringUtils.isEmpty(password)) {
            msg = "\u7528\u6237\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a";
        } else if (username.length() < 2 || username.length() > 20) {
            msg = "\u8d26\u6237\u957f\u5ea6\u5fc5\u987b\u57282\u523020\u4e2a\u5b57\u7b26\u4e4b\u95f4";
        } else if (password.length() < 5 || password.length() > 20) {
            msg = "\u5bc6\u7801\u957f\u5ea6\u5fc5\u987b\u57285\u523020\u4e2a\u5b57\u7b26\u4e4b\u95f4";
        } else if (!this.userService.checkUserNameUnique(sysUser)) {
            msg = "\u4fdd\u5b58\u7528\u6237'" + username + "'\u5931\u8d25\uff0c\u6ce8\u518c\u8d26\u53f7\u5df2\u5b58\u5728";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = this.userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "\u6ce8\u518c\u5931\u8d25,\u8bf7\u8054\u7cfb\u7cfb\u7edf\u7ba1\u7406\u4eba\u5458";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, "Register", MessageUtils.message("user.register.success", new Object[0]), new Object[0]));
            }
        }
        return msg;
    }

    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = "captcha_codes:" + StringUtils.nvl(uuid, "");
        String captcha = (String)this.redisCache.getCacheObject(verifyKey);
        this.redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}

