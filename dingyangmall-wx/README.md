# 鼎阳商城 - 接口文档

本文档汇总 **管理端（dingyangmall-admin）** 与 **移动端**（小程序、Android、iOS）所调用的全部后端接口。  
**请求根地址**：以各端配置为准（如小程序 `config/env.js` 的 `basePath`、App 端配置的 baseUrl），默认 `http://localhost:7500`，应用 `context-path: /`。

---

## 文档结构说明

| 端 | 路径前缀 | 认证方式 | 说明 |
|----|----------|----------|------|
| **管理端** | 无统一前缀（如 `/system`、`/monitor`、`/orderinfo` 等） | `Authorization: Bearer {token}` | 后台 PC 管理，需管理员登录 |
| **移动端** | `/weixin/api/ma/*`、`/app/*` | `third-session` 等（见下方通用说明） | 小程序、Android、iOS 用户端共用 |
| **商家端** | `/api/mall/merchant/*`、`/login`、`/captchaImage` | `Authorization: Bearer {merchantToken}` | 商家扫码、核销等，与用户端登录分离 |

---

# 第一部分：管理端接口

以下为 **dingyangmall-admin** 后台管理端全部 REST 接口。

---

## 一、首页与登录

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/` | 首页欢迎语 | 无 |
| POST | `/login` | 登录（用户名、密码、验证码、uuid） | 无 |
| GET | `/getInfo` | 获取当前用户信息、角色、权限 | 需登录 |
| GET | `/getRouters` | 获取当前用户路由/菜单树 | 需登录 |
| POST | `/register` | 用户注册 | 无（需配置开启） |
| POST | `/registerDistributor` | 分销商注册 | 无（需配置开启） |

---

## 二、通用与公共

### 2.1 验证码与短信

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/captchaImage` | 获取图形/数学验证码（返回 uuid、img base64） | 无 |
| GET | `/common/sms/send` | 发送短信验证码（Query: phone） | 无 |

### 2.2 文件与下载

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/profile/file/{id}` | 根据文件 ID 查看/下载文件（MySQL 存储） | 无 |
| GET | `/common/download` | 通用文件下载（fileName, delete） | - |
| GET | `/common/download/resource` | 本地资源下载（resource） | - |
| POST | `/common/upload` | 单文件上传（存 MySQL） | - |
| POST | `/common/uploads` | 多文件上传（存 MySQL） | - |

---

## 三、系统管理

### 3.1 个人信息（/system/user/profile）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/system/user/profile` | 获取当前登录用户个人信息 | 需登录 |
| PUT | `/system/user/profile` | 修改个人信息（昵称、邮箱、手机、性别） | 需登录 |
| PUT | `/system/user/profile/updatePwd` | 修改密码（oldPassword, newPassword） | 需登录 |
| POST | `/system/user/profile/avatar` | 上传头像（avatarfile） | 需登录 |

### 3.2 仪表盘（/system/dashboard）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/system/dashboard/data` | 仪表盘数据（用户总数、积分发放量、待发货订单、今日核销数） | - |

### 3.3 通知公告（/system/notice）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/system/notice/list` | 分页查询通知公告列表 | system:notice:list |
| GET | `/system/notice/{noticeId}` | 根据 ID 获取公告详情 | system:notice:query |
| POST | `/system/notice` | 新增通知公告 | system:notice:add |
| PUT | `/system/notice` | 修改通知公告 | system:notice:edit |
| DELETE | `/system/notice/{noticeIds}` | 删除通知公告（支持多 ID） | system:notice:remove |

---

## 四、系统监控

### 4.1 在线用户（/monitor/online）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/monitor/online/list` | 在线用户列表（可选 ipaddr、userName） | monitor:online:list |
| DELETE | `/monitor/online/{tokenId}` | 强退指定用户 | monitor:online:forceLogout |

### 4.2 操作日志（/monitor/operlog）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/monitor/operlog/list` | 分页查询操作日志 | monitor:operlog:list |
| POST | `/monitor/operlog/export` | 导出操作日志 Excel | monitor:operlog:export |
| DELETE | `/monitor/operlog/{operIds}` | 删除操作日志 | monitor:operlog:remove |
| DELETE | `/monitor/operlog/clean` | 清空操作日志 | monitor:operlog:remove |

