/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.mapper.TbLotteryConfigMapper;
import com.dingyangmall.mall.mapper.TbLotteryPrizeMapper;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbLotteryConfigServiceImpl
extends ServiceImpl<TbLotteryConfigMapper, TbLotteryConfig>
implements TbLotteryConfigService {
    private final TbLotteryPrizeMapper lotteryPrizeMapper;

    @Override
    public TbLotteryConfig getActiveConfig() {
        TbLotteryConfig config = (TbLotteryConfig)this.getOne((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryConfig::getStatus, "1")).orderByDesc(TbLotteryConfig::getCreateTime)).last("LIMIT 1"));
        if (config != null) {
            List<TbLotteryPrize> prizes = this.lotteryPrizeMapper.selectList((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryPrize::getConfigId, config.getId())).orderByAsc(TbLotteryPrize::getSortOrder));
            config.setPrizeList(prizes);
        }
        return config;
    }

    @Override
    public List<TbLotteryConfig> getActiveList() {
        List<TbLotteryConfig> configs = this.list((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryConfig::getStatus, "1")).orderByDesc(TbLotteryConfig::getCreateTime));
        if (configs != null && !configs.isEmpty()) {
            for (TbLotteryConfig config : configs) {
                List<TbLotteryPrize> prizes = this.lotteryPrizeMapper.selectList((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryPrize::getConfigId, config.getId())).orderByAsc(TbLotteryPrize::getSortOrder));
                config.setPrizeList(prizes);
            }
        }
        return configs;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean saveConfig(TbLotteryConfig config) {
        boolean result = this.saveOrUpdate(config);
        if (!result || config.getId() == null) {
            return result;
        }
        if (config.getPrizeList() != null) {
            this.lotteryPrizeMapper.delete((Wrapper)Wrappers.lambdaQuery().eq(TbLotteryPrize::getConfigId, config.getId()));
            if (!config.getPrizeList().isEmpty()) {
                config.getPrizeList().forEach(prize -> prize.setConfigId(config.getId()));
                config.getPrizeList().forEach(this.lotteryPrizeMapper::insert);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean removeConfigById(Long id) {
        if (id == null) {
            return false;
        }
        this.lotteryPrizeMapper.delete((Wrapper)Wrappers.lambdaQuery().eq(TbLotteryPrize::getConfigId, id));
        return this.removeById(id);
    }

    @Generated
    public TbLotteryConfigServiceImpl(TbLotteryPrizeMapper lotteryPrizeMapper) {
        this.lotteryPrizeMapper = lotteryPrizeMapper;
    }
}

