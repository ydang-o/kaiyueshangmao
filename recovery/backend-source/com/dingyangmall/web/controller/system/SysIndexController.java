/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.config.DingyangmallConfig
 *  com.dingyangmall.common.utils.StringUtils
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysIndexController {
    @Autowired
    private DingyangmallConfig ruoyiConfig;

    @RequestMapping(value={"/"})
    public String index() {
        return StringUtils.format((String)"\u6b22\u8fce\u4f7f\u7528{}\u540e\u53f0\u7ba1\u7406\u6846\u67b6\uff0c\u5f53\u524d\u7248\u672c\uff1av{}\uff0c\u8bf7\u901a\u8fc7\u524d\u7aef\u5730\u5740\u8bbf\u95ee\u3002", (Object[])new Object[]{this.ruoyiConfig.getName(), this.ruoyiConfig.getVersion()});
    }
}

