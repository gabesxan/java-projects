# Conceitos iniciais de JDBC

JDBC significa **Java Database Connectivity**.

Ele é o recurso usado em Java para conectar uma aplicação a um banco de dados relacional.

Neste projeto, JDBC será estudado depois do modelo conceitual do banco, para que a aplicação possa futuramente substituir a persistência em CSV por persistência em banco de dados.

No projeto atual, esses conceitos já foram aplicados usando SQLite. As classes principais da integração são `ConexaoBanco`, `InicializadorBanco`, `ContaRepositoryJdbc` e `TransacaoRepositoryJdbc`.

A ideia geral é:

```text
Aplicação Java -> JDBC -> Banco de dados
```

## Por que JDBC existe

O Java, sozinho, não grava dados diretamente em um banco relacional.

Ele precisa de uma forma padronizada para:

- abrir conexão com o banco;
- enviar comandos SQL;
- receber resultados;
- tratar erros;
- fechar recursos.

JDBC fornece esse caminho.

## Driver JDBC

Um driver JDBC é uma biblioteca que permite que o Java converse com um banco de dados específico.

Exemplos:

- SQLite usa um driver JDBC próprio.
- MySQL usa um driver JDBC próprio.
- PostgreSQL usa um driver JDBC próprio.

Sem o driver, o Java conhece a ideia de JDBC, mas não sabe se comunicar com o banco escolhido.

No Maven, um driver normalmente entra como dependência no `pom.xml`.

Exemplo conceitual:

```xml
<dependency>
    <groupId>grupo-do-driver</groupId>
    <artifactId>nome-do-driver</artifactId>
    <version>versao</version>
</dependency>
```

Ainda não vamos adicionar essa dependência até entender bem o fluxo.

## URL de conexão

A URL de conexão é o endereço usado pelo Java para encontrar o banco.

Ela informa:

- qual banco será usado;
- onde o banco está;
- em alguns casos, o nome do arquivo ou servidor.

Exemplo conceitual para SQLite:

```text
jdbc:sqlite:data/banco.db
```

Significado:

- `jdbc`: indica que será usada uma conexão JDBC.
- `sqlite`: indica o tipo de banco.
- `data/banco.db`: indica o arquivo onde o banco SQLite ficará salvo.

Para outros bancos, a URL muda.

Exemplo conceitual para MySQL:

```text
jdbc:mysql://localhost:3306/nome_do_banco
```

Exemplo conceitual para PostgreSQL:

```text
jdbc:postgresql://localhost:5432/nome_do_banco
```

## Connection

`Connection` representa uma conexão aberta entre a aplicação Java e o banco de dados.

Conceitualmente, é como abrir uma linha de comunicação:

```text
Java conectado ao banco
```

Com uma `Connection`, a aplicação pode preparar e executar comandos SQL.

Exemplo conceitual:

```java
Connection conexao = DriverManager.getConnection(url);
```

Ainda não estamos implementando, mas a ideia é:

```text
pegar a URL do banco
abrir conexão
usar a conexão
fechar conexão
```

## DriverManager

`DriverManager` é uma classe do Java usada para abrir conexões JDBC.

Ela recebe a URL de conexão e devolve uma `Connection`.

Fluxo conceitual:

```text
DriverManager + URL -> Connection
```

Exemplo conceitual:

```java
DriverManager.getConnection("jdbc:sqlite:data/banco.db");
```

## Statement

`Statement` é um objeto usado para executar comandos SQL simples.

Exemplo conceitual:

```java
Statement stmt = conexao.createStatement();
stmt.execute("CREATE TABLE contas (...)");
```

Ele pode executar comandos como:

- `CREATE TABLE`;
- `SELECT`;
- `INSERT`;
- `UPDATE`;
- `DELETE`.

Mas existe um cuidado: para comandos com valores vindos do usuário, `Statement` não é a opção mais segura.

## PreparedStatement

`PreparedStatement` é uma forma mais segura e organizada de executar SQL com valores variáveis.

Em vez de montar SQL juntando texto, usamos espaços reservados com `?`.

Exemplo conceitual:

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
- deixa o código mais organizado;
- ajuda o banco a entender melhor o comando.

No seu projeto, `PreparedStatement` será usado para salvar contas e transações.

## SQL Injection

SQL Injection é um problema de segurança que acontece quando valores digitados pelo usuário são colocados diretamente dentro de comandos SQL.

Exemplo ruim conceitual:

```java
"SELECT * FROM contas WHERE titular = '" + nomeDigitado + "'"
```

Se o usuário digitar algo malicioso, ele pode alterar o sentido do SQL.

Com `PreparedStatement`, os valores são tratados como dados, não como pedaços do comando.

Por isso, mesmo em projeto educacional, é melhor aprender `PreparedStatement` desde cedo.

## ResultSet

`ResultSet` representa o resultado de uma consulta `SELECT`.

Quando você faz:

```sql
SELECT * FROM contas;
```

o banco retorna linhas.

No Java, essas linhas aparecem dentro de um `ResultSet`.

Fluxo conceitual:

```text
SELECT -> ResultSet -> objetos Java
```

