# Analise Tecnica e Plano de Melhorias - gastro-factor-api

Data da analise: 2026-06-29

## 1. Resumo executivo

O projeto esta bem organizado por camadas (adapters, application, ports, infrastructure) e a API sobe com sucesso. Porem, existem riscos relevantes em seguranca, consistencia de contrato HTTP, governanca de banco de dados e cobertura de testes.

Pontos mais criticos:
- Seguranca: segredo JWT com fallback hardcoded, endpoints sensiveis publicos, logout com token na URL.
- Dados: Flyway desabilitado e migracao inicial praticamente vazia.
- Qualidade: apenas 1 teste de contexto, sem testes de comportamento.
- Operacao: configuracoes conflitantes entre ambientes e bootstrap de dados rodando no startup.

## 2. Escopo revisado

Arquivos principais avaliados:
- Build e dependencias: pom.xml
- Configuracoes: src/main/resources/application.yml, src/main/resources/application-default.yml
- Seguranca e autenticacao:
  - src/main/java/br/com/gastrofactorapi/infrastructure/config/SecurityConfig.java
  - src/main/java/br/com/gastrofactorapi/infrastructure/security/JwtAuthenticationFilter.java
  - src/main/java/br/com/gastrofactorapi/infrastructure/security/JwtUtils.java
  - src/main/java/br/com/gastrofactorapi/infrastructure/security/auth/AuthController.java
  - src/main/java/br/com/gastrofactorapi/infrastructure/security/RateLimitFilter.java
  - src/main/java/br/com/gastrofactorapi/infrastructure/config/RateLimitConfig.java
