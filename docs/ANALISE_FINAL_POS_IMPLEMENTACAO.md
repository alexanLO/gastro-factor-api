# Analise Final Pos-Implementacao - gastro-factor-api

Data da analise: 2026-06-29

## Resumo Executivo

O projeto evoluiu bem e hoje tem uma base tecnica solida para continuar crescendo.

Status geral:
- Seguranca: evoluiu bastante, com pontos criticos importantes ja corrigidos.
- Banco de dados: Flyway ativo e migracoes reais versionadas.
- Qualidade: suites de testes foram ampliadas, mas cobertura ainda esta abaixo do ideal para reduzir regressao.
- Arquitetura hexagonal: boa estrutura geral, com alguns acoplamentos pontuais para refinamento.

Decisao funcional confirmada:
- O endpoint de calculadora e intencionalmente publico (`permitAll`) e nao deve ser tratado como vulnerabilidade.

## Pontos Ja Resolvidos

- JWT sem fallback inseguro em producao e validacao de segredo no startup.
- Logout por POST sem token na URL.
- Blacklist de access token no logout.
- Endpoints de receita protegidos por autenticacao.
- Flyway habilitado e migrations de schema criadas.
- Indice unico de email criado via migration.
- `application-default` endurecido (h2-console off, show-sql off, shutdown graceful, open-in-view off).
- Suites de testes adicionadas (dominio, seguranca e integracao).

## Riscos e Melhorias Pendentes (Repriorizados)

### P0 - Alta prioridade tecnica

1) Hardening do filtro JWT para nao gerar erro 500 em token invalido
- Contexto:
	- O filtro ainda depende de fluxos que podem lançar excecoes sem mapeamento consistente para 401.
- Melhoria:
	- Envolver extracao/validacao de token em tratamento explicito e responder 401 padronizado em qualquer falha de token/usuario.

2) Tratamento de usuario inexistente no fluxo JWT
- Contexto:
	- Uso de `orElseThrow()` generico no filtro.
- Melhoria:
	- Converter para resposta de unauthorized com payload padrao (sem excecao interna).

### P1 - Importante para operacao e manutencao

3) Seed CSV executando no startup sem profile dedicado
- Contexto:
	- `ReadCSV` roda como `CommandLineRunner` por padrao.
- Melhoria:
	- Isolar por profile (ex.: `seed`) ou feature flag, e remover execucao automatica fora do ambiente desejado.

4) Acoplamento no boundary de autenticacao
- Contexto:
	- `AuthController` recebe `UserEntity` diretamente.
- Melhoria:
	- Criar DTOs de entrada/saida para auth (register/login) e mapear para entidade internamente.

5) CodeQL com baixa profundidade para Java compilado
- Contexto:
	- Workflow configurado com `build-mode: none`.
- Melhoria:
	- Trocar para `autobuild` ou `manual` com comando Maven para analise mais completa.

### P2 - Evolucao de qualidade e arquitetura

6) Cobertura de testes ainda baixa para gate confiavel
- Contexto:
	- Cobertura agregada atual aproximada:
		- Instrucao: 26.26%
		- Branch: 10.68%
		- Linha: 25.50%
- Melhoria:
	- Expandir testes por camada e criar gate progressivo no CI (ex.: 50% inicial, depois 70%).

7) Aderencia hexagonal: dependencia do dominio em infraestrutura
- Contexto:
	- `CalculatorService` importa excecoes da camada `infrastructure`.
- Melhoria:
	- Mover excecoes de dominio para pacote de dominio/aplicacao e manter infraestrutura como detalhe externo.

8) Rate limit com politicas hardcoded e mensagem inconsistente
- Contexto:
	- Limites estao fixos no filtro e mensagem nao reflete bem a janela.
- Melhoria:
	- Externalizar limites por propriedade, incluir headers padrao (`Retry-After`, `X-RateLimit-*`) e revisar mensagem.

9) Robustez do handler global
- Contexto:
	- `BusinessExceptionHandler` usa `Integer.parseInt` e ainda possui `@SuppressWarnings` pendente.
- Melhoria:
	- Migrar para modelo tipado de status/erro sem parse fragil e remover suppressions.

## Conclusao Final

Nao ha bloqueio estrutural grave no projeto neste momento.

Com os ajustes de hardening acima (especialmente no JWT filter) e evolucao de cobertura/CI, o projeto fica em um nivel muito bom de confiabilidade para crescimento continuo.

## Proximo Ciclo Recomendado (Sprint Curta)

1. Hardening completo de autenticacao (JWT filter + unauthorized padronizado).
2. Isolamento do seed por profile/flag.
3. DTOs de auth para reduzir acoplamento com entidade.
4. CodeQL com build real.
5. Meta de cobertura com gate progressivo no CI.
6. Refino de dependencias para fortalecer hexagonal puro.

## Checklist de Execucao Desta Rodada

- [X] Hardening completo de autenticacao (JWT filter + unauthorized padronizado).
- [X] Isolamento do seed por profile/flag.
- [X] DTOs de auth para reduzir acoplamento com entidade.
- [X] CodeQL com build real.
- [X] Meta de cobertura com gate progressivo no CI.
- [X] Rate limit com propriedades externas e headers de retorno.
- [X] Handler global mais robusto sem parse fragil.

Implementacao do gate de cobertura:
- Workflow: `.github/workflows/ci.yml`.
- Regra atual (progressiva e automatica por contexto):
  - `default` (demais branches): 19% (`MIN_LINE_COVERAGE_DEFAULT=0.19`)
  - `main`/`master`: 25% (`MIN_LINE_COVERAGE_MAIN=0.25`)
  - `release/*`: 50% (`MIN_LINE_COVERAGE_RELEASE=0.50`)
  - `tags` (release versionada): 70% (`MIN_LINE_COVERAGE_TAG=0.70`)
- Fonte de dados: `target/site/jacoco/jacoco.csv` gerado pelo JaCoCo no `mvn verify`.
