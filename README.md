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
                ├── TransacaoRepositoryJdbcTest.java
                └── TransacaoRepositoryTest.java
```

A pasta `target/` e gerada automaticamente pelo Maven durante compilacao e testes. Ela nao faz parte do codigo-fonte.

A pasta `data/` guarda principalmente o arquivo `banco.db` usado pela persistencia atual em SQLite. Os arquivos `contas.csv` e `transacoes.csv` existem como suporte de migracao legada e nao sao a fonte principal de dados hoje.

> Observacao: arquivos gerados em `data/`, como `banco.db`, `contas.csv` e `transacoes.csv`, nao devem ser commitados no Git.

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
- `persistence.ContaRepositoryJdbc`: salva e carrega as contas no banco `data/banco.db`.
- `persistence.TransacaoRepositoryJdbc`: salva e carrega as transacoes no banco `data/banco.db`.
- `persistence.MigradorCsvParaJdbc`: converte os dados antigos de CSV para o banco SQLite.
- `persistence.ContaRepository`: interface de repositorio de contas.
- `persistence.TransacaoRepository`: interface de repositorio de transacoes.

## Persistência atual

A persistencia principal do projeto agora usa SQLite via JDBC.

- O banco de dados SQLite fica em `data/banco.db`.
- Ao iniciar a aplicacao, `InicializadorBanco` cria automaticamente as tabelas se elas ainda nao existirem.
- As contas sao salvas e carregadas por `ContaRepositoryJdbc`.
- As transacoes/extrato sao salvas e carregadas por `TransacaoRepositoryJdbc`.
- `ConexaoBanco` fornece a conexao JDBC para `data/banco.db`.

Os arquivos CSV ainda existem no projeto, mas hoje servem apenas como suporte para a migracao de dados antiga. A persistencia principal ja e feita no banco SQLite.

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
- `ContaRepositoryTest`: valida a interface de repositorio de contas, incluindo suporte CSV.
- `TransacaoRepositoryTest`: valida a interface de repositorio de transacoes, incluindo suporte CSV.
- `ConexaoBancoTest`: valida a configuracao da conexao SQLite.
- `InicializadorBancoTest`: valida a criacao e inicializacao das tabelas.
- `MigradorCsvParaJdbcTest`: valida a migracao de CSV para SQLite.

## Conceitos praticados

- Programacao orientada a objetos.
- Encapsulamento e separacao de responsabilidades.
- Uso de enums para tipos e resultados.
- Persistencia com JDBC e SQLite.
- Testes unitarios com JUnit 5.
- Migracao de dados entre formatos legados e atuais.


- Sintaxe basica Java.
- Aplicacao de terminal com `Scanner`.
- Estruturas de decisao e repeticao.
- Classes, objetos, construtores e metodos.
- Encapsulamento com atributos privados e getters.
- Separacao entre interface de terminal, regras do banco e regras da conta.
- Uso de `ArrayList`.
- Uso de `enum` para tipos de operacao e resultados.
- Persistencia simples com `Files`, `Path` e arquivo CSV.
- Registro de data e hora com `LocalDateTime`.
- Formatacao de dados para exibicao no terminal.
- Tratamento de entradas invalidas com `try/catch`.
- Retorno defensivo de listas para proteger dados internos.
- Estrutura de projeto Java com Maven.
- Testes automatizados com JUnit 5.

## Observacao

Este projeto foi criado para praticar fundamentos de Java e evoluir uma aplicacao simples com organizacao, testes e regras de dominio claras.
