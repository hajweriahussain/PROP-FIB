package Presentacio;

import Domini.CtrlDomini;
import Exceptions.DominiException;
import Exceptions.PersistenciaException;

import java.util.*;
import javax.swing.JFrame;

public class CtrlPresentacio {
    private CtrlDomini ctrlDomini;
    private JFrame actFrame;

    public CtrlPresentacio() {
        this.ctrlDomini = CtrlDomini.getInstance();
    }
    
    // VISTES

    public void mostrarMenuInici() {
        if (actFrame != null) {
            actFrame.dispose();
        }
        actFrame = new VistaMenuInici();
        actFrame.setVisible(true);
    }

    public void mostrarMenuUsuari() {
        if (actFrame != null) {
            actFrame.dispose();
        }
        actFrame = new VistaMenuUsuari();
        actFrame.setVisible(true);
    }
    

    public Map<String, Map<String, String>> mostrarProductes() {
        return ctrlDomini.llistarProductesUsuari();
    }

    public Map<String, Map<String, String>> mostrarPrestatgeries() {
        return ctrlDomini.llistarPrestatgeriesUsuari();
    }
    
    // AUX
    
    private Map<Integer, Double> convertirStringAMap(String input) {
        Map<Integer, Double> map = new HashMap<>();

        String trimmed = input.substring(1, input.length() - 1);

        String[] pairs = trimmed.split(", ");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            Integer key = Integer.valueOf(keyValue[0]);
            Double value = Double.valueOf(keyValue[1]);

            map.put(key, value);
        }

        return map;
    }
    
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
      
    public void crearProducte(String idProd, String nomProd, String similituds) {
        Map<Integer, Double> similitudsMap;

        if (similituds == null || similituds.trim().isEmpty() || "{}".equals(similituds.trim())) {
            similitudsMap = new HashMap<>();
        }
        else {
            similitudsMap = convertirStringAMap(similituds);
        }

        ctrlDomini.crearProducte(Integer.parseInt(idProd), nomProd, similitudsMap);
    }

    public void crearProducteFitxer(String path) throws PersistenciaException {
        ctrlDomini.LlegirProducteFitxer(path);
    }
    
    public void esborrarProducte(String idProd) {
        ctrlDomini.esborrarProducte(Integer.parseInt(idProd));
    }
    
    public void editarIdProducte(String idActual, String nouId) {
        ctrlDomini.modificarProducte(Integer.valueOf(idActual), Integer.valueOf(nouId), "");
    }
    
    public void editarNomProducte(String idProd, String nouNom) {
        ctrlDomini.modificarProducte(Integer.valueOf(idProd), null ,nouNom);
    }
    
    public void modificarSimilitudProductes(String idProd1, String idProd2, String novaSimilitud, String bf) throws DominiException {
        ctrlDomini.modificarSimilituds(Integer.valueOf(idProd1), Integer.valueOf(idProd2), Double.parseDouble(novaSimilitud), Boolean.valueOf(bf));
    }
    
    public void modificarPosicioProductes(String idPres, String idProd1, String idProd2) throws DominiException {
        ctrlDomini.modificarPrestatgeria(Integer.parseInt(idPres), Integer.parseInt(idProd1), Integer.parseInt(idProd2));
    }

    public boolean existeixProducteId(String idProd, Map<String, Map<String, String>> productes) {
        return productes.containsKey(idProd);
    }
    
    // PRESTATGERIES

    public void crearPrestatgeria(String idPres, String nom, String numCols, String productes, String bf) throws DominiException {
        Set<Integer> productesSet = convertirStringASet(productes);
        ctrlDomini.crearPrestatgeria(Integer.parseInt(idPres), nom, Integer.parseInt(numCols), productesSet, Boolean.valueOf(bf));
    }

    public void crearPrestatgeriaFitxer(String nom, String idPres, String cols, String path) throws DominiException, PersistenciaException {
        ctrlDomini.LlegirPrestatgeriaFitxer(nom, idPres, cols, path);
    }
    
    public void esborrarPrestatgeria(String idPres) {
        ctrlDomini.esborrarPrestatgeria(Integer.parseInt(idPres));
    }
    
    public boolean existeixPrestatgeriaId(String idPres, Map<String, Map<String, String>> prestatgeries) {
        return prestatgeries.containsKey(idPres);
    }
    
    // USUARI

    public boolean validarLogin(String username, String pwd) throws PersistenciaException {
        return ctrlDomini.comprovarUsuari(username, pwd);
    }

    public void realizarLogin(String username, String pwd) throws DominiException, PersistenciaException {
        ctrlDomini.iniciarSessio(username, pwd);
    }

    public boolean existeixUsuari(String username) throws PersistenciaException {
        return ctrlDomini.existeixUsuari(username);
    }

    public String nomUsuariActual() {
        return ctrlDomini.getUsuariActual();
    }

    public void registrarUsuari(String username, String pwd) throws DominiException, PersistenciaException {
        ctrlDomini.crearUsuari(username, pwd);
    }

    public void esborrarUsuari() throws PersistenciaException {
        ctrlDomini.esborrarUsuari();
    }
    
    public void canviarContrasenya(String username, String pwd) throws PersistenciaException {
        ctrlDomini.canviarContrasenya(username, pwd);
    }

    public void logout() throws PersistenciaException {
        ctrlDomini.tancarSessio();
    }

    public static void main(String[] args) {
    }
}