- Persistencia e dominio:
  - src/main/java/br/com/gastrofactorapi/adapters/output/persistence/recipe/RecipePersistence.java
  - src/main/java/br/com/gastrofactorapi/adapters/output/persistence/recipe/entity/*.java
  - src/main/resources/db/migrations/V1__init.sql
- DTOs e Controllers:
  - src/main/java/br/com/gastrofactorapi/adapters/input/api/recipe/RecipeController.java
  - src/main/java/br/com/gastrofactorapi/adapters/input/api/recipe/dto/request/*.java
- Tratamento de erros:
  - src/main/java/br/com/gastrofactorapi/infrastructure/exceptions/BusinessExceptionHandler.java
- Testes e CI:
  - src/test/java/br/com/gastrofactorapi/GastrofactorapiApplicationTests.java
  - .github/workflows/codeql.yml

Comando validado:
- ./mvnw -q test (executou com sucesso)

## 3. Achados e melhorias (priorizados)

### P0 - Critico (corrigir imediatamente)

1) Segredo JWT inseguro por fallback hardcoded
- Evidencia:
  - src/main/resources/application.yml (jwt.secret com valor default de desenvolvimento)
- Risco:
  - Em ausencia de variavel de ambiente, a API usa segredo conhecido/publico.
- Como melhorar:
  - Remover fallback do secret; exigir JWT_SECRET obrigatorio em runtime.
  - Falhar startup se secret nao existir ou for fraco.
  - Rotacionar segredo atual e invalidar tokens antigos.

2) Logout expondo token na URL + bug de blacklist
- Evidencia:
  - AuthController usa /logout/{accessToken}/{refreshToken}
  - Na blacklist, o valor salvo eh refreshToken, mas expiracao vem do accessToken.
- Risco:
  - Token em URL pode vazar via logs/proxy/browser history.
  - Blacklist inconsistente e passivel de comportamento incorreto.
- Como melhorar:
  - Trocar para POST /logout com body JSON ou uso exclusivo de Authorization header.
  - Salvar na blacklist apenas access token (ou revisar modelo para suportar ambos com tipo).
  - Adicionar teste de regressao para fluxo login-refresh-logout.

3) Endpoints de dominio liberados para qualquer usuario
- Evidencia:
  - SecurityConfig com permitAll para /v1/recipes/** e /v1/calculator/**.
- Risco:
  - Falta de controle de acesso e trilha de autorizacao.
- Como melhorar:
  - Definir matriz de autorizacao por endpoint (anonymous/authenticated/roles).
  - Manter publicos apenas endpoints intencionais.
  - Incluir testes de seguranca por endpoint.

### P1 - Alto (proxima sprint)

4) Governanca de banco fragil: Flyway desabilitado
- Evidencia:
  - flyway.enabled=false em profiles.
  - V1__init.sql cria apenas tabela flyway_baseline.
- Risco:
  - Drift entre ambientes; schema nao versionado de fato.
- Como melhorar:
  - Habilitar Flyway em dev/homolog/prod.
  - Criar migracoes reais para todas as entidades.
  - Adotar regra: toda mudanca de schema exige migration.

5) Contract mismatch no endpoint de criacao de receita
- Evidencia:
  - Controller retorna ResponseEntity<UUID>, mas responde 201 sem body.
  - Metodo save em controller nao anota @RequestBody diretamente.
- Risco:
  - Contrato OpenAPI e implementacao divergem; clientes quebram.
- Como melhorar:
  - Retornar UUID no body ou alterar assinatura para ResponseEntity<Void>.
  - Manter anotacoes de request no controller para clareza e compatibilidade.

6) Validacoes fracas em DTOs de receita
- Evidencia:
  - RecipeRequest, IngredientRequest, DetailsRequest, NutritionalRequest sem anotacoes Bean Validation relevantes.
- Risco:
  - Entrada invalida chega na regra de negocio/persistencia.
- Como melhorar:
  - Aplicar @NotNull, @NotBlank, @Size, @PositiveOrZero, @Valid em estruturas aninhadas.
  - Padronizar mensagens de erro e exemplos no Swagger.

7) Risco de excecoes 500 no filtro JWT
- Evidencia:
  - JwtAuthenticationFilter chama extractEmail sem tratamento de token malformado/expirado.
  - userRepository.findByEmail(...).orElseThrow() sem mapeamento especifico.
- Risco:
  - Requests com token invalido podem gerar erro interno ao inves de 401/403 consistente.
- Como melhorar:
  - Tratar explicitamente erros JWT e devolver 401 padronizado.
  - Evitar orElseThrow generico; retornar unauthorized quando usuario nao existir.

8) Rate limit com erro de regra e mensagem
- Evidencia:
  - numberOfattempts=5, minutes=1.
  - RateLimitConfig usa capacity(minutes), ignorando capacity desejada.
  - Mensagem diz "5 minuto" mesmo com janela de 1 minuto.
- Risco:
  - Limite efetivo incorreto (1 req/min) e comunicacao enganosa.
- Como melhorar:
  - Corrigir createBucket para capacity(capacity).
  - Externalizar limites por propriedade e endpoint.
  - Incluir headers padrao de rate limit (Remaining, Retry-After).

9) Seed de CSV executando no startup em qualquer ambiente
- Evidencia:
  - ReadCSV implementa CommandLineRunner e roda automaticamente.
- Risco:
  - Carga indevida em ambientes produtivos e bootstrap nao controlado.
- Como melhorar:
  - Proteger com profile dedicado (ex.: seed) ou feature flag.
  - Substituir System.out por logging estruturado.
  - Tornar idempotencia explicita e auditavel.

### P2 - Medio (evolucao)

10) Cobertura de testes insuficiente
- Evidencia:
  - Apenas GastrofactorapiApplicationTests com contextLoads.
- Risco:
  - Alta chance de regressao em seguranca, validacao e calculos.
- Como melhorar:
  - Adicionar testes unitarios:
    - CalculatorService + strategies
    - JwtUtils e fluxo AuthController
    - mappers principais
  - Adicionar testes de integracao com MockMvc para controllers.
  - Definir gate de cobertura Jacoco (ex.: 70% inicial).

11) Dependencias e stack em versoes muito de fronteira
- Evidencia:
  - Spring Boot 4.1.0 + Java 25.
  - Uso simultaneo de spring-boot-starter-webmvc e spring-boot-starter-web.
- Risco:
  - Menor maturidade/ecossistema de bibliotecas, possiveis incompatibilidades.
- Como melhorar:
  - Avaliar baseline LTS (ex.: Java 21) e versao Spring com maturidade do ecossistema.
  - Remover starter web redundante (manter apenas um).
  - Rodar matriz de compatibilidade em CI.

12) Constraints de dados ausentes para identidade
- Evidencia:
  - UserEntity.email sem unique na entidade; repository assume unicidade.
- Risco:
  - Corrida de concorrencia pode permitir emails duplicados.
- Como melhorar:
  - Adicionar unique constraint/index em email na migration.
  - Tratar violacao de unique no handler para responder 409.

13) Logs e hardening operacional
- Evidencia:
  - application-default expoe show-sql=true e h2 console habilitado.
  - shutdown difere entre profiles (IMMEDIATE x GRACEFUL).
- Risco:
  - Exposicao de internals e comportamento nao previsivel entre ambientes.
- Como melhorar:
  - Definir profiles claros (local, test, prod).
  - Em prod: h2-console off, show-sql off, shutdown graceful.
  - Revisar open-in-view e pool/connections conforme carga.

14) Tratamento de erro pode melhorar robustez
- Evidencia:
  - BusinessExceptionHandler usa parseInt em erroCode e @SuppressWarnings("null") com TODO.
- Risco:
  - Fragilidade para codigos malformados e manutencao ruim.
- Como melhorar:
  - Migrar BusinessException para carregar HttpStatus diretamente.
  - Remover suppressions e padronizar payload de erro com codigo interno.

15) CI de seguranca incompleto
- Evidencia:
  - CodeQL em build-mode none para java-kotlin.
- Risco:
  - Menor profundidade de analise para cenarios que exigem build completo.
- Como melhorar:
  - Ajustar para autobuild/manual com mvnw -DskipTests package.
  - Adicionar pipeline de testes + qualidade (spotbugs/checkstyle/owasp dependency-check).

## 4. Roadmap recomendado

### Fase 1 (1-2 semanas)
- Corrigir seguranca critica:
  - JWT secret obrigatorio, logout por body/header, blacklist correta.
  - Regras de autorizacao por endpoint.
- Corrigir rate limit e incluir testes.
- Ajustar contrato do endpoint de receita (retorno e request body).

### Fase 2 (2-3 semanas)
- Habilitar Flyway e criar migracoes reais.
- Adicionar constraints e indices principais.
- Revisar bootstrap CSV por profile/flag.

### Fase 3 (2-4 semanas)
- Expandir testes unitarios e integracao com metas de cobertura.
- Refinar CI/CD (build/test/security gates).
- Revisar baseline de versao (Java/Spring) conforme estrategia de produto.

## 5. Backlog objetivo (com criterio de pronto)

1) Seguranca JWT
- DoD:
  - Sem fallback de secret em prod.
  - Endpoint logout sem token em URL.
  - Testes cobrindo login/refresh/logout/blacklist.

2) Acesso e autorizacao
- DoD:
  - Matriz de acesso documentada e implementada.
  - Testes 401/403 para cada endpoint protegido.

3) Banco e migracoes
- DoD:
  - Flyway habilitado por ambiente.
  - Todas as tabelas sob migration versionada.
  - Pipeline falha se migration estiver faltando.

4) Qualidade de API
- DoD:
  - DTOs com Bean Validation completa.
  - OpenAPI coerente com retorno real.
  - Padrao de erro unico para 400/401/404/409/500.

5) Testes e observabilidade
- DoD:
  - Cobertura minima >= 70%.
  - Testes de contrato de controllers.
  - Logs estruturados sem vazamento de dados sensiveis.

## 6. Checklist de implementacao rapida

- [X] Remover fallback de jwt.secret e validar no startup.
- [X] Trocar logout para POST com body/header.
- [X] Corrigir blacklist para access token.
- [X] Reconfigurar SecurityConfig com autenticacao em /v1/recipes e /v1/calculator.
- [X] Corrigir regra de RateLimitConfig (capacity).
- [X] Corrigir RecipeController (RequestBody e retorno coerente).
- [ ] Adicionar validacoes em RecipeRequest e filhos.
- [ ] Habilitar Flyway e escrever migracoes reais.
- [ ] Criar indice unico para email.
- [ ] Criar suites de teste de dominio, seguranca e integracao.
- [ ] Endurecer application-default para uso local apenas.

## 7. Resultado da verificacao executada

- Build/teste: OK no comando ./mvnw -q test.
- Observacao importante: existem warnings de instrumentacao/agents e um unico teste de contexto; isso nao valida comportamento funcional nem seguranca em profundidade.
