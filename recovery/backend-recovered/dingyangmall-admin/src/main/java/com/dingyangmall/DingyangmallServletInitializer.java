/*
 * Decompiled with CFR.
 */
package com.dingyangmall;

import com.dingyangmall.DingyangmallApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class DingyangmallServletInitializer
extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DingyangmallApplication.class);
    }
}

