/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
package com.dingyangmall.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 商城相关配置
 *
 * @author
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mall")
public class MallConfigProperties {

	private String notifyHost = "notify-host";

	/** 原物流相关 key，保留兼容 */
	private String logisticsKey = "logistics-key";

	/** 快递100（Api100）授权 Key，用于实时查询签名 */
	private String kuaidi100Key = "";
	/** 快递100（Api100）公司编号 Customer */
	private String kuaidi100Customer = "";
}

