/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value={"/integralrule", "/dev-api/integralrule"})
public class TbIntegralRuleController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TbIntegralRuleController.class);
    private final TbIntegralRuleService tbIntegralRuleService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:index')")
    public AjaxResult getTbIntegralRulePage(Page page, TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(this.tbIntegralRuleService.page(page, Wrappers.query(tbIntegralRule)));
    }

    @GetMapping(value={"/list"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:index')")
    public AjaxResult list(TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(this.tbIntegralRuleService.list(Wrappers.query(tbIntegralRule)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:get')")
    public AjaxResult getById(@PathVariable(value="id") Long id) {
        return AjaxResult.success(this.tbIntegralRuleService.getById(id));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:add')")
    public AjaxResult save(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(this.tbIntegralRuleService.save(tbIntegralRule));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:edit')")
    public AjaxResult updateById(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(this.tbIntegralRuleService.updateById(tbIntegralRule));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:del')")
    public AjaxResult removeById(@PathVariable Long id) {
        return AjaxResult.success(this.tbIntegralRuleService.removeById(id));
    }

    @Generated
    public TbIntegralRuleController(TbIntegralRuleService tbIntegralRuleService) {
        this.tbIntegralRuleService = tbIntegralRuleService;
    }
}

