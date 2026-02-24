package com.dingyangmall.mall.service;

import com.dingyangmall.mall.dto.ExpressTrackResult;
import com.dingyangmall.mall.entity.OrderLogistics;

/**
 * 快递100（Api100）实时查询服务
 *
 * @author dingyangmall
 */
public interface Kuaidi100QueryService {

    /**
     * 根据订单物流信息实时查询快递轨迹
     * 若未配置 key/customer 或查询失败，则返回仅含本地物流信息的简单结果
     *
     * @param orderLogistics 订单物流（含 logistics 公司编码、logisticsNo 单号，可选 telNum 顺丰需收件人手机后4位）
     * @return 轨迹结果，不会为 null
     */
    ExpressTrackResult query(OrderLogistics orderLogistics);
}
