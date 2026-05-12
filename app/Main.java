package app;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Banco;
import model.Conta;
import model.Tipo_Operacao;

public class Main{
   public static void mostrarMenu(){
            System.out.println("======Sistema Bancario======");
            System.out.println("1 - Criar conta: ");
            System.out.println("2 - Deposito: "); 
            System.out.println("3 - Saque: ");
            System.out.println("4 - Consulta de saldo: ");
            System.out.println("5 - Listar contas:");
            System.out.println("6 - Pesquisar uma conta:");
            System.out.println("7 - Transferência");
            System.out.println("8 - Gerar extrato ");
            System.out.println("0 - Sair:");
            System.out.println("===========================");
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
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Banco banco = new Banco();
        boolean continuar = true;
        
        while (continuar) {
        mostrarMenu();
       int opcao = lerInteiro(scanner, "Escolha uma opcao:");
            switch (opcao){
                case 0:{
                    System.out.println("Saindo...");
                    continuar = false;
                break;
                }
            
                case 1:{
                    int numeroDigitado = lerInteiro(scanner, "Digite o numero da conta:");
                        Conta contaExistente = banco.buscarContaPorNumero(numeroDigitado);

                        if (contaExistente != null) {
                            System.out.println("Já existe uma conta com esse número.");
                        } 
                        else {
                            scanner.nextLine();
                            System.out.println("Digite o nome da conta:");
                            String nomeDigitado = scanner.nextLine();
                            Conta novaConta = new Conta(numeroDigitado, nomeDigitado);
                            banco.adicionarConta(novaConta);
                            System.out.println("Conta criada para: " + novaConta.getTitular());
                            System.out.println("Numero da conta: " + novaConta.getNumero());
                        }
                break;
                }

                case 2:{
                    System.out.println("Deposito foi selecionado");

                    if (banco.estaVazio()){
                        System.out.println("Nenhuma conta cadastrada.");
                    } else {
                            int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
                            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);
                            if (contaEncontrada != null) {
                            double valorDeposito = lerDouble(scanner, "Digite o valor a ser depositado:");
                            boolean depositoRealizado = contaEncontrada.depositar(valorDeposito);
                            if (depositoRealizado) {
                                System.out.println("Depósito realizado com sucesso.");
                                System.out.println("Numero da conta: " + contaEncontrada.getNumero());
                                System.out.println("Titular: " + contaEncontrada.getTitular());
                                System.out.println("Saldo: " + contaEncontrada.getSaldo());
                            } 
                            else {
                                System.out.println("Valor depositado inválido.");
                            }
                        } 
                        else {
                            System.out.println("Conta não encontrada.");
                        }
                    }

                break;
                }

                case 3:{
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
                                    System.out.println("Numero da conta: " + contaEncontrada.getNumero());
                                    System.out.println("Titular: " + contaEncontrada.getTitular());
                                    System.out.println("Saldo: " + contaEncontrada.getSaldo());
                                } 
                                else {
                                    System.out.println("Saque não.");
                                }
                            } 
                            else {
                                System.out.println("Conta não encontrada.");
                            }
                        }
                    }
                break;

                case 4:{
                int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
                Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

            
                        if (contaEncontrada != null) {
                        System.out.println("Numero da conta: " + contaEncontrada.getNumero());
                        System.out.println("Titular: " + contaEncontrada.getTitular());
                        System.out.println("Saldo: " + contaEncontrada.getSaldo());
                        } 
                        else {
                        System.out.println("Conta não encontrada.");
                    }

                    break;
                }
                
                case 5:
                    if(banco.estaVazio()){
                    System.out.println("Sem nenhuma conta cadastrada");
                    }
                    else {
                        for (Conta c: banco.listarContas()){
                            System.out.println("Numero: " + c.getNumero());
                            System.out.println("Titular: " + c.getTitular());
                            System.out.println("Saldo: " + c.getSaldo());
                        
                        }
                    }
                break;
    
                case 6:{
                    int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
                    Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);


                        if (contaEncontrada != null) {
                            System.out.println("Numero: " + contaEncontrada.getNumero());
                            System.out.println("Titular: " + contaEncontrada.getTitular());
                            System.out.println("Saldo: " + contaEncontrada.getSaldo());
                        } else {
                            System.out.println("Conta não encontrada");
                        }

                break;
                   }   
                
                
               case 7:{
                 System.out.println("Transfêrencia foi selecionada.");
                    if (banco.quantidadeDeContas() < 2){
                        System.out.println("Não é possivel fazer uma transfêrencia");
                    }
                    else{ 
                    int numeroOrigem = lerInteiro(scanner, "Digite o número da conta de origem:");
                        Conta origem = banco.buscarContaPorNumero(numeroOrigem);

                    int numeroDestino = lerInteiro(scanner, "Digite o número da conta de destino:");
                        Conta destino = banco.buscarContaPorNumero(numeroDestino);

                        if (origem != null && destino != null) {
                            System.out.println("Origem: " + origem.getTitular());
                            System.out.println("Destino: " + destino.getTitular());
                            double valorTransferencia = lerDouble(scanner, "Digite o valor da transferência:");
                            boolean debitoRealizado = origem.debitarSemExtrato(valorTransferencia);


                            if (debitoRealizado) {
                            destino.creditarSemExtrato(valorTransferencia);
                                System.out.println("Transferência realizada com sucesso.");
                                origem.registrarOperacao(
                                       Tipo_Operacao.TRANSFERENCIA_ENVIADA,
                                        valorTransferencia,
                                        "Para conta " + destino.getNumero()
                                );

                                destino.registrarOperacao(
                                    Tipo_Operacao.TRANSFERENCIA_RECEBIDA,
                                    valorTransferencia,
                                    "Da conta " + origem.getNumero()
                                );
                            } 
                            else {
                                System.out.println("Transferência não realizada.");
                            }
                        
                        }
                        else {
                        System.out.println("Conta de origem ou destino não encontrada.");
                        }
                    }
                
                break;
                }
                
                case 8:{
                        if (banco.estaVazio()) {
                            System.out.println("Nenhuma conta cadastrada.");
                        } 
                        else {
                            int numeroBuscado = lerInteiro(scanner, "Qual o número da conta?");
                            Conta contaEncontrada = banco.buscarContaPorNumero(numeroBuscado);

                            if (contaEncontrada != null) {
                                if (contaEncontrada.getExtrato().isEmpty()) {
                                    System.out.println("Nenhuma operação realizada.");
                                } 
                                else {
                                    for (String linha : contaEncontrada.getExtrato()) {
                                        System.out.println(linha);
                                    }
                                }
                            } 
                            else {
                                System.out.println("Conta não encontrada.");
                            }
                        }
                break; 
                    }  
                
                default:{
                    System.out.println("Opcao invalida");
                break; 
                }
            } 
        }
        scanner.close();
    }
}
