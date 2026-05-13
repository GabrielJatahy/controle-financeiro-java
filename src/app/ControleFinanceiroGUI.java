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

    private int idSelecionado;
    private boolean editando = false;

    public ControleFinanceiroGUI() {

        controle.carregarTransacoes();

        setTitle("Controle Financeiro");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza tela

       
        setLayout(new BorderLayout(10, 10));

      
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 5, 5));

        txtDescricao = new JTextField();
        txtValor = new JTextField();
        cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});

        painelForm.setBorder(BorderFactory.createTitledBorder("Nova Transação"));

        painelForm.add(new JLabel("Descrição:"));
        painelForm.add(txtDescricao);

        painelForm.add(new JLabel("Valor:"));
        painelForm.add(txtValor);

        painelForm.add(new JLabel("Tipo:"));
        painelForm.add(cbTipo);

       
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnAdicionar = new JButton("Adicionar / Salvar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        
        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Descrição");
        modelo.addColumn("Valor");
        modelo.addColumn("Tipo");
        modelo.addColumn("Data");

        tabela = new JTable(modelo);
        tabela.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tabela);

        
        lblSaldo = new JLabel("Saldo: R$ 0.00");
        JPanel painelSaldo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSaldo.add(lblSaldo);

        
        add(painelForm, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.add(painelBotoes, BorderLayout.CENTER);
        inferior.add(painelSaldo, BorderLayout.SOUTH);

        add(inferior, BorderLayout.SOUTH);

   

        btnAdicionar.addActionListener(e -> {

            if (editando) {

                for (Transacao t : controle.getTransacoes()) {

                    if (t.getId() == idSelecionado) {

                        t.setDescricao(txtDescricao.getText());
                        t.setValor(Double.parseDouble(txtValor.getText()));

                        String tipoStr = cbTipo.getSelectedItem().toString();
                        t.setTipo(TipoTransacao.valueOf(tipoStr));
                    }
                }

                editando = false;

            } else {
                adicionarTransacao();
            }

            controle.salvarTransacoes();
            atualizarLista();
            atualizarSaldo();
            limparCampos();
        });

        btnEditar.addActionListener(e -> editarTransacao());

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();

            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma linha!");
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

        if (txtDescricao.getText().isEmpty() || txtValor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            return;
        }

        double valor;

        try {
            valor = Double.parseDouble(txtValor.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor inválido!");
            return;
        }

        Transacao t = new Transacao(
                controle.gerarId(),
                txtDescricao.getText(),
                valor,
                TipoTransacao.valueOf(cbTipo.getSelectedItem().toString()),
                LocalDate.now(),
                false
        );

        controle.adicionarTransacao(t);
        controle.salvarTransacoes();

        atualizarLista();
        atualizarSaldo();
        limparCampos();
    }

    private void editarTransacao() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada!");
            return;
        }

        idSelecionado = (int) tabela.getValueAt(linha, 0);

        txtDescricao.setText(tabela.getValueAt(linha, 1).toString());
        txtValor.setText(tabela.getValueAt(linha, 2).toString());
        cbTipo.setSelectedItem(tabela.getValueAt(linha, 3).toString());

        editando = true;
    }

    private void atualizarLista() {

        modelo.setRowCount(0);

        for (Transacao t : controle.getTransacoes()) {

            if (!t.isOculto()) {

                modelo.addRow(new Object[]{
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

    private void limparCampos() {
        txtDescricao.setText("");
        txtValor.setText("");
        cbTipo.setSelectedIndex(0);
    }
}