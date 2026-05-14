# Simulador de Conta Bancaria em Java

Projeto educacional de um simulador bancario feito em Java puro, executado pelo terminal e organizado com Maven. A aplicacao foi criada para praticar programacao orientada a objetos, separacao de responsabilidades, encapsulamento, enums, registro de extrato, persistencia simples em arquivo e testes automatizados com JUnit 5.

O projeto nao usa Spring Boot, banco de dados, API web ou interface grafica. Toda a interacao acontece pelo terminal.

## Funcionalidades

- Criar contas bancarias.
- Realizar depositos e saques.
- Consultar saldo de uma conta.
- Listar contas cadastradas.
- Buscar conta por numero.
- Transferir valores entre contas.
- Gerar extrato com historico de operacoes.
- Salvar e carregar contas em arquivo CSV.
- Salvar e carregar transacoes do extrato em arquivo CSV.

## Tecnologias

- Java 25.
- Maven.
- JUnit 5.
- Maven Surefire Plugin.

## Estrutura do projeto

```text
.
├── pom.xml
├── README.md
├── data
│   ├── contas.csv
│   └── transacoes.csv
└── src
    ├── main
    │   └── java
    │       ├── app
    │       │   ├── AplicacaoBancaria.java
    │       │   └── Main.java
    │       ├── model
    │       │   ├── Banco.java
    │       │   ├── Conta.java
    │       │   ├── ResultadoTransferencia.java
    │       │   ├── TipoOperacao.java
    │       │   └── Transacao.java
    │       └── persistence
    │           ├── ContaRepository.java
    │           └── TransacaoRepository.java
    └── test
        └── java
            ├── model
            │   ├── BancoTest.java
            │   ├── ContaTest.java
            │   ├── ResultadoTransferenciaTest.java
            │   ├── TipoOperacaoTest.java
            │   └── TransacaoTest.java
            └── persistence
                ├── ContaRepositoryTest.java
                └── TransacaoRepositoryTest.java
```

A pasta `target/` e gerada automaticamente pelo Maven durante compilacao e testes. Ela nao faz parte do codigo-fonte.

A pasta `data/` guarda os arquivos `contas.csv` e `transacoes.csv`, usados pela aplicacao para manter contas e extratos entre execucoes.

## Organizacao da aplicacao

- `app.Main`: ponto de entrada do programa. Cria a aplicacao bancaria e inicia a execucao.
- `app.AplicacaoBancaria`: controla o menu, a leitura de dados no terminal e o fluxo das operacoes.
- `model.Banco`: gerencia as contas, busca por numero e coordena transferencias.
- `model.Conta`: representa uma conta individual, com numero, titular, saldo e extrato.
- `model.Transacao`: representa um item do extrato, com tipo, valor, data/hora e descricao.
- `model.TipoOperacao`: enum com os tipos de operacao registrados no extrato.
- `model.ResultadoTransferencia`: enum com os possiveis resultados de uma transferencia.
- `persistence.ContaRepository`: salva e carrega as contas no arquivo `data/contas.csv`.
- `persistence.TransacaoRepository`: salva e carrega as transacoes do extrato no arquivo `data/transacoes.csv`.

## Persistência em arquivos

Os dados da aplicacao sao salvos em arquivos CSV dentro da pasta `data/`. Isso permite manter contas e extratos entre diferentes execucoes do programa.

- `data/contas.csv`: guarda os dados principais das contas bancarias.
- `data/transacoes.csv`: guarda o historico de movimentacoes (extrato) de cada conta.

Ao iniciar, a aplicacao carrega os dados desses arquivos. Quando uma conta e criada, ou quando acontece deposito, saque ou transferencia, os dados atualizados sao gravados novamente nos CSVs.

Formato de `data/contas.csv`:

```text
numero;titular;saldo
```

Formato de `data/transacoes.csv`:

```text
numeroConta;tipo;valor;dataHora;descricao
```

