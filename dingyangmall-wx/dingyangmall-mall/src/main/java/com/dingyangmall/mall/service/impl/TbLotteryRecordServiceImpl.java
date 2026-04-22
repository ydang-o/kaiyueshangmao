package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.mapper.TbLotteryRecordMapper;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 抽奖记录 Service 实现类
 *
 * @author dingyangmall
 * @date 2026-02-13
 */
@Slf4j
@Service
@AllArgsConstructor
public class TbLotteryRecordServiceImpl extends ServiceImpl<TbLotteryRecordMapper, TbLotteryRecord> implements TbLotteryRecordService {

    private final TbLotteryConfigService lotteryConfigService;
    private final TbIntegralFlowService integralFlowService;
    private final TbCouponInfoService couponInfoService;
    private final GoodsSpuService goodsSpuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbLotteryRecord draw(Long userId) {
        // 1. 获取活动配置
        TbLotteryConfig config = lotteryConfigService.getActiveConfig();
        if (config == null) {
            throw new RuntimeException("当前没有开启的抽奖活动");
        }

        // 2. 检查每日限制
        Long count = baseMapper.selectCount(Wrappers.<TbLotteryRecord>lambdaQuery()
                .eq(TbLotteryRecord::getUserId, userId)
                .ge(TbLotteryRecord::getCreateTime, LocalDate.now().atStartOfDay()));
        if (config.getDailyLimit() != null && count >= config.getDailyLimit()) {
            throw new RuntimeException("今日抽奖次数已达上限");
        }

        // 3. 扣除积分
        if (config.getCostPoints() != null && config.getCostPoints() > 0) {
            try {
                // 8: 抽奖获得 (这里是消耗，传负数)
                integralFlowService.addPoints(userId, -config.getCostPoints(), 8, "抽奖消耗");
            } catch (Exception e) {
                throw new RuntimeException("积分不足，无法抽奖");
            }
        }

        // 4. 执行抽奖算法
        List<TbLotteryPrize> prizes = config.getPrizeList();
        TbLotteryPrize wonPrize = null;
        
        // 生成 0-100 的随机数 (假设概率总和+未中奖概率=100)
        double random = RandomUtil.randomDouble(0, 100);
        double cumulativeProbability = 0;
        
        if (prizes != null) {
            for (TbLotteryPrize prize : prizes) {
                cumulativeProbability += prize.getProbability();
                if (random < cumulativeProbability) {
                    wonPrize = prize;
                    break;
                }
            }
        }

        // 5. 生成记录
        TbLotteryRecord record = new TbLotteryRecord();
        record.setUserId(userId);
        record.setCostPoints(config.getCostPoints());
        record.setCreateTime(LocalDateTime.now());
        record.setGrantStatus("0"); // 默认待发放/未发放

        if (wonPrize != null) {
            record.setIsWin("1");
            record.setPrizeId(wonPrize.getId());
            record.setPrizeName(wonPrize.getPrizeName());
            record.setPrizeType(wonPrize.getPrizeType());
            
            // 6. 发放奖品
            if ("1".equals(wonPrize.getPrizeType())) { // 积分
                Integer points = wonPrize.getPointAmount();
                if (points != null && points > 0) {
                    integralFlowService.addPoints(userId, 8, points, "抽奖中奖-" + wonPrize.getPrizeName());
                    record.setGrantStatus("1"); // 已发放
                    record.setBusinessId("POINTS");
                }
            } else if ("0".equals(wonPrize.getPrizeType())) { // 商品/商品券
                GoodsSpu goodsSpu = goodsSpuService.getById(wonPrize.getGoodsId());
                if (goodsSpu != null) {
                    // 商品类型（0：普通商品；1：虚拟商品；2：商品券）
                    if ("2".equals(goodsSpu.getGoodsType())) {
                        // 商品券自动发放
                        try {
                            com.dingyangmall.mall.entity.TbCouponInfo coupon = couponInfoService.createCoupon(userId, goodsSpu.getId());
                            record.setGrantStatus("1");
                            record.setBusinessId(String.valueOf(coupon.getId()));
                        } catch (Exception e) {
                            log.error("抽奖发放商品券失败: userId={}, goodsId={}", userId, goodsSpu.getId(), e);
                            record.setGrantStatus("0"); // 发放失败
                        }
                    } else if ("1".equals(goodsSpu.getGoodsType())) {
                        // 虚拟商品自动发放（假设也是直接完成，这里暂不处理具体逻辑，需对接虚拟发货）
                        record.setGrantStatus("1");
                        record.setBusinessId("VIRTUAL_GOODS");
                    } else {
                        // 实体商品需手动领奖（填写地址）
                        record.setGrantStatus("0");
                    }
                }
            }
        } else {
            record.setIsWin("0");
            record.setPrizeName("未中奖");
            record.setGrantStatus("1"); // 未中奖视为已处理
        }
        
        save(record);
        
        // 如果是商品券，在这里补充逻辑（需要引入 GoodsSpuService 判断类型，或者尝试创建券）
        // 为了完整性，我应该注入 GoodsSpuService。
        // 但为了避免循环依赖或过耦合，我先把 record 保存。
        // 如果要支持自动发券，最好在 prize 表里标识 "is_coupon" 或者查 goods 表。
        
        return record;
    }
}
