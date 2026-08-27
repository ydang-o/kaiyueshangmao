/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.manager.factory;

import com.dingyangmall.common.utils.LogUtils;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.ip.AddressUtils;
import com.dingyangmall.common.utils.ip.IpUtils;
import com.dingyangmall.common.utils.spring.SpringUtils;
import com.dingyangmall.system.domain.SysLogininfor;
import com.dingyangmall.system.domain.SysOperLog;
import com.dingyangmall.system.service.ISysLogininforService;
import com.dingyangmall.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    public static TimerTask recordLogininfor(final String username, final String status, final String message, final Object ... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask(){

            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                sys_user_logger.info(s.toString(), args);
                String os = userAgent.getOperatingSystem().getName();
                String browser = userAgent.getBrowser().getName();
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                if (StringUtils.equalsAny(status, "Success", "Logout", "Register")) {
                    logininfor.setStatus("0");
                } else if ("Error".equals(status)) {
                    logininfor.setStatus("1");
                }
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask(){

            @Override
            public void run() {
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}

