package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ResultadoTransferenciaTest {

    @Test
    void deveConterResultadoSucesso() {
        assertEquals(ResultadoTransferencia.SUCESSO, ResultadoTransferencia.valueOf("SUCESSO"));
    }

    @Test
    void deveConterResultadoContaOrigemNaoEncontrada() {
        assertEquals(
                ResultadoTransferencia.CONTA_ORIGEM_NAO_ENCONTRADA,
                ResultadoTransferencia.valueOf("CONTA_ORIGEM_NAO_ENCONTRADA")
        );
    }

    @Test
    void deveConterResultadoContaDestinoNaoEncontrada() {
        assertEquals(
                ResultadoTransferencia.CONTA_DESTINO_NAO_ENCONTRADA,
                ResultadoTransferencia.valueOf("CONTA_DESTINO_NAO_ENCONTRADA")
        );
    }

    @Test
    void deveConterResultadoValorInvalido() {
        assertEquals(
                ResultadoTransferencia.VALOR_INVALIDO,
                ResultadoTransferencia.valueOf("VALOR_INVALIDO")
        );
    }

    @Test
    void deveConterResultadoSaldoInsuficiente() {
        assertEquals(
                ResultadoTransferencia.SALDO_INSUFICIENTE,
                ResultadoTransferencia.valueOf("SALDO_INSUFICIENTE")
        );
    }

    @Test
    void deveConterResultadoContasIguais() {
        assertEquals(
                ResultadoTransferencia.CONTAS_IGUAIS,
                ResultadoTransferencia.valueOf("CONTAS_IGUAIS")
        );
    }
}
