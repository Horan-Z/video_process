create database video_process;

use video_process;

CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,  -- 自增主键（内部使用）
                      uuid CHAR(36) UNIQUE NOT NULL,      -- 外部唯一标识
                      username VARCHAR(50) UNIQUE NOT NULL,
                      password CHAR(60) NOT NULL,
                      create_time DATETIME NOT NULL,
                      update_time DATETIME NOT NULL
);

CREATE TABLE `file` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
                        `belong_to` char(36) NOT NULL COMMENT '所属用户ID',
                        `file_type` tinyint NOT NULL DEFAULT 0 COMMENT '文件最后一次操作类型',
                        `file_name` varchar(255) NOT NULL COMMENT '原始文件名',
                        `file_uuid` char(36) UNIQUE NOT NULL COMMENT '文件UUID',
                        `file_extension` varchar(10) NOT NULL COMMENT '文件后缀',
                        `file_path` varchar(100) NOT NULL COMMENT '文件存储路径',
                        `file_size_bytes` bigint NOT NULL COMMENT '文件大小(字节)',
                        `video_codec` varchar(20) DEFAULT NULL COMMENT '视频编码格式',
                        `video_format` varchar(20) DEFAULT NULL COMMENT '视频文件格式',
                        `video_bitrate` bigint DEFAULT NULL COMMENT '视频比特率',
                        `video_duration_millis` bigint DEFAULT NULL COMMENT '视频时长(毫秒)',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `last_used_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后使用时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_belong_used` (`belong_to`,`last_used_time`),
                        KEY `idx_belong_size` (`belong_to`,`file_size_bytes`),
                        KEY `idx_belong_name` (`belong_to`,`file_name`),
                        KEY `idx_belong_extension` (`belong_to`,`file_extension`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';