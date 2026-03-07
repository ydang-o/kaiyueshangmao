package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbLotteryConfig;

/**
 * 抽奖配置 Service 接口
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
public interface TbLotteryConfigService extends IService<TbLotteryConfig> {
    /**
     * 获取当前生效的抽奖配置（包含奖品列表）
     */
    TbLotteryConfig getActiveConfig();

    /**
     * 保存抽奖配置及其奖品（新增或更新，奖品以 config.prizeList 为准覆盖）
     */
    boolean saveConfig(TbLotteryConfig config);

    /**
     * 删除配置并级联删除其下奖品
     */
    boolean removeConfigById(Long id);
}
