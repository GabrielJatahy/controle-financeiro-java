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
    private JComboBox<String> cbFiltro;
    private JTable tabela;
    private DefaultTableModel modelo;
    private JLabel lblSaldo;

    private int idSelecionado;
    private boolean editando = false;

    public ControleFinanceiroGUI() {

        controle.carregarTransacoes();

        setTitle("Controle Financeiro");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(20, 20, 20));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

       
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBackground(new Color(20, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtDescricao = new JTextField();
        txtValor = new JTextField();
        cbTipo = new JComboBox<>(new String[] { "ENTRADA", "SAIDA" });
        cbFiltro = new JComboBox<>(new String[] { "TODOS", "ENTRADA", "SAIDA" });

        styleField(txtDescricao, font);
        styleField(txtValor, font);
        styleField(cbTipo, font);
        styleField(cbFiltro, font);

        form.add(label("Descrição"));
        form.add(label("Valor"));
        form.add(label("Tipo"));
        form.add(label("Filtro"));

        form.add(txtDescricao);
        form.add(txtValor);
        form.add(cbTipo);
        form.add(cbFiltro);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        botoes.setBackground(new Color(20, 20, 20));
        botoes.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JButton btnAdd = styledButton("Adicionar");
        JButton btnEdit = styledButton("Editar");
        JButton btnDel = styledButton("Excluir");

        botoes.add(btnAdd);
        botoes.add(btnEdit);
        botoes.add(btnDel);

       
        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Descrição");
        modelo.addColumn("Valor");
        modelo.addColumn("Tipo");
        modelo.addColumn("Data");

        tabela = new JTable(modelo);
        tabela.setRowHeight(28);
        tabela.setFont(font);

        tabela.setSelectionBackground(new Color(70, 70, 70));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setBackground(new Color(30, 30, 30));
        tabela.setForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());

     
        lblSaldo = new JLabel();
        lblSaldo.setForeground(new Color(0, 220, 0));
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSaldo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(20, 20, 20));
        bottom.add(botoes, BorderLayout.CENTER);
        bottom.add(lblSaldo, BorderLayout.SOUTH);

        add(form, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

     
        btnAdd.addActionListener(e -> {

            if (editando) {

                for (Transacao t : controle.getTransacoes()) {
                    if (t.getId() == idSelecionado) {
                        t.setDescricao(txtDescricao.getText());
                        t.setValor(Double.parseDouble(txtValor.getText()));
                        t.setTipo(TipoTransacao.valueOf(cbTipo.getSelectedItem().toString()));
                    }
                }

                editando = false;

            } else {
                adicionar();
            }

            controle.salvarTransacoes();
            atualizar();
        });

        cbFiltro.addActionListener(e -> atualizar());

        btnEdit.addActionListener(e -> editar());

        btnDel.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1)
                return;

            int id = (int) tabela.getValueAt(linha, 0);
            controle.ocultarTransacao(id);

            controle.salvarTransacoes();
            atualizar();
        });

        atualizar();
        setVisible(true);
    }

    private void adicionar() {
        Transacao t = new Transacao(
                controle.gerarId(),
                txtDescricao.getText(),
                Double.parseDouble(txtValor.getText()),
                TipoTransacao.valueOf(cbTipo.getSelectedItem().toString()),
                LocalDate.now(),
                false);

        controle.adicionarTransacao(t);
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1)
            return;

        idSelecionado = (int) tabela.getValueAt(linha, 0);

        txtDescricao.setText(tabela.getValueAt(linha, 1).toString());
        txtValor.setText(tabela.getValueAt(linha, 2).toString());
        cbTipo.setSelectedItem(tabela.getValueAt(linha, 3).toString());

        editando = true;
    }

    private void atualizar() {

        modelo.setRowCount(0);

        String filtro = cbFiltro.getSelectedItem().toString();

        for (Transacao t : controle.getTransacoes()) {
            if (!t.isOculto()) {

                if (!filtro.equals("TODOS") && !t.getTipo().toString().equals(filtro)) {
                    continue;
                }

                modelo.addRow(new Object[] {
                        t.getId(),
                        t.getDescricao(),
                        t.getValor(),
                        t.getTipo(),
                        t.getData()
                });
            }
        }

        lblSaldo.setText("Saldo: R$ " + controle.calcularSaldo());
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.LIGHT_GRAY);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private void styleField(JComponent c, Font f) {
        c.setFont(f);
        c.setForeground(Color.WHITE);
        c.setBackground(new Color(45, 45, 45));
        c.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
    }

    private JButton styledButton(String text) {

        JButton b = new JButton(text);

        Color normal = new Color(55, 55, 55);
        Color hover = new Color(85, 85, 85);

        b.setBackground(normal);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(normal);
            }
        });

        return b;
    }
}