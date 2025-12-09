# Tasklist API - Backend

Backend REST API para um sistema de gerenciamento de tarefas com autenticação JWT segura.

## 🚀 Quick Start

### Requisitos
- Java 17+
- PostgreSQL
- Maven 3.9.6+

### Setup

1. **Criar banco de dados**
```sql
CREATE DATABASE tasklist_db;
```

2. **Configurar propriedades** (`src/main/resources/application.properties`)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist_db
spring.datasource.username=postgres
spring.datasource.password=seu_password
```

3. **Compilar e executar**
```bash
bash mvnw clean compile
bash mvnw spring-boot:run
```

4. **Executar testes**
```bash
bash mvnw test
```

API disponível em: `http://localhost:8081/api`

---

## 📚 Endpoints Principais

### Autenticação
- `POST /auth/register` - Registrar novo usuário
- `POST /auth/login` - Fazer login
- `POST /auth/refresh` - Renovar token

### Tarefas (Autenticado)
- `POST /tasks` - Criar tarefa
- `GET /tasks` - Listar tarefas
- `GET /tasks/{id}` - Obter tarefa
- `PUT /tasks/{id}` - Atualizar tarefa
- `DELETE /tasks/{id}` - Deletar tarefa

---

## 🏗️ Arquitetura

```
Controllers → Services → Repositories → Database
```

Implementação com:
- ✅ Spring Boot 4.0.0
- ✅ Spring Security + JWT
- ✅ Spring Data JPA + PostgreSQL
- ✅ BCrypt para hashing de senhas
- ✅ Exception Handling centralizado
- ✅ 20 testes automatizados (JUnit 5 + Mockito)

---

## 📖 Documentação Completa

Ver `IMPLEMENTATION.md` para detalhes completos incluindo:
- Especificação de endpoints
- Exemplos de requisições/respostas
- Princípios SOLID implementados
- Testes e cobertura
- Configurações de segurança

---

## 📄 Licença

MIT
