package app;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import service.ControleFinanceiro;

public class ControleFinanceiroGUI extends JFrame {

    private ControleFinanceiro controle = new ControleFinanceiro();

    private JTextField txtDescricao;
    private JTextField txtValor;
    private JComboBox<String> cbTipo;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JLabel lblSaldo;

    public ControleFinanceiroGUI() {

        controle.carregarTransacoes();

        setTitle("Controle Financeiro");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        txtDescricao = new JTextField(20);
        txtValor = new JTextField(10);

        cbTipo = new JComboBox<>(new String[] { "ENTRADA", "SAIDA" });

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnExcluir = new JButton("Excluir");

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Descricao");
        modelo.addColumn("Valor");
        modelo.addColumn("Tipo");
        modelo.addColumn("Data");

        tabela = new JTable(modelo);

        lblSaldo = new JLabel("Saldo: R$ " + controle.calcularSaldo());

        add(new JLabel("Descrição"));
        add(txtDescricao);

        add(new JLabel("Valor"));
        add(txtValor);

        add(new JLabel("Tipo"));
        add(cbTipo);

        add(lblSaldo);

        add(btnAdicionar);
        add(btnExcluir);

        add(new JScrollPane(tabela));

        btnAdicionar.addActionListener(e -> adicionarTransacao());

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada!");
                return;
            }

            int id = (int) tabela.getValueAt(linha, 0);

            controle.ocultarTransacao(id);
            controle.salvarTransacoes();

            atualizarLista();
            atualizarSaldo();
        });

        atualizarLista();
        atualizarSaldo();

        setVisible(true);
    }

    private void adicionarTransacao() {
        try {

            if (txtDescricao.getText().isEmpty() || txtValor.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                return;
            }

            String descricao = txtDescricao.getText();

            double valor;

            try {
                valor = Double.parseDouble(txtValor.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!");
                return;
            }

            String tipoStr = cbTipo.getSelectedItem().toString();
            TipoTransacao tipo = TipoTransacao.valueOf(tipoStr);

            Transacao t = new Transacao(
                    controle.gerarId(),
                    descricao,
                    valor,
                    tipo,
                    LocalDate.now(),
                    false);

            controle.adicionarTransacao(t);
            controle.salvarTransacoes();

            atualizarLista();
            atualizarSaldo();

            txtDescricao.setText("");
            txtValor.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar transação");
        }
    }

    private void atualizarLista() {

        modelo.setRowCount(0);

        for (Transacao t : controle.getTransacoes()) {

            if (!t.isOculto()) {

                modelo.addRow(new Object[] {
                        t.getId(),
                        t.getDescricao(),
                        t.getValor(),
                        t.getTipo(),
                        t.getData()
                });
            }
        }
    }

    private void atualizarSaldo() {
        lblSaldo.setText("Saldo: R$ " + controle.calcularSaldo());
    }
}