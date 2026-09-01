# BinliCoder Spring Boot Template

一套可直接创建新项目的 Java 21 / Spring Boot 3.5 后端脚手架，默认根包为 `com.binlicoder`。

项目只提供新应用需要的通用基础能力，不绑定任何具体业务、平台账号或生产密钥。

## 核心能力

- PostgreSQL、HikariCP、MyBatis-Plus、Flyway
- Redis 与 Caffeine 本地缓存
- Spring Security、JWT、角色权限、API Key
- 统一响应、错误码、异常处理、参数校验
- 分页、审计字段、防全表更新和删除
- 异步任务、定时任务和优雅停机
- OpenAPI、Swagger UI、Actuator
- dev/prod 多环境配置
- Docker Compose 开发环境和多阶段 Docker 镜像
- GitHub Actions 持续集成
- 模块化 CRUD 示例和单元测试

## 项目结构

```text
src/main/java/com/binlicoder
├── common
│   ├── api                 # 统一响应和分页
│   ├── error               # 错误码、异常处理
│   └── persistence         # 审计字段
├── config                  # 框架配置
├── security                # JWT、API Key、权限配置
├── controller              # HTTP 接口层
├── service                 # 业务接口
│   └── impl                # 业务实现和事务边界
├── mapper                  # MyBatis 数据访问接口
├── dto                     # 请求和服务入参模型
├── vo                      # 接口响应视图模型
└── entity                  # 数据库实体
```

## 环境要求

| 软件 | 建议版本 | 用途 |
|---|---:|---|
| JDK | 21 | 编译和运行 |
| Maven | 3.9+ | 构建和依赖管理 |
| Docker Desktop | 稳定版 | 启动 PostgreSQL 和 Redis |
| Git | 2.x | 版本管理 |

检查环境：

```bash
java -version
mvn -version
docker --version
git --version
```

## 五分钟快速启动

### 使用 IntelliJ IDEA 打开

推荐直接使用 IDEA 打开仓库根目录的 `pom.xml`，选择 **Open as Project**，然后执行 **Reload All Maven Projects**。不要只把 `src/main/java` 标记为普通目录，否则 IDE 无法正确关联 Spring、Jakarta 和 MyBatis 依赖。

### 1. 进入项目

如果 GitHub 仓库根目录就是本脚手架：

```bash
git clone https://github.com/你的账号/你的仓库.git
cd 你的仓库
```

如果保留当前父项目结构，则继续执行 `cd spring`。

### 2. 启动基础设施

```bash
docker compose up -d
docker compose ps
```

本地默认配置：

- PostgreSQL：`localhost:5432`
- 数据库：`binlicoder_template`
- 用户名和密码：`postgres / postgres`
- Redis：`localhost:6379`

这些默认值仅用于本地开发。

PostgreSQL 和 Redis 默认只绑定 `127.0.0.1`，不会直接暴露到局域网或公网。可先复制配置模板再按需调整端口和本地密码：

```bash
cp .env.example .env
```

### 3. 配置必要密钥

```bash
export JWT_SECRET='替换为至少32位的高强度随机字符串'
export EXTERNAL_API_KEY='替换为随机生成的API-Key'
```

生成随机值：

```bash
openssl rand -base64 48
```

完整配置模板见 [.env.example](./.env.example)。Spring Boot 不会自动加载 `.env`，请在 IDE、Shell、Docker 或部署平台配置环境变量。

### 4. 测试并启动

```bash
mvn clean test
mvn spring-boot:run
```

启动后访问：

| 地址 | 说明 |
|---|---|
| `http://localhost:8080/actuator/health` | 健康检查 |
| `http://localhost:8080/api/public/ping` | 匿名接口示例 |
| `http://localhost:8080/swagger-ui.html` | Swagger 文档 |
| `http://localhost:8080/v3/api-docs` | OpenAPI JSON |

## 配置说明

`application.yml` 只保存应用名称、激活环境和服务端口。开发环境配置位于 `application-dev.yml`，生产环境配置位于 `application-prod.yml`；敏感值统一通过环境变量注入。

