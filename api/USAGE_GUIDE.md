# 🔧 Guia de Uso da API

## 📌 Configuração Inicial

### 1. Instalar PostgreSQL

**macOS (Homebrew)**:
```bash
brew install postgresql@15
brew services start postgresql@15
```

**Linux (Debian/Ubuntu)**:
```bash
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql
```

**Windows**:
Baixar em https://www.postgresql.org/download/

### 2. Criar Banco de Dados

```bash
psql -U postgres

# No psql:
CREATE DATABASE tasklist_db;
\q
```

### 3. Configurar Application Properties

Editar `src/main/resources/application.properties`:

```properties
# Configuração PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist_db
spring.datasource.username=postgres
spring.datasource.password=seu_password_aqui

# JWT (Trocar por valores seguros em produção)
app.jwt.secret=MinhaChaveSecretaMuitoLongaComPeloMenos32Caracteres!
app.jwt.expiration=86400000
app.jwt.refresh-expiration=604800000
```

### 4. Compilar e Rodar

```bash
# Compilar
bash mvnw clean compile

# Rodar testes
bash mvnw test

# Iniciar aplicação
bash mvnw spring-boot:run
```

API estará em: `http://localhost:8081/api`

---

## 🔐 Fluxo de Autenticação

### Passo 1: Registrar Usuário

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha@123"
  }'
```

**Resposta (201)**:
```json
{
  "message": "User registered successfully"
}
```

### Passo 2: Fazer Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha@123"
  }'
```

**Resposta (200)**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

**Guardar o `accessToken` para requisições futuras!**

---

## 📋 Gerenciar Tarefas

### Criar Tarefa

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implementar autenticação",
    "description": "Adicionar JWT e BCrypt"
  }'
```

**Resposta (201)**:
```json
{
  "id": 1,
  "title": "Implementar autenticação",
  "description": "Adicionar JWT e BCrypt",
  "completed": false,
  "createdAt": "2025-12-03T16:30:00",
  "updatedAt": "2025-12-03T16:30:00"
}
```

### Listar Minhas Tarefas

```bash
curl -X GET http://localhost:8081/api/tasks \
  -H "Authorization: Bearer {accessToken}"
```

**Resposta (200)**:
```json
[
  {
    "id": 1,
    "title": "Implementar autenticação",
    "description": "Adicionar JWT e BCrypt",
    "completed": false,
    "createdAt": "2025-12-03T16:30:00",
    "updatedAt": "2025-12-03T16:30:00"
  },
  {
    "id": 2,
    "title": "Criar testes",
    "description": "JUnit 5 + Mockito",
    "completed": false,
    "createdAt": "2025-12-03T16:35:00",
    "updatedAt": "2025-12-03T16:35:00"
  }
]
```

### Obter Tarefa Específica

```bash
curl -X GET http://localhost:8081/api/tasks/1 \
  -H "Authorization: Bearer {accessToken}"
```

### Atualizar Tarefa

```bash
curl -X PUT http://localhost:8081/api/tasks/1 \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implementar autenticação [CONCLUÍDO]",
    "description": "Adicionar JWT e BCrypt",
    "completed": true
  }'
```

### Deletar Tarefa

```bash
curl -X DELETE http://localhost:8081/api/tasks/1 \
  -H "Authorization: Bearer {accessToken}"
