/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Presentacio;

import Exceptions.PersistenciaException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guill
 */
public class MenuUsuariPanel extends javax.swing.JPanel {
    private CtrlPresentacio cp;
    /**
     * Creates new form MenuUsuariPanel
     */
    public MenuUsuariPanel() {
        cp = new CtrlPresentacio();
        initComponents();
        titulo.setText(cp.nomUsuariActual());
    }
    
   // public MenuUsuariPanel getMenuPanel() {
      //  return MenuUsuariPanel; // Asume que el panel se llama menuUsuariPanel
   // }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroun = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        perfil = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        backgroun.setBackground(new java.awt.Color(255, 255, 204));
        backgroun.setPreferredSize(new java.awt.Dimension(500, 500));

        titulo.setBackground(new java.awt.Color(255, 255, 255));
        titulo.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 18)); // NOI18N
        titulo.setText("Usuari");

        perfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/personIcon.png"))); // NOI18N

        jButton1.setText("Canvi Contrasenya");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgrounLayout = new javax.swing.GroupLayout(backgroun);
        backgroun.setLayout(backgrounLayout);
        backgrounLayout.setHorizontalGroup(
            backgrounLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgrounLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(backgrounLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(backgrounLayout.createSequentialGroup()
                        .addComponent(perfil)
                        .addGap(48, 48, 48)
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(344, Short.MAX_VALUE))
        );
        backgrounLayout.setVerticalGroup(
            backgrounLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgrounLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(backgrounLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(perfil)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgrounLayout.createSequentialGroup()
                        .addComponent(titulo)
                        .addGap(38, 38, 38)))
                .addGap(61, 61, 61)
                .addComponent(jButton1)
                .addContainerGap(507, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroun, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroun, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonChangeActionPerformed
  
    }//GEN-LAST:event_buttonChangeActionPerformed

    /**
     * jButton1ActionPerformed
     * 
     * Mètode que es crida quan es fa clic al botó "Canvi Contrasenya".
     * Permet introduir una nova contrasenya per l'usuari actual i actualitzar-la
     * mitjançant el controlador de presentació.
     * 
     * @param evt l'esdeveniment que ha disparat aquesta acció (clic al botó).
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String newPassword = javax.swing.JOptionPane.showInputDialog(this, "Introdueix la nova contrassenya:");
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String username = cp.nomUsuariActual();
            try {
                cp.canviarContrasenya(username, newPassword);
            } catch (PersistenciaException ex) {
                javax.swing.JOptionPane.showMessageDialog(this,
                "Error al canviar la contrasenya",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } 
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroun;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel perfil;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
