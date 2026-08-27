/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.domain.entity.SysDictData
 *  com.dingyangmall.common.core.page.TableDataInfo
 *  com.dingyangmall.system.service.ISysDictDataService
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
import com.dingyangmall.common.core.domain.entity.SysDictData;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.system.service.ISysDictDataService;
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
@RequestMapping(value={"/system/dict/data"})
public class SysDictDataController
extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @GetMapping(value={"/type/{dictType}"})
    public AjaxResult dictType(@PathVariable String dictType) {
        SysDictData dictData = new SysDictData();
        dictData.setStatus("0");
        dictData.setDictType(dictType);
        List list = this.dictDataService.selectDictDataList(dictData);
        return this.success(list);
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:list')")
    @GetMapping(value={"/list"})
    public TableDataInfo list(SysDictData dictData) {
        this.startPage();
        List list = this.dictDataService.selectDictDataList(dictData);
        return this.getDataTable(list);
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:query')")
    @GetMapping(value={"/{dictCode}"})
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return this.success(this.dictDataService.selectDictDataById(dictCode));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:add')")
    @PostMapping
    public AjaxResult add(@RequestBody SysDictData dictData) {
        return this.toAjax(this.dictDataService.insertDictData(dictData));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody SysDictData dictData) {
        return this.toAjax(this.dictDataService.updateDictData(dictData));
    }

    @PreAuthorize(value="@ss.hasPermi('system:dict:remove')")
    @DeleteMapping(value={"/{dictCodes}"})
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        return this.toAjax(this.dictDataService.deleteDictDataByIds(dictCodes));
    }
}

