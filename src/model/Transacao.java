package model;
import java.time.LocalDate;

public class Transacao {
    private  int id;
    private String descricao;
    private double valor;
    private TipoTransacao tipo;
    private LocalDate data;
    private boolean oculto;

    public Transacao(int id, String descricao, double valor,
                 TipoTransacao tipo, LocalDate data,
                 boolean oculto) {

    this.id = id;
    this.descricao = descricao;
    this.valor = valor;
    this.tipo = tipo;
    this.data = data;
    this.oculto = oculto;
}

   public TipoTransacao getTipo() {
    return tipo;
   }

   public double getValor() {
    return valor;
   } 

    public int getId() {
    return id;
}

public String getDescricao() {
    return descricao;
}

public LocalDate getData() {
    return data;
}

public boolean isOculto() {
    return oculto;
   
}

public void setOculto(boolean oculto) {
    this.oculto = oculto;
}

}

 