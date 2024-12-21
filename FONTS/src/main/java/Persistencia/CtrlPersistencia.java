package Persistencia;

import java.util.List;
import Exceptions.ExceptionFormat;

/**
 * Controlador de persistència que gestiona les operacions relacionades amb els usuaris, els productes 
 * i les prestatgeries.
 * Aquesta classe fa servir els gestors específics per a usuaris, productes i prestatgeries per realitzar
 * accions com crear, eliminar, modificar o importar usuaris i dades relacionades.
 * 
 * Els mètodes d'aquesta classe poden llançar excepcions del tipus {@link ExceptionFormat} si hi ha un error
 * de format o altres problemes durant la manipulació de les dades.
 */
public class CtrlPersistencia {

    /**
     * Constructor de la classe CtrlPersistencia.
     * Inicialitza el controlador sense realitzar cap operació addicional.
     */
    public CtrlPersistencia() {

    }
    
    /**
     * Verifica si un usuari existeix en el sistema.
     * 
     * @param user El nom d'usuari a verificar.
     * @return true si l'usuari existeix, false en cas contrari.
     * @throws ExceptionFormat Si hi ha un error amb el format de l'usuari.
     */
    public boolean existeixUsuari(String user) throws ExceptionFormat{
        return GestorUsuaris.existeixUsuari(user);
    }
    
    /**
     * Afegeix un nou usuari amb el nom d'usuari i la contrasenya especificats.
     * 
     * @param user El nom d'usuari a afegir.
     * @param password La contrasenya per a l'usuari.
     * @throws ExceptionFormat Si hi ha un error amb el format dels paràmetres.
     */
    public void afegirUsuari(String user, String password) throws ExceptionFormat{
        GestorUsuaris.afegirUsuari(user,password);
    }
    
    /**
     * Elimina un usuari del sistema. Aquesta operació també elimina les prestatgeries i els productes associats a l'usuari.
     * 
     * @param user El nom d'usuari a eliminar.
     * @throws ExceptionFormat Si hi ha un error amb el format de l'usuari.
     */
    public void eliminarUsuari(String user) throws ExceptionFormat{ 
        GestorUsuaris.eliminarUsuari(user);
        GestorCjtProductes.esborrarProductes(user);
        GestorCjtPrestatgeries.esborrarPrestatgeriesUsuari(user);
    }

    /**
     * Canvia la contrasenya d'un usuari.
     * 
     * @param user El nom d'usuari per al qual es vol canviar la contrasenya.
     * @param novaPassword La nova contrasenya.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public void canviarContrasenya(String user, String novaPassword) throws ExceptionFormat{
        GestorUsuaris.canviarContrasenya(user,novaPassword);
    }
    
    /**
     * Verifica si la contrasenya proporcionada és correcta per a un usuari determinat.
     * 
     * @param user El nom d'usuari.
     * @param password La contrasenya per verificar.
     * @return true si la contrasenya és correcta, false en cas contrari.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public boolean verificarContrasenya(String user, String password) throws ExceptionFormat{
        return GestorUsuaris.verificarContrasenya(user,password);
    }
    
    /**
     * Importa les prestatgeries associades a un usuari.
     * 
     * @param usuari El nom d'usuari per al qual es volen importar les prestatgeries.
     * @return Una llista amb les prestatgeries de l'usuari.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public List<String> importarPrestatgeries(String usuari) throws ExceptionFormat{
        return GestorCjtPrestatgeries.importarPrestatgeries(usuari);
    }
    
    /**
     * Desa una llista de prestatgeries per a un usuari.
     * 
     * @param prestatgeries La llista de prestatgeries a desar.
     * @param usuari El nom de l'usuari per al qual es volen desar les prestatgeries.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public void guardarPrestatgeries(List<String> prestatgeries, String usuari) throws ExceptionFormat{
        GestorCjtPrestatgeries.guardarPrestatgeries(prestatgeries,usuari);
    }

    /**
     * Importa un fitxer que conté una llista de prestatgeries.
     * 
     * @param path La ruta al fitxer que conté les prestatgeries.
     * @return Una llista amb les prestatgeries importades des del fitxer.
     * @throws ExceptionFormat Si hi ha un error amb el format del fitxer.
     */
    public List<String> importarFitxerPrestatgeria(String path) throws ExceptionFormat{
        return GestorCjtPrestatgeries.importarFitxerPrestatgeria(path);
    }
    
    /**
     * Importa els productes associats a un usuari.
     * 
     * @param usuari El nom d'usuari per al qual es volen importar els productes.
     * @return Una llista amb els productes de l'usuari.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public List<String> importarProductes(String usuari) throws ExceptionFormat{
        return GestorCjtProductes.importarProductes(usuari);
    }
    
    /**
     * Desa una llista de productes per a un usuari.
     * 
     * @param productes La llista de productes a desar.
     * @param usuari El nom de l'usuari per al qual es volen desar els productes.
     * @throws ExceptionFormat Si hi ha un error amb el format de les dades.
     */
    public void guardarProductes(List<String> productes, String usuari) throws ExceptionFormat{
        GestorCjtProductes.guardarProductes(productes, usuari);
    }
    
    /**
     * Importa un fitxer que conté una llista de productes.
     * 
     * @param path La ruta al fitxer que conté els productes.
     * @return Una llista amb els productes importats des del fitxer.
     * @throws ExceptionFormat Si hi ha un error amb el format del fitxer.
     */
    public List<String> importarFitxerProducte(String path) throws ExceptionFormat{
        return GestorCjtProductes.importarFitxerProducte(path);
    }
}
