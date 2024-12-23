package Presentacio;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import Exceptions.DominiException;

/**
 *
 * @author ariadna
 */

/**
 * Classe que representa la vista per crear un nou producte dins de l'aplicació.
 * Aquesta classe gestiona la interfície d'usuari per introduir les dades del producte,
 * validar-les i gestionar la seva creació.
 */
public class VistaCrearProducte extends javax.swing.JPanel {
    
    private CtrlPresentacio cp;
    private VistaProducte vistaProducte;

    /**
     * Constructor de la classe VistaCrearProducte.
     * Inicialitza els components de la vista i configura els botons.
     */
    public VistaCrearProducte() {
        initComponents();
        cp = new CtrlPresentacio();
        this.setSize(800, 800);
        mostrarProductesEnJList();
        precompletarAreaSimilituds();
        configurarBotons();
    }
    
    /**
     * Reinicia els etiquetatges d'error i els camps d'entrada.
     */
    private void resetLabels() {
        labelErrorId.setText("");
        labelErrorNom.setText("");
        labelErrorSimilituds.setText("");
        textId.setText("");
        textNom.setText("");
        precompletarAreaSimilituds();
    }
    
    /**
     * Analitza una cadena de similituds i la converteix en un mapa.
     * @param similituds La cadena que conté les similituds.
     * @return Un mapa amb les similituds associades als IDs dels productes.
     * @throws Exception Si el format és incorrecte o si hi ha IDs duplicats.
     */
    private Map<String, Double> parseSimilituds(String similituds) throws Exception {
        Map<String, Double> similitudMap = new HashMap<>();

        String[] lines = similituds.split("\\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(":");
            if (parts.length != 2) {
                throw new Exception("Format incorrecte per a la similitud: " + line);
            }

            String id = parts[0].trim();
            double value = Double.parseDouble(parts[1].trim());

            if (similitudMap.containsKey(id)) {
                throw new Exception("ID repetit: " + id);
            }

            similitudMap.put(id, value);
        }

        return similitudMap;
    }
    
