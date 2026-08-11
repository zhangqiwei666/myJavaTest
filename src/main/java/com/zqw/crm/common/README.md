# 📁 `com.zqw.crm.common` 通用组件包说明与语法解析

> 本目录放置系统通用的工具类、统一响应包装对象、自定义异常以及全局异常处理器。

---

## 📄 包含文件列表与作用

| 文件名 | 作用描述 | 核心语法 / 注解 |
| :--- | :--- | :--- |
| [`Result.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/Result.java) | 统一 REST API 响应对象封装 | 泛型 `<T>`, Lombok `@Data`, 静态工厂方法 |
| [`PageResult.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/PageResult.java) | 统一分页数据响应包装 | 泛型 `<T>`, `list`, `total` 结构 |
| [`BizException.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/BizException.java) | 自定义业务运行期异常 | 继承 `RuntimeException`, 自定义 `code` |
| [`GlobalExceptionHandler.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/GlobalExceptionHandler.java) | 全局统一异常捕获与格式化 | `@RestControllerAdvice`, `@ExceptionHandler` |
| [`JwtUtils.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/JwtUtils.java) | JWT 令牌生成、解析与签名校验 | JJWT 框架, `@Value`, HMAC-SHA256 算法 |

---

## 🔍 文件详细语法与代码分析

### 1. [`Result.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/Result.java) - 统一响应对象

#### 💡 语法要点：
- **泛型 `Result<T>`**：支持返回任意类型数据（如 `Result<SysUser>`, `Result<List<CrmCustomer>>`），保持前端接收到的 JSON 格式统一：
  ```json
  {
    "code": 200,
    "msg": "操作成功",
    "data": { ... }
  }
  ```
- **Lombok 注解**：
  - `@Data`：自动为类生成 Getter、Setter、`equals()`、`hashCode()` 和 `toString()` 方法。
  - `@NoArgsConstructor`：生成无参构造函数。
  - `@AllArgsConstructor`：生成全参构造函数。
- **静态工厂方法 `Result.success(data)`**：方便在 Controller 中直接写 `return Result.success(user)`，提升代码简洁性。

---

### 2. [`GlobalExceptionHandler.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/GlobalExceptionHandler.java) - 全局异常捕获器

#### 💡 核心注解与语法解析：
- **`@RestControllerAdvice`**：
  - 切面编程 (AOP) 注解。它拦截所有 `@RestController` 控制器抛出的异常，并将返回结果自动序列化为 JSON 格式。
- **`@ExceptionHandler(BizException.class)`**：
  - 指定捕获特定类型的异常。例如捕获自定义 `BizException` 时，返回友好的错误 JSON 格式，而不是向前端抛出 500 堆栈信息。
- **捕获 Security 异常**：
  - 捕获 `AccessDeniedException` 返回 `403 权限不足`。
  - 捕获 `AuthenticationException` 返回 `401 登录失效`。

---

### 3. [`JwtUtils.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/common/JwtUtils.java) - JWT 令牌工具

#### 💡 核心语法解析：
- **`@Component`**：把 `JwtUtils` 注册为 Spring 管理的 Bean 实例。
- **`@Value("${crm.jwt.secret}")`**：注入 `application.yml` 中配置的 JWT 密钥和过期时间。
- **`Jwts.builder()`**：
  - `setSubject(username)`：设置 Token 所属用户名。
  - `setIssuedAt(now)`：设置签发时间。
  - `setExpiration(expiryDate)`：设置到期时间。
  - `signWith(getSigningKey(), SignatureAlgorithm.HS256)`：使用 HMAC-SHA256 密匙对令牌进行数字签名，防篡改。
