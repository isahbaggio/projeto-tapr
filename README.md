# Sistema de Oficina - Microserviços

Sistema completo de gerenciamento de oficina automotiva utilizando arquitetura de microserviços com Spring Boot, JWT e controle de acesso baseado em roles.

## 📋 Índice

- [🚀 Início Rápido](#-início-rápido)
- [📖 Sobre o Projeto](#-sobre-o-projeto)
- [⚙️ Instalação e Execução](#️-instalação-e-execução)
- [👤 Primeiros Passos](#-primeiros-passos)
- [🔐 Autenticação e Autorização](#-autenticação-e-autorização)
- [📚 Guia de Endpoints](#-guia-de-endpoints)
- [🎯 Exemplo Prático Completo](#-exemplo-prático-completo)
- [🔍 Verificando o Sistema](#-verificando-o-sistema)
- [🛠️ Desenvolvimento](#️-desenvolvimento)
- [❓ FAQ](#-faq)
- [🐛 Troubleshooting](#-troubleshooting)
- [🏗️ Arquitetura](#️-arquitetura)
- [💾 Banco de Dados](#-banco-de-dados)

---

## 🚀 Início Rápido

Quer apenas rodar o sistema? Siga estes 5 passos:

```bash
docker-compose up
```

```bash
curl -X POST http://localhost/auth/users \
  -H "Content-Type: application/json" \
  -d '{"name":"João Silva","email":"joao@test.com","password":"senha123"}'
```

```bash
curl -X POST http://localhost/auth/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@test.com","password":"senha123"}'
```

Copie o `accessToken` retornado.

```bash
curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer {seu_token}"
```

⚠️ Você receberá **403 Forbidden** porque novos usuários são criados como **CLIENTE** por padrão. Endpoints de oficina requerem role **MECANICO** ou superior.

📖 Continue lendo para aprender como mudar a role do usuário e usar o sistema completo.

---

## 📖 Sobre o Projeto

### Arquitetura

```
Cliente → Nginx (porta 80) → Gateway (porta 8083) → Auth Service (porta 8084)
                                                   → Oficina Service (porta 8085) → PostgreSQL
```

### Serviços

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| **nginx** | 80 | Load balancer e ponto de entrada |
| **gateway-service** | 8083 | API Gateway com validação JWT e RBAC |
| **auth-service** | 8084 | Autenticação e gerenciamento de usuários |
| **oficina** | 8085 | Gerenciamento de oficina (clientes, produtos, serviços) |
| **oficina-db** | 5432 | PostgreSQL |

### Tecnologias

- Java 17 (Java 21 no Docker)
- Spring Boot 3.5.5+
- Spring Data JPA
- PostgreSQL 15
- H2 Database (auth-service)
- JWT Authentication (Auth0 java-jwt)
- MapStruct para mapeamento DTO
- Lombok
- Docker & Docker Compose
- Nginx
- Arquitetura Hexagonal (Ports and Adapters)

---

## ⚙️ Instalação e Execução

### Pré-requisitos

- Docker instalado ([Download Docker](https://www.docker.com/products/docker-desktop))
- Docker Compose instalado (já incluído no Docker Desktop)

### Iniciar o Sistema

Inicie todos os serviços:

```bash
docker-compose up
```

Inicie em background (sem logs no terminal):

```bash
docker-compose up -d
```

Rebuild completo (quando houver mudanças no código):

```bash
docker-compose up --build
```

### Parar o Sistema

```bash
docker-compose down
```

Parar e remover volumes (limpa banco de dados):

```bash
docker-compose down -v
```

---

## 👤 Primeiros Passos

### Passo 1: Criar Seu Primeiro Usuário

Registre um novo usuário no sistema:

```bash
curl -X POST http://localhost/auth/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@test.com",
    "password": "senha123"
  }'
```

**Resposta esperada:**

```json
{
  "id": "e645f285-009d-42ab-8c98-86e9dd4c4506",
  "name": "João Silva",
  "email": "joao@test.com",
  "role": "CLIENTE"
}
```

✅ Usuário criado com sucesso! Por padrão, todos os novos usuários recebem a role **CLIENTE**.

### Passo 2: Fazer Login

Faça login para obter um token JWT:

```bash
curl -X POST http://localhost/auth/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@test.com",
    "password": "senha123"
  }'
```

**Resposta esperada:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "",
  "expiresIn": 900
}
```

✅ Token obtido com sucesso! Ele expira em **15 minutos** (900 segundos).

### Passo 3: Tentar Acessar Endpoint de Oficina

Tente listar os clientes da oficina:

```bash
TOKEN="seu_token_aqui"

curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN"
```

**Resposta esperada:**

```
HTTP/1.1 403 Forbidden
```

❌ **403 Forbidden**: Você não tem permissão! Endpoints de oficina requerem role **MECANICO** ou superior.

### Passo 4: Mudar Role do Usuário

Para acessar endpoints de oficina, você precisa mudar a role do usuário para **MECANICO** ou **GESTOR**.

#### Opção 1: Via Console H2 (Recomendado para Testes)

Acesse o console H2 do auth-service:

```
http://localhost:8084/h2-console
```

**Configurações de conexão:**
- JDBC URL: `jdbc:h2:mem:auth_db`
- User: `sa`
- Password: `password`

Execute este SQL para mudar a role do usuário:

```sql
UPDATE tb_users SET role = 'MECANICO' WHERE email = 'joao@test.com';
```

✅ Role atualizada para MECANICO!

#### Opção 2: Criar Usuário com Role Específica via SQL

Você também pode criar usuários diretamente no banco:

```sql
INSERT INTO tb_users (id, name, email, password_hash, role, created_at, updated_at)
VALUES (
  RANDOM_UUID(),
  'Maria Mecânica',
  'maria@oficina.com',
  '$2a$10$hashed_password_here',
  'MECANICO',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
);
```

⚠️ Para gerar o hash da senha, use BCrypt com cost factor 10.

### Passo 5: Login Novamente e Acessar Oficina

Faça login novamente para obter um novo token com a role atualizada:

```bash
curl -X POST http://localhost/auth/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@test.com",
    "password": "senha123"
  }'
```

Agora tente acessar a oficina com o novo token:

```bash
TOKEN="novo_token_aqui"

curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN"
```

**Resposta esperada:**

```json
{
  "content": [],
  "totalElements": 0,
  "totalPages": 0,
  "size": 20
}
```

✅ **200 OK**: Acesso autorizado! Agora você pode usar todos os endpoints de oficina.

---

## 🔐 Autenticação e Autorização

### Como Funciona o JWT

1. Usuário faz login com email e senha
2. Sistema valida credenciais
3. Sistema gera um token JWT assinado contendo:
   - ID do usuário
   - Email
   - Role
   - Level da role
   - Tipo do token (access)
   - Data de expiração (15 minutos)
4. Cliente envia o token em todas as requisições protegidas
5. Gateway valida o token e verifica permissões

### Estrutura do Token JWT

O token JWT contém estas informações (claims):

```json
{
  "iss": "authservice",
  "aud": "deliveryapp",
  "sub": "e645f285-009d-42ab-8c98-86e9dd4c4506",
  "iat": 1762126604,
  "exp": 1762127504,
  "type": "access",
  "email": "joao@test.com",
  "role": "MECANICO",
  "level": 3
}
```

### Hierarquia de Roles

O sistema possui 4 roles com hierarquia de permissões:

```
GESTOR (level 4) > MECANICO (level 3) > ATENDENTE (level 2) > CLIENTE (level 1)
```

| Role | Level | Descrição | Acesso |
|------|-------|-----------|--------|
| **CLIENTE** | 1 | Clientes da oficina | Endpoints de autenticação |
| **ATENDENTE** | 2 | Atendentes da recepção | Gestão de clientes |
| **MECANICO** | 3 | Mecânicos | Todos os endpoints de oficina |
| **GESTOR** | 4 | Gestores | Acesso total ao sistema |

### Controle de Acesso

Roles superiores herdam permissões das roles inferiores. Por exemplo:

- **MECANICO** pode acessar tudo que **ATENDENTE** acessa
- **GESTOR** pode acessar tudo que **MECANICO** acessa

**Rotas protegidas:**

| Rota | Role Mínima |
|------|-------------|
| `/auth/**` | Nenhuma (público) |
| `/oficina/**` | MECANICO |

---

## 📚 Guia de Endpoints

### Auth Service (`/auth`)

Endpoints públicos para autenticação e gerenciamento de usuários.

#### Registrar Usuário

```bash
POST http://localhost/auth/users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@test.com",
  "password": "senha123"
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "name": "João Silva",
  "email": "joao@test.com",
  "role": "CLIENTE"
}
```

#### Login

```bash
POST http://localhost/auth/auth/login/password
Content-Type: application/json

{
  "email": "joao@test.com",
  "password": "senha123"
}
```

**Resposta (200 OK):**

```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "",
  "expiresIn": 900
}
```

#### Listar Usuários

```bash
GET http://localhost/auth/users
```

**Resposta (200 OK):**

```json
{
  "content": [
    {
      "id": "uuid",
      "name": "João Silva",
      "email": "joao@test.com",
      "role": "CLIENTE"
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

#### Buscar Usuário por ID

```bash
GET http://localhost/auth/users/{id}
```

---

### Oficina Service (`/oficina`)

⚠️ **Todos os endpoints requerem:**
- Header `Authorization: Bearer {token}`
- Role **MECANICO** ou superior

#### Clientes

##### Listar Clientes

```bash
GET http://localhost/oficina/clientes
Authorization: Bearer {token}
```

**Resposta (200 OK):**

```json
{
  "content": [
    {
      "id": "uuid",
      "nome": "Maria Santos",
      "telefone": "11999999999",
      "email": "maria@test.com",
      "cpf": "12345678900",
      "endereco": "Rua Teste 123",
      "ativo": true
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 20
}
```

##### Criar Cliente

```bash
POST http://localhost/oficina/clientes
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Santos",
  "telefone": "11999999999",
  "email": "maria@test.com",
  "cpf": "12345678900",
  "endereco": "Rua Teste 123",
  "ativo": true
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "nome": "Maria Santos",
  "telefone": "11999999999",
  "email": "maria@test.com",
  "cpf": "12345678900",
  "endereco": "Rua Teste 123",
  "ativo": true
}
```

##### Buscar Cliente por ID

```bash
GET http://localhost/oficina/clientes/{id}
Authorization: Bearer {token}
```

##### Atualizar Cliente

```bash
PUT http://localhost/oficina/clientes/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Maria Santos Silva",
  "telefone": "11988888888",
  "email": "maria@test.com",
  "cpf": "12345678900",
  "endereco": "Rua Nova 456",
  "ativo": true
}
```

##### Deletar Cliente

```bash
DELETE http://localhost/oficina/clientes/{id}
Authorization: Bearer {token}
```

---

#### Produtos

##### Listar Produtos

```bash
GET http://localhost/oficina/produtos
Authorization: Bearer {token}
```

##### Criar Produto

```bash
POST http://localhost/oficina/produtos
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Óleo Motor 5W30",
  "descricao": "Óleo sintético para motor",
  "preco": 89.90,
  "categoria": "LUBRIFICANTES",
  "ativo": true
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "nome": "Óleo Motor 5W30",
  "descricao": "Óleo sintético para motor",
  "preco": 89.90,
  "categoria": "LUBRIFICANTES",
  "ativo": true
}
```

##### Buscar Produto por ID

```bash
GET http://localhost/oficina/produtos/{id}
Authorization: Bearer {token}
```

##### Atualizar Produto

```bash
PUT http://localhost/oficina/produtos/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Óleo Motor 5W30 Premium",
  "descricao": "Óleo sintético premium",
  "preco": 99.90,
  "categoria": "LUBRIFICANTES",
  "ativo": true
}
```

##### Deletar Produto

```bash
DELETE http://localhost/oficina/produtos/{id}
Authorization: Bearer {token}
```

---

#### Serviços

##### Listar Serviços

```bash
GET http://localhost/oficina/servicos
Authorization: Bearer {token}
```

##### Criar Serviço

```bash
POST http://localhost/oficina/servicos
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Troca de Óleo",
  "descricao": "Troca de óleo e filtro",
  "preco": 150.00,
  "duracao": 30,
  "ativo": true
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "nome": "Troca de Óleo",
  "descricao": "Troca de óleo e filtro",
  "preco": 150.00,
  "duracao": 30,
  "ativo": true
}
```

##### Buscar Serviço por ID

```bash
GET http://localhost/oficina/servicos/{id}
Authorization: Bearer {token}
```

##### Atualizar Serviço

```bash
PUT http://localhost/oficina/servicos/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Troca de Óleo Completa",
  "descricao": "Troca de óleo, filtro e revisão",
  "preco": 200.00,
  "duracao": 45,
  "ativo": true
}
```

##### Deletar Serviço

```bash
DELETE http://localhost/oficina/servicos/{id}
Authorization: Bearer {token}
```

---

## 🎯 Exemplo Prático Completo

Vamos criar um fluxo completo: do zero até criar um cliente na oficina.

### 1️⃣ Subir o Sistema

```bash
docker-compose up -d
```

Aguarde ~30 segundos para todos os serviços iniciarem.

### 2️⃣ Verificar se Está Rodando

```bash
docker-compose ps
```

Todos os serviços devem estar com status `Up`.

### 3️⃣ Registrar um Usuário

```bash
curl -X POST http://localhost/auth/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos Mecânico",
    "email": "carlos@oficina.com",
    "password": "senha123"
  }'
```

### 4️⃣ Fazer Login

```bash
curl -X POST http://localhost/auth/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "carlos@oficina.com",
    "password": "senha123"
  }'
```

Copie o `accessToken` retornado.

### 5️⃣ Testar Acesso (Vai Falhar)

```bash
TOKEN="seu_token_aqui"

curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -v
```

Resultado: **403 Forbidden** (esperado, pois você é CLIENTE)

### 6️⃣ Mudar Role para MECANICO

Acesse http://localhost:8084/h2-console

Execute:

```sql
UPDATE tb_users SET role = 'MECANICO' WHERE email = 'carlos@oficina.com';
```

### 7️⃣ Login Novamente

```bash
curl -X POST http://localhost/auth/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "carlos@oficina.com",
    "password": "senha123"
  }'
```

Copie o **novo token** (agora com role MECANICO).

### 8️⃣ Listar Clientes (Agora Funciona!)

```bash
TOKEN="novo_token_aqui"

curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN"
```

Resultado: **200 OK** com lista vazia `[]`

### 9️⃣ Criar um Cliente

```bash
curl -X POST http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João das Couves",
    "telefone": "11987654321",
    "email": "joao@email.com",
    "cpf": "12345678900",
    "endereco": "Rua das Flores, 123",
    "ativo": true
  }'
```

Resultado: **201 Created** com dados do cliente criado

### 🔟 Listar Clientes Novamente

```bash
curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN"
```

Agora você verá o cliente "João das Couves" na lista!

✅ **Sucesso!** Você completou o fluxo inteiro: criou usuário, obteve permissões e usou a API de oficina.

---

## 🔍 Verificando o Sistema

### Ver Status dos Containers

```bash
docker-compose ps
```

Todos devem estar `Up`.

### Ver Logs de um Serviço

```bash
docker-compose logs auth-service
```

Ver logs em tempo real:

```bash
docker-compose logs -f auth-service
```

Ver logs de todos os serviços:

```bash
docker-compose logs -f
```

### Ver Últimas 50 Linhas de Log

```bash
docker-compose logs --tail=50 gateway-service
```

### Verificar se Portas Estão Abertas

```bash
curl http://localhost/auth/users
```

Se retornar JSON, está funcionando!

### Restart de um Serviço Específico

```bash
docker-compose restart auth-service
```

### Verificar Consumo de Recursos

```bash
docker stats
```

---

## 🛠️ Desenvolvimento

### Rodar Sem Docker

Você pode rodar cada serviço localmente para desenvolvimento.

#### Auth Service

```bash
cd auth-service
./mvnw spring-boot:run
```

Roda em http://localhost:8084

#### Gateway Service

```bash
cd gateway-service
./mvnw spring-boot:run
```

Roda em http://localhost:8083

#### Oficina Service

Primeiro, inicie o PostgreSQL:

```bash
docker-compose up -d oficina-db
```

Depois:

```bash
cd oficina
./mvnw spring-boot:run
```

Roda em http://localhost:8085

### Compilar sem Rodar

```bash
cd auth-service
./mvnw clean install
```

### Executar Testes

```bash
cd auth-service
./mvnw test
```

### Rebuild de um Serviço Específico

```bash
docker-compose build auth-service
docker-compose up -d --force-recreate auth-service
```

### Limpar Tudo e Começar do Zero

```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```

---

## ❓ FAQ

### Como criar usuário MECANICO ou GESTOR?

Novos usuários são criados como CLIENTE por padrão. Para mudar:

1. Acesse http://localhost:8084/h2-console
2. Execute: `UPDATE tb_users SET role = 'MECANICO' WHERE email = 'seu@email.com';`
3. Faça login novamente para obter novo token

### Quanto tempo o token JWT dura?

O token expira em **15 minutos** (900 segundos). Depois disso, você precisa fazer login novamente.

### Posso usar Postman ou Insomnia?

Sim! Configure assim:

- **URL Base**: http://localhost
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer {seu_token}`

### Como resetar o banco de dados?

```bash
docker-compose down -v
docker-compose up
```

Isso apaga todos os dados e recria os bancos vazios.

### Onde ficam os dados armazenados?

- **Auth Service**: H2 em memória (dados são perdidos ao parar o container)
- **Oficina Service**: PostgreSQL em volume Docker (dados persistem)

Para ver volumes:

```bash
docker volume ls
```

### Posso acessar o PostgreSQL?

Sim! Use qualquer cliente SQL:

- **Host**: localhost
- **Port**: 5432
- **Database**: oficina
- **User**: oficina
- **Password**: oficina123

### Como vejo apenas erros nos logs?

```bash
docker-compose logs -f | grep -i error
```

ou

```bash
docker-compose logs -f | grep -i exception
```

### Posso mudar as portas?

Sim, edite o `docker-compose.yml` e mapeie para outras portas.

Exemplo:
```yaml
ports:
  - "8080:80"  # Nginx agora na porta 8080
```

---

## 🐛 Troubleshooting

### Erro: "Porta já em uso"

**Problema**: Porta 80, 8083, 8084, 8085 ou 5432 já está em uso.

**Solução 1**: Pare o processo que está usando a porta.

No Windows:
```bash
netstat -ano | findstr :80
taskkill /PID <PID> /F
```

No Linux/Mac:
```bash
lsof -i :80
kill -9 <PID>
```

**Solução 2**: Mude a porta no `docker-compose.yml`.

### Erro: "Connection refused" ao acessar endpoint

**Problema**: Serviço ainda está inicializando.

**Solução**: Aguarde 30-60 segundos após `docker-compose up` e tente novamente.

Verifique os logs:
```bash
docker-compose logs -f auth-service
```

Procure por: `Started AuthserviceApplication in X seconds`

### Erro: "Empty reply from server"

**Problema**: Gateway não está se comunicando com o serviço backend.

**Solução**:
```bash
docker-compose restart gateway-service
docker-compose logs -f gateway-service
```

Verifique se auth-service e oficina estão rodando:
```bash
docker-compose ps
```

### Erro: 401 Unauthorized

**Causas possíveis:**

1. **Token expirado** (após 15 minutos)
   - Solução: Faça login novamente

2. **Token inválido** ou malformado
   - Solução: Copie o token completo, sem espaços

3. **Header Authorization faltando ou incorreto**
   - Solução: Use `Authorization: Bearer {token}`

### Erro: 403 Forbidden

**Causa**: Seu usuário não tem a role necessária.

**Solução**:
1. Verifique qual role você tem: decode o JWT em https://jwt.io
2. Mude sua role para MECANICO ou GESTOR (veja [FAQ](#-faq))
3. Faça login novamente

### Erro: 409 Conflict ao criar usuário

**Causa**: Já existe um usuário com esse email.

**Solução**: Use outro email ou delete o usuário existente no H2 console.

### Container não sobe

**Solução 1**: Limpar tudo e reconstruir
```bash
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```

**Solução 2**: Verificar logs do container com problema
```bash
docker-compose logs <nome-do-servico>
```

### Erro: "Cannot connect to database"

**Problema**: oficina-db não está rodando ou não está pronto.

**Solução**:
```bash
docker-compose up -d oficina-db
sleep 10
docker-compose up -d oficina
```

### Serviço fica reiniciando

Veja o log para identificar o erro:
```bash
docker-compose logs --tail=100 <nome-do-servico>
```

Causas comuns:
- Erro de compilação (veja os logs do Maven)
- Porta já em uso internamente
- Dependência de outro serviço que não subiu

### Como limpar TUDO e começar do zero?

```bash
docker-compose down -v
docker system prune -a
docker-compose build --no-cache
docker-compose up
```

⚠️ Isso apaga TODOS os containers, imagens, volumes e cache do Docker.

---

## 🏗️ Arquitetura

### Arquitetura do Sistema

```
┌─────────┐
│ Cliente │
└────┬────┘
     │
     ▼
┌────────────┐
│   Nginx    │  Porta 80 - Load Balancer
│  (Proxy)   │
└─────┬──────┘
      │
      ▼
┌──────────────┐
│   Gateway    │  Porta 8083 - Validação JWT + RBAC
└──────┬───────┘
       │
       ├─────────────┐
       │             │
       ▼             ▼
┌────────────┐  ┌──────────────┐
│   Auth     │  │   Oficina    │
│  Service   │  │   Service    │
│  Porta     │  │   Porta      │
│   8084     │  │   8085       │
└─────┬──────┘  └──────┬───────┘
      │                │
      ▼                ▼
┌──────────┐    ┌──────────────┐
│    H2    │    │  PostgreSQL  │
│ (memória)│    │  Porta 5432  │
└──────────┘    └──────────────┘
```

### Arquitetura Hexagonal (Ports and Adapters)

Todos os serviços seguem a Arquitetura Hexagonal:

```
src/main/java/com/example/servico/
│
├── domain/              # Núcleo - Lógica de Negócio Pura
│   ├── cliente/         # Entidade Cliente
│   │   ├── Cliente.java
│   │   ├── ClienteRepository.java (interface/port)
│   │   └── vo/          # Value Objects
│   │       └── Cpf.java
│   │
│   └── exceptions/      # Exceções de domínio
│
├── application/         # Casos de Uso
│   ├── cliente/
│   │   ├── CriarClienteHandler.java
│   │   ├── BuscarClienteHandler.java
│   │   └── ListarClientesHandler.java
│   │
│   └── ports/          # Interfaces (portas)
│       └── ClienteService.java
│
├── infrastructure/      # Detalhes Técnicos (Adaptadores)
│   ├── persistence/    # Adaptador JPA
│   │   ├── JpaClienteRepository.java
│   │   └── SpringDataClienteJpa.java
│   │
│   ├── config/         # Configurações
│   │   └── AppConfig.java
│   │
│   └── security/       # Segurança
│       └── SecurityConfig.java
│
└── interfaces/         # APIs (Adaptadores)
    └── rest/          # Controllers REST
        ├── ClienteController.java
        ├── dto/       # DTOs
        │   ├── ClienteRequest.java
        │   └── ClienteResponse.java
        └── mapper/    # MapStruct
            └── ClienteMapper.java
```

**Benefícios:**
- Testabilidade (domínio independente)
- Flexibilidade (trocar infraestrutura sem afetar negócio)
- Manutenibilidade (responsabilidades bem definidas)

### Fluxo de uma Requisição

1. **Cliente** envia requisição para Nginx (porta 80)
2. **Nginx** faz load balancing e encaminha para Gateway
3. **Gateway** valida JWT e verifica permissões (AuthorizationFilter)
4. Se autorizado, **Gateway** encaminha para serviço apropriado
5. **Serviço** processa (Controller → Handler → Repository)
6. **Repository** acessa banco de dados
7. Resposta volta pelo mesmo caminho

---

## 💾 Banco de Dados

### H2 Database (Auth Service)

Banco em memória usado pelo auth-service.

**Acesso:**
- Console: http://localhost:8084/h2-console
- JDBC URL: `jdbc:h2:mem:auth_db`
- User: `sa`
- Password: `password`

**Tabelas:**
- `tb_users` - Usuários
- `tb_magic_links` - Links mágicos para autenticação

**⚠️ Dados são perdidos quando o container para!**

### PostgreSQL (Oficina Service)

Banco persistente usado pelo oficina service.

**Acesso:**
- Host: `localhost`
- Port: `5432`
- Database: `oficina`
- User: `oficina`
- Password: `oficina123`

**Tabelas:**
- `clientes` - Clientes da oficina
- `produtos` - Produtos (peças, materiais)
- `servicos` - Serviços oferecidos
- `veiculos` - Veículos dos clientes
- `ordens_servico` - Ordens de serviço

**✅ Dados persistem mesmo após parar containers!**

### Estrutura da Tabela de Usuários (H2)

```sql
CREATE TABLE tb_users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### Queries Úteis

Listar todos os usuários:
```sql
SELECT * FROM tb_users;
```

Mudar role de um usuário:
```sql
UPDATE tb_users SET role = 'MECANICO' WHERE email = 'user@example.com';
```

Deletar usuário:
```sql
DELETE FROM tb_users WHERE email = 'user@example.com';
```

Ver clientes da oficina (PostgreSQL):
```sql
SELECT * FROM clientes;
```

---

## 📊 Códigos de Status HTTP

| Código | Significado | Quando Ocorre |
|--------|-------------|---------------|
| **200** | OK | Requisição bem-sucedida |
| **201** | Created | Recurso criado com sucesso |
| **400** | Bad Request | Dados inválidos enviados |
| **401** | Unauthorized | Token ausente, inválido ou expirado |
| **403** | Forbidden | Token válido mas sem permissão (role insuficiente) |
| **404** | Not Found | Recurso não encontrado |
| **409** | Conflict | Conflito (ex: email já cadastrado) |
| **500** | Internal Server Error | Erro no servidor |

---

## 📝 Exemplo de Collection Postman

Importe esta collection no Postman para testar facilmente:

```json
{
  "info": {
    "name": "Sistema Oficina",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth - Register",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "url": "http://localhost/auth/users",
        "body": {
          "mode": "raw",
          "raw": "{\"name\":\"João Silva\",\"email\":\"joao@test.com\",\"password\":\"senha123\"}"
        }
      }
    },
    {
      "name": "Auth - Login",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "url": "http://localhost/auth/auth/login/password",
        "body": {
          "mode": "raw",
          "raw": "{\"email\":\"joao@test.com\",\"password\":\"senha123\"}"
        }
      }
    },
    {
      "name": "Oficina - Listar Clientes",
      "request": {
        "method": "GET",
        "header": [{"key": "Authorization", "value": "Bearer {{token}}"}],
        "url": "http://localhost/oficina/clientes"
      }
    }
  ]
}
```

---

## 🚀 Próximos Passos

Agora que você domina o básico, explore:

1. **Implemente novos endpoints** no oficina service
2. **Adicione validações** customizadas nos DTOs
3. **Implemente refresh tokens** para melhor UX
4. **Adicione testes de integração**
5. **Configure CI/CD** com GitHub Actions
6. **Deploy em produção** com Kubernetes ou AWS

---

## 📄 Licença

Este projeto é parte de um trabalho acadêmico.

---

## 👥 Contribuindo

Pull requests são bem-vindos! Para mudanças maiores, abra uma issue primeiro.

---

**Desenvolvido com ❤️ para a disciplina de Tópicos Avançados em Programação**
