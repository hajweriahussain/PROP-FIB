package Persistencia;

import java.util.List;

/**
 * Controlador de persistència que gestiona les operacions relacionades amb els usuaris, els productes 
 * i les prestatgeries.
 * Aquesta classe fa servir els gestors específics per a usuaris, productes i prestatgeries per realitzar
 * accions com crear, eliminar, modificar o importar usuaris i dades relacionades.
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
     */
    public boolean existeixUsuari(String user) {
        return GestorUsuaris.existeixUsuari(user);
    }
    
    /**
     * Afegeix un nou usuari amb el nom d'usuari i la contrasenya especificats.
     * 
     * @param user El nom d'usuari a afegir.
     * @param password La contrasenya per a l'usuari.
     */
    public void afegirUsuari(String user, String password){
        GestorUsuaris.afegirUsuari(user,password);
    }
    
    /**
     * Elimina un usuari del sistema. Aquesta operació també elimina les prestatgeries i els productes associats a l'usuari.
     * 
     * @param user El nom d'usuari a eliminar.
     */
    public void eliminarUsuari(String user){ 
        GestorUsuaris.eliminarUsuari(user);
        GestorCjtProductes.esborrarProductes(user);
        GestorCjtPrestatgeries.esborrarPrestatgeriesUsuari(user);
    }

    /**
     * Canvia la contrasenya d'un usuari.
     * 
     * @param user El nom d'usuari per al qual es vol canviar la contrasenya.
     * @param novaPassword La nova contrasenya.
     */
    public void canviarContrasenya(String user, String novaPassword){
        GestorUsuaris.canviarContrasenya(user,novaPassword);
    }
    
    /**
     * Verifica si la contrasenya proporcionada és correcta per a un usuari determinat.
     * 
     * @param user El nom d'usuari.
     * @param password La contrasenya per verificar.
     * @return true si la contrasenya és correcta, false en cas contrari.
     */
    public boolean verificarContrasenya(String user, String password){
        return GestorUsuaris.verificarContrasenya(user,password);
    }
    
    /**
     * Importa les prestatgeries associades a un usuari.
     * 
     * @param usuari El nom d'usuari per al qual es volen importar les prestatgeries.
     * @return Una llista amb les prestatgeries de l'usuari.
     */
    public List<String> importarPrestatgeries(String usuari){
        return GestorCjtPrestatgeries.importarPrestatgeries(usuari);
    }
    
    /**
     * Desa una llista de prestatgeries per a un usuari.
     * 
     * @param prestatgeries La llista de prestatgeries a desar.
     * @param usuari El nom de l'usuari per al qual es volen desar les prestatgeries.
     */
    public void guardarPrestatgeries(List<String> prestatgeries, String usuari) {
        GestorCjtPrestatgeries.guardarPrestatgeries(prestatgeries,usuari);
    }

    /**
     * Importa un fitxer que conté una llista de prestatgeries.
     * 
     * @param path La ruta al fitxer que conté les prestatgeries.
     * @return Una llista amb les prestatgeries importades des del fitxer.
     */
    public List<String> importarFitxerPrestatgeria(String path) {
        return GestorCjtPrestatgeries.importarFitxerPrestatgeria(path);
    }
    
    /**
     * Importa els productes associats a un usuari.
     * 
     * @param usuari El nom d'usuari per al qual es volen importar els productes.
     * @return Una llista amb els productes de l'usuari.
     */
    public List<String> importarProductes(String usuari){
        return GestorCjtProductes.importarProductes(usuari);
    }
    
    /**
     * Desa una llista de productes per a un usuari.
     * 
     * @param productes La llista de productes a desar.
     * @param usuari El nom de l'usuari per al qual es volen desar els productes.
     */
    public void guardarProductes(List<String> productes, String usuari){
        GestorCjtProductes.guardarProductes(productes, usuari);
    }
    
    /**
     * Importa un fitxer que conté una llista de productes.
     * 
     * @param path La ruta al fitxer que conté els productes.
     * @return Una llista amb els productes importats des del fitxer.
     */
    public List<String> importarFitxerProducte(String path) {
        return GestorCjtProductes.importarFitxerProducte(path);
    }
}
