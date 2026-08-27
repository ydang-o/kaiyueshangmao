package com.dingyangmall.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import com.dingyangmall.mall.service.TbIntegralRuleService;
import com.dingyangmall.mall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/mall/referral-code")
public class ReferralCodeApi {
 @Autowired private UmsMemberService memberService; @Autowired private TbIntegralFlowService flowService; @Autowired private TbIntegralRuleService ruleService;
 @GetMapping("/members") public AjaxResult members(@RequestParam(defaultValue="1") int current,@RequestParam(defaultValue="20") int size,@RequestParam(required=false) String keyword){com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UmsMember> query=Wrappers.<UmsMember>lambdaQuery().eq(UmsMember::getDelFlag,"0"); if(keyword!=null&&!keyword.trim().isEmpty()){query.and(w->w.like(UmsMember::getPhone,keyword).or().like(UmsMember::getNickname,keyword).or().like(UmsMember::getMemberCode,keyword));} List<UmsMember> all=memberService.list(query);int from=Math.max(0,(current-1)*size),to=Math.min(all.size(),from+size);List<Map<String,Object>> rows=new ArrayList<>();for(UmsMember m:all.subList(from,to)){Map<String,Object> r=new HashMap<>();r.put("id",m.getId());r.put("nickname",m.getNickname());r.put("phone",m.getPhone());r.put("memberCode",m.getMemberCode());r.put("points",m.getPoints());r.put("createTime",m.getCreateTime());rows.add(r);}Map<String,Object> page=new HashMap<>();page.put("records",rows);page.put("total",all.size());page.put("current",current);page.put("size",size);return AjaxResult.success(page);}
 @PostMapping("/gift") public AjaxResult gift(@RequestBody Map<String,Object> body){if(body==null||body.get("targetUserId")==null)return AjaxResult.error("被推荐人不能为空");Long id=((Number)body.get("targetUserId")).longValue();UmsMember target=memberService.getById(id);if(target==null)return AjaxResult.error("用户不存在");Number points=body.get("points") instanceof Number?(Number)body.get("points"):10;flowService.addPoints(id,points.intValue(),6,"推荐奖励");return AjaxResult.success("发放成功");}
 @GetMapping("/records") public AjaxResult records(@RequestParam(defaultValue="1") int current,@RequestParam(defaultValue="20") int size){List<TbIntegralFlow> all=flowService.list(Wrappers.<TbIntegralFlow>lambdaQuery().eq(TbIntegralFlow::getOperType,6).orderByDesc(TbIntegralFlow::getOperTime));int from=Math.max(0,(current-1)*size),to=Math.min(all.size(),from+size);Map<String,Object> page=new HashMap<>();page.put("records",all.subList(from,to));page.put("total",all.size());page.put("current",current);page.put("size",size);return AjaxResult.success(page);}
}
