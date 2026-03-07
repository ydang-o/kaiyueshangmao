package com.dingyangmall.mall.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.mall.config.CommonConstants;
import com.dingyangmall.mall.constant.MallConstants;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.OrderInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时任务：超时未支付订单自动取消（30 分钟）
 * 作为 Redis key 过期监听的补充，不依赖 notify-keyspace-events
 */
@Component
@RequiredArgsConstructor
public class OrderAutoCancelTask {

    private static final Logger log = LoggerFactory.getLogger(OrderAutoCancelTask.class);

    private final OrderInfoService orderInfoService;

    /**
     * 每 5 分钟执行一次：扫描超时 30 分钟未支付的订单并自动取消
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void cancelTimeoutUnpaidOrders() {
        try {
            LocalDateTime deadline = LocalDateTime.now().minusMinutes(MallConstants.ORDER_TIME_OUT_0);
            List<OrderInfo> list = orderInfoService.list(Wrappers.<OrderInfo>lambdaQuery()
                    .eq(OrderInfo::getIsPay, CommonConstants.NO)
                    .and(w -> w.isNull(OrderInfo::getStatus).or().ne(OrderInfo::getStatus, OrderInfoEnum.STATUS_5.getValue()))
                    .lt(OrderInfo::getCreateTime, deadline));

            if (!list.isEmpty()) {
                for (OrderInfo order : list) {
                    try {
                        orderInfoService.orderCancel(order);
                        log.info("[OrderAutoCancel] 自动取消超时订单 orderId={}, orderNo={}", order.getId(), order.getOrderNo());
                    } catch (Exception e) {
                        log.warn("[OrderAutoCancel] 取消订单失败 orderId={}: {}", order.getId(), e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("[OrderAutoCancel] 执行失败: {}", e.getMessage());
        }
    }
}
