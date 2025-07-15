-- 用户画像表
CREATE TABLE user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    age INT,
    gender VARCHAR(10),
    region VARCHAR(50),
    register_time DATETIME,
    device_type VARCHAR(50),
    os_type VARCHAR(50),
    game_types TEXT,
    play_frequency VARCHAR(20),
    skill_level VARCHAR(20),
    total_game_time INT,
    win_rate DOUBLE,
    total_watch_time INT,
    total_interaction_count INT,
    total_consumption DECIMAL(10,2),
    last_active_time DATETIME,
    current_game VARCHAR(50),
    online_status VARCHAR(20),
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_user_id (user_id)
);

-- 主播表
CREATE TABLE streamer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    age INT,
    gender VARCHAR(10),
    region VARCHAR(50),
    register_time DATETIME,
    nickname VARCHAR(50),
    avatar VARCHAR(200),
    introduction TEXT,
    categories TEXT,
    stream_level VARCHAR(20),
    interaction_rate DOUBLE,
    total_viewers INT,
    engagement_rate DOUBLE,
    rating DOUBLE,
    completed_streams INT,
    gift_price DECIMAL(10,2),
    price_range VARCHAR(50),
    online_status TINYINT,
    current_viewers INT,
    current_category VARCHAR(50),
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_user_id (user_id)
);

-- 用户-主播交互表
CREATE TABLE user_streamer_interaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    streamer_id BIGINT NOT NULL,
    watch_time INT,
    like_count INT,
    comment_count INT,
    share_count INT,
    gift_amount DECIMAL(10,2),
    rating DOUBLE,
    feedback TEXT,
    category_match DOUBLE,
    level_match DOUBLE,
    style_match DOUBLE,
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_user_id (user_id),
    INDEX idx_streamer_id (streamer_id)
);

-- 标签表
CREATE TABLE tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '标签名称',
    type TINYINT NOT NULL COMMENT '标签类型：1-用户标签，2-主播标签，3-分类标签',
    category VARCHAR(50) NOT NULL COMMENT '标签分类',
    weight DOUBLE DEFAULT 1.0 COMMENT '标签权重',
    description VARCHAR(200) COMMENT '标签描述',
    status TINYINT DEFAULT 1 COMMENT '标签状态：0-禁用，1-启用',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_type_category (type, category)
);

-- 用户标签关联表
CREATE TABLE user_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    weight DOUBLE DEFAULT 1.0 COMMENT '标签权重',
    source TINYINT DEFAULT 1 COMMENT '标签来源：1-系统计算，2-用户选择，3-运营设置',
    status TINYINT DEFAULT 1 COMMENT '标签状态：0-无效，1-有效',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_user_id (user_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_user_tag (user_id, tag_id)
);

-- 主播标签关联表
CREATE TABLE streamer_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    streamer_id BIGINT NOT NULL COMMENT '主播ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    weight DOUBLE DEFAULT 1.0 COMMENT '标签权重',
    source TINYINT DEFAULT 1 COMMENT '标签来源：1-系统计算，2-主播选择，3-运营设置',
    status TINYINT DEFAULT 1 COMMENT '标签状态：0-无效，1-有效',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_streamer_id (streamer_id),
    INDEX idx_tag_id (tag_id),
    UNIQUE KEY uk_streamer_tag (streamer_id, tag_id)
);

-- 直播分类表
CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    type VARCHAR(50) NOT NULL COMMENT '分类类型',
    icon VARCHAR(200) COMMENT '分类图标',
    description TEXT COMMENT '分类描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_type (type),
    INDEX idx_status (status)
);

-- 主播分类关联表
CREATE TABLE streamer_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    streamer_id BIGINT NOT NULL COMMENT '主播ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    level INT DEFAULT 1 COMMENT '主播在该分类的等级',
    score DECIMAL(5,2) DEFAULT 5.0 COMMENT '主播在该分类的评分',
    stream_count INT DEFAULT 0 COMMENT '在该分类的直播场次',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_streamer_id (streamer_id),
    INDEX idx_category_id (category_id),
    UNIQUE KEY uk_streamer_category (streamer_id, category_id)
);

-- 用户画像宽表（HBase表结构）
-- 表名：user_profile_wide
-- RowKey: userId
-- 列族：
--   - basic: 基础特征
--   - behavior: 行为特征
--   - tag: 标签特征
--   - preference: 偏好特征
--   - stat: 统计特征
--   - meta: 元数据

-- 主播画像宽表（HBase表结构）
-- 表名：streamer_wide
-- RowKey: streamerId
-- 列族：
--   - basic: 基础特征
--   - professional: 专业特征
--   - tag: 标签特征
--   - talent: 才艺特征
--   - stat: 统计特征
--   - meta: 元数据

