# Modelo conceitual do banco de dados

O objetivo deste documento é planejar, em linguagem simples, como os dados do simulador bancário podem ser organizados em um banco de dados relacional.

Este documento começou como um plano conceitual e o modelo já foi aplicado no projeto usando SQLite. As tabelas principais reais são `contas` e `transacoes`.

No projeto atual, o modelo conceitual de contas e transações já foi aplicado em SQLite. As tabelas principais são `contas` e `transacoes`.

## O que é um banco de dados relacional

Um banco de dados relacional é uma forma organizada de guardar dados em tabelas.

Ele é chamado de relacional porque as tabelas podem se relacionar entre si.

No projeto do simulador bancário, isso aparece assim:

- uma conta bancária fica em uma tabela de contas;
- as transações do extrato ficam em uma tabela de transações;
- cada transação precisa indicar a qual conta pertence.

## O que é uma tabela

Uma tabela é uma estrutura parecida com uma planilha, usada para guardar dados de um mesmo tipo.

Em uma tabela:

- coluna: é o campo da tabela, ou seja, o tipo de informação que será guardado.
- linha ou registro: é um item salvo na tabela.

Exemplo simples: em uma tabela de contas, as colunas podem ser `numero`, `titular` e `saldo`. Cada linha representa uma conta diferente.

## Tabela contas

A tabela `contas` representa o estado atual das contas bancárias.

Cada linha da tabela corresponde a uma conta do sistema.

| Coluna | Tipo conceitual | Significado |
|---|---|---|
| numero | INTEGER | Identifica a conta de forma única |
| titular | TEXT | Guarda o nome do titular da conta |
| saldo | REAL | Guarda o saldo atual da conta |

O campo `numero` identifica a conta porque cada conta precisa ter um número próprio, sem repetição, para podermos encontrá-la com segurança. Por isso, ele pode ser usado como chave primária.

O campo `titular` é necessário para sabermos quem é o dono da conta.

O campo `saldo` precisa ser salvo porque ele representa o valor atual disponível na conta naquele momento.

Para aprendizado inicial, `REAL` é suficiente para representar saldo. Em sistemas financeiros reais, dinheiro costuma exigir tipos mais controlados, como `DECIMAL`, para evitar problemas de precisão.

Exemplo visual:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |
| 2 | Maria | 80.0 |

### Relação com Java

- `contas.numero` -> `Conta.numero`
- `contas.titular` -> `Conta.titular`
- `contas.saldo` -> `Conta.saldo`

Cada linha da tabela `contas` pode ser transformada em um objeto `Conta` no Java.

## Tabela transacoes

A tabela `transacoes` representa o histórico do extrato das contas.

Cada linha da tabela corresponde a uma transação.

| Coluna | Tipo conceitual | Significado |
|---|---|---|
| id | INTEGER | Identifica cada transação de forma única |
| numero_conta | INTEGER | Indica a qual conta a transação pertence |
| tipo | TEXT | Representa o tipo da operação |
| valor | REAL | Guarda o valor movimentado |
| data_hora | TEXT | Guarda quando a transação aconteceu |
| descricao | TEXT | Guarda uma descrição textual da operação |

O campo `id` existe mesmo já tendo `numero_conta` porque uma mesma conta pode ter várias transações. Então, `numero_conta` identifica a conta, mas não identifica sozinho uma transação específica.

Quando falamos que `id` é autoincremental, de forma conceitual, significa que cada nova transação recebe automaticamente o próximo número (`1`, `2`, `3`...), sem repetir.

O campo `numero_conta` aponta para uma conta para deixar claro de qual conta aquela transação faz parte.

O campo `tipo` se relaciona com o enum `TipoOperacao`, que no projeto possui valores como:

- `DEPOSITO`
- `SAQUE`
- `TRANSFERENCIA_ENVIADA`
- `TRANSFERENCIA_RECEBIDA`

No banco, esses valores podem ser salvos como texto.

O campo `data_hora` se relaciona com `LocalDateTime`, que representa data e hora completas da transação.

No banco, a data/hora pode ser salva como texto em formato ISO, por exemplo:

```text
2026-05-15T10:30
```

O campo `descricao` é útil para o extrato porque traz um texto explicando a operação realizada.

Exemplo visual:

| id | numero_conta | tipo | valor | data_hora | descricao |
|---|---|---|---|---|---|
| 1 | 1 | DEPOSITO | 200.0 | 2026-05-15T10:30 | Depósito realizado |
| 2 | 1 | SAQUE | 50.0 | 2026-05-15T11:00 | Saque realizado |

### Relação com Java

- `transacoes.tipo` -> `Transacao.tipo`
- `transacoes.valor` -> `Transacao.valor`
- `transacoes.data_hora` -> `Transacao.dataHora`
- `transacoes.descricao` -> `Transacao.descricao`

`numero_conta` não precisa estar dentro da classe `Transacao` hoje porque, no Java atual, a transação fica dentro do extrato de uma `Conta`. Já no banco, esse campo é necessário para sabermos a qual conta aquela transação pertence.

Cada linha da tabela `transacoes` pode ser transformada em um objeto `Transacao` no Java.

## Chave primária

Chave primária é a coluna que identifica uma linha de forma única dentro de uma tabela.

Na tabela `contas`, a chave primária será:

```text
numero
```

Isso significa que não podem existir duas contas com o mesmo número.

Na tabela `transacoes`, a chave primária será:

