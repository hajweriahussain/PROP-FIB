package Domini;

import Exceptions.DominiException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import java.util.Set;

/**
 * Classe que representa un conjunt de productes gestionats per un usuari.
 */
public class CjtProductes {

    /** Nom de l'usuari que gestiona el conjunt de productes. */
    private String usuari;

    /** Mapa d'identificadors únics de producte a les seves instàncies corresponents. */
    private Map<Integer, Producte> productes;

    /**
     * Constructor de la classe CjtProductes.
     * @param usuari Nom de l'usuari gestor del conjunt de productes.
     */
    public CjtProductes(String usuari) {
        this.usuari = usuari;
        this.productes = new HashMap<>();
    }

    /**
     * Retorna el mapa de productes si el nom d'usuari és vàlid.
     * @param nomUsuari Nom de l'usuari que sol·licita el mapa.
     * @return Mapa de productes o null si l'usuari no és vàlid.
     */
    public Map<Integer, Producte> getProductes(String nomUsuari) throws DominiException {
        if (nomUsuari != null && usuari.equals(nomUsuari)) {
            return productes;
        } else {
            throw new DominiException("Usuari no vàlid: " + nomUsuari);
        }
    }

    /**
     * Obté un producte específic pel seu identificador.
     * @param idProd Identificador del producte.
     * @return El producte corresponent o null si no existeix.
     */
    public Producte getProducte(int idProd) throws DominiException {
        Producte producte = productes.get(idProd);
        if (producte == null) {
            throw new DominiException("No existeix un producte amb l'ID: " + idProd);
        }
        return producte;
    }

    /**
     * Obté la posició d'un producte en una prestatgeria específica.
     * @param idProd Identificador del producte.
     * @param idPres Identificador de la prestatgeria.
     * @return La posició del producte a la prestatgeria o null si no existeix.
     */
    public Pair<Integer, Integer> getPosProducte(int idProd, int idPres) throws DominiException {
        Producte prod = getProducte(idProd);
        Pair<Integer, Integer> posicio = prod.getPosPrestatgeria(idPres);
        if (posicio == null) {
            throw new DominiException("No hi ha posició per al producte amb ID " + idProd + " a la prestatgeria " + idPres);
        }
        return posicio;
    }

    /**
     * Defineix el mapa de productes.
     * @param prods Mapa de productes a assignar.
     */
    public void setMapProductes(Map<Integer, Producte> prods) {
        this.productes = new HashMap<>(prods);
    }

    /**
     * Obté les posicions de totes les prestatgeries associades a un producte.
     * @param idProd Identificador del producte.
     * @return Mapa de posicions per prestatgeria del producte.
     */
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeriesProducte(int idProd) throws DominiException {
        Producte prod = getProducte(idProd);
        return prod.getPosPrestatgeries();
    }

    /**
     * Converteix els productes en un array.
     * @return Array de productes del conjunt.
     */
    public Producte[] getVecProductes() {
        List<Producte> producteList = new ArrayList<>(productes.values());
        return producteList.toArray(Producte[]::new);
    }

    /**
     * Comprova si existeix un producte donat el seu identificador.
     * @param idProd Identificador del producte.
     * @return Cert si el producte existeix, fals altrament.
     */
    public boolean existeixProducte(int idProd) {
        return productes.containsKey(idProd);
    }
    
    public void comprovarSims(Map<Integer, Double> mapSims) throws DominiException {
        for (Integer id : productes.keySet()) {
            if (!mapSims.containsKey(id)) {
                throw new DominiException("Error: el map de similituds no conté la similitud amb l'ID: " + id);
            }
        }
    }

