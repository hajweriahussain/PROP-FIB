/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Presentacio;

import Exceptions.DominiException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author hajweriahussain
 */
public class VistaPrestatgeria extends javax.swing.JPanel {
    private CardLayout cardLayout;
    private CtrlPresentacio cp;
    private VistaCrearPrestatgeria vistaCrear;
    private GridBagConstraints gbc;
    private JLabel selectedLabel1 = null; // Para almacenar el primer JLabel seleccionado
    private JLabel selectedLabel2 = null;
        // Declara la vista
    /**
     * Creates new form VistaPrestatgeria
     */
    public VistaPrestatgeria() {
        cp = new CtrlPresentacio();
        initComponents();
        this.setSize(800, 800);
        page1.setSize(800,800);
        page2.setSize(800,800);
        cardInit();
        page1Constraints();
        initGridPres();
        mostrarPrestatgeries();
        
    }
    
    public void mostrarCrearPrestatgeriaPanel() {
        cardLayout.show(bg, "crearPrestatgeriaPanel");
    }
    
    public void initGridPres(){
        panelGrid.setLayout(new GridLayout(0, 4, 10, 10)); // Espaciado de 10px
    }
    
    public void cardInit(){
        cardLayout = new CardLayout();
        bg.setLayout(cardLayout); // Asignar el CardLayout al JPanel 'bg'

        bg.add(page1, "Page1");
        bg.add(page2, "Page2");

         // Mostrar Page1 por defecto
         cardLayout.show(bg, "Page1");
    }
    
