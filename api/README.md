# Tasklist API - Backend

Backend REST API para um sistema de gerenciamento de tarefas com autenticação JWT segura.

## Quick Start

### Requisitos

- Java 17+
- PostgreSQL
- Maven 3.9.6+

### Setup

**Banco de dados**

```sql
  RODANDO NO DOCKER
  rodar
    docker compose up -d
```

**Rordar a api via vscode**

```
Abrir o projeto e rodar a api, classe de entrada
  TaskApplication
```


API disponível em: `http://localhost:8081/api`

---

## Endpoints Principais
```
### Autenticação
- `POST /auth/register` - Registrar novo usuário
- `POST /auth/login` - Fazer login
```

### Tarefas (Autenticado)

- `POST /tasks` - Criar tarefa
- `GET /tasks` - Listar tarefas
- `GET /tasks/{id}` - Obter tarefa
- `PUT /tasks/{id}` - Atualizar tarefa
- `DELETE /tasks/{id}` - Deletar tarefa
---

### Lista de Tarefas (Autenticado)

- `POST /taskslist` - Criar tarefa
- `GET /taskslist` - Listar tarefas
- `GET /taskslist/{id}` - Obter tarefa
- `PUT /taskslist/{id}` - Atualizar tarefa
- `DELETE /taskslist/{id}` - Deletar tarefa
---

## Arquitetura

```
Controllers / Services / Repositories / Database
```

Implementação com:
- Spring Boot 4.0.0
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- BCrypt para hashing de senhas
- Exception Handling centralizado
- Testes automatizados (JUnit 5 + Mockito)

---
```
