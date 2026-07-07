# AI 智能行程助理

基于 Spring Boot 3 + 通义千问大模型 + JWT 的智能行程管理系统。

## 技术栈
- Spring Boot 3.x
- Spring Security + JWT
- PostgreSQL + JPA
- 通义千问大模型 API
- Docker + Docker Compose
- Swagger 接口文档

## 核心功能
- 用户注册/登录（JWT 认证）
- 行程管理（增删改查 + 分页 + 关键词搜索）
- **AI 自然语言创建行程**（如“明天下午3点开会”自动创建行程）
- Swagger API 文档
- Docker 一键部署

## 快速启动
```bash
docker-compose up -d


访问 http://localhost:8081/index.html

API 文档
启动后访问 http://localhost:8081/swagger-ui/index.html
