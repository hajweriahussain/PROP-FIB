package Presentacio;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ariadna
 */
public class VistaProducte extends javax.swing.JPanel {

    private CardLayout cardLayout;
    private VistaCrearProducte vistaCrearProducte;
    private GridBagConstraints gbc;
    private CtrlPresentacio cp;
    
    /**
     * Creates new form VistaProducte
     */
    public VistaProducte() {
        cp = new CtrlPresentacio();
        initComponents();
        this.setSize(700, 500);
        llistaPanel.setSize(700, 500);
        infoPanel.setSize(700, 500);
        editarPanel.setSize(700,500);
        configurarPanelGeneral();
        initGridProds();
        titolLlistaConstraints();
        carregarProductesEnScrollPanel();
        configurarBotons();
        configurarEditarPanel();
    }
    
    public void initGridProds() {
        productPanel.setLayout(new GridLayout(0, 4, 10, 10));
    }
    
    public void configurarPanelGeneral() {
        cardLayout = new CardLayout();
        jPanelGeneral.setLayout(cardLayout);

        jPanelGeneral.add(llistaPanel, "llistaPanel");
        jPanelGeneral.add(infoPanel, "infoPanel");
        jPanelGeneral.add(editarPanel, "editarPanel");
        
        vistaCrearProducte = new VistaCrearProducte();
        vistaCrearProducte.setVistaProducte(this);
        jPanelGeneral.add(vistaCrearProducte, "crearProductePanel");
        
        cardLayout.show(jPanelGeneral, "llistaPanel");
    }
        
