package com.dingyangmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@EnableScheduling
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class DingyangmallApplication
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DingyangmallApplication.class);

    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(DingyangmallApplication.class, args);
        log.info("如囍优选启动成功");
    }
}


