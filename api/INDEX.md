# 📖 Índice de Documentação - Tasklist Backend

## 🎯 Comece Aqui

Novo no projeto? Leia nesta ordem:

1. **[README_BACKEND.md](README_BACKEND.md)** - Visão geral e setup rápido (5 min)
2. **[USAGE_GUIDE.md](USAGE_GUIDE.md)** - Como usar a API (10 min)
3. **[IMPLEMENTATION.md](IMPLEMENTATION.md)** - Documentação técnica completa (20 min)

---

## 📑 Documentação Completa

### 📚 Guias Principais

| Documento | Propósito | Tempo |
|-----------|-----------|-------|
| **[README_BACKEND.md](README_BACKEND.md)** | Setup rápido, principais endpoints | 5 min |
| **[USAGE_GUIDE.md](USAGE_GUIDE.md)** | Guia passo a passo, exemplos, troubleshooting | 15 min |
| **[IMPLEMENTATION.md](IMPLEMENTATION.md)** | Especificações técnicas, arquitetura, endpoints | 20 min |
| **[PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)** | Estrutura de arquivos, estatísticas | 10 min |
| **[SUMMARY.md](SUMMARY.md)** | Resumo executivo da implementação | 5 min |
| **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** | Problemas comuns e soluções | 10 min |

---

## 🔍 Encontre o Que Precisa

