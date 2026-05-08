package app;

import java.time.LocalDate;
import java.util.Scanner;
import model.TipoTransacao;
import model.Transacao;
import service.ControleFinanceiro;

public class Main {
  public static void main(String[] args) {

    ControleFinanceiro controle = new ControleFinanceiro();

    Scanner sc = new Scanner(System.in);
    int opcao;

    while (true) {
      System.out.println("===== CONTROLE FINANCEIRO =====");
      System.out.println("1 - Adicionar transação");
      System.out.println("2 - Ver saldo");
      System.out.println("3 - Listar transações");
      System.out.println("4 - Remover Transação");
      System.out.println("0 - Sair");

      opcao = sc.nextInt();
      sc.nextLine();

      if (opcao == 0) {
        break;
      }

      if (opcao == 1) {

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Valor: ");
        double valor = Double.parseDouble(sc.nextLine());

        System.out.print("Tipo (ENTRADA/SAIDA): ");
        String tipoStr = sc.nextLine();

        TipoTransacao tipo = TipoTransacao.valueOf(tipoStr.toUpperCase());

        Transacao t = new Transacao(controle.gerarId(), descricao, valor, tipo, LocalDate.now());
        controle.adicionarTransacao(t);
        controle.salvarTransacoes();

        System.out.println("Transação adicionada!");
      }

      if (opcao == 2) {
        double saldo = controle.calcularSaldo();
        System.out.println("Saldo atual: " + saldo);
      }

      if (opcao == 3) {
        controle.listarTransacoes();
      }

      if (opcao == 4) {
        System.out.print("Digite o ID da transação a remover: ");
        int id = sc.nextInt();
        sc.nextLine();

        controle.removerTransacao(id);

        System.out.println("Transação removida!");
      }
    }
    sc.close();
  }
}