Durante o carregamento, linhas invalidas sao ignoradas.

Na proxima gravacao, os arquivos podem ser reescritos apenas com os dados validos que ficaram carregados em memoria.

## Regras de negocio

- Nao e permitido criar duas contas com o mesmo numero pelo fluxo da aplicacao.
- Depositos, saques e transferencias precisam ter valor maior que zero.
- Saques e transferencias exigem saldo suficiente.
- Transferencias validam origem, destino, valor, saldo e contas diferentes.
- Operacoes validas registram movimentacoes no extrato.
- Tentativas invalidas nao devem alterar saldo nem registrar novas transacoes.

## Como executar

Na raiz do projeto, compile e execute a classe principal:

```bash
mvn compile
java -cp target/classes app.Main
```

Tambem e possivel executar diretamente pelo Maven quando o plugin de execucao estiver disponivel no ambiente:

```bash
mvn compile exec:java -Dexec.mainClass=app.Main
```

## Testes automatizados

Os testes ficam em `src/test/java`, usam JUnit 5 e sao executados pelo Maven Surefire.

Para rodar todos os testes:

```bash
mvn test
```

Resultado da ultima execucao local:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Classes de teste atuais:

- `ContaTest`: valida depositos, saques, saldo e registro de extrato para operacoes validas e invalidas.
- `BancoTest`: valida busca de contas e transferencias entre contas, incluindo origem inexistente, destino inexistente, contas iguais e saldo insuficiente.
- `TransacaoTest`: valida criacao de transacoes, dados registrados e formatacao para extrato.
- `TipoOperacaoTest`: valida as descricoes amigaveis dos tipos de operacao.
- `ResultadoTransferenciaTest`: valida os valores esperados do enum de resultados de transferencia.
- `ContaRepositoryTest`: valida salvamento, carregamento e retorno de lista vazia quando o arquivo ainda nao existe.
- `TransacaoRepositoryTest`: valida salvamento e carregamento de transacoes em arquivo CSV.

Cobertura comportamental principal:

- Deposito valido aumenta saldo e registra extrato.
- Deposito invalido mantem saldo e nao registra extrato.
- Saque valido reduz saldo e registra extrato.
- Saque invalido ou com saldo insuficiente mantem saldo.
- Busca de conta retorna a conta correta ou `null` quando inexistente.
- Transferencia valida debita a origem, credita o destino e registra extrato nas duas contas.
- Transferencias invalidas retornam o resultado adequado e preservam os saldos.
- Transferencia para a mesma conta preserva o extrato.
- Transacoes mantem tipo, valor, descricao, data/hora e formato de exibicao.
- Enums de operacao e de resultado de transferencia mantem os valores esperados.
- Repositorio de contas grava o arquivo CSV e carrega as contas salvas.
- Repositorio de transacoes grava o arquivo CSV e recarrega o extrato por conta.

## Checklist de testes manuais

Depois de executar o programa pelo terminal, use o menu para validar os principais fluxos:

- [ ] Criar uma conta com numero ainda nao usado.
- [ ] Tentar criar conta com numero repetido.
- [ ] Depositar valor maior que zero.
- [ ] Tentar depositar valor zero ou negativo.
- [ ] Sacar valor valido dentro do saldo disponivel.
- [ ] Tentar sacar valor maior que o saldo.
- [ ] Consultar saldo de uma conta existente.
- [ ] Listar contas cadastradas.
- [ ] Buscar conta por numero.
- [ ] Transferir entre duas contas diferentes.
- [ ] Tentar transferir para a mesma conta.
- [ ] Tentar transferir com origem ou destino inexistente.
- [ ] Tentar transferir com saldo insuficiente.
- [ ] Gerar extrato apos deposito, saque ou transferencia.
- [ ] Fechar e abrir a aplicacao novamente para confirmar que as contas foram carregadas.
- [ ] Confirmar que operacoes invalidas nao alteram saldo nem extrato.

## Conceitos praticados

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
