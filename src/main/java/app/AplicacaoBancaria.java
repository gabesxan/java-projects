package app;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Banco;
import model.Conta;
import model.ResultadoTransferencia;
import model.Transacao;
import persistence.ConexaoBanco;
import persistence.ContaRepository;
import persistence.InicializadorBanco;
import persistence.TransacaoRepository;

public class AplicacaoBancaria {
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final InicializadorBanco inicializadorBanco;
    private final Scanner scanner;
    private final Banco banco;

    public AplicacaoBancaria() {
        scanner = new Scanner(System.in);
        banco = new Banco();
        contaRepository = new ContaRepository(Path.of("data", "contas.csv"));
        transacaoRepository = new TransacaoRepository(Path.of("data", "transacoes.csv"));

        ConexaoBanco conexaoBanco = new ConexaoBanco();
        inicializadorBanco = new InicializadorBanco(conexaoBanco);
    }

    public void executar() {
        inicializarBanco();
        carregarContas();

        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opção:");
            continuar = processarOpcao(opcao);
        }
    }

    private boolean processarOpcao(int opcao) {
        switch (opcao) {
            case 0 -> {
                System.out.println("Saindo...");
                return false;
            }
            case 1 -> criarConta();
            case 2 -> depositar();
            case 3 -> sacar();
            case 4 -> consultarSaldo();
            case 5 -> listarContas();
            case 6 -> buscarConta();
            case 7 -> transferir();
            case 8 -> gerarExtrato();
            default -> System.out.println("Opção inválida.");
        }

        return true;
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Digite apenas números inteiros.");
                scanner.nextLine();
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("======Sistema Bancário======");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Depósito");
        System.out.println("3 - Saque");
        System.out.println("4 - Consulta de saldo");
        System.out.println("5 - Listar contas");
        System.out.println("6 - Buscar conta por número");
        System.out.println("7 - Transferência");
        System.out.println("8 - Gerar extrato");
        System.out.println("0 - Sair");
        System.out.println("===========================");
    }

    private void criarConta() {
        int numeroDigitado = lerInteiro("Digite o número da conta:");
        Conta contaExistente = banco.buscarContaPorNumero(numeroDigitado);

        if (contaExistente != null) {
            System.out.println("Já existe uma conta com esse número.");
        } else {
            scanner.nextLine();

            System.out.println("Digite o nome da conta:");
            String nomeDigitado = scanner.nextLine();

            Conta novaConta = new Conta(numeroDigitado, nomeDigitado);
            boolean contaAdicionada = banco.adicionarConta(novaConta);

            if (contaAdicionada) {
                salvarContas();

                System.out.println("Conta criada para: " + novaConta.getTitular());
                System.out.println("Número da conta: " + novaConta.getNumero());

            } else {
                System.out.println("Já existe uma conta com esse número.");
            }
        }
    }

    private void salvarContas() {
        try {
            contaRepository.salvar(banco.listarContas());
        } catch (IOException e) {
            System.out.println("Erro ao salvar contas em arquivo.");
        }
    }

    private void carregarContas() {
        try {
            for (Conta conta : contaRepository.carregar()) {
                banco.adicionarConta(conta);
            }

            transacaoRepository.carregar(banco.listarContas());
        } catch (IOException e) {
            System.out.println("Nenhum arquivo de contas encontrado ou erro ao carregar contas.");
        }
    }

    private void inicializarBanco() {
        try {
            inicializadorBanco.inicializar();
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar banco de dados.");
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.println(mensagem);
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Digite apenas números.");
                scanner.nextLine();
            }
        }
    }

    private void mostrarDadosConta(Conta conta) {
        System.out.println("Número: " + conta.getNumero());
        System.out.println("Titular: " + conta.getTitular());
        System.out.println("Saldo: " + conta.getSaldo());
    }

    private void depositar() {
        System.out.println("Depósito selecionado.");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro("Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorDeposito = lerDouble("Digite o valor a ser depositado:");
                boolean depositoRealizado = contaEncontrada.depositar(valorDeposito);

                if (depositoRealizado) {
                    salvarContas();
                    salvarTransacoes();

                    System.out.println("Depósito realizado com sucesso.");
                    mostrarDadosConta(contaEncontrada);
                } else {
                    System.out.println("Valor depositado inválido.");
                }
            } else {
                System.out.println("Conta não encontrada.");
            }
        }
    }

    private void sacar() {
        System.out.println("Saque selecionado.");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro("Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorSaque = lerDouble("Digite o valor a ser sacado:");
                boolean saqueRealizado = contaEncontrada.sacar(valorSaque);

                if (saqueRealizado) {
                    salvarContas();
                    salvarTransacoes();

                    System.out.println("Saque realizado com sucesso.");
                    mostrarDadosConta(contaEncontrada);
                } else {
                    System.out.println("Saque não realizado. Verifique se o valor é válido e se há saldo suficiente.");
                }
            } else {
                System.out.println("Conta não encontrada.");
            }
        }
    }

    private void mostrarContaPorNumero() {
        int numeroBuscado = lerInteiro("Qual o número da conta?");
        Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

        if (contaEncontrada != null) {
            mostrarDadosConta(contaEncontrada);
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    private void consultarSaldo() {
        mostrarContaPorNumero();
    }

    private void listarContas() {
        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            for (Conta c : banco.listarContas()) {
                mostrarDadosConta(c);
            }
        }
    }

    private void buscarConta() {
        mostrarContaPorNumero();
    }

    private void transferir() {
        System.out.println("Transferência selecionada.");

        if (banco.quantidadeDeContas() < 2) {
            System.out.println("Não é possível fazer uma transferência.");
        } else {
            int numeroOrigem = lerInteiro("Digite o número da conta de origem:");
            int numeroDestino = lerInteiro("Digite o número da conta de destino:");
            double valorTransferencia = lerDouble("Digite o valor da transferência:");

            ResultadoTransferencia resultado = banco.transferir(numeroOrigem, numeroDestino, valorTransferencia);

            switch (resultado) {
                case SUCESSO -> {
                    salvarContas();
                    salvarTransacoes();

                    Conta origem = banco.buscarContaPorNumero(numeroOrigem);
                    Conta destino = banco.buscarContaPorNumero(numeroDestino);

                    System.out.println("Transferência realizada com sucesso.");

                    if (origem != null) {
                        System.out.println("Origem:");
                        mostrarDadosConta(origem);
                    }

                    if (destino != null) {
                        System.out.println("Destino:");
                        mostrarDadosConta(destino);
                    }
                }

                case CONTA_ORIGEM_NAO_ENCONTRADA -> System.out.println("Conta de origem não encontrada.");

                case CONTA_DESTINO_NAO_ENCONTRADA -> System.out.println("Conta de destino não encontrada.");

                case VALOR_INVALIDO -> System.out.println("Valor de transferência inválido.");

                case SALDO_INSUFICIENTE -> System.out.println("Saldo insuficiente.");

                case CONTAS_IGUAIS -> System.out.println("A conta de origem e destino devem ser diferentes.");
            }
        }
    }

    private void gerarExtrato() {
        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro("Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                if (contaEncontrada.getExtrato().isEmpty()) {
                    System.out.println("Nenhuma operação realizada.");
                } else {
                    for (Transacao transacao : contaEncontrada.getExtrato()) {
                        System.out.println(transacao.formatarParaExtrato());
                    }
                }
            } else {
                System.out.println("Conta não encontrada.");
            }
        }
    }

    private void salvarTransacoes() {
        try {
            transacaoRepository.salvar(banco.listarContas());
        } catch (IOException e) {
            System.out.println("Erro ao salvar transações em arquivo.");
        }
    }
}
