package service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import model.TipoTransacao;
import model.Transacao;

public class ControleFinanceiro {

    private int proximoId = 1;
    private ArrayList<Transacao> transacoes;

    public ControleFinanceiro() {
        this.transacoes = new ArrayList<>();
    }

    public void adicionarTransacao(Transacao transacao) {
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
                    t.getId() + " | " +
                    t.getDescricao() + " | " +
                    t.getValor() + " | " +
                    t.getTipo() + " | " +
                    t.getData()
            );
        }
    }

    
   public ArrayList<Transacao> getTransacoes() {
    return transacoes;
}

    public void salvarTransacoes() {

        try {
            FileWriter writer = new FileWriter("transacoes.txt");

            for (Transacao t : transacoes) {
                writer.write(
                        t.getId() + ";" +
                        t.getDescricao() + ";" +
                        t.getValor() + ";" +
                        t.getTipo() + ";" +
                        t.getData() + "\n"
                );
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("Erro ao salvar transações");
        }
    }

    public int gerarId() {
        return proximoId++;
    }

    public void ocultarTransacao(int id) {
        for (Transacao t : transacoes) {
            if (t.getId() == id) {
                t.setOculto(true);
                break;
            }
        }
    }

    public void carregarTransacoes() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader("transacoes.txt"));

            String linha;

            while ((linha = reader.readLine()) != null) {

                String[] dados = linha.split(";");

                int id = Integer.parseInt(dados[0]);
                String descricao = dados[1];
                double valor = Double.parseDouble(dados[2]);
                TipoTransacao tipo = TipoTransacao.valueOf(dados[3]);
                LocalDate data = LocalDate.parse(dados[4]);

                Transacao t = new Transacao(id, descricao, valor, tipo, data);

                transacoes.add(t);
            }

            reader.close();

          
            for (Transacao t : transacoes) {
                if (t.getId() >= proximoId) {
                    proximoId = t.getId() + 1;
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar transações");
        }
    }
}