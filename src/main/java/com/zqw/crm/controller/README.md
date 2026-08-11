# 📁 `com.zqw.crm.controller` REST 控制层（Controller）包说明与语法解析

> 本目录为对外暴露 HTTP 接口的控制器层（Controller）。负责接收前端 RESTful 请求、参数校验、拦截鉴权并返回统一 JSON 结果。

---

## 📄 包含文件列表与作用

| 文件名 | REST 映射基路径 | 作用描述 | 核心权限标识 |
| :--- | :--- | :--- | :--- |
| [`AuthController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/AuthController.java) | `/api/auth` | 认证接口：登录、获取当前登录用户信息、退出登录 | 放行/需登录 |
| [`UserController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/UserController.java) | `/api/system/user` | 用户管理接口：用户 CRUD、分配角色 | `sys:user:*` |
| [`RoleController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/RoleController.java) | `/api/system/role` | 角色管理接口：角色 CRUD、分配权限 | `sys:role:*` |
| [`PermissionController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/PermissionController.java) | `/api/system/permission` | 权限菜单树接口：全量菜单/按钮权限查询 | `sys:role:query` |
| [`CustomerController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/CustomerController.java) | `/api/crm/customer` | **客户管理接口 (CRUD)**：客户创建、分页、修改、删除、分配负责人、划入公海池 | `crm:customer:*` |
| [`ClueController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/ClueController.java) | `/api/crm/clue` | 线索管理接口：线索录入与线索转化客户 | `crm:clue:*` |
| [`OpportunityController.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/OpportunityController.java) | `/api/crm/opportunity` | 商机管理接口：商机录入与商机阶段推进 | `crm:opportunity:*` |

---

## 🔍 语法与核心注解详解

### 代码标准模版示例：
```java
package com.zqw.crm.controller;

import com.zqw.crm.common.PageResult;
import com.zqw.crm.common.Result;
import com.zqw.crm.entity.CrmCustomer;
import com.zqw.crm.security.SecurityUser;
import com.zqw.crm.service.CrmCustomerService;
import com.zqw.crm.vo.CustomerQueryReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. 客户管理接口 (CRUD)")
@RestController
@RequestMapping("/api/crm/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CrmCustomerService customerService;

    @Operation(summary = "新增客户档案")
    @PostMapping
    @PreAuthorize("hasAuthority('crm:customer:add')")
    public Result<Boolean> createCustomer(@RequestBody CrmCustomer customer,
                                         @AuthenticationPrincipal SecurityUser loginUser) {
        if (loginUser != null && loginUser.getSysUser() != null) {
            customer.setCreatorId(loginUser.getSysUser().getId());
        }
        customerService.save(customer);
        return Result.success(true);
    }
}
```

#### 💡 核心注解深度解析：

1. **`@RestController`**：
   - 组合注解，等价于 `@Controller` + `@ResponseBody`。表明该控制器方法的所有返回值都直接转换为 JSON 格式输出给客户端，而不是寻找 HTML 视图页面。
2. **`@RequestMapping("/api/crm/customer")`**：
   - 映射 HTTP 请求的基本路径 URL 前缀。
3. **`@Tag` 与 `@Operation`**：
   - 来自 OpenAPI 3 / Swagger / Knife4j 注解。
   - `@Tag` 用于设置接口分类组名（在在线文档侧边栏展示）。
   - `@Operation(summary = "新增客户档案")` 用于描述具体每个 API 方法的作用。
4. **HTTP 请求动作映射注解**：
   - **`@GetMapping`**：查询数据 (SELECT)。
   - **`@PostMapping`**：提交/新增数据 (INSERT)。
   - **`@PutMapping`**：全量更新数据 (UPDATE)。
   - **`@DeleteMapping`**：删除数据 (DELETE)。
5. **权限拦截注解 `@PreAuthorize("hasAuthority('crm:customer:add')")`**：
   - Spring Security 的方法级安全注解。
   - 解释：在执行此 API 前，Security 会检查当前登录用户的权限集合中是否包含字符串 `'crm:customer:add'`。如果不包含，框架会自动拦截并拒绝执行，抛出 403 异常。
6. **`@AuthenticationPrincipal SecurityUser loginUser`**：
   - 自动获取当前发送请求的已登录用户身份对象（从 Spring `SecurityContext` 中自动注入），方便直接读取当前登录人的 `id`、`username` 等。
