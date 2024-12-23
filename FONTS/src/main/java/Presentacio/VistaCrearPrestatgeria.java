/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Presentacio;

import Exceptions.DominiException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * La classe {@code VistaCrearPrestatgeria} representa la vista per crear una prestatgeria a l'aplicació.
 * Aquesta interfície permet a l'usuari introduir dades com l'identificador, nom, 
 * nombre de columnes i seleccionar productes i algoritmes per a la creació d'una prestatgeria.
 * També inclou funcionalitats per importar prestatgeries des d'un fitxer.
 * 
 * <p>Extén {@link javax.swing.JPanel} i forma part de la capa de presentació de l'aplicació.</p>
 * 
 * <h3>Funcionalitats principals:</h3>
 * <ul>
 *   <li>Validació de camps com l'identificador, nom i nombre de columnes.</li>
 *   <li>Selecció de productes per afegir a la prestatgeria.</li>
 *   <li>Opció d'escollir entre diferents algoritmes per gestionar la prestatgeria.</li>
 *   <li>Importació de prestatgeries des d'un fitxer extern.</li>
 * </ul>
 * 
 * @author hajweriahussain
 */
public class VistaCrearPrestatgeria extends javax.swing.JPanel {
    
        private CardLayout cl;
        private VistaPrestatgeria vp;
        private CtrlPresentacio cp;
     
    /**
     * Constructor de la classe {@code VistaCrearPrestatgeria}.
     * Inicialitza els components de la interfície, configura els botons d'acció 
     * i carrega els productes disponibles al panell corresponent.
     */
    public VistaCrearPrestatgeria() {
        
        initComponents();
        cp = new CtrlPresentacio();
        initComponents();
        this.setSize(800,800);
        buttongroup();
        cargarProductosEnScrollPane();
        configurarBotoImportar();
    }
    
    /**
     * Configura el botó per importar prestatgeries des d'un fitxer.
     * Aquest mètode assigna un {@code ActionListener} al botó d'importació.
     */
    private void configurarBotoImportar() {
        btnImportar.addActionListener(e -> importarPrestatgeriaDesdeFitxer());
    }
    
