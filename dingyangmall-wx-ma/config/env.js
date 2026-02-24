/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 *
 * 全项目唯一后端地址：小程序、所有 request 均通过 utils/api.js 使用本 basePath，端口与后端一致（7500）。
 */
export default {
  // 后端接口根地址（与后端 http-nio-7500 一致）。若有 context-path 请追加，如: 'http://localhost:7500/prod-api'
  basePath: 'http://localhost:7500',
  //广告配置，小程序流量主：https://mp.weixin.qq.com/wxopen/frame
  //广告开关（true/false）
  adEnable: true,
  //Banner广告ID
  adBannerID: 'adunit-9d021cb4da95d1d8',
  //插屏广告ID
  adInsertScreenID: 'adunit-2de35a3c8ce17ce8',
  //激励式广告ID
  adIncentiveID: 'adunit-c4029aba1e16094b',
}
