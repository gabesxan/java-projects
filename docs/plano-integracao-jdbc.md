# Plano de integração JDBC

## Estado atual

A aplicação agora usa persistência principal em SQLite via JDBC.

O banco de dados SQLite fica em `data/banco.db`.

As tabelas são inicializadas automaticamente ao iniciar a aplicação.

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

A persistência CSV hoje é uma etapa anterior. Os repositories CSV ainda existem no projeto para suportar a migração de dados antigos para SQLite, mas a aplicação principal já usa SQLite para contas e transações.

Os arquivos CSV podem ser mantidos como legado ou removidos em uma etapa futura.

## Por que não trocar tudo de uma vez

Trocar CSV por JDBC diretamente pode misturar várias mudanças ao mesmo tempo:

- mudança de infraestrutura;
- mudança no carregamento inicial;
- mudança no salvamento após operações;
- possível alteração no tratamento de erros;
- risco de quebrar a aplicação de terminal.

Por isso, a integração deve ser feita em etapas pequenas.

## Etapas executadas

O plano de integração JDBC já foi realizado no projeto.

- Criação da conexão SQLite em `ConexaoBanco`.
- Inicialização automática das tabelas com `InicializadorBanco`.
- Criação dos repositories `ContaRepositoryJdbc` e `TransacaoRepositoryJdbc`.
- Migração de dados CSV para SQLite por `MigradorCsvParaJdbc` e `MigracaoCsvParaJdbcMain`.
- Uso de SQLite pela `AplicacaoBancaria` para contas e transações.

## Cuidados

- Não alterar regras de negócio em `Conta` e `Banco`.
- Não misturar integração JDBC com novas funcionalidades.
- Rodar `mvn test` após cada etapa.
- Testar manualmente a aplicação de terminal após cada mudança.
- Não commitar arquivos `.db`.
- `data/banco.db` não deve ser versionado no Git.
- O CSV pode ser mantido como legado ou removido em uma etapa futura.