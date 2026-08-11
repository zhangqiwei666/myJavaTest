# 📁 `com.zqw.crm.service` 业务逻辑接口包说明与语法解析

> 本目录为业务逻辑层（Service Layer）的 **接口定义** 规范。遵循“面向接口编程（Interface-oriented Programming）”原则，实现接口与具体实现解耦。

---

## 📄 包含文件列表与作用

| 文件名 | 继承基础接口 | 业务功能职责描述 |
| :--- | :--- | :--- |
| [`SysUserService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/SysUserService.java) | `IService<SysUser>` | 用户业务接口：账号登录校验、加密创建用户、分页查询、分配角色 |
| [`SysRoleService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/SysRoleService.java) | `IService<SysRole>` | 角色业务接口：查询角色已绑定的权限菜单 ID 列表、给角色分配权限 |
| [`SysPermissionService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/SysPermissionService.java) | `IService<SysPermission>` | 权限/菜单业务接口：获取全量系统菜单与按钮权限树 |
| [`CrmCustomerService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/CrmCustomerService.java) | `IService<CrmCustomer>` | 客户业务接口：客户高级多条件分页检索、变更客户负责人、移交到公海池 |
| [`CrmClueService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/CrmClueService.java) | `IService<CrmClue>` | 线索业务接口：销售线索一键转化为正式客户档案 |
| [`CrmOpportunityService.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/CrmOpportunityService.java) | `IService<CrmOpportunity>` | 商机业务接口：销售商机阶段状态更新 |

---

## 🔍 语法与设计模式分析

### `IService<T>` 的作用：
```java
package com.zqw.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zqw.crm.entity.CrmCustomer;

public interface CrmCustomerService extends IService<CrmCustomer> {

    void assignOwner(Long customerId, Long ownerId);

    void transferToPublicPool(Long customerId);
}
```

#### 💡 语法要点：
- **继承 `IService<CrmCustomer>`**：
  `IService` 是 MyBatis-Plus 提供的顶级通用 Service 接口。继承它之后，接口默认拥有了强大的批量保存（`saveBatch`）、快捷查询（`getById`、`list`）、删除（`removeById`）等高级业务方法。
- **自定义业务方法定义**：
  在基础 CRUD 之上，定义符合 CRM 业务场景的方法名（例如 `assignOwner` 分配负责人、`transferToPublicPool` 划入公海池），使接口语义非常清晰。
