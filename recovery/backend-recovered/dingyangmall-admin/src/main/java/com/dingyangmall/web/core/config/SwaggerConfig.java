/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.core.config;

import com.dingyangmall.common.config.DingyangmallConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Autowired
    private DingyangmallConfig ruoyiConfig;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().components(new Components().addSecuritySchemes("apikey", this.securityScheme())).addSecurityItem(new SecurityRequirement().addList("apikey")).info(this.getApiInfo());
    }

    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.APIKEY).name("Authorization").in(SecurityScheme.In.HEADER).scheme("Bearer");
    }

    public Info getApiInfo() {
        return new Info().title("\u5982\u56cd\u4e25\u9009\u63a5\u53e3\u6587\u6863").description("\u7ba1\u7406\u7aef\u3001\u79fb\u52a8\u7aef\uff08\u5c0f\u7a0b\u5e8f/App\uff09\u3001\u5546\u5bb6\u7aef\u5168\u90e8\u63a5\u53e3\uff0c\u53ef\u5728\u5de6\u4fa7\u5206\u7ec4\u6216\u300c\u5168\u90e8\u63a5\u53e3\u300d\u4e2d\u67e5\u770b\u3002").contact(new Contact().name(this.ruoyiConfig.getName())).version("v" + this.ruoyiConfig.getVersion());
    }
}

