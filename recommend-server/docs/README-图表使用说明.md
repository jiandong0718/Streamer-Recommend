# 架构图表使用说明

## 📊 架构图表文件位置

**HTML文件**：`recommend-server/docs/架构图表.html`
**图片存储目录**：`recommend-server/docs/images/`

## 🎯 如何生成图片

### 方法一：浏览器截图（推荐）

1. **打开HTML文件**
   ```bash
   # 在项目根目录下执行
   open recommend-server/docs/架构图表.html
   
   # 或者直接用浏览器打开文件
   ```

2. **等待图表加载**
   - 页面加载后，等待2-3秒让Mermaid图表完全渲染
   - 确保所有图表都正确显示

3. **保存图片**
   - 右键点击任意图表
   - 选择"图片另存为" 
   - 保存到 `recommend-server/docs/images/` 目录

4. **推荐文件名**
   - `system-architecture.png` - 系统总体架构图
   - `interaction-flow.png` - 系统交互流程时序图  
   - `data-flow.png` - 数据流图
   - `deployment-architecture.png` - 部署架构图

### 方法二：命令行截图（适合自动化）

如果您需要自动化生成图片，可以使用以下工具：

#### 使用 Puppeteer (Node.js)

```bash
# 安装依赖
npm install puppeteer

# 创建截图脚本
node generate-diagrams.js
```

#### 使用 mermaid-cli

```bash
# 安装mermaid-cli
npm install -g @mermaid-js/mermaid-cli

# 生成图片（需要先提取mermaid代码到单独文件）
mmdc -i architecture.mmd -o images/system-architecture.png
mmdc -i interaction.mmd -o images/interaction-flow.png
mmdc -i dataflow.mmd -o images/data-flow.png
mmdc -i deployment.mmd -o images/deployment-architecture.png
```

## 📁 目录结构

```
recommend-server/
├── docs/
│   ├── images/                    # 图片存储目录
│   │   ├── system-architecture.png
│   │   ├── interaction-flow.png
│   │   ├── data-flow.png
│   │   └── deployment-architecture.png
│   ├── 架构图表.html              # 图表HTML文件
│   ├── README-图表使用说明.md      # 本说明文件
│   └── ...
├── 系统架构与交互文档.md
├── API接口文档.md
├── 部署运维文档.md
└── ...
```

## 🎨 图表说明

### 1. 系统总体架构图 (system-architecture.png)
- **用途**：展示整个系统的分层架构
- **包含**：前端层、网关层、应用层、业务层、数据层、监控层
- **适用场景**：技术方案介绍、架构评审

### 2. 系统交互流程时序图 (interaction-flow.png)
- **用途**：展示推荐请求的完整处理流程
- **包含**：各组件间的时序交互、缓存策略、并行处理
- **适用场景**：流程说明、问题排查

### 3. 数据流图 (data-flow.png)
- **用途**：展示数据在系统中的流转过程
- **包含**：数据输入、特征工程、算法处理、结果输出
- **适用场景**：算法介绍、数据链路分析

### 4. 部署架构图 (deployment-architecture.png)
- **用途**：展示生产环境的部署架构
- **包含**：负载均衡、集群部署、监控体系
- **适用场景**：运维部署、容量规划

## 🔧 图片质量优化

### 浏览器设置
1. **分辨率**：建议使用1920x1080或更高分辨率
2. **缩放**：设置为100%缩放比例
3. **浏览器**：推荐使用Chrome或Firefox

### 截图技巧
1. **全图截取**：使用浏览器的"整页截图"功能
2. **单图截取**：只截取单个图表部分
3. **背景**：确保图表有足够的白色背景

### 图片格式
- **PNG**：推荐格式，支持透明背景，质量高
- **SVG**：矢量格式，可无限缩放（如果支持）
- **JPG**：文件较小，但不支持透明背景

## 📝 文档更新

当架构发生变化时，请按以下步骤更新：

1. **修改HTML文件**：更新对应的Mermaid图表代码
2. **重新生成图片**：按照上述方法生成新的图片
3. **更新文档**：在相关文档中引用新的图片

## 🆘 常见问题

### Q: 图表显示不正常？
A: 检查网络连接，确保能访问CDN资源，或者等待页面完全加载

### Q: 右键无法保存图片？
A: 确保浏览器允许右键菜单，或者使用截图工具

### Q: 图片太小/太大？
A: 调整浏览器缩放比例，或者修改HTML中的CSS样式

### Q: 需要修改图表样式？
A: 修改HTML文件中的Mermaid配置或CSS样式

---

**最后更新时间**：2024年12月  
**维护团队**：架构设计团队 