    /**
     * Afegeix un nou producte al conjunt.
     * @param id Identificador del producte.
     * @param nom Nom del producte.
     * @param similituds Mapa de similituds del producte amb altres productes.
     */
    public void afegirProducte(int id, String nom, Map<Integer, Double> similituds) throws DominiException {
        Producte p = new Producte(id, nom, similituds);
        productes.put(p.getId(), p);

        if (similituds != null && !similituds.isEmpty()) {
            for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
                int prodVecId = entry.getKey();
                double similitud = entry.getValue();

                Producte prodVec = getProducte(prodVecId);
                prodVec.afegirSimilitud(p.getId(), similitud);
            }
        }
    }

    /**
     * Edita un producte existent.
     * @param p Instància del producte amb les noves dades.
     */
    public void editarProducte(Producte p) throws DominiException {
        Producte prod = getProducte(p.getId());
        prod.setNom(p.getNom());
    }

    /**
     * Modifica l'identificador d'un producte.
     * @param idProd Identificador actual del producte.
     * @param nouIdProd Nou identificador del producte.
     */
    public void editarIdProducte(int idProd, int nouIdProd) throws DominiException {
        Producte prod = getProducte(idProd);

        for (Producte prod2 : productes.values()) {
            if (prod2.getSimilituds().containsKey(idProd)) {
                double similitud = prod2.getSimilitud(idProd);
                prod2.afegirSimilitud(nouIdProd, similitud);
                prod2.getSimilituds().remove(idProd);
            }
        }

        prod.setId(nouIdProd);
        productes.remove(idProd);
        productes.put(nouIdProd, prod);
    }

    /**
     * Modifica el nom d'un producte.
     * @param idProd Identificador del producte.
     * @param nouNom Nou nom a assignar al producte.
     */
    public void editarNomProducte(int idProd, String nouNom) throws DominiException {
        Producte prod = getProducte(idProd);
        prod.setNom(nouNom);
    }

    /**
     * Modifica la posició d'un producte en una prestatgeria.
     * @param idProd Identificador del producte.
     * @param idPres Identificador de la prestatgeria.
     * @param novaPos Nova posició a assignar.
     */
    public void editarPosProducte(int idProd, int idPres, Pair<Integer, Integer> novaPos) throws DominiException {
        Producte prod = getProducte(idProd);
        prod.modificarPosPrestatgeria(idPres, novaPos);
    }

    /**
     * Elimina un producte del conjunt.
     * @param idProd Identificador del producte a eliminar.
     */
    public void eliminarProducte(int idProd) {
        productes.remove(idProd);
        for (Producte prod2 : productes.values()) {
            if (prod2.getSimilituds().containsKey(idProd)) {
                prod2.getSimilituds().remove(idProd);
            }
        }
    }

    /**
     * Modifica la similitud entre dos productes especificats pels seus identificadors.
     * 
     * @param idProd1 Identificador del primer producte.
     * @param idProd2 Identificador del segon producte.
     * @param novaSimilitud Nou valor de la similitud entre els productes.
     */
    public void modificarSimilitud(int idProd1, int idProd2, double novaSimilitud) throws DominiException {
        Producte prod1 = getProducte(idProd1);
        Producte prod2 = getProducte(idProd2);

        prod1.modificarSimilitud(idProd2, novaSimilitud);
        prod2.modificarSimilitud(idProd1, novaSimilitud);
    }

    /**
     * Retorna les similituds associades a un producte donat el seu identificador.
     * 
     * @param idProd Identificador del producte.
     * @return Mapa de similituds amb altres productes o null si el producte no existeix.
     */
    public Map<Integer, Double> getSimilituds(int idProd) throws DominiException {
        Producte prod = getProducte(idProd);
        return prod.getSimilituds();
    }

    /**
     * Genera una matriu de similituds per a tots els productes gestionats.
     * 
     * @return Matriu de similituds on cada element [i][j] representa la similitud entre dos productes.
     */
    public double[][] getMatriuSimilituds() {
        int n = productes.size();
        double[][] mat = new double[n][n];

        int idx = 0;
        for (Producte prod : productes.values()) {
            int colx = 0;
            for (Producte prod2 : productes.values()) {
                if (prod.getId() == prod2.getId()) {
                    mat[idx][idx] = 0.0;
                } else {
                    mat[idx][colx] = prod.getSimilitud(prod2.getId());
                }
                ++colx;
            }
            ++idx;
        }
        return mat;
    }

    /**
     * Genera una matriu de similituds per a un conjunt específic de productes identificats pels seus IDs.
     * 
     * @param idsProds Array d'identificadors dels productes per incloure a la matriu.
     * @return Matriu de similituds corresponent als productes especificats.
     */
    public double[][] getMatriuSimilitudsPerIds(int[] idsProds) throws DominiException {
        int n = idsProds.length;
        double[][] mat = new double[n][n];

        for (int i = 0; i < n; i++) {
            Producte prod1 = getProducte(idsProds[i]);
            for (int j = 0; j < n; j++) {
                Producte prod2 = getProducte(idsProds[j]);
                if (prod1.getId() == prod2.getId()) {
                    mat[i][j] = 0.0;
                } else {
                    mat[i][j] = prod1.getSimilitud(prod2.getId());
                }
            }
        }
        return mat;
    }

    /**
     * Converteix un array bidimensional d'IDs de productes en una matriu d'objectes Producte.
     * 
     * @param idsProds Matriu d'IDs dels productes.
     * @return Matriu d'objectes Producte corresponents o null si els IDs són nuls.
     */
    public Producte[][] getMatProductes(Integer[][] idsProds) throws DominiException {
        if (idsProds == null) return null;

        int files = idsProds.length;
        Producte[][] matProds = new Producte[files][];

        for (int i = 0; i < files; i++) {
            if (idsProds[i] == null) {
                matProds[i] = null;
                continue;
            }

            int columnes = idsProds[i].length;
            matProds[i] = new Producte[columnes];

            for (int j = 0; j < columnes; j++) {
                if (idsProds[i][j] != null) {
                    matProds[i][j] = getProducte(idsProds[i][j]);
                } else {
                    matProds[i][j] = null;
                }
            }
        }

        return matProds;
    }

    /**
     * Converteix una llista de JSONs de productes en un mapa d'objectes Producte.
     * 
     * @param producteJsonList Llista de cadenes JSON que representen productes.
     * @return Mapa d'identificadors de productes als seus objectes Producte corresponents.
     */
    public Map<Integer, Producte> listToProductes(List<String> producteJsonList) throws DominiException {
        Gson gson = new Gson();
        Map<Integer, Producte> productesMap = new HashMap<>();

        for (String jsonProducte : producteJsonList) {
            try {
                Map<String, Object> producteData = gson.fromJson(jsonProducte, Map.class);

                int id = Integer.parseInt((String) producteData.get("id"));
                String nom = (String) producteData.get("nom");

                Map<Integer, Double> similituds = new HashMap<>();
                Map<String, Double> similitudsRaw = (Map<String, Double>) producteData.get("similituds");
                if (similitudsRaw != null) {
                    for (Map.Entry<String, Double> entry : similitudsRaw.entrySet()) {
                        Integer key = Integer.parseInt(entry.getKey());
                        similituds.put(key, entry.getValue());
                    }
                }


                Map<Integer, Pair<Integer, Integer>> prestatgeriesPos = new HashMap<>();
                Map<String, List<Double>> posPrestatgeriesRaw = (Map<String, List<Double>>) producteData.get("posPrestatgeries");
                if (posPrestatgeriesRaw != null) {
                    for (Map.Entry<String, List<Double>> entry : posPrestatgeriesRaw.entrySet()) {
                        Integer key = Integer.parseInt(entry.getKey());
                        List<Double> value = entry.getValue();
                        if (value.size() == 2) {
                            prestatgeriesPos.put(key, new Pair<>(value.get(0).intValue(), value.get(1).intValue()));
                        }
                    }
                }

                Producte prod = new Producte(id, nom, similituds);
                prod.setPosPrestatgeries(prestatgeriesPos);

                productesMap.put(id, prod);
            } catch (NumberFormatException e) {
                throw new DominiException("Error de format numèric en el JSON: " + jsonProducte);
            } catch (ClassCastException e) {
                throw new DominiException("Error de tipus de dades en el JSON: " + jsonProducte);
            } catch (Exception e) {
                throw new DominiException("Error inesperat al processar el JSON: " + e.getMessage());
            }
        }

        return productesMap;
    }

    /**
     * Converteix un mapa de productes en una llista de cadenes JSON.
     * 
     * @param productes Mapa de productes a convertir.
     * @return Llista de cadenes JSON que representen els productes.
     */
    public List<String> productesToList(Map<Integer, Producte> productes) throws DominiException {
        Gson gson = new Gson();
        List<String> producteList = new ArrayList<>();

        if (productes != null) {
            for (Producte prod : productes.values()) {
                try {
                    Map<String, Object> producteData = Map.of(
                        "id", prod.getId(),
                        "nom", prod.getNom(),
                        "similituds", prod.getSimilituds(),
                        "posPrestatgeries", prod.getPosPrestatgeries()
                    );
                    String jsonProducte = gson.toJson(producteData);
                    producteList.add(jsonProducte);
                } catch (Exception e) {
                    throw new DominiException("Error al processar el producte amb ID " + prod.getId() + ": " + e.getMessage());
                }
            }
        }
        return producteList;
    }

    /**
     * Llista tots els productes d'un usuari en format JSON.
     * 
     * @return Mapa amb la informació dels productes en format JSON.
     */
    public Map<String, Map<String, String>> llistarProductesUsuari() throws DominiException {
        Map<String, Map<String, String>> llistatProductes = new HashMap<>();
        Gson gson = new Gson();

        for (Producte prod : productes.values()) {
            String productId = String.valueOf(prod.getId());
            Map<String, String> infoProducte = new HashMap<>();

            infoProducte.put("id", productId);
            infoProducte.put("nom", prod.getNom());

            try {
                infoProducte.put("similituds", gson.toJson(prod.getSimilituds()));
                infoProducte.put("posPrestatgeries", gson.toJson(prod.getPosPrestatgeries()));
            } catch (Exception e) {
                throw new DominiException("Error al convertir el producte amb ID " + productId + " a JSON: " + e.getMessage());
            }

            llistatProductes.put(productId, infoProducte);
        }

        return llistatProductes;
    }
 }