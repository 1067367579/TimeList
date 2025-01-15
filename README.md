# 时刻清单 - Android待办事项应用

一个功能完整的Android待办事项应用，具有现代化的Material Design界面设计。

## 功能特点

- 创建、编辑、删除待办事项
- 支持设置优先级(低、中、高)和标签分类
- 截止日期提醒功能
- 支持按标题搜索
- 已完成/未完成事项分类展示
- 通知提醒功能
- 深色/浅色主题切换

## 技术特性

- 使用Java开发
- 遵循MVVM架构设计模式
- 使用Android Jetpack组件
  - ViewModel
  - LiveData
  - Room数据库
  - ViewBinding
  - WorkManager
  - Preference
- Material Design 3设计规范
- ViewPager2 + TabLayout实现标签页
- RecyclerView实现列表展示
- 适配Android 13通知权限

## 系统要求

- 最低支持Android版本：Android 8.0 (API 26)
- 目标Android版本：Android 13 (API 33)
- 编译SDK版本：33

## 开发环境

- Android Studio Ladybug
- Gradle 8.2
- JDK 17

## 项目结构
app/<br>
├── src/main/<br>
│ ├── java/com/example/todoapp/<br>
│ │ ├── data/ // 数据库相关类<br>
│ │ ├── fragment/ // 片段类<br>
│ │ ├── model/ // 数据模型类<br>
│ │ ├── util/ // 工具类<br>
│ │ ├── worker/ // WorkManager工作器类<br>
│ │ ├── MainActivity.java<br>
│ │ ├── AddTodoActivity.java<br>
│ │ └── TodoDetailActivity.java<br>
│ └── res/ // 资源文件<br>
└── build.gradle // 应用级构建配置<br>


## 主要功能说明

### 1. 待办事项管理
- 支持添加、编辑、删除待办事项
- 可设置标题、描述、标签、优先级
- 支持设置截止日期和提醒

### 2. 分类展示
- 支持按标签分类（工作、学习、生活、娱乐、其他）
- 已完成/未完成分类展示
- 支持按标题搜索

### 3. 提醒功能
- 支持设置截止日期提醒
- 适配Android 13通知权限
- 支持通知栏提醒

### 4. 主题设置
- 支持浅色/深色主题
- 可跟随系统主题自动切换

## 使用说明

1. 添加待办事项
  - 点击右下角的悬浮按钮
  - 填写待办事项信息(标题、描述、标签、优先级)
  - 可选择设置截止日期和提醒

2. 编辑待办事项
  - 点击列表项进入详情页
  - 修改相关信息
  - 点击保存按钮更新

3. 删除待办事项
  - 在详情页点击删除按钮

4. 搜索待办事项
  - 使用顶部搜索栏按标题搜索

## 开发者

- 作者：[Lu Jingxiang]
- 邮箱：[1067367579@qq.com]
- GitHub：[1067367579]

## 许可证

MIT License

Copyright (c) 2024 Lu Jingxiang

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