```text
id
```

Isso significa que cada transação terá um identificador próprio.

## Chave estrangeira

Chave estrangeira é uma coluna que aponta para a chave primária de outra tabela.

Na tabela `transacoes`, a coluna:

```text
numero_conta
```

aponta para:

```text
contas.numero
```

Isso significa que cada transação pertence a uma conta existente.

## Relação entre contas e transacoes

Uma conta pode ter várias transações.

Cada transação pertence a uma única conta.

Isso é uma relação de 1 para muitos.

Desenho conceitual:

```text
contas.numero -> transacoes.numero_conta
```

Nesse relacionamento:

- `contas.numero` é a chave primária da tabela `contas`.
- `transacoes.numero_conta` será a chave estrangeira da tabela `transacoes`.
- chave primária identifica uma linha única.
- chave estrangeira aponta para uma linha existente em outra tabela.

Exemplo:

contas:

| numero | titular | saldo |
|---|---|---|
| 1 | Gabriel | 150.0 |

transacoes:

| id | numero_conta | tipo | valor |
|---|---|---|---|
| 1 | 1 | DEPOSITO | 200.0 |
| 2 | 1 | SAQUE | 50.0 |

As duas transações pertencem à conta de número `1`.

## Por que usar duas tabelas

`contas` e `transacoes` representam conceitos diferentes:

- conta representa estado atual.
- transação representa histórico.

Seria ruim guardar tudo em uma única tabela porque:

- repetiria `titular` e `saldo` em várias linhas.
- misturaria estado atual com histórico.
- dificultaria organizar o extrato.

Exemplo ruim de tabela única:

| numero | titular | saldo | tipo | valor |
|---|---|---|---|---|
| 1 | Gabriel | 150.0 | DEPOSITO | 200.0 |
| 1 | Gabriel | 150.0 | SAQUE | 50.0 |

O titular e o saldo ficariam repetidos em cada transação.

Separar em duas tabelas deixa o modelo mais limpo e reflete a implementação já usada na etapa SQLite/JDBC.

## SQL conceitual inicial

Abaixo está uma versão dos comandos SQL que refletem a estrutura usada na implementação SQLite/JDBC atual.

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

Estes comandos refletem a estrutura SQL usada pela aplicação SQLite/JDBC atual.

- `CREATE TABLE` cria uma tabela.
- `INTEGER` representa número inteiro.
- `TEXT` representa texto.
- `REAL` representa número com casas decimais.
- `PRIMARY KEY` define a chave primária.
- `AUTOINCREMENT` indica que o banco pode gerar o próximo número automaticamente.
- `NOT NULL` indica que o campo é obrigatório.
- `FOREIGN KEY` define uma relação com outra tabela.
- `REFERENCES contas(numero)` indica que `numero_conta` aponta para uma conta existente.

## Primeiros comandos SQL

### SELECT

O comando `SELECT` é usado para consultar dados em uma tabela.

Exemplo:

```sql
SELECT * FROM contas;
```

Esse comando busca todas as colunas de todas as contas.

Neste exemplo:

- `SELECT` indica que queremos consultar dados.
- `*` significa todas as colunas.
- `FROM contas` indica que os dados vêm da tabela `contas`.

Outro exemplo:

```sql
SELECT numero, titular FROM contas;
```

Esse comando busca apenas o número e o titular das contas.

### WHERE

O comando `WHERE` é usado para filtrar os resultados de uma consulta.

Exemplo:

```sql
SELECT * FROM contas WHERE numero = 1;
```

Esse comando busca apenas a conta cujo número é `1`.

Outro exemplo:

```sql
SELECT * FROM transacoes WHERE numero_conta = 1;
```

Esse comando busca apenas as transações que pertencem à conta número `1`.

### INSERT

O comando `INSERT` é usado para inserir uma nova linha em uma tabela.

Exemplo na tabela `contas`:

```sql
INSERT INTO contas (numero, titular, saldo)
VALUES (1, 'Gabriel', 150.0);
```

Esse comando cria uma nova conta com número `1`, titular `Gabriel` e saldo `150.0`.

Exemplo na tabela `transacoes`:

```sql
INSERT INTO transacoes (numero_conta, tipo, valor, data_hora, descricao)
VALUES (1, 'DEPOSITO', 150.0, '2026-05-15T10:30', 'Depósito realizado');
```

Nesse exemplo, não informamos o `id`, porque ele será gerado automaticamente pelo banco.

### UPDATE

O comando `UPDATE` é usado para alterar dados existentes.

Exemplo:

```sql
UPDATE contas
SET saldo = 200.0
WHERE numero = 1;
```

Esse comando altera o saldo da conta número `1` para `200.0`.

O `WHERE` é muito importante em um `UPDATE`. Sem ele, todas as linhas da tabela poderiam ser alteradas.

### DELETE

O comando `DELETE` é usado para remover linhas de uma tabela.

Exemplo:

```sql
DELETE FROM transacoes
WHERE id = 1;
```

Esse comando remove a transação cujo `id` é `1`.

Assim como no `UPDATE`, o `WHERE` é muito importante. Sem ele, todas as linhas da tabela poderiam ser removidas.

## Exemplos de consultas úteis para o projeto

Buscar uma conta pelo número:

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

Buscar todas as transações de depósito:

```sql
SELECT * FROM transacoes
WHERE tipo = 'DEPOSITO';
```


