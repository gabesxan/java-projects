# Simulador de Conta Bancaria em Java

Projeto educacional de um simulador bancario feito em Java puro, executado pelo terminal e organizado com Maven. A aplicacao foi criada para praticar programacao orientada a objetos, separacao de responsabilidades, encapsulamento, enums, registro de extrato, persistencia com JDBC/SQLite e testes automatizados com JUnit 5.

O projeto nao usa Spring Boot, API web ou interface grafica. Toda a interacao acontece pelo terminal.

## Funcionalidades

- Criar contas bancarias.
- Realizar depositos e saques.
- Consultar saldo de uma conta.
- Listar contas cadastradas.
- Buscar conta por numero.
- Transferir valores entre contas.
- Gerar extrato com historico de operacoes.
- Persistir contas e transacoes em banco SQLite.
- Suporte a migracao de dados CSV para SQLite.

## Tecnologias

- Java 25.
- Maven.
- JDBC.
- SQLite.
- JUnit 5.
- Maven Surefire Plugin.

## Estrutura do projeto

```text
.
├── pom.xml
├── README.md
├── data
│   ├── banco.db
│   ├── contas.csv
│   └── transacoes.csv
└── src
    ├── main
    │   └── java
    │       ├── app
    │       │   ├── AplicacaoBancaria.java
    │       │   ├── Main.java
    │       │   └── MigracaoCsvParaJdbcMain.java
    │       ├── model
    │       │   ├── Banco.java
    │       │   ├── Conta.java
    │       │   ├── ResultadoTransferencia.java
    │       │   ├── TipoOperacao.java
    │       │   └── Transacao.java
    │       └── persistence
    │           ├── ConexaoBanco.java
    │           ├── ContaRepository.java
    │           ├── ContaRepositoryJdbc.java
    │           ├── InicializadorBanco.java
    │           ├── MigradorCsvParaJdbc.java
    │           ├── PersistenciaBancoService.java
    │           ├── TransacaoRepository.java
    │           └── TransacaoRepositoryJdbc.java
    └── test
        └── java
            ├── model
            │   ├── BancoTest.java
            │   ├── ContaTest.java
            │   ├── ResultadoTransferenciaTest.java
            │   ├── TipoOperacaoTest.java
            │   └── TransacaoTest.java
            └── persistence
                ├── ContaRepositoryJdbcTest.java
                ├── ContaRepositoryTest.java
                ├── ConexaoBancoTest.java
                ├── InicializadorBancoTest.java
                ├── MigradorCsvParaJdbcTest.java
                ├── PersistenciaBancoServiceTest.java
                ├── TransacaoRepositoryJdbcTest.java
                └── TransacaoRepositoryTest.java
```

A pasta `target/` e gerada automaticamente pelo Maven durante compilacao e testes. Ela nao faz parte do codigo-fonte.

A pasta `data/` é usada em tempo de execução e pode conter o arquivo `banco.db` do banco SQLite e, localmente, arquivos CSV de suporte à migração.

> Observação: arquivos gerados em `data/`, como `banco.db`, `contas.csv` e `transacoes.csv`, não devem ser versionados no Git.

## Organizacao da aplicacao

- `app.Main`: ponto de entrada do programa. Cria a aplicacao bancaria e inicia a execucao.
- `app.MigracaoCsvParaJdbcMain`: classe para executar a migracao de dados CSV para SQLite.
- `app.AplicacaoBancaria`: controla o menu, a leitura de dados no terminal e o fluxo das operacoes.
- `model.Banco`: gerencia as contas, busca por numero e coordena transferencias.
- `model.Conta`: representa uma conta individual, com numero, titular, saldo e extrato.
- `model.Transacao`: representa um item do extrato, com tipo, valor, data/hora e descricao.
- `model.TipoOperacao`: enum com os tipos de operacao registrados no extrato.
- `model.ResultadoTransferencia`: enum com os possiveis resultados de uma transferencia.
- `persistence.ConexaoBanco`: gerencia a conexao JDBC com o banco SQLite.
- `persistence.InicializadorBanco`: cria e inicializa as tabelas automaticamente ao iniciar a aplicacao.
- `persistence.PersistenciaBancoService`: coordena o salvamento de contas e transacoes em uma unica transacao SQL.
- `persistence.ContaRepositoryJdbc`: salva e carrega as contas no banco `data/banco.db`.
- `persistence.TransacaoRepositoryJdbc`: salva e carrega as transacoes no banco `data/banco.db`.
- `persistence.MigradorCsvParaJdbc`: converte os dados antigos de CSV para o banco SQLite.
- `persistence.ContaRepository`: persistência legada de contas em CSV.
- `persistence.TransacaoRepository`: persistência legada de transações em CSV.

