package Presentacio;

import Domini.CtrlDomini;
import Exceptions.DominiException;
import Exceptions.PersistenciaException;

import java.util.*;
import javax.swing.JFrame;

/**
 * Controlador de la capa de presentació.
 * Gestiona la interacció entre la interfície d'usuari i el controlador de domini.
 */
public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private JFrame actFrame;

    /**
     * Constructor del controlador de presentació.
     * Inicialitza el controlador de domini.
     */
    public CtrlPresentacio() {
        this.ctrlDomini = CtrlDomini.getInstance();
    }
    
    // VISTES

    /**
     * Mostra el menú d'inici de l'aplicació.
     */
    public void mostrarMenuInici() {
        if (actFrame != null) {
            actFrame.dispose();
        }
        actFrame = new VistaMenuInici();
        actFrame.setVisible(true);
    }

    /**
     * Mostra el menú principal de l'usuari.
     */
    public void mostrarMenuUsuari() {
        if (actFrame != null) {
            actFrame.dispose();
        }
        actFrame = new VistaMenuUsuari();
        actFrame.setVisible(true);
    }
    

    /**
     * Obté la llista de productes de l'usuari actual.
     * @return Mapa amb la informació dels productes.
     * @throws DominiException Si hi ha un error en obtenir els productes.
     */
    public Map<String, Map<String, String>> mostrarProductes() throws DominiException {
        return ctrlDomini.llistarProductesUsuari();
    }

    /**
     * Obté la llista de prestatgeries de l'usuari actual.
     * @return Mapa amb la informació de les prestatgeries.
     */
    public Map<String, Map<String, String>> mostrarPrestatgeries() {
        return ctrlDomini.llistarPrestatgeriesUsuari();
    }
    
    // AUX
    
    /**
     * Converteix una cadena de text en un mapa de similituds.
     * @param input Cadena de text amb les similituds.
     * @return Mapa de similituds.
     */
    private Map<Integer, Double> convertirStringAMap(String input) {
        Map<Integer, Double> similitudMap = new HashMap<>();
        String[] pairs = input.split("\n");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                int key = Integer.parseInt(keyValue[0].trim());
                double value = Double.parseDouble(keyValue[1].trim());
                similitudMap.put(key, value);
            }
        }
        return similitudMap;
    }
    
    /**
     * Converteix una cadena de text en un conjunt d'enters.
     * @param input Cadena de text amb els valors.
     * @return Conjunt d'enters.
     */
    private Set<Integer> convertirStringASet(String input) {
        Set<Integer> set = new HashSet<>();

        String trimmed = input.substring(1, input.length() - 1);

        String[] values = trimmed.split(", ");

        for (String value : values) {
            set.add(Integer.valueOf(value));
        }

        return set;
    }
    
    // PRODUCTES
      
    /**
     * Crea un nou producte.
     * @param idProd Identificador del producte.
     * @param nomProd Nom del producte.
     * @param similituds Similituds del producte.
     * @throws DominiException Si hi ha un error en crear el producte.
     */
    public void crearProducte(String idProd, String nomProd, String similituds) throws DominiException {
        Map<Integer, Double> similitudsMap;

        if (similituds == null || similituds.trim().isEmpty() || "{}".equals(similituds.trim())) {
            similitudsMap = new HashMap<>();
        }
        else {
            similitudsMap = convertirStringAMap(similituds);
        }

        ctrlDomini.crearProducte(Integer.parseInt(idProd), nomProd, similitudsMap);
    }

    /**
     * Crea un producte a partir d'un fitxer.
     * @param path Ruta del fitxer.
     * @throws PersistenciaException Si hi ha un error en llegir el fitxer.
     * @throws DominiException Si hi ha un error en crear el producte.
     */
    public void crearProducteFitxer(String path) throws PersistenciaException, DominiException {
        ctrlDomini.LlegirProducteFitxer(path);
    }
    
    /**
     * Esborra un producte.
     * @param idProd Identificador del producte a esborrar.
     * @throws DominiException Si hi ha un error en esborrar el producte.
     */
    public void esborrarProducte(String idProd) throws DominiException {
        ctrlDomini.esborrarProducte(Integer.parseInt(idProd));
    }
    
    /**
     * Edita l'identificador d'un producte.
     * @param idActual Identificador actual del producte.
     * @param nouId Nou identificador del producte.
     * @throws DominiException Si hi ha un error en editar l'identificador.
     */
    public void editarIdProducte(String idActual, String nouId) throws DominiException {
        ctrlDomini.modificarProducte(Integer.valueOf(idActual), Integer.valueOf(nouId), "-");
    }
    
    /**
     * Edita el nom d'un producte.
     * @param idProd Identificador del producte.
     * @param nouNom Nou nom del producte.
     * @throws DominiException Si hi ha un error en editar el nom.
     */
    public void editarNomProducte(String idProd, String nouNom) throws DominiException {
        ctrlDomini.modificarProducte(Integer.valueOf(idProd), null ,nouNom);
    }
    
    /**
     * Modifica la similitud entre dos productes.
     * @param idProd1 Identificador del primer producte.
     * @param idProd2 Identificador del segon producte.
     * @param novaSimilitud Nova similitud entre els productes.
     * @param bf Indica si s'utilitza força bruta (true) o no (false).
     * @throws DominiException Si hi ha un error en modificar la similitud.
     */
    public void modificarSimilitudProductes(String idProd1, String idProd2, String novaSimilitud, String bf) throws DominiException {
        ctrlDomini.modificarSimilituds(Integer.valueOf(idProd1), Integer.valueOf(idProd2), Double.parseDouble(novaSimilitud), Boolean.valueOf(bf));
    }
    
    /**
     * Modifica la posició de dos productes en una prestatgeria.
     * @param idPres Identificador de la prestatgeria.
     * @param idProd1 Identificador del primer producte.
     * @param idProd2 Identificador del segon producte.
     * @throws DominiException Si hi ha un error en modificar les posicions.
     */
    public void modificarPosicioProductes(String idPres, String idProd1, String idProd2) throws DominiException {
        ctrlDomini.modificarPrestatgeria(Integer.parseInt(idPres), Integer.parseInt(idProd1), Integer.parseInt(idProd2));
    }

    /**
     * Comprova si existeix un producte amb l'identificador donat.
     * @param idProd Identificador del producte.
     * @param productes Mapa de productes.
     * @return true si el producte existeix, false altrament.
     */
    public boolean existeixProducteId(String idProd, Map<String, Map<String, String>> productes) {
        return productes.containsKey(idProd);
    }
    
    // PRESTATGERIES

    /**
     * Crea una nova prestatgeria.
     * @param idPres Identificador de la prestatgeria.
     * @param nom Nom de la prestatgeria.
     * @param numCols Nombre de columnes.
     * @param productes Conjunt de productes.
     * @param bf Indica si s'utilitza força bruta (true) o no (false).
     * @throws DominiException Si hi ha un error en crear la prestatgeria.
     */
    public void crearPrestatgeria(String idPres, String nom, String numCols, String productes, String bf) throws DominiException {
        Set<Integer> productesSet = convertirStringASet(productes);
        ctrlDomini.crearPrestatgeria(Integer.parseInt(idPres), nom, Integer.parseInt(numCols), productesSet, Boolean.valueOf(bf));
    }

    /**
     * Crea una prestatgeria a partir d'un fitxer.
     * @param path Ruta del fitxer.
     * @throws DominiException Si hi ha un error en crear la prestatgeria.
     * @throws PersistenciaException Si hi ha un error en llegir el fitxer.
     */
    public void crearPrestatgeriaFitxer(String path) throws DominiException, PersistenciaException {
        ctrlDomini.LlegirPrestatgeriaFitxer(path);
    }
    
    /**
     * Esborra una prestatgeria.
     * @param idPres Identificador de la prestatgeria a esborrar.
     */
    public void esborrarPrestatgeria(String idPres) throws DominiException{
        ctrlDomini.esborrarPrestatgeria(Integer.parseInt(idPres));
    }
    
    /**
     * Comprova si existeix una prestatgeria amb l'identificador donat.
     * @param idPres Identificador de la prestatgeria.
     * @param prestatgeries Mapa de prestatgeries.
     * @return true si la prestatgeria existeix, false altrament.
     */
    public boolean existeixPrestatgeriaId(String idPres, Map<String, Map<String, String>> prestatgeries) {
        return prestatgeries.containsKey(idPres);
    }
    
    // USUARI

    /**
     * Valida les credencials d'un usuari.
     * @param username Nom d'usuari.
     * @param pwd Contrasenya.
     * @return true si les credencials són vàlides, false altrament.
     * @throws PersistenciaException Si hi ha un error en la validació.
     */
    public boolean validarLogin(String username, String pwd) throws PersistenciaException {
        return ctrlDomini.comprovarUsuari(username, pwd);
    }

    /**
     * Realitza el login d'un usuari.
     * @param username Nom d'usuari.
     * @param pwd Contrasenya.
     * @throws DominiException Si hi ha un error en el procés de login.
     * @throws PersistenciaException Si hi ha un error de persistència.
     */
    public void realizarLogin(String username, String pwd) throws DominiException, PersistenciaException {
        try{
            ctrlDomini.iniciarSessio(username, pwd);
        } catch(DominiException e){
            throw new DominiException("Error al carregar la estanteria en presentacion " + e.getMessage());
        }
    }

    /**
     * Comprova si existeix un usuari amb el nom donat.
     * @param username Nom d'usuari.
     * @return true si l'usuari existeix, false altrament.
     * @throws PersistenciaException Si hi ha un error en la comprovació.
     */
    public boolean existeixUsuari(String username) throws PersistenciaException {
        return ctrlDomini.existeixUsuari(username);
    }

    /**
     * Obté el nom de l'usuari actual.
     * @return Nom de l'usuari actual.
     */
    public String nomUsuariActual() {
        return ctrlDomini.getUsuariActual();
    }

    /**
     * Registra un nou usuari.
     * @param username Nom d'usuari.
     * @param pwd Contrasenya.
     * @throws DominiException Si hi ha un error en el registre.
     * @throws PersistenciaException Si hi ha un error de persistència.
     */
    public void registrarUsuari(String username, String pwd) throws DominiException, PersistenciaException {
        ctrlDomini.crearUsuari(username, pwd);
    }

    /**
     * Esborra l'usuari actual.
     * @throws PersistenciaException Si hi ha un error de persistència.
     * @throws DominiException Si hi ha un error en esborrar l'usuari.
     */
    public void esborrarUsuari() throws PersistenciaException, DominiException {
        ctrlDomini.esborrarUsuari();
    }
    
    /**
     * Canvia la contrasenya d'un usuari.
     * @param username Nom d'usuari.
     * @param pwd Nova contrasenya.
     * @throws PersistenciaException Si hi ha un error en canviar la contrasenya.
     */
    public void canviarContrasenya(String username, String pwd) throws PersistenciaException {
        ctrlDomini.canviarContrasenya(username, pwd);
    }

    /**
     * Tanca la sessió de l'usuari actual.
     * @throws PersistenciaException Si hi ha un error de persistència.
     * @throws DominiException Si hi ha un error en tancar la sessió.
     */
    public void logout() throws PersistenciaException, DominiException {
        try{
            ctrlDomini.tancarSessio();
        }catch(DominiException e){
            throw new DominiException("Error al guardar la estanteria presen " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    }
}