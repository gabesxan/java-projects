package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Conta;

class ContaRepositoryTest {

    private Path pastaTeste;
    private Path caminhoArquivo;

    @BeforeEach
    void preparar() throws Exception {
        pastaTeste = Path.of("target", "test-data");
        caminhoArquivo = pastaTeste.resolve("contas.csv");

        Files.createDirectories(pastaTeste);
        Files.deleteIfExists(caminhoArquivo);
    }

    @AfterEach
    void limpar() throws Exception {
        Files.deleteIfExists(caminhoArquivo);
    }

    @Test
    void deveSalvarContasEmArquivo() throws Exception {
        ContaRepository repository = new ContaRepository(caminhoArquivo);

        Conta conta = new Conta(1, "Gabriel");
        conta.depositar(150);

        repository.salvar(List.of(conta));

        List<String> linhas = Files.readAllLines(caminhoArquivo);

        assertEquals(1, linhas.size());
        assertEquals("1;Gabriel;150.0", linhas.get(0));
    }

    @Test
    void deveCarregarContasDoArquivo() throws Exception {
        Files.write(caminhoArquivo, List.of("1;Gabriel;150.0"));

        ContaRepository repository = new ContaRepository(caminhoArquivo);

        List<Conta> contas = repository.carregar();

        assertEquals(1, contas.size());
        assertEquals(1, contas.get(0).getNumero());
        assertEquals("Gabriel", contas.get(0).getTitular());
        assertEquals(150.0, contas.get(0).getSaldo());
    }

    @Test
    void deveRetornarListaVaziaQuandoArquivoNaoExiste() throws Exception {
        ContaRepository repository = new ContaRepository(caminhoArquivo);

        List<Conta> contas = repository.carregar();

        assertTrue(contas.isEmpty());
    }

    @Test
    void deveIgnorarLinhaInvalidaAoCarregarContas() throws Exception {
        Files.write(caminhoArquivo, List.of(
                "1;Gabriel;150.0",
                "linha-invalida",
                "2;Maria;80.0"));

        ContaRepository repository = new ContaRepository(caminhoArquivo);

        List<Conta> contas = repository.carregar();

        assertEquals(2, contas.size());
        assertEquals(1, contas.get(0).getNumero());
        assertEquals(2, contas.get(1).getNumero());
    }
}
