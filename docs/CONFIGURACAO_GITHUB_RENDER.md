# Configuracao de Variaveis - GitHub e Render

## Objetivo

Este guia mostra exatamente o que deve ser configurado em:

- GitHub (Actions)
- Render (servico Web + banco)

para que o microsservico rode e o CI nao quebre.

## 1. GitHub - Secrets e Variables

Caminho no GitHub:

- Repository Settings -> Secrets and variables -> Actions

### 1.1 Secrets obrigatorios

1. JWT_SECRET

- Tipo: Secret
- Obrigatorio: Sim
- Uso: autenticacao JWT no runtime e no workflow de CI
- Regra: minimo de 32 bytes

Exemplo de geracao (Linux/macOS/Git Bash):

```bash
openssl rand -base64 48
```

### 1.2 Variables obrigatorias

No estado atual do projeto, nao ha Variables obrigatorias para o GitHub Actions.

Os thresholds de cobertura estao definidos diretamente no workflow em:

- .github/workflows/ci.yml

### 1.3 O que o CI usa hoje

No workflow de CI:

- JWT_SECRET vem de secrets.JWT_SECRET
- o restante (thresholds de cobertura) e interno do arquivo

Resumo do gate de cobertura atual:

- default (demais branches): 19%
- main/master: 25%
- release/*: 50%
- tags: 70%

## 2. Render - Environment Variables

Caminho no Render:

- Dashboard -> Seu Servico -> Environment

## 2.1 Variaveis obrigatorias no Render (Web Service)

1. JWT_SECRET

- Obrigatoria
- Mesmo criterio: minimo de 32 bytes

2. SERVER_PORT

- Obrigatoria no Render para este projeto
- Valor recomendado: o valor de PORT fornecido pelo Render
- Forma pratica: iniciar a aplicacao com argumento --server.port=$PORT no Start Command

3. DB_HOST
4. DB_PORT
5. DB_NAME
6. DB_USER
7. DB_PASS

- Obrigatorias quando usar PostgreSQL no Render
- Devem apontar para o banco provisionado (Render PostgreSQL ou externo)

## 2.2 Variaveis opcionais no Render

1. RATE_LIMIT_LOGIN_ATTEMPTS

- Default no projeto: 5

2. RATE_LIMIT_LOGIN_MINUTES

- Default no projeto: 1

3. GOOGLE_CLIENT_ID
4. GOOGLE_CLIENT_SECRET

- O projeto possui chaves placeholder no application.yml para oauth google
- Configure apenas se realmente for usar login social

## 2.3 Start Command recomendado no Render

Como o projeto usa SERVER_PORT e o Render injeta PORT, recomenda-se:

```bash
java -jar app.jar --server.port=$PORT
```

Se voce usa Docker no Render, garanta que a aplicacao escute na porta do Render da mesma forma.

## 3. Exemplo de matriz de configuracao

### 3.1 GitHub

- Secret: JWT_SECRET = valor forte (>= 32 bytes)

### 3.2 Render (Web Service)

- JWT_SECRET = valor forte (>= 32 bytes)
- DB_HOST = host do PostgreSQL
- DB_PORT = porta do PostgreSQL
- DB_NAME = nome do banco
- DB_USER = usuario do banco
- DB_PASS = senha do banco
- RATE_LIMIT_LOGIN_ATTEMPTS = 5 (ou valor desejado)
- RATE_LIMIT_LOGIN_MINUTES = 1 (ou valor desejado)

## 4. Checklist rapido

### 4.1 GitHub

- JWT_SECRET criado em Actions Secrets
- pipeline de CI executando sem falha de segredo ausente

### 4.2 Render

- JWT_SECRET configurado
- variaveis de banco configuradas
- aplicacao iniciando sem erro de datasource/flyway
- endpoint /health respondendo

## 5. Erros comuns e como resolver

### Erro no CI: JWT_SECRET is not configured in GitHub Secrets

- Criar o secret JWT_SECRET no repositorio

### Erro no Render: jwt.secret nao pode ser vazio

- Definir JWT_SECRET com tamanho minimo adequado

### Erro no Render: falha de conexao com banco

- Revisar DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASS
- Confirmar que o banco esta acessivel pela rede do servico

### App sobe mas nao responde no Render

- Validar porta de escuta
- Usar Start Command com --server.port=$PORT

## 6. Arquivos de referencia no projeto

- .github/workflows/ci.yml
- src/main/resources/application.yml
- src/main/resources/application-default.yml
- src/main/resources/application-local.yml
