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

## 通用说明

### 请求与认证

- **管理端**：除登录、注册、验证码、短信、首页、部分文件接口外，需在请求头携带 `Authorization: Bearer {token}`。
- **移动端（小程序/Android/iOS）**：小程序请求带 `app-id`；用户登录后各端带统一登录态（如小程序 `third-session`、Android/iOS Token）。
- **商家端**：请求带 `Authorization: Bearer {merchantToken}`。

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
