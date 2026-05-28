package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {
    private final TipoOperacao tipo;
    private final double valor;
    private final LocalDateTime dataHora;
    private final String descricao;

    public Transacao(TipoOperacao tipo, double valor, String descricao) {
        this(tipo, valor, LocalDateTime.now(), descricao);
    }
       
    public Transacao(TipoOperacao tipo, double valor, LocalDateTime dataHora, String descricao) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public String formatarParaExtrato() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String valorFormatado = String.format("%.2f", valor);
        String dataFormatada = dataHora.format(formatter);
        return dataFormatada + " - " + tipo.getDescricao() + " - R$ " + valorFormatado + " - " + descricao;
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