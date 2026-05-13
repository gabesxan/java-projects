package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ContaTest {

    @Test
    void deveDepositarValorValido() {
        Conta conta = new Conta(1, "Gabriel");

        boolean resultado = conta.depositar(100);

        assertTrue(resultado);
        assertEquals(100, conta.getSaldo());
    }

    @Test
    void naoDeveDepositarValorInvalido() {
        Conta conta = new Conta(1, "Gabriel");

        boolean resultado = conta.depositar(-50);

        assertFalse(resultado);
        assertEquals(0, conta.getSaldo());
    }

    @Test
    void deveSacarValorValido() {
        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(100);

        boolean resultado = conta.sacar(40);

        assertTrue(resultado);
        assertEquals(60, conta.getSaldo());
    }

    @Test
    void naoDeveSacarComSaldoInsuficiente() {
        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(30);

        boolean resultado = conta.sacar(50);

        assertFalse(resultado);
        assertEquals(30, conta.getSaldo());
    }

    @Test
    void naoDeveSacarValorInvalido() {
        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(100);

        boolean resultado = conta.sacar(-10);

        assertFalse(resultado);
        assertEquals(100, conta.getSaldo());
    }

    @Test
    void deveRegistrarDepositoNoExtrato() {
        Conta conta = new Conta(1, "Gabriel");

        conta.depositar(100);

        assertEquals(1, conta.getExtrato().size());
        assertEquals(TipoOperacao.DEPOSITO, conta.getExtrato().get(0).getTipo());
        assertEquals(100, conta.getExtrato().get(0).getValor());
    }

    @Test
    void deveRegistrarSaqueNoExtrato() {
        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(100);

        conta.sacar(40);

        assertEquals(2, conta.getExtrato().size());
        assertEquals(TipoOperacao.SAQUE, conta.getExtrato().get(1).getTipo());
        assertEquals(40, conta.getExtrato().get(1).getValor());
    }

    @Test
    void depositoInvalidoNaoDeveRegistrarExtrato() {
        Conta conta = new Conta(1, "Gabriel");

        conta.depositar(-50);

        assertTrue(conta.getExtrato().isEmpty());
    }

    @Test
    void saqueInvalidoNaoDeveRegistrarExtrato() {
        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(100);

        conta.sacar(-10);

        assertEquals(1, conta.getExtrato().size());
    }
}
