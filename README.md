# Simulador de Conta Bancária em Java Puro

Projeto de estudo desenvolvido em Java puro, com execução pelo terminal. O objetivo é praticar fundamentos da linguagem Java criando um sistema bancário simples, sem Spring Boot, sem banco de dados e sem interface gráfica.

## Funcionalidades

- Criar conta
- Depositar
- Sacar
- Consultar saldo
- Listar contas
- Buscar conta por número
- Transferir entre contas
- Gerar extrato

## Conceitos praticados

- Sintaxe básica Java
- `Scanner`
- `while`
- `switch`
- `if/else`
- Metodos
- Classes e objetos
- Construtores
- Encapsulamento
- Getters
- `ArrayList`
- `for-each`
- Tratamento de erros com `try/catch`
- `enum`
- `LocalDateTime`
- Organização em pacotes

## Atualizações realizadas

- Adicionada a classe `Banco` para centralizar o cadastro, a busca e a listagem de contas.
- Implementada validação para evitar contas duplicadas pelo mesmo número.
- Implementadas transferências entre contas com registro no extrato da conta de origem e da conta de destino.
- Adicionado histórico de operações com `LocalDateTime` e `Tipo_Operacao`.
- Melhorado o tratamento de entradas inválidas para números inteiros e valores monetários.
- Atualizada a documentação do projeto com estrutura e comando de compilação corretos.

## Como executar

Na raiz do projeto, compile os arquivos Java:

```bash
javac -d out app/Main.java model/Banco.java model/Conta.java model/Tipo_Operacao.java
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
│   └── Tipo_Operacao.java
└── README.md
```

## Observação

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. Ele pode evoluir futuramente com novas funcionalidades, melhorias de organização e persistência de dados.
