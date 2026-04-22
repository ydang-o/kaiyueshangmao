package com.dingyangmall.web.controller.tool;

import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 工具 API Controller
 *
 * @author dingyangmall
 */
@Slf4j
@RestController
@RequestMapping({"/tool", "/dev-api/tool", "/prod-api/tool"})
public class ToolController extends BaseController {

    /**
     * 测试连接
     */
    @GetMapping("/test")
    public AjaxResult test() {
        return AjaxResult.success("连接成功");
    }

    /**
     * 获取服务器时间
     */
    @GetMapping("/serverTime")
    public AjaxResult getServerTime() {
        Map<String, Object> data = new HashMap<>();
        data.put("serverTime", System.currentTimeMillis());
        return AjaxResult.success(data);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public AjaxResult health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        return AjaxResult.success(data);
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/sysInfo")
    public AjaxResult getSysInfo() {
        Map<String, Object> data = new HashMap<>();
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("osName", System.getProperty("os.name"));
        data.put("osVersion", System.getProperty("os.version"));
        return AjaxResult.success(data);
    }
}
