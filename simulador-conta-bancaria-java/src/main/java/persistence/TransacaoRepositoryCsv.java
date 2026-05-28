package persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Conta;
import model.TipoOperacao;
import model.Transacao;

public class TransacaoRepositoryCsv {

    private final Path caminhoArquivo;

    public TransacaoRepositoryCsv(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    private void carregarLinha(List<Conta> contas, String linha) {
        String[] partes = linha.split(";");

        if (partes.length != 5) {
            throw new IllegalArgumentException("Linha de transação inválida");
        }

        int numeroConta = Integer.parseInt(partes[0]);
        Transacao transacao = converterPartesParaTransacao(partes);

        Conta conta = buscarContaPorNumero(contas, numeroConta);

        if (conta != null) {
            conta.adicionarTransacao(transacao);
        }
    }

    private Transacao converterPartesParaTransacao(String[] partes) {
        TipoOperacao tipo = TipoOperacao.valueOf(partes[1]);
        double valor = Double.parseDouble(partes[2]);
        LocalDateTime dataHora = LocalDateTime.parse(partes[3]);
        String descricao = partes[4];

        return new Transacao(tipo, valor, dataHora, descricao);
    }

    private Conta buscarContaPorNumero(List<Conta> contas, int numeroConta) {
        for (Conta conta : contas) {
            if (conta.getNumero() == numeroConta) {
                return conta;
            }
        }

        return null;
    }

    public void salvar(List<Conta> contas) throws IOException {
        List<String> linhas = new ArrayList<>();

        for (Conta conta : contas) {
            for (Transacao transacao : conta.getExtrato()) {
                String linha = converterTransacaoParaLinha(conta, transacao);
                linhas.add(linha);
            }
        }

        criarPastaSeNecessario();
        Files.write(caminhoArquivo, linhas);
    }

    private void criarPastaSeNecessario() throws IOException {
        Path pasta = caminhoArquivo.getParent();

        if (pasta != null) {
            Files.createDirectories(pasta);
        }
    }

    private String converterTransacaoParaLinha(Conta conta, Transacao transacao) {
        return conta.getNumero()
                + ";"
                + transacao.getTipo()
                + ";"
                + transacao.getValor()
                + ";"
                + transacao.getDataHora()
                + ";"
                + transacao.getDescricao();
    }

    public void carregar(List<Conta> contas) throws IOException {
        if (!Files.exists(caminhoArquivo)) {
            return;
        }

        List<String> linhas = Files.readAllLines(caminhoArquivo);
        for (String linha : linhas) {
            if (!linha.isBlank()) {
                try {
                    carregarLinha(contas, linha);
                } catch (RuntimeException e) {
                    System.out.println("Linha inválida ignorada em transacoes.csv: " + linha);
                }
            }
        }
    }
}
