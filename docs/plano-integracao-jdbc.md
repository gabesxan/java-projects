# Plano de integração JDBC

## Estado atual

A aplicação atualmente usa persistência em arquivos CSV.

Arquivos usados:

- `data/contas.csv`
- `data/transacoes.csv`

Esses arquivos são lidos ao iniciar a aplicação e atualizados após operações relevantes.

## O que já existe em JDBC

Já foram criadas as primeiras classes para trabalhar com SQLite via JDBC:

- `ConexaoBanco`
- `InicializadorBanco`
- `ContaRepositoryJdbc`
- `TransacaoRepositoryJdbc`

Também já existem testes automatizados cobrindo:

- abertura de conexão;
- criação das tabelas;
- salvamento e carregamento de contas;
- salvamento e carregamento de transações.

## O que ainda usa CSV

A classe `AplicacaoBancaria` ainda usa os repositories baseados em CSV:

- `ContaRepository`
- `TransacaoRepository`

Ou seja, mesmo com JDBC implementado e testado, a aplicação de terminal ainda não usa o banco SQLite.

## Por que não trocar tudo de uma vez

Trocar CSV por JDBC diretamente pode misturar várias mudanças ao mesmo tempo:

- mudança de infraestrutura;
- mudança no carregamento inicial;
- mudança no salvamento após operações;
- possível alteração no tratamento de erros;
- risco de quebrar a aplicação de terminal.

Por isso, a integração deve ser feita em etapas pequenas.

## Plano de integração

### Etapa 1: manter CSV funcionando

Antes de integrar JDBC, garantir que a versão atual com CSV continua funcionando e com testes passando.

### Etapa 2: inicializar o banco ao iniciar a aplicação

Adicionar o uso de `InicializadorBanco` para garantir que as tabelas existam.

Nesta etapa, a aplicação ainda pode continuar usando CSV.

### Etapa 3: carregar contas pelo JDBC

Trocar apenas o carregamento inicial de contas para usar `ContaRepositoryJdbc`.

Ainda não trocar transações.

### Etapa 4: salvar contas pelo JDBC

Depois que o carregamento funcionar, salvar contas no banco após criação, depósito, saque e transferência.

### Etapa 5: carregar transações pelo JDBC

Carregar o extrato das contas usando `TransacaoRepositoryJdbc`.

### Etapa 6: salvar transações pelo JDBC

Salvar transações no banco após depósito, saque e transferência.

### Etapa 7: remover ou manter CSV

Depois que JDBC estiver funcionando, decidir se os repositories CSV serão mantidos como estudo/backup ou se serão removidos da aplicação.

## Cuidados

- Não alterar regras de negócio em `Conta` e `Banco`.
- Não misturar integração JDBC com novas funcionalidades.
- Rodar `mvn test` após cada etapa.
- Testar manualmente a aplicação de terminal após cada mudança.
- Não commitar arquivos `.db`.