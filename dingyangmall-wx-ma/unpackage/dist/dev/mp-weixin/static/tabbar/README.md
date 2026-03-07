# TabBar 图标说明

本目录存放**小程序底部导航栏图标**，图标随小程序包上传，不请求后端。可使用**微信/腾讯系**或 **Element UI / Element Plus** 风格图标。

## 所需文件

| 文件名 | 用途 | 建议尺寸 |
|--------|------|----------|
| `home.png` | 首页-未选中 | 81×81 px |
| `home-active.png` | 首页-选中 | 81×81 px |
| `category.png` | 分类-未选中 | 81×81 px |
| `category-active.png` | 分类-选中 | 81×81 px |
| `user.png` | 我的-未选中 | 81×81 px |
| `user-active.png` | 我的-选中 | 81×81 px |
| `cart.png` | 购物车-未选中 | 81×81 px |
| `cart-active.png` | 购物车-选中 | 81×81 px |

微信建议：单张 ≤ 40KB，格式 png。放置完成后无需改代码，`pages.json` 已配置上述路径。

---

## 从官方/推荐渠道下载图标

### 1. 微信开放文档 - 设计资源

- 设计指南与规范：<https://developers.weixin.qq.com/miniprogram/design/>
- 其他开发资源（含 TDesign 等）：<https://developers.weixin.qq.com/miniprogram/dev/devtools/other-resource.html>  
  页面中可找到与微信小程序风格一致的设计资源入口。

### 2. TDesign 设计资源（腾讯官方，与微信同系）

- 设计资源下载：<https://tdesign.tencent.com/source>  
  在「图标」或「组件」中查找底部导航/ tabBar 相关资源，导出为 81×81 的 PNG，按上表重命名后放入本目录。

### 3. 微信原生 icon 文档（仅作参考，tabBar 需用图片路径）

- icon 组件说明：<https://developers.weixin.qq.com/miniprogram/dev/component/icon.html>  
  原生 `<icon>` 仅用于页面内，**tabBar 的 iconPath 必须为本地图片路径**，故需使用上面 1 或 2 的 PNG 资源。

### 4. 阿里 Iconfont（可选）

- <https://www.iconfont.cn/>  
  搜索「首页」「分类」「我的」等，选择与小程序风格接近的图标，下载 PNG（建议 81×81），按上表重命名后放入本目录。

---

## 一键下载（使用官方/开源图标）

### 方式 A：腾讯 WeUI 图标

```bash
node scripts/download-tabbar-icons.js
```

### 方式 B：Element UI / Element Plus 风格图标（推荐）

从 [Element Plus 图标集](https://element-plus.org/zh-CN/component/icon.html)（Iconify `ep` 集）拉取首页、分类、购物车、我的对应图标，风格与 Element UI 一致：

```bash
node scripts/download-tabbar-icons-element.js
```

- 会从 `api.iconify.design/ep/` 下载 SVG 并保存到本目录。
- 若已安装 `sharp`（`npm install sharp --save-dev`），会自动转为 81×81 PNG；否则仅生成 SVG，可用 [CloudConvert](https://cloudconvert.com/svg-to-png) 等转为 PNG 后按上表重命名。
- 图标对应：首页 `house` / `home-filled`，分类 `grid`，购物车 `shopping-cart` / `shopping-cart-full`，我的 `user` / `user-filled`。

若脚本中的源不可用，请按上面 1、2、4 任一渠道手动下载并放入本目录。
