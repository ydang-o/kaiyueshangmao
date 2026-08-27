/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.mall.entity.TbIntegralRule
 *  com.dingyangmall.mall.service.TbIntegralRuleService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
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
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import java.io.Serializable;
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
        return AjaxResult.success((Object)this.tbIntegralRuleService.page((IPage)page, (Wrapper)Wrappers.query((Object)tbIntegralRule)));
    }

    @GetMapping(value={"/list"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:index')")
    public AjaxResult list(TbIntegralRule tbIntegralRule) {
        return AjaxResult.success((Object)this.tbIntegralRuleService.list((Wrapper)Wrappers.query((Object)tbIntegralRule)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:get')")
    public AjaxResult getById(@PathVariable(value="id") Long id) {
        return AjaxResult.success((Object)this.tbIntegralRuleService.getById((Serializable)id));
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:add')")
    public AjaxResult save(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success((Object)this.tbIntegralRuleService.save((Object)tbIntegralRule));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:edit')")
    public AjaxResult updateById(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success((Object)this.tbIntegralRuleService.updateById((Object)tbIntegralRule));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:integralrule:del')")
    public AjaxResult removeById(@PathVariable Long id) {
        return AjaxResult.success((Object)this.tbIntegralRuleService.removeById((Serializable)id));
    }

    @Generated
    public TbIntegralRuleController(TbIntegralRuleService tbIntegralRuleService) {
        this.tbIntegralRuleService = tbIntegralRuleService;
    }
}

