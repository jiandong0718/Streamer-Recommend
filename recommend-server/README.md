# 主播推荐系统 (Recommend Server)

一个基于Spring Boot的智能主播推荐系统，提供个性化的主播推荐服务。

## 项目概述

本项目是一个企业级的推荐系统，专门为直播社交场景设计。系统采用现代化的微服务架构，支持多种推荐算法，具备完善的缓存机制和监控能力。

### 核心特性

- 🎯 **智能推荐**: 支持协同过滤、内容推荐、混合推荐等多种算法
- 🚀 **高性能**: 多级缓存、异步处理、连接池优化
- 🌍 **国际化**: 支持多语言和地区配置
- 📊 **监控完善**: 集成指标监控、健康检查、性能分析
- 🔧 **易扩展**: 微服务架构，支持水平扩展
- 💾 **多数据源**: 支持MySQL、Redis、Elasticsearch、HBase

## 技术栈

### 核心框架
- **Spring Boot 2.7.0** - 主框架
- **Spring Cloud 2021.0.3** - 微服务框架
- **Spring Cloud Alibaba** - 阿里云组件

### 数据存储
- **MySQL 8.0** - 主数据库
- **Redis 6.0** - 缓存数据库
- **Elasticsearch 7.x** - 搜索引擎
- **HBase** - 大数据存储

### 数据访问
- **MyBatis Plus 3.5.2** - ORM框架
- **Druid** - 数据库连接池
- **Spring Data JPA** - JPA实现

### 服务治理
- **Nacos** - 服务注册与配置中心
- **OpenFeign** - 服务调用
- **Hystrix** - 断路器

### 开发工具
- **Java 17** - 编程语言
- **Maven** - 项目管理
- **Lombok** - 代码简化
- **Hutool** - 工具库
- **FastJSON** - JSON处理

### 监控运维
- **Spring Boot Actuator** - 应用监控
- **Prometheus** - 指标收集
- **Druid** - SQL监控

## 项目结构

```
recommend-server/
├── src/main/java/com/recommend/
│   ├── algorithm/                    # 推荐算法
│   │   ├── impl/                    # 算法实现
│   │   └── RecommendAlgorithm.java
│   ├── common/                      # 公共组件
│   │   ├── config/                  # 配置类
│   │   ├── dto/                     # 数据传输对象
│   │   ├── entity/                  # 实体类
│   │   ├── enums/                   # 枚举类
│   │   ├── exception/               # 异常处理
│   │   ├── request/                 # 请求类
│   │   └── utils/                   # 工具类
│   ├── config/                      # 系统配置
│   │   ├── CacheConfig.java         # 缓存配置
│   │   ├── RedisConfig.java         # Redis配置
│   │   └── SwaggerConfig.java       # API文档配置
│   ├── controller/                  # 控制器层
│   │   ├── StreamerSearchController.java  # 主播搜索控制器
│   │   ├── RecommendController.java     # 推荐控制器
│   │   └── ...                      # 其他控制器
│   ├── service/                     # 服务层
│   │   ├── impl/                    # 服务实现
│   │   ├── mapper/                  # MyBatis映射
│   │   ├── cache/                   # 缓存服务
│   │   ├── feature/                 # 特征工程
│   │   ├── filter/                  # 过滤服务
│   │   ├── rank/                    # 排序服务
│   │   ├── recall/                  # 召回服务
│   │   └── ...                      # 其他服务
│   ├── monitor/                     # 监控组件
│   └── RecommendApplication.java    # 启动类
├── src/main/resources/
│   ├── mapper/                      # MyBatis映射文件
│   ├── db/                          # 数据库脚本
│   ├── application.yml              # 主配置文件
│   ├── application-dev.yml          # 开发环境配置
│   ├── application-prod.yml         # 生产环境配置
│   └── bootstrap.yml                # 启动配置
├── pom.xml                          # Maven配置
└── README.md                        # 项目文档
```

## 快速开始

### 环境要求

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.0+（可选）

### 1. 克隆项目

```bash
git clone <repository-url>
cd recommend-server
```

### 2. 配置数据库

创建MySQL数据库：
```sql
CREATE DATABASE recommend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

导入数据库脚本：
```bash
mysql -u root -p recommend < src/main/resources/db/schema.sql
```

### 3. 配置Redis

启动Redis服务：
```bash
redis-server
```

### 4. 修改配置

编辑 `src/main/resources/application-dev.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recommend?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_redis_password  # 如果有密码
```

### 5. 编译运行

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run

# 或者打包运行
mvn clean package
java -jar target/recommend-server-1.0.0.jar
```

### 6. 验证服务

访问以下地址验证服务是否启动成功：

- 健康检查: http://localhost:8080/actuator/health
- API文档: http://localhost:8080/swagger-ui.html

## API文档

### 主播搜索相关接口

#### 1. 搜索主播

```
POST /api/streamer/search/live
```

**请求参数:**
- `userId` (Long): 用户ID
- `keyword` (String): 搜索关键词 (可选)
- `category` (String): 直播分类 (可选)
- `region` (String): 地区 (可选)
- `minLevel` (Integer): 最小等级 (可选)
- `maxLevel` (Integer): 最大等级 (可选)
- `page` (Integer): 页码，默认1
- `limit` (Integer): 每页数量，默认20

