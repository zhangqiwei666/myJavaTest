# 📁 `com.zqw.crm.security` 安全与鉴权包说明与语法解析

> 本目录实现了系统的**认证（Authentication）**与**授权（Authorization）**控制，整合了 Spring Security 6 与 JWT 无状态 Token。

---

## 📄 包含文件列表与作用

| 文件名 | 作用描述 | 核心语法 / 注解 |
| :--- | :--- | :--- |
| [`SecurityConfig.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/SecurityConfig.java) | Spring Security 主配置类 | `@Configuration`, `@EnableWebSecurity`, `@EnableMethodSecurity`, `SecurityFilterChain` |
| [`JwtAuthenticationFilter.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/JwtAuthenticationFilter.java) | HTTP 请求 JWT Token 拦截校验过滤器 | 继承 `OncePerRequestFilter`, `SecurityContextHolder` |
| [`UserDetailsServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/UserDetailsServiceImpl.java) | 用户详情加载服务 | 实现 `UserDetailsService`, 联查用户角色与权限 |
| [`SecurityUser.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/SecurityUser.java) | 安全框架认知的用户实体对象 | 实现 `UserDetails`, `GrantedAuthority` |

---

## 🔍 文件详细语法与代码分析

### 1. [`SecurityConfig.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/SecurityConfig.java)

#### 💡 核心注解与语法解析：
- **`@EnableWebSecurity`**：开启 Spring Security 网络安全拦截。
- **`@EnableMethodSecurity`**：开启方法级别的权限控制注解（允许使用 `@PreAuthorize("hasAuthority('crm:customer:add')")`）。
- **`SecurityFilterChain` Bean 配置**：
  - `csrf(AbstractHttpConfigurer::disable)`：禁用 CSRF 防御（JWT 无状态模式不需要 Session Cookie CSRF）。
  - `sessionManagement(SessionCreationPolicy.STATELESS)`：禁用默认 Session 机制，构建完全无状态 API 服务。
  - `authorizeHttpRequests(...)`：配置放行白名单（如 `/api/auth/login`、Swagger `/doc.html`）。
  - `addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)`：在 Security 默认登录过滤器之前插入我们的 JWT 校验过滤器。

---

### 2. [`JwtAuthenticationFilter.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/JwtAuthenticationFilter.java)

#### 💡 拦截器流转逻辑与语法：
1. 继承 **`OncePerRequestFilter`**：确保每个 HTTP 请求只被此过滤器拦截过滤 1 次。
2. 提取 Header：读取请求头 `Authorization: Bearer <token>`。
3. 校验 Token：调用 `jwtUtils.validateToken(token)` 校验签名是否合法及过期。
4. 设置上下文：
   ```java
   UsernamePasswordAuthenticationToken authentication =
           new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
   SecurityContextHolder.getContext().setAuthentication(authentication);
   ```
   把验证通过的用户信息存入 Spring 当前线程的 **`SecurityContextHolder`** 中，供后续 Controller 鉴权使用。

---

### 3. [`UserDetailsServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/UserDetailsServiceImpl.java)

#### 💡 语法要点：
- 实现 Spring Security 标准接口 `UserDetailsService` 的 `loadUserByUsername(String username)` 方法。
- 通过 `SysUserMapper` 从 MySQL 中根据账号查询用户实体、赋予的角色标识（如 `ROLE_SALES`）与拥有的权限标识（如 `crm:customer:add`），装配封装为 `SecurityUser`。
