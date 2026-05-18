package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL_PADRAO = "jdbc:sqlite:data/banco.db";

    private final String url;

    public ConexaoBanco() {
        this(URL_PADRAO);
    }

    public ConexaoBanco(String url) {
        this.url = url;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }
}