-- 创建数据库
CREATE DATABASE IF NOT EXISTS study_room DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE study_room;

-- 用户表
CREATE TABLE `user` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username`        VARCHAR(50)  NOT NULL UNIQUE            COMMENT '用户名（学号/工号）',
    `password`        VARCHAR(255) NOT NULL                   COMMENT '密码（加密）',
    `real_name`       VARCHAR(50)  NOT NULL                   COMMENT '真实姓名',
    `phone`           VARCHAR(20)  DEFAULT NULL               COMMENT '手机号',
    `email`           VARCHAR(100) DEFAULT NULL               COMMENT '邮箱',
    `avatar`          VARCHAR(255) DEFAULT NULL               COMMENT '头像URL',
    `role`            TINYINT      NOT NULL DEFAULT 0         COMMENT '角色：0学生 1管理员 2超级管理员',
    `status`          TINYINT      NOT NULL DEFAULT 1         COMMENT '状态：0禁用 1正常',
    `violation_count` INT          NOT NULL DEFAULT 0         COMMENT '违约次数',
    `deleted`         TINYINT      NOT NULL DEFAULT 0         COMMENT '逻辑删除：0未删 1已删',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 教室表
CREATE TABLE `room` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '教室ID',
    `name`            VARCHAR(50)  NOT NULL                   COMMENT '教室名称',
    `building`        VARCHAR(50)  NOT NULL                   COMMENT '楼栋',
    `floor`           INT          NOT NULL                   COMMENT '楼层',
    `total_seats`     INT          NOT NULL DEFAULT 0         COMMENT '总座位数',
    `description`     VARCHAR(255) DEFAULT NULL               COMMENT '描述',
    `status`          TINYINT      NOT NULL DEFAULT 1         COMMENT '状态：0关闭 1开放',
    `admin_id`        BIGINT       DEFAULT NULL               COMMENT '负责管理员ID',
    `deleted`         TINYINT      NOT NULL DEFAULT 0         COMMENT '逻辑删除',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_building` (`building`),
    INDEX `idx_admin_id` (`admin_id`),
    FOREIGN KEY (`admin_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室表';

-- 座位表
CREATE TABLE `seat` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '座位ID',
    `room_id`         BIGINT       NOT NULL                   COMMENT '所属教室ID',
    `seat_no`         VARCHAR(20)  NOT NULL                   COMMENT '座位编号',
    `row_num`         INT          NOT NULL                   COMMENT '行号',
    `col_num`         INT          NOT NULL                   COMMENT '列号',
    `type`            TINYINT      NOT NULL DEFAULT 0         COMMENT '类型：0普通 1靠窗 2电源插座',
    `status`          TINYINT      NOT NULL DEFAULT 1         COMMENT '状态：0禁用 1可用',
    `deleted`         TINYINT      NOT NULL DEFAULT 0         COMMENT '逻辑删除',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    INDEX `idx_room_id` (`room_id`),
    UNIQUE KEY `uk_room_seat` (`room_id`, `seat_no`),
    FOREIGN KEY (`room_id`) REFERENCES `room`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

-- 预约表
CREATE TABLE `reservation` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '预约ID',
    `user_id`         BIGINT       NOT NULL                   COMMENT '预约用户ID',
    `room_id`         BIGINT       NOT NULL                   COMMENT '教室ID',
    `seat_id`         BIGINT       NOT NULL                   COMMENT '座位ID',
    `date`            DATE         NOT NULL                   COMMENT '预约日期',
    `start_time`      TIME         NOT NULL                   COMMENT '开始时间',
    `end_time`        TIME         NOT NULL                   COMMENT '结束时间',
    `status`          TINYINT      NOT NULL DEFAULT 0         COMMENT '状态：0待签到 1已签到 2已签退 3已取消 4违约 5超时释放',
    `checkin_time`    DATETIME     DEFAULT NULL               COMMENT '实际签到时间',
    `checkout_time`   DATETIME     DEFAULT NULL               COMMENT '实际签退时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0         COMMENT '逻辑删除',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_seat_date` (`seat_id`, `date`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`room_id`) REFERENCES `room`(`id`),
    FOREIGN KEY (`seat_id`) REFERENCES `seat`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 签到记录表
CREATE TABLE `checkin_record` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `reservation_id`  BIGINT       NOT NULL                   COMMENT '预约ID',
    `user_id`         BIGINT       NOT NULL                   COMMENT '用户ID',
    `type`            TINYINT      NOT NULL                   COMMENT '类型：1签到 2签退',
    `time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '操作时间',
    `ip`              VARCHAR(50)  DEFAULT NULL               COMMENT 'IP地址',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    INDEX `idx_reservation_id` (`reservation_id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`reservation_id`) REFERENCES `reservation`(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到记录表';

-- 违约记录表
CREATE TABLE `violation` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '违约ID',
    `user_id`         BIGINT       NOT NULL                   COMMENT '用户ID',
    `reservation_id`  BIGINT       NOT NULL                   COMMENT '关联预约ID',
    `reason`          TINYINT      NOT NULL                   COMMENT '原因：1超时未签到 2提前离开 3恶意占座',
    `description`     VARCHAR(255) DEFAULT NULL               COMMENT '详细说明',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '记录时间',
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`reservation_id`) REFERENCES `reservation`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='违约记录表';

-- 教室开放时间表
CREATE TABLE `room_open_time` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `room_id`         BIGINT       NOT NULL                   COMMENT '教室ID',
    `day_of_week`     TINYINT      NOT NULL                   COMMENT '星期几：1-7',
    `start_time`      TIME         NOT NULL                   COMMENT '开放时间',
    `end_time`        TIME         NOT NULL                   COMMENT '关闭时间',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '创建时间',
    INDEX `idx_room_id` (`room_id`),
    FOREIGN KEY (`room_id`) REFERENCES `room`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教室开放时间表';

-- 系统配置表
CREATE TABLE `system_config` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `config_key`      VARCHAR(50)  NOT NULL UNIQUE            COMMENT '配置键',
    `config_value`    VARCHAR(255) NOT NULL                   COMMENT '配置值',
    `description`     VARCHAR(255) DEFAULT NULL               COMMENT '描述',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE `operation_log` (
    `id`              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id`         BIGINT       NOT NULL                   COMMENT '操作人ID',
    `module`          VARCHAR(50)  NOT NULL                   COMMENT '模块',
    `operation`       VARCHAR(50)  NOT NULL                   COMMENT '操作',
    `detail`          TEXT         DEFAULT NULL               COMMENT '操作详情',
    `ip`              VARCHAR(50)  DEFAULT NULL               COMMENT 'IP地址',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP     COMMENT '操作时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 插入系统默认配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES
('max_reserve_days', '3', '最多提前几天预约'),
('max_reserve_per_day', '2', '每人每天最多预约次数'),
('checkin_timeout', '30', '签到超时时间（分钟）'),
('max_violation_count', '5', '最大违约次数'),
('reserve_duration', '4', '单次预约最大时长（小时）');

-- 创建超管账号的方法（二选一）：
-- 方法1：启动后端后调用接口
-- POST http://localhost:8080/api/auth/register
-- Body: {"username":"admin","password":"admin123","realName":"超级管理员","role":2}

-- 方法2：手动插入（需先生成BCrypt哈希值）
-- 运行 GeneratePassword.java 生成哈希后替换下面的值
-- INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `status`) VALUES
-- ('admin', '替换为生成的BCrypt哈希', '超级管理员', 2, 1);
