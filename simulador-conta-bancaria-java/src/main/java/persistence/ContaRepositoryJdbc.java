package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Conta;

public class ContaRepositoryJdbc {

    private final ConexaoBanco conexaoBanco;

    public ContaRepositoryJdbc(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public void salvar(Conta conta) throws SQLException {
        try (Connection conexao = conexaoBanco.conectar()) {
            salvar(conta, conexao);
        }
    }

    public void salvar(Conta conta, Connection conexao) throws SQLException {
        String sql = """
                INSERT INTO contas (numero, titular, saldo)
                VALUES (?, ?, ?)
                ON CONFLICT(numero) DO UPDATE SET
                    titular = excluded.titular,
                    saldo = excluded.saldo
                """;

        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setInt(1, conta.getNumero());
            statement.setString(2, conta.getTitular());
            statement.setDouble(3, conta.getSaldo());

            statement.executeUpdate();
        }
    }

    public List<Conta> carregarTodas() throws SQLException {
        List<Conta> contas = new ArrayList<>();

        String sql = "SELECT numero, titular, saldo FROM contas";

        try (Connection conexao = conexaoBanco.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql);
                ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                int numero = resultado.getInt("numero");
                String titular = resultado.getString("titular");
                double saldo = resultado.getDouble("saldo");

                Conta conta = new Conta(numero, titular, saldo);
                contas.add(conta);
            }
        }

        return contas;
    }

    public Conta buscarPorNumero(int numeroBuscado) throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";

        try (Connection conexao = conexaoBanco.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setInt(1, numeroBuscado);

            try (ResultSet resultado = statement.executeQuery()) {
                if (resultado.next()) {
                    int numero = resultado.getInt("numero");
                    String titular = resultado.getString("titular");
                    double saldo = resultado.getDouble("saldo");

                    return new Conta(numero, titular, saldo);
                }
            }
        }

        return null;
    }
}