package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.mall.utils.MemberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping({"/weixin/api/ma/integralflow","/api/ma/integralflow"})
public class IntegralFlowApi {
 @Autowired private TbIntegralFlowService flowService; @Autowired private UmsMemberService memberService; @Autowired private GoodsSpuService goodsService;
 @PostMapping("/exchange") @Transactional public AjaxResult exchange(@RequestBody IntegralExchangeDTO dto){UmsMember m=current();if(m==null)return AjaxResult.error("请先登录");if(dto==null||dto.spuId==null)return AjaxResult.error("商品不能为空");GoodsSpu g=goodsService.getById2(dto.spuId);if(g==null)return AjaxResult.error("商品不存在");int qty=dto.quantity==null?1:Math.max(1,dto.quantity);int cost=dto.integralAmount==null?(g.getIntegralPrice()==null?0:g.getIntegralPrice()*qty):dto.integralAmount;if(cost<=0)return AjaxResult.error("积分价格无效");if((m.getPoints()==null?0:m.getPoints())<cost)return AjaxResult.error("积分不足");flowService.addPoints(m.getId(),-cost,1,"积分兑换商品："+g.getName());Map<String,Object> r=new HashMap<>();r.put("spuId",dto.spuId);r.put("quantity",qty);r.put("integralAmount",cost);r.put("message","兑换成功");return AjaxResult.success(r);}
 @GetMapping("/points") public AjaxResult points(){UmsMember m=current();if(m==null)return AjaxResult.error("请先登录");Map<String,Object> r=new HashMap<>();r.put("points",m.getPoints()==null?0:m.getPoints());return AjaxResult.success(r);}
 @GetMapping("/list") public AjaxResult list(@RequestParam(defaultValue="1")long page,@RequestParam(defaultValue="20")long pageSize){UmsMember m=current();if(m==null)return AjaxResult.error("请先登录");Page<TbIntegralFlow> p=new Page<>(page,pageSize);return AjaxResult.success(flowService.page(p,Wrappers.<TbIntegralFlow>lambdaQuery().eq(TbIntegralFlow::getUserId,m.getId()).orderByDesc(TbIntegralFlow::getOperTime)));}
 @PostMapping("/grant") public AjaxResult grant(@RequestBody Map<String,Object> body){if(body==null||body.get("userId")==null)return AjaxResult.error("用户不能为空");Long id=((Number)body.get("userId")).longValue();Number n=body.get("points") instanceof Number?(Number)body.get("points"):null;if(n==null||n.intValue()<=0)return AjaxResult.error("积分必须大于0");flowService.addPoints(id,n.intValue(),2,String.valueOf(body.getOrDefault("remark","平台发放积分")));return AjaxResult.success("发放成功");}
 private UmsMember current(){try{String s=MemberUtils.getMemberId();return s==null||s.isEmpty()?null:memberService.getById(Long.valueOf(s));}catch(Exception e){return null;}}
 public static class IntegralExchangeDTO {private String spuId;private Integer quantity;private Integer payType;private Integer integralAmount;private String addressId;public String getSpuId(){return spuId;}public void setSpuId(String v){spuId=v;}public Integer getQuantity(){return quantity;}public void setQuantity(Integer v){quantity=v;}public Integer getPayType(){return payType;}public void setPayType(Integer v){payType=v;}public Integer getIntegralAmount(){return integralAmount;}public void setIntegralAmount(Integer v){integralAmount=v;}public String getAddressId(){return addressId;}public void setAddressId(String v){addressId=v;}}
}
