# Guia de Conventional Commits

Este documento padroniza mensagens de commit para o backend.

## Objetivo

- historico legivel
- revisao de PR mais rapida
- rastreabilidade de mudancas por tipo
- apoio para release notes

## Formato padrao

`tipo(escopo-opcional): descricao curta no imperativo`

Exemplos:

- `feat(auth): adiciona refresh token rotativo`
- `fix(security): corrige validacao de authorization no logout`
- `refactor(recipe): simplifica mapeamento de DTO para command`
- `docs(api): atualiza exemplo de refresh com path param`

## Tipos principais

### feat

Use para nova funcionalidade.

Exemplos:

- `feat(calculator): adiciona validacao de peso positivo`
- `feat(auth): implementa blacklist de access token no logout`

### fix

Use para correcao de bug.

Exemplos:

- `fix(auth): corrige retorno 401 para token invalido`
- `fix(recipe): evita null pointer em mapeamento de preparo`

### refactor

Use para reorganizacao interna sem mudar regra de negocio.

Exemplos:

- `refactor(auth): extrai logica de revogacao de refresh token`
- `refactor(config): organiza beans de seguranca`

### docs

Use para alteracoes de documentacao.

Exemplos:

- `docs(readme): adiciona guia de commits convencionais`
- `docs(collection): corrige exemplos de logout e refresh`

### test

Use para criar/ajustar testes.

Exemplos:

- `test(auth): cobre cenario de refresh revogado`
- `test(calculator): adiciona validacao de typeWeight`

### chore

Use para manutencao tecnica geral.

Exemplos:

- `chore(maven): atualiza plugins de build`
- `chore(deps): atualiza versoes de bibliotecas`

### ci

Use para pipeline/workflow.

Exemplo:

- `ci(codeql): ajusta gatilho para pull requests`

## Escopos sugeridos para backend

- `auth`
- `security`
- `recipe`
- `calculator`
- `db`
- `config`
- `docs`
- `ci`

## Breaking changes

Quando houver quebra de contrato, use `!`:

- `feat(auth)!: altera contrato de endpoint de refresh`

No corpo do commit, documente impacto e acao necessaria no consumidor.

## Modelo rapido

Titulo:

`tipo(escopo): descricao`

Corpo opcional:

- contexto
- alteracoes principais
- impacto

Rodape opcional:

- referencia de issue/PR (`Refs #123`)
