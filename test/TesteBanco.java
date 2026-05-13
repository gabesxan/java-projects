package test;

import model.Banco;
import model.Conta;
import model.ResultadoTransferencia;

public class TesteBanco {
    public static void main(String[] args) {
        Banco banco = new Banco();
        Conta conta = new Conta(1, "Gabriel");

        banco.adicionarConta(conta);

        boolean resultado = conta.depositar(100);

        verificar(resultado && conta.getSaldo() == 100, "depósito válido");

        Conta contaDepositoInvalido = new Conta(2, "Maria");

        boolean depositoInvalido = contaDepositoInvalido.depositar(-50);

        verificar(!depositoInvalido && contaDepositoInvalido.getSaldo() == 0, "depósito inválido");

        boolean saqueValido = conta.sacar(40);

        verificar(saqueValido && conta.getSaldo() == 60, "saque válido");

        Conta contaSaldoInsuficiente = new Conta(4, "Ana");
        contaSaldoInsuficiente.depositar(30);
        boolean saqueSemSaldo = contaSaldoInsuficiente.sacar(50);
        verificar(!saqueSemSaldo && contaSaldoInsuficiente.getSaldo() == 30, "saque com saldo insuficiente");

        Banco bancoBusca = new Banco();
        Conta contaBusca = new Conta(5, "Carlos");
        bancoBusca.adicionarConta(contaBusca);
        Conta contaEncontrada = bancoBusca.buscarContaPorNumero(5);
        verificar(contaEncontrada != null && contaEncontrada.getTitular().equals("Carlos"), "buscar conta existente");

        Conta contaNaoEncontrada = bancoBusca.buscarContaPorNumero(999);
        verificar(contaNaoEncontrada == null, "buscar conta inexistente");

        Banco bancoTransferencia = new Banco();
        Conta origem = new Conta(6, "Origem");
        Conta destino = new Conta(7, "Destino");
        origem.depositar(100);
        bancoTransferencia.adicionarConta(origem);
        bancoTransferencia.adicionarConta(destino);

        var resultadoTransferencia = bancoTransferencia.transferir(6, 7, 40);

        verificar(
                resultadoTransferencia == ResultadoTransferencia.SUCESSO
                        && origem.getSaldo() == 60
                        && destino.getSaldo() == 40,
                "transferência válida");

        Banco bancoSaldoInsuficiente = new Banco();
        Conta origemSemSaldo = new Conta(8, "Origem sem saldo");
        Conta destinoSaldo = new Conta(9, "Destino");
        origemSemSaldo.depositar(20);
        bancoSaldoInsuficiente.adicionarConta(origemSemSaldo);
        bancoSaldoInsuficiente.adicionarConta(destinoSaldo);

        var resultadoSaldoInsuficiente = bancoSaldoInsuficiente.transferir(8, 9, 50);
        verificar(
                resultadoSaldoInsuficiente == model.ResultadoTransferencia.SALDO_INSUFICIENTE
                        && origemSemSaldo.getSaldo() == 20
                        && destinoSaldo.getSaldo() == 0,
                "transferência com saldo insuficiente");

        Banco bancoMesmaConta = new Banco();
        Conta contaMesma = new Conta(10, "Mesma conta");
        contaMesma.depositar(100);
        bancoMesmaConta.adicionarConta(contaMesma);

        var resultadoMesmaConta = bancoMesmaConta.transferir(10, 10, 30);

        verificar(
                resultadoMesmaConta == model.ResultadoTransferencia.CONTAS_IGUAIS
                        && contaMesma.getSaldo() == 100,
                "transferência para mesma conta");

        Conta contaExtrato = new Conta(11, "Extrato");
        contaExtrato.depositar(100);

        verificar(contaExtrato.getExtrato().size() == 1, "extrato registra depósito");

        contaExtrato.sacar(40);

        verificar(contaExtrato.getExtrato().size() == 2, "extrato registra saque");

        Banco bancoExtratoMesmaConta = new Banco();
        Conta contaExtratoMesma = new Conta(12, "Extrato mesma conta");
        contaExtratoMesma.depositar(100);
        int quantidadeAntes = contaExtratoMesma.getExtrato().size();

        bancoExtratoMesmaConta.adicionarConta(contaExtratoMesma);
        bancoExtratoMesmaConta.transferir(12, 12, 50);

        int quantidadeDepois = contaExtratoMesma.getExtrato().size();

        verificar(quantidadeAntes == quantidadeDepois, "transferência para mesma conta não registra extrato");

    }

    private static void verificar(boolean condicao, String nomeDoTeste) {
        if (condicao) {
            System.out.println("PASSOU: " + nomeDoTeste);
        } else {
            System.out.println("FALHOU: " + nomeDoTeste);
        }
    }
}
