-- ========================================================
-- CRM 后台管理系统 - 数据库初始化脚本
-- 兼容 MySQL 8.0+ / 5.7+
-- ========================================================

CREATE DATABASE IF NOT EXISTS `crm_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `crm_db`;

-- ----------------------------
-- 1. 用户表 (sys_user)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(100) NOT NULL COMMENT '加密密码',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ----------------------------
-- 2. 角色表 (sys_role)
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(50) NOT NULL COMMENT '角色标识',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 3. 权限/菜单表 (sys_permission)
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父节点ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单/权限名称',
  `perm_key` VARCHAR(100) DEFAULT NULL COMMENT '权限标识(例 sys:user:add)',
  `type` TINYINT NOT NULL COMMENT '类型: 1-目录 2-菜单 3-按钮',
  `path` VARCHAR(100) DEFAULT NULL COMMENT '路由路径',
  `sort` INT DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限菜单表';

-- ----------------------------
-- 4. 用户-角色关联表 (sys_user_role)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 5. 角色-权限关联表 (sys_role_permission)
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_id` BIGINT NOT NULL,
  `permission_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ----------------------------
-- 6. 客户档案表 (crm_customer)
-- ----------------------------
DROP TABLE IF EXISTS `crm_customer`;
CREATE TABLE `crm_customer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '客户姓名/公司名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `company` VARCHAR(150) DEFAULT NULL COMMENT '公司名称',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '联系地址',
  `industry` VARCHAR(50) DEFAULT NULL COMMENT '所属行业',
  `level` VARCHAR(20) DEFAULT '普通客户' COMMENT '客户级别 (VIP客户/重要客户/普通客户)',
  `status` VARCHAR(20) DEFAULT '跟进中' COMMENT '跟进状态 (潜在/跟进中/已成交/已流失)',
  `owner_id` BIGINT DEFAULT NULL COMMENT '负责人User ID',
  `creator_id` BIGINT DEFAULT NULL COMMENT '创建人User ID',
  `remark` TEXT DEFAULT NULL COMMENT '备注信息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户管理表';

-- ----------------------------
-- 7. 销售线索表 (crm_clue)
-- ----------------------------
DROP TABLE IF EXISTS `crm_clue`;
CREATE TABLE `crm_clue` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '线索名称/联系人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `company` VARCHAR(150) DEFAULT NULL COMMENT '公司',
  `source` VARCHAR(50) DEFAULT '线上咨询' COMMENT '线索来源',
  `status` VARCHAR(20) DEFAULT '未处理' COMMENT '状态 (未处理/转化中/已转化/已作废)',
  `owner_id` BIGINT DEFAULT NULL COMMENT '负责人User ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售线索表';

-- ----------------------------
-- 8. 销售商机表 (crm_opportunity)
-- ----------------------------
DROP TABLE IF EXISTS `crm_opportunity`;
CREATE TABLE `crm_opportunity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` BIGINT NOT NULL COMMENT '关联客户ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商机名称',
  `amount` DECIMAL(12, 2) DEFAULT 0.00 COMMENT '预计金额',
  `stage` VARCHAR(30) DEFAULT '初步沟通' COMMENT '商机阶段 (初步沟通/需求确认/方案报价/签订合同/赢单)',
  `owner_id` BIGINT DEFAULT NULL COMMENT '负责人User ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售商机表';

-- ========================================================
-- 初始 Seed 数据 (初始管理员密码为 123456)
-- BCrypt 密文: $2a$10$IUj5SL6b4GrrTUfEz0QdA.l6drshMBZpougilAMF1Y86anUdoSlwK
-- ========================================================

-- 插入默认管理员与销售账号
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES
(1, 'admin', '$2a$10$IUj5SL6b4GrrTUfEz0QdA.l6drshMBZpougilAMF1Y86anUdoSlwK', '超级管理员', '13800138000', 'admin@crm.com', 1),
(2, 'seller', '$2a$10$IUj5SL6b4GrrTUfEz0QdA.l6drshMBZpougilAMF1Y86anUdoSlwK', '资深销售张三', '13900139000', 'zhangsan@crm.com', 1);

-- 插入默认角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `description`) VALUES
(1, '超级管理员', 'ROLE_ADMIN', '拥有系统最高权限'),
(2, '销售主管', 'ROLE_MANAGER', '可管理团队客户与商机'),
(3, '普通销售', 'ROLE_SALES', '管理个人负责的客户与线索');

-- 插入默认权限菜单
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `perm_key`, `type`, `path`, `sort`) VALUES
(1, 0, '系统管理', 'sys:manage', 1, '/system', 1),
(2, 1, '用户管理', 'sys:user:query', 2, '/system/user', 1),
(3, 2, '新增用户', 'sys:user:add', 3, NULL, 1),
(4, 2, '修改用户', 'sys:user:update', 3, NULL, 2),
(5, 2, '删除用户', 'sys:user:delete', 3, NULL, 3),
(6, 1, '角色管理', 'sys:role:query', 2, '/system/role', 2),
(7, 6, '分配角色', 'sys:role:assign', 3, NULL, 1),
(8, 0, 'CRM客户管理', 'crm:manage', 1, '/crm', 2),
(9, 8, '客户列表', 'crm:customer:query', 2, '/crm/customer', 1),
(10, 9, '新增客户', 'crm:customer:add', 3, NULL, 1),
(11, 9, '编辑客户', 'crm:customer:update', 3, NULL, 2),
(12, 9, '删除客户', 'crm:customer:delete', 3, NULL, 3),
(13, 8, '线索管理', 'crm:clue:query', 2, '/crm/clue', 2),
(14, 8, '商机管理', 'crm:opportunity:query', 2, '/crm/opportunity', 3);

-- 用户绑定角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin -> 超级管理员
(2, 3); -- seller -> 普通销售

-- 角色绑定权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14),
(3, 8), (3, 9), (3, 10), (3, 11), (3, 13), (3, 14);

-- 插入示例客户数据
INSERT INTO `crm_customer` (`id`, `name`, `phone`, `email`, `company`, `address`, `industry`, `level`, `status`, `owner_id`, `creator_id`, `remark`) VALUES
(1, '阿里巴巴科技', '0571-88888888', 'contact@alibaba.com', '阿里巴巴集团', '浙江省杭州市余杭区文一西路969号', '互联网/科技', 'VIP客户', '已成交', 2, 1, '重点战略合作客户'),
(2, '腾讯计算机', '0755-86013388', 'biz@tencent.com', '腾讯控股', '广东省深圳市南山区深南大道10000号', '互联网/软件', 'VIP客户', '跟进中', 2, 1, '意向强烈，需推进方案'),
(3, '华为技术有限公司', '0755-28560888', 'info@huawei.com', '华为', '广东省深圳市龙岗区坂田华为基地', '通讯/硬件', '重要客户', '跟进中', 1, 1, '正在进行需求确认');

-- 插入示例线索数据
INSERT INTO `crm_clue` (`id`, `name`, `phone`, `company`, `source`, `status`, `owner_id`) VALUES
(1, '李总', '13700001111', '字节跳动', '官网在线咨询', '未处理', 2),
(2, '王经理', '13700002222', '美团点评', '行业展会', '转化中', 2);

-- 插入示例商机数据
INSERT INTO `crm_opportunity` (`id`, `customer_id`, `name`, `amount`, `stage`, `owner_id`) VALUES
(1, 1, '云服务扩容采购项目', 500000.00, '签订合同', 2),
(2, 2, 'CRM系统定制开发项目', 200000.00, '方案报价', 2);
