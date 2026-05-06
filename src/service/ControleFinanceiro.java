package service;

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
                t.getTipo() + " | " + t.getValor()
            );
        }
    }
}