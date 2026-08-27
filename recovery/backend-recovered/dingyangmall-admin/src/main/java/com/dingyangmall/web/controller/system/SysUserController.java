/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.annotation.RepeatSubmit;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.web.service.SmsService;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.system.service.ISysUserService;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/user"})
public class SysUserController
extends BaseController {
    private static final String DEFAULT_DEALER_PASSWORD = "123456";
    @Autowired
    private ISysUserService userService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UmsMemberService umsMemberService;

    @PreAuthorize(value="@ss.hasPermi('system:user:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysUser user) {
        this.startPage();
        List<SysUser> list = this.userService.selectUserList(user);
        ArrayList rows = new ArrayList();
        for (SysUser u : list) {
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("userId", u.getUserId());
            row.put("userName", u.getUserName());
            row.put("nickName", u.getNickName());
            row.put("phonenumber", u.getPhonenumber());
            row.put("status", u.getStatus());
            row.put("dealerLevel", u.getDealerLevel());
            row.put("parentDistributorId", u.getParentDistributorId());
            row.put("deptId", u.getDeptId());
            row.put("createTime", u.getCreateTime());
            row.put("inviteCode", u.getInviteCode());
            if (StringUtils.isNotEmpty(u.getPhonenumber())) {
                UmsMember member = this.umsMemberService.getByPhone(u.getPhonenumber());
                row.put("points", member != null ? member.getPoints() : null);
                row.put("balance", member != null ? member.getBalance() : null);
                row.put("memberCode", member != null ? member.getMemberCode() : null);
            } else {
                row.put("points", null);
                row.put("balance", null);
                row.put("memberCode", null);
            }
            if (u.getParentDistributorId() != null) {
                SysUser parent = this.userService.selectUserById(u.getParentDistributorId());
                row.put("parentDistributorName", parent != null ? parent.getNickName() : null);
            } else {
                row.put("parentDistributorName", null);
            }
            rows.add(row);
        }
        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setCode(200);
        dataInfo.setMsg("\u67e5\u8be2\u6210\u529f");
        dataInfo.setRows(rows);
        dataInfo.setTotal(new PageInfo<SysUser>(list).getTotal());
        return dataInfo;
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:query')")
    @GetMapping(value={"/{userId}"})
    public AjaxResult getInfo(@PathVariable Long userId) {
        SysUser user = this.userService.selectUserById(userId);
        if (user == null) {
            return AjaxResult.error("\u7528\u6237\u4e0d\u5b58\u5728");
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user", user);
        if (user.getParentDistributorId() != null) {
            SysUser parent = this.userService.selectUserById(user.getParentDistributorId());
            data.put("parentDistributorName", parent != null ? parent.getNickName() : null);
        }
        return AjaxResult.success(data);
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user) {
        String rawPassword;
        Long currentUserId = SecurityUtils.getUserId();
        SysUser currentUser = this.userService.selectUserById(currentUserId);
        if (currentUser == null) {
            return AjaxResult.error("\u5f53\u524d\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 2) {
            return AjaxResult.error("\u60a8\u6ca1\u6709\u6743\u9650\u521b\u5efa\u7ecf\u9500\u5546");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 1) {
            if (user.getDealerLevel() == null || user.getDealerLevel() != 2) {
                return AjaxResult.error("\u4e00\u7ea7\u7ecf\u9500\u5546\u53ea\u80fd\u521b\u5efa\u4e8c\u7ea7\u7ecf\u9500\u5546");
            }
            user.setParentDistributorId(currentUserId);
        }
        if (StringUtils.isEmpty(user.getUserName()) && StringUtils.isNotEmpty(user.getPhonenumber())) {
            user.setUserName(user.getPhonenumber());
        }
        if (StringUtils.isEmpty(user.getUserName())) {
            return AjaxResult.error("\u7528\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a");
        }
        SysUser existingUser = this.userService.selectUserByUserName(user.getUserName());
        if (existingUser != null) {
            return AjaxResult.error("\u7528\u6237\u540d\u5df2\u5b58\u5728");
        }
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !this.userService.checkPhoneUnique(user)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u5df2\u88ab\u5176\u4ed6\u7528\u6237\u4f7f\u7528");
        }
        if (user.getPhonenumber() != null && user.getSmsCode() != null) {
            try {
                this.smsService.validateSmsCode(user.getPhonenumber(), user.getSmsCode());
            }
            catch (Exception e) {
                return AjaxResult.error("\u77ed\u4fe1\u9a8c\u8bc1\u7801\u9519\u8bef\u6216\u5df2\u8fc7\u671f");
            }
        }
        if (StringUtils.isEmpty(rawPassword = user.getPassword())) {
            rawPassword = DEFAULT_DEALER_PASSWORD;
        }
        user.setPassword(SecurityUtils.encryptPassword(rawPassword));
        if (user.getDealerLevel() != null && user.getDealerLevel() > 0 && StringUtils.isEmpty(user.getInviteCode())) {
            return AjaxResult.error("\u9080\u8bf7\u7801\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u624b\u52a8\u586b\u5199\u9080\u8bf7\u7801");
        }
        user.setCreateBy(currentUser.getUserName());
        int result = this.userService.insertUser(user);
        if (result > 0) {
            if (DEFAULT_DEALER_PASSWORD.equals(rawPassword)) {
                return AjaxResult.success("\u521b\u5efa\u6210\u529f\uff0c\u521d\u59cb\u5bc6\u7801\u4e3a\uff1a123456\uff0c\u8bf7\u9996\u6b21\u767b\u5f55\u540e\u4fee\u6539\u5bc6\u7801");
            }
            return AjaxResult.success("\u521b\u5efa\u6210\u529f");
        }
        return AjaxResult.error("\u521b\u5efa\u5931\u8d25");
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SysUser user) {
        Long currentUserId = SecurityUtils.getUserId();
        SysUser currentUser = this.userService.selectUserById(currentUserId);
        if (currentUser == null) {
            return AjaxResult.error("\u5f53\u524d\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 2 && !currentUserId.equals(user.getUserId())) {
            return AjaxResult.error("\u60a8\u6ca1\u6709\u6743\u9650\u4fee\u6539\u5176\u4ed6\u7ecf\u9500\u5546");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 1) {
            SysUser targetUser = this.userService.selectUserById(user.getUserId());
            if (targetUser == null) {
                return AjaxResult.error("\u76ee\u6807\u7528\u6237\u4e0d\u5b58\u5728");
            }
            if (targetUser.getDealerLevel() != null && targetUser.getDealerLevel() == 2) {
                if (!currentUserId.equals(targetUser.getParentDistributorId())) {
                    return AjaxResult.error("\u60a8\u53ea\u80fd\u4fee\u6539\u81ea\u5df1\u521b\u5efa\u7684\u4e8c\u7ea7\u7ecf\u9500\u5546");
                }
            } else if (!currentUserId.equals(user.getUserId())) {
                return AjaxResult.error("\u60a8\u6ca1\u6709\u6743\u9650\u4fee\u6539\u8be5\u7528\u6237");
            }
        }
        user.setUserName(null);
        user.setCreateBy(null);
        user.setCreateTime(null);
        user.setUpdateBy(currentUser.getUserName());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !this.userService.checkPhoneUnique(user)) {
            return AjaxResult.error("\u624b\u673a\u53f7\u5df2\u88ab\u5176\u4ed6\u7528\u6237\u4f7f\u7528");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }
        int result = this.userService.updateUser(user);
        if (result > 0) {
            return AjaxResult.success("\u4fee\u6539\u6210\u529f");
        }
        return AjaxResult.error("\u4fee\u6539\u5931\u8d25");
    }

    @PreAuthorize(value="@ss.hasPermi('system:user:remove')")
    @DeleteMapping(value={"/{userId}"})
    public AjaxResult remove(@PathVariable Long userId) {
        int result;
        Long currentUserId = SecurityUtils.getUserId();
        SysUser currentUser = this.userService.selectUserById(currentUserId);
        if (currentUser == null) {
            return AjaxResult.error("\u5f53\u524d\u7528\u6237\u4e0d\u5b58\u5728");
        }
        if (currentUserId.equals(userId)) {
            return AjaxResult.error("\u4e0d\u80fd\u5220\u9664\u5f53\u524d\u767b\u5f55\u7528\u6237");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 2) {
            return AjaxResult.error("\u60a8\u6ca1\u6709\u6743\u9650\u5220\u9664\u7ecf\u9500\u5546");
        }
        if (currentUser.getDealerLevel() != null && currentUser.getDealerLevel() == 1) {
            SysUser targetUser = this.userService.selectUserById(userId);
            if (targetUser == null) {
                return AjaxResult.error("\u76ee\u6807\u7528\u6237\u4e0d\u5b58\u5728");
            }
            if (targetUser.getDealerLevel() == null || targetUser.getDealerLevel() != 2) {
                return AjaxResult.error("\u60a8\u53ea\u80fd\u5220\u9664\u4e8c\u7ea7\u7ecf\u9500\u5546");
            }
            if (!currentUserId.equals(targetUser.getParentDistributorId())) {
                return AjaxResult.error("\u60a8\u53ea\u80fd\u5220\u9664\u81ea\u5df1\u521b\u5efa\u7684\u4e8c\u7ea7\u7ecf\u9500\u5546");
            }
        }
        if ((result = this.userService.deleteUserById(userId)) > 0) {
            return AjaxResult.success("\u5220\u9664\u6210\u529f");
        }
        return AjaxResult.error("\u5220\u9664\u5931\u8d25");
    }

    @RepeatSubmit(interval=60000, message="\u8bf760\u79d2\u540e\u518d\u8bd5")
    @GetMapping(value={"/send-sms-code"})
    public AjaxResult sendSmsCode(@RequestParam String phone) {
        if (phone == null || phone.length() != 11) {
            return AjaxResult.error("\u624b\u673a\u53f7\u683c\u5f0f\u4e0d\u6b63\u786e");
        }
        this.smsService.sendSmsCode(phone);
        return AjaxResult.success("\u9a8c\u8bc1\u7801\u53d1\u9001\u6210\u529f");
    }

    private String generateInviteCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        sb.append(chars.charAt(random.nextInt(26)));
        sb.append(chars.charAt(26 + random.nextInt(26)));
        sb.append(chars.charAt(52 + random.nextInt(10)));
        for (int i = 0; i < 3; ++i) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        char[] arr = sb.toString().toCharArray();
        for (int i = arr.length - 1; i > 0; --i) {
            int j = random.nextInt(i + 1);
            char temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return new String(arr);
    }
}