| 环境变量 | 是否必须 | 默认值 | 说明 |
|---|---|---|---|
| `SPRING_PROFILES_ACTIVE` | 否 | `dev` | 生产环境使用 `prod` |
| `SERVER_PORT` | 否 | `8080` | 服务端口 |
| `POSTGRES_DB` | 否 | `binlicoder_template` | Docker Compose 创建的数据库名 |
| `POSTGRES_PORT` | 否 | `5432` | Docker Compose 映射到本机的 PostgreSQL 端口 |
| `DB_URL` | 生产必须 | 本地 PostgreSQL | JDBC 地址 |
| `DB_USERNAME` | 生产必须 | `postgres` | 数据库账号 |
| `DB_PASSWORD` | 生产必须 | `postgres` | 数据库密码 |
| `DB_POOL_MAX_SIZE` | 否 | dev `20`、prod `30` | 数据库连接池上限 |
| `DB_POOL_MIN_IDLE` | 否 | dev `5`、prod `10` | 数据库连接池最小空闲连接数 |
| `REDIS_HOST` | 生产必须 | `localhost` | Redis 地址 |
| `REDIS_PORT` | 否 | `6379` | Redis 端口 |
| `REDIS_PASSWORD` | 视环境而定 | 空 | Redis 密码 |
| `JWT_ISSUER` | 否 | `binlicoder-springboot-template` | JWT 签发方，多个系统间应保持唯一 |
| `JWT_SECRET` | 是 | 开发占位值 | JWT 签名密钥，至少 32 位 |
| `JWT_ACCESS_TOKEN_TTL` | 否 | `2h` | Access Token 有效期 |
| `EXTERNAL_API_KEY` | 是 | 开发占位值 | 外部接口访问密钥 |
| `CACHE_MAXIMUM_SIZE` | 否 | `10000` | 最大本地缓存条数 |
| `CACHE_TTL` | 否 | `30m` | 本地缓存有效期 |
| `LOG_PATH` | 否 | dev 为 `./logs` | 日志根目录，prod 默认 `/var/log/binlicoder` |
| `LOG_MAX_FILE_SIZE` | 否 | `100MB` | 单个归档日志最大大小 |
| `LOG_MAX_HISTORY` | 否 | dev 14 天、prod 30 天 | 日志保留天数 |
| `LOG_TOTAL_SIZE_CAP` | 否 | dev `2GB`、prod `10GB` | 所有归档日志容量上限 |

生产环境不得使用默认密码和占位密钥，也不要提交真实 `.env`、`application-local.yml`、证书或云平台密钥。

## 日志目录

日志由 `logback-spring.xml` 统一管理，开发和生产环境都会输出控制台日志并异步写入文件：

```text
${LOG_PATH}
├── binlicoder-springboot-template.log                         # 当前 INFO 及以上应用日志
├── history
│   └── binlicoder-springboot-template-2026-09-01.0.log.gz     # 按日期和大小压缩归档
└── error
    ├── binlicoder-springboot-template-error.log                # 当前 ERROR 日志
    └── history
        └── binlicoder-springboot-template-error-2026-09-01.0.log.gz
```

日志同时按照日期和文件大小滚动，超过保留天数或容量上限后自动清理。应用归档日志和 ERROR 归档日志分别应用 `LOG_TOTAL_SIZE_CAP`，因此两类归档合计容量最多约为该配置的两倍。ERROR 会同时出现在应用日志和独立错误日志中。开发环境默认保存到项目的 `logs/`，该目录已经加入 `.gitignore`；生产环境应确保运行用户对 `LOG_PATH` 有写权限，并将日志目录挂载到持久化磁盘。

业务代码统一使用 SLF4J：

```java
private static final Logger log = LoggerFactory.getLogger(YourService.class);

log.info("Order created: orderId={}", orderId);
log.error("Order creation failed: orderId={}", orderId, exception);
```

禁止使用 `System.out.println`，禁止记录密码、JWT、API Key、验证码和完整个人敏感信息。

## 鉴权方式

- `/api/public/**`：匿名访问
- `/api/external/**`：需要 `X-API-Key`
- `/api/admin/**`：JWT 需要 `ADMIN` 角色
- `/api/**`：默认需要登录
- 未明确开放的路径：默认拒绝

JWT 请求：

```http
Authorization: Bearer eyJ...
```

外部 API 请求：

```http
X-API-Key: your-api-key
```

脚手架提供 `JwtTokenService`，但没有虚构登录业务。新项目应自行实现密码校验、登录、刷新 Token、退出和 Token 撤销策略。

## 创建业务功能

新增业务时，将对应类放入 `com.binlicoder` 下的标准分层目录：

```text
com.binlicoder
├── controller/UserController.java
├── service/UserService.java
├── service/impl/UserServiceImpl.java
├── mapper/UserMapper.java
├── dto/UserSaveDTO.java
├── vo/UserVO.java
└── entity/UserEntity.java
```

规范要求：

