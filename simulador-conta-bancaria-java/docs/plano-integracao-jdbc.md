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

## Etapas concluídas

A integração SQLite/JDBC foi concluída no projeto.

- Adicionar SQLite JDBC.
- Criar `ConexaoBanco`.
- Criar `InicializadorBanco`.
- Criar `ContaRepositoryJdbc`.
- Criar `TransacaoRepositoryJdbc`.
- Criar migração CSV -> SQLite com `MigradorCsvParaJdbc` e `MigracaoCsvParaJdbcMain`.
- Integrar contas e transações na `AplicacaoBancaria`.

