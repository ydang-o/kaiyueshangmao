/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.task;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.service.OrderInfoService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderAutoCancelTask {
    private static final Logger log = LoggerFactory.getLogger(OrderAutoCancelTask.class);
    private final OrderInfoService orderInfoService;

    @Scheduled(cron="0 */5 * * * ?")
    public void cancelTimeoutUnpaidOrders() {
        try {
            LocalDateTime deadline = LocalDateTime.now().minusMinutes(30L);
            List list = this.orderInfoService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(OrderInfo::getIsPay, "0")).and(w -> ((LambdaQueryWrapper)((LambdaQueryWrapper)w.isNull(OrderInfo::getStatus)).or()).ne(OrderInfo::getStatus, OrderInfoEnum.STATUS_5.getValue()))).lt(OrderInfo::getCreateTime, deadline));
            if (!list.isEmpty()) {
                for (OrderInfo order : list) {
                    try {
                        this.orderInfoService.orderCancel(order);
                        log.info("[OrderAutoCancel] \u81ea\u52a8\u53d6\u6d88\u8d85\u65f6\u8ba2\u5355 orderId={}, orderNo={}", (Object)order.getId(), (Object)order.getOrderNo());
                    }
                    catch (Exception e) {
                        log.warn("[OrderAutoCancel] \u53d6\u6d88\u8ba2\u5355\u5931\u8d25 orderId={}: {}", (Object)order.getId(), (Object)e.getMessage());
                    }
                }
            }
        }
        catch (Exception e) {
            log.error("[OrderAutoCancel] \u6267\u884c\u5931\u8d25: {}", (Object)e.getMessage());
        }
    }

    @Generated
    public OrderAutoCancelTask(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }
}

