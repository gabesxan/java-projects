package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Conta;
import model.TipoOperacao;
import model.Transacao;

public class TransacaoRepositoryJdbc {

    private final ConexaoBanco conexaoBanco;

    public TransacaoRepositoryJdbc(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public void salvar(int numeroConta, Transacao transacao) throws SQLException {
        String sql = """
                INSERT INTO transacoes (numero_conta, tipo, valor, data_hora, descricao)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = conexaoBanco.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setInt(1, numeroConta);
            statement.setString(2, transacao.getTipo().name());
            statement.setDouble(3, transacao.getValor());
            statement.setString(4, transacao.getDataHora().toString());
            statement.setString(5, transacao.getDescricao());

            statement.executeUpdate();
        }
    }

    public List<Transacao> carregarPorConta(int numeroConta) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();

        String sql = """
                SELECT tipo, valor, data_hora, descricao
                FROM transacoes
                WHERE numero_conta = ?
                ORDER BY data_hora
                """;

        try (Connection conexao = conexaoBanco.conectar();

                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setInt(1, numeroConta);

            try (ResultSet resultado = statement.executeQuery()) {
                while (resultado.next()) {
                    TipoOperacao tipo = TipoOperacao.valueOf(resultado.getString("tipo"));
                    double valor = resultado.getDouble("valor");
                    LocalDateTime dataHora = LocalDateTime.parse(resultado.getString("data_hora"));
                    String descricao = resultado.getString("descricao");

                    Transacao transacao = new Transacao(tipo, valor, dataHora, descricao);
                    transacoes.add(transacao);
                }
            }
        }

        return transacoes;
    }

    public void salvarTodasDaConta(Conta conta) throws SQLException {
        apagarPorConta(conta.getNumero());

        for (Transacao transacao : conta.getExtrato()) {
            salvar(conta.getNumero(), transacao);
        }
    }

    public void apagarPorConta(int numeroConta) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE numero_conta = ?";

        try (Connection conexao = conexaoBanco.conectar();
                PreparedStatement statement = conexao.prepareStatement(sql)) {

            statement.setInt(1, numeroConta);
            statement.executeUpdate();
        }
    }
}
