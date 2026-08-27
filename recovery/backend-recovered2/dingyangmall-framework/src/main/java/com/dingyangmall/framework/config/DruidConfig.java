/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.spring.boot3.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import com.dingyangmall.common.enums.DataSourceType;
import com.dingyangmall.common.utils.spring.SpringUtils;
import com.dingyangmall.framework.config.properties.DruidProperties;
import com.dingyangmall.framework.datasource.DynamicDataSource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(value="spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties(value="spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix="spring.datasource.druid.slave", name={"enabled"}, havingValue="true")
    public DataSource slaveDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean(name={"dynamicDataSource"})
    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource) {
        HashMap<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        this.setDataSource(targetDataSources, DataSourceType.SLAVE.name(), "slaveDataSource");
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

    public void setDataSource(Map<Object, Object> targetDataSources, String sourceName, String beanName) {
        try {
            DataSource dataSource = (DataSource)SpringUtils.getBean(beanName);
            targetDataSources.put(sourceName, dataSource);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Bean
    @ConditionalOnProperty(name={"spring.datasource.druid.statViewServlet.enabled"}, havingValue="true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties) {
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        String filePath = "support/http/resources/js/common.js";
        Filter filter = new Filter(){

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
                response.resetBuffer();
                String text = Utils.readFromResource("support/http/resources/js/common.js");
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }

            @Override
            public void destroy() {
            }
        };
        FilterRegistrationBean<1> registrationBean = new FilterRegistrationBean<1>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}

