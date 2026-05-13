# Simulador de Conta Bancária em Java

Simulador de conta bancária desenvolvido em Java puro, com execução pelo terminal. O projeto tem objetivo educacional e foi criado para praticar programação orientada a objetos, encapsulamento, separação de responsabilidades, enums, testes automatizados e organização de um projeto Java com Maven.

O projeto não usa Spring Boot, banco de dados, API web ou interface gráfica. Toda a interação acontece pelo terminal.

## Funcionalidades

- Criar conta
- Realizar depósito
- Realizar saque
- Consultar saldo de uma conta
- Listar contas
- Buscar conta por número
- Transferir valores entre contas
- Gerar extrato de operações

## Estrutura do projeto

O projeto usa Maven e segue a estrutura padrão de código-fonte e testes:

```text
.
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   └── java
│   │       ├── app
│   │       │   ├── AplicacaoBancaria.java
│   │       │   └── Main.java
│   │       └── model
│   │           ├── Banco.java
│   │           ├── Conta.java
│   │           ├── ResultadoTransferencia.java
│   │           ├── TipoOperacao.java
│   │           └── Transacao.java
│   └── test
│       └── java
│           └── model
│               ├── BancoTest.java
│               └── ContaTest.java
└── target
```

A pasta `target/` é gerada automaticamente pelo Maven durante compilação, execução e testes. Ela não faz parte do código-fonte e deve ser ignorada pelo Git.

## Organização da aplicação

A aplicação foi dividida em pacotes para separar melhor as responsabilidades:

- `app.Main`: ponto de entrada do programa. Cria uma instância de `AplicacaoBancaria` e chama o método `executar()`.
- `app.AplicacaoBancaria`: controla o menu, a leitura de dados no terminal e o fluxo da aplicação. Ela recebe as escolhas do usuário e chama o `Banco` para realizar as operações.
- `model.Banco`: gerencia a lista de contas, busca contas por número, informa a quantidade de contas cadastradas e coordena transferências entre contas.
- `model.Conta`: representa uma conta bancária individual. Guarda número, titular, saldo e extrato. Também contém as operações próprias da conta, como depositar, sacar, creditar, debitar e registrar movimentações.
- `model.Transacao`: representa um registro do extrato. Guarda tipo da operação, valor, data/hora e descrição, além de formatar a saída exibida no terminal.
- `model.TipoOperacao`: enum que define os tipos de operação registrados no extrato, como depósito, saque, transferência enviada e transferência recebida.
- `model.ResultadoTransferencia`: enum que representa os possíveis resultados de uma tentativa de transferência, como sucesso, conta não encontrada, valor inválido, contas iguais ou saldo insuficiente.

## Regras principais

- Não é permitido criar duas contas com o mesmo número pelo fluxo da aplicação.
- Depósitos, saques e transferências precisam ter valor maior que zero.
- Saques e transferências dependem de saldo suficiente.
- Transferências validam conta de origem, conta de destino, valor, saldo e se as contas são diferentes.
- O extrato registra depósitos, saques e transferências enviadas ou recebidas.

## Como executar

Na raiz do projeto, execute a aplicação com Maven:

```bash
mvn compile exec:java -Dexec.mainClass=app.Main
```

Se o plugin de execução não estiver configurado no ambiente, também é possível compilar com Maven e executar a classe principal a partir dos arquivos compilados:

```bash
mvn compile
java -cp target/classes app.Main
```

## Testes automatizados

Os testes automatizados ficam em `src/test/java/model` e usam JUnit 5. As classes de teste atuais são:

- `ContaTest`
- `BancoTest`

Para rodar todos os testes, execute na raiz do projeto:

```bash
mvn test
```

Os testes cobrem regras importantes do domínio bancário:

- depósito válido;
- depósito inválido;
- saque válido;
- saque com saldo insuficiente;
- busca de conta existente e inexistente;
- transferência válida;
- transferência com saldo insuficiente;
- transferência para a mesma conta;
- registros de extrato para depósito, saque e transferência.

## Conceitos praticados

- Sintaxe básica Java
- Aplicação de terminal com `Scanner`
- Estruturas de decisão com `if/else` e `switch`
- Estruturas de repetição com `while` e `for-each`
- Métodos e organização de comportamento
- Classes, objetos e construtores
- Encapsulamento com atributos privados e getters
- Separação de responsabilidades entre entrada da aplicação, regras de banco e regras de conta
- Uso de `ArrayList`
- Uso de `enum` para representar tipos de operação e resultados de transferência
- Registro de data e hora com `LocalDateTime`
- Formatação de informações para exibição no terminal
- Tratamento de entradas inválidas com `try/catch`
- Retorno defensivo de listas para proteger dados internos
- Organização do código em pacotes
- Estrutura de projeto Java com Maven
- Testes automatizados com JUnit 5

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

## Observação

Este é um projeto educacional, criado para praticar conceitos fundamentais de Java. A ideia principal é entender como dividir responsabilidades entre classes, testar regras de domínio e evoluir o código gradualmente sem tornar o `Main` responsável por tudo.
