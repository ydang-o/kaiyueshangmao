package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralRule;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 积分规则
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({"/integralrule", "/dev-api/integralrule"})
public class TbIntegralRuleController extends BaseController {

    private final TbIntegralRuleService tbIntegralRuleService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param tbIntegralRule 积分规则
     * @return
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:integralrule:index')")
    public AjaxResult getTbIntegralRulePage(Page page, TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(tbIntegralRuleService.page(page, Wrappers.query(tbIntegralRule)));
    }

    /**
     * 列表查询（不分页，用于下拉等）
     */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('mall:integralrule:index')")
    public AjaxResult list(TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(tbIntegralRuleService.list(Wrappers.query(tbIntegralRule)));
    }

    /**
     * 通过id查询积分规则
     * @param id id
     * @return AjaxResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:integralrule:get')")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(tbIntegralRuleService.getById(id));
    }

    /**
     * 新增积分规则
     * @param tbIntegralRule 积分规则
     * @return AjaxResult
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:integralrule:add')")
    public AjaxResult save(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(tbIntegralRuleService.save(tbIntegralRule));
    }

    /**
     * 修改积分规则
     * @param tbIntegralRule 积分规则
     * @return AjaxResult
     */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:integralrule:edit')")
    public AjaxResult updateById(@RequestBody TbIntegralRule tbIntegralRule) {
        return AjaxResult.success(tbIntegralRuleService.updateById(tbIntegralRule));
    }

    /**
     * 通过id删除积分规则
     * @param id id
     * @return AjaxResult
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:integralrule:del')")
    public AjaxResult removeById(@PathVariable Long id) {
        return AjaxResult.success(tbIntegralRuleService.removeById(id));
    }
}
