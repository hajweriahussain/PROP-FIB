/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacio;

/**
 *
 * @author laura
 */
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configurar el GUI usando el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Crear la ventana principal (JFrame)
            JFrame frame = new JFrame("Vista de Inicio de Sesión");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450); // Tamaño de la ventana
            frame.setLocationRelativeTo(null); // Centrar en la pantalla
            
            // Crear una instancia del panel VistaLogIn2
            VistaLogIn vistaLogIn = new VistaLogIn();
            
            // Añadir el panel al contenido de la ventana
            frame.getContentPane().add(vistaLogIn);
            
            // Hacer visible la ventana
            frame.setVisible(true);
        });
    }
}
