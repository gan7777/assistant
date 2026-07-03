# 智能个人助理系统

## 技术栈
- Spring Boot 3.x
- Spring Security + JWT
- PostgreSQL + JPA
- Maven

## 核心功能
- [x] 用户注册与登录（JWT 认证）
- [x] 行程创建与查询
- [ ] 行程修改与删除（开发中）

## 快速启动
1. 启动 PostgreSQL（Docker）：
   ```bash
   docker run --name assistant-db -e POSTGRES_DB=assistant -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 -p 5432:5432 -d pgvector/pgvector:pg17
