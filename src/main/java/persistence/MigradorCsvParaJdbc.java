package persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Conta;
import model.Transacao;

public class MigradorCsvParaJdbc {

    private final ContaRepository contaRepositoryCsv;
    private final TransacaoRepository transacaoRepositoryCsv;
    private final ContaRepositoryJdbc contaRepositoryJdbc;
    private final TransacaoRepositoryJdbc transacaoRepositoryJdbc;

    public MigradorCsvParaJdbc(
            ContaRepository contaRepositoryCsv,
            TransacaoRepository transacaoRepositoryCsv,
            ContaRepositoryJdbc contaRepositoryJdbc,
            TransacaoRepositoryJdbc transacaoRepositoryJdbc) {
        this.contaRepositoryCsv = contaRepositoryCsv;
        this.transacaoRepositoryCsv = transacaoRepositoryCsv;
        this.contaRepositoryJdbc = contaRepositoryJdbc;
        this.transacaoRepositoryJdbc = transacaoRepositoryJdbc;
    }

    public void migrar() throws IOException, SQLException {
        List<Conta> contas = contaRepositoryCsv.carregar();
        transacaoRepositoryCsv.carregar(contas);

        for (Conta conta : contas) {
            contaRepositoryJdbc.salvar(conta);

            for (Transacao transacao : conta.getExtrato()) {
                transacaoRepositoryJdbc.salvar(conta.getNumero(), transacao);
            }
        }
    }
}