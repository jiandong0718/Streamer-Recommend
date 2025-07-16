# 项目结构与架构规范

## 整体项目结构

```
recommend/
├── README.md                    # 项目说明文档
└── recommend-server/           # 推荐系统服务端
    ├── pom.xml                 # Maven依赖配置
    ├── docs/                   # 项目文档
    └── src/main/
        ├── java/com/recommend/
        └── resources/
```

## Java包结构规范

### 核心包组织
```
com.recommend/
├── RecommendApplication.java    # 主启动类
├── algorithm/                   # 推荐算法层
│   ├── impl/                   # 算法具体实现
│   ├── RecommendAlgorithm.java # 算法接口
│   └── RecommendMetrics.java   # 算法指标
├── common/                     # 通用组件
│   ├── config/                 # 通用配置
│   ├── dto/                    # 数据传输对象
│   ├── entity/                 # 实体类
│   ├── enums/                  # 枚举类
│   ├── exception/              # 异常处理
│   ├── request/                # 请求对象
│   ├── response/               # 响应对象
│   └── utils/                  # 工具类
├── config/                     # 系统配置类
├── controller/                 # 控制器层
├── service/                    # 服务层
│   ├── impl/                   # 服务实现
│   ├── mapper/                 # MyBatis映射接口
│   ├── cache/                  # 缓存服务
│   ├── feature/                # 特征工程
│   ├── filter/                 # 过滤服务
│   ├── rank/                   # 排序服务
│   └── recall/                 # 召回服务
└── monitor/                    # 监控组件
```

## 架构分层规范

### 1. 控制器层 (Controller)
- 处理HTTP请求和响应
- 参数验证和转换
- 异常处理
- 使用`@RestController`和`@RequestMapping`

### 2. 服务层 (Service)
- 业务逻辑处理
- 事务管理
- 缓存操作
- 使用`@Service`注解

### 3. 数据访问层 (Mapper)
- 数据库操作
- MyBatis映射
- 使用`@Mapper`注解

### 4. 实体层 (Entity)
- 数据模型定义
- 使用Lombok简化代码
- MyBatis Plus注解

## 推荐系统特有架构

### 推荐流程分层
```
召回层 (Recall) -> 排序层 (Rank) -> 过滤层 (Filter)
```

1. **召回层**: 从海量数据中快速筛选候选集
2. **排序层**: 对候选集进行精确排序
3. **过滤层**: 业务规则过滤和重排序

### 算法模块组织
- `algorithm/` - 算法接口和基础类
- `algorithm/impl/` - 具体算法实现
  - `CollaborativeFilteringAlgorithm.java` - 协同过滤
  - `ContentBasedAlgorithm.java` - 内容推荐
  - `HybridAlgorithm.java` - 混合推荐

## 配置文件结构

### Resources目录
```
resources/
├── application.yml              # 主配置
├── application-dev.yml          # 开发环境
├── application-prod.yml         # 生产环境
├── bootstrap.yml                # 启动配置
├── db/
│   └── schema.sql              # 数据库脚本
└── mapper/
    └── *.xml                   # MyBatis映射文件
```

## 命名规范

### 类命名
- Controller: `XxxController`
- Service: `XxxService` / `XxxServiceImpl`
- Mapper: `XxxMapper`
- Entity: 直接使用业务名称
- DTO: `XxxRequest` / `XxxResponse`

### 方法命名
- 查询: `get` / `find` / `query`
- 新增: `add` / `create` / `save`
- 修改: `update` / `modify`
- 删除: `delete` / `remove`
- 推荐: `recommend` / `suggest`

### 包命名
- 全小写
- 使用点分隔
- 体现业务含义

## 文档组织

### docs目录结构
```
docs/
├── README.md                   # 项目说明
├── API接口文档.md              # API文档
├── 架构设计文档.md             # 架构文档
├── 技术深度实现文档.md         # 技术实现
├── 部署运维文档.md             # 部署指南
└── images/                     # 图片资源
```

## 代码组织原则

1. **单一职责**: 每个类只负责一个功能
2. **依赖注入**: 使用Spring的依赖注入
3. **接口隔离**: 定义清晰的接口边界
4. **配置外部化**: 配置信息放在配置文件中
5. **异常统一处理**: 使用全局异常处理器
6. **日志规范**: 使用SLF4J + Logback
7. **缓存分层**: 本地缓存 + Redis缓存