```

**Resposta (204)**: Sem conteúdo (sucesso)

---

## 🧪 Testando com Postman ou Insomnia

### Importar Coleção Postman

Criar um arquivo `tasklist-api.postman_collection.json`:

```json
{
  "info": {
    "name": "Tasklist API",
    "description": "Coleção de testes da API"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/auth/register"
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/auth/login"
          }
        }
      ]
    },
    {
      "name": "Tasks",
      "item": [
        {
          "name": "Create Task",
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/tasks"
          }
        },
        {
          "name": "Get All Tasks",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/tasks"
          }
        },
        {
          "name": "Get Task by ID",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/tasks/{{taskId}}"
          }
        }
      ]
    }
  ]
}
```

---

## ❌ Tratamento de Erros

### Email já registrado (409)

```json
{
  "timestamp": "2025-12-03T16:30:00",
  "status": 409,
  "error": "Conflito",
  "message": "Email já em uso: joao@example.com",
  "path": "/api/auth/register"
}
```

### Credenciais inválidas (400)

```json
{
  "timestamp": "2025-12-03T16:30:00",
  "status": 400,
  "error": "Requisição Inválida",
  "message": "Email ou senha inválidos",
  "path": "/api/auth/login"
}
```

### Tarefa não encontrada (404)

```json
{
  "timestamp": "2025-12-03T16:30:00",
  "status": 404,
  "error": "Não Encontrado",
  "message": "Tarefa não encontrada com id: 999",
  "path": "/api/tasks/999"
}
```

### Não autenticado (401)

```json
{
  "status": 401,
  "error": "Não Autorizado",
  "message": "Autenticação necessária para acessar este recurso",
  "path": "/api/tasks"
}
```

### Validação inválida (400)

```json
{
  "timestamp": "2025-12-03T16:30:00",
  "status": 400,
  "error": "Falha de Validação",
  "message": "Parâmetros de entrada inválidos",
  "validationErrors": {
    "email": "Email deve ser válido",
    "password": "A senha deve ter pelo menos 6 caracteres"
  },
  "path": "/api/auth/register"
}
```

---

## 🔍 Variáveis de Ambiente (Recomendado para Produção)

Criar arquivo `.env`:

```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/tasklist_db
DB_USERNAME=postgres
DB_PASSWORD=seu_password_seguro

# JWT
JWT_SECRET=sua_chave_secreta_muito_longa_e_segura_de_32_chars_ou_mais
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Server
SERVER_PORT=8081
```

Usar em `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=${JWT_EXPIRATION}
app.jwt.refresh-expiration=${JWT_REFRESH_EXPIRATION}
server.port=${SERVER_PORT:8081}
```

---

## 🐛 Troubleshooting

### Erro: "database ... does not exist"

```bash
# Verificar banco
psql -U postgres -l

# Criar banco
createdb tasklist_db

# Ou no psql
psql -U postgres
CREATE DATABASE tasklist_db;
```

### Erro: "Connection refused"

PostgreSQL não está rodando:

**macOS**:
```bash
brew services start postgresql@15
```

**Linux**:
```bash
sudo systemctl start postgresql
```

### Porta 8081 já em uso

```bash
# Mudar porta em application.properties
server.port=8082

# Ou matar processo usando a porta (macOS/Linux)
lsof -i :8081
kill -9 <PID>
```
---

## 📊 Monitoramento

### Ver logs em tempo real

```bash
bash mvnw spring-boot:run | grep -i error
```

### Ver statistísticas do banco

```bash
psql -U postgres tasklist_db

# Ver tabelas
\dt

# Ver usuários
SELECT * FROM users;

# Ver tarefas
SELECT * FROM tasks;

# Contar tarefas por usuário
SELECT u.email, COUNT(t.id) FROM users u 
LEFT JOIN tasks t ON u.id = t.user_id 
GROUP BY u.id, u.email;
```

---

## 🚀 Deploy (Docker)

### Criar Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/tasklist-0.0.1-SNAPSHOT.war app.war
ENTRYPOINT ["java","-jar","/app.war"]
EXPOSE 8081
```

### Build e Run

```bash
# Compilar
bash mvnw clean package

# Build imagem
docker build -t tasklist-api .

# Run container
docker run -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://db:5432/tasklist_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=password \
  tasklist-api
```

---

## 📚 Referências

- Spring Boot: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- JWT: https://jwt.io
- PostgreSQL: https://www.postgresql.org/docs
- Postman: https://www.postman.com
- Insomnia: https://insomnia.rest

---

**Status**: ✅ Pronto para uso

Qualquer dúvida, consulte `IMPLEMENTATION.md` para documentação técnica completa!
