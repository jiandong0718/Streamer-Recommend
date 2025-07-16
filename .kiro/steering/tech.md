# 技术栈与构建指南

## 核心技术栈

### 后端框架
- **Spring Boot 2.7.0** - 主应用框架
- **Spring Cloud 2021.0.3** - 微服务框架
- **Spring Cloud Alibaba 2021.0.1.0** - 阿里云微服务组件
- **Java 8** - 编程语言版本

### 数据存储
- **MySQL 8.0** - 主数据库，使用Druid连接池
- **Redis 6.0** - 缓存数据库，支持集群
- **Elasticsearch 7.x** - 搜索引擎
- **HBase 2.4+** - 大数据存储（宽表）

### 数据访问层
- **MyBatis Plus 3.5.2** - ORM框架，支持代码生成
- **Spring Data JPA** - JPA实现
- **Spring Data Redis** - Redis操作

### 服务治理
- **Nacos** - 服务注册发现与配置中心
- **OpenFeign** - 服务间调用
- **Spring Cloud LoadBalancer** - 负载均衡

### 开发工具库
- **Lombok** - 代码简化注解
- **Hutool 5.8.5** - Java工具库
- **FastJSON 2.0.12** - JSON处理
- **Apache Commons** - 通用工具库

### 缓存与消息
- **JetCache 2.6.5** - 多级缓存框架
- **RocketMQ** - 消息队列

### 监控与文档
- **Spring Boot Actuator** - 应用监控
- **Micrometer + Prometheus** - 指标收集
- **SpringDoc OpenAPI** - API文档生成

## 构建系统

### Maven配置
- **Maven 3.6+** 作为构建工具
- 使用Spring Boot Maven插件进行打包
- 支持多环境配置（dev/prod）

### 常用命令

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包应用
mvn clean package -Dmaven.test.skip=true

# 运行应用（开发环境）
mvn spring-boot:run -Dspring.profiles.active=dev

# 运行应用（生产环境）
java -jar target/recommend-server-1.0.0.jar --spring.profiles.active=prod
```

## 环境要求

### 开发环境
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+
- IDE推荐：IntelliJ IDEA

### 可选组件
- HBase 2.4+ (大数据存储)
- Elasticsearch 7.17+ (搜索功能)
- Nacos 2.0+ (服务治理)

## 配置管理

### 多环境配置
- `application.yml` - 主配置文件
- `application-dev.yml` - 开发环境
- `application-prod.yml` - 生产环境
- `bootstrap.yml` - 启动配置

### 关键配置项
- 数据库连接池配置
- Redis缓存配置
- 推荐算法参数配置
- 监控指标配置

## 代码质量

### 依赖管理
- 使用Spring Boot BOM管理版本
- 明确指定第三方库版本
- 定期更新安全补丁

### 性能优化
- 连接池优化（数据库、Redis）
- 多级缓存策略
- 异步处理（@Async）
- 定时任务调度