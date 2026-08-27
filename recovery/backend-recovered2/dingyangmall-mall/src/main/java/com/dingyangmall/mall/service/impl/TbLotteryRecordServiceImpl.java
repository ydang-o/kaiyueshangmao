/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.dto.PlaceOrderGoodsDTO;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.TbLotteryConfig;
import com.dingyangmall.mall.entity.TbLotteryPrize;
import com.dingyangmall.mall.entity.TbLotteryRecord;
import com.dingyangmall.mall.mapper.TbLotteryRecordMapper;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbLotteryConfigService;
import com.dingyangmall.mall.service.TbLotteryRecordService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbLotteryRecordServiceImpl
extends ServiceImpl<TbLotteryRecordMapper, TbLotteryRecord>
implements TbLotteryRecordService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TbLotteryRecordServiceImpl.class);
    private final TbLotteryConfigService lotteryConfigService;
    private final TbIntegralFlowService integralFlowService;
    private final TbCouponInfoService couponInfoService;
    private final GoodsSpuService goodsSpuService;
    private final OrderInfoService orderInfoService;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public TbLotteryRecord draw(Long userId) {
        TbLotteryRecord record;
        block22: {
            TbLotteryConfig config = this.lotteryConfigService.getActiveConfig();
            if (config == null) {
                throw new RuntimeException("\u5f53\u524d\u6ca1\u6709\u5f00\u542f\u7684\u62bd\u5956\u6d3b\u52a8");
            }
            Long count = ((TbLotteryRecordMapper)this.baseMapper).selectCount((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(TbLotteryRecord::getUserId, userId)).ge(TbLotteryRecord::getCreateTime, LocalDate.now().atStartOfDay()));
            if (config.getDailyLimit() != null && count >= (long)config.getDailyLimit().intValue()) {
                throw new RuntimeException("\u4eca\u65e5\u62bd\u5956\u6b21\u6570\u5df2\u8fbe\u4e0a\u9650");
            }
            if (config.getCostPoints() != null && config.getCostPoints() > 0) {
                try {
                    this.integralFlowService.addPoints(userId, -config.getCostPoints().intValue(), 8, "\u62bd\u5956\u6d88\u8017");
                }
                catch (Exception e) {
                    throw new RuntimeException("\u79ef\u5206\u4e0d\u8db3\uff0c\u65e0\u6cd5\u62bd\u5956");
                }
            }
            List<TbLotteryPrize> prizes = config.getPrizeList();
            TbLotteryPrize wonPrize = null;
            double random = RandomUtil.randomDouble(0.0, 100.0);
            double cumulativeProbability = 0.0;
            if (prizes != null) {
                for (TbLotteryPrize prize : prizes) {
                    if (!(random < (cumulativeProbability += prize.getProbability().doubleValue()))) continue;
                    wonPrize = prize;
                    break;
                }
            }
            record = new TbLotteryRecord();
            record.setUserId(userId);
            record.setConfigId(config.getId());
            record.setCostPoints(config.getCostPoints());
            record.setCreateTime(LocalDateTime.now());
            record.setGrantStatus("0");
            if (wonPrize != null) {
                GoodsSpu goodsSpu;
                record.setIsWin("1");
                record.setPrizeId(wonPrize.getId());
                record.setPrizeName(wonPrize.getPrizeName());
                record.setPrizeType(wonPrize.getPrizeType());
                if ("1".equals(wonPrize.getPrizeType())) {
                    Integer points = wonPrize.getPointAmount();
                    if (points != null && points > 0) {
                        this.integralFlowService.addPoints(userId, points, 8, "\u62bd\u5956\u4e2d\u5956-" + wonPrize.getPrizeName());
                        record.setGrantStatus("1");
                        record.setBusinessId("POINTS");
                    }
                } else if ("0".equals(wonPrize.getPrizeType()) && (goodsSpu = (GoodsSpu)this.goodsSpuService.getById((Serializable)((Object)wonPrize.getGoodsId()))) != null) {
                    if ("2".equals(goodsSpu.getGoodsType())) {
                        try {
                            TbCouponInfo coupon = this.couponInfoService.createCoupon(userId, goodsSpu.getId());
                            record.setGrantStatus("1");
                            record.setBusinessId(String.valueOf(coupon.getId()));
                        }
                        catch (Exception e) {
                            log.error("\u62bd\u5956\u53d1\u653e\u5546\u54c1\u5238\u5931\u8d25: userId={}, goodsId={}", userId, goodsSpu.getId(), e);
                            record.setGrantStatus("0");
                        }
                    } else if ("1".equals(goodsSpu.getGoodsType())) {
                        record.setGrantStatus("1");
                        record.setBusinessId("VIRTUAL_GOODS");
                    } else {
                        try {
                            PlaceOrderDTO orderDTO = new PlaceOrderDTO();
                            orderDTO.setUserId(String.valueOf(userId));
                            orderDTO.setPaymentWay("4");
                            PlaceOrderGoodsDTO goodsDTO = new PlaceOrderGoodsDTO();
                            goodsDTO.setSpuId(goodsSpu.getId());
                            goodsDTO.setQuantity(1);
                            goodsDTO.setPaymentPrice(new BigDecimal("0"));
                            goodsDTO.setFreightPrice(new BigDecimal("0"));
                            orderDTO.setSkus(Collections.singletonList(goodsDTO));
                            OrderInfo orderInfo = this.orderInfoService.orderSub(orderDTO);
                            if (orderInfo != null) {
                                record.setGrantStatus("1");
                                record.setBusinessId(orderInfo.getOrderNo());
                                break block22;
                            }
                            record.setGrantStatus("0");
                        }
                        catch (Exception e) {
                            log.error("\u62bd\u5956\u521b\u5efa\u5546\u54c1\u8ba2\u5355\u5931\u8d25: userId={}, goodsId={}", userId, goodsSpu.getId(), e);
                            record.setGrantStatus("0");
                        }
                    }
                }
            } else {
                record.setIsWin("0");
                record.setPrizeName("\u672a\u4e2d\u5956");
                record.setGrantStatus("1");
            }
        }
        this.save(record);
        return record;
    }

    @Generated
    public TbLotteryRecordServiceImpl(TbLotteryConfigService lotteryConfigService, TbIntegralFlowService integralFlowService, TbCouponInfoService couponInfoService, GoodsSpuService goodsSpuService, OrderInfoService orderInfoService) {
        this.lotteryConfigService = lotteryConfigService;
        this.integralFlowService = integralFlowService;
        this.couponInfoService = couponInfoService;
        this.goodsSpuService = goodsSpuService;
        this.orderInfoService = orderInfoService;
    }
}

