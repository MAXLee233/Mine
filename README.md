# Mine

## 常用备注测试版

测试分支：`codex/common-notes`。备注弹窗支持按分类选择、编辑常用备注。

测试版包名为 `com.coderpage.mine.commonnotes`，显示名称为“我的记账本（备注测试版）”，使用 Android debug 签名，可与原软件分别安装。
在 GitHub 的 **Actions → Common notes test APK** 中查看构建结果；成功后从该次运行的 **Artifacts → mine-common-notes-test-apk** 下载 APK 压缩包。
构建失败时以该次日志为准；原项目的构建配置和 Android 兼容性仍需检查。

本地构建：复制 `local.properties.example` 为 `local.properties`，填写 Android SDK 路径，在原项目要求的 JDK/SDK 环境执行 `gradlew :app:testOfficialDebugUnitTest :app:assembleOfficialDebug`。
测试版不直接读取原软件的账目，可自行试记或使用原软件导出的备份做导入测试。
详细功能、独立逻辑测试及版本差异见[常用备注功能说明](常用备注功能说明.md)。

自己写的一个记账本应用，简单干净

应用中使用的图标来自 [iconfont](http://iconfont.cn/) 和 [material-design-icons](https://github.com/google/material-design-icons)

[应用宝下载](http://sj.qq.com/myapp/detail.htm?apkName=com.coderpage.mine)
