# 推荐系统 (Recommendation System)

基于Spring Boot的企业级推荐系统，提供用户画像、陪玩师画像、标签系统、推荐算法等功能。

## 项目结构

```
recommend/
├── README.md                    # 项目说明文档
└── recommend-server/           # 推荐系统服务端
    ├── pom.xml                 # Maven依赖配置
    ├── README.md               # 服务端说明文档
    └── src/
        ├── main/
        │   ├── java/com/recommend/
        │   │   ├── RecommendApplication.java    # 主启动类
        │   │   ├── algorithm/                   # 推荐算法
        │   │   │   ├── impl/                   # 算法实现
        │   │   │   ├── RecommendAlgorithm.java # 算法接口
        │   │   │   └── RecommendMetrics.java   # 算法指标
        │   │   ├── common/                     # 通用组件
        │   │   │   ├── config/                 # 配置类
        │   │   │   ├── dto/                    # 数据传输对象
        │   │   │   ├── entity/                 # 实体类
        │   │   │   ├── exception/              # 异常处理
        │   │   │   ├── request/                # 请求对象
        │   │   │   └── utils/                  # 工具类
        │   │   ├── config/                     # 配置类
        │   │   ├── controller/                 # 控制器层
        │   │   ├── dto/                        # 数据传输对象
        │   │   ├── entity/                     # 实体类
        │   │   ├── monitor/                    # 监控组件
        │   │   └── service/                    # 服务层
        │   │       ├── feature/                # 特征工程
        │   │       ├── filter/                 # 过滤服务
        │   │       ├── impl/                   # 服务实现
        │   │       ├── mapper/                 # 数据访问层
        │   │       ├── rank/                   # 排序服务
        │   │       └── recall/                 # 召回服务
        │   └── resources/
        │       ├── application.yml             # 主配置文件
        │       ├── application-dev.yml         # 开发环境配置
        │       ├── application-prod.yml        # 生产环境配置
        │       ├── db/schema.sql              # 数据库schema
        │       └── mapper/                     # MyBatis映射文件
        └── target/                            # 构建输出目录
```

## 技术栈

### 后端技术
- **Spring Boot 2.7.0** - 主框架
- **Spring Cloud 2021.0.3** - 微服务框架
- **Spring Cloud Alibaba 2021.0.1.0** - 阿里云微服务套件
- **MyBatis Plus 3.5.2** - ORM框架
- **MySQL** - 关系型数据库
- **Redis** - 缓存数据库
- **HBase** - 大数据存储
- **Elasticsearch** - 搜索引擎
- **Nacos** - 配置中心和服务发现

### 开发工具
- **Maven** - 项目构建工具
- **Lombok** - 代码生成工具
- **Swagger** - API文档工具
- **FastJSON** - JSON处理工具
- **Hutool** - Java工具库

## 功能特性

### 1. 用户画像系统
- 用户基础信息管理
- 用户行为数据收集
- 用户标签系统
- 用户特征工程
- 用户偏好分析

### 2. 陪玩师画像系统
- 陪玩师基础信息管理
- 陪玩师行为数据收集
- 陪玩师标签系统
- 陪玩师特征工程
- 陪玩师技能评估

### 3. 推荐算法引擎
- **协同过滤算法** - 基于用户行为的推荐
- **内容推荐算法** - 基于内容特征的推荐
- **混合推荐算法** - 多算法融合推荐
- **召回层** - 候选集生成
- **排序层** - 精确排序
- **过滤层** - 结果过滤和重排

### 4. 标签系统
- 标签定义和管理
- 用户标签关联
- 陪玩师标签关联
- 标签权重计算
- 标签相似度计算

### 5. 监控系统
- 系统性能监控
- 推荐质量监控
- 用户行为监控
- 告警系统
- 可视化报表

### 6. 数据存储
- **MySQL** - 基础数据存储
- **Redis** - 实时数据缓存
- **HBase** - 宽表数据存储
- **Elasticsearch** - 搜索数据存储

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+
- HBase 2.4+ (可选)
- Elasticsearch 7.17+ (可选)
- Nacos 2.0+ (可选)

### 1. 克隆项目
```bash
git clone <repository-url>
cd recommend
```

### 2. 配置数据库
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE recommend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构
mysql -u root -p recommend < recommend-server/src/main/resources/db/schema.sql
```

### 3. 修改配置
编辑 `recommend-server/src/main/resources/application-dev.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recommend?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
    database: 0
