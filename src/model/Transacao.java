package model;
import java.time.LocalDate;

public class Transacao {
    private  int id;
    private String descricao;
    private double valor;
    private TipoTransacao tipo;
    private LocalDate data;

    public Transacao (int id, String descricao, double valor, TipoTransacao tipo, LocalDate data) {
    this.id = id;
    this.descricao = descricao;
    this.valor = valor;
    this.tipo = tipo;
    this.data = data;
   }

   public TipoTransacao getTipo() {
    return tipo;
   }

   public double getValor() {
    return valor;
   } 
   
}

 