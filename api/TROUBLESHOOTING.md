# 🔧 Guia Rápido de Troubleshooting

## ⚡ Problemas Comuns e Soluções

### 1. **Erro: PostgreSQL Connection Refused**

```
Error: Connection refused to host: localhost:5432
```

**Solução**:
```bash
# macOS
brew services start postgresql@15
# ou
pg_ctl -D /usr/local/var/postgres start

# Linux
sudo systemctl start postgresql

# Verificar status
psql -U postgres -l
```

---

### 2. **Erro: Database "tasklist_db" Does Not Exist**

```
ERROR: database "tasklist_db" does not exist
```

**Solução**:
```bash
# Criar banco
createdb tasklist_db

# Ou no psql
psql -U postgres
CREATE DATABASE tasklist_db;
\q

# Verificar
psql -l | grep tasklist_db
```

---

### 3. **Erro: Maven Build Failure**

```
[ERROR] Compilation failure
```

**Solução**:
```bash
# Limpar cache
bash mvnw clean

# Atualizar dependências
bash mvnw dependency:resolve

# Recompilar
bash mvnw compile
```

---

### 4. **Erro: Port 8081 Already in Use**

```
Caused by: java.net.BindException: Address already in use
```

**Solução**:
```bash
# Encontrar processo usando a porta
lsof -i :8081

# Matar processo (macOS/Linux)
kill -9 <PID>

# Ou mudar porta em application.properties
server.port=8082

# Ou no terminal
bash mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

---

### 5. **Erro: JWT Token Invalid**

```
Status 401: Autenticação necessária para acessar este recurso
```

**Solução**:
1. Fazer login novamente para obter novo token
2. Verificar se o token está sendo enviado no header correto:
   ```
   Authorization: Bearer {token}
   ```
3. Verificar se o token não expirou (24 horas)
---

### 6. **Erro: Email Already Exists (409)**

```json
{
  "status": 409,
  "error": "Conflito",
  "message": "Email já em uso"
}
```

**Solução**:
- Use um email diferente para registrar novo usuário
- Ou delete o banco e recrie:
  ```bash
  dropdb tasklist_db
  createdb tasklist_db
  bash mvnw spring-boot:run  # Vai criar tabelas automaticamente
  ```

---

### 7. **Erro: Invalid Credentials (400)**

```json
{
  "status": 400,
  "error": "Requisição Inválida",
  "message": "Email ou senha inválidos"
}
```

**Solução**:
- Verificar se email e senha estão corretos
- Fazer login novamente
- Registrar novo usuário se necessário

---

### 8. **Erro: Validation Failed (400)**

```json
{
  "validationErrors": {
    "email": "Email deve ser válido",
    "password": "A senha deve ter pelo menos 6 caracteres"
  }
}
```

**Solução**:
- Email deve ser um email válido (xxx@xxx.xxx)
- Senha deve ter mínimo 6 caracteres
- Nome deve ter entre 2-255 caracteres
- Título da tarefa é obrigatório

---

### 9. **Erro: Task Not Found (404)**

```json
{
  "status": 404,
  "error": "Não Encontrado",
  "message": "Tarefa não encontrada com id: 999"
}
```

**Solução**:
- Verificar se o ID da tarefa é correto
- Listar todas as tarefas para obter IDs válidos:
  ```bash
  curl -X GET http://localhost:8081/api/tasks \
    -H "Authorization: Bearer {token}"
  ```
- Cada usuário só vê suas próprias tarefas

---

### 10. **Erro: Test Failures**

```
Tests run: 20, Failures: 7
```

**Solução**:
```bash
# Limpar e recompilar
bash mvnw clean compile

# Rodar testes novamente
bash mvnw test

# Ver detalhes do erro
bash mvnw test -e

# Rodar apenas um teste
bash mvnw test -Dtest=TaskServiceTest
```

---

## 🔍 Debugging Tips

### 1. **Verificar Logs**

```bash
# Durante execução
bash mvnw spring-boot:run

# Ver apenas erros
bash mvnw spring-boot:run 2>&1 | grep -i error

# Aumentar verbosidade
bash mvnw spring-boot:run -X
```

### 2. **Verificar Banco de Dados**

```bash
psql -U postgres tasklist_db

