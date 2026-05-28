package persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBanco {

    private final ConexaoBanco conexaoBanco;

    public InicializadorBanco(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public void inicializar() throws SQLException {
        try (Connection conexao = conexaoBanco.conectar();
             Statement statement = conexao.createStatement()) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS contas (
                        numero INTEGER PRIMARY KEY,
                        titular TEXT NOT NULL,
                        saldo REAL NOT NULL
                    )
                    """);

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS transacoes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        numero_conta INTEGER NOT NULL,
                        tipo TEXT NOT NULL,
                        valor REAL NOT NULL,
                        data_hora TEXT NOT NULL,
                        descricao TEXT NOT NULL,
                        FOREIGN KEY (numero_conta) REFERENCES contas(numero)
                    )
                    """);
        }
    }
}