### 4.3 登录日志（/monitor/logininfor）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/monitor/logininfor/list` | 分页查询登录日志 | monitor:logininfor:list |
| POST | `/monitor/logininfor/export` | 导出登录日志 Excel | monitor:logininfor:export |
| DELETE | `/monitor/logininfor/{infoIds}` | 删除登录日志 | monitor:logininfor:remove |
| DELETE | `/monitor/logininfor/clean` | 清空登录日志 | monitor:logininfor:remove |
| GET | `/monitor/logininfor/unlock/{userName}` | 解锁指定用户账户 | monitor:logininfor:unlock |

### 4.4 服务器监控（/monitor/server）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/monitor/server` | 获取服务器信息（CPU、内存、磁盘等） | monitor:server:list |

---

## 五、商城业务（Mall）- 管理端

### 5.1 抽奖（/mall/lottery）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/mall/lottery/config` | 获取当前抽奖配置 | mall:lottery:config |
| POST | `/mall/lottery/config` | 保存抽奖配置 | mall:lottery:config |
| GET | `/mall/lottery/record/page` | 分页查询抽奖记录 | mall:lottery:record |

### 5.2 商品券（/mall/coupon）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/mall/coupon/page` | 分页查询商品券 | mall:coupon:index |
| GET | `/mall/coupon/{id}` | 根据 ID 查询商品券 | mall:coupon:get |
| POST | `/mall/coupon` | 新增商品券 | mall:coupon:add |
| PUT | `/mall/coupon` | 修改商品券 | mall:coupon:edit |
| DELETE | `/mall/coupon/{id}` | 删除商品券 | mall:coupon:del |

### 5.3 积分规则（/integralrule）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/integralrule/page` | 分页查询积分规则 | mall:integralrule:index |
| GET | `/integralrule/{id}` | 根据 ID 查询积分规则 | mall:integralrule:get |
| POST | `/integralrule` | 新增积分规则 | mall:integralrule:add |
| PUT | `/integralrule` | 修改积分规则 | mall:integralrule:edit |
| DELETE | `/integralrule/{id}` | 删除积分规则 | mall:integralrule:del |

### 5.4 订单（/orderinfo）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/orderinfo/page` | 分页查询订单 | mall:orderinfo:index |
| GET | `/orderinfo/count` | 按条件统计订单数量 | - |
| GET | `/orderinfo/{id}` | 订单详情（含物流、用户信息） | mall:orderinfo:get |
| POST | `/orderinfo` | 新增订单 | mall:orderinfo:add |
| PUT | `/orderinfo` | 修改订单 | mall:orderinfo:edit |
| DELETE | `/orderinfo/{id}` | 删除订单 | mall:orderinfo:del |
| PUT | `/orderinfo/cancel/{id}` | 取消订单（仅未支付） | mall:orderinfo:edit |
| PUT | `/orderinfo/doOrderRefunds` | 操作退款（RequestBody: OrderItem） | mall:orderinfo:edit |

### 5.5 会员（/mall/member）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/mall/member/page` | 分页查询会员 | mall:member:index |
| GET | `/mall/member/{id}` | 根据 ID 查询会员 | mall:member:get |
| POST | `/mall/member` | 新增会员 | mall:member:add |
| PUT | `/mall/member` | 修改会员 | mall:member:edit |
| DELETE | `/mall/member/{id}` | 删除会员 | mall:member:del |

### 5.6 购物车（/shoppingcart）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/shoppingcart/page` | 分页查询购物车 | mall:shoppingcart:index |
| GET | `/shoppingcart/{id}` | 根据 ID 查询购物车 | mall:shoppingcart:get |
| POST | `/shoppingcart` | 新增购物车 | mall:shoppingcart:add |
| PUT | `/shoppingcart` | 修改购物车 | mall:shoppingcart:edit |
| DELETE | `/shoppingcart/{id}` | 删除购物车 | mall:shoppingcart:del |

### 5.7 用户收货地址（/useraddress）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/useraddress/page` | 分页查询用户地址 | mall:useraddress:index |
| GET | `/useraddress/{id}` | 根据 ID 查询地址 | mall:useraddress:get |
| POST | `/useraddress` | 新增用户地址 | mall:useraddress:add |
| PUT | `/useraddress` | 修改用户地址 | mall:useraddress:edit |
| DELETE | `/useraddress/{id}` | 删除用户地址 | mall:useraddress:del |

