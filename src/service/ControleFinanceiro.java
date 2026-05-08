package service;

import java.io.FileWriter;
import java.util.ArrayList;
import model.TipoTransacao;
import model.Transacao;

public class ControleFinanceiro {

    private ArrayList<Transacao> transacoes;

    public ControleFinanceiro() {
        this.transacoes = new ArrayList<>();
    }

    public void adicionarTransacao(Transacao transacao){
        this.transacoes.add(transacao);
    }

    public double calcularSaldo() {
        double saldo = 0.0;

        for (Transacao t : transacoes) {
            if (t.getTipo() == TipoTransacao.ENTRADA) {
                saldo += t.getValor();
            } else {
                saldo -= t.getValor();
            }
        }

        return saldo;
    }

    public void listarTransacoes() {
        for (Transacao t : transacoes) {
            System.out.println(
                t.getId() + ";"
               + t.getDescricao() + ";"
              + t.getValor() + ";"
               + t.getTipo() + ";"
                + t.getData() + "\n"
                
            );
        }
    }
    public void salvarTransacoes() {

    try {

        FileWriter writer = new FileWriter("transacoes.txt");
        for (Transacao t : transacoes){
            writer.write( "ID: " + t.getId() +
               " | Descrição: " + t.getDescricao() +
               " | Valor: " + t.getValor() +
               " | Tipo: " + t.getTipo() +
               " | Data: " + t.getData() + "\n");
        }
        writer.close();

    } catch (Exception e) {

        System.out.println("Erro ao salvar arquivo");

    }

    
}
}