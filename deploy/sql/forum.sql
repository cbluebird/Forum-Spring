SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           BIGINT                                                        NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
    `nickname`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '昵称',
    `username`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `phone`        varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '手机号',
    `password`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'sha256 密码',
    `status`       tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态，1 正常，2 注销 3 禁言',
    `avatar`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
    `type` tinyint NOT NULL DEFAULT 1 COMMENT '用户类型，1 普通用户，2 管理员 3 官方认证',
    `email`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '邮箱',
    `created_on`   DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on`  DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`   DATETIME                                                               DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_username` (`username`),
    UNIQUE KEY `idx_user_phone` (`phone`),
    UNIQUE KEY `idx_user_email` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户';


-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `id`               BIGINT                                                       NOT NULL AUTO_INCREMENT COMMENT '主题 ID',
    `user_id`          BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `category_id`      BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '板块 ID',
    `title`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
    `comment_count`    BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '评论数',
    `collection_count` BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '收藏数',
    `upvote_count`     BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '点赞数',
    `view_count`       BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '浏览数',
    `share_count`      BIGINT                                                       NOT NULL DEFAULT '0' COMMENT '分享数',
    `visibility` tinyint NOT NULL DEFAULT '0' COMMENT '可见性: 0 私密 1 关注可见 2 好友可见 3 公开',
    `is_top`           tinyint                                                      NOT NULL DEFAULT '0' COMMENT '是否置顶',
    `is_essence`       tinyint                                                      NOT NULL DEFAULT '0' COMMENT '是否精华',
    `is_self_hot`      tinyint                                                      NOT NULL DEFAULT '0' COMMENT '是否为个人页面的置顶',
    `ip`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP 地址',
    `ip_loc`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'IP 城市地址',
    `created_on`       DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on`      DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`       DATETIME                                                              DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_user_id` (`user_id`),
    KEY `idx_post_visibility` (`visibility`),
    KEY `idx_post_created_on` (`created_on`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='冒泡/文章';

-- ----------------------------
-- Table structure for post_content
-- ----------------------------
DROP TABLE IF EXISTS `post_content`;
CREATE TABLE `post_content`
(
    `id`      BIGINT                                                         NOT NULL AUTO_INCREMENT COMMENT '内容 ID',
    `post_id` BIGINT                                                         NOT NULL DEFAULT '0' COMMENT 'POST ID',
    `content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
    `type`    tinyint                                                        NOT NULL DEFAULT '2' COMMENT '类型1 文字段落，2 图片地址，3 视频地址，4 链接地址',
    `is_del`  tinyint                                                        NOT NULL DEFAULT '0' COMMENT '是否删除 0 为未删除、 1 为已删除',
    PRIMARY KEY (`id`),
    KEY `idx_post_content_post_id` (`post_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='冒泡/文章内容';



-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`
(
    `id`          BIGINT                                                        NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `name`        VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '板块名称',
    `description` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
    `quote_num`   BIGINT                                                        NOT NULL DEFAULT '0' COMMENT '引用数',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签';

-- ----------------------------
-- Table structure for post_setting
-- ----------------------------
DROP TABLE IF EXISTS `post_setting`;
CREATE TABLE `post_setting`
(
    `id`             BIGINT NOT NULL AUTO_INCREMENT COMMENT '内容 ID',
    `user_id`        BIGINT NOT NULL DEFAULT '0' COMMENT 'POST ID',
    `time_limit_id`  int    NOT NULL DEFAULT '100' COMMENT '时间限制',
    `reply_limit_id` int    NOT NULL DEFAULT '100' COMMENT '回复限制',
    PRIMARY KEY (`id`),
    KEY `idx_post_setting_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='post 权限设置';

-- ----------------------------
-- Table structure for post_time_limit
-- ----------------------------
DROP TABLE IF EXISTS `post_time_limit`;
CREATE TABLE `post_time_limit`
(
    `id`      BIGINT      NOT NULL AUTO_INCREMENT COMMENT '内容 ID',
    `content` varchar(32) NOT NULL DEFAULT '' COMMENT '内容',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='post 时间限制';

-- ----------------------------
-- Table structure for post_reply_limit
-- ----------------------------
DROP TABLE IF EXISTS `post_reply_limit`;
CREATE TABLE `post_reply_limit`
(
    `id`      BIGINT      NOT NULL AUTO_INCREMENT COMMENT '内容 ID',
    `content` varchar(32) NOT NULL DEFAULT '' COMMENT '内容',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='post 回复限制';

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`
(
    `id`                BIGINT                                                         NOT NULL AUTO_INCREMENT COMMENT '评论 ID',
    `post_id`           BIGINT                                                         NOT NULL DEFAULT '0' COMMENT 'POST ID',
    `root`              BIGINT                                                         NOT NULL DEFAULT '0' COMMENT 'Root reply ID',
    `parent`            BIGINT                                                         NOT NULL DEFAULT '0' COMMENT 'parent reply ID',
    `user_id`           BIGINT                                                         NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `content`           varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
    `ip`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT 'IP 地址',
    `ip_loc`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT 'IP 城市地址',
    `is_essence`        tinyint                                                        NOT NULL DEFAULT 0 COMMENT '是否精选',
    `reply_count`       int                                                            NOT NULL DEFAULT 0 COMMENT '回复数',
    `thumbs_up_count`   int                                                            NOT NULL DEFAULT 0 COMMENT '点赞数',
    `thumbs_down_count` int                                                            NOT NULL DEFAULT 0 COMMENT '点踩数',
    `created_on`        DATETIME                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on`       DATETIME                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`        DATETIME                                                                DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_reply_user_id` (`user_id`),
    KEY `idx_reply_created_on` (`created_on`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='评论';

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`               BIGINT                                                        NOT NULL AUTO_INCREMENT COMMENT '消息通知 ID',
    `sender_user_id`   BIGINT                                                        NOT NULL DEFAULT '0' COMMENT '发送方用户 ID',
    `receiver_user_id` BIGINT                                                        NOT NULL DEFAULT '0' COMMENT '接收方用户 ID',
    `message_type_id` tinyint NOT NULL DEFAULT '1' COMMENT '通知类型，1 动态，2 评论，3 回复，4 私信，5 系统通知',
    `content`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '详细内容',
    `is_read`          tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否已读',
    `created_on`       DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_message_receiver_user_id` (`receiver_user_id`),
    KEY `idx_message_is_read` (`is_read`),
    KEY `idx_message_type` (`message_type_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='消息通知';

-- ----------------------------
-- Table structure for message_type
-- ----------------------------
DROP TABLE IF EXISTS `message_type`;
CREATE TABLE `message_type`
(
    `id`         BIGINT                                                       NOT NULL AUTO_INCREMENT COMMENT '消息通知 ID',
    `name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
    `created_on` DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_message_type_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='消息通知类型';

-- ----------------------------
-- Table structure for post_collection
-- ----------------------------
DROP TABLE IF EXISTS `post_collection`;
CREATE TABLE `post_collection`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT COMMENT '收藏 ID',
    `post_id`    BIGINT   NOT NULL DEFAULT '0' COMMENT 'POST ID',
    `user_id`    BIGINT   NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted_on` DATETIME          DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_collection_post_id` (`post_id`),
    KEY `idx_post_collection_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章收藏';

-- ----------------------------
-- Table structure for post_star
-- ----------------------------
DROP TABLE IF EXISTS `post_star`;
CREATE TABLE `post_star`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT COMMENT '点赞 ID',
    `post_id`    BIGINT   NOT NULL DEFAULT '0' COMMENT 'POST ID',
    `user_id`    BIGINT   NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted_on` DATETIME          DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_star_post_id` (`post_id`),
    KEY `idx_post_star_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章点赞';

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`         BIGINT                                                        NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `user_id`    BIGINT                                                        NOT NULL DEFAULT '0' COMMENT '创建者 ID',
    `tag`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
    `quote_num`  BIGINT                                                        NOT NULL DEFAULT '0' COMMENT '引用数',
    `created_on` DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_tag_tag` (`tag`),
    KEY `idx_tag_user_id` (`user_id`),
    KEY `idx_tag_quote_num` (`quote_num`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签';

-- ----------------------------
-- Table structure for post_tag
-- ----------------------------
DROP TABLE IF EXISTS `post_tag`;
CREATE TABLE `post_tag`
(
    `id`      BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `post_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'post ID',
    `tag_id`  BIGINT NOT NULL DEFAULT '0' COMMENT 'tag ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_post_tag_post_tag` (`post_id`, `tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章的标签';

-- ----------------------------
-- Table structure for shield_tag
-- ----------------------------
DROP TABLE IF EXISTS `shield_tag`;
CREATE TABLE `shield_tag`
(
    `id`         BIGINT   NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `user_id`    BIGINT   NOT NULL DEFAULT '0' COMMENT '创建者 ID',
    `tag_id`     BIGINT   NOT NULL DEFAULT '0' COMMENT 'Tag ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_shield_tag_user_tag` (`user_id`, `tag_id`),
    KEY `idx_shield_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='屏蔽的标签';

-- ----------------------------
-- Table structure for shield_user
-- ----------------------------
DROP TABLE IF EXISTS `shield_user`;
CREATE TABLE `shield_user`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        BIGINT   NOT NULL DEFAULT '0' COMMENT '创建者 ID',
    `shield_user_id` BIGINT   NOT NULL DEFAULT '0' COMMENT '屏蔽的用户 ID',
    `created_on`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_shield_user_user` (`user_id`, `shield_user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='屏蔽的用户';

-- ----------------------------
-- Table structure for following
-- ----------------------------
DROP TABLE IF EXISTS `following`;
CREATE TABLE `following`
(
    `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
    `user_id`            BIGINT   NOT NULL,
    `follow_id`          BIGINT   NOT NULL,
    `following_group_id` BIGINT   NOT NULL DEFAULT '0' COMMENT '分组 ID',
    `created_on`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_following_user_follow` (`user_id`, `follow_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for following_group
-- ----------------------------
DROP TABLE IF EXISTS `following_group`;
CREATE TABLE `following_group`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    int         NOT NULL DEFAULT '0' COMMENT '用户 id',
    `name`       varchar(32) NOT NULL DEFAULT '' COMMENT '分组名称',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_following_group_user_name` (`user_id`, `name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='联系人分组';

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    int         NOT NULL DEFAULT '0' COMMENT '用户 id',
    `content`    varchar(32) NOT NULL DEFAULT '' COMMENT '内容',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_notice_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='通知';

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`
(
    `id`             BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        int         NOT NULL DEFAULT '0' COMMENT '用户 id',
    `post_id`        int         NOT NULL DEFAULT '0' COMMENT '帖子 id',
    `reason`         varchar(32) NOT NULL DEFAULT '' COMMENT '举报理由',
    `created_on`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status`         tinyint     NOT NULL DEFAULT '0' COMMENT '状态 0 未处理 1 已处理',
    `report_type_id` int         NOT NULL DEFAULT '0' COMMENT '举报类型',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_report_user_post` (`user_id`, `post_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='举报';

-- ----------------------------
-- Table structure for report_type
-- ----------------------------
DROP TABLE IF EXISTS `report_type`;
CREATE TABLE `report_type`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`       varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_report_type_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='举报类型';