package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.mapper.TbLotteryConfigMapper;
import com.dingyangmall.mall.mapper.TbLotteryPrizeMapper;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 抽奖配置 Service 实现类
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@Service
@AllArgsConstructor
public class TbLotteryConfigServiceImpl extends ServiceImpl<TbLotteryConfigMapper, TbLotteryConfig> implements TbLotteryConfigService {

    private final TbLotteryPrizeMapper lotteryPrizeMapper;

    @Override
    public TbLotteryConfig getActiveConfig() {
        // 获取开启状态的配置（假设只有一个生效，或者取最新的一个）
        TbLotteryConfig config = this.getOne(Wrappers.<TbLotteryConfig>lambdaQuery()
                .eq(TbLotteryConfig::getStatus, "1")
                .orderByDesc(TbLotteryConfig::getCreateTime)
                .last("LIMIT 1"));

        if (config != null) {
            List<TbLotteryPrize> prizes = lotteryPrizeMapper.selectList(Wrappers.<TbLotteryPrize>lambdaQuery()
                    .eq(TbLotteryPrize::getConfigId, config.getId())
                    .orderByAsc(TbLotteryPrize::getSortOrder));
            config.setPrizeList(prizes);
        }
        return config;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public boolean saveConfig(TbLotteryConfig config) {
        boolean result = this.saveOrUpdate(config);
        if (!result || config.getId() == null) {
            return result;
        }
        if (config.getPrizeList() != null) {
            lotteryPrizeMapper.delete(Wrappers.<TbLotteryPrize>lambdaQuery().eq(TbLotteryPrize::getConfigId, config.getId()));
            if (!config.getPrizeList().isEmpty()) {
                config.getPrizeList().forEach(prize -> prize.setConfigId(config.getId()));
                config.getPrizeList().forEach(lotteryPrizeMapper::insert);
            }
        }
        return true;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public boolean removeConfigById(Long id) {
        if (id == null) return false;
        lotteryPrizeMapper.delete(Wrappers.<TbLotteryPrize>lambdaQuery().eq(TbLotteryPrize::getConfigId, id));
        return this.removeById(id);
    }
}
