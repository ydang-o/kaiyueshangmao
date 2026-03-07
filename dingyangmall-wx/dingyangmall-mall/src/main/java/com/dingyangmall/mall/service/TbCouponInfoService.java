package com.dingyangmall.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dingyangmall.mall.entity.TbCouponInfo;

/**
 * 商品券信息 服务类
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
public interface TbCouponInfoService extends IService<TbCouponInfo> {
    
    /**
     * 根据券码查询有效券
     * @param couponCode 券码
     * @return 商品券信息
     */
    TbCouponInfo getValidCouponByCode(String couponCode);
    
    /**
     * 核销商品券
     * @param couponId 券ID
     * @param dealerId 经销商ID
     * @param dealerName 经销商名称
     * @return 是否成功
     */
    boolean verifyCoupon(Long couponId, Long dealerId, String dealerName);

    /**
     * 查询用户优惠券
     * @param userId 用户ID
     * @param status 状态：1-未使用 2-已使用 3-已过期
     * @return 优惠券列表
     */
    java.util.List<TbCouponInfo> getUserCoupons(Long userId, Integer status);

    /**
     * 创建商品券
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 生成的商品券
     */
    TbCouponInfo createCoupon(Long userId, String goodsId);

    /**
     * 发放代金券：向指定用户发放若干张商品券
     * @param userId 用户ID（ums_member.id）
     * @param goodsId 商品ID（商品券类型 goods_type=2）
     * @param count 发放数量
     * @param validityDays 有效天数
     * @return 已发放的券列表
     */
    java.util.List<TbCouponInfo> distributeCoupon(Long userId, String goodsId, int count, int validityDays);
}
