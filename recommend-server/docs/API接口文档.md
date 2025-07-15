# 主播推荐系统 API 接口文档

## 1. 接口概览

本文档描述了主播推荐系统的所有API接口，包括推荐接口、主播管理接口、用户管理接口等。

### 1.1 基础信息

- **Base URL**: `http://localhost:8080/api`
- **协议**: HTTP/HTTPS
- **数据格式**: JSON
- **字符编码**: UTF-8

### 1.2 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 1.3 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 推荐相关接口

### 2.1 获取个性化推荐

**接口说明**: 根据用户画像和行为历史，返回个性化的主播推荐列表

```
GET /api/recommend/personal
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| limit | Integer | 否 | 返回数量，默认20 |
| offset | Integer | 否 | 偏移量，默认0 |
| keyword | String | 否 | 搜索关键词 |
| region | String | 否 | 地区筛选 |
| category | String | 否 | 分类筛选 |
| minLevel | Integer | 否 | 最低等级 |
| maxLevel | Integer | 否 | 最高等级 |
| level | Integer | 否 | 指定等级 |

**请求示例**:
```
GET /api/recommend/personal?userId=12345&limit=10&category=游戏&region=北京
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "recommendations": [
      {
        "streamerId": 1001,
        "nickname": "主播小王",
        "avatar": "http://example.com/avatar1.jpg",
        "level": 5,
        "rating": 4.8,
        "category": "游戏",
        "tags": ["英雄联盟", "技术流", "幽默"],
        "giftPrice": 50.0,
        "fansCount": 10000,
        "onlineStatus": true,
        "roomTitle": "王者荣耀上分之路",
        "roomCover": "http://example.com/cover1.jpg",
        "score": 0.95,
        "reason": "基于您的观看历史推荐"
      }
    ],
    "total": 100,
    "hasMore": true
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 2.2 获取热门推荐

**接口说明**: 返回当前热门的主播推荐列表

```
GET /api/recommend/hot
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 返回数量，默认20 |
| offset | Integer | 否 | 偏移量，默认0 |
| category | String | 否 | 分类筛选 |

**响应格式**: 与个性化推荐相同

### 2.3 获取新人推荐

**接口说明**: 返回新主播推荐列表

```
GET /api/recommend/newbie
```

**请求参数**: 与热门推荐相同

**响应格式**: 与个性化推荐相同

### 2.4 提交用户反馈

**接口说明**: 用户对推荐结果的反馈

```
POST /api/recommend/feedback
```

**请求体**:
```json
{
  "userId": 12345,
  "streamerId": 1001,
  "action": "click",
  "rating": 5,
  "comment": "很喜欢这个主播"
}
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| streamerId | Long | 是 | 主播ID |
| action | String | 是 | 行为类型：click/view/like/follow/gift |
| rating | Integer | 否 | 评分1-5 |
| comment | String | 否 | 评论内容 |

**响应示例**:
```json
{
  "code": 200,
  "message": "反馈提交成功",
  "data": null,
  "timestamp": "2024-12-20T10:00:00Z"
}
```

## 3. 主播相关接口

### 3.1 获取主播列表

**接口说明**: 分页获取主播列表

