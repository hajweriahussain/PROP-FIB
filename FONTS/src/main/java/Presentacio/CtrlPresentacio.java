package Presentacio;

import Domini.CtrlDomini;
import Domini.Pair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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
    
    // PRODUCTES
    
    public void afegirProducteLocal(int id, String nom, Map<Integer, Double> similituds) {
        Map<String, String> infoProducte = new HashMap<>();
        infoProducte.put("id", String.valueOf(id));
        infoProducte.put("nom", nom);
        infoProducte.put("similituds", new Gson().toJson(similituds));
        infoProducte.put("posPrestatgeries", "{}");

        productes.put(String.valueOf(id), infoProducte);
    }

      
    public boolean crearProducte(int idProd, String nomProd, Map<Integer, Double> similituds) {
        if (!existeixProducteId(idProd)) {
            ctrlDomini.crearProducte(idProd, nomProd, similituds);
            afegirProducteLocal(idProd, nomProd, similituds);
            return true;
        }
        else {
            System.out.println("Error: Ja existeix un producte amb l'ID " + idProd);
            return false;
        }
    }

/*
    public void crearProducteFitxer(int idProd, String path) throws ExceptionFormats {
        ctrlDomini.llegirProducteFitxer(idProd, path);
    }
*/
    
    public void esborrarProducte(int idProd) {
        String idStr = String.valueOf(idProd);
        if (existeixProducteId(idProd)) {
            productes.remove(idStr);
            ctrlDomini.esborrarProducte(idProd);

            // Actualizar las referencias en las prestatgerías
            //actualitzarPrestatgeriesDespresEsborrarProducte(idProd);

            System.out.println("Producte amb ID " + idProd + " esborrat correctament.");
        }
        else {
            System.out.println("Error: No existeix un producte amb l'ID " + idProd);
        }
    }
    
    
    private Set<Integer> getProducteIds() {
        Set<Integer> ids = new HashSet<>();
        for (String idStr : productes.keySet()) {
            try {
                ids.add(Integer.valueOf(idStr));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido encontrado: " + idStr);
            }
        }
        return ids;
    }
    
    public String getNomProducte(int idProd) {
        if (existeixProducteId(idProd)) {
            return productes.get(String.valueOf(idProd)).get("nom");
        }
        return null;
    }
    
    public Map<Integer, Double> getSimilitudsProducte(int idProd) {
        if (existeixProducteId(idProd)) {
            String similitudsJson = productes.get(String.valueOf(idProd)).get("similituds");
            Gson gson = new Gson();
            Type type = new TypeToken<Map<Integer, Double>>(){}.getType();
            return gson.fromJson(similitudsJson, type);
        }
        return new HashMap<>();
    }
    
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeriesProducte(int idProd) {
        if (existeixProducteId(idProd)) {
            String posPrestatgeriesJson = productes.get(String.valueOf(idProd)).get("posPrestatgeries");
            Gson gson = new Gson();
            Type type = new TypeToken<Map<Integer, Pair<Integer, Integer>>>(){}.getType();
            return gson.fromJson(posPrestatgeriesJson, type);
        }
        return new HashMap<>();
    }
    
    public void editarIdProducte(int idActual, int nouId) {
        if (!existeixProducteId(nouId)) {
            Map<String, String> infoProducte = productes.remove(String.valueOf(idActual));
            if (infoProducte != null) {
                infoProducte.put("id", String.valueOf(nouId));
                productes.put(String.valueOf(nouId), infoProducte);
                ctrlDomini.modificarProducte(idActual, nouId, "");
            }
        }
        else {
            System.out.println("Error: Ja existeix un producte amb l'ID " + nouId);
        }
    }
    
    public void editarNomProducte(int idProd, String nouNom) {
        String idStr = String.valueOf(idProd);
        if (existeixProducteId(idProd)) {
            Map<String, String> infoProducte = productes.get(idStr);
            infoProducte.put("nom", nouNom);
            ctrlDomini.modificarProducte(idProd, null ,nouNom);
        }
        else {
            System.out.println("Error: No existeix un producte amb l'ID " + idProd);
        }
    }
    
    public void modificarSimilitudProductes(int idProd1, int idProd2, double novaSimilitud, boolean bf) {
        if (!existeixProducteId(idProd1) || !existeixProducteId(idProd2)) {
            System.out.println("Error: Un o ambdós productes no existeixen.");
            return;
        }

        ctrlDomini.modificarSimilituds(idProd1, idProd2, novaSimilitud, bf);

        actualitzarSimilitudLocal(idProd1, idProd2, novaSimilitud);
        actualitzarSimilitudLocal(idProd2, idProd1, novaSimilitud);
    }

    private void actualitzarSimilitudLocal(int idProd1, int idProd2, double novaSimilitud) {
        String idStr1 = String.valueOf(idProd1);
        if (existeixProducteId(idProd1)) {
            Map<String, String> infoProducte = productes.get(idStr1);
            String similitudsJson = infoProducte.get("similituds");
            Gson gson = new Gson();
            Type type = new TypeToken<Map<Integer, Double>>(){}.getType();
            Map<Integer, Double> similituds = gson.fromJson(similitudsJson, type);

            similituds.put(idProd2, novaSimilitud);
            infoProducte.put("similituds", gson.toJson(similituds));
        }
    }
    
    public void modificarPosicioProductes(int idPrestatgeria, int idProd1, int idProd2) {
        if (!existeixPrestatgeriaId(idPrestatgeria) || !existeixProducteId(idProd1) || !existeixProducteId(idProd2)) {
            System.out.println("Error: La prestatgeria o algun dels productes no existeix.");
            return;
        }

        Map<Integer, Pair<Integer, Integer>> posProd1 = getPosPrestatgeriesProducte(idProd1);
        Map<Integer, Pair<Integer, Integer>> posProd2 = getPosPrestatgeriesProducte(idProd2);

        if (!posProd1.containsKey(idPrestatgeria) || !posProd2.containsKey(idPrestatgeria)) {
            System.out.println("Error: Un o ambdós productes no estan a la prestatgeria especificada.");
            return;
        }

        Pair<Integer, Integer> tempPos = posProd1.get(idPrestatgeria);
        posProd1.put(idPrestatgeria, posProd2.get(idPrestatgeria));
        posProd2.put(idPrestatgeria, tempPos);

        // Actualizar en el dominio
        ctrlDomini.modificarPrestatgeria(idPrestatgeria, idProd1, idProd2);

        // Actualizar en la presentación
        actualitzarPosicioProducteLocal(idProd1, posProd1);
        actualitzarPosicioProducteLocal(idProd2, posProd2);
    }

    private void actualitzarPosicioProducteLocal(int idProd, Map<Integer, Pair<Integer, Integer>> novaPos) {
        String idStr = String.valueOf(idProd);
        if (productes.containsKey(idStr)) {
            Map<String, String> infoProducte = productes.get(idStr);
            Gson gson = new Gson();
            infoProducte.put("posPrestatgeries", gson.toJson(novaPos));
        }
    }


    public boolean existeixProducteId(int id) {
        return productes.containsKey(String.valueOf(id));
    }
    
    // PRESTATGERIES
    
    public void afegirPrestatgeriaLocal(int id, String nom, int numCols, Set<Integer> productes, boolean bf) {
        Map<String, String> infoPrestatgeria = new HashMap<>();
        infoPrestatgeria.put("id", String.valueOf(id));
        infoPrestatgeria.put("nom", nom);
        infoPrestatgeria.put("numCols", String.valueOf(numCols));
        infoPrestatgeria.put("productes", new Gson().toJson(productes));
        infoPrestatgeria.put("bruteForce", String.valueOf(bf));

        prestatgeries.put(String.valueOf(id), infoPrestatgeria);
    }

    public boolean crearPrestatgeria(int idPres, String nom, int numCols, Set<Integer> productes, boolean bf) {
        if (!existeixPrestatgeriaId(idPres)) {
            ctrlDomini.crearPrestatgeria(idPres, nom, numCols, productes, bf);
            afegirPrestatgeriaLocal(idPres, nom, numCols, productes, bf);
            return true;
        }
        else {
            System.out.println("Error: Ja existeix una prestatgeria amb l'ID " + idPres);
            return false;
        }
    }
   