### 5.8 SPU 商品（/goodsspu）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/goodsspu/page` | 分页查询 SPU 商品 | mall:goodsspu:index |
| GET | `/goodsspu/list` | 列表查询（id、name 等简易字段） | - |
| GET | `/goodsspu/count` | 按条件统计商品数量 | - |
| GET | `/goodsspu/{id}` | 根据 ID 查询 SPU 详情 | mall:goodsspu:get |
| POST | `/goodsspu` | 新增 SPU 商品 | mall:goodsspu:add |
| PUT | `/goodsspu` | 修改 SPU 商品 | mall:goodsspu:edit |
| PUT | `/goodsspu/shelf` | 商品上下架（shelf, ids） | mall:goodsspu:edit |
| DELETE | `/goodsspu/{id}` | 删除 SPU 商品 | mall:goodsspu:del |

### 5.9 商品类目（/goodscategory）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/goodscategory/page` | 分页查询商品类目 | mall:goodscategory:index |
| GET | `/goodscategory/tree` | 获取类目树形结构 | mall:goodscategory:index |
| GET | `/goodscategory/{id}` | 根据 ID 查询类目 | mall:goodscategory:get |
| POST | `/goodscategory` | 新增商品类目 | mall:goodscategory:add |
| PUT | `/goodscategory` | 修改商品类目 | mall:goodscategory:edit |
| DELETE | `/goodscategory/{id}` | 删除商品类目 | mall:goodscategory:del |

### 5.10 订单物流（/orderlogistics）

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/orderlogistics/page` | 分页查询订单物流 | mall:orderlogistics:index |
| GET | `/orderlogistics/{id}` | 根据 ID 查询物流 | mall:orderlogistics:get |
| POST | `/orderlogistics` | 新增订单物流 | mall:orderlogistics:add |
| PUT | `/orderlogistics` | 修改订单物流 | mall:orderlogistics:edit |
| DELETE | `/orderlogistics/{id}` | 删除订单物流 | mall:orderlogistics:del |
| GET | `/orderlogistics/dict/{type}` | 获取物流相关枚举字典 | - |

---

# 第二部分：移动端（小程序 / Android / iOS）接口

以下为 **移动端**（小程序、Android、iOS）共用的接口，路径均相对于 basePath（如 `http://localhost:7500`）。  
当前文档以小程序项目 **dingyangmall-wx-ma** 的调用为准，**Android、iOS App** 对接同一套路径与协议即可。  
小程序请求会带 `app-id`；用户登录后各端带统一登录态（如小程序 `third-session`、App 端 Token 等）。

---

## 一、认证与用户

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 1 | 小程序登录 | POST | `/weixin/api/ma/wxuser/login` | App.vue 静默登录、商家相关 | 入参含 jsCode 等 |
| 2 | 微信用户新增 | POST | `/weixin/api/ma/wxuser` | 用户信息完善 | |
| 3 | 会员信息查询 | GET | `/app/member/info` | 个人中心、订单等 | 需登录态 |
| 4 | 每日签到 | POST | `/app/member/sign-in` | 我的-每日签到 | |
| 5 | 发送短信验证码 | GET | `/app/member/send-sms-code` | 积分红包等 | Query: phone |
| 6 | 发送积分红包 | POST | `/app/member/send-packet` | 积分红包页 | |

---

## 二、商品与分类

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 7 | 商品分类树 | GET | `/weixin/api/ma/goodscategory/tree` | 首页宫格、分类页 | |
| 8 | 商品分页列表 | GET | `/weixin/api/ma/goodsspu/page` | 首页、商品列表、搜索 | current, size, categorySecond, keyword, hot 等 |
| 9 | 商品详情 | GET | `/weixin/api/ma/goodsspu/{id}` | 商品详情页 | 路径参数 id |

---

## 三、公告（可选）

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 10 | 公告列表 | GET | `/weixin/api/ma/notice/list` | 首页公告轮播 | 无此接口时首页不展示公告 |

---

## 四、购物车

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 11 | 购物车分页 | GET | `/weixin/api/ma/shoppingcart/page` | 购物车页 | |
| 12 | 购物车新增 | POST | `/weixin/api/ma/shoppingcart` | 商品详情加购 | |
| 13 | 购物车修改 | PUT | `/weixin/api/ma/shoppingcart` | 购物车改数量等 | |
| 14 | 购物车删除 | POST | `/weixin/api/ma/shoppingcart/del` | 购物车删除 | |
| 15 | 购物车数量 | GET | `/weixin/api/ma/shoppingcart/count` | TabBar 角标、个人中心等 | |

