# 📁 `sql` 数据库脚本包说明

> 本目录放置 CRM 系统的数据库初始化建表与 Seed 测试数据 SQL 脚本。

---

## 📄 包含文件列表

| 文件名 | 适用数据库 | 说明 |
| :--- | :--- | :--- |
| [`crm_init.sql`](file:///d:/javaProject/javaTest/sql/crm_init.sql) | MySQL 8.0+ / 5.7+ | 数据库创建、建表语句、主外键索引、初始角色权限数据及示例客户数据 |

---

## 🔍 表结构与关系图解

SQL 包含 8 张核心数据表：

```
[系统权限 RBAC 模块]
sys_user (用户表) ◄─── sys_user_role ───► sys_role (角色表) ◄─── sys_role_permission ───► sys_permission (菜单权限表)

[CRM 业务流转模块]
crm_clue (销售线索表) ───(转化)───► crm_customer (客户档案表) ──────► crm_opportunity (销售商机表)
```

---

## 💡 SQL 核心语法要点解析

```sql
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(100) NOT NULL COMMENT '加密密码',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
```

1. **`AUTO_INCREMENT`**：设置主键自增策略。
2. **`DATETIME DEFAULT CURRENT_TIMESTAMP`**：数据库自动记录数据创建时间。
3. **`ON UPDATE CURRENT_TIMESTAMP`**：当记录发生 UPDATE 修改时，数据库自动把 `update_time` 更新为当前最新时间。
4. **`UNIQUE KEY uk_username (username)`**：唯一索引，防止创建同名的重复登录账号。