/*
    public void crearPrestatgeriaFitxer(int idPres, String path) throws ExceptionFormats {
        ctrlDomini.llegirPrestatgeriaFitxer(idPres,path);
    }
*/
    
    public void esborrarPrestatgeria(int idPres) {
        ctrlDomini.esborrarPrestatgeria(idPres);
    }
    
    private Set<Integer> getPrestatgeriesIds() {
        Set<Integer> ids = new HashSet<>();
        for (String idStr : prestatgeries.keySet()) {
            try {
                ids.add(Integer.valueOf(idStr));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido encontrado: " + idStr);
            }
        }
        return ids;
    }
    
    public boolean existeixPrestatgeriaId(int id) {
        return prestatgeries.containsKey(String.valueOf(id));
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

    public String nomUsuariActual() {
        return ctrlDomini.getUsuariActual();
    }

    public void registrarUsuari(String username, String pwd) {
        ctrlDomini.crearUsuari(username, pwd);
    }

    public void esborrarUsuari() {
        ctrlDomini.esborrarUsuari();
    }
    
    public void canviarContrasenya(String username, String pwd) {
        ctrlDomini.canviarContrasenya(username, pwd);
    }

    public void logout() {
        ctrlDomini.tancarSessio();
    }

    public static void main(String[] args) {
        CtrlPresentacio ctrlPresentacio = new CtrlPresentacio();
    }
}