---

## 五、订单

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 16 | 订单提交 | POST | `/weixin/api/ma/orderinfo` | 确认订单页 | |
| 17 | 订单分页 | GET | `/weixin/api/ma/orderinfo/page` | 订单列表 | status, current, size |
| 18 | 订单详情 | GET | `/weixin/api/ma/orderinfo/{id}` | 订单详情页 | |
| 19 | 订单取消 | PUT | `/weixin/api/ma/orderinfo/cancel/{id}` | 订单详情 | |
| 20 | 申请退款 | POST | `/weixin/api/ma/orderinfo/refunds` | 申请退款页 | |
| 21 | 确认收货 | PUT | `/weixin/api/ma/orderinfo/receive/{id}` | 订单详情 | |
| 22 | 订单物流 | GET | `/weixin/api/ma/orderinfo/logistics/{id}` | 物流信息页 | |
| 23 | 订单删除 | DELETE | `/weixin/api/ma/orderinfo/{id}` | 订单列表/详情 | |
| 24 | 订单各状态数量 | GET | `/weixin/api/ma/orderinfo/countAll` | 个人中心角标等 | 可带状态 |
| 25 | 统一下单(支付) | POST | `/weixin/api/ma/orderinfo/unifiedOrder` | 确认订单支付 | |

---

## 六、收货地址

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 26 | 地址分页 | GET | `/weixin/api/ma/useraddress/page` | 收货地址列表 | |
| 27 | 地址新增 | POST | `/weixin/api/ma/useraddress` | 编辑地址页 | |
| 28 | 地址删除 | DELETE | `/weixin/api/ma/useraddress/{id}` | 地址列表 | |

---

## 七、优惠券

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 29 | 我的优惠券 | GET | `/app/coupon/my` | 我的优惠券页 | Query: status |

---

## 八、抽奖

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 30 | 抽奖配置 | GET | `/app/lottery/config` | 积分抽奖页 | |
| 31 | 抽奖 | POST | `/app/lottery/draw` | 积分抽奖页 | |
| 32 | 抽奖记录 | GET | `/app/lottery/record` | 积分抽奖页 | |

---

## 九、商家端（需商家 Token）

| 序号 | 接口说明 | 方法 | 路径 | 前端调用处 | 备注 |
|------|----------|------|------|------------|------|
| 33 | 图形验证码 | GET | `/captchaImage` | 商家登录页 | 与管理端共用 |
| 34 | 商家登录 | POST | `/login` | 商家登录页 | 与小程序用户登录不同 |
| 35 | 扫码用户 | GET | `/api/mall/merchant/scan/user/{code}` | 商家扫码 | Header: Authorization |
| 36 | 赠送积分 | POST | `/api/mall/merchant/scan/points` | 商家扫码 | |
| 37 | 核销优惠券 | POST | `/api/mall/merchant/scan/coupon/verify` | 商家核销 | |

---

## 移动端接口实现说明

