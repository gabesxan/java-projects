package persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Conta;

public class PersistenciaBancoService {

    private final ConexaoBanco conexaoBanco;
    private final ContaRepositoryJdbc contaRepositoryJdbc;
    private final TransacaoRepositoryJdbc transacaoRepositoryJdbc;

    public PersistenciaBancoService(
            ConexaoBanco conexaoBanco,
            ContaRepositoryJdbc contaRepositoryJdbc,
            TransacaoRepositoryJdbc transacaoRepositoryJdbc) {
        this.conexaoBanco = conexaoBanco;
        this.contaRepositoryJdbc = contaRepositoryJdbc;
        this.transacaoRepositoryJdbc = transacaoRepositoryJdbc;
    }

    public void salvarEstado(List<Conta> contas) throws SQLException {
        try (Connection conexao = conexaoBanco.conectar()) {
            conexao.setAutoCommit(false);

            try {
                for (Conta conta : contas) {
                    contaRepositoryJdbc.salvar(conta, conexao);
                    transacaoRepositoryJdbc.salvarTodasDaConta(conta, conexao);
                }

                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw e;
            } catch (RuntimeException e) {
                conexao.rollback();
                throw e;
            } finally {
                conexao.setAutoCommit(true);
            }
        }
    }
}
