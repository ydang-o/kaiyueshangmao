/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.page.TableDataInfo
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.system.domain.SysConfig
 *  com.dingyangmall.system.service.ISysConfigService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.system.domain.SysConfig;
import com.dingyangmall.system.service.ISysConfigService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/system/config"})
public class SysConfigController
extends BaseController {
    @Autowired
    private ISysConfigService configService;

    @PreAuthorize(value="@ss.hasPermi('system:config:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysConfig config) {
        this.startPage();
        List list = this.configService.selectConfigList(config);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:query')")
    @GetMapping(value={"/{configId}"})
    public AjaxResult getInfo(@PathVariable Long configId) {
        return this.success(this.configService.selectConfigById(configId));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:query')")
    @GetMapping(value={"/configKey/{configKey}"})
    public AjaxResult getByKey(@PathVariable String configKey) {
        String value = this.configService.selectConfigByKey(configKey);
        return this.success(StringUtils.isNotEmpty((String)value) ? value : "");
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SysConfig config) {
        if (!this.configService.checkConfigKeyUnique(config)) {
            return this.error("\u65b0\u589e\u53c2\u6570'" + config.getConfigName() + "'\u5931\u8d25\uff0c\u53c2\u6570\u952e\u540d\u5df2\u5b58\u5728");
        }
        return this.toAjax(this.configService.insertConfig(config));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SysConfig config) {
        if (!this.configService.checkConfigKeyUnique(config)) {
            return this.error("\u4fee\u6539\u53c2\u6570'" + config.getConfigName() + "'\u5931\u8d25\uff0c\u53c2\u6570\u952e\u540d\u5df2\u5b58\u5728");
        }
        return this.toAjax(this.configService.updateConfig(config));
    }

    @PreAuthorize(value="@ss.hasPermi('system:config:remove')")
    @DeleteMapping(value={"/{configIds}"})
    public AjaxResult remove(@PathVariable Long[] configIds) {
        this.configService.deleteConfigByIds(configIds);
        return this.success();
    }
}

