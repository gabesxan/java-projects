package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;
import model.TipoOperacao;
import model.Transacao;

class PersistenciaBancoServiceTest {

    private Path caminhoBanco;

    @BeforeEach
    void preparar() throws Exception {
        caminhoBanco = Path.of("target", "test-data", "persistencia-banco-service-test.db");
        Files.createDirectories(caminhoBanco.getParent());
        Files.deleteIfExists(caminhoBanco);
    }

    @Test
    void deveSalvarContasETransacoesEmUmaTransacao() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc contaRepositoryJdbc = new ContaRepositoryJdbc(conexaoBanco);
        TransacaoRepositoryJdbc transacaoRepositoryJdbc = new TransacaoRepositoryJdbc(conexaoBanco);

        PersistenciaBancoService service = new PersistenciaBancoService(
                conexaoBanco,
                contaRepositoryJdbc,
                transacaoRepositoryJdbc);

        Conta conta = new Conta(1, "Gabriel", 100.0);
        conta.depositar(50);

        service.salvarEstado(List.of(conta));

        List<Conta> contas = contaRepositoryJdbc.carregarTodas();
        List<Transacao> transacoes = transacaoRepositoryJdbc.carregarPorConta(1);

        assertEquals(1, contas.size());
        assertEquals(150.0, contas.get(0).getSaldo());

        assertEquals(1, transacoes.size());
        assertEquals(TipoOperacao.DEPOSITO, transacoes.get(0).getTipo());
    }
}
