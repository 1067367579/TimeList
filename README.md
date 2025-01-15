# TodoApp - Android待办事项应用

一个功能完整的Android待办事项应用，具有现代化的Material Design界面设计。

## 功能特点

- 创建、编辑、删除待办事项
- 支持设置优先级和标签分类
- 截止日期提醒功能
- 支持搜索和过滤
- 支持按优先级、截止日期等多种方式排序
- 已完成/未完成事项分类展示
- 通知提醒功能
- 右滑删除功能
- 支持撤销删除操作

## 技术特性

- 完全使用Kotlin/Java开发
- 遵循MVVM架构设计模式
- 使用Android Jetpack组件
  - ViewModel
  - LiveData
  - Room数据库
  - ViewBinding
  - WorkManager
- Material Design 3设计规范
- ViewPager2 + TabLayout实现标签页
- RecyclerView实现列表展示
- 支持Android 13通知权限适配

## 系统要求

- 最低支持Android版本：Android 8.0 (API 26)
- 目标Android版本：Android 13 (API 33)
- 编译SDK版本：33

## 开发环境

- Android Studio Hedgehog | 2023.1.1
- Gradle 8.0
- JDK 17

## 项目结构 
app/
├── src/main/
│ ├── java/com/example/todoapp/
│ │ ├── activity/ // 活动类
│ │ ├── adapter/ // 适配器类
│ │ ├── dao/ // 数据访问对象
│ │ ├── database/ // 数据库相关类
│ │ ├── fragment/ // 片段类
│ │ ├── model/ // 数据模型类
│ │ ├── util/ // 工具类
│ │ ├── viewmodel/ // ViewModel类
│ │ └── worker/ // WorkManager工作器类
│ └── res/ // 资源文件
└── build.gradle // 应用级构建配置


## 主要功能说明

### 1. 待办事项管理
- 支持添加、编辑、删除待办事项
- 可设置标题、描述、标签、优先级
- 支持设置截止日期和提醒

### 2. 分类与过滤
- 支持按标签分类（工作、学习、生活等）
- 可按优先级排序
- 支持按标题、描述、标签搜索
- 已完成/未完成分类展示

### 3. 提醒功能
- 支持设置截止日期提醒
- 适配Android 13通知权限
- 支持通知栏、状态栏提醒
- 可在锁屏界面显示提醒

### 4. 用户体验
- Material Design界面设计
- 流畅的动画效果
- 支持右滑删除
- 删除操作可撤销
- 空状态界面提示

## 使用说明

1. 添加待办事项
  - 点击右下角的悬浮按钮
  - 填写待办事项信息
  - 可选择设置提醒

2. 编辑待办事项
  - 点击列表项进入详情页
  - 修改相关信息
  - 保存更改

3. 删除待办事项
  - 右滑列表项
  - 或在详情页点击删除按钮
  - 可通过Snackbar撤销删除

4. 搜索和过滤
  - 使用顶部搜索栏搜索
  - 通过菜单选择排序方式
  - 使用标签过滤器筛选

## 开发者

- 作者：[Lu Jingxiang]
- 邮箱：[1067367579@qq.com]
- GitHub：[1067367579]

## 许可证
MIT License

Copyright (c) 2025 Lu Jingxiang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
