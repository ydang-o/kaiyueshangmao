/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.config.MallConfigProperties;
import com.dingyangmall.mall.dto.PlaceOrderDTO;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.OrderInfo;
import com.dingyangmall.mall.entity.OrderItem;
import com.dingyangmall.mall.entity.OrderLogistics;
import com.dingyangmall.mall.entity.ShoppingCart;
import com.dingyangmall.mall.entity.TbCouponInfo;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.entity.UserAddress;
import com.dingyangmall.mall.enums.OrderInfoEnum;
import com.dingyangmall.mall.enums.OrderLogisticsEnum;
import com.dingyangmall.mall.mapper.OrderInfoMapper;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.OrderInfoService;
import com.dingyangmall.mall.service.OrderItemService;
import com.dingyangmall.mall.service.OrderLogisticsService;
import com.dingyangmall.mall.service.ShoppingCartService;
import com.dingyangmall.mall.service.TbCouponInfoService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.service.UserAddressService;
import com.dingyangmall.weixin.config.WxPayConfiguration;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderInfoServiceImpl
extends ServiceImpl<OrderInfoMapper, OrderInfo>
implements OrderInfoService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OrderInfoServiceImpl.class);
    private final GoodsSpuService goodsSpuService;
    private final ShoppingCartService shoppingCartService;
    private final UserAddressService userAddressService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OrderItemService orderItemService;
    private final OrderLogisticsService orderLogisticsService;
    private final MallConfigProperties mallConfigProperties;
    private final TbCouponInfoService couponInfoService;
    private final TbIntegralFlowService integralFlowService;
    private final UmsMemberService umsMemberService;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean updateById(OrderInfo entity) {
        if (StrUtil.isNotBlank(entity.getLogistics()) && StrUtil.isNotBlank(entity.getLogisticsNo())) {
            entity.setDeliveryTime(LocalDateTime.now());
            OrderLogistics orderLogistics = (OrderLogistics)this.orderLogisticsService.getOne((Wrapper)Wrappers.lambdaQuery().eq(OrderLogistics::getId, entity.getLogisticsId()));
            boolean sendRedis = false;
            if (StrUtil.isBlank(orderLogistics.getLogistics()) && StrUtil.isBlank(orderLogistics.getLogisticsNo())) {
                sendRedis = true;
            }
            orderLogistics.setLogistics(entity.getLogistics());
            orderLogistics.setLogisticsNo(entity.getLogisticsNo());
            orderLogistics.setStatus(OrderLogisticsEnum.STATUS_1.getValue());
            this.orderLogisticsService.updateById(orderLogistics);
            entity.setStatus(OrderInfoEnum.STATUS_2.getValue());
            if (sendRedis) {
                String keyRedis = String.valueOf(StrUtil.format((CharSequence)"{}:{}", "mall:order:status_2:", entity.getId()));
                this.redisTemplate.opsForValue().set(keyRedis, entity.getOrderNo(), 7L, TimeUnit.DAYS);
            }
        }
        return super.updateById(entity);
    }

    @Override
    public IPage<OrderInfo> page1(IPage<OrderInfo> page, Wrapper<OrderInfo> queryWrapper) {
        return ((OrderInfoMapper)this.baseMapper).selectPage1(page, queryWrapper.getEntity());
    }

    @Override
    public IPage<OrderInfo> page2(IPage<OrderInfo> page, OrderInfo orderInfo) {
        return ((OrderInfoMapper)this.baseMapper).selectPage2(page, orderInfo);
    }

    @Override
    public OrderInfo getById2(Serializable id) {
        OrderInfo orderInfo = ((OrderInfoMapper)this.baseMapper).selectById2(id);
        if (orderInfo != null) {
            Long outTime;
            String keyRedis = null;
            if ("0".equals(orderInfo.getIsPay())) {
                keyRedis = String.valueOf(StrUtil.format((CharSequence)"{}:{}", "mall:order:is_pay_0:", orderInfo.getId()));
            }
            if (OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())) {
                keyRedis = String.valueOf(StrUtil.format((CharSequence)"{}:{}", "mall:order:status_2:", orderInfo.getId()));
            }
            if (keyRedis != null && (outTime = this.redisTemplate.getExpire(keyRedis)) != null && outTime > 0L) {
                orderInfo.setOutTime(outTime);
            }
        }
        return orderInfo;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void orderCancel(OrderInfo orderInfo) {
        if ("0".equals(orderInfo.getIsPay()) && !OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus())) {
            orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
            List<OrderItem> listOrderItem = this.orderItemService.list((Wrapper)Wrappers.lambdaQuery().eq(OrderItem::getOrderId, orderInfo.getId()));
            listOrderItem.forEach(orderItem -> {
                GoodsSpu goodsSpu = (GoodsSpu)this.goodsSpuService.getById((Serializable)((Object)orderItem.getSpuId()));
                if (goodsSpu != null) {
                    goodsSpu.setStock(goodsSpu.getStock() + orderItem.getQuantity());
                    this.goodsSpuService.updateById(goodsSpu);
                }
            });
            ((OrderInfoMapper)this.baseMapper).updateById(orderInfo);
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void orderReceive(OrderInfo orderInfo) {
        orderInfo.setStatus(OrderInfoEnum.STATUS_3.getValue());
        orderInfo.setReceiverTime(LocalDateTime.now());
        ((OrderInfoMapper)this.baseMapper).updateById(orderInfo);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public OrderInfo orderSub(PlaceOrderDTO placeOrderDTO) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtil.copyProperties(placeOrderDTO, orderInfo);
        boolean isIntegralOrder = placeOrderDTO.getIsPay() != null && placeOrderDTO.getIsPay() != false;
        orderInfo.setIsPay(isIntegralOrder ? "1" : "0");
        orderInfo.setPayIntegral(placeOrderDTO.getPayIntegral() != null ? placeOrderDTO.getPayIntegral() : 0);
        if (isIntegralOrder) {
            orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
            orderInfo.setPaymentTime(LocalDateTime.now());
        }
        orderInfo.setOrderNo(IdUtil.getSnowflake(0L, 0L).nextIdStr());
        orderInfo.setSalesPrice(BigDecimal.ZERO);
        orderInfo.setPaymentPrice(BigDecimal.ZERO);
        orderInfo.setFreightPrice(BigDecimal.ZERO);
        orderInfo.setCreateTime(LocalDateTime.now());
        ArrayList listOrderItem = new ArrayList();
        ArrayList listGoodsSpu = new ArrayList();
        placeOrderDTO.getSkus().forEach(orderGoods -> {
            GoodsSpu goodsSpu = (GoodsSpu)this.goodsSpuService.getOne((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(GoodsSpu::getId, orderGoods.getSpuId())).eq(GoodsSpu::getShelf, "1")).ge(GoodsSpu::getStock, orderGoods.getQuantity()));
            if (goodsSpu != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderInfo.getId());
                orderItem.setSpuId(goodsSpu.getId());
                orderItem.setSpuName(goodsSpu.getName());
                String[] picUrls = goodsSpu.getPicUrls();
                if (picUrls != null && picUrls.length > 0) {
                    orderItem.setPicUrl(picUrls[0]);
                } else {
                    orderItem.setPicUrl("");
                }
                orderItem.setQuantity(orderGoods.getQuantity());
                orderItem.setSalesPrice(goodsSpu.getSalesPrice());
                orderItem.setFreightPrice(orderGoods.getFreightPrice());
                orderItem.setPaymentPrice(orderGoods.getPaymentPrice().add(orderItem.getFreightPrice()));
                BigDecimal quantity = new BigDecimal(orderGoods.getQuantity());
                listOrderItem.add(orderItem);
                orderInfo.setSalesPrice(orderInfo.getSalesPrice().add(goodsSpu.getSalesPrice().multiply(quantity)));
                orderInfo.setFreightPrice(orderInfo.getFreightPrice().add(orderItem.getFreightPrice()));
                orderInfo.setPaymentPrice(orderInfo.getPaymentPrice().add(orderItem.getPaymentPrice()));
                goodsSpu.setStock(goodsSpu.getStock() - orderItem.getQuantity());
                listGoodsSpu.add(goodsSpu);
                this.shoppingCartService.remove((Wrapper)((LambdaQueryWrapper)Wrappers.lambdaQuery().eq(ShoppingCart::getSpuId, goodsSpu.getId())).eq(ShoppingCart::getUserId, placeOrderDTO.getUserId()));
            }
        });
        if (listOrderItem.size() > 0) {
            UserAddress userAddress;
            if (placeOrderDTO.getUserAddressId() != null && !placeOrderDTO.getUserAddressId().isEmpty() && (userAddress = (UserAddress)this.userAddressService.getById((Serializable)((Object)placeOrderDTO.getUserAddressId()))) != null) {
                OrderLogistics orderLogistics = new OrderLogistics();
                orderLogistics.setPostalCode(userAddress.getPostalCode());
                orderLogistics.setUserName(userAddress.getUserName());
                orderLogistics.setTelNum(userAddress.getTelNum());
                orderLogistics.setAddress(userAddress.getProvinceName() + userAddress.getCityName() + userAddress.getCountyName() + userAddress.getDetailInfo());
                this.orderLogisticsService.save(orderLogistics);
                orderInfo.setLogisticsId(orderLogistics.getId());
            }
            orderInfo.setName(((OrderItem)listOrderItem.get(0)).getSpuName());
            super.save(orderInfo);
            listOrderItem.forEach(orderItem -> orderItem.setOrderId(orderInfo.getId()));
            this.orderItemService.saveBatch(listOrderItem);
            listGoodsSpu.forEach(goodsSpuItem -> this.goodsSpuService.updateById(goodsSpuItem));
            if (!isIntegralOrder) {
                long orderTimeOut = 30L;
                String keyRedis = String.valueOf(StrUtil.format((CharSequence)"{}:{}", "mall:order:is_pay_0:", orderInfo.getId()));
                this.redisTemplate.opsForValue().set(keyRedis, orderInfo.getOrderNo(), orderTimeOut, TimeUnit.MINUTES);
            }
        } else {
            return null;
        }
        return orderInfo;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void notifyOrder(OrderInfo orderInfo) {
        if ("0".equals(orderInfo.getIsPay())) {
            orderInfo.setIsPay("1");
            orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
            List listOrderItem = this.orderItemService.list((Wrapper)Wrappers.lambdaQuery().eq(OrderItem::getOrderId, orderInfo.getId()));
            Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
            List<GoodsSpu> listGoodsSpu = this.goodsSpuService.listByIds(resultList.keySet());
            listGoodsSpu.forEach(goodsSpu -> {
                ((List)resultList.get(goodsSpu.getId())).forEach(orderItem -> goodsSpu.setSaleNum(goodsSpu.getSaleNum() + orderItem.getQuantity()));
                if ("2".equals(goodsSpu.getGoodsType())) {
                    ((List)resultList.get(goodsSpu.getId())).forEach(orderItem -> {
                        for (int i = 0; i < orderItem.getQuantity(); ++i) {
                            TbCouponInfo coupon = new TbCouponInfo();
                            try {
                                coupon.setUserId(Long.parseLong(orderInfo.getUserId()));
                            }
                            catch (NumberFormatException e) {
                                log.error("Failed to parse userId: {}", (Object)orderInfo.getUserId());
                                continue;
                            }
                            coupon.setCouponCode(IdUtil.simpleUUID().substring(0, 10).toUpperCase());
                            coupon.setGoodsId(goodsSpu.getId());
                            coupon.setGoodsName(goodsSpu.getName());
                            coupon.setGoodsPic(goodsSpu.getPicUrls() != null && goodsSpu.getPicUrls().length > 0 ? goodsSpu.getPicUrls()[0] : "");
                            coupon.setCouponStatus(1);
                            coupon.setValidityStart(LocalDateTime.now());
                            coupon.setValidityEnd(LocalDateTime.now().plusYears(1L));
                            coupon.setCreateTime(LocalDateTime.now());
                            coupon.setUpdateTime(LocalDateTime.now());
                            this.couponInfoService.save(coupon);
                        }
                    });
                }
                this.goodsSpuService.updateById(goodsSpu);
                ((OrderInfoMapper)this.baseMapper).updateById(orderInfo);
            });
        }
    }

    @Override
    public void saveRefunds(OrderItem orderItem) {
        if ((orderItem = (OrderItem)this.orderItemService.getById((Serializable)((Object)orderItem.getId()))) != null && "0".equals(orderItem.getIsRefund()) && ("0".equals(orderItem.getStatus()) || "2".equals(orderItem.getStatus()))) {
            orderItem.setStatus("1");
            this.orderItemService.updateById(orderItem);
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void doOrderRefunds(OrderItem orderItem) {
        OrderItem orderItem2 = (OrderItem)this.orderItemService.getById((Serializable)((Object)orderItem.getId()));
        OrderInfo orderInfo = (OrderInfo)((OrderInfoMapper)this.baseMapper).selectById((Serializable)((Object)orderItem2.getOrderId()));
        if (orderItem2 != null) {
            if ("3".equals(orderItem.getStatus())) {
                boolean isIntegralOrder = "4".equals(orderInfo.getPaymentWay());
                if (isIntegralOrder) {
                    try {
                        BigDecimal refundPrice;
                        int refundPoints;
                        String userId = orderInfo.getUserId();
                        UmsMember member = (UmsMember)this.umsMemberService.getById(Long.valueOf(Long.parseLong(userId)));
                        if (member != null && (refundPoints = (refundPrice = orderItem2.getPaymentPrice() != null ? orderItem2.getPaymentPrice() : BigDecimal.ZERO).intValue()) > 0) {
                            this.integralFlowService.addPoints(member.getId(), refundPoints, 3, "\u8ba2\u5355\u9000\u6b3e\u8fd4\u8fd8\u79ef\u5206\uff0c\u8ba2\u5355\u53f7\uff1a" + orderInfo.getOrderNo());
                            log.info("\u79ef\u5206\u5151\u6362\u8ba2\u5355\u9000\u6b3e\uff0c\u8fd4\u8fd8\u7528\u6237 {} \u79ef\u5206 {}\uff0c\u8ba2\u5355\u53f7\uff1a{}", member.getId(), refundPoints, orderInfo.getOrderNo());
                        }
                        orderItem2.setStatus(orderItem.getStatus());
                        orderItem2.setIsRefund("1");
                        this.orderItemService.updateById(orderItem2);
                        orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
                        if (orderItem2.getPaymentPrice() != null) {
                            BigDecimal refundAmount = orderItem2.getPaymentPrice();
                            BigDecimal currentPaymentPrice = orderInfo.getPaymentPrice() != null ? orderInfo.getPaymentPrice() : BigDecimal.ZERO;
                            BigDecimal currentSalesPrice = orderInfo.getSalesPrice() != null ? orderInfo.getSalesPrice() : BigDecimal.ZERO;
                            orderInfo.setPaymentPrice(currentPaymentPrice.subtract(refundAmount));
                            orderInfo.setSalesPrice(currentSalesPrice.subtract(refundAmount));
                            if (orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO) < 0) {
                                orderInfo.setPaymentPrice(BigDecimal.ZERO);
                            }
                            if (orderInfo.getSalesPrice().compareTo(BigDecimal.ZERO) < 0) {
                                orderInfo.setSalesPrice(BigDecimal.ZERO);
                            }
                        }
                        ((OrderInfoMapper)this.baseMapper).updateById(orderInfo);
                    }
                    catch (Exception e) {
                        log.error("\u79ef\u5206\u5151\u6362\u8ba2\u5355\u9000\u6b3e\u5931\u8d25", e);
                        throw new RuntimeException("\u79ef\u5206\u5151\u6362\u8ba2\u5355\u9000\u6b3e\u5931\u8d25\uff1a" + e.getMessage());
                    }
                } else {
                    WxPayRefundRequest request = new WxPayRefundRequest();
                    request.setTransactionId(orderInfo.getTransactionId());
                    request.setOutRefundNo(orderItem2.getId());
                    request.setTotalFee(orderItem2.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
                    request.setRefundFee(orderItem2.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
                    request.setNotifyUrl(this.mallConfigProperties.getNotifyHost() + "/weixin/api/ma/orderinfo/notify-refunds");
                    WxPayService wxPayService = WxPayConfiguration.getPayService();
                    try {
                        wxPayService.refund(request);
                        orderItem2.setStatus(orderItem.getStatus());
                        this.orderItemService.updateById(orderItem2);
                    }
                    catch (WxPayException e) {
                        log.error("\u5fae\u4fe1\u9000\u6b3e\u8bf7\u6c42\u5931\u8d25", e);
                        throw new RuntimeException(e.getReturnMsg() + e.getCustomErrorMsg() + e.getErrCodeDes());
                    }
                }
            } else if ("2".equals(orderItem.getStatus())) {
                orderItem2.setStatus(orderItem.getStatus());
                this.orderItemService.updateById(orderItem2);
            }
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void notifyRefunds(WxPayRefundNotifyResult notifyResult) {
        OrderItem orderItem = (OrderItem)this.orderItemService.getById((Serializable)((Object)notifyResult.getReqInfo().getOutRefundNo()));
        OrderInfo orderInfo = (OrderInfo)((OrderInfoMapper)this.baseMapper).selectById((Serializable)((Object)orderItem.getOrderId()));
        if ("3".equals(orderItem.getStatus())) {
            orderItem.setIsRefund("1");
            orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
            if (orderItem.getPaymentPrice() != null) {
                BigDecimal refundAmount = orderItem.getPaymentPrice();
                BigDecimal currentPaymentPrice = orderInfo.getPaymentPrice() != null ? orderInfo.getPaymentPrice() : BigDecimal.ZERO;
                BigDecimal currentSalesPrice = orderInfo.getSalesPrice() != null ? orderInfo.getSalesPrice() : BigDecimal.ZERO;
                orderInfo.setPaymentPrice(currentPaymentPrice.subtract(refundAmount));
                orderInfo.setSalesPrice(currentSalesPrice.subtract(refundAmount));
                if (orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO) < 0) {
                    orderInfo.setPaymentPrice(BigDecimal.ZERO);
                }
                if (orderInfo.getSalesPrice().compareTo(BigDecimal.ZERO) < 0) {
                    orderInfo.setSalesPrice(BigDecimal.ZERO);
                }
            }
            this.orderItemService.updateById(orderItem);
            ((OrderInfoMapper)this.baseMapper).updateById(orderInfo);
        }
    }

    @Generated
    public OrderInfoServiceImpl(GoodsSpuService goodsSpuService, ShoppingCartService shoppingCartService, UserAddressService userAddressService, RedisTemplate<String, Object> redisTemplate, OrderItemService orderItemService, OrderLogisticsService orderLogisticsService, MallConfigProperties mallConfigProperties, TbCouponInfoService couponInfoService, TbIntegralFlowService integralFlowService, UmsMemberService umsMemberService) {
        this.goodsSpuService = goodsSpuService;
        this.shoppingCartService = shoppingCartService;
        this.userAddressService = userAddressService;
        this.redisTemplate = redisTemplate;
        this.orderItemService = orderItemService;
        this.orderLogisticsService = orderLogisticsService;
        this.mallConfigProperties = mallConfigProperties;
        this.couponInfoService = couponInfoService;
        this.integralFlowService = integralFlowService;
        this.umsMemberService = umsMemberService;
    }
}