**响应示例:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "streamers": [...],
    "page": 1,
    "limit": 20,
    "total": 100
  }
}
```

#### 2. 技能匹配搜索

```
POST /api/streamer/search/skill-match
```

#### 3. 新用户推荐

```
POST /api/streamer/recommend/newbie
```

#### 4. 热门推荐

```
POST /api/streamer/recommend/hot
```

#### 5. 个性化推荐

```
POST /api/streamer/recommend/personal
```

### 推荐系统配置

```yaml
recommend:
  system:
    version: 1.0.0
    enable-cache: true          # 启用缓存
    enable-monitor: true        # 启用监控
    default-recommend-count: 10 # 默认推荐数量
    max-recommend-count: 100    # 最大推荐数量
    cache-expire-time: 300      # 缓存过期时间（秒）
    default-algorithm: hybrid   # 默认算法
  
  algorithm:
    collaborative-filtering:
      similarity-threshold: 0.1
      neighbor-count: 50
    content-based:
      feature-weight: 0.6
      tag-weight: 0.4
    hybrid:
      cf-weight: 0.4
      cb-weight: 0.6
```

## 推荐算法

### 1. 协同过滤 (Collaborative Filtering)
- 基于用户行为相似性
- 支持用户-用户和物品-物品协同过滤
- 适用于有足够历史数据的场景

### 2. 内容推荐 (Content-Based)
- 基于物品特征相似性
- 利用游戏陪玩师的标签、技能等特征
- 适用于冷启动场景

### 3. 混合推荐 (Hybrid)
- 结合多种推荐算法
- 动态调整权重
- 提供更准确的推荐结果

### 4. 特征工程
- 用户画像构建
- 物品特征提取
- 实时特征计算
- 相似度计算

## 缓存策略

### 多级缓存架构
1. **本地缓存**: JVM内存缓存，响应最快
2. **Redis缓存**: 分布式缓存，支持集群
3. **数据库**: 持久化存储

### 缓存配置
- 用户信息缓存: 1小时
- 游戏陪玩师信息: 30分钟
- 推荐结果: 5分钟
- 热门推荐: 10分钟

## 监控指标

### 系统指标
- JVM内存使用率
- CPU使用率
- 垃圾回收情况
- 线程池状态

### 业务指标
- 推荐请求QPS
- 推荐响应时间
- 缓存命中率
- 算法准确率

### 数据库指标
- 连接池使用情况
- SQL执行时间
- 慢查询统计

## 部署指南

### Docker部署

1. 构建镜像：
```bash
docker build -t recommend-server:1.0.0 .
```

2. 运行容器：
```bash
docker run -d \
  --name recommend-server \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://your-mysql:3306/recommend \
  -e DB_USERNAME=your_username \
  -e DB_PASSWORD=your_password \
  -e REDIS_HOST=your-redis \
  recommend-server:1.0.0
```

### Kubernetes部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: recommend-server
spec:
  replicas: 3
  selector:
    matchLabels:
      app: recommend-server
  template:
    metadata:
      labels:
        app: recommend-server
    spec:
      containers:
      - name: recommend-server
        image: recommend-server:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
```

## 开发指南

### 代码规范
- 使用Java 17语言特性
- 遵循Spring Boot最佳实践
- 使用Lombok简化代码
- 编写单元测试

### 扩展推荐算法
1. 实现 `RecommendAlgorithm` 接口
2. 添加算法配置
3. 注册为Spring Bean
4. 配置权重参数

### 添加新的推荐场景
1. 扩展 `RecommendRequest` 添加场景参数
2. 在服务层添加场景处理逻辑
3. 配置缓存策略
4. 添加监控指标

## 性能优化

### 数据库优化
- 合理设计索引
- 使用连接池
- 开启查询缓存
- 分库分表（如需要）

### 缓存优化
- 合理设置过期时间
- 使用缓存预热
- 避免缓存雪崩
- 实现缓存降级

### 算法优化
- 离线计算用户画像
- 预计算相似度矩阵
- 使用批量处理
- 实现增量更新

## 故障排查

### 常见问题

**1. 服务启动失败**
- 检查Java版本是否为17+
- 检查数据库连接配置
- 检查Redis连接配置
- 查看启动日志

**2. 推荐结果为空**
- 检查数据库中是否有数据
- 检查算法配置参数
- 查看业务日志
- 检查缓存状态

**3. 性能问题**
- 检查数据库慢查询
- 查看缓存命中率
- 监控JVM内存使用
- 分析线程池状态

### 日志配置

开发环境开启详细日志：
```yaml
logging:
  level:
    com.recommend: DEBUG
    org.springframework.cache: DEBUG
```

生产环境建议使用INFO级别：
```yaml
logging:
  level:
    root: WARN
    com.recommend: INFO
```

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目维护者: liujiandong
- Email: jiandong.yh@gmail.com
- 项目地址: https://github.com/jiandong0718/recommend-server

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 实现基础推荐功能
- 支持多种推荐算法
- 完善监控和缓存机制 