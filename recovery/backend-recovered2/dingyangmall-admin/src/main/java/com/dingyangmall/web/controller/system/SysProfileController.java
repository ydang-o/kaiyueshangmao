/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.annotation.Log;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.enums.BusinessType;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.framework.web.service.TokenService;
import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.system.service.ISysUserService;
import com.dingyangmall.web.service.SysUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/system/user/profile"})
public class SysProfileController
extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SysUploadFileService sysUploadFileService;
    @Autowired
    private SmsService smsService;

    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = this.getLoginUser();
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", (Object)this.userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", (Object)this.userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }

    @Log(title="\u4e2a\u4eba\u4fe1\u606f", businessType=BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user, @RequestParam(required=false) String phoneSmsCode) {
        LoginUser loginUser = this.getLoginUser();
        SysUser currentUser = loginUser.getUser();
        String oldPhone = currentUser.getPhonenumber();
        String newPhone = user.getPhonenumber();
        if (StringUtils.isNotEmpty(newPhone) && StringUtils.isNotEmpty(oldPhone) && !oldPhone.equals(newPhone)) {
            if (StringUtils.isEmpty(phoneSmsCode)) {
                return this.error("\u4fee\u6539\u624b\u673a\u53f7\u9700\u8981\u8f93\u5165\u65e7\u624b\u673a\u6536\u5230\u7684\u9a8c\u8bc1\u7801");
            }
            try {
                this.smsService.validateSmsCode(oldPhone, phoneSmsCode);
            }
            catch (Exception e) {
                return this.error("\u9a8c\u8bc1\u7801\u9519\u8bef\u6216\u5df2\u5931\u6548");
            }
        }
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !this.userService.checkPhoneUnique(currentUser)) {
            return this.error("\u4fee\u6539\u7528\u6237'" + loginUser.getUsername() + "'\u5931\u8d25\uff0c\u624b\u673a\u53f7\u7801\u5df2\u5b58\u5728");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !this.userService.checkEmailUnique(currentUser)) {
            return this.error("\u4fee\u6539\u7528\u6237'" + loginUser.getUsername() + "'\u5931\u8d25\uff0c\u90ae\u7bb1\u8d26\u53f7\u5df2\u5b58\u5728");
        }
        if (this.userService.updateUserProfile(currentUser) > 0) {
            this.tokenService.setLoginUser(loginUser);
            return this.success();
        }
        return this.error("\u4fee\u6539\u4e2a\u4eba\u4fe1\u606f\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
    }

    @Log(title="\u4e2a\u4eba\u4fe1\u606f", businessType=BusinessType.UPDATE)
    @PutMapping(value={"/updatePwd"})
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = this.getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return this.error("\u4fee\u6539\u5bc6\u7801\u5931\u8d25\uff0c\u65e7\u5bc6\u7801\u9519\u8bef");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return this.error("\u65b0\u5bc6\u7801\u4e0d\u80fd\u4e0e\u65e7\u5bc6\u7801\u76f8\u540c");
        }
        if (this.userService.resetUserPwd(userName, newPassword = SecurityUtils.encryptPassword(newPassword)) > 0) {
            loginUser.getUser().setPassword(newPassword);
            this.tokenService.setLoginUser(loginUser);
            return this.success();
        }
        return this.error("\u4fee\u6539\u5bc6\u7801\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
    }

    @Log(title="\u7528\u6237\u5934\u50cf", businessType=BusinessType.UPDATE)
    @PostMapping(value={"/avatar"})
    public AjaxResult avatar(@RequestParam(value="avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = this.getLoginUser();
            SysUploadFile entity = this.sysUploadFileService.save(file);
            String avatar = "/profile/file/" + entity.getFileId();
            if (this.userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", (Object)avatar);
                loginUser.getUser().setAvatar(avatar);
                this.tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return this.error("\u4e0a\u4f20\u56fe\u7247\u5f02\u5e38\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
    }
}

