package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 积分流水（管理端）
 * 查看全平台积分流水、流转明细
 */
@RestController
@AllArgsConstructor
@RequestMapping("/integralflow")
public class TbIntegralFlowController extends BaseController {

    private static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final TbIntegralFlowService integralFlowService;

    /**
     * 分页查询积分流水，支持按用户ID、操作类型、时间筛选
     * beginTime/endTime 格式：yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:integralflow:list')")
    public AjaxResult page(Page<TbIntegralFlow> page,
                           @RequestParam(required = false) Long userId,
                           @RequestParam(required = false) Integer operType,
                           @RequestParam(required = false) String beginTime,
                           @RequestParam(required = false) String endTime) {
        LocalDateTime begin = parseTime(beginTime, true);
        LocalDateTime end = parseTime(endTime, false);
        var wrapper = Wrappers.<TbIntegralFlow>lambdaQuery()
                .eq(userId != null, TbIntegralFlow::getUserId, userId)
                .eq(operType != null, TbIntegralFlow::getOperType, operType)
                .ge(begin != null, TbIntegralFlow::getOperTime, begin)
                .le(end != null, TbIntegralFlow::getOperTime, end)
                .orderByDesc(TbIntegralFlow::getOperTime);
        com.baomidou.mybatisplus.core.metadata.IPage<TbIntegralFlow> result = integralFlowService.page(page, wrapper);
        return AjaxResult.success(result);
    }

    /** 管理端/经销商手动发放积分。 */
    @PostMapping("/grant")
    @PreAuthorize("@ss.hasPermi('mall:integralflow:grant')")
    public AjaxResult grant(@RequestBody Map<String, Object> body) {
        if (body == null || body.get("userId") == null) return AjaxResult.error("用户不能为空");
        Number points = body.get("points") instanceof Number ? (Number) body.get("points") : null;
        if (points == null || points.intValue() <= 0) return AjaxResult.error("积分必须大于0");
        Long userId = ((Number) body.get("userId")).longValue();
        String remark = body.get("remark") == null ? "平台发放积分" : String.valueOf(body.get("remark"));
        integralFlowService.addPoints(userId, points.intValue(), 2, remark);
        return AjaxResult.success("发放成功");
    }

    private static LocalDateTime parseTime(String s, boolean startOfDay) {
        if (!StringUtils.hasText(s)) return null;
        try {
            if (s.length() > 10) return LocalDateTime.parse(s, DATETIME);
            return startOfDay ? LocalDate.parse(s, DATE).atStartOfDay() : LocalDate.parse(s, DATE).atTime(23, 59, 59);
        } catch (Exception e) {
            return null;
        }
    }
}
