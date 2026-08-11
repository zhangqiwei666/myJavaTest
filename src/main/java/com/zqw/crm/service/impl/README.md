# 📁 `com.zqw.crm.service.impl` 业务逻辑实现类包说明与语法解析

> 本目录为 Service 接口的具体实现类（Implementation Class）。包含具体的业务判定、密码加密、Lambda 多条件构造以及事务控制。

---

## 📄 包含文件列表与作用

| 文件名 | 继承/实现 | 核心业务与语法 |
| :--- | :--- | :--- |
| [`SysUserServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/SysUserServiceImpl.java) | `ServiceImpl<SysUserMapper, SysUser>`, `SysUserService` | 登录校验、密码 BCrypt 加密、生成 JWT、动态 Lambda 条件分页查询 |
| [`SysRoleServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/SysRoleServiceImpl.java) | `ServiceImpl<SysRoleMapper, SysRole>`, `SysRoleService` | 角色与权限关系批量重置绑定（`@Transactional` 事务保障） |
| [`SysPermissionServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/SysPermissionServiceImpl.java) | `ServiceImpl<SysPermissionMapper, SysPermission>`, `SysPermissionService` | 列表与菜单树按 Sort 排序返回 |
| [`CrmCustomerServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/CrmCustomerServiceImpl.java) | `ServiceImpl<CrmCustomerMapper, CrmCustomer>`, `CrmCustomerService` | 客户模糊匹配检索、分配负责人变更、清空负责人放回公海池 |
| [`CrmClueServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/CrmClueServiceImpl.java) | `ServiceImpl<CrmClueMapper, CrmClue>`, `CrmClueService` | **线索转客户事务方法**：创建新客户记录 + 变更线索状态 |
| [`CrmOpportunityServiceImpl.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/service/impl/CrmOpportunityServiceImpl.java) | `ServiceImpl<CrmOpportunityMapper, CrmOpportunity>`, `CrmOpportunityService` | 变更商机推演阶段 |

---

## 🔍 语法与核心组件分析

### 1. `ServiceImpl<M, T>` 继承机制：
```java
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    ...
}
```

#### 💡 语法要点：
- **`@Service`**：Spring 注解，将此类标识为 Spring 管理的 Service 组件 Bean。
- **`ServiceImpl<SysUserMapper, SysUser>`**：
  MyBatis-Plus 提供的基础实现类模板。通过传入 `SysUserMapper` 和 `SysUser`，当前类就自动注入了 `baseMapper`，无需写构造函数注入 Mapper。
- **`@RequiredArgsConstructor`**：
  Lombok 注解。自动为 `final` 修饰的属性（如 `private final PasswordEncoder passwordEncoder;`）生成带参构造函数，实现 **Spring 官方推荐的构造器依赖注入**。

---

### 2. LambdaQueryWrapper 语法：
```java
Page<CrmCustomer> page = new Page<>(req.getPageNum(), req.getPageSize());
LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();

if (StringUtils.hasText(req.getName())) {
    wrapper.like(CrmCustomer::getName, req.getName()).or().like(CrmCustomer::getCompany, req.getName());
}
if (StringUtils.hasText(req.getStatus())) {
    wrapper.eq(CrmCustomer::getStatus, req.getStatus());
}
wrapper.orderByDesc(CrmCustomer::getCreateTime);

Page<CrmCustomer> result = this.page(page, wrapper);
```

#### 💡 语法解析：
- **`LambdaQueryWrapper`**：链式 SQL 条件构造器。
- **`CrmCustomer::getName`（方法引用 Method Reference）**：强类型安全的列名引用。比起写死字符串 `"name"`，方法引用能防止字段拼写错误，若数据库字段改名，编译期就会报错提示。
- **`like` / `eq` / `or`**：分别对应 SQL 的 `LIKE '%xx%'`、`= 'xx'` 和 `OR` 运算符。

---

### 3. 事务控制 `@Transactional`：
```java
@Override
@Transactional(rollbackFor = Exception.class)
public void convertToCustomer(Long clueId, Long currentUserId) {
    // 1. 创建客户记录
    customerService.save(customer);
    // 2. 更新线索状态
    this.updateById(clue);
}
```

#### 💡 语法解析：
- **`@Transactional(rollbackFor = Exception.class)`**：
  开启 Spring 声明式事务。保证此方法内的“保存客户”与“更新线索”处于同一个数据库事务中。如果中途发生任何异常，自动触发 SQL 回滚（Rollback），防止产生半落盘的脏数据。
