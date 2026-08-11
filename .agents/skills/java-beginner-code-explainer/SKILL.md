---
name: java-beginner-code-explainer
description: "Detailed Chinese explanations, line-by-line code breakdowns, and frontend-to-Java concept mappings targeting Java beginners with a frontend background. Triggers whenever writing, modifying, or explaining Java code, Spring Boot backend components, or Maven project configurations."
---

# Java 初学者与前端背景详细代码讲解规范 (Java Beginner Code Explainer)

本 Skill 用于在生成、修改或重构 Java 代码、Spring Boot 架构或 Maven 配置时，主动为用户提供最详细的中文代码解析。

## 🎯 核心指导原则

### 1. 前后端概念对比映射 (Frontend to Java Concept Mapping)
用户具备前端开发基础，在讲解 Java 概念时，积极联系前端已知技术栈做直观类比：
- **`Spring Boot @RestController`**  ➜ 类比 `Express.js / Fastify` 中的路由 Controller 函数。
- **`DTO / VO / Entity`** ➜ 类比 TypeScript 中的 `interface` / `type` 类型定义。
- **`MyBatis-Plus`** ➜ 类比前端熟悉的 ORM 框架（如 Prisma / TypeORM / Mongoose）。
- **`Maven / pom.xml`** ➜ 类比前端包管理器 `npm / pnpm / package.json`。
- **`@Valid / Jakarta Validation`** ➜ 类比前端数据校验库 `Zod / Yup`。
- **`Result<T> 统一响应类`** ➜ 类比前端标准的 `{ code: 200, message: "ok", data: T }` 响应结构。

### 2. 逐行与逐段代码详解 (Line-by-Line Code Breakdown)
每次生成或修改 Java 代码时，必须附带结构清晰的中文说明表或分类清单，包含：
- **代码语法/注解名称**（如 `@PostMapping`, `public final` 等）；
- **具体功能与运行机制**；
- **为什么要这样写**（背后的设计意图）。

### 3. 避坑提示与 Java 基础拓展 (Beginner Tips & Gotchas)
- 强调 Java 作为强类型语言与 JS/TS 的异同（如 `Long` 与 `Integer` 类型转换、空指针 `NullPointerException` 防范）。
- 提示常见的脚手架工具坑点（如 Lombok 注解冲突、Spring 依赖注入规则等）。
