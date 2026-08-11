# Java 后端进阶答疑与 Spring 运行机制详解

**日期**：2026-08-12  
**项目名称**：CRM 后端管理系统 (`crm-backend`)  
**目标读者**：具备前端开发背景的 Java 初学者  
**相关核心代码文件**：
- [AuthController.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/controller/AuthController.java)
- [SecurityConfig.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/SecurityConfig.java)
- [JwtAuthenticationFilter.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/JwtAuthenticationFilter.java)
- [CrmApplication.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/CrmApplication.java)

---

## 目录

1. [他人如何调用我本地运行的 API 接口](#1-他人如何调用我本地运行的-api-接口)
2. [编译器中项目的启动与关闭指南](#2-编译器中项目的启动与关闭指南)
3. [前后端“接口先行 (API-First)”开发范式](#3-前后端接口先行-api-first开发范式)
4. [Controller 入参 SecurityUser 的注入原理](#4-controller-入参-securityuser-的注入原理)
5. [Spring Boot 接口路径拼接与路由分发原理](#5-spring-boot-接口路径拼接与路由分发原理)
6. [“注释 (Comment)” 🆚 “注解 (Annotation)” 区别澄清](#6-注释-comment--注解-annotation-区别澄清)

---

## 1. 他人如何调用我本地运行的 API 接口

当你的项目成功运行在 `8080` 端口时，其他开发人员调用你接口的方式分为 3 种常见场景：

### 场景 A：同一局域网（同 Wi-Fi / 同网络，最常用）
1. **获取本机局域网 IP**：在终端运行 `ipconfig`，找到 `IPv4 地址`（如 `192.168.1.108`）。
2. **替换 URL 地址**：
   - 对方在浏览器打开在线文档：`http://192.168.1.108:8080/doc.html`
   - 前端代码 `axios` 配置：`baseURL: 'http://192.168.1.108:8080'`
3. **⚠️ Windows 防火墙放行**：若对方连不上，在 Windows 防火墙【入站规则】中新建规则，放行 `8080` 端口。

### 场景 B：跨域问题处理 (CORS)
前端项目（如 Vue/Vite 开发服务器 `localhost:5173`）调用 `8080` 会触发浏览器跨域拦截。  
已在 [SecurityConfig.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/SecurityConfig.java) 中完成了全量跨域配置（`CorsConfigurationSource`），前端直接调用不会报错。

### 场景 C：异地公网访问
使用内网穿透工具（如 `cpolar` / `ngrok`），运行 `cpolar http 8080` 映射生成公网 HTTPS 域名即可供异地人员访问。

---

## 2. 编译器中项目的启动与关闭指南

### 启动项目（3 种方式）
1. **▶️ 按钮启动（最推荐）**：打开 [CrmApplication.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/CrmApplication.java)，点击 `main` 方法上方的绿色 **`Run`** 图标。
2. **右键启动**：在资源管理器中右键 `CrmApplication.java` ➔ 点击 **`Run Java`**。
3. **终端启动**：在项目根路径终端输入 `mvn spring-boot:run`（类比前端 `npm run dev`）。

### 关闭项目（3 种方式）
1. **红色停止按钮 🔴**：点击 IDE 下方控制台（Console/Terminal）右上角的红色正方形 **Stop / Terminate** 按钮。
2. **快捷键中断**：在命令行窗口中按下 **`Ctrl + C`**。
3. **端口卡住处理**：若提示 `Port 8080 was already in use`，运行命令强行杀掉进程：
   ```cmd
   netstat -ano | findstr 8080
   taskkill /F /PID <进程号>
   ```

---

## 3. 前后端“接口先行 (API-First)”开发范式

在实际大厂开发中，**接口先行（Contract-First）** 是前后端并行开发的标准范式：

```
1. 后端定义 Controller 路由 + VO 参数结构 + Swagger 注解（无需写数据库逻辑）
   │
   ▼
2. 启动项目 ➔ 自动生成 Knife4j 在线文档 (http://localhost:8080/doc.html)
   │
   ├───────────────────────────────┐ (前后端同步并行)
   ▼                               ▼
前端：读取文档 / 接入假数据 (Mock)    后端：补全 Service/Mapper 真实逻辑与数据库 CRUD
提前开发界面与组件联调               逻辑编写与事务控制
   │                               │
   └───────────────┬───────────────┘
                   ▼
3. 前后端对接真实数据，几乎同时完成开发上线！
```

---

## 4. Controller 入参 SecurityUser 的注入原理

看 `AuthController.java` 中的代码：
```java
@GetMapping("/info")
public Result<SecurityUser> getInfo(@AuthenticationPrincipal SecurityUser securityUser) {
    return Result.success(securityUser);
}
```

### 为什么无需实例化即可直接使用 `securityUser`？
类比 Express 中间件：
```javascript
// Express 鉴权中间件把解析好的用户挂在 req 上
app.use((req, res, next) => { req.user = currentUser; next(); });
app.get('/info', (req, res) => res.json(req.user)); // 直接使用 req.user
```

**Java 运行全过程**：
1. **过滤器拦截**：请求进入时，[JwtAuthenticationFilter.java](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/security/JwtAuthenticationFilter.java) 读取 Token，从数据库查出对象并存入 `SecurityContextHolder` 内存池。
2. **注解魔法注入**：`@AuthenticationPrincipal` 告诉 Spring 从全局上下文取出对象，直接赋值给参数 `securityUser`。

---

## 5. Spring Boot 接口路径拼接与路由分发原理

### 拼接公式
$$\text{完整 API 路径} = \text{类级别的 @RequestMapping("公共前缀")} + \text{方法级别的 @GetMapping/@PostMapping("子路径")}$$

以 `AuthController.java` 为例：
- 类上：`@RequestMapping("/api/auth")`
- 方法上：`@PostMapping("/login")`
- **最终完整路径**：`POST /api/auth/login`

### 底层原理：HandlerMapping 路由字典
项目启动时，Spring Boot 的 `RequestMappingHandlerMapping` 扫描所有 `@RestController`，在内存中维护一张【URL ➔ 方法】的 Map 哈希表。请求到达时由 `DispatcherServlet` 查表分发并执行对应方法。

---

## 6. “注释 (Comment)” 🆚 “注解 (Annotation)” 区别澄清

| 概念 | 语法标识 | 是否会被程序执行？ | 核心作用与定位 |
|---|---|---|---|
| **注释 (Comment)** | `//` 或 `/* ... */` | ❌ **绝对不执行** | **给人类程序员看**的文字备注，编译时被直接擦除。 |
| **注解 (Annotation)** | `@名称`<br>(如 `@RestController`) | ✅ **必定会被框架解析并执行** | **给 Spring Boot 框架看**的控制配置与指令！ |

### 类比：
- `@RestController` 类似于 CSS 里的 `@media` 指令，或 NestJS/TypeScript 里的装饰器（`@Controller`）。
- 启动时 Spring 的扫描器看到注解，便会**强制执行对应的路由注册与 JSON 转换逻辑**。
