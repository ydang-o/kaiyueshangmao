# README 接口联调执行清单

本清单按 `README.md` 的移动端接口顺序设计，目标是快速定位问题并确认前后端契约一致。

## 0. 联调前准备

- 后端确认启动：`http-nio-7500`。
- 前端基地址：`config/env.js` 的 `basePath = http://localhost:7500`。
- 已开启联调日志：`config/env.js` 的 `apiDebug = true`。
- 前端请求层支持成功码：`code = 200 / 0`（已兼容）。

## 1. 先打通登录与会话

1. 小程序登录：`POST /weixin/api/ma/wxuser/login`
2. 会员信息：`GET /app/member/info`
3. 异常验证：会话失效时返回 `code = 60001`，前端应自动清 session 并重拉页面

## 2. 商品域联调

1. 分类树：`GET /weixin/api/ma/goodscategory/tree`
2. 商品分页：`GET /weixin/api/ma/goodsspu/page`
3. 商品详情：`GET /weixin/api/ma/goodsspu/{id}`
4. 可选公告：`GET /weixin/api/ma/notice/list`（无此接口时首页可不展示公告）

## 3. 购物车联调

1. 列表：`GET /weixin/api/ma/shoppingcart/page`
2. 新增：`POST /weixin/api/ma/shoppingcart`
3. 修改：`PUT /weixin/api/ma/shoppingcart`
4. 删除：`POST /weixin/api/ma/shoppingcart/del`
5. 数量：`GET /weixin/api/ma/shoppingcart/count`

## 4. 交易链路联调

1. 提交订单：`POST /weixin/api/ma/orderinfo`
2. 统一下单：`POST /weixin/api/ma/orderinfo/unifiedOrder`
3. 订单列表：`GET /weixin/api/ma/orderinfo/page`
4. 订单详情：`GET /weixin/api/ma/orderinfo/{id}`
5. 取消订单：`PUT /weixin/api/ma/orderinfo/cancel/{id}`
6. 确认收货：`PUT /weixin/api/ma/orderinfo/receive/{id}`
7. 退款申请：`POST /weixin/api/ma/orderinfo/refunds`
8. 物流信息：`GET /weixin/api/ma/orderinfo/logistics/{id}`
9. 删除订单：`DELETE /weixin/api/ma/orderinfo/{id}`
10. 订单统计：`GET /weixin/api/ma/orderinfo/countAll`

## 5. 用户资产联调

1. 地址分页：`GET /weixin/api/ma/useraddress/page`
2. 地址保存：`POST /weixin/api/ma/useraddress`
3. 地址删除：`DELETE /weixin/api/ma/useraddress/{id}`
4. 我的优惠券：`GET /app/coupon/my`
5. 签到：`POST /app/member/sign-in`
6. 发短信：`GET /app/member/send-sms-code`
7. 发红包：`POST /app/member/send-packet`

## 6. 抽奖联调

1. 抽奖配置：`GET /app/lottery/config`
2. 抽奖：`POST /app/lottery/draw`
3. 抽奖记录：`GET /app/lottery/record`

## 7. 商家端联调

1. 验证码：`GET /captchaImage`
2. 登录：`POST /login`
3. 扫会员码：`GET /api/mall/merchant/scan/user/{code}`
4. 赠送积分：`POST /api/mall/merchant/scan/points`
5. 核销券：`POST /api/mall/merchant/scan/coupon/verify`

## 8. 常见问题快速定位

- 弹窗 `No static resource weixin/api/ma/wxuser/login`：后端路由未命中 API，被当成静态资源处理；需检查 Controller 映射和网关转发。
- 前端显示失败但后端返回 `code=0`：已在请求层兼容；若仍失败，检查 `statusCode` 与返回 JSON 结构。
- 一直走旧端口：仅看 `config/env.js` 的 `basePath`；编译产物目录 `unpackage` 不作为配置源。
