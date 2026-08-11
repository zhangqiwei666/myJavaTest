# 📁 `com.zqw.crm` 包说明与代码语法解析

> 本目录为 CRM 系统 Java 后端代码的核心根包（Root Package）。包含主启动类以及各个业务子包。

---

## 📄 包含文件列表与作用

| 文件名 | 作用描述 | 核心语法 / 注解 |
| :--- | :--- | :--- |
| [`CrmApplication.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/CrmApplication.java) | Spring Boot 项目启动类（主入口） | `@SpringBootApplication`, `@MapperScan`, `SpringApplication.run()` |

---

## 🔍 文件详细语法与代码分析

### 1. [`CrmApplication.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/CrmApplication.java)

#### 源代码片段及讲解：
```java
package com.zqw.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zqw.crm.mapper")
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
        System.out.println("CRM Backend Application Started Successfully!");
    }
}
```

#### 💡 核心注解与语法解析：
1. **`@SpringBootApplication`**：
   - 这是 Spring Boot 的核心复合注解。它包含了：
     - `@SpringBootConfiguration`：声明这是一个 Spring 配置类。
     - `@EnableAutoConfiguration`：开启 Spring Boot 的自动配置机制（例如自动配置 Web 服务器、数据库连接池等）。
     - `@ComponentScan`：自动扫描当前包（`com.zqw.crm`）及其子包下的所有 `@Component`、`@Service`、`@RestController` 组件类。
2. **`@MapperScan("com.zqw.crm.mapper")`**：
   - MyBatis / MyBatis-Plus 的包扫描注解。
   - 作用：自动扫描指定包路径下的 DAO/Mapper 接口类，并为其动态生成代理实现对象注册到 Spring IOC 容器中，免去在每个 Mapper 接口上手动加 `@Mapper` 的麻烦。
3. **`SpringApplication.run(CrmApplication.class, args)`**：
   - 引导 Spring Boot 应用启动的入口代码。内部会自动启动内嵌的 Tomcat Web 服务器（默认 8080 端口），并初始化 Spring 上下文容器。
