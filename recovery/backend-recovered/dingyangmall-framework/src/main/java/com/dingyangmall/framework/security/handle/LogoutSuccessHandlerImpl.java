/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.utils.MessageUtils;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.framework.manager.AsyncManager;
import com.dingyangmall.framework.manager.factory.AsyncFactory;
import com.dingyangmall.framework.web.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class LogoutSuccessHandlerImpl
implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = this.tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            this.tokenService.delLoginUser(loginUser.getToken());
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, "Logout", MessageUtils.message("user.logout.success", new Object[0]), new Object[0]));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success", new Object[0]))));
    }
}

