/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.monitor;

import com.dingyangmall.common.annotation.Log;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.enums.BusinessType;
import com.dingyangmall.common.utils.poi.ExcelUtil;
import com.dingyangmall.framework.web.service.SysPasswordService;
import com.dingyangmall.system.domain.SysLogininfor;
import com.dingyangmall.system.service.ISysLogininforService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/logininfor"})
public class SysLogininforController
extends BaseController {
    @Autowired
    private ISysLogininforService logininforService;
    @Autowired
    private SysPasswordService passwordService;

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysLogininfor logininfor) {
        this.startPage();
        List<SysLogininfor> list = this.logininforService.selectLogininforList(logininfor);
        return this.getDataTable(list);
    }

    @Log(title="\u767b\u5f55\u65e5\u5fd7", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysLogininfor logininfor) {
        List<SysLogininfor> list = this.logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        util.exportExcel(response, list, "\u767b\u5f55\u65e5\u5fd7");
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title="\u767b\u5f55\u65e5\u5fd7", businessType=BusinessType.DELETE)
    @DeleteMapping(value={"/{infoIds}"})
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return this.toAjax(this.logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title="\u767b\u5f55\u65e5\u5fd7", businessType=BusinessType.CLEAN)
    @DeleteMapping(value={"/clean"})
    public AjaxResult clean() {
        this.logininforService.cleanLogininfor();
        return this.success();
    }

    @PreAuthorize(value="@ss.hasPermi('monitor:logininfor:unlock')")
    @Log(title="\u8d26\u6237\u89e3\u9501", businessType=BusinessType.OTHER)
    @GetMapping(value={"/unlock/{userName}"})
    public AjaxResult unlock(@PathVariable(value="userName") String userName) {
        this.passwordService.clearLoginRecordCache(userName);
        return this.success();
    }
}

