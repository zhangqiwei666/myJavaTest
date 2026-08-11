# 📁 `com.zqw.crm.mapper` 持久层（DAO / Mapper）包说明与语法解析

> 本目录为 MyBatis-Plus 的 Mapper 接口定义层。通过继承 `BaseMapper<T>` 自动获得对数据库表的大量通用 CRUD 操作方法，无需编写繁琐的 XML 文件。

---

## 📄 包含文件列表与作用

| 文件名 | 对应实体 | 作用描述与扩展 SQL |
| :--- | :--- | :--- |
| [`SysUserMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/SysUserMapper.java) | `SysUser` | 用户 Mapper，扩展了多表联查用户角色 `@Select` 与清空/插入用户角色关系 |
| [`SysRoleMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/SysRoleMapper.java) | `SysRole` | 角色 Mapper，扩展了查询角色权限 ID 列表以及角色权限多对多关联操作 |
| [`SysPermissionMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/SysPermissionMapper.java) | `SysPermission` | 权限/菜单 Mapper，继承 `BaseMapper` 提供基础查询 |
| [`CrmCustomerMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/CrmCustomerMapper.java) | `CrmCustomer` | 客户 Mapper，继承 `BaseMapper` 提供客户数据查询与持久化 |
| [`CrmClueMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/CrmClueMapper.java) | `CrmClue` | 线索 Mapper，继承 `BaseMapper` |
| [`CrmOpportunityMapper.java`](file:///d:/javaProject/javaTest/src/main/java/com/zqw/crm/mapper/CrmOpportunityMapper.java) | `CrmOpportunity` | 商机 Mapper，继承 `BaseMapper` |

---

## 🔍 语法与设计模式分析

### 1. `BaseMapper<T>` 继承机制：
```java
public interface CrmCustomerMapper extends BaseMapper<CrmCustomer> {
}
```
只需要让接口继承 `BaseMapper<CrmCustomer>`，我们就免费拥有了以下常用方法：
- `selectById(id)`：根据主键查询。
- `insert(entity)`：插入一条记录。
- `updateById(entity)`：根据主键更新。
- `deleteById(id)`：根据主键删除。
- `selectPage(page, wrapper)`：条件分页查询。

### 2. 在接口上直接使用 `@Select` 注解实现多表联查 SQL：
```java
package com.zqw.crm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zqw.crm.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT r.role_key FROM sys_role r " +
            "JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);
}
```

#### 💡 语法要点：
- **`@Select("SQL语句")`**：注解内直接手写原生 SQL。适用于轻量级的多表 JOIN 联查。
- **`#{userId}`**：MyBatis 的预编译参数占位符（相当于 PreparedStatement 的 `?`），可以有效防止 SQL 注入风险。
- **`@Param("userId")`**：将 Java 方法参数显式绑定映射到 SQL 占位符 `#{userId}` 中。
