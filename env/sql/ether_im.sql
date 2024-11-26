/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.4_3306
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : 192.168.1.4:3306
 Source Schema         : ether_im

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 25/09/2024 19:31:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `im_chat_message`;
CREATE TABLE `im_chat_message`
(
    `id`              bigint(20)  NOT NULL,
    `message_type`    varchar(16) NOT NULL COMMENT '消息类型',
    `sender_id`       varchar(32) NOT NULL COMMENT '发送者ID',
    `sender_terminal` varchar(8)  NOT NULL COMMENT '发送者终端类型:WEB  APP ',
    `receiver_id`     varchar(32) NOT NULL COMMENT '接收者ID',
    `status`          varchar(16) NOT NULL COMMENT '消息状态：未发送 已发送 已推送 已触达 已撤回 已读',
    `content`         text        NOT NULL COMMENT '消息内容',
    `content_type`    varchar(16) NOT NULL COMMENT '消息内容类型: 文字 图片 文件 语音 视频 ',
    `send_time`       bigint(20)  NOT NULL COMMENT '发送时间',
    `create_time`     datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `im_personal_message_pk` (`sender_id`, `sender_terminal`, `send_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='单聊消息表';

-- ----------------------------
-- Table structure for im_chat_message_inbox
-- ----------------------------
DROP TABLE IF EXISTS `im_chat_message_inbox`;
CREATE TABLE `im_chat_message_inbox`
(
    `receiver_id`  varchar(32) NOT NULL COMMENT '接收者ID',
    `sender_id`    varchar(32) NOT NULL COMMENT '发送者ID',
    `message_id`   bigint(20)  NOT NULL COMMENT '消息ID',
    `message_type` varchar(16) NOT NULL COMMENT '消息类型  群聊还是单聊',
    `send_time`    bigint(20)  NOT NULL COMMENT '发送时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='收件箱';

-- ----------------------------
-- Table structure for im_group_user
-- ----------------------------
DROP TABLE IF EXISTS `im_group_user`;
CREATE TABLE `im_group_user`
(
    `group_id` varchar(32) NOT NULL COMMENT '群ID',
    `user_id`  varchar(32) NOT NULL COMMENT '用户ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='群和用户关系';



SET FOREIGN_KEY_CHECKS = 1;
