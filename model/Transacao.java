package model;

import java.time.LocalDateTime;

public class Transacao {
    private final TipoOperacao tipo;
    private final double valor;
    private final LocalDateTime dataHora;
    private final String descricao;

    public Transacao(TipoOperacao tipo, double valor, String descricao) {
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataHora = LocalDateTime.now();
    }

    public String formatarParaExtrato() {
        return dataHora + " - " + tipo + " - R$ " + valor + " - " + descricao;
    }

    public TipoOperacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }
}