```

### 4. 启动服务
```bash
cd recommend-server
mvn spring-boot:run
```

### 5. 访问服务
- 应用地址：http://localhost:8080/api
- API文档：http://localhost:8080/api/swagger-ui.html
- 健康检查：http://localhost:8080/api/monitor/health

## API文档

### 推荐接口
- `POST /api/recommend/masters` - 推荐游戏陪玩师
- `POST /api/recommend/games` - 推荐游戏
- `POST /api/recommend/personalized` - 个性化推荐
- `GET /api/recommend/hot` - 热门推荐
- `GET /api/recommend/newcomer` - 新人推荐

### 用户管理
- `GET /api/users/{id}` - 获取用户信息
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户信息
- `DELETE /api/users/{id}` - 删除用户

### 陪玩师管理
- `GET /api/game-masters/{id}` - 获取陪玩师信息
- `POST /api/game-masters` - 创建陪玩师
- `PUT /api/game-masters/{id}` - 更新陪玩师信息
- `DELETE /api/game-masters/{id}` - 删除陪玩师

### 标签管理
- `GET /api/tags/{id}` - 获取标签信息
- `POST /api/tags` - 创建标签
- `PUT /api/tags/{id}` - 更新标签信息
- `DELETE /api/tags/{id}` - 删除标签

### 监控接口
- `GET /api/monitor/health` - 系统健康状态
- `GET /api/monitor/metrics` - 系统指标
- `GET /api/monitor/metrics/chart` - 指标图表数据

## 部署说明

### 开发环境部署
```bash
# 使用开发配置启动
mvn spring-boot:run -Dspring.profiles.active=dev
```

### 生产环境部署
```bash
# 打包
mvn clean package -Dmaven.test.skip=true

# 运行
java -jar target/recommend-server-1.0.0.jar --spring.profiles.active=prod
```

### Docker部署
```bash
# 构建镜像
docker build -t recommend-server .

# 运行容器
docker run -d -p 8080:8080 --name recommend-server recommend-server
```

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/recommend
    username: root
    password: root
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 10000
```

### HBase配置
```yaml
hbase:
  zookeeper:
    quorum: localhost
    port: 2181
  table:
    user-profile: user_profile_wide
    game-master: game_master_wide
```

### Elasticsearch配置
```yaml
elasticsearch:
  host: localhost
  port: 9200
  username: elastic
  password: elastic
```

## 监控指标

### 系统指标
- 请求总数
- 成功率
- 平均响应时间
- 错误率
- 缓存命中率

### 推荐质量指标
- 点击率 (CTR)
- 转化率
- 多样性得分
- 新颖性得分
- 覆盖率

### 业务指标
- 活跃用户数
- 活跃陪玩师数
- 订单数量
- 订单完成率
- 用户满意度

## 开发指南

### 代码规范
- 使用驼峰命名法
- 类名使用帕斯卡命名法
- 方法和变量使用驼峰命名法
- 常量使用全大写加下划线

### 分支管理
- `master` - 主分支，用于生产环境
- `develop` - 开发分支，用于集成测试
- `feature/*` - 功能分支，用于新功能开发
- `hotfix/*` - 热修复分支，用于紧急修复

### 提交规范
- `feat:` - 新功能
- `fix:` - 修复bug
- `docs:` - 文档更新
- `style:` - 代码格式调整
- `refactor:` - 代码重构
- `test:` - 测试相关
- `chore:` - 其他修改

## 常见问题

### Q: 如何添加新的推荐算法？
A: 实现 `RecommendAlgorithm` 接口，并在 `algorithm/impl` 包下创建具体实现类。

### Q: 如何自定义标签？
A: 通过 `/api/tags` 接口创建标签，并通过用户标签关联接口绑定到用户。

### Q: 如何监控系统性能？
A: 访问 `/api/monitor/health` 查看系统健康状态，访问 `/api/monitor/metrics` 查看详细指标。

### Q: 如何扩展存储？
A: 系统支持MySQL、Redis、HBase、Elasticsearch多种存储方式，可根据需要进行扩展。

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 Apache License 2.0 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 联系我们

- 项目主页：https://github.com/jiandong0718/recommend
- 问题反馈：https://github.com/jiandong0718/recommend/issues
- 邮箱：jiandong.yh@gmail.com

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 实现基础推荐功能
- 支持用户画像和陪玩师画像
- 集成监控系统
- 提供完整的API文档
