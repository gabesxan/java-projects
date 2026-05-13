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

## Atualizações recentes

- README atualizado para refletir a estrutura atual do projeto.
- Documentação reorganizada para explicar melhor o papel de `Main`, `AplicacaoBancaria`, `Banco`, `Conta`, `Transacao`, `TipoOperacao` e `ResultadoTransferencia`.
- Comando de compilação atualizado para listar explicitamente todos os arquivos Java do projeto.
- Seção de conceitos praticados revisada com foco em POO, encapsulamento, enums, extrato com objetos e separação de responsabilidades.

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

## Checklist de testes manuais

Depois de executar o programa pelo terminal, use o menu para validar manualmente as principais funcionalidades:

- [ ] Criar uma conta com sucesso: escolha a opção de criar conta, informe um número ainda não usado e um nome de titular. O sistema deve confirmar a criação da conta.
- [ ] Tentar criar uma conta com número repetido: use novamente um número de conta já cadastrado. O sistema deve informar que já existe uma conta com esse número.
- [ ] Depositar um valor válido: escolha a opção de depósito, informe uma conta existente e um valor maior que zero. O saldo da conta deve aumentar.
- [ ] Tentar depositar um valor inválido: informe valor zero ou negativo no depósito. O sistema deve recusar a operação.
- [ ] Sacar um valor válido: faça um saque com valor maior que zero e menor ou igual ao saldo disponível. O saldo da conta deve diminuir.
- [ ] Tentar sacar valor maior que o saldo: informe um valor acima do saldo disponível. O sistema deve recusar o saque e manter o saldo.
- [ ] Consultar saldo: escolha a opção de consulta e informe uma conta existente. O sistema deve mostrar número, titular e saldo atual.
- [ ] Listar contas: cadastre uma ou mais contas e escolha a opção de listagem. O sistema deve exibir os dados das contas cadastradas.
- [ ] Buscar conta por número: informe o número de uma conta existente. O sistema deve mostrar os dados dessa conta.
- [ ] Transferir entre contas diferentes: crie duas contas, deposite saldo na conta de origem e transfira um valor válido para a conta de destino. O saldo da origem deve diminuir e o saldo do destino deve aumentar.
- [ ] Tentar transferir para a mesma conta: informe o mesmo número para origem e destino. A transferência deve ser recusada.
- [ ] Tentar transferir com saldo insuficiente: informe uma transferência maior que o saldo da conta de origem. O sistema deve recusar a operação.
- [ ] Gerar extrato: realize depósito, saque ou transferência e depois escolha a opção de extrato. O sistema deve listar as operações registradas.
- [ ] Conferir o formato do extrato: cada item deve mostrar data/hora, tipo da operação, valor formatado e descrição.
- [ ] Confirmar que transferência para a mesma conta não altera saldo nem extrato: consulte o saldo e gere o extrato antes e depois da tentativa. O saldo deve permanecer igual e nenhuma nova transação deve ser adicionada.

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
