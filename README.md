# Lease

## 项目简介

`lease` 是一个面向长租公寓场景的后端项目，覆盖公寓、房间、租约、看房预约、用户和系统管理等业务域。

当前仓库整体处于骨架阶段：
- 多模块结构已完成
- 实体与枚举已定义
- Controller 路由已定义
- Mapper 接口与 XML 已生成
- API 文档分组已配置

大部分业务逻辑仍待实现。

## 技术栈

- Java 17
- Spring Boot 3.0.5
- Spring Web
- MyBatis-Plus 3.5.3.1
- MySQL + HikariCP
- OpenAPI 3 + Knife4j
- Lombok

## 模块结构

```text
lease
|-- common                # 公共模块（统一返回结构 + MyBatis-Plus 配置）
|-- model                 # 领域模型（entity + enum）
`-- web                   # Web 聚合模块
    |-- web-admin         # 后台管理端（当前可运行入口）
    `-- web-app           # 面向客户端的预留模块（当前基本为空）
```

## 主要接口域（已定义路由）

- 登录与用户信息：`/admin/login/**`、`/admin/info`
- 系统管理：`/admin/system/**`
- 公寓管理：`/admin/apartment/**`
- 房间管理：`/admin/room/**`
- 基础字典：`/admin/label/**`、`/admin/facility/**`、`/admin/fee/**`、`/admin/attr/**`、`/admin/payment/**`、`/admin/region/**`、`/admin/term/**`
- 租赁管理：`/admin/agreement/**`、`/admin/appointment/**`
- 平台用户管理：`/admin/user/**`
- 文件上传：`/admin/file/**`

## 当前实现状态

- `web-admin` 中大多数 Controller 仍返回占位结果（`Result.ok()`）。
- `service` 层目前主要是接口定义，尚未提供 `ServiceImpl` 实现。
- 多数 Mapper XML 还是空模板。
- 仓库内暂无数据库初始化 SQL 或迁移脚本。
- 测试代码目前较少或为空。

## 环境要求

- JDK 17
- Maven 3.8+
- MySQL 8.x（或兼容版本）

## 快速开始

1. 配置数据源  
   修改 `web/web-admin/src/main/resources/application.yml` 中：
   - `spring.datasource.url`
   - `spring.datasource.username`
   - `spring.datasource.password`

2. 构建项目

```bash
mvn clean package -DskipTests
```

3. 启动后台服务

```bash
mvn -pl web/web-admin -am spring-boot:run
```

4. 访问地址

- 服务地址：`http://localhost:8080`
- Knife4j 文档：`http://localhost:8080/doc.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`

## 统一返回结构

`common` 模块提供 `Result<T>` 作为统一响应封装：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

## 后续建议

1. 补全 `service` 与 `mapper` 层业务逻辑。
2. 增加数据库建表脚本或引入迁移工具（Flyway/Liquibase）。
3. 增加认证鉴权与全局异常处理。
4. 增加集成测试与接口测试。
