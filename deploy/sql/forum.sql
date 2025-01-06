SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          INT                                                           NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
    `nickname`    VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '昵称',
    `username`    VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `phone`       VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '手机号',
    `password`    VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `status`      TINYINT                                                       NOT NULL DEFAULT 1 COMMENT '状态 1 正常 2 注销 3 禁言',
    `avatar`      VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
    `type`        TINYINT                                                       NOT NULL DEFAULT 1 COMMENT '用户类型 1 普通用户 2 管理员 3 官方认证',
    `email`       VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '邮箱',
    `created_on`  DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on` DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`  DATETIME                                                               DEFAULT NULL COMMENT '删除时间',
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
    `id`            INT                                                            NOT NULL AUTO_INCREMENT COMMENT '帖子 ID',
    `user_id`       INT                                                            NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `category_id`   INT                                                            NOT NULL DEFAULT 0 COMMENT '板块 ID',
    `content`       VARCHAR(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
    `title`         VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '标题',
    `view_count`    INT                                                            NOT NULL DEFAULT 0 COMMENT '浏览数',
    `upvote_count`  INT                                                            NOT NULL DEFAULT 0 COMMENT '点赞数',
    `reply_count`   INT                                                            NOT NULL DEFAULT 0 COMMENT '回复数',
    `collect_count` INT                                                            NOT NULL DEFAULT 0 COMMENT '收藏数',
    `share_count`   INT                                                            NOT NULL DEFAULT 0 COMMENT '分享数',
    `visibility`    TINYINT                                                        NOT NULL DEFAULT 4 COMMENT '可见性 1 私密 2 关注可见 3 好友可见 4 公开',
    `is_top`        TINYINT                                                        NOT NULL DEFAULT 0 COMMENT '是否置顶',
    `is_essence`    TINYINT                                                        NOT NULL DEFAULT 0 COMMENT '是否精华',
    `is_self_top`   TINYINT                                                        NOT NULL DEFAULT 0 COMMENT '是否为个人页面的置顶',
    `ip`            VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT 'IP 地址',
    `ip_loc`        VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT 'IP 城市地址',
    `created_on`    DATETIME                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on`   DATETIME                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`    DATETIME                                                                DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_user_id` (`user_id`),
    KEY `idx_post_category_id` (`category_id`),
    KEY `idx_post_visibility` (`visibility`),
    KEY `idx_post_created_on` (`created_on`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='帖子';

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`          INT                                                           NOT NULL AUTO_INCREMENT COMMENT '板块 ID',
    `name`        VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '板块名称',
    `description` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '板块描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_category_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='板块';

-- ----------------------------
-- Table structure for user_setting
-- ----------------------------
DROP TABLE IF EXISTS `user_setting`;
CREATE TABLE `user_setting`
(
    `id`               INT     NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`          INT     NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `post_time_limit`  TINYINT NOT NULL DEFAULT 4 COMMENT '帖子时间限制 1 一周 2 一个月 3 半年 4 全部',
    `post_reply_limit` TINYINT NOT NULL DEFAULT 2 COMMENT '帖子回复限制 1 禁止 2 全部',
    PRIMARY KEY (`id`),
    KEY `idx_user_setting_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户设置';

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`
(
    `id`           INT                                                           NOT NULL AUTO_INCREMENT COMMENT '回复 ID',
    `post_id`      INT                                                           NOT NULL DEFAULT 0 COMMENT '帖子 ID',
    `root`         INT                                                           NOT NULL DEFAULT 0 COMMENT 'Root reply ID',
    `parent`       INT                                                           NOT NULL DEFAULT 0 COMMENT 'Parent reply ID',
    `user_id`      INT                                                           NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `content`      VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
    `ip`           VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'IP 地址',
    `ip_loc`       VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'IP 城市地址',
    `is_essence`   TINYINT                                                       NOT NULL DEFAULT 0 COMMENT '是否精选',
    `upvote_count` INT                                                           NOT NULL DEFAULT 0 COMMENT '点赞数',
    `reply_count`  INT                                                           NOT NULL DEFAULT 0 COMMENT '回复数',
    `created_on`   DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_on`  DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted_on`   DATETIME                                                               DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    KEY `idx_reply_user_id` (`user_id`),
    KEY `idx_reply_created_on` (`created_on`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='回复';

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`               INT                                                           NOT NULL AUTO_INCREMENT COMMENT '消息 ID',
    `sender_user_id`   INT                                                           NOT NULL DEFAULT 0 COMMENT '发送方用户 ID',
    `receiver_user_id` INT                                                           NOT NULL DEFAULT 0 COMMENT '接收方用户 ID',
    `type`             TINYINT                                                       NOT NULL DEFAULT 0 COMMENT '消息类型 1 评论 2 回复 3 私信 4 系统通知',
    `content`          VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
    `is_read`          TINYINT                                                       NOT NULL DEFAULT 0 COMMENT '是否已读',
    `created_on`       DATETIME                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_message_sender_user_id` (`sender_user_id`),
    KEY `idx_message_receiver_user_id` (`receiver_user_id`),
    KEY `idx_message_is_read` (`is_read`),
    KEY `idx_message_type` (`type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='消息';

-- ----------------------------
-- Table structure for post_collect
-- ----------------------------
DROP TABLE IF EXISTS `post_collect`;
CREATE TABLE `post_collect`
(
    `id`         INT      NOT NULL AUTO_INCREMENT COMMENT '收藏 ID',
    `post_id`    INT      NOT NULL DEFAULT 0 COMMENT '帖子 ID',
    `user_id`    INT      NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_post_collect_post_user` (`post_id`, `user_id`),
    KEY `idx_post_collect_post_id` (`post_id`),
    KEY `idx_post_collect_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='收藏';

-- ----------------------------
-- Table structure for post_upvote
-- ----------------------------
DROP TABLE IF EXISTS `post_upvote`;
CREATE TABLE `post_upvote`
(
    `id`         INT      NOT NULL AUTO_INCREMENT COMMENT '点赞 ID',
    `post_id`    INT      NOT NULL DEFAULT 0 COMMENT '帖子 ID',
    `user_id`    INT      NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_post_upvote_post_user` (`post_id`, `user_id`),
    KEY `idx_post_upvote_post_id` (`post_id`),
    KEY `idx_post_upvote_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='点赞';

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`         INT                                                          NOT NULL AUTO_INCREMENT COMMENT '标签 ID',
    `user_id`    INT                                                          NOT NULL DEFAULT 0 COMMENT '创建者 ID',
    `name`       VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签名称',
    `created_on` DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_tag_name` (`name`),
    KEY `idx_tag_user_id` (`user_id`)
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
    `id`      INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `post_id` INT NOT NULL DEFAULT 0 COMMENT '帖子 ID',
    `tag_id`  INT NOT NULL DEFAULT 0 COMMENT '标签 ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_post_tag_post_tag` (`post_id`, `tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='帖子标签';

-- ----------------------------
-- Table structure for shield_tag
-- ----------------------------
DROP TABLE IF EXISTS `shield_tag`;
CREATE TABLE `shield_tag`
(
    `id`         INT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    INT      NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `tag_id`     INT      NOT NULL DEFAULT 0 COMMENT '标签 ID',
    `created_on` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_shield_tag_user_tag` (`user_id`, `tag_id`),
    KEY `idx_shield_tag_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='屏蔽标签';

-- ----------------------------
-- Table structure for shield_user
-- ----------------------------
DROP TABLE IF EXISTS `shield_user`;
CREATE TABLE `shield_user`
(
    `id`             INT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        INT      NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `shield_user_id` INT      NOT NULL DEFAULT 0 COMMENT '屏蔽用户 ID',
    `created_on`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_shield_user_user` (`user_id`, `shield_user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='屏蔽用户';

-- ----------------------------
-- Table structure for following
-- ----------------------------
DROP TABLE IF EXISTS `following`;
CREATE TABLE `following`
(
    `id`                 INT      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`            INT      NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `follow_id`          INT      NOT NULL DEFAULT 0 COMMENT '关注用户 ID',
    `following_group_id` INT      NOT NULL DEFAULT 0 COMMENT '分组 ID',
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
    `id`         INT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    INT         NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `name`       VARCHAR(32) NOT NULL DEFAULT '' COMMENT '分组名称',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_following_group_user_name` (`user_id`, `name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='关注分组';

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`
(
    `id`         INT          NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    INT          NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `content`    VARCHAR(255) NOT NULL DEFAULT '' COMMENT '内容',
    `created_on` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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
    `id`         INT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`    INT         NOT NULL DEFAULT 0 COMMENT '用户 ID',
    `post_id`    INT         NOT NULL DEFAULT 0 COMMENT '帖子 ID',
    `type_id`    INT         NOT NULL DEFAULT 0 COMMENT '举报类型 ID',
    `reason`     VARCHAR(64) NOT NULL DEFAULT '' COMMENT '举报理由',
    `status`     TINYINT     NOT NULL DEFAULT 0 COMMENT '状态 1 未处理 2 举报失败 3 举报成功',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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
    `id`         INT         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`       VARCHAR(32) NOT NULL DEFAULT '' COMMENT '名称',
    `created_on` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_report_type_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='举报类型';