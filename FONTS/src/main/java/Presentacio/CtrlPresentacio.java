package Presentacio;

import Domini.CtrlDomini;
import Domini.Producte;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;

    private VistaMenuInici vistaMenuInici;
    private VistaLogin vistaLogin;
    private VistaSignup vistaSignup;
    private VistaMenuUsuari vistaMenuUsuari;
    private VistaProducte vistaProducte;
    private VistaCrearProducte vistaCrearProducte;
    private VistaPrestatgeria vistaPrestatgeria;
    private VistaCrearPrestatgeria vistaCrearPrestatgeria;

    public CtrlPresentacio() {
        ctrlDomini = CtrlDomini.getInstance();
        vistaMenuInici = new VistaMenuInici(this);
        vistaLogin = new VistaLogin(this);
        vistaSignup = new VistaSignup(this);
        vistaMenuUsuari = new VistaMenuUsuari(this);
        vistaProducte = new VistaProducte(this);
        vistaCrearProducte = new VistaCrearProducte(this);
        vistaPrestatgeria = new VistaPrestatgeria(this);
        vistaCrearPrestatgeria = new VistaCrearPrestatgeria(this);
    }

    public void mostrarMenuInici() {
        new VistaMenuInici();
    }

    public static void mostrarLogin() {
        new VistaLogin();
    }

    public void mostrarSignup() {
        new VistaSignup();
    }

    public void mostrarMenuUsuari() {
        new VistaMenuUsuari();
    }

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
    
/*
    public Pair<Boolean,Boolean> validarLogin(String username, String pwd) {
        return ctrlDomini.comprovarUsuari(username, pwd); //
    }
 */

    public void realizarLogin(String username, String pwd)throws ExceptionFormat, ExceptionFormats {
        ctrlDomini.iniciarSessio(username, pwd);
    }

    public void logout() {
        ctrlDomini.tancarSessio();
    }

    public Producte[] mostrarProductes() {
        return ctrlDomini.llistarProductesUsuari();
    }

    public Producte[][] mostrarPrestatgeria(int idPres) {
        return ctrlDomini.llistarPrestatgeriaUsuari(idPres);
    }
  
    public void crearProducte(int idProd, String nomProd, Map<Integer, Double> sims, boolean bf) {
        ctrlDomini.crearProducte(idProd, nomProd, sims, bf);
    }

/*
    public void crearProducteFitxer(int idProd, String path) throws ExceptionFormats {
        ctrlDomini.llegirProducteFitxer(idProd, path);
    }
*/
    
    public void esborrarProducte(int idProd, boolean bf) {
        ctrlDomini.esborrarProducte(idProd, bf);
    }

/*
    public void crearPrestatgeria(int idPres, boolean bf) {
        ctrlDomini.crearPrestatgeria(idPres, bf);
    }
   

    public void crearPrestatgeriaFitxer(int idPres, String path) throws ExceptionFormats {
        ctrlDomini.llegirPrestatgeriaFitxer(idPres,path);
    }
*/
    
    public void esborrarPrestatgeria(int idPres) {
        ctrlDomini.esborrarPrestatgeria(idPres);
    }

/*
    public boolean existeixUsuari(String Username) {
        return ctrlDomini.existeixUsuari(Username);
    }
 */

    public String nom_usuari_actual() {
        return ctrlDomini.getUsuariActual();
    }

    public void registrarUsuari(String username, String pwd)throws ExceptionFormat, ExceptionFormats {
        ctrlDomini.crearUsuari(username, pwd);
    }

/*
    public void eliminarUsuari(String username) {
        ctrlDomini.eliminarUsuari(username);
    }
*/

    public static void main(String[] args) {
        CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
        ctrlPresentacio.mostrarMenuInici();
    }
}
