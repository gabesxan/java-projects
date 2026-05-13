package app;

import java.util.InputMismatchException;
import java.util.Scanner;
import model.Banco;
import model.Conta;
import model.ResultadoTransferencia;

public class Main {
    public static void mostrarMenu() {
        System.out.println("======Sistema Bancario======");
        System.out.println("1 - Criar conta");
        System.out.println("2 - Depósito ");
        System.out.println("3 - Saque ");
        System.out.println("4 - Consulta de saldo ");
        System.out.println("5 - Listar contas ");
        System.out.println("6 - Buscar conta por número ");
        System.out.println("7 - Transferência");
        System.out.println("8 - Gerar extrato ");
        System.out.println("0 - Sair");
        System.out.println("===========================");
    }

    public static void mostrarDadosConta(Conta conta) {
        System.out.println("Numero: " + conta.getNumero());
        System.out.println("Titular: " + conta.getTitular());
        System.out.println("Saldo: " + conta.getSaldo());
    }

    public static double lerDouble(Scanner scanner, String mensagem) {
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

    public static int lerInteiro(Scanner scanner, String mensagem) {
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

    public static void criarConta(Scanner scanner, Banco banco) {
        int numeroDigitado = lerInteiro(scanner, "Digite o numero da conta:");
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
            System.out.println("Numero da conta: " + novaConta.getNumero());
        }
    }

    public static void depositar(Scanner scanner, Banco banco) {
        System.out.println("Deposito foi selecionado");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorDeposito = lerDouble(scanner, "Digite o valor a ser depositado:");
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

    public static void sacar(Scanner scanner, Banco banco) {
        System.out.println("Saque foi selecionado.");

        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            if (contaEncontrada != null) {
                double valorSaque = lerDouble(scanner, "Digite o valor a ser sacado:");
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

    public static void consultarSaldo(Scanner scanner, Banco banco) {

        int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
        Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

        if (contaEncontrada != null) {
            mostrarDadosConta(contaEncontrada);
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    public static void listarContas(Banco banco) {
        if (banco.estaVazio()) {
            System.out.println("Sem nenhuma conta cadastrada");
        } else {
            for (Conta c : banco.listarContas()) {
                mostrarDadosConta(c);
            }
        }
    }

    public static void buscarConta(Scanner scanner, Banco banco) {
        int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
        Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

        if (contaEncontrada != null) {
            mostrarDadosConta(contaEncontrada);
        } else {
            System.out.println("Conta não encontrada");
        }
    }

    public static void transferir(Scanner scanner, Banco banco) {
        System.out.println("Transfêrencia foi selecionada.");

        if (banco.quantidadeDeContas() < 2) {
            System.out.println("Não é possivel fazer uma transfêrencia");
        } else {
            int numeroOrigem = lerInteiro(scanner, "Digite o número da conta de origem:");
            int numeroDestino = lerInteiro(scanner, "Digite o número da conta de destino:");
            double valorTransferencia = lerDouble(scanner, "Digite o valor da transferência:");

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

    public static void gerarExtrato(Scanner scanner, Banco banco) {
        if (banco.estaVazio()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Banco banco = new Banco();
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro(scanner, "Escolha uma opcao:");
            switch (opcao) {
                case 0: {
                    System.out.println("Saindo...");
                    continuar = false;
                    break;
                }

                case 1: {
                    criarConta(scanner, banco);
                    break;
                }

                case 2: {
                    depositar(scanner, banco);
                    break;
                }

                case 3: {
                    sacar(scanner, banco);
                    break;
                }

                case 4: {
                    consultarSaldo(scanner, banco);
                    break;
                }

                case 5: {
                    listarContas(banco);
                    break;
                }

                case 6: {
                    buscarConta(scanner, banco);
                    break;
                }

                case 7: {
                    transferir(scanner, banco);
                    break;
                }

                case 8: {
                    gerarExtrato(scanner, banco);
                    break;
                }

                default: {
                    System.out.println("Opcao invalida");
                    break;
                }
            }
        }
        scanner.close();
    }
}
