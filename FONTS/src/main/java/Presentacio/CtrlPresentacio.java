package Presentacio;

import Domini.CtrlDomini;
import java.util.*;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private VistaMenuInici vistaMenuInici;
    private Map<String, Map<String, String>> productes;
    private Map<String, Map<String, String>> prestatgeries;

    public CtrlPresentacio() {
        this.ctrlDomini = CtrlDomini.getInstance();
        this.vistaMenuInici = new VistaMenuInici();
        this.productes = new HashMap<>();
        this.prestatgeries = new HashMap<>();
        initialize();
    }
    
    private void initialize() {
        vistaMenuInici.setVisible(true);
    }
    
    // VISTES

    public void mostrarMenuInici() {
        vistaMenuInici.setVisible(true);
    }

    public void mostrarMenuUsuari() {
        VistaMenuUsuari vistaMenuUsuari = new VistaMenuUsuari();
        
        vistaMenuUsuari.setVisible(true);
        vistaMenuInici.dispose(); // Cerrar el JFrame de inicio
    }
    

    public void mostrarProductes() {
        productes = ctrlDomini.llistarProductesUsuari();
    }

    public void mostrarPrestatgeries() {
        prestatgeries = ctrlDomini.llistarPrestatgeriesUsuari();
    }
    
    /* Eliminar??
    public void mostrarProducteIndividual(int idProd) {
        new VistaProducte(idProd);
    }
    
    public void mostrarPrestatgeriaIndividual(int idPres) {
        new VistaPrestatgeria(idPres);
    }
    
    public void mostrarCrearProducte() {
        new VistaCrearProducte();
    }
    
    public void mostrarCrearPrestatgeria() {
        new VistaCrearPrestatgeria();
    }
    */
    
    // PRODUCTES
      
    public void crearProducte(int idProd, String nomProd, Map<Integer, Double> sims) {
        ctrlDomini.crearProducte(idProd, nomProd, sims);
    }

/*
    public void crearProducteFitxer(int idProd, String path) throws ExceptionFormats {
        ctrlDomini.llegirProducteFitxer(idProd, path);
    }
*/
    
    public void esborrarProducte(int idProd) {
        ctrlDomini.esborrarProducte(idProd);
    }
    
    // PRESTATGERIES

    public void crearPrestatgeria(int idPres, String nom, int numCols, Set<Integer> productes, boolean bf) {
        ctrlDomini.crearPrestatgeria(idPres, nom, numCols, productes, bf);
    }
   
/*
    public void crearPrestatgeriaFitxer(int idPres, String path) throws ExceptionFormats {
        ctrlDomini.llegirPrestatgeriaFitxer(idPres,path);
    }
*/
    
    public void esborrarPrestatgeria(int idPres) {
        ctrlDomini.esborrarPrestatgeria(idPres);
    }
    
    // USUARI

    public Boolean validarLogin(String username, String pwd) {
        return ctrlDomini.comprovarUsuari(username, pwd); //
    }

    public void realizarLogin(String username, String pwd) {
        ctrlDomini.iniciarSessio(username, pwd);
    }

    public boolean existeixUsuari(String username) {
        return ctrlDomini.existeixUsuari(username);
    }

    public String nom_usuari_actual() {
        return ctrlDomini.getUsuariActual();
    }

    public void registrarUsuari(String username, String pwd) {
        ctrlDomini.crearUsuari(username, pwd);
    }

    public void esborrarUsuari() {
        ctrlDomini.esborrarUsuari();
    }

    public void logout() {
        ctrlDomini.tancarSessio();
    }

    public static void main(String[] args) {
        new CtrlPresentacio();
    }
}
