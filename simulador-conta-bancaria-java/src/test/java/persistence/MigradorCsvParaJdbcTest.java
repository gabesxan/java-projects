package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;
import model.Transacao;

class MigradorCsvParaJdbcTest {

    private Path pastaTeste;
    private Path caminhoContasCsv;
    private Path caminhoTransacoesCsv;
    private Path caminhoBanco;

    @BeforeEach
    void preparar() throws Exception {
        pastaTeste = Path.of("target", "test-data", "migrador");
        caminhoContasCsv = pastaTeste.resolve("contas.csv");
        caminhoTransacoesCsv = pastaTeste.resolve("transacoes.csv");
        caminhoBanco = pastaTeste.resolve("banco-migracao-test.db");

        Files.createDirectories(pastaTeste);
        Files.deleteIfExists(caminhoContasCsv);
        Files.deleteIfExists(caminhoTransacoesCsv);
        Files.deleteIfExists(caminhoBanco);
    }

    @Test
    void deveMigrarContasETransacoesDoCsvParaJdbc() throws Exception {
        Files.write(caminhoContasCsv, List.of(
                "1;Gabriel;150.0"));

        Files.write(caminhoTransacoesCsv, List.of(
                "1;DEPOSITO;200.0;2026-05-18T10:00;Depósito realizado",
                "1;SAQUE;50.0;2026-05-18T11:00;Saque realizado"));

        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializadorBanco = new InicializadorBanco(conexaoBanco);
        inicializadorBanco.inicializar();

        ContaRepositoryCsv contaRepositoryCsv = new ContaRepositoryCsv(caminhoContasCsv);
        TransacaoRepositoryCsv transacaoRepositoryCsv = new TransacaoRepositoryCsv(caminhoTransacoesCsv);
        ContaRepositoryJdbc contaRepositoryJdbc = new ContaRepositoryJdbc(conexaoBanco);
        TransacaoRepositoryJdbc transacaoRepositoryJdbc = new TransacaoRepositoryJdbc(conexaoBanco);

        MigradorCsvParaJdbc migrador = new MigradorCsvParaJdbc(
                contaRepositoryCsv,
                transacaoRepositoryCsv,
                contaRepositoryJdbc,
                transacaoRepositoryJdbc);

        migrador.migrar();

        List<Conta> contas = contaRepositoryJdbc.carregarTodas();

        assertEquals(1, contas.size());
        assertEquals(1, contas.get(0).getNumero());
        assertEquals("Gabriel", contas.get(0).getTitular());
        assertEquals(150.0, contas.get(0).getSaldo());

        List<Transacao> transacoes = transacaoRepositoryJdbc.carregarPorConta(1);

        assertEquals(2, transacoes.size());
        assertEquals(200.0, transacoes.get(0).getValor());
        assertEquals(50.0, transacoes.get(1).getValor());
    }
}
