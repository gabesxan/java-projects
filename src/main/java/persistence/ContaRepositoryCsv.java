package persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Conta;

public class ContaRepositoryCsv {

    private final Path caminhoArquivo;

    public ContaRepositoryCsv(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void salvar(List<Conta> contas) throws IOException {
        List<String> linhas = new ArrayList<>();

        for (Conta conta : contas) {
            String linha = converterContaParaLinha(conta);
            linhas.add(linha);
        }

        criarPastaSeNecessario();
        Files.write(caminhoArquivo, linhas);
    }

    public List<Conta> carregar() throws IOException {
        List<Conta> contas = new ArrayList<>();

        if (!Files.exists(caminhoArquivo)) {
            return contas;
        }

        List<String> linhas = Files.readAllLines(caminhoArquivo);

        for (String linha : linhas) {
            if (!linha.isBlank()) {
                try {
                    Conta conta = converterLinhaParaConta(linha);
                    contas.add(conta);
                } catch (RuntimeException e) {
                    System.out.println("Linha inválida ignorada em contas.csv: " + linha);
                }
            }
        }

        return contas;
    }

    private void criarPastaSeNecessario() throws IOException {
        Path pasta = caminhoArquivo.getParent();

        if (pasta != null) {
            Files.createDirectories(pasta);
        }
    }

    private String converterContaParaLinha(Conta conta) {
        return conta.getNumero() + ";" + conta.getTitular() + ";" + conta.getSaldo();
    }

    private Conta converterLinhaParaConta(String linha) {
        String[] partes = linha.split(";");

        if (partes.length != 3) {
            throw new IllegalArgumentException("Linha de conta inválida");
        }

        int numero = Integer.parseInt(partes[0]);
        String titular = partes[1];
        double saldo = Double.parseDouble(partes[2]);

        return new Conta(numero, titular, saldo);
    }
}
