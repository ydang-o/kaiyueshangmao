/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.annotation.Log
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.domain.model.LoginUser
 *  com.dingyangmall.common.core.page.TableDataInfo
 *  com.dingyangmall.common.core.redis.RedisCache
 *  com.dingyangmall.common.enums.BusinessType
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.system.domain.SysUserOnline
 *  com.dingyangmall.system.service.ISysUserOnlineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.monitor;

import com.dingyangmall.common.annotation.Log;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.core.redis.RedisCache;
import com.dingyangmall.common.enums.BusinessType;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.system.domain.SysUserOnline;
import com.dingyangmall.system.service.ISysUserOnlineService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/online"})
public class SysUserOnlineController
extends BaseController {
    @Autowired
    private ISysUserOnlineService userOnlineService;
    @Autowired
    private RedisCache redisCache;

    @PreAuthorize(value="@ss.hasPermi('monitor:online:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(String ipaddr, String userName) {
        Collection keys = this.redisCache.keys("login_tokens:*");
        ArrayList<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys) {
            LoginUser user = (LoginUser)this.redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty((String)ipaddr) && StringUtils.isNotEmpty((String)userName)) {
                userOnlineList.add(this.userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                continue;
            }
            if (StringUtils.isNotEmpty((String)ipaddr)) {
                userOnlineList.add(this.userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                continue;
            }
            if (StringUtils.isNotEmpty((String)userName) && StringUtils.isNotNull((Object)user.getUser())) {
                userOnlineList.add(this.userOnlineService.selectOnlineByUserName(userName, user));
                continue;
            }
            userOnlineList.add(this.userOnlineService.loginUserToUserOnline(user));
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return this.getDataTable(userOnlineList);
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title="\u5728\u7ebf\u7528\u6237", businessType=BusinessType.FORCE)
    @DeleteMapping(value={"/{tokenId}"})
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        this.redisCache.deleteObject("login_tokens:" + tokenId);
        return this.success();
    }
}

