# Simulador de Conta Bancaria em Java

Projeto educacional de um simulador bancario feito em Java puro, executado pelo terminal e organizado com Maven.

A aplicacao foi criada para praticar programacao orientada a objetos, separacao de responsabilidades, encapsulamento, enums, registro de extrato, persistencia com JDBC/SQLite, migracao de CSV para banco de dados e testes automatizados com JUnit 5.

O projeto nao usa Spring Boot, API web ou interface grafica. Toda a interacao acontece pelo terminal.

---

## Indice

- [Visao geral](#visao-geral)
- [Estado atual](#estado-atual)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Como executar](#como-executar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Mapa dos arquivos](#mapa-dos-arquivos)
- [Guia arquivo por arquivo](#guia-arquivo-por-arquivo)
- [Organizacao da aplicacao](#organizacao-da-aplicacao)
- [Fluxo principal da aplicacao](#fluxo-principal-da-aplicacao)
- [Regras de dominio](#regras-de-dominio)
- [Persistencia atual](#persistencia-atual)
- [Migracao CSV para SQLite](#migracao-csv-para-sqlite)
- [Modelo conceitual do banco de dados](#modelo-conceitual-do-banco-de-dados)
- [Primeiros comandos SQL](#primeiros-comandos-sql)
- [Conceitos iniciais de JDBC](#conceitos-iniciais-de-jdbc)
- [Plano de integracao JDBC](#plano-de-integracao-jdbc)
- [Testes automatizados](#testes-automatizados)
- [Conceitos praticados](#conceitos-praticados)
- [Linha do tempo dos commits](#linha-do-tempo-dos-commits)

---

## Visao geral

O simulador representa um sistema bancario simples. Ele permite criar contas, movimentar saldo, registrar operacoes no extrato e manter os dados salvos entre execucoes usando SQLite.

A ideia principal do projeto e separar bem cada responsabilidade:

- a camada `app` cuida da execucao no terminal;
- a camada `model` cuida das regras do banco, das contas e das transacoes;
- a camada `persistence` cuida da gravacao e leitura dos dados;
- os testes em `src/test/java` verificam se as regras e a persistencia continuam funcionando.

Essa separacao ajuda a manter o codigo organizado. A aplicacao de terminal nao deve decidir regras internas da conta, e os repositories nao devem decidir regras de negocio. Cada parte tem uma funcao clara.

---

## Estado atual

A aplicacao usa persistencia principal em SQLite via JDBC.

Pontos principais do estado atual:

- `AplicacaoBancaria` inicializa o banco, carrega contas e transacoes, exibe o menu e processa as operacoes do usuario.
- `PersistenciaBancoService` salva contas e transacoes de forma coordenada em uma unica transacao SQL.
- `ContaRepositoryJdbc` salva e carrega contas no banco SQLite.
- `TransacaoRepositoryJdbc` salva, carrega e apaga transacoes vinculadas a uma conta.
- `InicializadorBanco` cria as tabelas `contas` e `transacoes` automaticamente caso elas ainda nao existam.
- `ConexaoBanco` centraliza a URL de conexao JDBC.
- `ContaRepositoryCsv` e `TransacaoRepositoryCsv` continuam no projeto como codigo legado e suporte para migracao.
- `MigradorCsvParaJdbc` permite migrar dados antigos de CSV para SQLite.
- Os arquivos gerados em `data/` nao devem ser versionados no Git.

---

## Funcionalidades

- Criar contas bancarias.
- Realizar depositos.
- Realizar saques.
- Consultar saldo de uma conta.
- Listar contas cadastradas.
- Buscar conta por numero.
- Transferir valores entre contas.
- Registrar transacoes no extrato.
- Gerar extrato com historico de operacoes.
- Persistir contas e transacoes em banco SQLite.
- Migrar dados antigos de CSV para SQLite.
- Testar regras de dominio e persistencia com JUnit 5.

---

## Tecnologias

- Java 25.
- Maven.
- JDBC.
- SQLite.
- SQLite JDBC Driver.
- JUnit 5.
- Maven Surefire Plugin.

Dependencias e plugins principais configurados no `pom.xml`:

| Item | Uso |
|---|---|
| `org.xerial:sqlite-jdbc` | Permite que o Java se conecte ao SQLite usando JDBC |
| `org.junit.jupiter:junit-jupiter` | Permite escrever e executar testes automatizados com JUnit 5 |
| `maven-surefire-plugin` | Executa os testes do projeto pelo Maven |

---

## Como executar

Na raiz do projeto, execute a aplicacao principal com:

```bash
mvn exec:java -Dexec.mainClass=app.Main
```

Ao iniciar, a aplicacao:

1. cria a estrutura do banco SQLite se necessario;
2. carrega as contas salvas;
3. carrega as transacoes de cada conta;
4. exibe o menu no terminal.

Menu principal:

```text
1 - Criar conta
2 - Deposito
3 - Saque
4 - Consulta de saldo
5 - Listar contas
6 - Buscar conta por numero
7 - Transferencia
8 - Gerar extrato
0 - Sair
```

Para rodar os testes:

```bash
mvn test
```

Para migrar dados antigos de CSV para SQLite:

```bash
mvn exec:java -Dexec.mainClass=app.MigracaoCsvParaJdbcMain
```

---

## Estrutura do projeto

```text
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       ├── app
│   │       │   ├── AplicacaoBancaria.java
│   │       │   ├── Main.java
│   │       │   └── MigracaoCsvParaJdbcMain.java
│   │       ├── model
│   │       │   ├── Banco.java
│   │       │   ├── Conta.java
│   │       │   ├── ResultadoTransferencia.java
│   │       │   ├── TipoOperacao.java
│   │       │   └── Transacao.java
│   │       └── persistence
│   │           ├── ConexaoBanco.java
│   │           ├── ContaRepositoryCsv.java
│   │           ├── ContaRepositoryJdbc.java
│   │           ├── InicializadorBanco.java
│   │           ├── MigradorCsvParaJdbc.java
│   │           ├── PersistenciaBancoService.java
│   │           ├── TransacaoRepositoryCsv.java
│   │           └── TransacaoRepositoryJdbc.java
│   └── test
│       └── java
│           ├── model
│           │   ├── BancoTest.java
│           │   ├── ContaTest.java
│           │   ├── ResultadoTransferenciaTest.java
│           │   ├── TipoOperacaoTest.java
│           │   └── TransacaoTest.java
│           └── persistence
│               ├── ConexaoBancoTest.java
│               ├── ContaRepositoryCsvTest.java
│               ├── ContaRepositoryJdbcTest.java
│               ├── InicializadorBancoTest.java
│               ├── MigradorCsvParaJdbcTest.java
│               ├── PersistenciaBancoServiceTest.java
│               ├── TransacaoRepositoryCsvTest.java
│               └── TransacaoRepositoryJdbcTest.java
└── data
    ├── banco.db
    ├── contas.csv
    └── transacoes.csv
```

A pasta `target/` e gerada automaticamente pelo Maven durante compilacao e testes. Ela nao faz parte do codigo-fonte.

A pasta `data/` e usada em tempo de execucao. Ela pode conter o arquivo `banco.db` do SQLite e, localmente, arquivos CSV de suporte a migracao.

Arquivos gerados em `data/`, como `banco.db`, `contas.csv` e `transacoes.csv`, nao devem ser versionados no Git.

---

## Mapa dos arquivos

Esta parte mostra onde cada arquivo entra no projeto.

```text
pom.xml
```

Arquivo de configuracao do Maven. Define versao do Java, dependencias, plugin de testes e informacoes basicas do projeto.

```text
.gitignore
```

Arquivo que impede o Git de versionar arquivos gerados pela IDE, pelo Maven ou pela aplicacao em tempo de execucao.

```text
README.md
```

Arquivo central de documentacao. Reune visao geral, explicacao das classes, conceitos estudados, modelo de banco, JDBC, testes e linha do tempo dos commits.

```text
src/main/java/app
```

Pasta da aplicacao de terminal. Aqui ficam o ponto de entrada (`Main`), o fluxo do menu (`AplicacaoBancaria`) e o comando de migracao (`MigracaoCsvParaJdbcMain`).

```text
src/main/java/model
```

Pasta do dominio. Aqui ficam as regras do banco, da conta, da transferencia e do extrato.

```text
src/main/java/persistence
```

Pasta de persistencia. Aqui ficam as classes que salvam e carregam dados em CSV ou SQLite/JDBC.

```text
src/test/java/model
```

Testes das regras de dominio.

```text
src/test/java/persistence
```

Testes de persistencia CSV, SQLite/JDBC, migracao e transacao SQL.

---

## Guia arquivo por arquivo

O objetivo desta parte e explicar o projeto quase linha por linha, mas agrupando linhas que formam uma mesma ideia. Assim fica mais legivel do que comentar literalmente uma linha por vez.

---

### `pom.xml`

| Linhas | Explicacao |
|---|---|
| 1-3 | Declaram que este e um projeto Maven e indicam o schema XML usado pelo Maven. |
| 5 | Informa a versao do modelo Maven: `4.0.0`. |
| 7-9 | Definem identificacao do projeto: grupo, artefato e versao. |
| 11-16 | Configuram propriedades: Java 25, encoding UTF-8 e versao centralizada do JUnit. |
| 18-24 | Adicionam JUnit 5 como dependencia de teste. Isso permite escrever testes com `@Test` e `assertEquals`. |
| 26-30 | Adicionam o driver `sqlite-jdbc`, que permite conectar Java ao SQLite via JDBC. |
| 33-41 | Configuram o `maven-surefire-plugin`, responsavel por executar os testes com `mvn test`. |

O que foi estudado aqui:

- estrutura de um projeto Maven;
- dependencias;
- plugins;
- diferenca entre dependencia de producao e dependencia de teste;
- configuracao de Java e JUnit.

---

### `.gitignore`

| Linhas | Explicacao |
|---|---|
| 1 | Ignora arquivos `.class`, que sao gerados pela compilacao Java. |
| 2 | Ignora a pasta `bin/`, comum em algumas IDEs. |
| 3 | Ignora configuracoes locais do VS Code. |
| 4 | Ignora `.DS_Store`, arquivo gerado pelo macOS. |
| 5 | Ignora `target/`, pasta gerada pelo Maven. |
| 6 | Ignora `data/`, pasta usada pela aplicacao para banco SQLite e CSV locais. |

O que foi estudado aqui:

- diferenca entre codigo-fonte e arquivo gerado;
- por que banco local e arquivos de build nao devem ir para o Git;
- organizacao limpa do repositorio.

---

### `README.md`

Este proprio arquivo funciona como o guia completo do projeto.

Ele esta dividido por barras horizontais (`---`) para separar os blocos principais:

- visao geral do projeto;
- estrutura de pastas;
- guia arquivo por arquivo;
- organizacao da aplicacao;
- regras de dominio;
- persistencia;
- modelo de banco;
- conceitos JDBC;
- testes automatizados;
- linha do tempo dos commits.

O que foi estudado aqui:

- documentacao tecnica;
- organizacao de conteudo em Markdown;
- indice com links internos;
- tabelas;
- blocos de codigo;
- explicacao de arquitetura e historico do projeto.

---

### `src/main/java/app/Main.java`

| Linhas | Explicacao |
|---|---|
| 1 | Linha em branco inicial. Nao altera o funcionamento, mas pode ser removida futuramente por estilo. |
| 2 | Declara o pacote `app`, onde ficam classes ligadas a execucao da aplicacao. |
| 4 | Declara a classe `Main`, ponto de entrada do programa. |
| 5 | Declara o metodo `main`, que e chamado pela JVM quando o programa inicia. |
| 6 | Cria uma instancia de `AplicacaoBancaria`. |
| 7 | Chama `executar()`, entregando para `AplicacaoBancaria` o controle do menu e do fluxo. |
| 8-9 | Fecham o metodo e a classe. |

O que foi estudado aqui:

- ponto de entrada em Java;
- separacao entre iniciar o programa e controlar o menu;
- responsabilidade pequena para a classe `Main`.

---

### `src/main/java/app/AplicacaoBancaria.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara o pacote `app`. |
| 3-5 | Importam classes Java usadas para excecoes SQL, entrada invalida e leitura pelo terminal. |
| 7-15 | Importam classes do dominio e da persistencia usadas pela aplicacao. |
| 17 | Declara a classe que controla a aplicacao de terminal. |
| 18-23 | Declaram dependencias principais como `final`: service de persistencia, inicializador, scanner, banco e repositories. |
| 25-39 | Construtor. Cria `Scanner`, `Banco`, conexao SQLite, inicializador, repositories JDBC e o service transacional. |
| 41-52 | Metodo `executar()`. Inicializa o banco, carrega dados e mantem o menu em loop ate o usuario sair. |
| 54-72 | `processarOpcao()`. Usa `switch` para chamar o metodo correto de acordo com a opcao digitada. |
| 74-84 | `lerInteiro()`. Fica tentando ler numero inteiro e trata entrada invalida com `InputMismatchException`. |
| 86-98 | `mostrarMenu()`. Imprime as opcoes disponiveis no terminal. |
| 100-125 | `criarConta()`. Le numero e nome, verifica duplicidade, cria a conta, adiciona no dominio e salva o estado. |
| 127-133 | `salvarEstado()`. Chama `PersistenciaBancoService` e trata erro de banco com mensagem simples para o usuario. |
| 135-151 | `carregarContas()`. Carrega contas do SQLite e depois carrega o extrato de cada conta. |
| 153-159 | `inicializarBanco()`. Garante que as tabelas existam antes de usar o banco. |
| 161-171 | `lerDouble()`. Le valores numericos para deposito, saque e transferencia. |
| 173-177 | `mostrarDadosConta()`. Centraliza a exibicao de numero, titular e saldo. |
| 179-204 | `depositar()`. Busca a conta, le o valor, chama `Conta.depositar()`, salva e mostra resultado. |
| 206-231 | `sacar()`. Busca a conta, le o valor, chama `Conta.sacar()`, salva e mostra resultado. |
| 233-242 | `mostrarContaPorNumero()`. Reaproveita a busca de conta para consulta de saldo e busca direta. |
| 244-246 | `consultarSaldo()`. Usa `mostrarContaPorNumero()` para evitar codigo repetido. |
| 248-256 | `listarContas()`. Mostra todas as contas ou avisa que nao ha nenhuma. |
| 258-260 | `buscarConta()`. Tambem reaproveita `mostrarContaPorNumero()`. |
| 262-305 | `transferir()`. Valida quantidade de contas, le origem/destino/valor, chama `Banco.transferir()` e trata cada resultado. |
| 307-326 | `gerarExtrato()`. Busca a conta e imprime cada `Transacao` formatada. |
| 327 | Fecha a classe. |

O que foi estudado aqui:

- menu de terminal;
- `Scanner`;
- `try/catch`;
- `switch` moderno;
- separacao entre interface de terminal, dominio e persistencia;
- tratamento de entradas invalidas;
- carregamento inicial e salvamento apos operacoes.

---

### `src/main/java/app/MigracaoCsvParaJdbcMain.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara o pacote `app`. |
| 3 | Importa `Path`, usado para apontar para arquivos CSV. |
| 5-11 | Importam classes de conexao, repositories e migrador. |
| 13 | Declara a classe responsavel por executar a migracao. |
| 15 | Declara o metodo `main` da migracao. |
| 16-19 | Abre o bloco `try`, cria conexao SQLite, inicializador e cria tabelas. |
| 21-22 | Cria repositories CSV apontando para `data/contas.csv` e `data/transacoes.csv`. |
| 24-25 | Cria repositories JDBC para gravar no SQLite. |
| 27-31 | Monta o `MigradorCsvParaJdbc` com os quatro repositories. |
| 33 | Executa a migracao. |
| 35 | Mostra mensagem de sucesso. |
| 36-39 | Captura erro, mostra mensagem e imprime stack trace para diagnostico. |
| 40-41 | Fecham metodo e classe. |

O que foi estudado aqui:

- programa auxiliar com `main` proprio;
- migracao de dados;
- composicao de objetos;
- leitura de CSV e escrita em SQLite.

---

### `src/main/java/model/Conta.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara o pacote `model`. |
| 3 | Importa `ArrayList`, usado para guardar extrato. |
| 5 | Declara a classe `Conta`. |
| 6-9 | Campos da conta: extrato, titular, saldo e numero. |
| 11-16 | Construtor completo. Recebe numero, titular e saldo inicial; cria extrato vazio. |
| 18-20 | Construtor simplificado. Cria conta com saldo inicial `0.0`. |
| 22-24 | `getExtrato()`. Retorna copia da lista para proteger o extrato interno. |
| 26-36 | Getters de numero, titular e saldo. |
| 38-41 | `registrarOperacao()`. Cria uma `Transacao` e adiciona ao extrato. |
| 43-45 | `adicionarTransacao()`. Usado ao carregar transacoes vindas de CSV ou SQLite. |
| 47-55 | `depositar()`. Rejeita valor invalido, soma no saldo e registra deposito no extrato. |
| 57-63 | `creditarSemExtrato()`. Soma saldo sem registrar extrato, usado na transferencia recebida. |
| 65-75 | `debitarSemExtrato()`. Subtrai saldo sem registrar extrato, usado na transferencia enviada. |
| 77-88 | `sacar()`. Rejeita valor invalido ou saldo insuficiente; se der certo, subtrai e registra saque. |
| 89 | Fecha a classe. |

O que foi estudado aqui:

- encapsulamento;
- construtores;
- retorno defensivo de listas;
- regra de deposito;
- regra de saque;
- separacao entre mudar saldo e registrar extrato.

---

### `src/main/java/model/Banco.java`

| Linhas | Explicacao |
|---|---|
| 1-3 | Declaram pacote e importam `ArrayList`. |
| 5-10 | Declaram a classe e inicializam a lista de contas. |
| 12-20 | `buscarContaPorNumero()`. Percorre as contas e retorna a conta encontrada ou `null`. |
| 22-29 | `adicionarConta()`. Impede numero repetido e retorna `true` ou `false`. |
| 31-37 | Metodos de consulta: verifica se esta vazio e conta quantas contas existem. |
| 39-41 | `listarContas()`. Retorna copia da lista para proteger a lista interna. |
| 43-45 | Inicio da transferencia. Busca conta de origem e destino. |
| 47-61 | Validacoes: origem inexistente, destino inexistente, contas iguais e valor invalido. |
| 63-67 | Tenta debitar da origem; se nao conseguir, retorna saldo insuficiente. |
| 69 | Credita o valor no destino. |
| 71-79 | Registra no extrato da origem e do destino. |
| 81 | Retorna sucesso. |
| 82-83 | Fecham metodo e classe. |

O que foi estudado aqui:

- colecoes com `ArrayList`;
- busca linear;
- validacao de regra de negocio;
- enum como resultado de operacao;
- transferencia como operacao coordenada entre duas contas.

---

### `src/main/java/model/Transacao.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara pacote `model`. |
| 3-4 | Importam data/hora e formatador. |
| 6-10 | Declaram classe e campos imutaveis da transacao. |
| 12-14 | Construtor que usa `LocalDateTime.now()` para operacoes novas. |
| 16-21 | Construtor completo, usado principalmente ao carregar dados persistidos. |
| 23-28 | `formatarParaExtrato()`. Formata data, valor e descricao para mostrar no terminal. |
| 30-44 | Getters dos campos. |
| 45 | Fecha a classe. |

O que foi estudado aqui:

- `LocalDateTime`;
- `DateTimeFormatter`;
- imutabilidade com campos `final`;
- formatacao de texto para extrato.

---

### `src/main/java/model/TipoOperacao.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara pacote `model`. |
| 3-7 | Declara os tipos de operacao possiveis e a descricao amigavel de cada um. |
| 9 | Campo que guarda a descricao. |
| 11-13 | Construtor do enum. Cada valor recebe sua descricao. |
| 15-17 | Getter da descricao. |
| 18 | Fecha o enum. |

O que foi estudado aqui:

- enums em Java;
- associar texto amigavel a valores internos;
- evitar strings soltas para tipos de operacao.

---

### `src/main/java/model/ResultadoTransferencia.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara pacote `model`. |
| 3-9 | Lista todos os resultados possiveis de uma transferencia. |
| 10-11 | Fecham o enum. |

O que foi estudado aqui:

- enum para representar resultado de regra;
- substituicao de `boolean` por retorno mais explicativo;
- comunicacao clara entre `Banco` e `AplicacaoBancaria`.

---

### `src/main/java/persistence/ConexaoBanco.java`

| Linhas | Explicacao |
|---|---|
| 1 | Declara pacote `persistence`. |
| 3-5 | Importam classes JDBC. |
| 7 | Declara classe de conexao. |
| 9 | Define URL padrao do SQLite: `jdbc:sqlite:data/banco.db`. |
| 11 | Campo que guarda a URL usada pela instancia. |
| 13-15 | Construtor padrao, usando a URL principal da aplicacao. |
| 17-19 | Construtor alternativo, usado principalmente nos testes com bancos temporarios. |
| 21-23 | Abre e retorna uma `Connection` usando `DriverManager`. |
| 24 | Fecha a classe. |

O que foi estudado aqui:

- JDBC;
- `Connection`;
- `DriverManager`;
- URL de conexao;
- injetar URL diferente para testes.

---

### `src/main/java/persistence/InicializadorBanco.java`

| Linhas | Explicacao |
|---|---|
| 1-5 | Declaram pacote e imports de JDBC. |
| 7-13 | Declaram classe, dependencia `ConexaoBanco` e construtor. |
| 15-18 | Abrem conexao e `Statement` com `try-with-resources`. |
| 19-25 | Criam a tabela `contas`, se ela ainda nao existir. |
| 27-37 | Criam a tabela `transacoes`, com chave primaria, chave estrangeira e campos do extrato. |
| 38-40 | Fecham recursos, metodo e classe. |

O que foi estudado aqui:

- `CREATE TABLE IF NOT EXISTS`;
- schema de banco;
- chave primaria;
- chave estrangeira;
- `try-with-resources`.

---

### `src/main/java/persistence/ContaRepositoryJdbc.java`

| Linhas | Explicacao |
|---|---|
| 1-10 | Declaram pacote, imports JDBC, colecoes e `Conta`. |
| 12-18 | Declaram classe, dependencia de conexao e construtor. |
| 20-24 | `salvar(conta)`. Abre conexao propria e delega para a versao que recebe `Connection`. |
| 26-33 | SQL de `INSERT` com `ON CONFLICT`, usado para inserir ou atualizar conta pelo numero. |
| 35-41 | Preenche `PreparedStatement` com numero, titular e saldo; executa o comando. |
| 44-64 | `carregarTodas()`. Faz `SELECT`, percorre `ResultSet` e cria objetos `Conta`. |
| 66-86 | `buscarPorNumero()`. Usa `WHERE numero = ?` e retorna a conta ou `null`. |
| 87 | Fecha a classe. |

O que foi estudado aqui:

- repository;
- `PreparedStatement`;
- `ResultSet`;
- `INSERT`;
- `SELECT`;
- `ON CONFLICT`;
- transformar linha do banco em objeto Java.

---

### `src/main/java/persistence/TransacaoRepositoryJdbc.java`

| Linhas | Explicacao |
|---|---|
| 1-13 | Declaram pacote, imports JDBC, data/hora, listas e classes do dominio. |
| 15-21 | Declaram classe, conexao e construtor. |
| 23-27 | `salvar()` com conexao propria. |
| 29-33 | SQL para inserir transacao com `numero_conta`, tipo, valor, data/hora e descricao. |
| 35-43 | Preenche `PreparedStatement` e executa o `INSERT`. |
| 46-55 | Monta consulta para carregar transacoes de uma conta, ordenadas por data/hora. |
| 56-75 | Executa o `SELECT`, converte texto para enum/data e cria objetos `Transacao`. |
| 78-82 | `salvarTodasDaConta()` com conexao propria. |
| 84-90 | Apaga transacoes antigas da conta e salva novamente o extrato atual. |
| 92-96 | `apagarPorConta()` com conexao propria. |
| 98-105 | Executa `DELETE FROM transacoes WHERE numero_conta = ?`. |
| 106 | Fecha a classe. |

O que foi estudado aqui:

- relacionamento entre conta e transacao;
- salvar enum como texto;
- salvar `LocalDateTime` como string ISO;
- carregar lista de objetos com `ResultSet`;
- substituir extrato antigo pelo atual.

---

### `src/main/java/persistence/PersistenciaBancoService.java`

| Linhas | Explicacao |
|---|---|
| 1-7 | Declaram pacote, imports e `Conta`. |
| 9-13 | Declaram classe e dependencias JDBC. |
| 15-22 | Construtor recebe conexao e repositories. |
| 24 | Metodo `salvarEstado()`, que recebe todas as contas em memoria. |
| 25-26 | Abre conexao e desativa `autoCommit`. |
| 28-32 | Para cada conta, salva a conta e suas transacoes usando a mesma conexao. |
| 34 | Executa `commit` quando tudo termina sem erro. |
| 35-40 | Executa `rollback` se ocorrer `SQLException` ou `RuntimeException`. |
| 41-43 | Reativa `autoCommit` no `finally`. |
| 44-46 | Fecham conexao, metodo e classe. |

O que foi estudado aqui:

- transacao SQL;
- atomicidade;
- `commit`;
- `rollback`;
- por que salvar conta e extrato juntos;
- reutilizar a mesma `Connection` em varias operacoes.

---

### `src/main/java/persistence/ContaRepositoryCsv.java`

| Linhas | Explicacao |
|---|---|
| 1-9 | Declaram pacote, imports de arquivos/listas e `Conta`. |
| 11-17 | Declaram classe, caminho do arquivo e construtor. |
| 19-29 | `salvar()`. Converte cada conta em linha CSV, cria pasta e grava arquivo. |
| 31-52 | `carregar()`. Se arquivo nao existe, retorna lista vazia; se existe, le linhas validas. |
| 54-60 | Cria pasta do arquivo quando necessario. |
| 62-64 | Converte `Conta` para linha `numero;titular;saldo`. |
| 66-78 | Converte linha CSV em objeto `Conta`; rejeita linha com formato errado. |
| 79 | Fecha a classe. |

O que foi estudado aqui:

- `Files`;
- `Path`;
- persistencia em arquivo;
- parsing simples de CSV com `split`;
- tratamento de linhas invalidas.

---

### `src/main/java/persistence/TransacaoRepositoryCsv.java`

| Linhas | Explicacao |
|---|---|
| 1-12 | Declaram pacote, imports e classes do dominio. |
| 14-20 | Declaram classe, caminho do arquivo e construtor. |
| 22-37 | `carregarLinha()`. Quebra a linha, valida quantidade de campos, cria transacao e associa a conta. |
| 39-46 | Converte partes da linha em `Transacao`. |
| 48-56 | Busca uma conta pelo numero dentro da lista carregada. |
| 58-70 | `salvar()`. Percorre contas e extratos, converte cada transacao em linha e grava arquivo. |
| 72-78 | Cria pasta se necessario. |
| 80-90 | Converte uma transacao em linha CSV com numero da conta, tipo, valor, data/hora e descricao. |
| 92-107 | `carregar()`. Le o arquivo e ignora linhas invalidas sem derrubar o programa. |
| 108 | Fecha a classe. |

O que foi estudado aqui:

- persistencia de relacao conta-transacao em arquivo;
- associar transacao a uma conta;
- `LocalDateTime.parse`;
- CSV como etapa anterior ao banco relacional.

---

### `src/main/java/persistence/MigradorCsvParaJdbc.java`

| Linhas | Explicacao |
|---|---|
| 1-8 | Declaram pacote, imports e classes do dominio. |
| 10-15 | Declaram classe e quatro repositories: dois CSV e dois JDBC. |
| 17-26 | Construtor injeta os repositories. |
| 28-30 | `migrar()`: carrega contas do CSV e depois carrega transacoes dentro dessas contas. |
| 32-38 | Para cada conta carregada, salva a conta no SQLite e salva suas transacoes. |
| 39-40 | Fecham metodo e classe. |

O que foi estudado aqui:

- migracao de dados;
- adaptar um formato antigo para um formato novo;
- separar leitura CSV da escrita JDBC;
- coordenar repositories diferentes.

---

### Testes em `src/test/java/model`

| Arquivo | Linhas | O que valida |
|---|---:|---|
| `ContaTest.java` | 1-112 | Construcao da conta, deposito, saque, saldo, extrato e operacoes invalidas. |
| `BancoTest.java` | 1-178 | Busca de contas, duplicidade, transferencia valida, erros de transferencia e preservacao do extrato. |
| `TransacaoTest.java` | 1-66 | Criacao de transacao, descricao, data/hora e formatacao para extrato. |
| `TipoOperacaoTest.java` | 1-27 | Descricoes amigaveis do enum de operacoes. |
| `ResultadoTransferenciaTest.java` | 1-52 | Existencia de todos os resultados esperados do enum. |

O que foi estudado aqui:

- testes unitarios;
- `assertEquals`, `assertTrue`, `assertFalse`, `assertNull`, `assertNotNull`;
- testar regras antes de depender de interface de terminal;
- proteger comportamento de dominio contra regressao.

---

### Testes em `src/test/java/persistence`

| Arquivo | Linhas | O que valida |
|---|---:|---|
| `ConexaoBancoTest.java` | 1-20 | Se a conexao SQLite pode ser aberta e permanece ativa dentro do bloco de teste. |
| `InicializadorBancoTest.java` | 1-40 | Se as tabelas `contas` e `transacoes` sao criadas no banco. |
| `ContaRepositoryCsvTest.java` | 1-88 | Salvamento, carregamento, arquivo inexistente e linha invalida em CSV de contas. |
| `TransacaoRepositoryCsvTest.java` | 1-84 | Salvamento e carregamento do extrato em CSV, incluindo linha invalida. |
| `ContaRepositoryJdbcTest.java` | 1-104 | Insercao, carregamento e busca de contas em SQLite. |
| `TransacaoRepositoryJdbcTest.java` | 1-149 | Insercao, carregamento, substituicao e ordenacao de transacoes em SQLite. |
| `MigradorCsvParaJdbcTest.java` | 1-73 | Migracao completa de contas e transacoes de CSV para SQLite. |
| `PersistenciaBancoServiceTest.java` | 1-79 | Salvamento de estado em uma transacao SQL e rollback quando uma transacao falha. |

O que foi estudado aqui:

- testes de integracao com banco local em `target/test-data`;
- preparacao com `@BeforeEach`;
- limpeza de arquivos de teste;
- banco SQLite isolado por teste;
- validacao de commit e rollback.

---

## Organizacao da aplicacao

### Camada `app`

| Classe | Responsabilidade |
|---|---|
| `Main` | Ponto de entrada do programa. Cria a aplicacao bancaria e chama `executar()` |
| `AplicacaoBancaria` | Controla o menu, le entradas do terminal, chama as regras de dominio e salva o estado |
| `MigracaoCsvParaJdbcMain` | Executa a migracao de dados antigos em CSV para SQLite |

### Camada `model`

| Classe | Responsabilidade |
|---|---|
| `Banco` | Gerencia a colecao de contas, busca contas e coordena transferencias |
| `Conta` | Representa uma conta bancaria com numero, titular, saldo e extrato |
| `Transacao` | Representa um item do extrato com tipo, valor, data/hora e descricao |
| `TipoOperacao` | Enum com os tipos de operacao registrados no extrato |
| `ResultadoTransferencia` | Enum com os possiveis resultados de uma transferencia |

### Camada `persistence`

| Classe | Responsabilidade |
|---|---|
| `ConexaoBanco` | Abre conexoes JDBC com o banco SQLite |
| `InicializadorBanco` | Cria as tabelas `contas` e `transacoes` se elas ainda nao existirem |
| `ContaRepositoryJdbc` | Salva, atualiza, busca e carrega contas no SQLite |
| `TransacaoRepositoryJdbc` | Salva, carrega e remove transacoes no SQLite |
| `PersistenciaBancoService` | Coordena o salvamento de contas e transacoes em uma unica transacao SQL |
| `MigradorCsvParaJdbc` | Le dados CSV antigos e grava esses dados no SQLite |
| `ContaRepositoryCsv` | Persistencia legada de contas em CSV |
| `TransacaoRepositoryCsv` | Persistencia legada de transacoes em CSV |

---

## Fluxo principal da aplicacao

O fluxo comeca em `Main`.

```text
Main -> AplicacaoBancaria -> executar()
```

Dentro de `AplicacaoBancaria`, o programa prepara os objetos principais:

- `Scanner`, para ler dados do terminal;
- `Banco`, para guardar as contas em memoria;
- `ConexaoBanco`, para abrir conexoes com SQLite;
- `InicializadorBanco`, para criar as tabelas;
- `ContaRepositoryJdbc`, para carregar e salvar contas;
- `TransacaoRepositoryJdbc`, para carregar e salvar transacoes;
- `PersistenciaBancoService`, para salvar o estado completo com controle transacional.

Quando `executar()` e chamado, a aplicacao segue esta ordem:

1. chama `inicializarBanco()`;
2. chama `carregarContas()`;
3. mostra o menu;
4. le a opcao do usuario;
5. executa a operacao escolhida;
6. quando a operacao altera dados com sucesso, chama `salvarEstado()`;
7. continua no menu ate o usuario escolher `0 - Sair`.

Fluxo simplificado:

```text
Terminal
   |
   v
AplicacaoBancaria
   |
   v
Banco / Conta / Transacao
   |
   v
PersistenciaBancoService
   |
   v
Repositories JDBC
   |
   v
SQLite
```

---

## Regras de dominio

### Conta

`Conta` representa uma conta bancaria individual.

Ela guarda:

- `numero`;
- `titular`;
- `saldo`;
- `extrato`.

Principais regras:

- deposito precisa ter valor maior que zero;
- saque precisa ter valor maior que zero;
- saque so acontece se houver saldo suficiente;
- deposito bem-sucedido registra uma transacao de tipo `DEPOSITO`;
- saque bem-sucedido registra uma transacao de tipo `SAQUE`;
- `getExtrato()` devolve uma copia da lista para proteger os dados internos da conta;
- `creditarSemExtrato()` e `debitarSemExtrato()` sao usados pela transferencia para separar a mudanca de saldo do registro textual do extrato.

### Banco

`Banco` representa o conjunto de contas em memoria.

Principais regras:

- nao permite adicionar duas contas com o mesmo numero;
- permite buscar uma conta pelo numero;
- permite listar uma copia das contas cadastradas;
- coordena a transferencia entre contas.

Na transferencia, `Banco` verifica:

- se a conta de origem existe;
- se a conta de destino existe;
- se origem e destino sao contas diferentes;
- se o valor e valido;
- se a conta de origem tem saldo suficiente.

Quando a transferencia da certo:

- debita o valor da conta de origem;
- credita o valor na conta de destino;
- registra `TRANSFERENCIA_ENVIADA` no extrato da origem;
- registra `TRANSFERENCIA_RECEBIDA` no extrato do destino;
- retorna `ResultadoTransferencia.SUCESSO`.

Quando algo impede a transferencia, o metodo retorna um valor de `ResultadoTransferencia`, como:

- `CONTA_ORIGEM_NAO_ENCONTRADA`;
- `CONTA_DESTINO_NAO_ENCONTRADA`;
- `CONTAS_IGUAIS`;
- `VALOR_INVALIDO`;
- `SALDO_INSUFICIENTE`.

### Transacao

`Transacao` representa uma movimentacao exibida no extrato.

Ela guarda:

- tipo da operacao;
- valor;
- data e hora;
- descricao.

O metodo `formatarParaExtrato()` transforma a transacao em texto para mostrar no terminal.

Exemplo conceitual:

```text
15/05/2026 10:30 - Deposito - R$ 150.00 - Deposito realizado
```

---

## Persistencia atual

A persistencia principal do projeto usa SQLite via JDBC.

O banco de dados padrao fica em:

```text
data/banco.db
```

A URL usada por `ConexaoBanco` e:

```text
jdbc:sqlite:data/banco.db
```

Ao iniciar a aplicacao, `InicializadorBanco` executa comandos `CREATE TABLE IF NOT EXISTS`. Isso garante que as tabelas existam antes de carregar ou salvar dados.

As tabelas principais sao:

- `contas`;
- `transacoes`.

### Como as contas sao persistidas

`ContaRepositoryJdbc` salva contas usando `INSERT ... ON CONFLICT`.

Isso significa:

- se a conta ainda nao existe, ela e inserida;
- se a conta ja existe com o mesmo numero, titular e saldo sao atualizados.

Fluxo simplificado:

```text
Conta Java -> PreparedStatement -> tabela contas
```

Ao carregar as contas, `ContaRepositoryJdbc` faz um `SELECT` na tabela `contas` e transforma cada linha em um objeto `Conta`.

### Como as transacoes sao persistidas

`TransacaoRepositoryJdbc` salva as transacoes na tabela `transacoes`.

Cada transacao precisa estar associada a uma conta. Por isso, a tabela guarda a coluna `numero_conta`.

Fluxo simplificado:

```text
Transacao Java + numero da conta -> PreparedStatement -> tabela transacoes
```

Ao carregar o extrato, `TransacaoRepositoryJdbc` busca as transacoes pelo numero da conta:

```sql
SELECT tipo, valor, data_hora, descricao
FROM transacoes
WHERE numero_conta = ?
ORDER BY data_hora;
```

Depois, cada linha do resultado vira um objeto `Transacao`.

### Salvamento coordenado

`PersistenciaBancoService` e responsavel por salvar o estado do banco de forma coordenada.

Ele recebe a lista de contas em memoria e, dentro de uma unica transacao SQL:

1. desativa o `autoCommit`;
2. salva cada conta;
3. apaga as transacoes antigas daquela conta;
4. salva novamente as transacoes atuais do extrato;
5. executa `commit` se tudo der certo;
6. executa `rollback` se alguma parte falhar.

Na pratica:

- `commit` confirma as alteracoes quando tudo e salvo sem erro;
- `rollback` desfaz as alteracoes se algo falhar no meio do processo;
- isso evita deixar o banco com contas atualizadas e extratos incompletos.

Essa logica fica fora de `AplicacaoBancaria` para manter a aplicacao de terminal mais simples e para concentrar o controle transacional em uma classe propria.

---

## Migracao CSV para SQLite

Antes da persistencia atual em SQLite, o projeto usava arquivos CSV.

Os repositories CSV ainda existem para permitir migrar dados antigos:

- `ContaRepositoryCsv`;
- `TransacaoRepositoryCsv`.

O fluxo de migracao fica em `MigradorCsvParaJdbc`.

Ele faz:

1. le as contas em `data/contas.csv`;
2. le as transacoes em `data/transacoes.csv`;
3. associa as transacoes as contas carregadas;
4. salva as contas no SQLite;
5. salva as transacoes no SQLite.

Para executar:

```bash
mvn exec:java -Dexec.mainClass=app.MigracaoCsvParaJdbcMain
```

Essa migracao e util quando ja existem dados antigos em CSV e o objetivo e leva-los para o banco `data/banco.db`.

---

## Modelo conceitual do banco de dados

O modelo conceitual organiza os dados do simulador bancario em tabelas relacionais.

No projeto atual, esse modelo ja foi aplicado em SQLite. As tabelas principais reais sao:

- `contas`;
- `transacoes`.

### O que e um banco de dados relacional

Um banco de dados relacional guarda dados em tabelas.

Ele e chamado de relacional porque as tabelas podem se relacionar entre si.

No simulador bancario:

- uma conta bancaria fica na tabela `contas`;
- as transacoes do extrato ficam na tabela `transacoes`;
- cada transacao indica a qual conta pertence.

### O que e uma tabela

Uma tabela e uma estrutura parecida com uma planilha.

Ela possui:

- colunas, que representam os campos guardados;
- linhas, que representam os registros salvos.

Exemplo simples:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |
| 2 | Maria | 80.0 |

Nesse exemplo, `numero`, `titular` e `saldo` sao colunas. Cada conta e uma linha.

### Tabela `contas`

A tabela `contas` representa o estado atual das contas bancarias.

Cada linha corresponde a uma conta do sistema.

| Coluna | Tipo conceitual | Significado |
|---|---|---|
| `numero` | `INTEGER` | Identifica a conta de forma unica |
| `titular` | `TEXT` | Guarda o nome do titular |
| `saldo` | `REAL` | Guarda o saldo atual |

O campo `numero` identifica a conta. Ele precisa ser unico para que o sistema consiga buscar uma conta com seguranca.

O campo `titular` guarda o nome do dono da conta.

O campo `saldo` guarda o valor disponivel naquele momento.

Para aprendizado inicial, `REAL` e suficiente para representar saldo. Em sistemas financeiros reais, dinheiro costuma exigir tipos mais controlados, como `DECIMAL`, para reduzir problemas de precisao.

Relacao com Java:

| Banco SQLite | Java |
|---|---|
| `contas.numero` | `Conta.numero` |
| `contas.titular` | `Conta.titular` |
| `contas.saldo` | `Conta.saldo` |

Cada linha da tabela `contas` pode ser transformada em um objeto `Conta`.

### Tabela `transacoes`

A tabela `transacoes` representa o historico do extrato das contas.

Cada linha corresponde a uma transacao.

| Coluna | Tipo conceitual | Significado |
|---|---|---|
| `id` | `INTEGER` | Identifica cada transacao de forma unica |
| `numero_conta` | `INTEGER` | Indica a qual conta a transacao pertence |
| `tipo` | `TEXT` | Representa o tipo da operacao |
| `valor` | `REAL` | Guarda o valor movimentado |
| `data_hora` | `TEXT` | Guarda quando a transacao aconteceu |
| `descricao` | `TEXT` | Guarda uma descricao textual da operacao |

O campo `id` existe porque uma mesma conta pode ter varias transacoes. O `numero_conta` identifica a conta, mas nao identifica sozinho uma transacao especifica.

Quando o `id` e autoincremental, cada nova transacao recebe automaticamente o proximo numero disponivel.

O campo `numero_conta` aponta para uma conta existente.

O campo `tipo` se relaciona com o enum `TipoOperacao`, que possui valores como:

- `DEPOSITO`;
- `SAQUE`;
- `TRANSFERENCIA_ENVIADA`;
- `TRANSFERENCIA_RECEBIDA`.

No banco, esses valores sao salvos como texto.

O campo `data_hora` se relaciona com `LocalDateTime`. No banco, a data e hora sao salvas como texto em formato ISO.

Exemplo:

```text
2026-05-15T10:30
```

Relacao com Java:

| Banco SQLite | Java |
|---|---|
| `transacoes.tipo` | `Transacao.tipo` |
| `transacoes.valor` | `Transacao.valor` |
| `transacoes.data_hora` | `Transacao.dataHora` |
| `transacoes.descricao` | `Transacao.descricao` |

`numero_conta` nao precisa estar dentro da classe `Transacao` hoje porque, no Java atual, a transacao fica dentro do extrato de uma `Conta`. No banco, esse campo e necessario para saber a qual conta aquela transacao pertence.

Exemplo visual:

| id | numero_conta | tipo | valor | data_hora | descricao |
|---|---|---|---|---|---|
| 1 | 1 | DEPOSITO | 200.0 | 2026-05-15T10:30 | Deposito realizado |
| 2 | 1 | SAQUE | 50.0 | 2026-05-15T11:00 | Saque realizado |

### Chave primaria

Chave primaria e a coluna que identifica uma linha de forma unica dentro de uma tabela.

Na tabela `contas`, a chave primaria e:

```text
numero
```

Isso significa que nao podem existir duas contas com o mesmo numero.

Na tabela `transacoes`, a chave primaria e:

```text
id
```

Isso significa que cada transacao tem um identificador proprio.

### Chave estrangeira

Chave estrangeira e uma coluna que aponta para a chave primaria de outra tabela.

Na tabela `transacoes`, a coluna:

```text
numero_conta
```

aponta para:

```text
contas.numero
```

Isso significa que cada transacao pertence a uma conta existente.

### Relacao entre `contas` e `transacoes`

Uma conta pode ter varias transacoes.

Cada transacao pertence a uma unica conta.

Isso e uma relacao de 1 para muitos.

Desenho conceitual:

```text
contas.numero -> transacoes.numero_conta
```

Nesse relacionamento:

- `contas.numero` e a chave primaria da tabela `contas`;
- `transacoes.numero_conta` e a chave estrangeira da tabela `transacoes`;
- a chave primaria identifica uma linha unica;
- a chave estrangeira aponta para uma linha existente em outra tabela.

Exemplo:

Tabela `contas`:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |

Tabela `transacoes`:

| id | numero_conta | tipo | valor |
|---|---|---|---|
| 1 | 1 | DEPOSITO | 200.0 |
| 2 | 1 | SAQUE | 50.0 |

As duas transacoes pertencem a conta de numero `1`.

### Por que usar duas tabelas

`contas` e `transacoes` representam conceitos diferentes.

- conta representa estado atual;
- transacao representa historico.

Seria ruim guardar tudo em uma unica tabela porque:

- repetiria `titular` e `saldo` em varias linhas;
- misturaria estado atual com historico;
- dificultaria organizar o extrato.

Exemplo ruim de tabela unica:

| numero | titular | saldo | tipo | valor |
|---|---|---|---|---|
| 1 | Gabriel | 150.0 | DEPOSITO | 200.0 |
| 1 | Gabriel | 150.0 | SAQUE | 50.0 |

Separar em duas tabelas deixa o modelo mais limpo e reflete melhor a implementacao em Java.

### SQL usado para criar as tabelas

A estrutura usada pelo projeto pode ser representada assim:

```sql
CREATE TABLE contas (
    numero INTEGER PRIMARY KEY,
    titular TEXT NOT NULL,
    saldo REAL NOT NULL
);

CREATE TABLE transacoes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_conta INTEGER NOT NULL,
    tipo TEXT NOT NULL,
    valor REAL NOT NULL,
    data_hora TEXT NOT NULL,
    descricao TEXT NOT NULL,
    FOREIGN KEY (numero_conta) REFERENCES contas(numero)
);
```

Significado dos principais termos:

- `CREATE TABLE` cria uma tabela;
- `INTEGER` representa numero inteiro;
- `TEXT` representa texto;
- `REAL` representa numero com casas decimais;
- `PRIMARY KEY` define a chave primaria;
- `AUTOINCREMENT` indica que o banco pode gerar o proximo numero automaticamente;
- `NOT NULL` indica que o campo e obrigatorio;
- `FOREIGN KEY` define uma relacao com outra tabela;
- `REFERENCES contas(numero)` indica que `numero_conta` aponta para uma conta existente.

---

## Primeiros comandos SQL

### `SELECT`

O comando `SELECT` e usado para consultar dados em uma tabela.

Exemplo:

```sql
SELECT * FROM contas;
```

Esse comando busca todas as colunas de todas as contas.

Neste exemplo:

- `SELECT` indica que queremos consultar dados;
- `*` significa todas as colunas;
- `FROM contas` indica que os dados vem da tabela `contas`.

Outro exemplo:

```sql
SELECT numero, titular FROM contas;
```

Esse comando busca apenas o numero e o titular das contas.

### `WHERE`

O comando `WHERE` e usado para filtrar os resultados de uma consulta.

Exemplo:

```sql
SELECT * FROM contas WHERE numero = 1;
```

Esse comando busca apenas a conta cujo numero e `1`.

Outro exemplo:

```sql
SELECT * FROM transacoes WHERE numero_conta = 1;
```

Esse comando busca apenas as transacoes que pertencem a conta numero `1`.

### `INSERT`

O comando `INSERT` e usado para inserir uma nova linha em uma tabela.

Exemplo na tabela `contas`:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (1, 'Gabriel', 150.0);
```

Esse comando cria uma nova conta com numero `1`, titular `Gabriel` e saldo `150.0`.

Exemplo na tabela `transacoes`:

```sql
INSERT INTO transacoes (numero_conta, tipo, valor, data_hora, descricao)
VALUES (1, 'DEPOSITO', 150.0, '2026-05-15T10:30', 'Deposito realizado');
```

Nesse exemplo, o `id` nao e informado porque ele e gerado automaticamente pelo banco.

### `UPDATE`

O comando `UPDATE` e usado para alterar dados existentes.

Exemplo:

```sql
UPDATE contas
SET saldo = 200.0
WHERE numero = 1;
```

Esse comando altera o saldo da conta numero `1` para `200.0`.

O `WHERE` e muito importante em um `UPDATE`. Sem ele, todas as linhas da tabela poderiam ser alteradas.

### `DELETE`

O comando `DELETE` e usado para remover linhas de uma tabela.

Exemplo:

```sql
DELETE FROM transacoes
WHERE id = 1;
```

Esse comando remove a transacao cujo `id` e `1`.

Assim como no `UPDATE`, o `WHERE` e muito importante. Sem ele, todas as linhas da tabela poderiam ser removidas.

### Consultas uteis para o projeto

Buscar uma conta pelo numero:

```sql
SELECT * FROM contas WHERE numero = 1;
```

Listar todas as contas:

```sql
SELECT * FROM contas;
```

Listar o extrato de uma conta:

```sql
SELECT * FROM transacoes WHERE numero_conta = 1;
```

Listar o extrato de uma conta em ordem de data:

```sql
SELECT * FROM transacoes
WHERE numero_conta = 1
ORDER BY data_hora;
```

Buscar todas as transacoes de deposito:

```sql
SELECT * FROM transacoes
WHERE tipo = 'DEPOSITO';
```

---

## Conceitos iniciais de JDBC

JDBC significa Java Database Connectivity.

Ele e o recurso usado em Java para conectar uma aplicacao a um banco de dados relacional.

Neste projeto, JDBC foi aplicado para substituir a persistencia principal em CSV por SQLite.

A ideia geral e:

```text
Aplicacao Java -> JDBC -> Banco de dados
```

### Por que JDBC existe

O Java, sozinho, nao grava dados diretamente em um banco relacional.

Ele precisa de uma forma padronizada para:

- abrir conexao com o banco;
- enviar comandos SQL;
- receber resultados;
- tratar erros;
- fechar recursos.

JDBC fornece esse caminho.

### Driver JDBC

Um driver JDBC e uma biblioteca que permite que o Java converse com um banco de dados especifico.

Exemplos:

- SQLite usa um driver JDBC proprio;
- MySQL usa um driver JDBC proprio;
- PostgreSQL usa um driver JDBC proprio.

Sem o driver, o Java conhece a ideia de JDBC, mas nao sabe se comunicar com o banco escolhido.

No projeto atual, o driver SQLite esta configurado como dependencia Maven.

### URL de conexao

A URL de conexao e o endereco usado pelo Java para encontrar o banco.

Ela informa:

- qual banco sera usado;
- onde o banco esta;
- em alguns casos, o nome do arquivo ou servidor.

Exemplo usado no projeto:

```text
jdbc:sqlite:data/banco.db
```

Significado:

- `jdbc` indica que sera usada uma conexao JDBC;
- `sqlite` indica o tipo de banco;
- `data/banco.db` indica o arquivo onde o banco SQLite fica salvo.

### `Connection`

`Connection` representa uma conexao aberta entre a aplicacao Java e o banco de dados.

Com uma `Connection`, a aplicacao pode preparar e executar comandos SQL.

Exemplo conceitual:

```java
Connection conexao = DriverManager.getConnection(url);
```

Na implementacao atual, esse e o fluxo:

```text
pegar a URL do banco
abrir conexao
usar a conexao
fechar conexao
```

### `DriverManager`

`DriverManager` e uma classe do Java usada para abrir conexoes JDBC.

Ela recebe a URL de conexao e devolve uma `Connection`.

Fluxo conceitual:

```text
DriverManager + URL -> Connection
```

Exemplo:

```java
DriverManager.getConnection("jdbc:sqlite:data/banco.db");
```

### `Statement`

`Statement` e um objeto usado para executar comandos SQL simples.

Exemplo conceitual:

```java
Statement statement = conexao.createStatement();
statement.execute("CREATE TABLE contas (...)");
```

Ele pode executar comandos como:

- `CREATE TABLE`;
- `SELECT`;
- `INSERT`;
- `UPDATE`;
- `DELETE`.

Para comandos com valores vindos do usuario, `Statement` nao e a opcao mais segura.

### `PreparedStatement`

`PreparedStatement` e uma forma mais segura e organizada de executar SQL com valores variaveis.

Em vez de montar SQL juntando texto, usamos espacos reservados com `?`.

Exemplo:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (?, ?, ?);
```

Depois o Java preenche os valores:

```text
? 1 -> numero
? 2 -> titular
? 3 -> saldo
```

Vantagens:

- evita montagem perigosa de strings;
- reduz risco de SQL Injection;
- deixa o codigo mais organizado;
- ajuda o banco a entender melhor o comando.

No projeto, `PreparedStatement` e usado para salvar e carregar contas e transacoes.

### SQL Injection

SQL Injection e um problema de seguranca que acontece quando valores digitados pelo usuario sao colocados diretamente dentro de comandos SQL.

Exemplo ruim:

```java
"SELECT * FROM contas WHERE titular = '" + nomeDigitado + "'"
```

Se o usuario digitar algo malicioso, ele pode alterar o sentido do SQL.

Com `PreparedStatement`, os valores sao tratados como dados, nao como pedacos do comando.

Mesmo em um projeto educacional, e melhor aprender `PreparedStatement` desde cedo.

### `ResultSet`

`ResultSet` representa o resultado de uma consulta `SELECT`.

Quando voce faz:

```sql
SELECT * FROM contas;
```

o banco retorna linhas.

No Java, essas linhas aparecem dentro de um `ResultSet`.

Fluxo conceitual:

```text
SELECT -> ResultSet -> objetos Java
```

Exemplo:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |
| 2 | Maria | 80.0 |

O `ResultSet` permite percorrer essas linhas uma por vez e transformar cada uma em objeto `Conta`.

### `SQLException`

`SQLException` e a excecao usada pelo Java para indicar problemas ao trabalhar com banco de dados.

Exemplos:

- banco nao encontrado;
- SQL escrito errado;
- tabela nao existe;
- conexao falhou;
- valor invalido;
- violacao de chave primaria;
- violacao de chave estrangeira.

Conceitualmente, qualquer operacao JDBC pode falhar. Por isso, o codigo precisa lidar com `SQLException`.

### Fechamento de recursos

Conexoes e comandos JDBC usam recursos externos.

Por isso, precisam ser fechados apos o uso.

Recursos comuns:

- `Connection`;
- `Statement`;
- `PreparedStatement`;
- `ResultSet`.

Em Java, normalmente usamos `try-with-resources`.

Exemplo:

```java
try (Connection conexao = DriverManager.getConnection(url)) {
    // usar conexao
}
```

Quando o bloco termina, o Java fecha a conexao automaticamente.

### Transacao de banco de dados

Uma transacao de banco de dados e um conjunto de operacoes que deve ser tratado como uma unidade.

No simulador bancario, uma transferencia envolve varias mudancas:

- atualizar saldo da conta de origem;
- atualizar saldo da conta de destino;
- registrar uma transacao enviada;
- registrar uma transacao recebida.

Essas operacoes devem acontecer juntas. Se uma falhar, todas devem ser desfeitas.

Conceitos importantes:

- `commit` confirma as alteracoes;
- `rollback` desfaz as alteracoes quando ocorre erro;
- atomicidade significa que tudo acontece ou nada acontece.

No projeto, `PersistenciaBancoService` aplica esse conceito ao salvar contas e transacoes na mesma transacao SQL.

### Repository JDBC

Um repository JDBC e uma classe responsavel por transformar objetos Java em comandos SQL e transformar resultados SQL de volta em objetos Java.

Responsabilidades:

- receber objetos Java;
- preparar comandos SQL;
- executar comandos no banco;
- ler resultados do banco;
- criar objetos Java a partir das linhas retornadas.

No projeto:

- `ContaRepositoryJdbc` trabalha com a tabela `contas`;
- `TransacaoRepositoryJdbc` trabalha com a tabela `transacoes`;
- os repositories CSV continuam apenas como legado e apoio para migracao.

### Como uma `Conta` vira SQL

Objeto Java:

```text
Conta(numero=1, titular=Gabriel, saldo=150.0)
```

Linha no banco:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |

SQL conceitual:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (1, 'Gabriel', 150.0);
```

Com `PreparedStatement`, a ideia e:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (?, ?, ?);
```

E o Java preenche:

```text
1 -> numero
Gabriel -> titular
150.0 -> saldo
```

### Como uma linha do banco vira `Conta`

Linha no banco:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |

Objeto Java:

```text
new Conta(1, "Gabriel", 150.0)
```

Fluxo conceitual:

```text
ResultSet -> ler numero, titular, saldo -> criar Conta
```

### Como uma `Transacao` vira SQL

Objeto Java:

```text
Transacao(tipo=DEPOSITO, valor=150.0, dataHora=2026-05-15T10:30, descricao=Deposito realizado)
```

Linha no banco:

| id | numero_conta | tipo | valor | data_hora | descricao |
|---|---|---|---|---|---|
| 1 | 1 | DEPOSITO | 150.0 | 2026-05-15T10:30 | Deposito realizado |

O `id` e gerado automaticamente pelo banco.

SQL conceitual:

```sql
INSERT INTO transacoes (numero_conta, tipo, valor, data_hora, descricao)
VALUES (1, 'DEPOSITO', 150.0, '2026-05-15T10:30', 'Deposito realizado');
```

### Como uma linha do banco vira `Transacao`

Linha no banco:

| tipo | valor | data_hora | descricao |
|---|---|---|---|
| DEPOSITO | 150.0 | 2026-05-15T10:30 | Deposito realizado |

Objeto Java:

```java
new Transacao(
    TipoOperacao.DEPOSITO,
    150.0,
    LocalDateTime.parse("2026-05-15T10:30"),
    "Deposito realizado"
)
```

Fluxo conceitual:

```text
ResultSet -> ler tipo, valor, data_hora, descricao -> criar Transacao
```

---

## Plano de integracao JDBC

Este projeto evoluiu em etapas. A persistencia principal ja esta em SQLite/JDBC, mas o caminho foi organizado para evitar trocar tudo de uma vez.

### O que ja existe em JDBC

Ja foram criadas as principais classes para trabalhar com SQLite:

- `ConexaoBanco`;
- `InicializadorBanco`;
- `ContaRepositoryJdbc`;
- `TransacaoRepositoryJdbc`;
- `PersistenciaBancoService`;
- `MigradorCsvParaJdbc`.

Tambem existem testes automatizados cobrindo:

- abertura de conexao;
- criacao das tabelas;
- salvamento e carregamento de contas;
- salvamento e carregamento de transacoes;
- migracao CSV para SQLite;
- salvamento coordenado com transacao SQL.

### O que ainda usa CSV

A aplicacao principal nao depende mais de CSV para persistir dados.

CSV permanece como etapa anterior do projeto e como suporte para migrar dados antigos.

Os arquivos CSV podem existir localmente em `data/`, mas essa pasta e ignorada pelo Git.

### Por que nao trocar tudo de uma vez

Trocar CSV por JDBC diretamente poderia misturar varias mudancas ao mesmo tempo:

- mudanca de infraestrutura;
- mudanca no carregamento inicial;
- mudanca no salvamento apos operacoes;
- alteracao no tratamento de erros;
- risco de quebrar a aplicacao de terminal.

Por isso, a integracao foi feita em etapas pequenas e testaveis.

### Etapas concluidas

- Adicionar o driver SQLite JDBC ao `pom.xml`.
- Criar `ConexaoBanco`.
- Criar `InicializadorBanco`.
- Criar `ContaRepositoryJdbc`.
- Testar salvar e carregar contas.
- Criar `TransacaoRepositoryJdbc`.
- Testar salvar e carregar transacoes.
- Criar migracao CSV para SQLite com `MigradorCsvParaJdbc`.
- Criar `MigracaoCsvParaJdbcMain`.
- Integrar contas e transacoes na `AplicacaoBancaria`.
- Criar `PersistenciaBancoService` para salvar contas e transacoes em uma unica transacao SQL.
- Manter CSV apenas como legado e suporte para migracao.

Ordem conceitual usada:

```text
CSV legado
   |
   v
Modelo relacional
   |
   v
SQLite + JDBC
   |
   v
Repositories JDBC
   |
   v
AplicacaoBancaria integrada ao banco
```

---

## Testes automatizados

Os testes ficam em `src/test/java`, usam JUnit 5 e sao executados pelo Maven Surefire.

Para rodar todos os testes:

```bash
mvn test
```

Na verificacao atual, o Maven executa 55 testes automatizados.

Avisos do SQLite sobre acesso nativo ou mensagens do SLF4J sobre logger NOP nao indicam falha se a execucao terminar com `BUILD SUCCESS`.

Classes de teste atuais:

| Classe | O que valida |
|---|---|
| `ContaTest` | Depositos, saques, saldo e registro de extrato |
| `BancoTest` | Busca de contas e transferencias entre contas |
| `TransacaoTest` | Criacao de transacoes, dados registrados e formatacao para extrato |
| `TipoOperacaoTest` | Descricoes amigaveis dos tipos de operacao |
| `ResultadoTransferenciaTest` | Valores esperados do enum de resultado da transferencia |
| `ContaRepositoryJdbcTest` | Salvamento e carregamento de contas em SQLite |
| `TransacaoRepositoryJdbcTest` | Salvamento e carregamento de transacoes em SQLite |
| `ContaRepositoryCsvTest` | Persistencia legada de contas em CSV |
| `TransacaoRepositoryCsvTest` | Persistencia legada de transacoes em CSV |
| `ConexaoBancoTest` | Configuracao e abertura da conexao SQLite |
| `InicializadorBancoTest` | Criacao e inicializacao das tabelas |
| `MigradorCsvParaJdbcTest` | Migracao de dados CSV para SQLite |
| `PersistenciaBancoServiceTest` | Salvamento coordenado de contas e transacoes em uma transacao SQL |

Esses testes ajudam a garantir que a evolucao da persistencia nao quebre as regras principais do projeto.

---

## Conceitos praticados

### Conceitos da fase atual

- Persistencia com JDBC e SQLite.
- Driver JDBC.
- URL de conexao.
- `Connection`.
- `DriverManager`.
- `Statement`.
- `PreparedStatement`.
- `ResultSet`.
- `SQLException`.
- Fechamento de recursos com `try-with-resources`.
- Transacoes SQL com `commit` e `rollback`.
- Schema de banco de dados.
- Chave primaria.
- Chave estrangeira.
- Relacao de 1 para muitos.
- Repositories JDBC.
- Migracao CSV para SQLite.
- Separacao entre dominio, aplicacao de terminal e persistencia.

### Conceitos das fases anteriores

- Java puro.
- Aplicacao de terminal com `Scanner`.
- `ArrayList`.
- Programacao orientada a objetos.
- Encapsulamento.
- Enums.
- Maven.
- JUnit 5.
- Persistencia CSV com `Files`, `Path` e arquivos locais.
- Registro de data e hora com `LocalDateTime`.
- Formatacao de dados para exibicao no terminal.
- Tratamento de entradas invalidas com `try/catch`.
- Retorno defensivo de listas para proteger dados internos.
- Estrutura de projeto Java com Maven.
- Testes automatizados.

---

## Linha do tempo dos commits

Esta linha do tempo foi montada olhando o historico do Git desde o primeiro commit do simulador ate o commit mais recente do repositorio atual.

O historico tem duas fases:

1. o simulador foi desenvolvido como projeto Java puro;
2. depois, em 28/05/2026, ele foi incorporado em um monorepo de projetos Java.

---

### 08/05/2026 - Inicio do simulador

| Commit | O que aconteceu |
|---|---|
| `ba2dae5` | Primeiro commit do simulador. Criou uma versao inicial em Java puro, com `Main`, `Conta`, enum de operacao, README e `.gitignore`. |
| `1300bba` / `3ecb9ac` | Ajustes de texto, typos e formatacao no README. |

O que foi estudado nesta fase:

- criar um projeto simples em Java;
- usar classe `Main`;
- criar uma classe `Conta`;
- organizar README inicial;
- comecar a versionar o projeto com Git.

---

### 12/05/2026 - Separacao de responsabilidades no dominio

| Commits | O que aconteceu |
|---|---|
| `e89f9db` | Introduziu `Banco` para gerenciar contas. Antes, muita logica ficava mais concentrada no fluxo principal. |
| `f9e8299` | Extraiu helper para mostrar dados de conta. |
| `7087828` | Moveu a logica de transferencia para `Banco`. |
| `d3a31cf` | Passou a retornar resultado detalhado de transferencia. |
| `664363a` / `c2f4216` | Passou a retornar copias de listas de contas e transacoes. |
| `03d213c` ate `72eae1e` | Separou os handlers do menu: criar conta, depositar, sacar, consultar saldo, listar, buscar, transferir e gerar extrato. |
| `b9fb86b`, `0aef2d8`, `10b0ff6` | Criou `AplicacaoBancaria` e deixou `Main` apenas como ponto de entrada. |

O que mudou na arquitetura:

- `Main` ficou pequeno;
- `AplicacaoBancaria` passou a controlar o terminal;
- `Banco` passou a controlar regras entre contas;
- `Conta` ficou responsavel por saldo e extrato da propria conta;
- o projeto ficou mais facil de testar e manter.

O que foi estudado nesta fase:

- refatoracao;
- extracao de metodos;
- separacao entre UI de terminal e regra de negocio;
- encapsulamento;
- retorno defensivo de listas;
- enum para comunicar resultados.

---

### 13/05/2026 - Refinamento de regras, extrato e testes

| Commits | O que aconteceu |
|---|---|
| `ae3a877` / `031bda8` | Campos importantes viraram `final`, reforcando imutabilidade onde fazia sentido. |
| `894cb8f` / `782c8ba` | Melhorou o uso de `switch`, deixando o fluxo mais moderno e legivel. |
| `e2e2301` | Extraiu o processamento de opcoes do menu. |
| `4f4074b` / `0d71454` | Impediu transferencia para a mesma conta. |
| `ed36dd0` | Modelou o extrato usando objetos `Transacao`, em vez de texto solto. |
| `9ba88f0`, `df44803`, `60f2e49` | Melhorou formatacao de data, valor e descricao de operacoes. |
| `dabf366` | Adicionou testes basicos de comportamento bancario. |
| `7378dc9` | Migrou o projeto para Maven e JUnit 5. |
| `4959a2e` ate `383f48a` | Expandiu a cobertura de testes de conta, banco, transacao e enums. |

O que foi estudado nesta fase:

- testes automatizados com JUnit 5;
- Maven;
- extrato estruturado com classe propria;
- `LocalDateTime`;
- formatacao de valores;
- prevencao de regressao com testes.

---

### 14/05/2026 - Persistencia em arquivos CSV

| Commits | O que aconteceu |
|---|---|
| `2be9ac7` | Garantiu por teste que transferencia para a mesma conta preserva o extrato. |
| `80a87aa` | Adicionou persistencia em arquivo para contas. |
| `c8810c2` | Passou a persistir contas e transacoes em arquivos. |
| `a9f92fa` | Consolidou persistencia de contas e historico em CSV. |
| `74645fa` / `ab5b612` | Melhorou mensagens do terminal e documentacao da persistencia. |

O que foi estudado nesta fase:

- `Files`;
- `Path`;
- arquivos locais;
- CSV;
- salvar e carregar estado;
- lidar com arquivo inexistente;
- ignorar linhas invalidas.

---

### 18/05/2026 - Estudo de banco relacional, JDBC e SQLite

| Commits | O que aconteceu |
|---|---|
| `ceff35e` | Criou documentacao do modelo conceitual do banco. |
| `7c56802` | Criou documentacao sobre banco de dados e JDBC. |
| `4dd5379` | Adicionou a dependencia do SQLite JDBC no `pom.xml`. |
| `75ef1a5` | Criou `ConexaoBanco`. |
| `8059f96` | Criou repository SQLite para contas. |
| `10d202d` | Criou repository SQLite para transacoes. |
| `328a703` | Criou plano de integracao JDBC. |
| `ce66878` | Passou a inicializar o banco SQLite ao iniciar. |
| `1d475c2` | Criou migracao CSV para SQLite. |
| `5af10ad` | Passou a usar SQLite para persistencia de contas. |
| `308e702` | Passou a usar SQLite para persistencia de transacoes. |

O que mudou na arquitetura:

- CSV deixou de ser persistencia principal;
- SQLite passou a guardar contas e transacoes;
- repositories JDBC assumiram o fluxo principal;
- CSV ficou como legado e suporte para migracao.

O que foi estudado nesta fase:

- banco relacional;
- tabela;
- chave primaria;
- chave estrangeira;
- JDBC;
- driver JDBC;
- `Connection`;
- `PreparedStatement`;
- `ResultSet`;
- migracao de dados.

---

### 18/05/2026 - Transacoes SQL e consistencia

| Commits | O que aconteceu |
|---|---|
| `d1b844c` | Documentou conceitos de transacao de banco de dados. |
| `761d053` | Criou salvamento de estado em uma transacao de banco. |
| `c799425` | Documentou o service de transacao. |
| `a5b4d4e` | Integra a persistencia transacional na aplicacao. |
| `9bae8ea` | Adicionou teste para validar `rollback` quando falha o salvamento. |

O que foi estudado nesta fase:

- `commit`;
- `rollback`;
- atomicidade;
- salvar contas e transacoes juntas;
- evitar banco parcialmente salvo;
- testar falhas de persistencia.

---

### 18/05/2026 a 20/05/2026 - Limpeza final da integracao JDBC

| Commits | O que aconteceu |
|---|---|
| `b4552b2` | Renomeou repositories CSV para deixar claro que sao legados. |
| `3a3376a` | Limpou a integracao JDBC. |
| `9e97ede` | Ajustou newline final em teste CSV. |
| `3937368` | Atualizou o status do projeto na documentacao. |

O que foi estudado nesta fase:

- nomes mais claros;
- limpeza de codigo;
- documentacao do estado real;
- manutencao depois da feature principal estar pronta.

---

### 20/05/2026 a 21/05/2026 - Outro projeto no historico: `banking-api`

Depois do simulador, o historico mostra commits de outro projeto chamado `banking-api`.

| Commits | O que aconteceu |
|---|---|
| `94e0bc9` ate `1bce48d` | Criacao e evolucao inicial de uma API Spring Boot separada, com health check, endpoint de criacao de conta, validacao e resposta de erro. |

Esses commits nao fazem parte do simulador de terminal. Eles aparecem porque depois os projetos foram organizados juntos em um monorepo.

---

### 28/05/2026 - Entrada no monorepo e README geral

| Commit | O que aconteceu |
|---|---|
| `c5ec909` | Inicializou o monorepo de projetos Java. |
| `9473b85` | Adicionou o projeto `banking-api/` ao monorepo. |
| `3905e88` | Adicionou `simulador-conta-bancaria-java/` ao monorepo a partir do commit `3937368`. |
| `9180d19` | Criou README do monorepo. |
| `0d05757` | Ignorou configuracoes de editor. |
| `3ff0911` | Expandiu descricoes dos projetos no README do monorepo. |

O que isso significa:

- o simulador continua sendo o mesmo projeto educacional;
- agora ele vive dentro de uma pasta propria;
- o repositorio passou a agrupar mais de um projeto Java;
- o README deste projeto explica o simulador, enquanto o README do monorepo explica o conjunto.

---

### Resumo da evolucao

```text
Java puro no terminal
   |
   v
Separacao em Main, AplicacaoBancaria, Banco e Conta
   |
   v
Extrato com Transacao e TipoOperacao
   |
   v
Testes com Maven e JUnit 5
   |
   v
Persistencia em CSV
   |
   v
Modelo relacional e estudo de JDBC
   |
   v
SQLite com repositories JDBC
   |
   v
Migracao CSV -> SQLite
   |
   v
Persistencia transacional com commit e rollback
   |
   v
Entrada no monorepo de projetos Java
```

---

## Observacao final

Este projeto foi criado para praticar fundamentos de Java e evoluir uma aplicacao simples com organizacao, testes e regras de dominio claras.

O ponto mais importante da arquitetura atual e este: o dominio altera objetos em memoria, e a persistencia grava o estado resultante no SQLite.

Essa separacao deixa o projeto mais facil de entender, testar e evoluir.
