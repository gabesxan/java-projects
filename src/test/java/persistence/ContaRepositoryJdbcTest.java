package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;

class ContaRepositoryJdbcTest {

    @Test
    void deveSalvarContaNoBanco() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc repository = new ContaRepositoryJdbc(conexaoBanco);

        Conta conta = new Conta(1, "Gabriel", 150.0);

        repository.salvar(conta);

        try (Connection conexao = conexaoBanco.conectar();
                Statement statement = conexao.createStatement();
                ResultSet resultado = statement.executeQuery("SELECT * FROM contas WHERE numero = 1")) {

            resultado.next();

            assertEquals(1, resultado.getInt("numero"));
            assertEquals("Gabriel", resultado.getString("titular"));
            assertEquals(150.0, resultado.getDouble("saldo"));
        }
    }

    @Test
    void deveCarregarTodasAsContasDoBanco() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);

        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc repository = new ContaRepositoryJdbc(conexaoBanco);

        repository.salvar(new Conta(1, "Gabriel", 150.0));
        repository.salvar(new Conta(2, "Maria", 80.0));

        List<Conta> contas = repository.carregarTodas();

        assertEquals(2, contas.size());
        assertEquals(1, contas.get(0).getNumero());
        assertEquals("Gabriel", contas.get(0).getTitular());
        assertEquals(150.0, contas.get(0).getSaldo());
        assertEquals(2, contas.get(1).getNumero());
        assertEquals("Maria", contas.get(1).getTitular());
        assertEquals(80.0, contas.get(1).getSaldo());
    }

    @Test
    void deveBuscarContaPorNumero() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc repository = new ContaRepositoryJdbc(conexaoBanco);

        repository.salvar(new Conta(1, "Gabriel", 150.0));

        Conta conta = repository.buscarPorNumero(1);

        assertEquals(1, conta.getNumero());
        assertEquals("Gabriel", conta.getTitular());
        assertEquals(150.0, conta.getSaldo());
    }

    @Test
    void deveRetornarNullAoBuscarContaInexistente() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:" + caminhoBanco);
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);
        inicializador.inicializar();

        ContaRepositoryJdbc repository = new ContaRepositoryJdbc(conexaoBanco);

        Conta conta = repository.buscarPorNumero(999);

        assertNull(conta);
    }

    private Path caminhoBanco;

    @BeforeEach
    void preparar() throws Exception {
        caminhoBanco = Path.of("target", "test-data", "conta-repository-jdbc-test.db");
        Files.createDirectories(caminhoBanco.getParent());
        Files.deleteIfExists(caminhoBanco);
    }
}