```
GET /api/streamer/list
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页大小，默认20 |
| category | String | 否 | 分类筛选 |
| region | String | 否 | 地区筛选 |
| onlineOnly | Boolean | 否 | 只显示在线，默认false |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1001,
        "nickname": "主播小王",
        "avatar": "http://example.com/avatar1.jpg",
        "gender": "男",
        "age": 25,
        "region": "北京",
        "level": 5,
        "rating": 4.8,
        "giftPrice": 50.0,
        "fansCount": 10000,
        "streamCount": 200,
        "onlineStatus": true,
        "roomTitle": "王者荣耀上分之路",
        "roomCover": "http://example.com/cover1.jpg",
        "introduction": "专业游戏主播，擅长MOBA类游戏",
        "tags": ["英雄联盟", "技术流", "幽默"],
        "createTime": "2024-01-01T00:00:00Z",
        "updateTime": "2024-12-20T10:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 1,
      "pageSize": 20
    },
    "totalElements": 100,
    "totalPages": 5
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 3.2 获取主播详情

**接口说明**: 根据主播ID获取详细信息

```
GET /api/streamer/{id}
```

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 主播ID |

**响应格式**: 返回单个主播详细信息

### 3.3 创建主播

**接口说明**: 创建新的主播信息

```
POST /api/streamer
```

**请求体**:
```json
{
  "nickname": "主播小李",
  "avatar": "http://example.com/avatar2.jpg",
  "gender": "女",
  "age": 23,
  "region": "上海",
  "giftPrice": 60.0,
  "introduction": "甜美声音主播",
  "roomTitle": "陪聊时光",
  "roomCover": "http://example.com/cover2.jpg"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1002,
    "nickname": "主播小李",
    // ... 其他字段
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 3.4 更新主播信息

**接口说明**: 更新主播信息

```
PUT /api/streamer/{id}
```

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 主播ID |

**请求体**: 与创建主播相同，但所有字段都是可选的

### 3.5 删除主播

**接口说明**: 删除主播信息

```
DELETE /api/streamer/{id}
```

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 主播ID |

## 4. 主播搜索接口

### 4.1 搜索主播

**接口说明**: 根据关键词搜索主播

```
GET /api/streamer/search/live
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 是 | 搜索关键词 |
| category | String | 否 | 分类筛选 |
| region | String | 否 | 地区筛选 |
| minLevel | Integer | 否 | 最低等级 |
| maxLevel | Integer | 否 | 最高等级 |
| page | Integer | 否 | 页码，默认1 |
| size | Integer | 否 | 每页大小，默认20 |

**响应格式**: 与主播列表相同

### 4.2 热门搜索词

**接口说明**: 获取热门搜索关键词

```
GET /api/streamer/search/hot-keywords
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "keywords": [
      "游戏",
      "聊天",
      "唱歌",
      "舞蹈",
      "美女主播"
    ]
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

## 5. 用户相关接口

### 5.1 获取用户信息

**接口说明**: 根据用户ID获取用户信息

```
GET /api/user/{id}
```

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 用户ID |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 12345,
    "username": "user123",
    "nickname": "小明",
    "avatar": "http://example.com/user_avatar.jpg",
    "gender": "男",
    "age": 28,
    "region": "北京",
    "preferences": ["游戏", "音乐"],
    "level": 3,
    "totalSpent": 1000.0,
    "createTime": "2024-01-01T00:00:00Z"
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 5.2 更新用户信息

**接口说明**: 更新用户基本信息

```
PUT /api/user/{id}
```

**请求体**:
```json
{
  "nickname": "小明",
  "avatar": "http://example.com/new_avatar.jpg",
  "age": 29,
  "region": "上海",
  "preferences": ["游戏", "音乐", "聊天"]
}
```

### 5.3 获取用户行为记录

**接口说明**: 获取用户的行为历史记录

```
GET /api/user/{id}/behaviors
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 用户ID |
| behaviorType | String | 否 | 行为类型筛选 |
| startTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

## 6. 订单相关接口

### 6.1 创建订单

**接口说明**: 创建直播服务订单

```
POST /api/order
```

**请求体**:
```json
{
  "userId": 12345,
  "streamerId": 1001,
  "categoryId": 1,
  "duration": 60,
  "totalAmount": 50.0,
  "requirements": "希望聊天互动"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "订单创建成功",
  "data": {
    "orderId": "ORDER_20241220_001",
    "status": "待支付",
    "createTime": "2024-12-20T10:00:00Z",
    "totalAmount": 50.0
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 6.2 获取订单列表

**接口说明**: 获取用户的订单列表

```
GET /api/order/user/{userId}
```

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | String | 否 | 订单状态筛选 |
| page | Integer | 否 | 页码 |
| size | Integer | 否 | 每页大小 |

### 6.3 更新订单状态

**接口说明**: 更新订单状态

```
PUT /api/order/{orderId}/status
```

**请求体**:
```json
{
  "status": "已完成",
  "remark": "服务满意"
}
```

## 7. 标签相关接口

### 7.1 获取标签列表

**接口说明**: 获取所有可用标签

```
GET /api/tag/list
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "tags": [
      {
        "id": 1,
        "name": "游戏",
        "category": "兴趣",
        "description": "游戏相关内容",
        "useCount": 1000
      },
      {
        "id": 2,
        "name": "音乐",
        "category": "兴趣", 
        "description": "音乐相关内容",
        "useCount": 800
      }
    ]
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 7.2 获取热门标签

**接口说明**: 获取使用频率最高的标签

```
GET /api/tag/hot
```

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 返回数量，默认10 |

## 8. 监控相关接口

### 8.1 获取系统健康状态

**接口说明**: 获取系统运行状态

```
GET /api/monitor/health
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "status": "UP",
    "components": {
      "database": "UP",
      "redis": "UP",
      "diskSpace": "UP"
    },
    "timestamp": "2024-12-20T10:00:00Z"
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 8.2 获取系统指标

**接口说明**: 获取系统性能指标

```
GET /api/monitor/metrics
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success", 
  "data": {
    "recommendationCount": 10000,
    "averageResponseTime": 150,
    "cacheHitRate": 0.92,
    "activeUsers": 1500,
    "systemLoad": 0.65
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

## 9. 错误处理

### 9.1 参数验证错误

```json
{
  "code": 400,
  "message": "参数验证失败",
  "data": {
    "errors": [
      {
        "field": "userId",
        "message": "用户ID不能为空"
      }
    ]
  },
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 9.2 业务逻辑错误

```json
{
  "code": 404,
  "message": "主播不存在",
  "data": null,
  "timestamp": "2024-12-20T10:00:00Z"
}
```

### 9.3 系统错误

```json
{
  "code": 500,
  "message": "系统内部错误",
  "data": null,
  "timestamp": "2024-12-20T10:00:00Z"
}
```

## 10. SDK和示例

### 10.1 Java SDK示例

```java
// 初始化客户端
RecommendClient client = new RecommendClient("http://localhost:8080/api");

// 获取个性化推荐
RecommendRequest request = RecommendRequest.builder()
    .userId(12345L)
    .limit(10)
    .category("游戏")
    .build();

RecommendResponse response = client.getPersonalRecommend(request);
```

### 10.2 JavaScript SDK示例

```javascript
// 获取个性化推荐
const response = await fetch('/api/recommend/personal?userId=12345&limit=10', {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json'
  }
});

const data = await response.json();
```

---

*本文档最后更新时间：2024年12月*
*版本：v1.0*
*维护团队：后端开发团队* 