- Controller 只接收 DTO、执行参数校验、调用 Service 并返回 VO。
- Service 接口声明业务能力，`service.impl` 负责业务逻辑、模型转换和事务边界；Controller 只依赖接口。
- Mapper 只负责数据访问，输入输出以 Entity 为主，复杂 SQL 放在对应 Mapper XML 中。
- DTO 用于接收请求或封装服务入参，不直接映射数据库表。
- VO 用于接口响应，不把 Entity 直接暴露给调用方。
- Entity 与数据库表对应，只在 Mapper 和 Service 层流转。
- 使用构造器注入，禁止字段注入。
- Entity 不直接作为公网请求或响应模型。
- 写操作使用 `@Transactional`。
- 跨模块通过对方 Service 调用，不直接访问其他模块 Mapper。
- Mapper 必须添加 `@Mapper`。
- DTO、VO、Entity 必须分开定义，禁止为了省代码而复用同一个对象。
- 并发更新使用 MyBatis-Plus `@Version` 乐观锁，更新条数为 0 时返回冲突错误。

数据库结构通过 Flyway 管理：

```text
src/main/resources/db/migration/
├── V1__create_demo_item.sql
├── V2__create_user.sql
└── V3__add_user_index.sql
```

已经执行过的迁移文件不得修改；每次数据库变更新增更高版本脚本。

## 创建新项目时如何改名

1. 修改 `pom.xml` 的 `artifactId`、`name` 和 `description`。
2. 修改 `application.yml` 的 `spring.application.name`。
3. 修改 `BinliApplication` 类名。
4. 修改 `OpenApiConfig` 的文档标题。
5. 修改数据库名以及 `compose.yml` 中的 PostgreSQL 配置。

脚手架默认且推荐保持根包 `com.binlicoder`。如果确实要修改根包，还要同步修改：

- `application-dev.yml` 和 `application-prod.yml` 的 `mybatis-plus.type-aliases-package`
- `MybatisPlusConfig` 的 `@MapperScan` 根包
- Java 主代码和测试代码的目录及 `package/import`

## 生产部署检查

- 设置 `SPRING_PROFILES_ACTIVE=prod`。
- 使用独立数据库、Redis 账号和强密码。
- 替换 JWT、API Key 占位值。
- Swagger UI 和 OpenAPI JSON 在 prod 配置中保持关闭。
- 数据库账号遵循最小权限原则。
- 对外服务使用 HTTPS。
- 配置日志采集、告警、数据库备份和健康检查。
- 不向公网开放 PostgreSQL、Redis 和敏感 Actuator 端点。
- CI 中执行 `mvn clean test`。

## Docker 镜像

构建应用镜像：

```bash
docker build -t binlicoder-springboot-template:local .
```

运行镜像时必须连接可访问的 PostgreSQL 和 Redis，并注入生产密钥：

```bash
docker run --rm -p 8080:8080 \
  -e DB_URL='jdbc:postgresql://host.docker.internal:5432/binlicoder_template' \
  -e DB_USERNAME='postgres' \
  -e DB_PASSWORD='replace-me' \
  -e REDIS_HOST='host.docker.internal' \
  -e REDIS_PASSWORD='' \
  -e JWT_SECRET='replace-with-at-least-32-random-characters' \
  -e JWT_ISSUER='binlicoder-springboot-template' \
  -e EXTERNAL_API_KEY='replace-with-a-random-api-key' \
  -v binlicoder-logs:/var/log/binlicoder \
  binlicoder-springboot-template:local
```

生产环境建议通过部署平台的 Secret 管理能力注入敏感配置，不要把真实值写入镜像、Compose 文件或 Git 仓库。

## 上传 GitHub

如果 `spring` 作为独立仓库：

```bash
cd spring
git init
git add .
git commit -m "feat: initialize BinliCoder Spring Boot template"
git branch -M main
git remote add origin https://github.com/你的账号/你的仓库.git
git push -u origin main
```

上传前检查：

```bash
git status
git grep -n -i -E 'password|secret|api[-_]?key|token'
```

第二条命令会命中字段名和文档示例；需要确认其中没有真实凭据。

## 可选扩展

PostGIS/JTS、WebSocket、消息队列、对象存储、短信、地图、天气和 AI 客户端等能力未强制加入基础依赖。需要时建议为对应业务模块增加 `client` 或 `integration` 包。

## License

仓库默认未附带开源许可证，即保留所有权利。准备公开开源时，请根据用途选择并添加许可证，例如 MIT 或 Apache-2.0。