    public void page1Constraints() {
        page1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(25, 25, 20, 0);
        page1.add(title1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(25, 0, 20, 25);
        page1.add(btnCrear, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 10, 10);
        page1.add(panelPres, gbc);
        btnCrear.addActionListener(e -> mostrarCrearPrestatgeriaPanel());
    }
    
    public Map<String, Map<String, String>> crearEjemploPrestatgeries() {
        Map<String, Map<String, String>> prestatgeries = new HashMap<>();
        
        Map<String, String> prestatgeria1 = new HashMap<>();
        prestatgeria1.put("id", "1");
        prestatgeria1.put("nom", "Estanteria Principal");
        prestatgeria1.put("files", "5");
        prestatgeria1.put("columnes", "3");
        prestatgeria1.put("productes", "[1,2,3,4,5,6]");
        prestatgeria1.put("layout", "Fila 0: (A1, 1), (K2, 2) | Fila 1: (B1, 3), (B2, 4) | Fila 2: (C1, 5), (C2, 6)");

        
        for ( int i = 0; i < 13; ++i){
            prestatgeries.put(Integer.toString(i), prestatgeria1);
        }

        

        return prestatgeries;
    }
    
    public void cargarPrestatgeriesEnScrollPanel(){
        Map<String, Map<String,String>> pres = cp.mostrarPrestatgeries();
        

//        Map<String, Map<String,String>> pres = crearEjemploPrestatgeries();
        
        if (pres == null || pres.isEmpty()) {
                panelGrid.removeAll();
                return;
            }

        panelGrid.removeAll();
//        try{
        if(pres != null) {
            for (String id : pres.keySet()) {
                String nom = pres.get(id).get("nom"); 
                String layout = pres.get(id).get("layout"); 

                JButton btnPrestatgeria = new JButton(id + " - " + nom);

                btnPrestatgeria.addActionListener(e -> {
                    title2.setText(nom);
                    idPres.setText(id);
                    mostrarDispEnPage2(layout);
                    cardLayout.show(bg, "Page2");
                });
                panelGrid.add(btnPrestatgeria);
            }
            panelGrid.revalidate();
            panelGrid.repaint();
            panelPres.setViewportView(panelGrid);
        }
//        }catch (DominiException e) {
//            javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat: " + e.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
    }
    
    public void mostrarDispEnPage2(String layout) {
        gridPanel.removeAll(); // Limpia el panel antes de añadir nuevos componentes
        gridPanel.revalidate();
        gridPanel.repaint();

        // Dividir el layout en filas
        String[] filas = layout.split("\\|");
        if (filas == null || filas.length == 0) {
            System.out.println("El layout está vacío o es nulo.");
            return;
        }
        int numFilas = filas.length;
        int numColumnas = 0; // Suponemos que las filas tienen el mismo número de columnas

        // Contar el número de columnas (de la primera fila)
        if (numFilas > 0) {
            String primeraFila = filas[0].split(":")[1].trim(); // Obtener solo las posiciones
            numColumnas = primeraFila.split(",").length;
        }

        // Configurar el GridLayout dinámicamente
        gridPanel.setLayout(new GridLayout(numFilas, numColumnas, 10, 10)); // Espaciado de 10px entre celdas

        // Procesar cada fila
        for (String fila : filas) {
            String[] partes = fila.split(":"); // Separar "Fila X" de las posiciones
            if (partes.length < 2) {
                continue;
            }

            String posiciones = partes[1].trim(); // Obtener posiciones
            String[] elementos = posiciones.split("\\),\\s*"); // Divide entre cada par de posiciones

            for (String elemento : elementos) {
                elemento = elemento.replace("(", "").replace(")", "").trim();
                String[] claveValor = elemento.split(",\\s*");

                // Verificar que el elemento está bien formado
                if (claveValor.length != 2) {
                    System.out.println("Formato incorrecto: " + elemento);
                    continue;
                }

                String clave = claveValor[0];
                String valor = claveValor[1];

                // Crear JLabel para la clave-valor
                JLabel label = new JLabel(clave + ":" + valor);
                label.setBackground(new Color(0xf3d9b1));
                label.setPreferredSize(new Dimension(150, 50));
                label.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                
                label.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        manejarSeleccion(label);
                    }
                });
                gridPanel.add(label);
            }
        }

        // Actualizar el panel después de añadir los componentes
        gridPanel.revalidate();
        gridPanel.repaint();

    }
    
    private void manejarSeleccion(JLabel label) {
        if (selectedLabel1 == null) {
            selectedLabel1 = label;
            label.setBackground(Color.YELLOW); // Marcar como seleccionada
        } else if (selectedLabel2 == null && label != selectedLabel1) {
            selectedLabel2 = label;
            label.setBackground(Color.YELLOW); // Marcar como seleccionada
        } else {
            if (label == selectedLabel1) {
                selectedLabel1.setBackground(new Color(0xf3d9b1));
                selectedLabel1 = null;
            } else if (label == selectedLabel2) {
                selectedLabel2.setBackground(new Color(0xf3d9b1));
                selectedLabel2 = null;
            }
        }
    }
    
    public void intercanviarDosProductes(){
        if (selectedLabel1 != null && selectedLabel2 != null) {
            String texto1 = selectedLabel1.getText();
            String texto2 = selectedLabel2.getText();
            
            String idProducto1 = (texto1.split(":")[1]);
            String idProducto2 = (texto2.split(":")[1]);
            String idPrestatgeria = idPres.getText();

            // Resetear selección
            selectedLabel1.setBackground(new Color(0xf3d9b1));
            selectedLabel2.setBackground(new Color(0xf3d9b1));
            selectedLabel1 = null;
            selectedLabel2 = null;
            try{
                cp.modificarPosicioProductes(idPrestatgeria, idProducto1, idProducto2);
                JOptionPane.showMessageDialog(gridPanel, "Intercanvi realitzat.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(bg, "Page1");
                mostrarPrestatgeries();
            }catch (Exception e) {
                JOptionPane.showMessageDialog(gridPanel, "Error al intercambiar productes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(gridPanel, "Selecciona dos productes per intercanviar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void mostrarPrestatgeries() {
        cardLayout.show(bg, "Page1");
        cargarPrestatgeriesEnScrollPanel();
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

        bg = new javax.swing.JPanel();
        page1 = new javax.swing.JPanel();
        title1 = new javax.swing.JLabel();
        panelPres = new javax.swing.JScrollPane();
        panelGrid = new javax.swing.JPanel();
        btnCrear = new javax.swing.JButton();
        page2 = new javax.swing.JPanel();
        title2 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        gridPanel = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        idPres = new javax.swing.JLabel();
        btnIntercambiar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setLayout(new java.awt.CardLayout());

        page1.setBackground(new java.awt.Color(255, 255, 255));
        page1.setLayout(new java.awt.GridBagLayout());

        title1.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        title1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title1.setText("LES TEVES PRESTATGERIES");
        title1.setAlignmentX(0.5F);
        page1.add(title1, new java.awt.GridBagConstraints());

        panelPres.setBackground(new java.awt.Color(255, 255, 255));

        panelGrid.setBackground(new java.awt.Color(255, 255, 255));
        panelGrid.setLayout(new java.awt.GridLayout(1, 0));
        panelPres.setViewportView(panelGrid);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 740;
        gridBagConstraints.ipady = 501;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(25, 26, 13, 18);
        page1.add(panelPres, gridBagConstraints);

        btnCrear.setText("Crear Nova Prestatgeria");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });
        page1.add(btnCrear, new java.awt.GridBagConstraints());

        bg.add(page1, "card2");

        page2.setBackground(new java.awt.Color(255, 255, 255));

        title2.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        title2.setText("PRESTATGERIA");

        btnBack.setText("Enrere");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        gridPanel.setBackground(new java.awt.Color(255, 255, 255));
        gridPanel.setPreferredSize(new java.awt.Dimension(1000, 700));
        gridPanel.setLayout(new java.awt.GridLayout(1, 0));

        btnEliminar.setText("Esborrar Prestatgeria");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        idPres.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        idPres.setText("ID");

        btnIntercambiar.setText("Intercanviar");
        btnIntercambiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntercambiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout page2Layout = new javax.swing.GroupLayout(page2);
        page2.setLayout(page2Layout);
        page2Layout.setHorizontalGroup(
            page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(page2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(page2Layout.createSequentialGroup()
                        .addGroup(page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(page2Layout.createSequentialGroup()
                                .addComponent(title2)
                                .addGap(18, 18, 18)
                                .addComponent(idPres))
                            .addComponent(gridPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 788, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(page2Layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnIntercambiar)
                        .addGap(55, 55, 55)
                        .addComponent(btnEliminar)))
                .addGap(6, 6, 6))
        );
        page2Layout.setVerticalGroup(
            page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(page2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnEliminar)
                    .addComponent(btnIntercambiar))
                .addGap(26, 26, 26)
                .addGroup(page2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(title2)
                    .addComponent(idPres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gridPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bg.add(page2, "card3");

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

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
//        if (vistaCrear == null) {
//            vistaCrear = new VistaCrearPrestatgeria(); // Instancia de tu JPanel externo
//            bg.add(vistaCrear, "VistaCrear");          // Agregar al CardLayout
//        }
//
//    // Mostrar la vista 'VistaCrear'
//        cardLayout.show(bg, "VistaCrear");

        try{
            if (cp.mostrarProductes() != null && !cp.mostrarProductes().isEmpty()){
                if (vistaCrear == null) {
                    vistaCrear = new VistaCrearPrestatgeria();
                    bg.add(vistaCrear, "vistaCrear");
                }
                cardLayout.show(bg, "vistaCrear");
                
            }else {
                 JOptionPane.showMessageDialog(this, "No hi ha productes disponibles. Si us plau, crea un producte per a poder crear una prestatgeria.", "No tens productes" , JOptionPane.WARNING_MESSAGE);
            }
        }
        catch(DominiException e){
             JOptionPane.showMessageDialog(this, "S'ha produit un error.", "." + e.getMessage(), JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        cardLayout.show(bg, "Page1");
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String id = idPres.getText();

        int confirmacio = JOptionPane.showConfirmDialog(this, "Estàs segur que vols esborrar la prestatgeria amb ID " + id + "?", "Confirmar esborrat", JOptionPane.YES_NO_OPTION);

        if (confirmacio == JOptionPane.YES_OPTION) {
            try {
                cp.esborrarPrestatgeria(id);
                JOptionPane.showMessageDialog(this, "Prestatgeria esborrada amb èxit", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(bg, "Page1");
                mostrarPrestatgeries();   // Torna a carregar els botons dels productes al llistaPanel
            } catch (DominiException ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ha hagut un error inesperat al esborrar la prestatgeria: " + ex.getMessage(), "Error Desconegut", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnIntercambiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntercambiarActionPerformed
        intercanviarDosProductes();
    }//GEN-LAST:event_btnIntercambiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnIntercambiar;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JLabel idPres;
    private javax.swing.JPanel page1;
    private javax.swing.JPanel page2;
    private javax.swing.JPanel panelGrid;
    private javax.swing.JScrollPane panelPres;
    private javax.swing.JLabel title1;
    private javax.swing.JLabel title2;
    // End of variables declaration//GEN-END:variables
}
