package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.constant.HttpStatus;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryPrizeService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 抽奖管理 Controller
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@RestController
@AllArgsConstructor
@RequestMapping({"/mall/lottery", "/dev-api/mall/lottery"})
public class TbLotteryController extends BaseController {

    private final TbLotteryConfigService lotteryConfigService;
    private final TbLotteryRecordService lotteryRecordService;
    private final TbLotteryPrizeService lotteryPrizeService;

    /**
     * 获取当前抽奖配置（编辑用）
     * 优先返回开启的配置，否则返回最新一条；始终带奖品列表，无则返回默认空配置
     */
    @GetMapping("/config")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult getConfig() {
        TbLotteryConfig config = lotteryConfigService.getActiveConfig();
        if (config == null) {
            config = lotteryConfigService.getOne(Wrappers.<TbLotteryConfig>lambdaQuery()
                    .orderByDesc(TbLotteryConfig::getCreateTime)
                    .last("LIMIT 1"));
            if (config == null) {
                config = new TbLotteryConfig();
                config.setStatus("0");
                config.setCostPoints(10);
                config.setDailyLimit(1);
            }
        }
        // 有 id 时统一补全奖品列表（getActiveConfig 已带，其它分支可能未带）
        if (config.getId() != null) {
            config.setPrizeList(lotteryPrizeService.lambdaQuery()
                    .eq(TbLotteryPrize::getConfigId, config.getId())
                    .orderByAsc(TbLotteryPrize::getSortOrder)
                    .list());
        }
        return AjaxResult.success(config);
    }

    /**
     * 分页查询抽奖配置列表
     */
    @GetMapping("/config/page")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public TableDataInfo getConfigPage(Page<TbLotteryConfig> page) {
        com.baomidou.mybatisplus.core.metadata.IPage<TbLotteryConfig> result = lotteryConfigService.page(page,
                Wrappers.<TbLotteryConfig>lambdaQuery().orderByDesc(TbLotteryConfig::getCreateTime));
        TableDataInfo rsp = new TableDataInfo();
        rsp.setCode(HttpStatus.SUCCESS);
        rsp.setMsg("查询成功");
        rsp.setRows(result.getRecords());
        rsp.setTotal(result.getTotal());
        return rsp;
    }

    /**
     * 按 id 查询抽奖配置（含奖品列表）
     */
    @GetMapping("/config/{id}")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult getConfigById(@PathVariable Long id) {
        TbLotteryConfig config = lotteryConfigService.getById(id);
        if (config == null) {
            return AjaxResult.error("配置不存在");
        }
        config.setPrizeList(lotteryPrizeService.lambdaQuery()
                .eq(TbLotteryPrize::getConfigId, id)
                .orderByAsc(TbLotteryPrize::getSortOrder)
                .list());
        return AjaxResult.success(config);
    }

    /**
     * 新增或更新抽奖配置（可带奖品列表，更新时以传入的 prizeList 为准覆盖）
     */
    @PostMapping("/config")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult saveConfig(@RequestBody TbLotteryConfig config) {
        if (config.getId() == null && "1".equals(config.getStatus())) {
            lotteryConfigService.update(Wrappers.<TbLotteryConfig>lambdaUpdate()
                    .set(TbLotteryConfig::getStatus, "0")
                    .eq(TbLotteryConfig::getStatus, "1"));
        }
        return toAjax(lotteryConfigService.saveConfig(config));
    }

    /**
     * 删除抽奖配置（级联删除其下奖品）
     */
    @DeleteMapping("/config/{id}")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult removeConfig(@PathVariable Long id) {
        return toAjax(lotteryConfigService.removeConfigById(id));
    }

    /**
     * 分页查询抽奖记录
     * @param page 分页对象
     * @param tbLotteryRecord 抽奖记录查询条件
     * @return 抽奖记录列表
     */
    @GetMapping("/record/page")
    @PreAuthorize("@ss.hasPermi('mall:lottery:record')")
    public TableDataInfo getRecordPage(Page<TbLotteryRecord> page, TbLotteryRecord tbLotteryRecord) {
        com.baomidou.mybatisplus.core.metadata.IPage<TbLotteryRecord> result = lotteryRecordService.page(page, Wrappers.query(tbLotteryRecord).lambda().orderByDesc(TbLotteryRecord::getCreateTime));
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(result.getRecords());
        rspData.setTotal(result.getTotal());
        return rspData;
    }

    /** 奖品列表（按配置ID） */
    @GetMapping("/prize/list")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult prizeList(@RequestParam(required = false) Long configId) {
        if (configId == null) {
            return AjaxResult.success(lotteryPrizeService.list());
        }
        return AjaxResult.success(lotteryPrizeService.lambdaQuery().eq(TbLotteryPrize::getConfigId, configId).orderByAsc(TbLotteryPrize::getSortOrder).list());
    }

    /** 新增奖品 */
    @PostMapping("/prize")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult addPrize(@RequestBody TbLotteryPrize prize) {
        return toAjax(lotteryPrizeService.save(prize));
    }

    /** 修改奖品 */
    @PutMapping("/prize")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult updatePrize(@RequestBody TbLotteryPrize prize) {
        return toAjax(lotteryPrizeService.updateById(prize));
    }

    /** 删除奖品 */
    @DeleteMapping("/prize/{id}")
    @PreAuthorize("@ss.hasPermi('mall:lottery:config')")
    public AjaxResult removePrize(@PathVariable Long id) {
        return toAjax(lotteryPrizeService.removeById(id));
    }
}
