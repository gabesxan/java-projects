package persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

class ConexaoBancoTest {
    @Test
    void deveAbrirConexaoComBanco() throws Exception {
        ConexaoBanco conexaoBanco = new ConexaoBanco("jdbc:sqlite:target/test-data/conexao-banco-test.db");

        try (Connection conexao = conexaoBanco.conectar()) {
            assertNotNull(conexao);
            assertFalse(conexao.isClosed());
        }
    }

}
