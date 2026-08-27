/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config.properties;

import com.dingyangmall.common.annotation.Anonymous;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class PermitAllUrlProperties
implements InitializingBean,
ApplicationContextAware {
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    private ApplicationContext applicationContext;
    private List<String> urls = new ArrayList<String>();
    public String ASTERISK = "*";

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map map = mapping.getHandlerMethods();
        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = (HandlerMethod)map.get(info);
            Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
            Optional.ofNullable(method).ifPresent(anonymous -> Objects.requireNonNull(info.getPathPatternsCondition().getPatternValues()).forEach(url -> this.urls.add(RegExUtils.replaceAll(url, PATTERN, this.ASTERISK))));
            Anonymous controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
            Optional.ofNullable(controller).ifPresent(anonymous -> Objects.requireNonNull(info.getPathPatternsCondition().getPatternValues()).forEach(url -> this.urls.add(RegExUtils.replaceAll(url, PATTERN, this.ASTERISK))));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}

