package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TipoOperacaoTest {

    @Test
    void deveRetornarDescricaoDeDeposito() {
        assertEquals("Depósito", TipoOperacao.DEPOSITO.getDescricao());
    }

    @Test
    void deveRetornarDescricaoDeSaque() {
        assertEquals("Saque", TipoOperacao.SAQUE.getDescricao());
    }

    @Test
    void deveRetornarDescricaoDeTransferenciaEnviada() {
        assertEquals("Transferência enviada", TipoOperacao.TRANSFERENCIA_ENVIADA.getDescricao());
    }

    @Test
    void deveRetornarDescricaoDeTransferenciaRecebida() {
        assertEquals("Transferência recebida", TipoOperacao.TRANSFERENCIA_RECEBIDA.getDescricao());
    }
}
