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
- Retorno defensivo de listas
- Organização em pacotes

## Atualizações realizadas

- Adicionada a classe `Banco` para centralizar o cadastro, a busca e a listagem de contas.
- Implementada validação para evitar contas duplicadas pelo mesmo número.
- Movida a regra de transferência para a classe `Banco`, mantendo a classe `Main` focada no menu e na interação com o usuário.
- Criado o enum `ResultadoTransferencia` para representar os possíveis resultados de uma transferência.
- Implementadas transferências entre contas com validação de origem, destino, valor e saldo.
- Registradas transferências no extrato da conta de origem e da conta de destino.
- Adicionado histórico de operações com `LocalDateTime` e `Tipo_Operacao`.
- Criado método auxiliar para exibir os dados de uma conta.
- Ajustado `listarContas()` para retornar uma cópia da lista de contas.
- Ajustado `getExtrato()` para retornar uma cópia do histórico da conta.
- Melhorado o tratamento de entradas inválidas para números inteiros e valores monetários.
- Atualizada a documentação do projeto com estrutura e comando de compilação corretos.

## Como executar

Na raiz do projeto, compile os arquivos Java:

```bash
javac -d out app/Main.java model/Banco.java model/Conta.java model/ResultadoTransferencia.java model/Tipo_Operacao.java
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
│   └── Tipo_Operacao.java
└── README.md
```

## Observação

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. Ele pode evoluir futuramente com novas funcionalidades, melhorias de organização e persistência de dados.
