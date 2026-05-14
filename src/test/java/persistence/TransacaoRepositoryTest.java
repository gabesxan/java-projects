package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;
import model.TipoOperacao;

class TransacaoRepositoryTest {

    private Path pastaTeste;
    private Path caminhoArquivo;

    @BeforeEach
    void preparar() throws Exception {
        pastaTeste = Path.of("target", "test-data");
        caminhoArquivo = pastaTeste.resolve("transacoes.csv");

        Files.createDirectories(pastaTeste);
        Files.deleteIfExists(caminhoArquivo);
    }

    @AfterEach
    void limpar() throws Exception {
        Files.deleteIfExists(caminhoArquivo);
    }

    @Test
    void deveSalvarTransacoesEmArquivo() throws Exception {
        TransacaoRepository repository = new TransacaoRepository(caminhoArquivo);

        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(150);

        repository.salvar(List.of(conta));

        List<String> linhas = Files.readAllLines(caminhoArquivo);

        assertEquals(1, linhas.size());
        assertEquals("1;DEPOSITO;150.0;" + conta.getExtrato().get(0).getDataHora() + ";Depósito realizado",
                linhas.get(0));
    }

    @Test
    void deveCarregarTransacoesDoArquivo() throws Exception {
        pastaTeste = Path.of("target", "test-data");
        caminhoArquivo = pastaTeste.resolve("transacoes.csv");

        Files.createDirectories(pastaTeste);
        Files.deleteIfExists(caminhoArquivo);

        Files.write(caminhoArquivo, List.of(
                "1;DEPOSITO;150.0;2026-05-14T10:30;Depósito realizado"));

        TransacaoRepository repository = new TransacaoRepository(caminhoArquivo);

        Conta conta = new Conta(1, "Gabriel");
        repository.carregar(List.of(conta));

        assertEquals(1, conta.getExtrato().size());
        assertEquals(TipoOperacao.DEPOSITO, conta.getExtrato().get(0).getTipo());
        assertEquals(150.0, conta.getExtrato().get(0).getValor());
        assertEquals("Depósito realizado", conta.getExtrato().get(0).getDescricao());

        Files.deleteIfExists(caminhoArquivo);
    }

}
