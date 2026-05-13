# Projeto Banco

Simulador de conta bancária desenvolvido em Java puro, com execução pelo terminal. O projeto foi criado para praticar fundamentos da linguagem Java por meio de um sistema bancário simples, sem Spring Boot, sem banco de dados e sem interface gráfica.

## Funcionalidades

- Criar conta
- Realizar depósito
- Realizar saque
- Consultar saldo de uma conta
- Listar contas
- Buscar conta por número
- Transferir entre contas
- Gerar extrato de operações

## Regras principais

- Não é permitido criar duas contas com o mesmo número.
- Depósitos, saques e transferências precisam ter valor maior que zero.
- Saques e transferências dependem de saldo suficiente.
- Transferências validam conta de origem, conta de destino, valor e saldo.
- O extrato registra depósitos, saques e transferências enviadas ou recebidas.

## Conceitos praticados

- Sintaxe básica Java
- `Scanner`
- `while`
- `switch`
- `if/else`
- Métodos
- Classes e objetos
- Construtores
- Encapsulamento
- Getters
- `ArrayList`
- `for-each`
- Tratamento de erros com `try/catch`
- `enum`
- `LocalDateTime`
- Retorno defensivo de listas
- Organização em pacotes
- Separação de responsabilidades entre menu, banco e conta

## Atualizações realizadas

- Adicionada a classe `Banco` para centralizar o cadastro, a busca e a listagem de contas.
- Implementada validação para evitar contas duplicadas pelo mesmo número.
- Movida a regra de transferência para a classe `Banco`, mantendo a classe `Main` focada no menu e na interação com o usuário.
- Criado o enum `ResultadoTransferencia` para representar os possíveis resultados de uma transferência.
- Implementadas transferências entre contas com validação de origem, destino, valor e saldo.
- Registradas transferências no extrato da conta de origem e da conta de destino.
- Adicionado histórico de operações com `LocalDateTime` e `TipoOperacao`.
- Criado método auxiliar para exibir os dados de uma conta.
- Ajustado `listarContas()` para retornar uma cópia da lista de contas.
- Ajustado `getExtrato()` para retornar uma cópia do histórico da conta.
- Melhorado o tratamento de entradas inválidas para números inteiros e valores monetários.
- Atualizada a documentação do projeto com estrutura e comando de compilação corretos.

## Requisitos

- Java JDK instalado
- Terminal para compilar e executar o projeto

## Como executar

Na raiz do projeto, compile os arquivos Java:

```bash
javac -d out app/Main.java model/Banco.java model/Conta.java model/ResultadoTransferencia.java model/TipoOperacao.java
```

Depois execute a classe principal:

```bash
java -cp out app.Main
```

## Estrutura do projeto

```text
.
├── app
│   └── Main.java
├── model
│   ├── Banco.java
│   ├── Conta.java
│   ├── ResultadoTransferencia.java
│   └── TipoOperacao.java
└── README.md
```

## Observação

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. Ele pode evoluir futuramente com novas funcionalidades, melhorias de organização e persistência de dados.