# Ver estrutura das tabelas
\dt

# Ver usuários
SELECT * FROM users;

# Ver tarefas
SELECT * FROM tasks;

# Ver estrutura de uma tabela
\d users
```

### 3. **Testar API com curl**

```bash
# Teste simples
curl -v http://localhost:8081/api/tasks

# Com todos os headers
curl -v \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  http://localhost:8081/api/tasks

# Ver headers de resposta
curl -i http://localhost:8081/api/tasks
```

### 4. **Verificar Configurações**

```bash
# Ver arquivo de propriedades
cat src/main/resources/application.properties

# Verificar se foi modificado
git diff src/main/resources/application.properties
```

---

## 🚀 Reiniciar Tudo do Zero

Se algo ficar muito errado:

```bash
# 1. Remover banco
dropdb tasklist_db

# 2. Remover cache Maven
rm -rf ~/.m2/repository/com/multi

# 3. Limpar projeto
bash mvnw clean

# 4. Recriar banco
createdb tasklist_db

# 5. Compilar novamente
bash mvnw compile

# 6. Rodar testes
bash mvnw test

# 7. Iniciar aplicação
bash mvnw spring-boot:run
```

---

## 📋 Checklist de Saúde

Use este checklist para verificar se tudo está OK:

```bash
# 1. PostgreSQL rodando?
pg_isready -h localhost -p 5432

# 2. Banco existe?
psql -U postgres -l | grep tasklist_db

# 3. Aplicação compila?
bash mvnw compile

# 4. Testes passam?
bash mvnw test

# 5. Aplicação inicia?
timeout 10 bash mvnw spring-boot:run

# 6. API responde?
curl -s http://localhost:8081/api/auth/login | head -c 50
```

---

## 🎯 Quick Fixes

### Reset Rápido
```bash
bash mvnw clean compile && \
dropdb tasklist_db && \
createdb tasklist_db && \
bash mvnw test
```

### Compilar e Rodar
```bash
bash mvnw clean compile && \
bash mvnw spring-boot:run
```

### Apenas Testes
```bash
bash mvnw clean test -DskipTests=false
```

---

## 💬 Mensagens de Sucesso Esperadas

### Compilação Bem-Sucedida
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXX s
[INFO] Finished at: YYYY-MM-DD
```

### Testes Bem-Sucedidos
```
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Aplicação Iniciada
```
Tomcat started on port(s): 8081 (http) with context path '/api'
Started TasklistApplication
```

### API Respondendo
```
HTTP/1.1 401 Unauthorized
```

(401 é esperado quando não tem token - indica que a API está rodando)

---

## 📱 Testar sem Curl (Alternativas)

### Usando Postman
1. Baixar em https://www.postman.com
2. Importar coleção
3. Usar {{accessToken}} como variável

### Usando Insomnia
1. Baixar em https://insomnia.rest
2. Criar workspace
3. Adicionar requisições

### Usando VSCode Rest Client
Instalar extensão "REST Client" e criar arquivo `.http`:

```http
### Registrar
POST http://localhost:8081/api/auth/register
Content-Type: application/json

{
  "name": "João",
  "email": "joao@test.com",
  "password": "123456"
}

### Login
@token = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "email": "joao@test.com",
  "password": "123456"
}

### Listar Tarefas
GET http://localhost:8081/api/tasks
Authorization: Bearer {{token}}
```

---

## 🆘 Quando Nada Funciona

1. **Limpe tudo**:
   ```bash
   bash mvnw clean && rm -rf target
   ```

2. **Reinicie o PostgreSQL**:
   ```bash
   brew services restart postgresql@15
   ```

3. **Recrie o banco**:
   ```bash
   dropdb tasklist_db
   createdb tasklist_db
   ```

4. **Compile do zero**:
   ```bash
   bash mvnw clean compile
   ```

5. **Rode os testes**:
   ```bash
   bash mvnw test
   ```

6. Se ainda não funcionar, consulte `IMPLEMENTATION.md` para detalhes completos.

---

**Última atualização**: 3 de dezembro de 2025  
**Status**: ✅ Pronto para ajudar
