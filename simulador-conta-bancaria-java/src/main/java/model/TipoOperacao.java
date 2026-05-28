package model;

public enum TipoOperacao {
    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TRANSFERENCIA_ENVIADA("Transferência enviada"),
    TRANSFERENCIA_RECEBIDA("Transferência recebida");

    private final String descricao;

    TipoOperacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
