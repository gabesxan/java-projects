package app;

import java.util.InputMismatchException;
import java.util.Scanner;
import model.Banco;
import model.Conta;
import model.ResultadoTransferencia;

public class AplicacaoBancaria {
    private final Scanner scanner;
    private final Banco banco;

    public AplicacaoBancaria() {
        scanner = new Scanner(System.in);
        banco = new Banco();
    }

    public void executar() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opção:");

            switch (opcao) {
                case 0: {
                    System.out.println("Saindo...");
                    continuar = false;
                    break;
                }

                case 1: {
                    criarConta();
                    break;
                }

                case 2: {
                    depositar();
                    break;
                }

                case 3: {
                    sacar();
                    break;
                }

                case 4: {
                    consultarSaldo();
                    break;
                }

                case 5: {
                    listarContas();
                    break;
                }

                case 6: {
                    buscarConta();
                    break;
                }

                case 7: {
                    transferir();
                    break;
                }

                case 8: {
                    gerarExtrato();
                    break;
                }

                default: {
                    System.out.println("Opção inválida.");
                    break;
                }
            }
        }

        scanner.close();
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
        System.out.println("======Sistema Bancario======");
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
            banco.adicionarConta(novaConta);

            System.out.println("Conta criada para: " + novaConta.getTitular());
            System.out.println("Número da conta: " + novaConta.getNumero());
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
        System.out.println("Depósito foi selecionado");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro("Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorDeposito = lerDouble("Digite o valor a ser depositado:");
                boolean depositoRealizado = contaEncontrada.depositar(valorDeposito);

                if (depositoRealizado) {
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
        System.out.println("Saque foi selecionado.");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro("Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorSaque = lerDouble("Digite o valor a ser sacado:");
                boolean saqueRealizado = contaEncontrada.sacar(valorSaque);

                if (saqueRealizado) {
                    System.out.println("Saque realizado com sucesso.");
                    mostrarDadosConta(contaEncontrada);
                } else {
                    System.out.println("Saque não realizado.");
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
            System.out.println("Sem nenhuma conta cadastrada");
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
        System.out.println("Transferência foi selecionada.");

        if (banco.quantidadeDeContas() < 2) {
            System.out.println("Não é possível fazer uma transferência.");
        } else {
            int numeroOrigem = lerInteiro("Digite o número da conta de origem:");
            int numeroDestino = lerInteiro("Digite o número da conta de destino:");
            double valorTransferencia = lerDouble("Digite o valor da transferência:");

            ResultadoTransferencia resultado = banco.transferir(numeroOrigem, numeroDestino, valorTransferencia);

            if (resultado == ResultadoTransferencia.SUCESSO) {
                System.out.println("Transferência realizada com sucesso.");
            } else if (resultado == ResultadoTransferencia.CONTA_ORIGEM_NAO_ENCONTRADA) {
                System.out.println("Conta de origem não encontrada.");
            } else if (resultado == ResultadoTransferencia.CONTA_DESTINO_NAO_ENCONTRADA) {
                System.out.println("Conta de destino não encontrada.");
            } else if (resultado == ResultadoTransferencia.VALOR_INVALIDO) {
                System.out.println("Valor de transferência inválido.");
            } else if (resultado == ResultadoTransferencia.SALDO_INSUFICIENTE) {
                System.out.println("Saldo insuficiente.");
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
                    for (String linha : contaEncontrada.getExtrato()) {
                        System.out.println(linha);
                    }
                }
            } else {
                System.out.println("Conta não encontrada.");
            }
        }
    }

}

