package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 小程序积分兑换接口
 */
@RestController
@RequestMapping(value = {"/weixin/api/ma/integralflow", "/api/ma/integralflow"})
public class IntegralFlowApi {

    @Autowired
    private TbIntegralFlowService integralFlowService;

    @Autowired
    private UmsMemberService umsMemberService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    /**
     * 积分兑换商品
     */
    @PostMapping("/exchange")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult exchange(@RequestBody IntegralExchangeDTO dto) {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("请先登录");
        }

        // 参数校验
        if (dto == null || StringUtils.isBlank(dto.getSpuId()) || dto.getQuantity() == null || dto.getQuantity() <= 0) {
            return AjaxResult.error("参数错误");
        }

        Long memberId;
        try {
            memberId = Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }

        // 查询商品
        GoodsSpu goods = goodsSpuService.getById(dto.getSpuId());
        if (goods == null) {
            return AjaxResult.error("商品不存在");
        }

        // 检查库存
        if (goods.getStock() == null || goods.getStock() <= 0) {
            return AjaxResult.error("商品库存不足");
        }

        // 计算所需积分 - 优先使用 integralPrice（积分价格），如果没有则使用 salesPrice
        Integer needPoints = dto.getIntegralAmount();
        if (needPoints == null || needPoints <= 0) {
            // 优先使用 integralPrice（积分价格字段）
            if (goods.getIntegralPrice() != null && goods.getIntegralPrice() > 0) {
                needPoints = goods.getIntegralPrice();
            } else if (goods.getSalesPrice() != null) {
                // salesPrice 是 BigDecimal，转换为 Integer
                needPoints = goods.getSalesPrice().intValue();
            } else {
                needPoints = 0;
            }
        }

        // 查询用户积分
        UmsMember member = umsMemberService.getById(memberId);
        if (member == null) {
            return AjaxResult.error("用户不存在");
        }

        Integer userPoints = member.getPoints() != null ? member.getPoints() : 0;
        if (userPoints < needPoints) {
            return AjaxResult.error("积分不足");
        }

        // 扣减库存
        goods.setStock(goods.getStock() - dto.getQuantity());
        goodsSpuService.updateById(goods);

        // 扣减积分（传入负数表示扣减）
        // 操作类型 1 表示平台分发（积分兑换）
        integralFlowService.addPoints(memberId, -needPoints, 1, 
                "兑换商品:" + goods.getName() + ",数量:" + dto.getQuantity());

        // 返回兑换结果
        AjaxResult result = AjaxResult.success("兑换成功");
        result.put("orderNo", "INT" + System.currentTimeMillis());
        result.put("goodsName", goods.getName());
        result.put("points", needPoints);
        return result;
    }

    /**
     * 查询用户积分余额
     */
    @GetMapping("/points")
    public AjaxResult getUserPoints() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("请先登录");
        }

        Long memberId;
        try {
            memberId = Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }

        UmsMember member = umsMemberService.getById(memberId);
        if (member == null) {
            return AjaxResult.error("用户不存在");
        }

        AjaxResult result = AjaxResult.success();
        result.put("points", member.getPoints() != null ? member.getPoints() : 0);
        return result;
    }

    /**
     * 查询积分流水记录
     */
    @GetMapping("/list")
    public AjaxResult list() {
        String memberIdStr = MemberUtils.getMemberId();
        if (StringUtils.isEmpty(memberIdStr)) {
            return AjaxResult.error("请先登录");
        }

        Long memberId;
        try {
            memberId = Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            return AjaxResult.error("无效的用户ID");
        }

        List<TbIntegralFlow> list = integralFlowService.lambdaQuery()
                .eq(TbIntegralFlow::getUserId, memberId)
                .orderByDesc(TbIntegralFlow::getOperTime)
                .list();

        return AjaxResult.success(list);
    }

    /**
     * 积分兑换DTO
     */
    @Data
    public static class IntegralExchangeDTO {
        /**
         * 商品ID
         */
        private String spuId;

        /**
         * 兑换数量
         */
        private Integer quantity;

        /**
         * 支付方式（2表示积分支付）
         */
        private Integer payType;

        /**
         * 积分金额
         */
        private Integer integralAmount;
    }
}
