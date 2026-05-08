# Simulador de Conta Bancaria em Java Puro

Projeto de estudo desenvolvido em Java puro, com execução pelo terminal. O objetivo e praticar fundamentos da linguagem Java criando um sistema bancario simples, sem Spring Boot, sem banco de dados e sem interface grafica.

## Funcionalidades

- Criar conta
- Depositar
- Sacar
- Consultar saldo
- Listar contas
- Buscar conta por numero
- Transferir entre contas
- Gerar extrato

## Conceitos praticados

- Sintaxe basica Java
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
- Organizacao em pacotes

## Como executar

Na raiz do projeto, compile os arquivos Java:

```bash
javac -d out app/Main.java model/Conta.java model/Tipo_Operacao.java
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
│   ├── Conta.java
│   └── Tipo_Operacao.java
└── README.md
```

## Observacao

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. Ele pode evoluir futuramente com novas funcionalidades, melhorias de organizacao e persistencia de dados.