    /**
     * Obre un diàleg per importar una prestatgeria des d'un fitxer extern.
     * Si la importació és exitosa, mostra un missatge d'èxit. Si hi ha un error, 
     * mostra un missatge d'error.
     */
    private void importarPrestatgeriaDesdeFitxer() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String path = selectedFile.getAbsolutePath();
                cp.crearPrestatgeriaFitxer(path);
                JOptionPane.showMessageDialog(this, "Prestatgeria importada amb èxit", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                sortir();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en importar la prestatgeria: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Agrupa els botons de selecció d'algorisme dins d'un {@code ButtonGroup}.
     * Permet que només un algorisme sigui seleccionat a la vegada.
     */
    public void buttongroup(){
        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(btnBF);
        buttonGroup.add(btnDosAp);
    }
    
    /**
     * Carrega els productes disponibles dins d'un {@code JScrollPane}.
     * Els productes es mostren com una llista de {@code JCheckBox} perquè 
     * l'usuari pugui seleccionar els productes a afegir a la prestatgeria.
     */

    public void cargarProductosEnScrollPane(){
        try{
            Map<String, Map<String, String>> productes = cp.mostrarProductes();
            if (productes == null) {
                return;
            }
            panelGrid.setLayout(new GridLayout(0, 4, 10, 10));
            for (String id : productes.keySet()) {
                JCheckBox checkBox = new JCheckBox((id));
                panelGrid.add(checkBox); 
            }
            panelProductos.setViewportView(panelGrid);
        }
        catch (DominiException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat: " + ex.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Valida el camp de l'identificador introduït per l'usuari.
     * Comprova si el camp no està buit, si conté un número vàlid 
     * i si l'identificador ja existeix en una altra prestatgeria.
     * 
     * @return {@code true} si l'identificador és vàlid; {@code false} en cas contrari.
     */
    private Boolean validarID() {
        String textoId = Idtxt.getText().trim();
//
        if (textoId.isEmpty()) {
            errorId.setText("Error: El camp no pot estar buit.");
            return false;
        }
        try {
            Map<String, Map<String, String>> pres = cp.mostrarPrestatgeries();
            int id = Integer.parseInt(textoId);
            
            if (id <= 0) { 
                errorId.setText("Error: El identificador ha de ser un número positiu.");
                return false;
            }
            else if (pres == null) {
                errorId.setText("Entrada vàlida.");
                errorId.setForeground(Color.green);
                return true;
            }
            else if (cp.existeixPrestatgeriaId(textoId, pres)) {
                JOptionPane.showMessageDialog(this, "Ja existeix una prestatgeria amb aquest ID. Si us plau, introdueix un altre.", "ID Duplicat", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            else {
                errorId.setText("Entrada válida."); 
                errorId.setForeground(Color.GREEN); 
            }
        } catch (NumberFormatException e) {
            errorId.setText("Error: Introdueix un número vàlid.");
            errorId.setForeground(Color.RED); 
            return false;
        }catch (Exception e) {
            errorId.setText("Error inesperat: " + e.getMessage());
            errorId.setForeground(Color.red);
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida el camp del nom introduït per l'usuari.
     * Comprova si el camp no està buit.
     * 
     * @return {@code true} si el nom és vàlid; {@code false} en cas contrari.
     */
    private Boolean validarNom() {
        String texto = nomtxt.getText().trim();

        if (texto.isEmpty() || texto.equals("Introdueix un nom")) {
            errorNom.setText("Error: El camp no pot estar buit.");
            errorNom.setForeground(Color.RED);
            return false;
        }

        errorNom.setText("Entrada vàlida.");
        errorNom.setForeground(Color.GREEN);
        return true;
    }
    
    /**
     * Valida el camp del nombre de columnes introduït per l'usuari.
     * Comprova si el camp conté un número enter positiu.
     * 
     * @return {@code true} si el nombre de columnes és vàlid; {@code false} en cas contrari.
     */
    private Boolean validarNumColumnes() {
        String texto = colstxt.getText().trim();

        if (texto.isEmpty()) {
            errorNcols.setText("Error: El camp no pot estar buit.");
            errorNcols.setForeground(Color.RED);
            return false;
        }

        try {
            int numColumnas = Integer.parseInt(texto); 
            if (numColumnas <= 0) { 
                errorNcols.setText("Error: El número de files ha de ser major que 0.");
                errorNcols.setForeground(Color.RED);
                return false;
            } else {
                errorNcols.setText("Entrada válida.");
                errorNcols.setForeground(Color.GREEN);
                return true;
            }
        } catch (NumberFormatException e) {
            errorNcols.setText("Error: Ha de ser un número enter positiu.");
            errorNcols.setForeground(Color.RED);
            return false;
        }
    }
    
    /**
     * Recull els productes seleccionats per l'usuari al panell de productes.
     * Converteix la selecció a un format de cadena per al seu posterior processament.
     * 
     * @return Una cadena amb els productes seleccionats en format llista.
     */
    public String parseaProductes() {
        StringBuilder productesBuilder = new StringBuilder();
        for (java.awt.Component comp : panelGrid.getComponents()) {
            if (comp instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) comp;
                if (checkBox.isSelected()) {
                    if (productesBuilder.length() > 0) {
                        productesBuilder.append(", ");
                    }
                    productesBuilder.append(checkBox.getText());
                }
            }
        }

        String productes = "[" + productesBuilder.toString() + "]";
        return productes;
    }
    
    /**
     * Tanca la vista actual i retorna a la vista de prestatgeries.
     * Si la vista de prestatgeries està disponible, l'actualitza per reflectir 
     * els canvis realitzats.
     */
    private void sortir() {
        if (vp != null) {
            vp.mostrarPrestatgeries();
        }
        this.setVisible(false);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCrear1 = new javax.swing.JButton();
        bg = new javax.swing.JPanel();
        IdLabel = new javax.swing.JLabel();
        nColsLabel = new javax.swing.JLabel();
        Idtxt = new javax.swing.JTextField();
        nomLabel = new javax.swing.JLabel();
        nomtxt = new javax.swing.JTextField();
        colstxt = new javax.swing.JTextField();
        btnEnrere = new javax.swing.JButton();
        errorId = new javax.swing.JLabel();
        errorNom = new javax.swing.JLabel();
        errorNcols = new javax.swing.JLabel();
        labelTitol = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        btnDosAp = new javax.swing.JRadioButton();
        btnBF = new javax.swing.JRadioButton();
        panelProductos = new javax.swing.JScrollPane();
        panelGrid = new javax.swing.JPanel();
        productesLabel = new javax.swing.JLabel();
        algoritmeLabel = new javax.swing.JLabel();
        btnCrear = new javax.swing.JButton();
        btnImportar = new javax.swing.JButton();

        btnCrear1.setText("Crear");

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(689, 500));
        setRequestFocusEnabled(false);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(689, 500));

        IdLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        IdLabel.setText("Identificador");

        nColsLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        nColsLabel.setText("Nombre de columnes");

        Idtxt.setForeground(new java.awt.Color(153, 153, 153));
        Idtxt.setText("Introdueix un identificador numeric");
        Idtxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                IdtxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                IdtxtFocusLost(evt);
            }
        });
        Idtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdtxtActionPerformed(evt);
            }
        });

        nomLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        nomLabel.setText("Nom");

        nomtxt.setForeground(new java.awt.Color(153, 153, 153));
        nomtxt.setText("Introdueix un nom");
        nomtxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nomtxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nomtxtFocusLost(evt);
            }
        });
        nomtxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomtxtActionPerformed(evt);
            }
        });

        colstxt.setForeground(new java.awt.Color(153, 153, 153));
        colstxt.setText("Introdueix el nombre de columnes");
        colstxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colstxtFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colstxtFocusLost(evt);
            }
        });
        colstxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colstxtActionPerformed(evt);
            }
        });

        btnEnrere.setText("Enrere");
        btnEnrere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrereActionPerformed(evt);
            }
        });

        errorId.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        errorId.setForeground(new java.awt.Color(255, 0, 0));

        errorNom.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N

        errorNcols.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N

        labelTitol.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        labelTitol.setText("CREAR PRESTATGERIA");

        btnDosAp.setText("Dos Aproximació");
        btnDosAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDosApActionPerformed(evt);
            }
        });

        btnBF.setText("Força Bruta");
        btnBF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBFActionPerformed(evt);
            }
        });

        panelGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelGrid.setLayout(new java.awt.GridLayout(1, 0));
        panelProductos.setViewportView(panelGrid);

        productesLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        productesLabel.setText("Productes a afegir");

        algoritmeLabel.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        algoritmeLabel.setText("Algoritme a utilitzar");

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        btnImportar.setText("Importar");
        btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separator, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IdLabel)
                                    .addComponent(errorId, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(algoritmeLabel)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnImportar)
                                            .addComponent(btnBF))
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(bgLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDosAp))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                                                .addComponent(btnCrear))))))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelTitol)
                                    .addComponent(btnEnrere)
                                    .addComponent(colstxt, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productesLabel)
                                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(panelProductos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                                        .addComponent(errorNcols, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nomtxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(errorNom, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nomLabel))
                                    .addComponent(nColsLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnEnrere)
                .addGap(24, 24, 24)
                .addComponent(labelTitol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(IdLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Idtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorId, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nomLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nomtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorNom, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nColsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colstxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorNcols, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(productesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(algoritmeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBF)
                            .addComponent(btnDosAp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDosApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDosApActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDosApActionPerformed

    private void colstxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colstxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_colstxtActionPerformed

    private void IdtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdtxtActionPerformed

    private void nomtxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomtxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomtxtActionPerformed

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImportarActionPerformed

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        boolean idValid = validarID();
        boolean nomValid = validarNom();
        boolean columnesValid = validarNumColumnes();

        if (idValid && nomValid && columnesValid) {
            try {
                String id = Idtxt.getText().trim();
                String nom = nomtxt.getText().trim();
                String cols = colstxt.getText().trim();
                String productes = parseaProductes();
                String bruteForce = btnBF.isSelected() ? "true" : "false";

                cp.crearPrestatgeria(id, nom, cols, productes, bruteForce);
                JOptionPane.showMessageDialog(this, "Prestatgeria creada!", "CONFIRMACIÓ", JOptionPane.INFORMATION_MESSAGE);
                sortir();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al crear la prestatgeria: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnEnrereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrereActionPerformed
        sortir();
    }//GEN-LAST:event_btnEnrereActionPerformed

    private void IdtxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdtxtFocusGained
        if (Idtxt.getText().equals("Introdueix un identificador numeric")) {
            Idtxt.setText("");
            Idtxt.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_IdtxtFocusGained

    private void IdtxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IdtxtFocusLost
        if (Idtxt.getText().isEmpty()) {
            Idtxt.setText("Introdueix un identificador numeric");
            Idtxt.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_IdtxtFocusLost

    private void nomtxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nomtxtFocusGained
        if (nomtxt.getText().equals("Introdueix un nom")) {
            nomtxt.setText("");
            nomtxt.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_nomtxtFocusGained

    private void nomtxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nomtxtFocusLost
        if (nomtxt.getText().isEmpty()) {
            nomtxt.setText("Introdueix el nombre de columnes");
            nomtxt.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_nomtxtFocusLost

    private void colstxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colstxtFocusGained
        if (colstxt.getText().equals("Introdueix el nombre de columnes")) {
            colstxt.setText("");
            colstxt.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_colstxtFocusGained

    private void colstxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colstxtFocusLost
        if (colstxt.getText().isEmpty()) {
            colstxt.setText("Introdueix un nom");
            colstxt.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_colstxtFocusLost

    private void btnBFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBFActionPerformed
        JOptionPane.showMessageDialog(this, "Si us plau tingues en compte que l'algoritme de força bruta funciona eficientment només per a prestatgeries amb menys de 13 productes", "ID Duplicat", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnBFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IdLabel;
    private javax.swing.JTextField Idtxt;
    private javax.swing.JLabel algoritmeLabel;
    private javax.swing.JPanel bg;
    private javax.swing.JRadioButton btnBF;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnCrear1;
    private javax.swing.JRadioButton btnDosAp;
    private javax.swing.JButton btnEnrere;
    private javax.swing.JButton btnImportar;
    private javax.swing.JTextField colstxt;
    private javax.swing.JLabel errorId;
    private javax.swing.JLabel errorNcols;
    private javax.swing.JLabel errorNom;
    private javax.swing.JLabel labelTitol;
    private javax.swing.JLabel nColsLabel;
    private javax.swing.JLabel nomLabel;
    private javax.swing.JTextField nomtxt;
    private javax.swing.JPanel panelGrid;
    private javax.swing.JScrollPane panelProductos;
    private javax.swing.JLabel productesLabel;
    private javax.swing.JSeparator separator;
    // End of variables declaration//GEN-END:variables
}
