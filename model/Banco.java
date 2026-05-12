package model;

import java.util.ArrayList;

public class Banco {
    private ArrayList<Conta> contas;

    public Banco() {
        contas = new ArrayList<>();
    }

    public Conta buscarContaPorNumero(int numeroBuscado) {
        for (Conta c : contas) {
            if (c.getNumero() == numeroBuscado) {
                return c;
            }
        }

        return null;
    }

    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public boolean estaVazio() {
        return contas.isEmpty();
    }

    public int quantidadeDeContas() {
        return contas.size();
    }

    public ArrayList<Conta> listarContas() {
        return new ArrayList<>(contas);
    }

    public ResultadoTransferencia transferir(int numeroOrigem, int numeroDestino, double valor) {
        Conta origem = buscarContaPorNumero(numeroOrigem);
        Conta destino = buscarContaPorNumero(numeroDestino);

        if (origem == null) {
            return ResultadoTransferencia.CONTA_ORIGEM_NAO_ENCONTRADA;
        }

        if (destino == null) {
            return ResultadoTransferencia.CONTA_DESTINO_NAO_ENCONTRADA;
        }

        if (valor <= 0) {
            return ResultadoTransferencia.VALOR_INVALIDO;
        }

        boolean debitoRealizado = origem.debitarSemExtrato(valor);

        if (!debitoRealizado) {
            return ResultadoTransferencia.SALDO_INSUFICIENTE;
        }

        destino.creditarSemExtrato(valor);

        origem.registrarOperacao(
            TipoOperacao.TRANSFERENCIA_ENVIADA,
            valor,
            "Para conta " + destino.getNumero()
        );

        destino.registrarOperacao(
            TipoOperacao.TRANSFERENCIA_RECEBIDA,
            valor,
            "Da conta " + origem.getNumero()
        );

        return ResultadoTransferencia.SUCESSO;
    }
}
