package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {

    @Test
    void deveDepositarValorValido() {
        Conta conta = new Conta(1, "Gabriel");

        boolean resultado = conta.depositar(100);

        assertTrue(resultado);
        assertEquals(100, conta.getSaldo());
    }
}