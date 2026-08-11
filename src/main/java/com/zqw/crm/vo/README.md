# 📁 `com.zqw.crm.vo` 值对象（View Object / Value Object）包说明与语法解析

> 本目录包含专门用于前端**接口传输的请求参数对象 (Req VO)** 与 **接口响应返回对象 (Resp VO)**。将内部数据库 Entity 与外部接口解耦。

---

## 📄 包含文件列表与作用

| 文件名 | 作用描述 | 核心语法 / 注解 |
| :--- | :--- | :--- |
| [`LoginReq.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/vo/LoginReq.java) | 用户登录前端请求参数 VO | `@NotBlank`, JSR-303 参数校验 |
| [`LoginResp.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/vo/LoginResp.java) | 用户登录成功前端响应 VO | `token`, `username`, `roles`, `permissions` |
| [`AssignRolesReq.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/vo/AssignRolesReq.java) | 给用户分配角色请求参数 VO | `@NotNull`, `userId`, `roleIds` |
| [`AssignPermissionsReq.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/vo/AssignPermissionsReq.java) | 给角色分配权限请求参数 VO | `@NotNull`, `roleId`, `permissionIds` |
| [`CustomerQueryReq.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/vo/CustomerQueryReq.java) | 客户分页查询条件过滤参数 VO | `pageNum`, `pageSize`, `name`, `status` |

---

## 🔍 语法与设计模式分析

### 为什么不直接使用 Entity，而是使用 VO？
1. **安全性**：Entity 包含了数据库敏感字段（如 `SysUser` 中的加密密码 `password`）。若直接返回 Entity，容易把密码暴露给前端。通过 `LoginResp` 仅返回必要信息。
2. **校验隔离**：前端请求参数可能需要特殊的输入格式验证，通过 JSR-303 参数校验注解可以直接在 VO 属性上做检查。

### 核心校验注解示范：
```java
package com.zqw.crm.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
```

#### 💡 校验注解解析：
- **`@NotBlank`**：校验字符串属性既不能为 `null`，也不能为空串 `""` 或纯空格字符串 `"   "`。
- **`@NotNull`**：校验对象/ID 类型属性不能为 `null`。
- **在 Controller 中配合 `@Valid` 开启验证**：
  ```java
  @PostMapping("/login")
  public Result<LoginResp> login(@Valid @RequestBody LoginReq loginReq) { ... }
  ```
  如果前端传入空参数，Spring Validation 会拦截并抛出 `MethodArgumentNotValidException`，被我们的 `GlobalExceptionHandler` 捕获并返回友好的错误信息。
