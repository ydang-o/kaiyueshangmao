/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.core.controller;

import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.model.LoginUser;
import com.dingyangmall.common.core.page.PageDomain;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.core.page.TableSupport;
import com.dingyangmall.common.exception.DemoModeException;
import com.dingyangmall.common.utils.DateUtils;
import com.dingyangmall.common.utils.PageUtils;
import com.dingyangmall.common.utils.SecurityUtils;
import com.dingyangmall.common.utils.ServletUtils;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport(){

            @Override
            public void setAsText(String text) {
                this.setValue(DateUtils.parseDate(text));
            }
        });
    }

    protected void startPage() {
        PageUtils.startPage();
    }

    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    protected void clearPage() {
        PageUtils.clearPage();
    }

    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("\u67e5\u8be2\u6210\u529f");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    public AjaxResult success() {
        return AjaxResult.success();
    }

    public AjaxResult error() {
        return AjaxResult.error();
    }

    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    public AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }

    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    public AjaxResult warn(String message) {
        return AjaxResult.warn(message);
    }

    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    protected AjaxResult toAjax(boolean result) {
        return result ? this.success() : this.error();
    }

    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    public Long getUserId() {
        return this.getLoginUser().getUserId();
    }

    public Long getDeptId() {
        return this.getLoginUser().getDeptId();
    }

    public String getUsername() {
        return this.getLoginUser().getUsername();
    }

    @ModelAttribute
    public void init(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        if (DingyangmallConfig.isDemoEnabled()) {
            Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
            if (SecurityUtils.isAdmin(userId)) {
                return;
            }
            String url = ServletUtils.getRequest().getRequestURI();
            if (StringUtils.isNotEmpty(url) && (url.contains("/demo") || url.contains("/tool/gen"))) {
                return;
            }
            if ("DELETE".equals(httpServletRequest.getMethod()) || "POST".equals(httpServletRequest.getMethod()) || "PUT".equals(httpServletRequest.getMethod())) {
                throw new DemoModeException();
            }
        }
    }
}

