package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Conta {
    private ArrayList<String> extrato;
    private String titular;
    private double saldo;
    private int numero;
    
    public Conta(int numero, String titularInicial) {
        this.numero = numero;
        titular = titularInicial;
        saldo = 0.0;
        extrato = new ArrayList<>();
    }

    public ArrayList<String> getExtrato(){
        return new ArrayList<>(extrato);
    }

    public int getNumero(){
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void registrarOperacao(TipoOperacao tipo, double valor, String descricao) {
        LocalDateTime agora = LocalDateTime.now();
    extrato.add(agora + " - " + tipo + " - R$ " + valor + " - " + descricao);
    }

    public boolean depositar(double valor) {
        if (valor <= 0) {
            return false;
        }

        saldo = saldo + valor;
        registrarOperacao(TipoOperacao.DEPOSITO, valor, "Depósito realizado");
        return true;
    }

    public void adicionarAoExtrato(String mensagem) {
        extrato.add(mensagem);
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
