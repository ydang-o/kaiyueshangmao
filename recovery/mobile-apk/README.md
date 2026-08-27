# 移动端 APK 逆向恢复记录

恢复时间：2026-08-26

## APK

- 原文件：`D:\work\kaiyueshangmao\__UNI__FABA405__20260704201833.apk.1.1`
- SHA-256：`FC94F9D14C0B2ED5DD89D4BF006B9CFD992E35B7F2B8E39819B24DCEC13614B3`
- 大小：`15230933` 字节
- 类型：uni-app Vue 2 / App Plus（不是纯原生页面）
- 应用标识：`__UNI__FABA405`
- 包名线索：`uni.app.UNIFABA405`

## 目录

- `apk-extracted/`：APK 完整解压内容，包含 `assets/apps/__UNI__FABA405/www/app-service.js`、`app-view.js`、静态图标、运行时文件。
- `reconstructed-source/`：从现有 uni-app sourcemap 的 `sourcesContent` 恢复的可读源代码（35 个源文件）。
- `modules-all/`：从 APK `app-service.js` 拆出的 webpack 模块，便于审计 APK 中额外页面。
- `source-backup/`：本次改动前的移动端页面、API、tabbar 资源备份。
- `apk-report.json`：APK 哈希、manifest 和路由清单。

## 已合并到移动端源码

源码目录：`D:\work\kaiyueshangmao\dingyangmall-wx-ma`

1. 增加 APK 路由清单，共 27 个页面；将 tabBar 调整为 APK 使用的原生 tabbar。
2. 恢复积分商城：`pages/integral/shop/index.vue`、`pages/integral/shop/detail/index.vue`。
3. 按 APK 恢复/重建：
   - `pages/auth/phone-login/index.vue`
   - `pages/integral/flow/index.vue`
   - `pages/lottery/detail/index.vue`
   - `pages/lottery/record/index.vue`
   - `pages/merchant/change-password/index.vue`
4. 在 `utils/api.js` 补齐 APK 使用的接口：短信登录/注册、积分商城、积分流水、抽奖列表、商家修改密码。
5. 将 App Plus appid 更新为 APK 的 `__UNI__FABA405`，并恢复 APK 内 tabbar PNG 图标。

## 还原边界

APK 没有携带 `.vue` 源文件。可读的公共页面来自仓库中已有 sourcemap 的 `sourcesContent`；APK 独有页面则依据其编译后的 template/script 进行恢复，保持接口、字段和路由一致，但格式化和注释不可能与原始源码逐字相同。

编译前请使用 HBuilderX/uni-app CLI 重新构建；本目录保留原始 APK 和解压文件，未覆盖原始 APK。
