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
        return contas;
    }
}
