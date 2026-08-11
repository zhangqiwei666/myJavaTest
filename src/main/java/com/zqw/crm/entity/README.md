# 📁 `com.zqw.crm.entity` 实体类包说明与语法解析

> 本目录包含与数据库 MySQL 表结构一一对应的 **DO（Data Object）实体类**。借助于 MyBatis-Plus 的注解映射，Java 对象与数据库字段实现自动转换。

---

## 📄 包含文件列表与作用

| 文件名 | 对应数据库表 | 作用描述 |
| :--- | :--- | :--- |
| [`SysUser.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/SysUser.java) | `sys_user` | 系统用户实体类（账号、密码、手机号、状态） |
| [`SysRole.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/SysRole.java) | `sys_role` | 系统角色实体类（角色名、角色 Key） |
| [`SysPermission.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/SysPermission.java) | `sys_permission` | 权限/菜单实体类（权限名称、权限 Key、类型、父ID） |
| [`CrmCustomer.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/CrmCustomer.java) | `crm_customer` | CRM 客户档案实体类（客户名、电话、公司、级别、负责人） |
| [`CrmClue.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/CrmClue.java) | `crm_clue` | CRM 销售线索实体类（线索名称、来源、状态） |
| [`CrmOpportunity.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/entity/CrmOpportunity.java) | `crm_opportunity` | CRM 销售商机实体类（关联客户ID、预计金额、销售阶段） |

---

## 🔍 语法与注解详解

### 核心注解示范：
```java
package com.zqw.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer")
public class CrmCustomer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String phone;
    private String level;
    private Long ownerId;
    private LocalDateTime createTime;
}
```

#### 💡 注解解析：
1. **`@TableName("crm_customer")`**：
   - 来自 MyBatis-Plus。告诉框架这个 Java 类对应的数据库表名是 `crm_customer`。如果类名与表名不一致，可通过此注解指定。
2. **`@TableId(type = IdType.AUTO)`**：
   - 标注主键字段 `id`。
   - `type = IdType.AUTO` 表示主键是数据库自增（MySQL `AUTO_INCREMENT`）。其他常见类型还有 `IdType.ASSIGN_ID`（雪花算法生成唯一长整型 ID）。
3. **字段下划线自动转驼峰**：
   - MyBatis-Plus 默认开启驼峰命名转换（Camel Case Mapping）。例如 Java 属性 `ownerId` 会自动映射 MySQL 字段 `owner_id`，`createTime` 映射 `create_time`，无需手动加 `@TableField` 注解。
4. **`LocalDateTime` 时间类型**：
   - JDK 8 推荐的新版日期时间类型，比旧版 `java.util.Date` 更加安全且包含丰富的时间计算 API。