    /**
     * Mostra els productes existents en una JList.
     */
    private void mostrarProductesEnJList() {
        try {
            Map<String, Map<String, String>> productes = cp.mostrarProductes();
            if (productes == null) {
                return;
            }

            DefaultListModel<String> model = new DefaultListModel<>();

            for (String id : productes.keySet()) {
                model.addElement(id + " - " + productes.get(id).get("nom"));
            }

            llistaProductesExistents.setModel(model);
        } catch (DominiException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat: " + ex.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Precompleta l'àrea de similituds amb els IDs dels productes existents.
     */
    private void precompletarAreaSimilituds() {
        try {
            Map<String, Map<String, String>> productes = cp.mostrarProductes();
            if (productes == null || productes.isEmpty()) {
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (String id : productes.keySet()) {
                builder.append(id).append(": ").append("\n");
            }

            textAreaSims.setText(builder.toString());
        } catch (DominiException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat: " + ex.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida l'ID introduït pel usuari.
     * @return true si l'ID és vàlid, false altrament.
     */
    private boolean validarId() {
        String id = textId.getText().trim();

        if (id.isEmpty() || id.equals("Introdueix un identificador numèric")) {
            labelErrorId.setText("Error: Has d'introduir un identificador.");
            labelErrorId.setForeground(Color.red);
            return false;
        }
        try {
            Map<String, Map<String, String>> productes = cp.mostrarProductes();

            int idNum = Integer.parseInt(id);

            if(idNum <= 0) {
                labelErrorId.setText("L'id ha de ser un nombre positiu.");
                return false;
            }
            else if (productes == null) {
                labelErrorId.setText("Entrada vàlida.");
                labelErrorId.setForeground(Color.green);
                return true;
            }
            else if (cp.existeixProducteId(id, productes)) {
                JOptionPane.showMessageDialog(this, "Ja existeix un producte amb aquest ID. Si us plau, introdueix un altre.", "ID Duplicat", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            else {
                labelErrorId.setText("Entrada vàlida.");
                labelErrorId.setForeground(Color.green);
                return true;
            }            
        } catch (NumberFormatException ex) {
            labelErrorId.setText("L'id ha de ser un nombre positiu.");
            labelErrorId.setForeground(Color.red);
            return false;
        } catch (Exception e) {
            labelErrorId.setText("Error inesperat: " + e.getMessage());
            labelErrorId.setForeground(Color.red);
            return false;
        }
    }
    
    /**
     * Valida el nom introduït pel usuari.
     * @return true si el nom és vàlid, false altrament.
     */
    private boolean validarNom() {
        String nom = textNom.getText().trim();

        if (nom.isEmpty() || nom.equals("Introdueix un nom")) {
            labelErrorNom.setText("Error: Has d'introduir un nom.");
            labelErrorNom.setForeground(Color.red);
            return false;
        }
        
        labelErrorNom.setText("Entrada vàlida.");
        labelErrorNom.setForeground(Color.green);
        return true;
    }

    /**
     * Valida les similituds introduïdes pel usuari.
     * @return true si les similituds són vàlides, false altrament.
     */
    private boolean validarSimilituds() {
        String similituds = textAreaSims.getText().trim();
        try {
            Map<String, Map<String, String>> productes = cp.mostrarProductes();
            
            if (productes == null || productes.isEmpty()) {     // El primer producte no té cap similitud associada
                return true;
            }

            try {
                Map<String, Double> similitudMap = parseSimilituds(similituds);

                // Verificar si tots els IDs existents tenen una similitud
                for (String id : productes.keySet()) {
                    if (!similitudMap.containsKey(id)) {
                        labelErrorSimilituds.setText("Error: Falta la similitud per al producte amb ID " + id);
                        labelErrorSimilituds.setForeground(Color.red);
                        return false;
                    }

                    Double similitud = similitudMap.get(id);
                    if (similitud < 0 || similitud > 1) {
                        labelErrorSimilituds.setText("Error: La similitud per al producte amb ID " + id + " ha d'estar entre 0 i 1");
                        labelErrorSimilituds.setForeground(Color.red);
                        return false;
                    }
                }

                labelErrorSimilituds.setText("Totes les similituds estan introduïdes correctament.");
                labelErrorSimilituds.setForeground(Color.green);
                return true;

            } catch (Exception e) {
                labelErrorSimilituds.setText("Error en el format de les similituds.");
                labelErrorSimilituds.setForeground(Color.red);
                return false;
            }
        } catch (DominiException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat: " + ex.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    /**
     * Configura els botons de la vista.
     */
    private void configurarBotons() {
        botoImportar.addActionListener(e -> importarProducteDesdeFitxer());
        botoSortir.addActionListener(e -> sortir());
    }
    
    /**
     * Permet a l'usuari importar un producte des d'un fitxer extern.
     */
    private void importarProducteDesdeFitxer() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String path = selectedFile.getAbsolutePath();
                cp.crearProducteFitxer(path);
                JOptionPane.showMessageDialog(this, "Producte importat amb èxit", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                mostrarProductesEnJList();
                precompletarAreaSimilituds();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en importar el producte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Tanca la vista actual i torna a la vista anterior.
     */
    private void sortir() {
        if (vistaProducte != null) {
            vistaProducte.mostrarLlistaPanel();
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

        bgPanel = new javax.swing.JPanel();
        labelTitol = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();
        botoSortir = new javax.swing.JButton();
        textId = new javax.swing.JTextField();
        botoCrear = new javax.swing.JButton();
        botoImportar = new javax.swing.JButton();
        labelErrorId = new javax.swing.JLabel();
        labelNom = new javax.swing.JLabel();
        textNom = new javax.swing.JTextField();
        labelSimilituds = new javax.swing.JLabel();
        labelErrorNom = new javax.swing.JLabel();
        labelErrorSimilituds = new javax.swing.JLabel();
        scrollPaneLlistaProductes = new javax.swing.JScrollPane();
        llistaProductesExistents = new javax.swing.JList<>();
        scrollPaneSimilituds = new javax.swing.JScrollPane();
        textAreaSims = new javax.swing.JTextArea();
        labelId = new javax.swing.JLabel();
        labelProductesRegistrats = new javax.swing.JLabel();

        bgPanel.setBackground(new java.awt.Color(255, 255, 255));

        labelTitol.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        labelTitol.setText("CREAR PRODUCTE");

        botoSortir.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        botoSortir.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        botoSortir.setText("X");
        botoSortir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botoSortir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botoSortirMouseClicked(evt);
            }
        });

        textId.setForeground(new java.awt.Color(153, 153, 153));
        textId.setText("Introdueix un identificador numèric");
        textId.setToolTipText("");
        textId.setActionCommand("<Not Set>");
        textId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textIdFocusLost(evt);
            }
        });

        botoCrear.setText("Crear");
        botoCrear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botoCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botoCrearMouseClicked(evt);
            }
        });

        botoImportar.setText("Importar");

        labelErrorId.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        labelErrorId.setText(" ");

        labelNom.setText("Nom:");

        textNom.setForeground(new java.awt.Color(153, 153, 153));
        textNom.setText("Introdueix un nom");
        textNom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textNomFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textNomFocusLost(evt);
            }
        });

