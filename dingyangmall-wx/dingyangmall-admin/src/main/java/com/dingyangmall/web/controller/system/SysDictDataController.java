package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysDictData;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
 @Autowired private ISysDictDataService service;
 @GetMapping("/type/{dictType}") public AjaxResult type(@PathVariable String dictType){SysDictData q=new SysDictData();q.setDictType(dictType);q.setStatus("0");return AjaxResult.success(service.selectDictDataList(q));}
 @GetMapping("/list") @PreAuthorize("@ss.hasPermi('system:dict:list')") public TableDataInfo list(SysDictData q){startPage();List<SysDictData> list=service.selectDictDataList(q);return getDataTable(list);}
 @GetMapping("/{dictCode}") @PreAuthorize("@ss.hasPermi('system:dict:query')") public AjaxResult get(@PathVariable Long dictCode){return AjaxResult.success(service.selectDictDataById(dictCode));}
 @PostMapping @PreAuthorize("@ss.hasPermi('system:dict:add')") public AjaxResult add(@RequestBody SysDictData q){return toAjax(service.insertDictData(q));}
 @PutMapping @PreAuthorize("@ss.hasPermi('system:dict:edit')") public AjaxResult edit(@RequestBody SysDictData q){return toAjax(service.updateDictData(q));}
 @DeleteMapping("/{dictCodes}") @PreAuthorize("@ss.hasPermi('system:dict:remove')") public AjaxResult remove(@PathVariable Long[] dictCodes){service.deleteDictDataByIds(dictCodes);return AjaxResult.success();}
}
