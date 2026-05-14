package persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.Conta;

class ContaRepositoryTest {

    @TempDir
    Path pastaTemporaria;

    @Test
    void deveSalvarContasEmArquivo() throws Exception {
        Path caminhoArquivo = pastaTemporaria.resolve("contas.csv");
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
        Path caminhoArquivo = pastaTemporaria.resolve("contas.csv");
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
        Path caminhoArquivo = pastaTemporaria.resolve("contas.csv");

        ContaRepository repository = new ContaRepository(caminhoArquivo);

        List<Conta> contas = repository.carregar();

        assertTrue(contas.isEmpty());
    }
}