        labelSimilituds.setText("Similituds amb altres productes (ex., 2: 0.75, 3: 0.4):");
        labelSimilituds.setFocusable(false);

        labelErrorNom.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        labelErrorNom.setText(" ");

        labelErrorSimilituds.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        labelErrorSimilituds.setForeground(new java.awt.Color(255, 255, 255));
        labelErrorSimilituds.setText("jLabel1");

        scrollPaneLlistaProductes.setViewportView(llistaProductesExistents);

        textAreaSims.setColumns(20);
        textAreaSims.setForeground(new java.awt.Color(153, 153, 153));
        textAreaSims.setRows(3);
        textAreaSims.setToolTipText("");
        textAreaSims.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textAreaSimsMousePressed(evt);
            }
        });
        scrollPaneSimilituds.setViewportView(textAreaSims);

        labelId.setText("Identificador (ID):");

        labelProductesRegistrats.setText("Productes registrats:");

        javax.swing.GroupLayout bgPanelLayout = new javax.swing.GroupLayout(bgPanel);
        bgPanel.setLayout(bgPanelLayout);
        bgPanelLayout.setHorizontalGroup(
            bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgPanelLayout.createSequentialGroup()
                        .addComponent(labelTitol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botoSortir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(separator)
                    .addGroup(bgPanelLayout.createSequentialGroup()
                        .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelProductesRegistrats)
                            .addComponent(scrollPaneLlistaProductes, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
                        .addGap(64, 64, 64)
                        .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgPanelLayout.createSequentialGroup()
                                .addComponent(botoImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(botoCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgPanelLayout.createSequentialGroup()
                                .addComponent(labelNom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textNom))
                            .addComponent(labelErrorId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPaneSimilituds, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgPanelLayout.createSequentialGroup()
                                .addComponent(labelErrorNom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(69, 69, 69))
                            .addComponent(labelErrorSimilituds, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgPanelLayout.createSequentialGroup()
                                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelSimilituds, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelId, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 108, Short.MAX_VALUE)))))
                .addGap(25, 25, 25))
        );
        bgPanelLayout.setVerticalGroup(
            bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitol)
                    .addComponent(botoSortir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId)
                    .addComponent(labelProductesRegistrats))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgPanelLayout.createSequentialGroup()
                        .addComponent(textId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelErrorId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelNom))
                        .addGap(6, 6, 6)
                        .addComponent(labelErrorNom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelSimilituds)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPaneSimilituds, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollPaneLlistaProductes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorSimilituds)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(bgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botoImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botoCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bgPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
    * Mètode que gestiona l'esdeveniment del clic al botó sortida,
    * reinicia els etiquetatges i tanca la vista actual.
    */
    private void botoSortirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botoSortirMouseClicked
        resetLabels();
        sortir();
    }//GEN-LAST:event_botoSortirMouseClicked

    /**
    * Mètode que gestiona l'esdeveniment del clic al botó crear,
    * valida les entrades i crea un nou producte si tot és correcte.
    */
    private void botoCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botoCrearMouseClicked
        boolean idValid = validarId();
        boolean nomValid = validarNom();
        boolean similitudsValides = validarSimilituds();

        if (idValid && nomValid && similitudsValides) {
            try {
                String id = textId.getText().trim();
                String nom = textNom.getText().trim();
                String similituds = textAreaSims.getText().trim();

                if (similituds.isEmpty()) {
                    similituds = "{}";
                }

                cp.crearProducte(id, nom, similituds);
                resetLabels();
                mostrarProductesEnJList();
                JOptionPane.showMessageDialog(this, "Producte creat!", "CONFIRMACIÓ", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en crear el producte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_botoCrearMouseClicked

    /**
    * Mètode que gestiona l'esdeveniment del clic al text area de similituds,
    * canviant el color del text quan es fa clic.
    */
    private void textAreaSimsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textAreaSimsMousePressed
        textAreaSims.setForeground(Color.black);
    }//GEN-LAST:event_textAreaSimsMousePressed

    /**
    * Mètode que gestiona l'esdeveniment quan es guanya focus al camp ID,
    * esborra el text per defecte si és necessari.
    */
    private void textIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textIdFocusGained
        if (textId.getText().equals("Introdueix un identificador numèric")) {
            textId.setText("");
            textId.setForeground(Color.black);
        }
    }//GEN-LAST:event_textIdFocusGained

    /**
    * Mètode que gestiona l'esdeveniment quan es perd focus al camp ID,
    * torna a mostrar el text per defecte si està buit.
    */
    private void textIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textIdFocusLost
        if ((textId.getText()).isEmpty()) {
            textId.setText("Introdueix un identificador numèric");
            textId.setForeground(Color.gray);
        }
    }//GEN-LAST:event_textIdFocusLost

    /**
    * Mètode que gestiona l'esdeveniment quan es guanya focus al camp Nom,
    * esborra el text per defecte si és necessari.
    */
    private void textNomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNomFocusGained
        if (textNom.getText().equals("Introdueix un nom")) {
            textNom.setText("");
            textNom.setForeground(Color.black);
        }
    }//GEN-LAST:event_textNomFocusGained

    /**
    * Mètode que gestiona l'esdeveniment quan es perd focus al camp Nom,
    * torna a mostrar el text per defecte si està buit.
    */
    private void textNomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNomFocusLost
        if ((textNom.getText()).isEmpty()) {
            textNom.setText("Introdueix un nom");
            textNom.setForeground(Color.gray);
        }
    }//GEN-LAST:event_textNomFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgPanel;
    private javax.swing.JButton botoCrear;
    private javax.swing.JButton botoImportar;
    private javax.swing.JButton botoSortir;
    private javax.swing.JLabel labelErrorId;
    private javax.swing.JLabel labelErrorNom;
    private javax.swing.JLabel labelErrorSimilituds;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelProductesRegistrats;
    private javax.swing.JLabel labelSimilituds;
    private javax.swing.JLabel labelTitol;
    private javax.swing.JList<String> llistaProductesExistents;
    private javax.swing.JScrollPane scrollPaneLlistaProductes;
    private javax.swing.JScrollPane scrollPaneSimilituds;
    private javax.swing.JSeparator separator;
    private javax.swing.JTextArea textAreaSims;
    private javax.swing.JTextField textId;
    private javax.swing.JTextField textNom;
    // End of variables declaration//GEN-END:variables
}
