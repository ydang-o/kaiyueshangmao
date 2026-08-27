/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.utils;

import com.dingyangmall.common.core.page.PageDomain;
import com.dingyangmall.common.core.page.TableSupport;
import com.dingyangmall.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;

public class PageUtils
extends PageHelper {
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage((int)pageNum, (int)pageSize, orderBy).setReasonable(reasonable);
    }

    public static void clearPage() {
        PageHelper.clearPage();
    }
}

