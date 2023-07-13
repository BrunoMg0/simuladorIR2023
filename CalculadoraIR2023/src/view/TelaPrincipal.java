/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.Component;
import static java.awt.SystemColor.desktop;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import model.Funcionario;
import view.TelaResultado;

/**
 *
 * @author bruno
 */
public class TelaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();
    }

    private TelaResultado tr;

    public void inserirValorHoraExtra(String valor) {
        horaExtraText.setText(valor);
    }
    
    public void inserirValorAdicionalNoturno(String valor) {
        adcNoturnoText.setText(valor);
    }
    
    public void getVencimento(){
        
    }

    public void calcular() {
        double salario, insalubridade, horaExtra, bonus, adcNoturno;
        int dependents = Integer.parseInt(DependentCB.getSelectedItem().toString());

        if (salarioText.getText().isEmpty() || insalubridadeText.getText().isEmpty() || horaExtraText.getText().isEmpty()
                || bonusText.getText().isEmpty() || adcNoturnoText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
        } else {
            salario = Double.parseDouble(salarioText.getText().replace(",", "."));
            insalubridade = Double.parseDouble(insalubridadeText.getText().replace(",", "."));
            horaExtra = Double.parseDouble(horaExtraText.getText().replace(",", "."));
            bonus = Double.parseDouble(bonusText.getText().replace(",", "."));
            adcNoturno = Double.parseDouble(adcNoturnoText.getText().replace(",", "."));

            double valorPrev;
            double porcentagemPrev;

            if (prevCB.getSelectedItem().equals("Previjop")) {
                valorPrev = (salario - insalubridade - horaExtra - bonus - adcNoturno) * 0.14;
                porcentagemPrev = 0.14;
            } else {
                double baseCalc = salario;

                double descontoINSSFx1, descontoINSSFx2, descontoINSSFx3, descontoINSSFx4, somaDescontos;
                if (baseCalc <= 1320.00) {
                    descontoINSSFx1 = baseCalc * 0.075;
                    somaDescontos = descontoINSSFx1;
                    porcentagemPrev = 0.075;
                } else if (baseCalc <= 2571.29) {
                    descontoINSSFx2 = (baseCalc - 1320.00) * 0.09;
                    descontoINSSFx1 = 1320.00 * 0.075;
                    somaDescontos = descontoINSSFx2 + descontoINSSFx1;
                    porcentagemPrev = 0.09;
                } else if (baseCalc <= 3856.94) {
                    descontoINSSFx3 = (baseCalc - 2571.29) * 0.12;
                    descontoINSSFx2 = (2571.29 - 1320.00) * 0.09;
                    descontoINSSFx1 = 1320.00 * 0.075;
                    somaDescontos = descontoINSSFx3 + descontoINSSFx2 + descontoINSSFx1;
                    porcentagemPrev = 0.12;
                }  else {
                    descontoINSSFx4 = (baseCalc-3856.94) * 0.14;
                    descontoINSSFx3 = (3856.94-2571.29) * 0.12;
                    descontoINSSFx2 = (2571.29-1320.00) * 0.09;
                    descontoINSSFx1 = 1320 * 0.075;
                    somaDescontos = descontoINSSFx4+descontoINSSFx3+descontoINSSFx2+descontoINSSFx1;
                    if(somaDescontos>876.97){
                        somaDescontos=876.97;
                    }
                    porcentagemPrev = (0.14);
                }

                valorPrev = somaDescontos;
            }

            double descDependents = dependents * 189.59;
            double deducoes = valorPrev + descDependents;
            
            if (deducoes < 528) {
                deducoes = 528;
            }

            double baseCalc = salario - deducoes;

            double[] aliquotas = {0, 0.075, 0.15, 0.225, 0.275};
            double[] Arraydeducoes = {0, 158.40, 370.40, 651.73, 884.96};
            double[] faixas = {2112, 2826.65, 3751.05, 4664.68, Double.POSITIVE_INFINITY};
            //basecacalc 1700
            double valorIR = 0;
            int i = 0;
            if (baseCalc<faixas[0]){
                valorIR = 0;
            }else{
                for (i= 1; i < faixas.length; i++) {
                    if (faixas[i - 1] <= baseCalc && baseCalc < faixas[i]) {
                        valorIR = (baseCalc * aliquotas[i]) - Arraydeducoes[i];
                        break;
                    }
                }
            }
            
            tr = new TelaResultado();

            if (valorIR > 0) {
                tr.setValorIRLabel(String.format("O valor do IR é: R$ %.2f", valorIR));
                tr.setValorPrevLabel(String.format("O valor da previdência é R$ %.2f (valor de referência %.1f%%)", valorPrev, porcentagemPrev * 100));
                tr.setBaseCalculoLabel(String.format("Sua base de cálculo é R$ %.2f, portanto sua alíquota é %.1f%%", baseCalc, aliquotas[i] * 100));
                tr.setSalarioLiquido((String.format("Seu salário líquido é R$ %.2f (desconsiderando empréstimos)", (salario - valorPrev - valorIR))));
            } else {
                tr.setValorIRLabel("Valor isento de IR");
                tr.setValorPrevLabel(String.format("O valor da previdência é R$ %.2f (valor de referência %.1f%%)", valorPrev, porcentagemPrev * 100));
                //tr.setBaseCalculoLabel(String.format("Sua base de cálculo é R$ %.2f, portanto sua alíquota é %.1f%%", baseCalc, aliquotas[i] * 100));
                tr.setSalarioLiquido((String.format("Seu salário líquido é R$ %.2f (desconsiderando empréstimos)", (salario - valorPrev - valorIR))));   
                tr.setBaseCalculoLabel("");
            }

            tr.setVisible(true);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        prevCB = new javax.swing.JComboBox<>();
        horaExtraText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DependentCB = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        salarioText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        insalubridadeText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        bonusText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        adcNoturnoText = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CalculadoraIR(Israel Bruno)");
        setMinimumSize(new java.awt.Dimension(764, 800));
        setPreferredSize(new java.awt.Dimension(764, 750));
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Simulador de IR 2023");
        jLabel1.setToolTipText("");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(310, 0, 174, 37);

        prevCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Previjop", "INSS" }));
        prevCB.setToolTipText("");
        prevCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevCBActionPerformed(evt);
            }
        });
        getContentPane().add(prevCB);
        prevCB.setBounds(270, 40, 200, 30);

        horaExtraText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        horaExtraText.setText("0");
        horaExtraText.setToolTipText("");
        horaExtraText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                horaExtraTextFocusGained(evt);
            }
        });
        horaExtraText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                horaExtraTextMouseClicked(evt);
            }
        });
        horaExtraText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                horaExtraTextActionPerformed(evt);
            }
        });
        horaExtraText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                horaExtraTextKeyPressed(evt);
            }
        });
        getContentPane().add(horaExtraText);
        horaExtraText.setBounds(270, 270, 200, 30);

        jLabel2.setText("Salário Bruto");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(330, 80, 80, 14);

        jLabel3.setText("Dependentes");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(330, 140, 78, 14);

        DependentCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" }));
        DependentCB.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(DependentCB);
        DependentCB.setBounds(270, 160, 200, 20);

        jLabel5.setText("Insalubridade/Periculosidade(R$)");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(290, 190, 180, 20);

        salarioText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        salarioText.setText("0");
        salarioText.setToolTipText("");
        salarioText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salarioTextFocusGained(evt);
            }
        });
        salarioText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salarioTextMouseClicked(evt);
            }
        });
        salarioText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salarioTextActionPerformed(evt);
            }
        });
        salarioText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salarioTextKeyPressed(evt);
            }
        });
        getContentPane().add(salarioText);
        salarioText.setBounds(270, 100, 200, 30);

        jLabel6.setText("Horas extras /gratificação (valor em R$)");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(270, 250, 220, 20);

        insalubridadeText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        insalubridadeText.setText("0");
        insalubridadeText.setToolTipText("");
        insalubridadeText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                insalubridadeTextFocusGained(evt);
            }
        });
        insalubridadeText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                insalubridadeTextMouseClicked(evt);
            }
        });
        insalubridadeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                insalubridadeTextKeyPressed(evt);
            }
        });
        getContentPane().add(insalubridadeText);
        insalubridadeText.setBounds(270, 210, 200, 30);

        jLabel7.setText("Abono");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(350, 310, 50, 14);

        bonusText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bonusText.setText("0");
        bonusText.setToolTipText("");
        bonusText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bonusTextFocusGained(evt);
            }
        });
        bonusText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bonusTextMouseClicked(evt);
            }
        });
        bonusText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bonusTextKeyPressed(evt);
            }
        });
        getContentPane().add(bonusText);
        bonusText.setBounds(270, 330, 200, 30);

        jLabel8.setText("Adicional Noturno");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(320, 370, 110, 14);

        adcNoturnoText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        adcNoturnoText.setText("0");
        adcNoturnoText.setToolTipText("");
        adcNoturnoText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                adcNoturnoTextFocusGained(evt);
            }
        });
        adcNoturnoText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adcNoturnoTextMouseClicked(evt);
            }
        });
        adcNoturnoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                adcNoturnoTextKeyPressed(evt);
            }
        });
        getContentPane().add(adcNoturnoText);
        adcNoturnoText.setBounds(270, 390, 200, 30);

        jButton1.setText("Calcular");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(330, 440, 80, 30);

        jButton2.setText("Abrir Tela de Horas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(490, 270, 140, 30);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(25, 30));

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Até 2112", "0", "0"},
                {"De R$ 2.112,01 até R$ 2.826,65", "7,5", "R$ 158,40"},
                {"De R$ 2.826,66 até R$ 3.751,05", "15", "R$ 370,40"},
                {"De R$ 3.751,06 até R$ 4.664,68", "22,5", "R$ 651,73"},
                {"Acima de R$ 4.664,68", "27,5", "R$ 869,36"}
            },
            new String [] {
                "Base de Cálculo", "Alíquiota(%)", "Dedução do IR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAlignmentX(0.7F);
        jTable1.setAlignmentY(0.7F);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setAutoscrolls(false);
        jTable1.setPreferredSize(new java.awt.Dimension(225, 150));
        jTable1.setRowHeight(30);
        jTable1.setRowMargin(2);
        jTable1.setSelectionBackground(new java.awt.Color(204, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(255, 102, 255));
        jTable1.setUpdateSelectionOnSort(false);
        jTable1.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 540, 680, 180);

        jLabel4.setBackground(new java.awt.Color(102, 0, 204));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Base de Cálculo do Imposto de Renda 2023 ");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 500, 680, 20);

        jButton3.setText("Tela Adicional Noturno");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(480, 390, 140, 30);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/fundo3.png"))); // NOI18N
        jLabel10.setMaximumSize(new java.awt.Dimension(800, 750));
        jLabel10.setMinimumSize(new java.awt.Dimension(350, 350));
        jLabel10.setName(""); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(800, 750));
        jLabel10.setRequestFocusEnabled(false);
        getContentPane().add(jLabel10);
        jLabel10.setBounds(0, -40, 780, 980);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prevCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prevCBActionPerformed

    private void salarioTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salarioTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salarioTextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        calcular();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void salarioTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salarioTextMouseClicked
        salarioText.setText("");
    }//GEN-LAST:event_salarioTextMouseClicked

    private void insalubridadeTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_insalubridadeTextMouseClicked
        insalubridadeText.setText("");
    }//GEN-LAST:event_insalubridadeTextMouseClicked

    private void horaExtraTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_horaExtraTextMouseClicked
        horaExtraText.setText("");
    }//GEN-LAST:event_horaExtraTextMouseClicked

    private void bonusTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bonusTextMouseClicked
        bonusText.setText("");
    }//GEN-LAST:event_bonusTextMouseClicked

    private void adcNoturnoTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adcNoturnoTextMouseClicked
        adcNoturnoText.setText("");
    }//GEN-LAST:event_adcNoturnoTextMouseClicked

    private void salarioTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salarioTextKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            calcular();
        }
    }//GEN-LAST:event_salarioTextKeyPressed

    private void insalubridadeTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_insalubridadeTextKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            calcular();
        }
    }//GEN-LAST:event_insalubridadeTextKeyPressed

    private void bonusTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bonusTextKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            calcular();
        }
    }//GEN-LAST:event_bonusTextKeyPressed

    private void adcNoturnoTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_adcNoturnoTextKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            calcular();
        }
    }//GEN-LAST:event_adcNoturnoTextKeyPressed

    private void insalubridadeTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_insalubridadeTextFocusGained
        insalubridadeText.setText("");
    }//GEN-LAST:event_insalubridadeTextFocusGained

    private void salarioTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salarioTextFocusGained
        salarioText.setText("");
    }//GEN-LAST:event_salarioTextFocusGained

    private void horaExtraTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_horaExtraTextFocusGained
        horaExtraText.setText("");
    }//GEN-LAST:event_horaExtraTextFocusGained

    private void bonusTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bonusTextFocusGained
        bonusText.setText("");
    }//GEN-LAST:event_bonusTextFocusGained

    private void adcNoturnoTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adcNoturnoTextFocusGained
        adcNoturnoText.setText("");
    }//GEN-LAST:event_adcNoturnoTextFocusGained

    private void horaExtraTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_horaExtraTextKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            calcular();
        }
    }//GEN-LAST:event_horaExtraTextKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        TelaHoraExtra th = new TelaHoraExtra(TelaPrincipal.this);
        th.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void horaExtraTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_horaExtraTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_horaExtraTextActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       TelaAdicionalNoturno ta = new TelaAdicionalNoturno(TelaPrincipal.this);
        ta.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    public class CellRenderer extends DefaultTableCellRenderer {

        public CellRenderer() {
            super();
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            this.setHorizontalAlignment(CENTER);

            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> DependentCB;
    private javax.swing.JTextField adcNoturnoText;
    private javax.swing.JTextField bonusText;
    private javax.swing.JTextField horaExtraText;
    private javax.swing.JTextField insalubridadeText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> prevCB;
    private javax.swing.JTextField salarioText;
    // End of variables declaration//GEN-END:variables
}
