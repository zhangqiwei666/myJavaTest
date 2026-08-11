# 📁 `src/main/resources` 资源配置包说明与 YAML 语法解析

> 本目录放置 Spring Boot 项目的全局配置文件、静态资源及 SQL/MyBatis XML 映射文件。

---

## 📄 包含文件列表

| 文件名 | 格式 | 说明 |
| :--- | :--- | :--- |
| [`application.yml`](file:///d:/javaProject/javaTest/src/main/resources/application.yml) | YAML | Spring Boot 应用核心配置文件（端口、数据库连接、MyBatis-Plus、JWT、Knife4j） |

---

## 🔍 YAML 配置语法与节点详解

```yaml
server:
  port: 8080                               # 1. 设置 Web 服务运行端口

spring:
  application:
    name: crm-backend                     # 2. 设置应用服务名称

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/crm_db?useSSL=false&serverTimezone=Asia/Shanghai  # 3. 数据库连接 URL
    username: root                         # 数据库账号
    password: rootpassword                 # 数据库密码

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 4. 控制台打印控制台 SQL
    map-underscore-to-camel-case: true     # 自动将下划线 (user_name) 转为驼峰 (userName)

crm:
  jwt:
    secret: CrmBackendSecretKeyForJwtSignatureAlgorithmStandard2026123456789 # 5. 自定义 JWT 密钥
    expiration: 86400000                   # 24小时过期 (单位 ms)

knife4j:
  enable: true                             # 6. 开启 Knife4j 图形化 Swagger 调试界面
```

### 💡 YAML 格式规则：
1. **缩进敏感**：使用空格（Space）缩进，不能混用 Tab 键。子节点比父节点多 2 个空格缩进。
2. **冒号后必须加空格**：键值对 `:` 之后必须跟一个空格（例：`port: 8080`），否则会导致配置解析报错！
