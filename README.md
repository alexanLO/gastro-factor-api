# Gastro Factor API

API REST para calculo nutricional de alimentos e gestao de receitas.

## Visao Geral

Este microsservico foi construido com Spring Boot e arquitetura inspirada em Hexagonal (Ports and Adapters). O projeto possui:

- autenticacao JWT com refresh token e blacklist no logout
- endpoint publico de calculadora nutricional
- endpoints de receitas protegidos por autenticacao
- migracoes versionadas com Flyway
- controle de taxa para login
- pipeline CI com testes e gate progressivo de cobertura

## Stack Tecnologica

- Java 25
- Spring Boot 4.1.0
- Spring Security + JWT (jjwt)
- Spring Data JPA
- Flyway
- PostgreSQL (padrao)
- H2 (perfil default/local)
- Maven
- Docker
- GitHub Actions (CI + CodeQL)

## Estrutura do Projeto

```text
src/main/java/br/com/gastrofactorapi
|- adapters        # adaptadores de entrada/saida
|- application     # comandos, servicos e regras de aplicacao
|- ports           # contratos de entrada e saida
|- infrastructure  # seguranca, config, excecoes, auth
|- shared          # utilitarios
```

## Como Executar Localmente

### 1) Requisitos

- Java 25
- Maven (ou wrapper ./mvnw)

### 2) Variaveis obrigatorias

A aplicacao exige segredo JWT com no minimo 32 bytes.

```bash
export JWT_SECRET="12345678901234567890123456789012"
```

No Windows PowerShell:

```powershell
$env:JWT_SECRET="12345678901234567890123456789012"
```

### 3) Rodar a aplicacao

Perfil default:

```bash
./mvnw spring-boot:run
```

Perfil local (habilita seed CSV):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Endpoints Principais

Base URL local: `http://localhost:8080`

### Auth (`/v1/auth`)

- `POST /v1/auth/register` (publico)
- `POST /v1/auth/login` (publico, com rate limit)
- `POST /v1/auth/refresh/{token}` (publico)
- `POST /v1/auth/logout` (Bearer + refreshToken no body)

### Calculadora (`/v1/calculator`)

- `POST /v1/calculator` (publico por decisao funcional)

### Receitas (`/v1/recipes`)

- `POST /v1/recipes` (autenticado)
- `GET /v1/recipes` (autenticado)

## Exemplo Rapido de Uso

### Login

```bash
curl -X POST http://localhost:8080/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@email.com","password":"Senha@123"}'
```

### Calculadora (publica)

```bash
curl -X POST http://localhost:8080/v1/calculator \
  -H "Content-Type: application/json" \
  -d '{"foodName":"Peito de Frango","foodWeight":900.00,"typeWeight":"COOKED"}'
```

## Banco de Dados e Migracoes

As migracoes ficam em `src/main/resources/db/migrations` e sao executadas automaticamente no startup.

Arquivos principais:

- `V1__init.sql`
- `V2__create_application_schema.sql`
- `V3__add_unique_index_user_email.sql`

## Seguranca

- JWT obrigatorio para endpoints protegidos
- resposta `401` padronizada para token invalido
- blacklist de access token no logout
- refresh token revogavel
- rate limit em `/v1/auth/login`

Propriedades de rate limit:

- `RATE_LIMIT_LOGIN_ATTEMPTS` (default: 5)
- `RATE_LIMIT_LOGIN_MINUTES` (default: 1)

## Qualidade e CI

Workflows:

- `.github/workflows/ci.yml`
- `.github/workflows/codeql.yml`

Gate progressivo de cobertura (linhas):

- branches gerais: 19%
- main/master: 25%
- release/*: 50%
- tags: 70%

## Testes

Executar testes:

```bash
./mvnw test
```

Executar verify (inclui relatorio JaCoCo):

```bash
./mvnw verify
```

## Docker

Build da imagem:

```bash
docker build -t gastro-factor-api .
```

Run:

```bash
docker run --rm -p 8080:8080 \
  -e JWT_SECRET="12345678901234567890123456789012" \
  gastro-factor-api
```

## Observabilidade

- health endpoint habilitado
- probes de liveness/readiness habilitadas
- actuator exposto de forma restrita

## Documentacao Completa

Veja o documento tecnico completo em:

- `docs/DOCUMENTACAO_MICROSSERVICO.md`
- `docs/CONFIGURACAO_GITHUB_RENDER.md`
- `docs/CONVENTIONAL_COMMITS.md`

## Licenca

Este projeto utiliza licenca MIT (arquivo `LICENSE`).
