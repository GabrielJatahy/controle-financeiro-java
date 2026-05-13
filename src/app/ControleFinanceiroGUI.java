package app;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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

        getContentPane().setBackground(new Color(25, 25, 25));
        setTitle("Controle Financeiro");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBackground(new Color(25, 25, 25));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtDescricao = new JTextField();
        txtValor = new JTextField();
        cbTipo = new JComboBox<>(new String[]{"ENTRADA", "SAIDA"});

        styleField(txtDescricao, font);
        styleField(txtValor, font);
        styleField(cbTipo, font);

        form.add(label("Descrição:"));
        form.add(txtDescricao);

        form.add(label("Valor:"));
        form.add(txtValor);

        form.add(label("Tipo:"));
        form.add(cbTipo);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botoes.setBackground(new Color(25, 25, 25));

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
        tabela.setRowHeight(26);
        tabela.setFont(font);
        tabela.setSelectionBackground(new Color(80, 80, 80));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(60, 60, 60));
        tabela.setBackground(new Color(35, 35, 35));
        tabela.setForeground(Color.WHITE);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tipo = table.getValueAt(row, 3).toString();

                if (!isSelected) {
                    if (tipo.equals("ENTRADA")) {
                        c.setForeground(new Color(0, 200, 0));
                    } else {
                        c.setForeground(new Color(230, 70, 70));
                    }
                    c.setBackground(new Color(35, 35, 35));
                }

                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        lblSaldo = new JLabel();
        lblSaldo.setForeground(new Color(0, 220, 0));
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(25, 25, 25));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        btnEdit.addActionListener(e -> editar());

        btnDel.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) return;

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
                false
        );

        controle.adicionarTransacao(t);
    }

    private void editar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) return;

        idSelecionado = (int) tabela.getValueAt(linha, 0);

        txtDescricao.setText(tabela.getValueAt(linha, 1).toString());
        txtValor.setText(tabela.getValueAt(linha, 2).toString());
        cbTipo.setSelectedItem(tabela.getValueAt(linha, 3).toString());

        editando = true;
    }

    private void atualizar() {

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

        lblSaldo.setText("Saldo: R$ " + controle.calcularSaldo());
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private void styleField(JComponent c, Font f) {
        c.setFont(f);
        c.setForeground(Color.WHITE);
        c.setBackground(new Color(45, 45, 45));
        c.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
    }

    private JButton styledButton(String text) {

        JButton b = new JButton(text);

        Color normal = new Color(60, 60, 60);
        Color hover = new Color(90, 90, 90);

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