### 🚀 Como Começar
- Como compilar? → [README_BACKEND.md](README_BACKEND.md#setup)
- Como rodar testes? → [README_BACKEND.md](README_BACKEND.md#executar-testes)
- Como iniciar a aplicação? → [USAGE_GUIDE.md](USAGE_GUIDE.md#configuração-inicial)

### 🔐 Autenticação
- Como registrar? → [USAGE_GUIDE.md](USAGE_GUIDE.md#passo-1-registrar-usuário)
- Como fazer login? → [USAGE_GUIDE.md](USAGE_GUIDE.md#passo-2-fazer-login)
- Como renovar token? → [USAGE_GUIDE.md](USAGE_GUIDE.md#passo-3-renovar-token)
- Como funciona JWT? → [IMPLEMENTATION.md](IMPLEMENTATION.md#autenticação-e-segurança)

### 📋 Tarefas
- Criar tarefa → [USAGE_GUIDE.md](USAGE_GUIDE.md#criar-tarefa)
- Listar tarefas → [USAGE_GUIDE.md](USAGE_GUIDE.md#listar-minhas-tarefas)
- Atualizar tarefa → [USAGE_GUIDE.md](USAGE_GUIDE.md#atualizar-tarefa)
- Deletar tarefa → [USAGE_GUIDE.md](USAGE_GUIDE.md#deletar-tarefa)
- Especificações completas → [IMPLEMENTATION.md](IMPLEMENTATION.md#tarefas-todas-requerem-autenticação)

### 🏗️ Arquitetura
- Visão geral → [IMPLEMENTATION.md](IMPLEMENTATION.md#arquitetura)
- Estrutura de camadas → [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md#estrutura-em-camadas)
- Pacotes → [IMPLEMENTATION.md](IMPLEMENTATION.md#estrutura-de-pacotes)
- Fluxo de segurança → [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md#destaques-da-implementação)

### 🧪 Testes
- Como rodar testes? → [README_BACKEND.md](README_BACKEND.md#setup)
- Quais testes existem? → [IMPLEMENTATION.md](IMPLEMENTATION.md#testes)
- Cobertura de testes → [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md#cobertura-de-testes)

### 🔧 Troubleshooting
- PostgreSQL não conecta? → [TROUBLESHOOTING.md](TROUBLESHOOTING.md#1-erro-postgresql-connection-refused)
- Banco não existe? → [TROUBLESHOOTING.md](TROUBLESHOOTING.md#2-erro-database-tasklist_db-does-not-exist)
- Build falha? → [TROUBLESHOOTING.md](TROUBLESHOOTING.md#3-erro-maven-build-failure)
- Porta em uso? → [TROUBLESHOOTING.md](TROUBLESHOOTING.md#4-erro-port-8081-already-in-use)
- Token inválido? → [TROUBLESHOOTING.md](TROUBLESHOOTING.md#5-erro-jwt-token-invalid)
- Problemas comuns → [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### 📊 Informações Técnicas
- Stack tecnológico → [IMPLEMENTATION.md](IMPLEMENTATION.md#stack-tecnológico-obrigatória)
- Endpoints → [IMPLEMENTATION.md](IMPLEMENTATION.md#endpoints-da-api)
- Dependências → [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md#dependências-principais)
- Princípios SOLID → [IMPLEMENTATION.md](IMPLEMENTATION.md#princípios-solid-implementados)

### 💡 Exemplos
- Exemplos com cURL → [USAGE_GUIDE.md](USAGE_GUIDE.md#fluxo-de-autenticação)
- Fluxo completo → [IMPLEMENTATION.md](IMPLEMENTATION.md#exemplo-de-fluxo-completo)
- Testando com Postman → [USAGE_GUIDE.md](USAGE_GUIDE.md#testando-com-postman-ou-insomnia)

---

## 📋 Checklist Rápido

### Antes de Começar
- [ ] PostgreSQL instalado
- [ ] Java 17+ instalado
- [ ] Maven 3.9.6+ instalado
- [ ] Banco `tasklist_db` criado

### Setup Inicial
- [ ] Clonar repositório
- [ ] Editar `application.properties`
- [ ] Rodar `bash mvnw clean compile`
- [ ] Rodar `bash mvnw test`
- [ ] Rodar `bash mvnw spring-boot:run`

### Primeiros Testes
- [ ] POST /api/auth/register (criar usuário)
- [ ] POST /api/auth/login (fazer login)
- [ ] POST /api/tasks (criar tarefa)
- [ ] GET /api/tasks (listar tarefas)

---

## 🎓 Aprender Mais

### Sobre Spring Boot
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### Sobre JWT
- [JWT.io](https://jwt.io)
- [JWT em Spring Security](https://spring.io/blog/2015/01/12/the-login-page-spring-security-java-configuration)

### Sobre PostgreSQL
- [PostgreSQL Official Docs](https://www.postgresql.org/docs)
- [JDBC Driver](https://jdbc.postgresql.org)

### Sobre Testing
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide)
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

## 📞 Suporte Rápido

### Perguntas Frequentes

**P: Como mudo a porta?**  
R: Editar `application.properties`: `server.port=8082`

**P: Como mudo o banco de dados?**  
R: Editar `spring.datasource.url` em `application.properties`

**P: Como vejo os dados no banco?**  
R: `psql -U postgres tasklist_db` e depois `SELECT * FROM users;`

**P: Como debug um erro?**  
R: Consultar [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

**P: Como contribuir?**  
R: Consultar [SUMMARY.md](SUMMARY.md#próximos-passos-opcional) para melhorias sugeridas

---

## 📊 Estatísticas

| Métrica | Valor |
|---------|-------|
| Documentação | 6 arquivos |
| Linhas de documentação | 2000+ |
| Tempo de leitura total | 1 hora |
| Exemplos de código | 50+ |
| Endpoints documentados | 8 |
| Cenários de teste | 20 |

---

## 🗺️ Mapa de Navegação

```
┌─ START HERE
│  ├─ README_BACKEND.md (5 min)
│  └─ USAGE_GUIDE.md (15 min)
│
├─ ENTENDER A ARQUITETURA
│  ├─ IMPLEMENTATION.md
│  └─ PROJECT_STRUCTURE.md
│
├─ USAR A API
│  └─ USAGE_GUIDE.md
│
├─ RESOLVER PROBLEMAS
│  └─ TROUBLESHOOTING.md
│
└─ VER RESUMO
   └─ SUMMARY.md
```

---

## 🎯 Próximos Passos

1. Leia [README_BACKEND.md](README_BACKEND.md) (5 min)
2. Siga [USAGE_GUIDE.md](USAGE_GUIDE.md) (15 min)
3. Execute os exemplos
4. Consulte [IMPLEMENTATION.md](IMPLEMENTATION.md) conforme necessário
5. Use [TROUBLESHOOTING.md](TROUBLESHOOTING.md) se tiver problemas

---

## ✨ Dicas

- 💾 Salve este arquivo como favorito
- 📖 Leia a documentação em ordem sugerida
- 🔍 Use Ctrl+F para buscar palavras-chave
- 📝 Tome notas enquanto aprende
- 🧪 Teste cada exemplo enquanto lê

---

**Última atualização**: 3 de dezembro de 2025  
**Status**: ✅ Documentação Completa  
**Tempo total de leitura**: ~1 hora  

🚀 **Pronto para começar? Vá para [README_BACKEND.md](README_BACKEND.md)!**
