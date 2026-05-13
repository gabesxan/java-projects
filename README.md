# Simulador de Conta Bancária em Java

Simulador de conta bancária desenvolvido em Java puro, com execução pelo terminal. O projeto tem objetivo educacional e foi construído para praticar programação orientada a objetos, encapsulamento, separação de responsabilidades e refatoração gradual.

O projeto não usa Spring Boot, banco de dados, API web ou interface gráfica. Toda a interação acontece pelo terminal.

## Funcionalidades

- Criar conta
- Realizar depósito
- Realizar saque
- Consultar saldo de uma conta
- Listar contas
- Buscar conta por número
- Transferir entre contas
- Gerar extrato de operações

## Organização da aplicação

A aplicação foi dividida em pacotes para separar melhor as responsabilidades:

- `app.Main`: ponto de entrada do programa. Essa classe apenas cria uma instância de `AplicacaoBancaria` e chama o método `executar()`.
- `app.AplicacaoBancaria`: controla o menu, a entrada de dados do usuário e o fluxo da aplicação no terminal. Ela conversa com o `Banco` para executar as operações escolhidas.
- `model.Banco`: gerencia a lista de contas, busca contas por número, informa se há contas cadastradas e coordena transferências entre contas.
- `model.Conta`: representa uma conta individual. Guarda número, titular, saldo e extrato. Também contém as operações próprias de uma conta, como depositar, sacar e registrar movimentações.
- `model.Transacao`: representa cada item do extrato. Guarda o tipo da operação, valor, data/hora e descrição, além de formatar a saída exibida no terminal.
- `model.TipoOperacao`: define os tipos de operação registrados no extrato, como depósito, saque e transferências. Cada tipo possui uma descrição amigável para exibição.
- `model.ResultadoTransferencia`: representa os possíveis resultados de uma tentativa de transferência, como sucesso, conta não encontrada, valor inválido ou saldo insuficiente.

## Como o código foi construído

O projeto foi melhorado aos poucos por meio de refatorações:

- No início, a maior parte da lógica estava concentrada no `Main`.
- Depois, foi criada a classe `Banco` para separar o gerenciamento das contas.
- Em seguida, o fluxo do terminal foi movido para `AplicacaoBancaria`, deixando o `Main` apenas como inicializador.
- A transferência passou a retornar um `ResultadoTransferencia`, evitando depender apenas de mensagens soltas ou valores booleanos.
- O extrato deixou de ser uma lista de `String` e passou a usar objetos `Transacao`, deixando cada movimentação mais organizada.
- As listas internas retornam cópias, como em `listarContas()` e `getExtrato()`, para proteger o encapsulamento.
- Alguns campos foram marcados como `final` porque recebem valor no construtor e não precisam mudar depois, como número da conta, titular e lista de transações.

## Regras principais

- Não é permitido criar duas contas com o mesmo número.
- Depósitos, saques e transferências precisam ter valor maior que zero.
- Saques e transferências dependem de saldo suficiente.
- Transferências validam conta de origem, conta de destino, valor e saldo.
- O extrato registra depósitos, saques e transferências enviadas ou recebidas.

## Conceitos praticados

- Sintaxe básica Java
- Aplicação de terminal com `Scanner`
- Estruturas de decisão com `if/else` e `switch`
- Estruturas de repetição com `while` e `for-each`
- Métodos e organização de comportamento
- Classes, objetos e construtores
- Encapsulamento com atributos privados e getters
- Separação de responsabilidades entre `Main`, aplicação, banco e conta
- Uso de `ArrayList`
- Uso de `enum` para representar tipos e resultados
- Registro de data e hora com `LocalDateTime`
- Formatação de informações para exibição no terminal
- Tratamento de entradas inválidas com `try/catch`
- Retorno defensivo de listas para proteger dados internos
- Uso de `final` em campos que não mudam depois da construção do objeto
- Organização do código em pacotes

## Requisitos

- Java JDK instalado
- Terminal para compilar e executar o projeto

## Como executar

Na raiz do projeto, compile os arquivos Java:

```bash
javac -d out app/Main.java app/AplicacaoBancaria.java model/Conta.java model/Banco.java model/Transacao.java model/TipoOperacao.java model/ResultadoTransferencia.java
```

Depois execute a classe principal:

```bash
java -cp out app.Main
```

## Estrutura do projeto

```text
.
├── app
│   ├── AplicacaoBancaria.java
│   └── Main.java
├── model
│   ├── Banco.java
│   ├── Conta.java
│   ├── ResultadoTransferencia.java
│   ├── TipoOperacao.java
│   └── Transacao.java
└── README.md
```

## Observação

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. A ideia principal é entender como dividir responsabilidades entre classes e evoluir o código gradualmente sem tornar o `Main` responsável por tudo.
