package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class BancoTest {

    @Test
    void deveBuscarContaExistente() {
        Banco banco = new Banco();
        Conta conta = new Conta(1, "Gabriel");

        banco.adicionarConta(conta);

        Conta resultado = banco.buscarContaPorNumero(1);

        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getTitular());
    }

    @Test
    void deveRetornarNullAoBuscarContaInexistente() {
        Banco banco = new Banco();

        Conta resultado = banco.buscarContaPorNumero(999);

        assertNull(resultado);
    }

    @Test
    void deveTransferirEntreContasDiferentes() {
        Banco banco = new Banco();
        Conta origem = new Conta(1, "Origem");
        Conta destino = new Conta(2, "Destino");

        origem.depositar(100);
        banco.adicionarConta(origem);
        banco.adicionarConta(destino);

        ResultadoTransferencia resultado = banco.transferir(1, 2, 40);

        assertEquals(ResultadoTransferencia.SUCESSO, resultado);
        assertEquals(60, origem.getSaldo());
        assertEquals(40, destino.getSaldo());
    }

    @Test
    void naoDeveTransferirComSaldoInsuficiente() {
        Banco banco = new Banco();
        Conta origem = new Conta(1, "Origem");
        Conta destino = new Conta(2, "Destino");

        origem.depositar(20);
        banco.adicionarConta(origem);
        banco.adicionarConta(destino);

        ResultadoTransferencia resultado = banco.transferir(1, 2, 50);

        assertEquals(ResultadoTransferencia.SALDO_INSUFICIENTE, resultado);
        assertEquals(20, origem.getSaldo());
        assertEquals(0, destino.getSaldo());
    }

    @Test
    void naoDeveTransferirParaMesmaConta() {
        Banco banco = new Banco();
        Conta conta = new Conta(1, "Gabriel");

        conta.depositar(100);
        banco.adicionarConta(conta);

        ResultadoTransferencia resultado = banco.transferir(1, 1, 30);

        assertEquals(ResultadoTransferencia.CONTAS_IGUAIS, resultado);
        assertEquals(100, conta.getSaldo());
    }

    @Test
    void naoDeveTransferirComOrigemInexistente() {
        Banco banco = new Banco();
        Conta destino = new Conta(2, "Destino");

        banco.adicionarConta(destino);

        ResultadoTransferencia resultado = banco.transferir(999, 2, 30);

        assertEquals(ResultadoTransferencia.CONTA_ORIGEM_NAO_ENCONTRADA, resultado);
        assertEquals(0, destino.getSaldo());
    }

    @Test
    void naoDeveTransferirComDestinoInexistente() {
        Banco banco = new Banco();
        Conta origem = new Conta(1, "Origem");

        origem.depositar(100);
        banco.adicionarConta(origem);

        ResultadoTransferencia resultado = banco.transferir(1, 999, 30);

        assertEquals(ResultadoTransferencia.CONTA_DESTINO_NAO_ENCONTRADA, resultado);
        assertEquals(100, origem.getSaldo());
    }

    @Test
    void transferenciaValidaDeveRegistrarExtratoNasDuasContas() {
        Banco banco = new Banco();
        Conta origem = new Conta(1, "Origem");
        Conta destino = new Conta(2, "Destino");

        origem.depositar(100);
        banco.adicionarConta(origem);
        banco.adicionarConta(destino);

        banco.transferir(1, 2, 40);

        assertEquals(2, origem.getExtrato().size());
        assertEquals(1, destino.getExtrato().size());

        assertEquals(TipoOperacao.TRANSFERENCIA_ENVIADA, origem.getExtrato().get(1).getTipo());
        assertEquals(TipoOperacao.TRANSFERENCIA_RECEBIDA, destino.getExtrato().get(0).getTipo());
    }
}