package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;
import model.TipoOperacao;
import model.Transacao;

class TransacaoRepositoryJdbcTest {

    private Path caminhoBanco;

    @BeforeEach
    void preparar() throws Exception {
        caminhoBanco = Path.of("target", "test-data", "transacao-repository-jdbc-test.db");
        Files.createDirectories(caminhoBanco.getParent());
        Files.deleteIfExists(caminhoBanco);
    }

    @Test
    void deveSalvarTransacaoNoBanco() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc contaRepository = new ContaRepositoryJdbc(conexaoBanco);
        contaRepository.salvar(new Conta(1, "Gabriel", 150.0));

        TransacaoRepositoryJdbc transacaoRepository = new TransacaoRepositoryJdbc(conexaoBanco);

        LocalDateTime dataHora = LocalDateTime.of(2026, 5, 18, 14, 30);
        Transacao transacao = new Transacao(
                TipoOperacao.DEPOSITO,
                150.0,
                dataHora,
                "Depósito realizado");

        transacaoRepository.salvar(1, transacao);

        try (Connection conexao = conexaoBanco.conectar();
                Statement statement = conexao.createStatement();
                ResultSet resultado = statement.executeQuery("SELECT * FROM transacoes WHERE numero_conta = 1")) {

            resultado.next();

            assertEquals(1, resultado.getInt("numero_conta"));
            assertEquals("DEPOSITO", resultado.getString("tipo"));
            assertEquals(150.0, resultado.getDouble("valor"));
            assertEquals(dataHora.toString(), resultado.getString("data_hora"));
            assertEquals("Depósito realizado", resultado.getString("descricao"));
        }
    }

    @Test
    void deveCarregarTransacoesDaConta() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc contaRepository = new ContaRepositoryJdbc(conexaoBanco);
        contaRepository.salvar(new Conta(1, "Gabriel", 150.0));

        TransacaoRepositoryJdbc transacaoRepository = new TransacaoRepositoryJdbc(conexaoBanco);

        LocalDateTime primeiraData = LocalDateTime.of(2026, 5, 18, 14, 30);
        LocalDateTime segundaData = LocalDateTime.of(2026, 5, 18, 15, 0);

        transacaoRepository.salvar(1, new Transacao(
                TipoOperacao.DEPOSITO,
                150.0,
                primeiraData,
                "Depósito realizado"));

        transacaoRepository.salvar(1, new Transacao(
                TipoOperacao.SAQUE,
                50.0,
                segundaData,
                "Saque realizado"));

        List<Transacao> transacoes = transacaoRepository.carregarPorConta(1);

        assertEquals(2, transacoes.size());
        assertEquals(TipoOperacao.DEPOSITO, transacoes.get(0).getTipo());
        assertEquals(150.0, transacoes.get(0).getValor());
        assertEquals(primeiraData, transacoes.get(0).getDataHora());
        assertEquals("Depósito realizado", transacoes.get(0).getDescricao());

        assertEquals(TipoOperacao.SAQUE, transacoes.get(1).getTipo());
        assertEquals(50.0, transacoes.get(1).getValor());
        assertEquals(segundaData, transacoes.get(1).getDataHora());
        assertEquals("Saque realizado", transacoes.get(1).getDescricao());
    }

    @Test
    void deveSalvarTodasAsTransacoesDaConta() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc contaRepository = new ContaRepositoryJdbc(conexaoBanco);

        Conta conta = new Conta(1, "Gabriel", 100.0);
        conta.depositar(50);
        conta.sacar(30);

        contaRepository.salvar(conta);

        TransacaoRepositoryJdbc transacaoRepository = new TransacaoRepositoryJdbc(conexaoBanco);
        transacaoRepository.salvarTodasDaConta(conta);

        List<Transacao> transacoes = transacaoRepository.carregarPorConta(1);

        assertEquals(2, transacoes.size());
        assertEquals(TipoOperacao.DEPOSITO, transacoes.get(0).getTipo());
        assertEquals(TipoOperacao.SAQUE, transacoes.get(1).getTipo());
    }
}