/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 *  com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.toolkit.Wrappers
 *  com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.core.page.TableDataInfo
 *  com.dingyangmall.mall.entity.TbLotteryConfig
 *  com.dingyangmall.mall.entity.TbLotteryPrize
 *  com.dingyangmall.mall.entity.TbLotteryRecord
 *  com.dingyangmall.mall.service.TbLotteryConfigService
 *  com.dingyangmall.mall.service.TbLotteryPrizeService
 *  com.dingyangmall.mall.service.TbLotteryRecordService
 *  lombok.Generated
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryPrizeService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import java.io.Serializable;
import lombok.Generated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/mall/lottery", "/dev-api/mall/lottery"})
public class TbLotteryController
extends BaseController {
    private final TbLotteryConfigService lotteryConfigService;
    private final TbLotteryRecordService lotteryRecordService;
    private final TbLotteryPrizeService lotteryPrizeService;

    @GetMapping(value={"/config"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult getConfig() {
        TbLotteryConfig config = this.lotteryConfigService.getActiveConfig();
        if (config == null && (config = (TbLotteryConfig)this.lotteryConfigService.getOne((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().orderByDesc(TbLotteryConfig::getCreateTime)).last("LIMIT 1"))) == null) {
            config = new TbLotteryConfig();
            config.setStatus("0");
            config.setCostPoints(Integer.valueOf(10));
            config.setDailyLimit(Integer.valueOf(1));
        }
        if (config.getId() != null) {
            config.setPrizeList(((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)this.lotteryPrizeService.lambdaQuery().eq(TbLotteryPrize::getConfigId, (Object)config.getId())).orderByAsc(TbLotteryPrize::getSortOrder)).list());
        }
        return AjaxResult.success((Object)config);
    }

    @GetMapping(value={"/config/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public TableDataInfo getConfigPage(Page<TbLotteryConfig> page) {
        IPage result = this.lotteryConfigService.page(page, (Wrapper)Wrappers.lambdaQuery().orderByDesc(TbLotteryConfig::getCreateTime));
        TableDataInfo rsp = new TableDataInfo();
        rsp.setCode(200);
        rsp.setMsg("\u67e5\u8be2\u6210\u529f");
        rsp.setRows(result.getRecords());
        rsp.setTotal(result.getTotal());
        return rsp;
    }

    @GetMapping(value={"/config/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult getConfigById(@PathVariable Long id) {
        TbLotteryConfig config = (TbLotteryConfig)this.lotteryConfigService.getById((Serializable)id);
        if (config == null) {
            return AjaxResult.error((String)"\u914d\u7f6e\u4e0d\u5b58\u5728");
        }
        config.setPrizeList(((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)this.lotteryPrizeService.lambdaQuery().eq(TbLotteryPrize::getConfigId, (Object)id)).orderByAsc(TbLotteryPrize::getSortOrder)).list());
        return AjaxResult.success((Object)config);
    }

    @PostMapping(value={"/config"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult saveConfig(@RequestBody TbLotteryConfig config) {
        if (config.getId() == null && "1".equals(config.getStatus())) {
            this.lotteryConfigService.update((Wrapper)((LambdaUpdateWrapper)Wrappers.lambdaUpdate().set(TbLotteryConfig::getStatus, (Object)"0")).eq(TbLotteryConfig::getStatus, (Object)"1"));
        }
        return this.toAjax(this.lotteryConfigService.saveConfig(config));
    }

    @DeleteMapping(value={"/config/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult removeConfig(@PathVariable Long id) {
        return this.toAjax(this.lotteryConfigService.removeConfigById(id));
    }

    @GetMapping(value={"/record/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:record')")
    public TableDataInfo getRecordPage(Page<TbLotteryRecord> page, TbLotteryRecord tbLotteryRecord) {
        IPage result = this.lotteryRecordService.page(page, (Wrapper)Wrappers.query((Object)tbLotteryRecord).lambda().orderByDesc(TbLotteryRecord::getCreateTime));
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("\u67e5\u8be2\u6210\u529f");
        rspData.setRows(result.getRecords());
        rspData.setTotal(result.getTotal());
        return rspData;
    }

    @GetMapping(value={"/prize/list"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult prizeList(@RequestParam(required=false) Long configId) {
        if (configId == null) {
            return AjaxResult.success((Object)this.lotteryPrizeService.list());
        }
        return AjaxResult.success((Object)((LambdaQueryChainWrapper)((LambdaQueryChainWrapper)this.lotteryPrizeService.lambdaQuery().eq(TbLotteryPrize::getConfigId, (Object)configId)).orderByAsc(TbLotteryPrize::getSortOrder)).list());
    }

    @PostMapping(value={"/prize"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult addPrize(@RequestBody TbLotteryPrize prize) {
        return this.toAjax(this.lotteryPrizeService.save((Object)prize));
    }

    @PutMapping(value={"/prize"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult updatePrize(@RequestBody TbLotteryPrize prize) {
        return this.toAjax(this.lotteryPrizeService.updateById((Object)prize));
    }

    @DeleteMapping(value={"/prize/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:lottery:config')")
    public AjaxResult removePrize(@PathVariable Long id) {
        return this.toAjax(this.lotteryPrizeService.removeById((Serializable)id));
    }

    @Generated
    public TbLotteryController(TbLotteryConfigService lotteryConfigService, TbLotteryRecordService lotteryRecordService, TbLotteryPrizeService lotteryPrizeService) {
        this.lotteryConfigService = lotteryConfigService;
        this.lotteryRecordService = lotteryRecordService;
        this.lotteryPrizeService = lotteryPrizeService;
    }
}

