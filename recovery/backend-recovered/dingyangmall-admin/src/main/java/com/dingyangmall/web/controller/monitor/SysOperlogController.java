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
import com.dingyangmall.system.domain.SysOperLog;
import com.dingyangmall.system.service.ISysOperLogService;
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
@RequestMapping(value={"/monitor/operlog"})
public class SysOperlogController
extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysOperLog operLog) {
        this.startPage();
        List<SysOperLog> list = this.operLogService.selectOperLogList(operLog);
        return this.getDataTable(list);
    }

    @Log(title="\u64cd\u4f5c\u65e5\u5fd7", businessType=BusinessType.EXPORT)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:export')")
    @PostMapping(value={"/export"})
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List<SysOperLog> list = this.operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "\u64cd\u4f5c\u65e5\u5fd7");
    }

    @Log(title="\u64cd\u4f5c\u65e5\u5fd7", businessType=BusinessType.DELETE)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping(value={"/{operIds}"})
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return this.toAjax(this.operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title="\u64cd\u4f5c\u65e5\u5fd7", businessType=BusinessType.CLEAN)
    @PreAuthorize(value="@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping(value={"/clean"})
    public AjaxResult clean() {
        this.operLogService.cleanOperLog();
        return this.success();
    }
}

