# Sistema de Oficina - Microserviços

Sistema completo de gerenciamento de oficina automotiva utilizando arquitetura de microserviços com Spring Boot, JWT e controle de acesso baseado em roles.

## 📋 Índice

- [🚀 Início Rápido](#-início-rápido)
- [👥 Usuários Pré-Cadastrados](#-usuários-pré-cadastrados)
- [📖 Sobre o Projeto](#-sobre-o-projeto)
- [⚙️ Instalação e Execução](#️-instalação-e-execução)
- [🔐 Autenticação e Autorização](#-autenticação-e-autorização)
- [📚 Guia de Endpoints](#-guia-de-endpoints)
- [🔍 Verificando o Sistema](#-verificando-o-sistema)
- [📊 Logs e Debugging](#-logs-e-debugging)
- [🛠️ Desenvolvimento](#️-desenvolvimento)
- [❓ FAQ](#-faq)
- [🐛 Troubleshooting](#-troubleshooting)
- [🏗️ Arquitetura](#️-arquitetura)
- [💾 Banco de Dados](#-banco-de-dados)

---

## 🚀 Início Rápido

Quer apenas rodar o sistema? Siga estes 4 passos:

### 1️⃣ Subir o Sistema

```bash
docker-compose up
```

Aguarde ~30 segundos para todos os serviços iniciarem.

### 2️⃣ Fazer Login com Usuário Pré-Cadastrado

O sistema já vem com 4 usuários pré-cadastrados! Use o mecânico para acessar a API:

```bash
curl -X POST http://localhost/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{"email":"mecanico@oficina.com","password":"senha123"}'
```

**Resposta:**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "",
  "expiresIn": 900
}
```

Copie o `accessToken` retornado.

### 3️⃣ Acessar Endpoint de Oficina

```bash
TOKEN="seu_token_aqui"

curl -X GET http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN"
```

**Resposta (200 OK):**
```json
{
  "content": [],
  "totalElements": 0,
  "totalPages": 0,
  "size": 20
}
```

✅ **Funcionou!** O usuário `mecanico@oficina.com` tem permissão para acessar a API de oficina.

### 4️⃣ Criar um Cliente

```bash
curl -X POST http://localhost/oficina/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "telefone": "11999999999",
    "email": "joao@email.com",
    "cpf": "12345678900",
    "endereco": "Rua Teste 123",
    "ativo": true
  }'
```

**Resposta (201 Created):**
```json
{
  "id": "uuid",
  "nome": "João Silva",
  "telefone": "11999999999",
  "email": "joao@email.com",
  "cpf": "12345678900",
  "endereco": "Rua Teste 123",
  "ativo": true
}
```

🎉 **Pronto!** Você já está usando o sistema completo.

---

## 👥 Usuários Pré-Cadastrados

O sistema já vem com 4 usuários pré-configurados no banco de dados H2 do auth-service, prontos para uso!

### Credenciais

**Todos os usuários usam a mesma senha:** `senha123`

| Email | Senha | Role | Level | Descrição |
|-------|-------|------|-------|-----------|
| `gestor@oficina.com` | senha123 | **GESTOR** | 4 | Acesso total ao sistema |
| `mecanico@oficina.com` | senha123 | **MECANICO** | 3 | Acessa todos endpoints de oficina |
| `atendente@oficina.com` | senha123 | **ATENDENTE** | 2 | Gerencia clientes (futuramente) |
| `cliente@oficina.com` | senha123 | **CLIENTE** | 1 | Acesso básico |

### Exemplos de Login

#### Gestor (Acesso Total)

```bash
curl -X POST http://localhost/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{"email":"gestor@oficina.com","password":"senha123"}'
```

#### Mecânico (Acessa API de Oficina)

```bash
curl -X POST http://localhost/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{"email":"mecanico@oficina.com","password":"senha123"}'
```

#### Cliente (Acesso Limitado)

```bash
curl -X POST http://localhost/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{"email":"cliente@oficina.com","password":"senha123"}'
```

**Testando permissões:**
- ✅ Gestor e Mecânico → Conseguem acessar `/oficina/**`
- ❌ Atendente e Cliente → Recebem **403 Forbidden** em `/oficina/**` (requer role MECANICO)

### Como Funciona

Os usuários são carregados automaticamente do arquivo `auth-service/src/main/resources/data.sql` quando o sistema inicia:

```sql
INSERT INTO usuario (id, name, email, password, role) VALUES
('...', 'Gestor Oficina', 'gestor@oficina.com', '$2a$10$...', 'GESTOR'),
('...', 'Mecanico Oficina', 'mecanico@oficina.com', '$2a$10$...', 'MECANICO'),
...
```

As senhas são hasheadas usando BCrypt com cost factor 10.

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

#### Veículos

##### Listar Veículos

```bash
GET http://localhost/oficina/veiculos
Authorization: Bearer {token}
```

##### Criar Veículo

```bash
POST http://localhost/oficina/veiculos
Authorization: Bearer {token}
Content-Type: application/json

{
  "placa": "ABC1234",
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2020,
  "cor": "Prata",
  "clienteId": "uuid-do-cliente",
  "ativo": true
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "placa": "ABC1234",
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2020,
  "cor": "Prata",
  "clienteId": "uuid-do-cliente",
  "ativo": true
}
```

##### Buscar Veículo por ID

```bash
GET http://localhost/oficina/veiculos/{id}
Authorization: Bearer {token}
```

##### Buscar Veículos por Cliente

```bash
GET http://localhost/oficina/veiculos/cliente/{clienteId}
Authorization: Bearer {token}
```

##### Atualizar Veículo

```bash
PUT http://localhost/oficina/veiculos/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "placa": "ABC1234",
  "marca": "Toyota",
  "modelo": "Corolla XEI",
  "ano": 2020,
  "cor": "Prata",
  "clienteId": "uuid-do-cliente",
  "ativo": true
}
```

##### Deletar Veículo

```bash
DELETE http://localhost/oficina/veiculos/{id}
Authorization: Bearer {token}
```

---

#### Vendas

##### Listar Vendas

```bash
GET http://localhost/oficina/vendas
Authorization: Bearer {token}
```

##### Criar Venda

```bash
POST http://localhost/oficina/vendas
Authorization: Bearer {token}
Content-Type: application/json

{
  "clienteId": "uuid-do-cliente",
  "observacoes": "Troca de óleo completa",
  "itens": [
    {
      "tipoItem": "PRODUTO",
      "itemId": "uuid-do-produto",
      "itemNome": "Óleo de Motor 5W30",
      "quantidade": 2,
      "precoUnitario": 65.00
    },
    {
      "tipoItem": "SERVICO",
      "itemId": "uuid-do-servico",
      "itemNome": "Troca de Óleo",
      "quantidade": 1,
      "precoUnitario": 80.00
    }
  ]
}
```

**Resposta (201 Created):**

```json
{
  "id": "uuid",
  "clienteId": "uuid-do-cliente",
  "dataVenda": "2025-11-04T16:06:33.895055",
  "itens": [
    {
      "id": "uuid",
      "tipoItem": "PRODUTO",
      "itemId": "uuid-do-produto",
      "itemNome": "Óleo de Motor 5W30",
      "quantidade": 2,
      "precoUnitario": 65.00,
      "subtotal": 130.00
    },
    {
      "id": "uuid",
      "tipoItem": "SERVICO",
      "itemId": "uuid-do-servico",
      "itemNome": "Troca de Óleo",
      "quantidade": 1,
      "precoUnitario": 80.00,
      "subtotal": 80.00
    }
  ],
  "valorTotal": 210.00,
  "observacoes": "Troca de óleo completa",
  "cancelada": false
}
```

##### Buscar Venda por ID

```bash
GET http://localhost/oficina/vendas/{id}
Authorization: Bearer {token}
```

##### Buscar Vendas por Cliente

```bash
GET http://localhost/oficina/vendas/cliente/{clienteId}
Authorization: Bearer {token}
```

##### Cancelar Venda

```bash
DELETE http://localhost/oficina/vendas/{id}
Authorization: Bearer {token}
```

**Nota:** Este endpoint não deleta a venda, apenas marca o campo `cancelada` como `true`.

---

#### Relatórios

##### Produtos Mais Vendidos

```bash
GET http://localhost/oficina/relatorios/produtos-mais-vendidos
Authorization: Bearer {token}
```

**Resposta (200 OK):**

```json
[
  {
    "itemId": "uuid",
    "itemNome": "Óleo de Motor 5W30",
    "tipoItem": "PRODUTO",
    "quantidadeVendida": 10,
    "valorTotal": 650.00
  }
]
```

##### Serviços Mais Vendidos

```bash
GET http://localhost/oficina/relatorios/servicos-mais-vendidos
Authorization: Bearer {token}
```

**Resposta (200 OK):**

```json
[
  {
    "itemId": "uuid",
    "itemNome": "Troca de Óleo",
    "tipoItem": "SERVICO",
    "quantidadeVendida": 15,
    "valorTotal": 1200.00
  }
]
```

##### Vendas por Cliente

```bash
GET http://localhost/oficina/relatorios/vendas-por-cliente
Authorization: Bearer {token}
```

**Resposta (200 OK):**

```json
[
  {
    "clienteId": "uuid",
    "clienteNome": "João Silva",
    "totalVendas": 5,
    "valorTotal": 1500.00
  }
]
```

##### Vendas por Período

```bash
GET http://localhost/oficina/relatorios/vendas-por-periodo?inicio=2025-11-01T00:00:00&fim=2025-11-30T23:59:59
Authorization: Bearer {token}
```

**Nota:** Este endpoint está com problema de parsing de data. Os outros 3 relatórios funcionam perfeitamente.

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

## 📊 Logs e Debugging

O sistema possui logs detalhados para facilitar o debugging e monitoramento de requisições.

### Logs Implementados

#### Gateway Service

O gateway registra todas as requisições que passam por ele:

**Logs de Requisição:**
```
INFO: Forwarding POST request to http://auth-service:8084/auth/login/password
DEBUG: Request body: {"email":"mecanico@oficina.com","password":"senha123"}
```

**Logs de Resposta:**
```
INFO: Received response from http://auth-service:8084/auth/login/password with status 200 OK
DEBUG: Response body: {"accessToken":"eyJ...","refreshToken":"","expiresIn":900}
```

**Logs de Erro:**
```
WARN: Service returned error: 401 UNAUTHORIZED - {"timestamp":"...","status":401,"error":"Unauthorized"}
```

#### Authorization Filter

O filtro de autorização registra todas as verificações de permissão:

**Rota Pública (sem autenticação):**
```
DEBUG: Authorization filter processing request: POST /auth/login/password
DEBUG: Path /auth/login/password does not require authorization, allowing request
```

**Rota Protegida (com sucesso):**
```
DEBUG: Authorization filter processing request: GET /oficina/clientes
DEBUG: User with role MECANICO authorized for path /oficina/clientes
```

**Acesso Negado:**
```
WARN: User with role CLIENTE is not authorized for path /oficina/clientes
```

**Token Inválido:**
```
WARN: Invalid JWT token for path /oficina/clientes: JWT expired at ...
```

### Como Visualizar os Logs

#### Ver logs de um serviço específico

```bash
docker logs gateway-service
```

#### Ver logs em tempo real

```bash
docker logs -f gateway-service
```

#### Ver últimas 100 linhas

```bash
docker logs --tail=100 gateway-service
```

#### Filtrar logs por nível

Apenas erros e warnings:
```bash
docker logs gateway-service 2>&1 | grep -E "WARN|ERROR"
```

Apenas requisições:
```bash
docker logs gateway-service 2>&1 | grep "Forwarding"
```

#### Ver logs de múltiplos serviços

```bash
docker-compose logs -f gateway-service auth-service
```

### Debugging de Problemas Comuns

#### Problema: 401 Unauthorized no login

**1. Verifique os logs do auth-service:**
```bash
docker logs auth-service 2>&1 | grep -i "login\|password\|unauthorized"
```

**2. Verifique se o usuário existe:**
- Os usuários pré-cadastrados são carregados do `data.sql` na inicialização
- Procure por: `Executing SQL script from file [/app/target/classes/data.sql]`

**3. Verifique se a senha está correta:**
- Todos os usuários pré-cadastrados usam a senha: `senha123`

#### Problema: 403 Forbidden ao acessar /oficina

**1. Verifique os logs do gateway:**
```bash
docker logs gateway-service 2>&1 | grep -E "WARN.*not authorized"
```

**2. Decodifique seu JWT para ver a role:**
- Acesse https://jwt.io
- Cole seu token
- Verifique o campo `"role"` no payload

**3. Verifique se está usando um usuário com permissão:**
- `/oficina/**` requer role **MECANICO** ou superior
- Use `mecanico@oficina.com` ou `gestor@oficina.com`

#### Problema: Serviço não responde

**1. Verifique se os serviços estão rodando:**
```bash
docker-compose ps
```

**2. Verifique os logs de inicialização:**
```bash
docker logs auth-service 2>&1 | grep -E "Started|ERROR"
```

Procure por: `Started AuthserviceApplication in X seconds`

**3. Verifique se há erros de conexão:**
```bash
docker logs gateway-service 2>&1 | grep -i "error\|connection"
```

### Logs SQL (Modo Debug)

Para ver todas as queries SQL executadas, os logs estão habilitados no auth-service:

```bash
docker logs auth-service 2>&1 | grep "Hibernate:"
```

Exemplo:
```
DEBUG: Hibernate: select u1_0.id,u1_0.email,u1_0.name,u1_0.password,u1_0.role from usuario u1_0 where u1_0.email=?
```

### Melhorias Implementadas

1. **Tratamento de Erros HTTP Correto**
   - Anteriormente, todos os erros eram retornados como `502 Bad Gateway`
   - Agora, o status code original do serviço é mantido (401, 403, 404, etc.)

2. **Logs Estruturados**
   - Logs de requisição/resposta no nível INFO
   - Detalhes do body no nível DEBUG
   - Erros no nível WARN/ERROR

3. **Rastreamento de Requisições**
   - Cada requisição pode ser rastreada do nginx → gateway → serviço
   - Facilita identificar onde uma requisição falhou

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

### Como usar um usuário com role MECANICO ou GESTOR?

Use os [usuários pré-cadastrados](#-usuários-pré-cadastrados)! O sistema já vem com 4 usuários prontos:

- `gestor@oficina.com` - Role GESTOR (acesso total)
- `mecanico@oficina.com` - Role MECANICO (acessa API de oficina)
- `atendente@oficina.com` - Role ATENDENTE
- `cliente@oficina.com` - Role CLIENTE

**Todos usam a senha:** `senha123`

Se criar novos usuários via API, eles terão role CLIENTE por padrão. Para alterar:

1. Acesse http://localhost:8084/h2-console
2. Conecte com: JDBC URL `jdbc:h2:mem:auth_db`, User `sa`, Password `password`
3. Execute: `UPDATE usuario SET role = 'MECANICO' WHERE email = 'seu@email.com';`
4. Faça login novamente para obter novo token com a role atualizada

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
  - ⚠️ **Importante:** Os 4 usuários pré-cadastrados são recarregados automaticamente do `data.sql` sempre que o serviço inicia
  - Outros usuários criados via API são perdidos ao reiniciar
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
- `vendas` - Vendas de produtos e serviços
- `itens_venda` - Itens das vendas

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

**Desenvolvido para a disciplina de Sistemas Distribuídos**
