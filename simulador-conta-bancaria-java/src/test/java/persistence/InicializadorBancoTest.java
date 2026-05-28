package persistence;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class InicializadorBancoTest {

    @Test
    void deveCriarTabelasNoBanco() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:target/test-data/inicializador-banco-test.db");
        InicializadorBanco inicializador = new InicializadorBanco(conexaoBanco);

        inicializador.inicializar();

        try (Connection conexao = conexaoBanco.conectar();
             ResultSet tabelas = conexao.getMetaData().getTables(null, null, "%", null)) {

            boolean encontrouContas = false;
            boolean encontrouTransacoes = false;

            while (tabelas.next()) {
                String nomeTabela = tabelas.getString("TABLE_NAME");

                if ("contas".equals(nomeTabela)) {
                    encontrouContas = true;
                }

                if ("transacoes".equals(nomeTabela)) {
                    encontrouTransacoes = true;
                }
            }

            assertTrue(encontrouContas);
            assertTrue(encontrouTransacoes);
        }
    }
}
