# 🚀 CRM 后台管理程序 - 标准 Java Spring Boot 后端项目

> 针对 Java 初学者与标准项目流程打造的轻量级、标准化 CRM 销售管理系统后端。

---

## 🌟 项目亮点

1. **零冗余标准架构**：基于 Spring Boot 3 + MyBatis-Plus + Spring Security + JWT 构建。
2. **完整 RBAC 权限体系**：用户-角色-权限菜单映射，支持方法级与按钮级注解鉴权（`@PreAuthorize`）。
3. **标准 CRM 商业闭环**：
   - **客户管理 (CRUD)**：客户创建、分页过滤、数据更新、客户删除、公海池划分、负责人分配。
   - **线索管理 (Clue)**：销售线索录入、线索一键转化为客户。
   - **商机管理 (Opportunity)**：关联客户商机、阶段推进（初步沟通 ➔ 方案报价 ➔ 签订合同 ➔ 赢单）。
4. **图形化 Swagger 接口文档**：集成 Knife4j，开箱即用。

---

## 📂 项目结构说明

```
javaTest
├── pom.xml                               # Maven 依赖配置文件
├── sql
│   └── crm_init.sql                      # 数据库建表与初始 seed 数据
├── CRM_DEVELOPER_GUIDE.md                # 🎓 初学者专属详细教学与流程讲解文档
└── src
    └── main
        ├── java/com/zqw/crm
        │   ├── CrmApplication.java       # Spring Boot 启动主程序
        │   ├── common                    # 统一响应 Result, PageResult, 全局异常, JwtUtils
        │   ├── security                  # SecurityConfig, JwtFilter, UserDetailsService
        │   ├── entity                    # 实体类 (SysUser, SysRole, CrmCustomer...)
        │   ├── vo                        # 请求与响应 VO (LoginReq, LoginResp...)
        │   ├── mapper                    # MyBatis-Plus Mapper 持久层接口
        │   ├── service                   # 业务逻辑 Service 接口及 Impl 实现
        │   └── controller                # RESTful 控制层 API
        └── resources
            └── application.yml           # 数据库配置与系统参数
```

---

## 🛠️ 快速启动

1. **导入 SQL 数据库**：
   在 MySQL (8.0+) 中创建数据库 `crm_db` 并导入 [`sql/crm_init.sql`](file:///d:/javaProject/javaTest/sql/crm_init.sql)。
2. **配置数据库密码**：
   打开 [`src/main/resources/application.yml`](file:///d:/javaProject/javaTest/src/main/resources/application.yml)，修改你的 MySQL `username` 和 `password`。
3. **运行 Backend 项目**：
   运行 `com.zqw.crm.CrmApplication` 的 `main` 方法。
4. **访问 Swagger 调试接口**：
   浏览器访问：`http://localhost:8080/doc.html`
   - 默认管理员账号：`admin`
   - 默认管理员密码：`123456`

详细教学请参阅：👉 [CRM_DEVELOPER_GUIDE.md](file:///d:/javaProject/javaTest/CRM_DEVELOPER_GUIDE.md)