-- 用户表
CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `gender` varchar(10) DEFAULT NULL COMMENT '性别',
    `age` int(11) DEFAULT NULL COMMENT '年龄',
    `region` varchar(64) DEFAULT NULL COMMENT '地区',
    `register_time` datetime NOT NULL COMMENT '注册时间',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_region` (`region`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 主播表
CREATE TABLE `streamer` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主播ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `nickname` varchar(64) NOT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `gender` varchar(10) DEFAULT NULL COMMENT '性别',
    `age` int(11) DEFAULT NULL COMMENT '年龄',
    `region` varchar(64) DEFAULT NULL COMMENT '地区',
    `level` int(11) NOT NULL DEFAULT '1' COMMENT '等级',
    `score` decimal(5,2) NOT NULL DEFAULT '5.00' COMMENT '评分',
    `stream_count` int(11) NOT NULL DEFAULT '0' COMMENT '直播场次',
    `fans_count` int(11) NOT NULL DEFAULT '0' COMMENT '粉丝数量',
    `online_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '在线状态：0-离线，1-在线，2-忙碌',
    `room_title` varchar(100) DEFAULT NULL COMMENT '直播间标题',
    `room_cover` varchar(255) DEFAULT NULL COMMENT '直播间封面',
    `category` varchar(32) DEFAULT NULL COMMENT '直播分类',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_region` (`region`),
    KEY `idx_level` (`level`),
    KEY `idx_category` (`category`),
    KEY `idx_online_status` (`online_status`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主播表';

-- 标签表
CREATE TABLE `tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` varchar(64) NOT NULL COMMENT '标签名称',
    `type` varchar(32) NOT NULL COMMENT '标签类型：user-用户标签，streamer-主播标签',
    `category` varchar(32) NOT NULL COMMENT '标签分类',
    `weight` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '权重',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name_type` (`name`, `type`),
    KEY `idx_type_category` (`type`, `category`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 用户标签关联表
CREATE TABLE `user_tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
    `weight` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '权重',
    `source` tinyint(4) NOT NULL COMMENT '来源：1-规则，2-行为，3-兴趣',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_tag` (`user_id`, `tag_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_tag_id` (`tag_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户标签关联表';

-- 陪玩标签关联表
CREATE TABLE `game_master_tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `master_id` bigint(20) NOT NULL COMMENT '陪玩ID',
    `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
    `weight` decimal(5,2) NOT NULL DEFAULT '1.00' COMMENT '权重',
    `source` tinyint(4) NOT NULL COMMENT '来源：1-规则，2-行为，3-技能',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_master_tag` (`master_id`, `tag_id`),
    KEY `idx_master_id` (`master_id`),
    KEY `idx_tag_id` (`tag_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='陪玩标签关联表';

-- 用户行为表
CREATE TABLE `user_behavior` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `type` varchar(32) NOT NULL COMMENT '行为类型：view-浏览，click-点击，order-下单，comment-评价',
    `target_id` bigint(20) NOT NULL COMMENT '目标ID',
    `target_type` varchar(32) NOT NULL COMMENT '目标类型：master-陪玩，game-游戏',
    `content` varchar(255) DEFAULT NULL COMMENT '行为内容',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_target` (`target_id`, `target_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为表';

-- 订单表
CREATE TABLE `order` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `streamer_id` bigint(20) NOT NULL COMMENT '主播ID',
    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
    `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
    `duration` int(11) NOT NULL COMMENT '订单时长(分钟)',
    `status` tinyint(4) NOT NULL COMMENT '状态：0-取消，1-待支付，2-已支付，3-已完成，4-已评价',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
    `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_streamer_id` (`streamer_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 评价表
CREATE TABLE `comment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `order_id` bigint(20) NOT NULL COMMENT '订单ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `streamer_id` bigint(20) NOT NULL COMMENT '主播ID',
    `score` decimal(3,1) NOT NULL COMMENT '评分',
    `content` varchar(500) DEFAULT NULL COMMENT '评价内容',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_streamer_id` (`streamer_id`),
    KEY `idx_score` (`score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 分类表 (兼容游戏表)
CREATE TABLE `category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(64) NOT NULL COMMENT '分类名称',
    `type` varchar(32) NOT NULL COMMENT '分类类型',
    `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
    `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 主播分类关联表 (兼容陪玩游戏关联表)
CREATE TABLE `streamer_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `streamer_id` bigint(20) NOT NULL COMMENT '主播ID',
    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
    `level` int(11) NOT NULL DEFAULT '1' COMMENT '等级',
    `score` decimal(5,2) NOT NULL DEFAULT '5.00' COMMENT '评分',
    `stream_count` int(11) NOT NULL DEFAULT '0' COMMENT '直播场次',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_master_game` (`master_id`, `game_id`),
    KEY `idx_master_id` (`master_id`),
    KEY `idx_game_id` (`game_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='陪玩游戏关联表';
