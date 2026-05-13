package model;

import java.util.ArrayList;

public class Conta {
    private final ArrayList<Transacao> extrato;
    private final String titular;
    private double saldo;
    private final int numero;

    public Conta(int numero, String titularInicial) {
        this.numero = numero;
        titular = titularInicial;
        saldo = 0.0;
        extrato = new ArrayList<>();
    }

    public ArrayList<Transacao> getExtrato() {
        return new ArrayList<>(extrato);
    }

    public int getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void registrarOperacao(TipoOperacao tipo, double valor, String descricao) {
        Transacao transacao = new Transacao(tipo, valor, descricao);
        extrato.add(transacao);
    }

    public boolean depositar(double valor) {
        if (valor <= 0) {
            return false;
        }

        saldo = saldo + valor;
        registrarOperacao(TipoOperacao.DEPOSITO, valor, "Depósito realizado");
        return true;
    }

    public boolean creditarSemExtrato(double valor) {
        if (valor <= 0) {
            return false;
        }
        saldo = saldo + valor;
        return true;
    }

    public boolean debitarSemExtrato(double valor) {
        if (valor <= 0) {
            return false;

        }
        if (saldo >= valor) {
            saldo = saldo - valor;
            return true;
        }
        return false;
    }

    public boolean sacar(double valor) {
        if (valor <= 0) {
            return false;
        }

        if (saldo >= valor) {
            saldo = saldo - valor;
            registrarOperacao(TipoOperacao.SAQUE, valor, "Saque realizado");
            return true;
        }
        return false;
    }
}
