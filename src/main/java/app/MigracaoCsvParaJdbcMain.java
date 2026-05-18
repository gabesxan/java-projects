package app;

import java.nio.file.Path;

import persistence.ConexaoBanco;
import persistence.ContaRepositoryCsv;
import persistence.ContaRepositoryJdbc;
import persistence.InicializadorBanco;
import persistence.MigradorCsvParaJdbc;
import persistence.TransacaoRepositoryCsv;
import persistence.TransacaoRepositoryJdbc;

public class MigracaoCsvParaJdbcMain {

    public static void main(String[] args) {
        try {
            ConexaoBanco conexaoBanco = new ConexaoBanco();
            InicializadorBanco inicializadorBanco = new InicializadorBanco(conexaoBanco);
            inicializadorBanco.inicializar();

            ContaRepositoryCsv contaRepositoryCsv = new ContaRepositoryCsv(Path.of("data", "contas.csv"));
            TransacaoRepositoryCsv transacaoRepositoryCsv = new TransacaoRepositoryCsv(Path.of("data", "transacoes.csv"));

            ContaRepositoryJdbc contaRepositoryJdbc = new ContaRepositoryJdbc(conexaoBanco);
            TransacaoRepositoryJdbc transacaoRepositoryJdbc = new TransacaoRepositoryJdbc(conexaoBanco);

            MigradorCsvParaJdbc migrador = new MigradorCsvParaJdbc(
                    contaRepositoryCsv,
                    transacaoRepositoryCsv,
                    contaRepositoryJdbc,
                    transacaoRepositoryJdbc);

            migrador.migrar();

            System.out.println("Migração de CSV para SQLite concluída com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao migrar dados de CSV para SQLite.");
            e.printStackTrace();
        }
    }
}