## Persistência atual

A persistência principal do projeto agora usa SQLite via JDBC.

- O banco de dados SQLite fica em `data/banco.db`.
- Ao iniciar a aplicação, `InicializadorBanco` cria automaticamente as tabelas se elas ainda não existirem.
- A persistência é coordenada por `PersistenciaBancoService`, que grava contas e transações de forma coordenada em uma única transação SQL.
- As contas são salvas e carregadas por `ContaRepositoryJdbc`.
- As transações/extrato são salvas e carregadas por `TransacaoRepositoryJdbc`.
- `ConexaoBanco` fornece a conexão JDBC para `data/banco.db`.
- `ContaRepository` e `TransacaoRepository` ainda existem como persistência legada em CSV, usada apenas para apoiar a migração CSV -> SQLite.

Na prática, isso significa: quando tudo é salvo sem erro, o sistema confirma as alterações com `commit`; se algo falha no meio do processo, faz `rollback` e desfaz tudo para não deixar dados pela metade.

Os arquivos CSV ainda existem no projeto, mas hoje servem apenas como suporte para a migração de dados antiga. A persistência principal já é feita no banco SQLite.

## Migracao CSV -> SQLite

Para migrar os dados antigos de `data/contas.csv` e `data/transacoes.csv` para o banco `data/banco.db`, execute:

```bash
mvn exec:java -Dexec.mainClass=app.MigracaoCsvParaJdbcMain
```

Esse comando usa o `MigradorCsvParaJdbc` para ler os arquivos CSV existentes e inserir os registros no SQLite.

## Como executar

Na raiz do projeto, execute a aplicacao principal com:

```bash
mvn exec:java -Dexec.mainClass=app.Main
```

## Testes automatizados

Os testes ficam em `src/test/java`, usam JUnit 5 e sao executados pelo Maven Surefire.

Para rodar todos os testes:

```bash
mvn test
```

Classes de teste atuais:

- `ContaTest`: valida depositos, saques, saldo e registro de extrato para operacoes validas e invalidas.
- `BancoTest`: valida busca de contas e transferencias entre contas, incluindo origem inexistente, destino inexistente, contas iguais e saldo insuficiente.
- `TransacaoTest`: valida criacao de transacoes, dados registrados e formatacao para extrato.
- `TipoOperacaoTest`: valida as descricoes amigaveis dos tipos de operacao.
- `ResultadoTransferenciaTest`: valida os valores esperados do enum de resultados de transferencia.
- `ContaRepositoryJdbcTest`: valida salvamento e carregamento de contas em SQLite.
- `TransacaoRepositoryJdbcTest`: valida salvamento e carregamento de transacoes em SQLite.
- `ContaRepositoryTest`: valida a persistência legada de contas em CSV.
- `TransacaoRepositoryTest`: valida a persistência legada de transações em CSV.
- `ConexaoBancoTest`: valida a configuracao da conexao SQLite.
- `InicializadorBancoTest`: valida a criacao e inicializacao das tabelas.
- `MigradorCsvParaJdbcTest`: valida a migracao de CSV para SQLite.
- `PersistenciaBancoServiceTest`: valida o salvamento coordenado de contas e transações em uma transação SQL.

## Conceitos praticados

### Fase atual

- Persistência com JDBC e SQLite.
- Driver JDBC.
- Connection.
- PreparedStatement.
- ResultSet.
- Schema de banco de dados.
- Repositories JDBC: `ContaRepositoryJdbc` e `TransacaoRepositoryJdbc`.
- Migração CSV -> SQLite.

### Fases anteriores

- Java puro.
- Aplicação de terminal com `Scanner`.
- `ArrayList`.
- Programação orientada a objetos.
- Encapsulamento.
- Maven.
- JUnit 5.
- Persistência CSV com `Files`, `Path` e arquivos locais.
- Registro de data e hora com `LocalDateTime`.
- Formatação de dados para exibição no terminal.
- Tratamento de entradas inválidas com `try/catch`.
- Retorno defensivo de listas para proteger dados internos.
- Estrutura de projeto Java com Maven.
- Testes automatizados com JUnit 5.

## Observacao

Este projeto foi criado para praticar fundamentos de Java e evoluir uma aplicacao simples com organizacao, testes e regras de dominio claras.