    public void carregarProductesEnScrollPanel() {
        Map<String, Map<String, String>> prods = cp.mostrarProductes();
        for (String id : prods.keySet()) {
            String nom = prods.get(id).get("nom");
            
            
            JButton botoProducte = new JButton(id + "-" + nom);
            
            botoProducte.addActionListener(e -> {
                String similituds = prods.get(id).get("similituds");
                String posPrestatgeries = prods.get(id).get("posPrestatgeries");
                textIdInfo.setText(id);
                textNomInfo.setText(nom);
                
                // Convertir la cadena de similituds a un format llegible
                StringBuilder sbSimilituds = new StringBuilder();
                String[] similitudEntries = similituds.replace("{", "").replace("}", "").split(",");

                for (String entry : similitudEntries) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        String productId = parts[0].trim().replace("\"", "");
                        String similarity = parts[1].trim();
                        sbSimilituds.append("Producte ID: ").append(productId)
                                    .append(", Similitud: ").append(similarity).append("\n");
                    }
                }
                textAreaSimilituds.setText(sbSimilituds.toString());
                
                StringBuilder sbPosPrestatgeries = new StringBuilder();
                if (posPrestatgeries.equals("{}") || posPrestatgeries.trim().isEmpty()) {
                    sbPosPrestatgeries.append("El producte no està en cap prestatgeria.");
                }
                else {
                    posPrestatgeries = posPrestatgeries.substring(1, posPrestatgeries.length() - 1);
                    String[] prestatgeriesEntries = posPrestatgeries.split("(?<=\\]),(?=\\\")");

                    for (String entry : prestatgeriesEntries) {
                        String[] parts = entry.split(":");
                        if (parts.length == 2) {
                            String shelfId = parts[0].replace("\"", "").trim();
                            String positions = parts[1].replace("[", "").replace("]", "").trim();
                            String[] coordinates = positions.split(",");

                            if (coordinates.length == 2) {
                                int row = Integer.parseInt(coordinates[0].trim());
                                int column = Integer.parseInt(coordinates[1].trim());

                                sbPosPrestatgeries.append("Prestatgeria ID: ").append(shelfId)
                                                  .append(", Fila: ").append(row)
                                                  .append(", Columna: ").append(column)
                                                  .append("\n");
                            }
                        }
                    }
                }
                textAreaPosPrestatgeries.setText(sbPosPrestatgeries.toString());
                
                cardLayout.show(jPanelGeneral, "infoPanel");
                mostrarProductesEnJList();
            });
            productPanel.add(botoProducte);
        }
        panelProds.setViewportView(productPanel);
    }
    
    private void mostrarProductesEnJList() {
        Map<String, Map<String, String>> productes = cp.mostrarProductes();
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String id : productes.keySet()) {
            model.addElement(id + " - " + productes.get(id).get("nom"));
        }

        llistaProductesExistents.setModel(model);
    }

    private void configurarEditarPanel() {
        editarPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        // Títol
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(25, 25, 10, 0);
        editarPanel.add(labelTitolEditar, gbc);

        // Botó Sortir
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(25, 0, 10, 25);
        editarPanel.add(botoSortirEditar, gbc);

        // Label Nou ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 25, 5, 25);
        editarPanel.add(labelNouId, gbc);

        // TextField Nou ID
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        editarPanel.add(textNouId, gbc);

        // Label Nou Nom
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 25, 5, 25);
        editarPanel.add(labelNouNom, gbc);

        // TextField Nou Nom
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 25, 10, 25);
        editarPanel.add(textNouNom, gbc);

        // Botó Guardar
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 25, 0);
        editarPanel.add(botoGuardar, gbc);
    }

    public void titolLlistaConstraints() {
        llistaPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(25, 25, 20, 0);
        llistaPanel.add(labelTitolLlista, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(25, 0, 20, 25);
        llistaPanel.add(botoCrear, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 10);
        llistaPanel.add(panelProds, gbc);
    }
    
    private void configurarBotons() {
        botoCrear.addActionListener(e -> mostrarCrearProductePanel());
        botoEditar.addActionListener(e -> {
            cardLayout.show(jPanelGeneral, "editarPanel");
            textNouId.setText(textIdInfo.getText());
            textNouNom.setText(textNomInfo.getText());
            inicialitzarTaulaSimilituds();
        });
        botoGuardar.addActionListener(e -> {
            editarProducte();
            guardarSimilitudsEditades();
            cardLayout.show(jPanelGeneral, "infoPanel");
        });
        botoEliminar.addActionListener(e -> esborrarProducte());
    }
    
    private void editarProducte() {
        String idOriginal = textIdInfo.getText();
        String nouId = textNouId.getText();
        String nouNom = textNouNom.getText();
        
        boolean canvis = false;

        if (!nouId.isEmpty() && !nouId.equals(idOriginal)) {
            try {
                int idNumeric = Integer.parseInt(nouId);
                if (idNumeric <= 0) {
                    JOptionPane.showMessageDialog(this, "L'ID ha de ser un número positiu", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Map<String, Map<String, String>> prods = cp.mostrarProductes();
                if (prods.containsKey(nouId)) {
                    JOptionPane.showMessageDialog(this, "Aquest ID ja existeix per a un altre producte", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cp.editarIdProducte(idOriginal, nouId);
                canvis = true;
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "L'ID ha de ser un número vàlid", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (!nouNom.isEmpty() && !nouNom.equals("Introdueix un nou nom") && !nouNom.equals(textNomInfo.getText())) {
            cp.editarNomProducte(nouId.isEmpty() ? idOriginal : nouId, nouNom);
            canvis = true;
        }

        if (canvis) {
            actualitzarVistaProducte(nouId.isEmpty() ? idOriginal : nouId);
            JOptionPane.showMessageDialog(this, "Canvis guardats amb èxit", "Èxit", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualitzarVistaProducte(String id) {
        Map<String, Map<String, String>> prods = cp.mostrarProductes();
        Map<String, String> producte = prods.get(id);
        
        textIdInfo.setText(id);
        textNomInfo.setText(producte.get("nom"));
        
        String similituds = producte.get("similituds");
        StringBuilder sbSimilituds = new StringBuilder();
        String[] similitudEntries = similituds.replace("{", "").replace("}", "").split(",");

        for (String entry : similitudEntries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                String productId = parts[0].trim().replace("\"", "");
                String similarity = parts[1].trim();
                sbSimilituds.append("Producte ID: ").append(productId)
                        .append(", Similitud: ").append(similarity).append("\n");
            }
        }
        textAreaSimilituds.setText(sbSimilituds.toString());
        
        carregarProductesEnScrollPanel();
    }

    private void inicialitzarTaulaSimilituds() {
        String[] columnes = {"ID Producte", "Similitud"};
        DefaultTableModel model = new DefaultTableModel(columnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        taulaSimilituds.setModel(model);

        Map<String, Map<String, String>> prods = cp.mostrarProductes();
        String similituds = prods.get(textIdInfo.getText()).get("similituds");
        String[] similitudEntries = similituds.replace("{", "").replace("}", "").split(",");

        for (String entry : similitudEntries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                model.addRow(new Object[]{parts[0].trim().replace("\"", ""), parts[1].trim()});
            }
        }

        // Configuració addicional de la taula
        taulaSimilituds.setPreferredScrollableViewportSize(new Dimension(300, 100));
        taulaSimilituds.setFillsViewportHeight(true);

        // Actualitza el JScrollPane
        scrollPaneTaulaSimilituds.setViewportView(taulaSimilituds);

        // Configura les restriccions per al JScrollPane
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5; // Ajusta segons la posició desitjada
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 25, 10, 25);

        // Elimina el JScrollPane si ja existeix i torna a afegir-lo
        editarPanel.remove(scrollPaneTaulaSimilituds);
        editarPanel.add(scrollPaneTaulaSimilituds, gbc);

        // Revalida i repinta el panel
        editarPanel.revalidate();
        editarPanel.repaint();
    }


    private void guardarSimilitudsEditades() {
        StringBuilder sbSimilituds = new StringBuilder("{");
        DefaultTableModel model = (DefaultTableModel) taulaSimilituds.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String id = model.getValueAt(i, 0).toString();
            String similitud = model.getValueAt(i, 1).toString();
            sbSimilituds.append("\"").append(id).append("\":").append(similitud);
            if (i < model.getRowCount() - 1) {
                sbSimilituds.append(",");
            }
        }
        sbSimilituds.append("}");

        String idProducte = textIdInfo.getText();
        Map<String, Map<String, String>> prods = cp.mostrarProductes();
        prods.get(idProducte).put("similituds", sbSimilituds.toString());
    }
    
    private void esborrarProducte() {
        String idProd = textIdInfo.getText();

        int confirmacio = JOptionPane.showConfirmDialog(this,
            "Estàs segur que vols esborrar el producte amb ID " + idProd + "?",
            "Confirmar esborrat",
            JOptionPane.YES_NO_OPTION);

        if (confirmacio == JOptionPane.YES_OPTION) {
            cp.esborrarProducte(idProd);
            JOptionPane.showMessageDialog(this, "Producte esborrat amb èxit", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(jPanelGeneral, "llistaPanel");
            carregarProductesEnScrollPanel();
        }
    }


    // Mètode per mostrar el panel de crear producte
    public void mostrarCrearProductePanel() {
        cardLayout.show(jPanelGeneral, "crearProductePanel");
    }

    // Mètode per tornar al llistaPanel
    public void mostrarLlistaPanel() {
        cardLayout.show(jPanelGeneral, "llistaPanel");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelGeneral = new javax.swing.JPanel();
        llistaPanel = new javax.swing.JPanel();
        labelTitolLlista = new javax.swing.JLabel();
        botoCrear = new javax.swing.JButton();
        panelProds = new javax.swing.JScrollPane();
        productPanel = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        labelTitol = new javax.swing.JLabel();
        botoSortir = new javax.swing.JButton();
        textIdInfo = new javax.swing.JTextField();
        textNomInfo = new javax.swing.JTextField();
        scrollPaneProductes = new javax.swing.JScrollPane();
        llistaProductesExistents = new javax.swing.JList<>();
        botoEditar = new javax.swing.JButton();
        botoEliminar = new javax.swing.JButton();
        scrollPaneSimilituds = new javax.swing.JScrollPane();
        textAreaSimilituds = new javax.swing.JTextArea();
        scrollPanePosPrestatgeries = new javax.swing.JScrollPane();
        textAreaPosPrestatgeries = new javax.swing.JTextArea();
        labelSimilituds = new javax.swing.JLabel();
        labelNom = new javax.swing.JLabel();
        labelId = new javax.swing.JLabel();
        labelLlistaProductes = new javax.swing.JLabel();
        labelPosPrestatgeries = new javax.swing.JLabel();
        editarPanel = new javax.swing.JPanel();
        labelTitolEditar = new javax.swing.JLabel();
        labelNouId = new javax.swing.JLabel();
        labelNouNom = new javax.swing.JLabel();
        textNouId = new javax.swing.JTextField();
        textNouNom = new javax.swing.JTextField();
        botoGuardar = new javax.swing.JButton();
        botoSortirEditar = new javax.swing.JButton();
        scrollPaneTaulaSimilituds = new javax.swing.JScrollPane();
        taulaSimilituds = new javax.swing.JTable();

        jPanelGeneral.setLayout(new java.awt.CardLayout());

        llistaPanel.setBackground(new java.awt.Color(255, 255, 255));
        llistaPanel.setPreferredSize(new java.awt.Dimension(700, 500));
        llistaPanel.setVerifyInputWhenFocusTarget(false);
        llistaPanel.setLayout(new java.awt.GridBagLayout());

        labelTitolLlista.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        labelTitolLlista.setText("ELS TEUS PRODUCTES");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 24, 0, 0);
        llistaPanel.add(labelTitolLlista, gridBagConstraints);

        botoCrear.setText("Crear Nou Producte");
        botoCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoCrearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(37, 108, 0, 0);
        llistaPanel.add(botoCrear, gridBagConstraints);

        productPanel.setBackground(new java.awt.Color(255, 255, 255));
        productPanel.setPreferredSize(new java.awt.Dimension(650, 400));
        productPanel.setLayout(new java.awt.GridLayout(1, 0));
        panelProds.setViewportView(productPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 636;
        gridBagConstraints.ipady = 386;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 31, 24);
        llistaPanel.add(panelProds, gridBagConstraints);

        jPanelGeneral.add(llistaPanel, "card2");

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));
        infoPanel.setPreferredSize(new java.awt.Dimension(700, 500));

        labelTitol.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        labelTitol.setText("INFORMACIÓ DEL PRODUCTE");

        botoSortir.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        botoSortir.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        botoSortir.setText("X");
        botoSortir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botoSortir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoSortirActionPerformed(evt);
            }
        });

        textIdInfo.setEditable(false);

        textNomInfo.setEditable(false);

        llistaProductesExistents.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Producte 1", "Producte 2", "Producte 3", "Producte 4", "Producte 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        llistaProductesExistents.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        scrollPaneProductes.setViewportView(llistaProductesExistents);

        botoEditar.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        botoEditar.setText("Editar");
        botoEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        botoEliminar.setBackground(new java.awt.Color(255, 51, 51));
        botoEliminar.setFont(new java.awt.Font("Liberation Sans", 0, 15)); // NOI18N
        botoEliminar.setText("Eliminar");
        botoEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        textAreaSimilituds.setColumns(20);
        textAreaSimilituds.setRows(5);
        scrollPaneSimilituds.setViewportView(textAreaSimilituds);

        textAreaPosPrestatgeries.setColumns(20);
        textAreaPosPrestatgeries.setRows(5);
        scrollPanePosPrestatgeries.setViewportView(textAreaPosPrestatgeries);

        labelSimilituds.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        labelSimilituds.setText("Similituds amb altres productes:");

        labelNom.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        labelNom.setText("Nom:");

        labelId.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        labelId.setText("Identificador (ID):");

        labelLlistaProductes.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        labelLlistaProductes.setText("Llista de productes:");

        labelPosPrestatgeries.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        labelPosPrestatgeries.setText("Posició en prestatgeries:");

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addComponent(botoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botoEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                        .addComponent(labelTitol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botoSortir, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPaneProductes)
                                .addGroup(infoPanelLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(labelNom)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(textNomInfo))
                                .addGroup(infoPanelLayout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addComponent(labelId)
                                    .addGap(18, 18, 18)
                                    .addComponent(textIdInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                            .addComponent(labelLlistaProductes))
                        .addGap(66, 66, 66)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(scrollPanePosPrestatgeries, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                                .addComponent(scrollPaneSimilituds))
                            .addComponent(labelSimilituds)
                            .addComponent(labelPosPrestatgeries))
                        .addGap(66, 66, 66))))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitol)
                    .addComponent(botoSortir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(labelSimilituds)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPaneSimilituds, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(labelPosPrestatgeries)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPanePosPrestatgeries, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelId)
                            .addComponent(textIdInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelNom)
                            .addComponent(textNomInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addComponent(labelLlistaProductes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPaneProductes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(60, 60, 60)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botoEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanelGeneral.add(infoPanel, "card3");

        editarPanel.setBackground(new java.awt.Color(255, 255, 255));

        labelTitolEditar.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        labelTitolEditar.setText("EDITAR PRODUCTE");

        labelNouId.setText("Nou identificador:");

        labelNouNom.setText("Nou nom:");

        textNouId.setForeground(new java.awt.Color(153, 153, 153));
        textNouId.setText("Introdueix un nou identificador");
        textNouId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textNouIdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textNouIdFocusLost(evt);
            }
        });

        textNouNom.setForeground(new java.awt.Color(153, 153, 153));
        textNouNom.setText("Introdueix un nou nom");
        textNouNom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textNouNomFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textNouNomFocusLost(evt);
            }
        });

        botoGuardar.setText("Guardar");

        botoSortirEditar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        botoSortirEditar.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        botoSortirEditar.setText("X");
        botoSortirEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botoSortirEditarActionPerformed(evt);
            }
        });

        taulaSimilituds.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        scrollPaneTaulaSimilituds.setViewportView(taulaSimilituds);

        javax.swing.GroupLayout editarPanelLayout = new javax.swing.GroupLayout(editarPanel);
        editarPanel.setLayout(editarPanelLayout);
        editarPanelLayout.setHorizontalGroup(
            editarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarPanelLayout.createSequentialGroup()
                .addGroup(editarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(labelNouId))
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(textNouId, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(labelNouNom))
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(textNouNom, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(scrollPaneTaulaSimilituds, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editarPanelLayout.createSequentialGroup()
                        .addGap(615, 615, 615)
                        .addComponent(botoGuardar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(editarPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(labelTitolEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botoSortirEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        editarPanelLayout.setVerticalGroup(
            editarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editarPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(editarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitolEditar)
                    .addComponent(botoSortirEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(labelNouId)
                .addGap(12, 12, 12)
                .addComponent(textNouId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelNouNom)
                .addGap(12, 12, 12)
                .addComponent(textNouNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(scrollPaneTaulaSimilituds, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(botoGuardar))
        );

        jPanelGeneral.add(editarPanel, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botoCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoCrearActionPerformed
        if (vistaCrearProducte == null) {
            vistaCrearProducte = new VistaCrearProducte();
        }
        vistaCrearProducte.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_botoCrearActionPerformed

    private void botoSortirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoSortirActionPerformed
        CardLayout cardLayout = (CardLayout) jPanelGeneral.getLayout();
        cardLayout.show(jPanelGeneral, "llistaPanel");
    }//GEN-LAST:event_botoSortirActionPerformed

    private void textNouIdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNouIdFocusGained
        if (textNouId.getText().equals("Introdueix un nou identificador")) {
            textNouId.setText("");
            textNouId.setForeground(Color.black);
        }
    }//GEN-LAST:event_textNouIdFocusGained

    private void textNouIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNouIdFocusLost
        if (textNouId.getText().isEmpty()) {
            textNouId.setText("Introdueix un nou identificador");
            textNouId.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_textNouIdFocusLost

    private void textNouNomFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNouNomFocusGained
        if (textNouNom.getText().equals("Introdueix un nou nom")) {
            textNouNom.setText("");
            textNouNom.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_textNouNomFocusGained

    private void textNouNomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textNouNomFocusLost
        if (textNouNom.getText().isEmpty()) {
            textNouNom.setText("Introdueix un nou nom");
            textNouNom.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_textNouNomFocusLost

    private void botoSortirEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoSortirEditarActionPerformed
        CardLayout cardLayout = (CardLayout) jPanelGeneral.getLayout();
        cardLayout.show(jPanelGeneral, "infoPanel");
    }//GEN-LAST:event_botoSortirEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botoCrear;
    private javax.swing.JButton botoEditar;
    private javax.swing.JButton botoEliminar;
    private javax.swing.JButton botoGuardar;
    private javax.swing.JButton botoSortir;
    private javax.swing.JButton botoSortirEditar;
    private javax.swing.JPanel editarPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel jPanelGeneral;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelLlistaProductes;
    private javax.swing.JLabel labelNom;
    private javax.swing.JLabel labelNouId;
    private javax.swing.JLabel labelNouNom;
    private javax.swing.JLabel labelPosPrestatgeries;
    private javax.swing.JLabel labelSimilituds;
    private javax.swing.JLabel labelTitol;
    private javax.swing.JLabel labelTitolEditar;
    private javax.swing.JLabel labelTitolLlista;
    private javax.swing.JPanel llistaPanel;
    private javax.swing.JList<String> llistaProductesExistents;
    private javax.swing.JScrollPane panelProds;
    private javax.swing.JPanel productPanel;
    private javax.swing.JScrollPane scrollPanePosPrestatgeries;
    private javax.swing.JScrollPane scrollPaneProductes;
    private javax.swing.JScrollPane scrollPaneSimilituds;
    private javax.swing.JScrollPane scrollPaneTaulaSimilituds;
    private javax.swing.JTable taulaSimilituds;
    private javax.swing.JTextArea textAreaPosPrestatgeries;
    private javax.swing.JTextArea textAreaSimilituds;
    private javax.swing.JTextField textIdInfo;
    private javax.swing.JTextField textNomInfo;
    private javax.swing.JTextField textNouId;
    private javax.swing.JTextField textNouNom;
    // End of variables declaration//GEN-END:variables
}