- **小程序登录**：`POST /weixin/api/ma/wxuser/login` 已实现，严格遵循微信官方服务端 API [小程序登录凭证校验 code2Session](https://developers.weixin.qq.com/miniprogram/dev/server/API/user-login/api_code2session.html)（接口路径 `GET https://api.weixin.qq.com/sns/jscode2session`）。**登录需用户主动点击「微信一键登录」授权，前端不会在进入页面时自动调 wx.login。**  
  - 流程：用户点击按钮后，小程序调用 **wx.login** 获取临时凭证 **code**，后端用 code 调用官方 **code2Session** 换取 **openid**（用户唯一标识）、**session_key**（会话密钥）、**unionid**（若已绑定开放平台）。以官方返回的 **openid 作为唯一 session 与用户标识**，不再使用自建 token；会话存 Redis 键 `wx:openid_session:{openid}`。**session_key 仅存服务端，绝不下发。**  
  - 入参：JSON 含 `code` 或 `jsCode`。返回 `thirdSession`（=openid）、`userId`（=openid）、`openid`；若该用户曾保存过昵称/头像则一并返回。  
  - 配置：**必须**在 `application.yml` 的 `wx.ma.configs` 中配置真实的小程序 AppID 和 AppSecret。  
  - **数据库**：登录与更新用户信息写入**旧表 `wx_user`**（字段映射：openid→open_id，昵称→nick_name，头像→headimg_url，app_type=1 表示小程序）。使用项目主 SQL（如 `sql/dingyangmall_ry_fixed.sql`）中的 `wx_user` 表即可，无需新建表。  
  - 同一 code 重复请求（如用户双击、前端重试）：后端幂等处理，会返回该 code 已对应的 openid 及会话；仅当无法复用会话时才提示重新点击「微信一键登录」。  
  - 错误码（与官方一致）：40029（code 无效）、40163（code 已被使用，后端会尽量复用已建会话）；40013/40125 为 appId/appSecret 配置错误。
- **获取当前用户信息**：`GET /weixin/api/ma/wxuser/info`，Header 携带 `third-session` 或 `openid`（值为微信官方 openid），返回 `userId`、`openid`、`nickname`、`avatarUrl`（若有）。
- **更新用户信息**：`POST /weixin/api/ma/wxuser`，Header 携带 `third-session` 或 `openid`（值为 openid）。支持两种方式：（1）body 传 `encryptedData`、`iv`（如 wx.getUserProfile 返回），后端用 sessionKey 解密得到昵称/头像并落库；（2）body 直接传明文 `nickname`、`avatarUrl`（如头像/昵称按钮返回）。昵称、头像会写入 Redis，下次登录或调 `/info` 时带出。
- **获取手机号**：`POST /weixin/api/ma/wxuser/phone` 已实现。前端 button `open-type="getPhoneNumber"` 回调拿到 `code` 后，请求本接口（body 传 `code`，可选传 `_thirdSession` 兜底）；需登录态（Header 的 third-session/openid）。后端调用微信 getuserphonenumber 用 code 换取手机号，返回 `phoneNumber`、`purePhoneNumber`、`countryCode`，并将手机号写入当前会话。**前提**：小程序已企业认证且在公众平台接口设置中开通「手机号」能力（见下一条）。
- **微信公众平台配置（头像、昵称、手机号）**：若小程序端无法正常获取头像、昵称或手机号，多半与**微信公众平台与能力配置**有关，需在 [微信公众平台](https://mp.weixin.qq.com) 登录你的小程序后逐项核对：  
  - **登录与基础**：在 **开发 → 开发管理 → 开发设置** 中确认 **AppID、AppSecret** 与后端 `application.yml` 中 `wx.ma.configs` 一致；**服务器域名** 里 request 合法域名包含你的后端域名（如 `https://your-api.com`）。  
  - **头像与昵称**：微信已收紧「直接拉取用户头像昵称」能力。当前推荐使用 [头像昵称填写能力](https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/userProfile.html)（按钮 `open-type="chooseAvatar"` + 输入框 `type="nickname"`），由用户主动选择头像、填写昵称。若仍使用 `wx.getUserProfile`，需在**用户点击按钮**时调用，且基础库 2.10.4+；部分版本会限制弹窗或不再返回头像/昵称，需改用「头像昵称填写」组件。  
  - **手机号**：获取用户手机号需同时满足：**（1）小程序主体为组织（企业/政府等）且完成微信认证**（个人主体无法开通）；（2）在 **开发 → 开发管理 → 接口设置** 中开通 **「手机号」** 能力；（3）自 2023 年起手机号能力为付费接口（有免费额度，超出需在 **付费管理** 购买）；（4）前端通过 button `open-type="getPhoneNumber"` 获取 **code**，后端需调用微信 [getPhoneNumber](https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html) 接口（`POST https://api.weixin.qq.com/wxa/business/getuserphonenumber`）用 code 换取手机号。若后端尚未实现该接口，需在服务端用 code + access_token 调微信接口解密得到手机号后再落库或返回前端。  
  - 总结：**拿不到头像/昵称** 时优先检查是否改用「头像昵称填写」组件、是否在按钮点击中调用；**拿不到手机号** 时优先检查是否已企业认证、是否在接口设置中开通手机号能力、后端是否实现 getPhoneNumber 解密接口。
- **公告列表**：`GET /weixin/api/ma/notice/list` 已实现，返回状态为正常的公告，供首页轮播使用。
- **购物车/订单等需登录态接口**：请求时 Header 携带 `third-session` 或 `openid`（值为微信官方 openid）。后端通过 `WxMaMemberFilter` 根据该 openid 从 Redis `wx:openid_session:{openid}` 校验会话并注入 memberId，供 `MemberUtils.getMemberId()` 使用。若仍返回 **60001「登录超时」**，请确认 **dingyangmall-framework** 中已将 `/weixin/api/ma/**` 配置为匿名路径（不校验 JWT）。
- **60002「session不能为空」**：若未登录时访问 `/weixin/api/ma/goodscategory/tree`、商品列表等接口返回 60002，说明框架对所有 `/weixin/api/ma/*` 做了 session 校验。可选方案：（1）在 **dingyangmall-framework** 中将公开接口（如 `/weixin/api/ma/wxuser/login`、`/weixin/api/ma/goodscategory/**`、`/weixin/api/ma/goodsspu/**`、`/weixin/api/ma/notice/**`）配置为匿名/免 session；（2）或保持现状，小程序端已在启动时先静默登录再调需 session 的接口，用户需先完成一次登录后才能正常浏览分类与商品。
- 其余移动端接口与现有 Controller/Api 一致，按上表路径调用即可。

---

# 第三部分：接口对照与通用说明

## 管理端 vs 移动端路径对照（同业务）

| 业务 | 管理端路径 | 移动端路径 |
|------|------------|------------|
| 商品分类树 | `/goodscategory/tree` | `/weixin/api/ma/goodscategory/tree` |
| 商品分页 | `/goodsspu/page` | `/weixin/api/ma/goodsspu/page` |
| 商品详情 | `/goodsspu/{id}` | `/weixin/api/ma/goodsspu/{id}` |
| 购物车 | `/shoppingcart/*` | `/weixin/api/ma/shoppingcart/*` |
| 订单 | `/orderinfo/*` | `/weixin/api/ma/orderinfo/*` |
| 收货地址 | `/useraddress/*` | `/weixin/api/ma/useraddress/*` |
| 抽奖配置 | `/mall/lottery/config` | `/app/lottery/config` |
| 短信验证码 | `/common/sms/send` | `/app/member/send-sms-code` |
| 验证码图片 | `/captchaImage` | 商家端共用 `/captchaImage` |

移动端（小程序、Android、iOS）在部分能力上会多出：登录/会员、签到、积分红包、公告、优惠券、抽奖 draw/record、统一下单、确认收货、申请退款、物流查询、购物车 count、订单 countAll 等，需后端提供对应接口或与现有管理端接口对齐。

---

## 第三方服务配置（短信与快递查询）

### 〇、微信接口连接超时（Connect to api.weixin.qq.com timed out）

若日志出现 `WxRuntimeException: ConnectTimeoutException: Connect to api.weixin.qq.com:443 ... Connect timed out`，说明**本机访问微信接口服务器超时**，属于网络/环境问题，可依次排查：

1. **网络是否可达**  
   在部署后端的那台机器上执行：`ping api.weixin.qq.com` 或 `curl -I https://api.weixin.qq.com`，确认能连通。

2. **DNS 是否正常**  
   若解析出的 IP 异常（例如 `198.18.x.x` 等非微信常见网段），可更换 DNS（如 114.114.114.114、8.8.8.8）或在本机 hosts 中绑定：  
   `api.weixin.qq.com` → 微信官方解析得到的 IP（以实际为准）。

3. **是否需要代理**  
   若服务器只能通过 HTTP/HTTPS 代理访问外网，需在 JVM 启动参数或环境中配置代理，例如：  
   `-Dhttps.proxyHost=代理主机 -Dhttps.proxyPort=端口`，或使用可配置代理的 HTTP 客户端（若项目中有对 WxMa 的 HttpClient 自定义）。

4. **防火墙/安全组**  
   确认放行本机对 `api.weixin.qq.com:443` 的出站访问。

后端已对“连接微信服务器超时”做友好提示：接口会返回「连接微信服务器超时，请检查服务器网络或配置代理后重试」，便于前端统一提示用户。

---

### 一、腾讯云短信（验证码）

- **用途**：发送短信验证码（登录、注册、积分红包等）。
- **实现**：`dingyangmall-framework` 中 `SmsService`，未配置或未启用时仅将验证码写入 Redis（模拟发送，不真实发短信）。
- **配置**：在 `application.yml` 或环境变量中配置以下项（均以 `tencent.sms` 为前缀）：

| 配置项 | 说明 | 环境变量示例 |
|--------|------|--------------|
| enabled | 是否启用腾讯云发送，false 时仅模拟 | TENCENT_SMS_ENABLED |
| secret-id | 腾讯云 CAM SecretId | TENCENT_SMS_SECRET_ID |
| secret-key | 腾讯云 CAM SecretKey | TENCENT_SMS_SECRET_KEY |
| sms-sdk-app-id | 短信控制台「应用列表」中的 SdkAppId | TENCENT_SMS_SDK_APP_ID |
| sign-name | 已审核通过的短信签名内容 | TENCENT_SMS_SIGN_NAME |
| template-id | 验证码模板 ID（如：您的验证码为：{1}，5分钟内有效。） | TENCENT_SMS_TEMPLATE_ID |
| region | 地域，默认 ap-guangzhou | - |

- **相关接口**：管理端 `GET /common/sms/send?phone=xxx`；移动端 `GET /app/member/send-sms-code?phone=xxx`。

### 二、快递100（Api100）物流查询

- **用途**：订单物流详情接口的实时轨迹（移动端「物流信息」页）。
- **实现**：`dingyangmall-mall` 中 `Kuaidi100QueryService`（实现类 `Kuaidi100QueryServiceImpl`），通过快递100实时查询接口拉取轨迹；未配置 key/customer 时仅返回本地订单物流信息，不调第三方。
- **配置**：在 `application.yml` 的 `mall` 下或环境变量配置：

| 配置项 | 说明 | 环境变量示例 |
|--------|------|--------------|
| kuaidi100-key | 快递100 授权 Key（签名用） | KUAIDI100_KEY |
| kuaidi100-customer | 快递100 公司编号 Customer | KUAIDI100_CUSTOMER |

- **获取方式**：在 [快递100 开放平台](https://api.kuaidi100.com) 注册并获取 Key、Customer。
- **相关接口**：移动端 `GET /weixin/api/ma/orderinfo/logistics/{订单id}`，返回 `{ "logistics": 订单物流基本信息, "track": 实时轨迹(含 data 轨迹列表) }`。
- **说明**：同一单号查询频率建议间隔 30 分钟以上，避免锁单。

---

## 通用说明

### 请求与认证

- **管理端**：除登录、注册、验证码、短信、首页、部分文件接口外，需在请求头携带 `Authorization: Bearer {token}`。
- **移动端（小程序/Android/iOS）**：小程序请求带 `app-id`；用户登录后各端带统一登录态（如小程序 `third-session`、Android/iOS Token）。
- **商家端**：请求带 `Authorization: Bearer {merchantToken}`。

**小程序登录态：必须放在请求头（Header）**  
购物车、订单、用户信息等需登录的接口，要在 **请求头** 里加 `third-session`，不能放在 URL 参数或 Body 里。  
用 Postman / Apifox / Knife4j 测接口时：在「请求头」里新增一行，名称为 `third-session` 或 `openid`，值为登录接口返回的 `data.thirdSession`（即微信官方 openid）。例如先调 `POST /weixin/api/ma/wxuser/login` 拿到 `thirdSession`（= openid），再在后续请求的 Headers 里加上 `third-session: 该值` 或 `openid: 该值`，否则会返回 60002「session不能为空」或 60001「登录超时」。

### 响应约定

- 业务成功建议统一返回 **code: 200**，失败时前端会提示 `res.data.msg`。
- **code: 60001** 时，前端会清 session 并重新拉取当前页（如登录态失效）。

### 权限

- 管理端表中“权限”列对应 `@PreAuthorize` 中的权限标识；未标注的接口可能仅需登录或为公开。
- 分页接口一般支持 `pageNum`/`pageSize` 或 `current`/`size`，以实际实现为准。

### 前端静态资源（仅小程序包内，不走后端；Android/iOS 使用原生或本地资源）

| 路径 | 用途 |
|------|------|
| `static/tabbar/home.png` / `home-active.png` | 首页 Tab 未选/选中 |
| `static/tabbar/category.png` / `category-active.png` | 分类 Tab |
| `static/tabbar/user.png` / `user-active.png` | 我的 Tab |
| `static/img/no_pic.png` | 商品/订单占位图 |
| `static/img/5-1.png` ~ `5-4.png`、`7-1.png` 等 | 个人中心宫格图标 |
| `static/img/shopping-cart.jpg` | 购物车空状态图 |

图标建议：TabBar 81×81 px，单张 ≤ 40KB。

---

*文档合并自管理端 Controller 与移动端需求文档，若有 context-path 或 basePath 变更请同步更新。*
