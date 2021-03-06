/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : db_second_kill

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 13/03/2020 22:54:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编号',
  `stock` bigint(20) NULL DEFAULT NULL COMMENT '库存',
  `purchase_time` date NULL DEFAULT NULL COMMENT '采购时间',
  `is_active` int(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for item_kill
-- ----------------------------
DROP TABLE IF EXISTS `item_kill`;
CREATE TABLE `item_kill`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `total` int(11) NULL DEFAULT NULL COMMENT '可被秒杀的总数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀结束时间',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '待秒杀商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for item_kill_success
-- ----------------------------
DROP TABLE IF EXISTS `item_kill_success`;
CREATE TABLE `item_kill_success`  (
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀成功生成的订单编号',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `kill_id` int(11) NULL DEFAULT NULL COMMENT '秒杀商品id',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `status` tinyint(4) NULL DEFAULT -1 COMMENT '秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for random_code
-- ----------------------------
DROP TABLE IF EXISTS `random_code`;
CREATE TABLE `random_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效(1=是；0=否)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 889 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
