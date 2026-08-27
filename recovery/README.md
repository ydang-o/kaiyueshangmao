# 最新版本逆向恢复报告

## 输入文件

- 后端：D:\work\kaiyueshangmao\dingyangmall-admin.jar
- 前端：D:\work\kaiyueshangmao\dist_phcth.tar.gz
- 线上参考：<https://kaiyueshangmao.xyz/>

## 后端恢复结果

dingyangmall-admin.jar 是 Spring Boot 可执行 JAR，包含以下模块的嵌套依赖包：

- dingyangmall-admin
- dingyangmall-common
- dingyangmall-framework
- dingyangmall-system
- dingyangmall-mall
- dingyangmall-weixin

已使用 CFR 0.152 反编译，源码位于：

D:\work\kaiyueshangmao\recovery\backend-recovered

其中 dingyangmall-weixin 是当前 Git 源码中缺失、但最新 JAR 中实际存在的完整模块。

## 前端恢复结果

已解压前端发布包到：

D:\work\kaiyueshangmao\recovery\frontend-recovered\dist

该发布包入口引用：

- assets/index-5HGVcfSt.js
- assets/index-BhxShI1f.css
- assets/login-PByZC2jS.js
- assets/login-DUe12y2c.css

线上页面加载的入口资源与压缩包中的资源名称一致，因此该压缩包可作为线上前端的发布版本备份。

为方便后续阅读，已将入口及其引用的 JavaScript 包格式化到：

D:\work\kaiyueshangmao\recovery\frontend-recovered\bundles-readable

当前仓库已有的 Vue 源码基线位于：

D:\work\kaiyueshangmao\recovery\frontend-recovered\source-baseline

## 重要限制

Java 字节码反编译可以恢复业务逻辑，但无法恢复原始注释、变量名细节、泛型信息和 Lombok 源码形式；前端发布包只能恢复构建后的 JavaScript/CSS，不能完整恢复原始 Vue 单文件组件、变量名和构建前目录结构。

恢复后的源码已与当前工作区分离保存，避免覆盖原有本地部署修改。

