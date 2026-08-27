/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.monitor;

import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.framework.web.domain.Server;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/monitor/server"})
public class ServerController {
    @PreAuthorize(value="@ss.hasPermi('monitor:server:list')")
    @GetMapping
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}

