/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.builder.SpringApplicationBuilder
 *  org.springframework.boot.web.servlet.support.SpringBootServletInitializer
 */
package com.dingyangmall;

import com.dingyangmall.DingyangmallApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class DingyangmallServletInitializer
extends SpringBootServletInitializer {
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[]{DingyangmallApplication.class});
    }
}

