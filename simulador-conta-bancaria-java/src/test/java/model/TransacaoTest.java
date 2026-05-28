package model;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;



class TransacaoTest {

    @Test
    void deveCriarTransacaoComDadosCorretos() {
        Transacao transacao = new Transacao(
                TipoOperacao.DEPOSITO,
                100,
                "Depósito realizado");

        assertEquals(TipoOperacao.DEPOSITO, transacao.getTipo());
        assertEquals(100, transacao.getValor());
        assertEquals("Depósito realizado", transacao.getDescricao());
        assertNotNull(transacao.getDataHora());
    }

    @Test
    void deveFormatarTransacaoParaExtrato() {
        Transacao transacao = new Transacao(
                TipoOperacao.DEPOSITO,
                100,
                "Depósito realizado");

        String texto = transacao.formatarParaExtrato();

        assertTrue(texto.contains("Depósito"));
        assertTrue(texto.contains("100"));
        assertTrue(texto.contains("Depósito realizado"));
    }

    @Test
    void tipoOperacaoDeveTerDescricaoAmigavel() {
        assertEquals("Depósito", TipoOperacao.DEPOSITO.getDescricao());
        assertEquals("Saque", TipoOperacao.SAQUE.getDescricao());
        assertEquals("Transferência enviada", TipoOperacao.TRANSFERENCIA_ENVIADA.getDescricao());
        assertEquals("Transferência recebida", TipoOperacao.TRANSFERENCIA_RECEBIDA.getDescricao());
    }

    @Test
    void deveCriarTransacaoComDataHoraInformada() {
        LocalDateTime dataHora = LocalDateTime.of(2026, 5, 14, 10, 30);

        Transacao transacao = new Transacao(
                TipoOperacao.DEPOSITO,
                100.0,
                dataHora,
                "Depósito carregado do arquivo");

        assertEquals(TipoOperacao.DEPOSITO, transacao.getTipo());
        assertEquals(100.0, transacao.getValor());
        assertEquals(dataHora, transacao.getDataHora());
        assertEquals("Depósito carregado do arquivo", transacao.getDescricao());
    }

}
