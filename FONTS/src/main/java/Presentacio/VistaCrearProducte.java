package Presentacio;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author ariadna
 */
public class VistaCrearProducte extends javax.swing.JPanel {
    
    private CtrlPresentacio cp;

    /**
     * Creates new form VistaCrearProducte
     */
    public VistaCrearProducte() {
        initComponents();
        cp = new CtrlPresentacio();
        this.setSize(700, 500);
        mostrarProductesEnJList();
        prellenarAreaSimilituds();
    }
    
    private void resetLabels() {
        labelErrorId.setText("");
        labelErrorNom.setText("");
        labelErrorSimilituds.setText("");
        textId.setText("");
        textNom.setText("");
        prellenarAreaSimilituds();
    }
    
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

    
    private void mostrarProductesEnJList() {
        Map<String, Map<String, String>> productes = crearExempleProductes();
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String id : productes.keySet()) {
            model.addElement(id + " - " + productes.get(id).get("nom"));
        }

        llistaProductesExistents.setModel(model);
    }
    
    
    private void prellenarAreaSimilituds() {
        Map<String, Map<String, String>> productes = crearExempleProductes();
        StringBuilder builder = new StringBuilder();

        for (String id : productes.keySet()) {
            builder.append(id).append(": ").append("\n"); // El usuario completará los valores
        }

        textAreaSims.setText(builder.toString());
    }
    
    /*
    private void importarProductosDesdeArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                Map<String, Map<String, String>> productosNuevos = new HashMap<>();
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length < 2) {
                        JOptionPane.showMessageDialog(this, "Línia incorrecta: " + linea, "Error de Format", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    if (!productosNuevos.containsKey(id)) {
                        Map<String, String> detalles = new HashMap<>();
                        detalles.put("id", id);
                        detalles.put("nom", nombre);
                        detalles.put("similituds", "{}");
                        detalles.put("posPrestatgeries", "{}");
                        productosNuevos.put(id, detalles);
                    } else {
                        JOptionPane.showMessageDialog(this, "Producte duplicat al fitxer: " + id, "Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                // Aquí actualizas el sistema con los productos nuevos
                // cp.afegirProductes(productosNuevos);
                JOptionPane.showMessageDialog(this, "Productes importats correctament!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No s'ha pogut llegir el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }*/


    
    public static Map<String, Map<String, String>> crearExempleProductes() {
        Map<String, Map<String, String>> productes = new HashMap<>();

        // Producto 1: Patates
        Map<String, String> producte1 = new HashMap<>();
        producte1.put("id", "1");
        producte1.put("nom", "Patates");
        producte1.put("similituds", "{\"2\":0.5}");
        producte1.put("posPrestatgeries", "{}"); // Vacío porque no está en ninguna estantería
        productes.put("1", producte1);

        // Producto 2: Poma
        Map<String, String> producte2 = new HashMap<>();
        producte2.put("id", "2");
        producte2.put("nom", "Poma");
        producte2.put("similituds", "{\"1\":0.5}");
        producte2.put("posPrestatgeries", "{}"); // Vacío porque no está en ninguna estantería
        productes.put("2", producte2);

        return productes;
    }
    
    private boolean validarId() {
        String id = textId.getText().trim();

        if (id.isEmpty() || id.equals("Introdueix un identificador numèric")) {
            labelErrorId.setText("Error: Has d'introduir un identificador.");
            labelErrorId.setForeground(Color.red);
            return false;
        }
        try {
            Map<String, Map<String, String>> productes = crearExempleProductes();
            int idNum = Integer.parseInt(id);

            if(idNum <= 0) {
                labelErrorId.setText("L'id ha de ser un nombre positiu.");
                return false;
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
        }
    }
    
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

    private boolean validarSimilituds() {
        String similituds = textAreaSims.getText().trim(); // Entrada del usuario
        Map<String, Map<String, String>> productes = crearExempleProductes();

        if (productes.isEmpty()) {
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
            }

            // Tot correcte
            labelErrorSimilituds.setText("Totes les similituds estan introduïdes correctament.");
            labelErrorSimilituds.setForeground(Color.green);
            return true;

        } catch (Exception e) {
            labelErrorSimilituds.setText("Error en el format de les similituds.");
            labelErrorSimilituds.setForeground(Color.red);
            return false;
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

        bg = new javax.swing.JPanel();
        labelTitol = new javax.swing.JLabel();
        separator7 = new javax.swing.JSeparator();
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
        scrollPaneProductes = new javax.swing.JScrollPane();
        llistaProductesExistents = new javax.swing.JList<>();
        scrollPaneSimilituds = new javax.swing.JScrollPane();
        textAreaSims = new javax.swing.JTextArea();
        labelId = new javax.swing.JLabel();
        labelProductesRegistrats = new javax.swing.JLabel();

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bgMouseClicked(evt);
            }
        });

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
        textId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textIdMousePressed(evt);
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
        textNom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textNomMousePressed(evt);
            }
        });

        labelSimilituds.setText("Similituds amb altres productes (ex., 2: 0.75, 3: 0.4):");
        labelSimilituds.setFocusable(false);

        labelErrorNom.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        labelErrorNom.setText(" ");

        labelErrorSimilituds.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        labelErrorSimilituds.setForeground(new java.awt.Color(255, 255, 255));
        labelErrorSimilituds.setText("jLabel1");

        llistaProductesExistents.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Producte 1", "Producte 2", "Producte 3", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrollPaneProductes.setViewportView(llistaProductesExistents);

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

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addComponent(separator7, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelProductesRegistrats)
                                    .addComponent(scrollPaneProductes, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                                        .addComponent(botoImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(botoCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                                        .addComponent(labelNom)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textNom))
                                    .addComponent(labelErrorId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textId, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(scrollPaneSimilituds, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bgLayout.createSequentialGroup()
                                        .addComponent(labelErrorNom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(69, 69, 69))
                                    .addComponent(labelErrorSimilituds, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(labelSimilituds, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelId, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(25, 25, 25))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(labelTitol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botoSortir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(labelTitol))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(botoSortir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelId)
                    .addComponent(labelProductesRegistrats))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(textId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelErrorId)
                        .addGap(12, 12, 12)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelNom))
                        .addGap(6, 6, 6)
                        .addComponent(labelErrorNom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelSimilituds)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPaneSimilituds, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                    .addComponent(scrollPaneProductes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelErrorSimilituds)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botoImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botoCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botoSortirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botoSortirMouseClicked
        //this.dispose();
    }//GEN-LAST:event_botoSortirMouseClicked

    private void textIdMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textIdMousePressed
        if (textId.getText().equals("Introdueix un identificador numèric")) {
            textId.setText("");
            textId.setForeground(Color.black);
        }
        if ((textNom.getText()).isEmpty()) {
            textNom.setText("Introdueix un nom");
            textNom.setForeground(Color.gray);
        }
    }//GEN-LAST:event_textIdMousePressed

    private void botoCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botoCrearMouseClicked
        boolean idValid = validarId();
        boolean nomValid = validarNom();
        boolean similitudsValides = validarSimilituds();

        if (idValid && nomValid && similitudsValides) {
            Map<String, Map<String, String>> prods = crearExempleProductes();
            Map<String, String> p = new HashMap<>();
            p.put("id", textId.getText());
            p.put("nom", textNom.getText());
            p.put("similituds", textAreaSims.getText());
            p.put("posPrestatgeries", "{}"); // Buit perquè no està en cap prestatgeria
            prods.put(textId.getText(), p);
            labelProductesRegistrats.setText("Nou producte: " + textNom.getText());

            resetLabels();
            JOptionPane.showMessageDialog(this, "Producte creat!", "CONFIRMACIÓ", JOptionPane.INFORMATION_MESSAGE);
            //cp.crearProducte(textId.getText().trim(), textNom.getText().trim(), textAreaSims.getText().trim());
        }
    }//GEN-LAST:event_botoCrearMouseClicked

    private void textNomMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textNomMousePressed
        if (textNom.getText().equals("Introdueix un nom")) {
            textNom.setText("");
            textNom.setForeground(Color.black);
        }
        if ((textId.getText()).isEmpty()) {
            textId.setText("Introdueix un identificador numèric");
            textId.setForeground(Color.gray);
        }
    }//GEN-LAST:event_textNomMousePressed

    private void textAreaSimsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textAreaSimsMousePressed
        if (textId.getText().equals("")) {
            textId.setText("Introdueix un identificador numèric");
            textId.setForeground(Color.gray);
        }
        if ((textNom.getText()).equals("")) {
            textNom.setText("Introdueix un nom");
            textNom.setForeground(Color.gray);
        }
        textAreaSims.setForeground(Color.black);
    }//GEN-LAST:event_textAreaSimsMousePressed

    private void bgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgMouseClicked
        if (textId.getText().equals("")) {
            textId.setText("Introdueix un identificador numèric");
            textId.setForeground(Color.gray);
        }
        if ((textNom.getText()).equals("")) {
            textNom.setText("Introdueix un nom");
            textNom.setForeground(Color.gray);
        }
    }//GEN-LAST:event_bgMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
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
    private javax.swing.JScrollPane scrollPaneProductes;
    private javax.swing.JScrollPane scrollPaneSimilituds;
    private javax.swing.JSeparator separator7;
    private javax.swing.JTextArea textAreaSims;
    private javax.swing.JTextField textId;
    private javax.swing.JTextField textNom;
    // End of variables declaration//GEN-END:variables
}
