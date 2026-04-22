package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 微信消息 API
 *
 * @author dingyangmall
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxmsg")
public class WxMsgApi {

    /**
     * 发送模板消息
     */
    @PostMapping("/send")
    public AjaxResult sendTemplateMsg(@RequestBody Object msgData) {
        // TODO: 实现发送模板消息逻辑
        return AjaxResult.success("消息发送成功");
    }

    /**
     * 获取消息列表
     */
    @GetMapping("/list")
    public AjaxResult getMsgList() {
        // TODO: 实现获取消息列表逻辑
        return AjaxResult.success();
    }
}
