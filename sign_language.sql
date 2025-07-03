/*
 Navicat Premium Dump SQL

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : sign_language

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 28/06/2025 21:40:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (4, '18790145359', '200436', 'http://localhost:7877/picture/default_avatar.jpg', 'USER', NULL, NULL);
INSERT INTO `account` VALUES (3, 'a', '200436', 'http://localhost:7877/picture/default_avatar.jpg', 'USER', NULL, NULL);
INSERT INTO `account` VALUES (2, 'aa', '200436', 'http://localhost:7877/picture/default_avatar.jpg', 'USER', NULL, NULL);
INSERT INTO `account` VALUES (1, 'aaa', '123456', 'http://localhost:7877/files/1746458508768-命令1-1.png', 'USER', '18790145359', 'ze535367@163.com');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of video
-- ----------------------------
INSERT INTO `video` VALUES (10, '谢谢', 'http://localhost:7877/videos/谢谢.mp4', '一手伸出拇指，弯曲两下，表示向人感谢。');
INSERT INTO `video` VALUES (9, '忙', 'http://localhost:7877/videos/忙.mp4', '	双手平伸，掌心向下，五指分开，在胸前左右摆动几下。表示双手忙个不停。');
INSERT INTO `video` VALUES (8, '家', 'http://localhost:7877/videos/家.mp4', '	双手搭成“＾”形，如屋顶状');
INSERT INTO `video` VALUES (7, '叫', 'http://localhost:7877/videos/叫.mp4', '一手拇指与四指作“└”形，虎口贴于嘴边，张开嘴，作喊叫状。');
INSERT INTO `video` VALUES (6, '再见', 'http://localhost:7877/videos/再见.mp4', '一手上举，五指自然伸出，手腕挥动两下。这是一般的“再见”手势。');
INSERT INTO `video` VALUES (5, '做', 'http://localhost:7877/videos/做.mp4', '双手握拳，上拳打下拳');
INSERT INTO `video` VALUES (4, '什么', 'http://localhost:7877/videos/什么.mp4', '双手伸开，掌心向下，然后翻转为掌心向上。');
INSERT INTO `video` VALUES (3, '你好', 'http://localhost:7877/videos/你好.mp4', '一手食指指向对方，一手握拳，向上伸出拇指。');
INSERT INTO `video` VALUES (2, '几点', 'http://localhost:7877/videos/几点.mp4', '同多少，一手五指分开，指尖向上，手指微微抖动几下，表示“数量”“多少”之意。');
INSERT INTO `video` VALUES (1, '二十', 'http://localhost:7877/videos/二十.mp4', '一手食、中指直立分开，掌心向外（或向内），弯动两下。');

SET FOREIGN_KEY_CHECKS = 1;
