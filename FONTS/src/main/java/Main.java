
import Presentacio.CtrlPresentacio;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import javax.swing.UIManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hajweriahussain
 */
public class Main {
	public static void main(String[] args) {
        	System.out.println("Hola, mundo!");
        	CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
                 FlatLightFlatIJTheme.setup();
                UIManager.put( "Button.arc", 20 );
                UIManager.put( "Component.arc", 20 );
                UIManager.put( "CheckBox.arc", 2 );
                UIManager.put( "TextComponent.arc", 20 );
                UIManager.put( "Component.arrowType", "triangle" );

            // Llama a un método del controlador (reemplaza 'iniciarAplicacion' por el método adecuado)
            ctrlPresentacio.mostrarMenuInici();
	
        }
   
    
}