Exemplo mental:

Tabela `contas`:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |
| 2 | Maria | 80.0 |

O `ResultSet` permite percorrer essas linhas uma por vez e transformar cada uma em objeto `Conta`.

## SQLException

`SQLException` é a exceção usada pelo Java para indicar problemas ao trabalhar com banco de dados.

Exemplos de problemas:

- banco não encontrado;
- SQL escrito errado;
- tabela não existe;
- conexão falhou;
- valor inválido;
- violação de chave primária;
- violação de chave estrangeira.

Conceitualmente, qualquer operação JDBC pode falhar, então o código precisa lidar com `SQLException`.

## Fechamento de recursos

Conexões e comandos JDBC usam recursos externos.

Por isso, precisam ser fechados após o uso.

Recursos comuns:

- `Connection`;
- `Statement`;
- `PreparedStatement`;
- `ResultSet`.

Em Java, normalmente usamos `try-with-resources`.

Exemplo conceitual:

```java
try (Connection conexao = DriverManager.getConnection(url)) {
    // usar conexão
}
```

Quando o bloco termina, o Java fecha a conexão automaticamente.

## Transação de banco de dados

Uma transação de banco de dados é um conjunto de operações que deve ser tratado como uma unidade.

Exemplo no seu projeto:

```text
transferência entre contas
```

Uma transferência envolve:

- debitar a conta de origem;
- creditar a conta de destino;
- registrar transação enviada;
- registrar transação recebida.

Essas operações precisam acontecer juntas.

Se uma parte falhar, o ideal é desfazer tudo.

Conceitos importantes:

- `commit`: confirma as alterações.
- `rollback`: desfaz as alterações.
- atomicidade: tudo acontece ou nada acontece.

Esse assunto pode ficar para depois do primeiro contato com JDBC, mas é importante saber que existe.

## Repository JDBC

Hoje o projeto tem repositories para persistência em CSV.

Na fase JDBC, a ideia será criar repositories que salvam e carregam dados do banco.

Exemplos possíveis:

```text
ContaRepositoryCsv
TransacaoRepositoryCsv
ContaRepositoryJdbc
TransacaoRepositoryJdbc
```

Ou, se o projeto for simplificar:

```text
ContaRepository
TransacaoRepository
```

mas mudando a implementação interna.

Responsabilidade de um repository JDBC:

- receber objetos Java;
- transformar em comandos SQL;
- executar no banco;
- ler resultados do banco;
- transformar linhas em objetos Java.

## Como uma Conta vira SQL

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

Com `PreparedStatement`, a ideia seria:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (?, ?, ?);
```

E o Java preencheria:

```text
1 -> numero
Gabriel -> titular
150.0 -> saldo
```

## Como uma linha do banco vira Conta

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

## Como uma Transacao vira SQL

Objeto Java:

```text
Transacao(tipo=DEPOSITO, valor=150.0, dataHora=2026-05-15T10:30, descricao=Depósito realizado)
```

Linha no banco:

| id | numero_conta | tipo | valor | data_hora | descricao |
|---|---|---|---|---|---|
| 1 | 1 | DEPOSITO | 150.0 | 2026-05-15T10:30 | Depósito realizado |

O `id` seria gerado automaticamente pelo banco.

SQL conceitual:

```sql
INSERT INTO transacoes (numero_conta, tipo, valor, data_hora, descricao)
VALUES (1, 'DEPOSITO', 150.0, '2026-05-15T10:30', 'Depósito realizado');
```

## Como uma linha do banco vira Transacao

Linha no banco:

| tipo | valor | data_hora | descricao |
|---|---|---|---|
| DEPOSITO | 150.0 | 2026-05-15T10:30 | Depósito realizado |

Objeto Java:

```text
new Transacao(
    TipoOperacao.DEPOSITO,
    150.0,
    LocalDateTime.parse("2026-05-15T10:30"),
    "Depósito realizado"
)
```

Fluxo conceitual:

```text
ResultSet -> ler tipo, valor, data_hora, descricao -> criar Transacao
```

## Ordem provável de implementação futura

Quando chegar a hora de implementar JDBC, uma ordem segura seria:

1. Escolher SQLite como banco inicial.
2. Adicionar o driver SQLite JDBC no `pom.xml`.
3. Criar uma classe de conexão.
4. Criar uma inicialização do banco com `CREATE TABLE IF NOT EXISTS`.
5. Criar repository JDBC para contas.
6. Testar salvar e carregar contas.
7. Integrar contas na aplicação.
8. Criar repository JDBC para transações.
9. Testar salvar e carregar transações.
10. Integrar extrato na aplicação.
11. Avaliar se os repositories CSV serão mantidos ou substituídos.

## O que ainda não fazer

Nesta etapa, ainda não devemos:

- adicionar dependência JDBC no `pom.xml`;
- criar conexão com banco;
- criar arquivo `.db`;
- substituir CSV;
- alterar `AplicacaoBancaria`;
- alterar regras de negócio;
- usar Spring Boot;
- criar API REST.

O objetivo agora é entender os conceitos antes de implementar.
