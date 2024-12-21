package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import java.util.Set;
import Exceptions.ExceptionFormat;

public class CjtProductes {
    private String usuari;
    private Map<Integer, Producte> productes;


    public CjtProductes(String usuari) {
        this.usuari = usuari;
        this.productes = new HashMap<>();
    }


    public Map<Integer, Producte> getProductes(String nomUsuari) throws ExceptionFormat {
        if (nomUsuari != null && usuari.equals(nomUsuari)) {
            return productes;
        }
        else {
            throw new ExceptionFormat("Usuari no vàlid: " + nomUsuari);
        }
    }

    public Producte getProducte(int idProd) throws ExceptionFormat {
        Producte producte = productes.get(idProd);
        if (producte == null) {
            throw new ExceptionFormat("No existeix un producte amb l'ID: " + idProd);
        }
        return producte;
    }
    
    public Pair<Integer, Integer> getPosProducte(int idProd, int idPres) throws ExceptionFormat {
        Producte prod = getProducte(idProd);
        Pair<Integer, Integer> posicio = prod.getPosPrestatgeria(idPres);
        if (posicio == null) {
            throw new ExceptionFormat("No hi ha posició per al producte amb ID " + idProd + " a la prestatgeria " + idPres);
        }
        return posicio;
    }
    
    public void setMapProductes(Map<Integer, Producte> prods) throws ExceptionFormat {
        if (prods != null && !prods.isEmpty()) {
            this.productes = new HashMap<>(prods);
        }
        else {
            throw new ExceptionFormat("El map de productes és nul o buit");
        }
    }
    
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeriesProducte(int idProd) throws ExceptionFormat {
        Producte prod = getProducte(idProd);
        return prod.getPosPrestatgeries();
    }

    public Producte[] getVecProductes() {
        List<Producte> producteList = new ArrayList<>(productes.values());
        return producteList.toArray(Producte[]::new);
    }

    public boolean existeixProducte(int idProd) throws ExceptionFormat {
        if (idProd <= 0) {
            throw new ExceptionFormat("L'ID ha de ser un nombre positiu.");
        }
        return productes.containsKey(idProd);
    }

    public void afegirProducte(int id, String nom, Map<Integer, Double> similituds) throws ExceptionFormat {
        if (id <= 0) {
            throw new ExceptionFormat("L'ID ha de ser un nombre positiu.");
        }

        if (existeixProducte(id)) {
            throw new ExceptionFormat("Ja existeix un producte amb l'id especificat: " + id);
        }

        Producte p = new Producte(id, nom, similituds);
        productes.put(p.getId(), p);

        if (similituds != null && !similituds.isEmpty()) {
            for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
                int prodVecId = entry.getKey();
                double similitud = entry.getValue();

                Producte prodVec = getProducte(prodVecId);
                if (prodVec != null) {
                    prodVec.afegirSimilitud(p.getId(), similitud);
                }
                else {
                    throw new ExceptionFormat("No existeix un producte amb l'ID de similitud: " + prodVecId);
                }
            }
        }
    }


    public void editarProducte(Producte p) throws ExceptionFormat {
        if (!existeixProducte(p.getId())) {
            throw new ExceptionFormat("No existeix producte a editar amb ID: " + p.getId());
        }

        Producte prod = getProducte(p.getId());
        prod.setNom(p.getNom());
    }

    public void editarIdProducte(int idProd, int nouIdProd) throws ExceptionFormat {

        Producte prod = getProducte(idProd);
        if (prod == null) {
            throw new ExceptionFormat("No existeix producte a editar amb ID: " + idProd);
        }

        if (existeixProducte(nouIdProd)) {
            throw new ExceptionFormat("Ja existeix un producte amb el nou ID especificat: " + nouIdProd);
        }

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


    public void editarNomProducte(int idProd, String nouNom) throws ExceptionFormat {
        Producte prod = getProducte(idProd);
        if (prod == null) {
            throw new ExceptionFormat("No existeix producte a editar amb ID: " + idProd);
        }

        prod.setNom(nouNom);
    }


    public void editarPosProducte(int idProd, int idPres, Pair<Integer, Integer> novaPos) throws ExceptionFormat {
        Producte prod = getProducte(idProd);
        if (prod == null) {
            throw new ExceptionFormat("No existeix producte amb ID: " + idProd);
        }

        prod.modificarPosPrestatgeria(idPres, novaPos);
    }

    public void eliminarProducte(int idProd) throws ExceptionFormat {
        if (!existeixProducte(idProd)) {
            throw new ExceptionFormat("No existeix producte a eliminar amb ID: " + idProd);
        }

        productes.remove(idProd);
        for (Producte prod2 : productes.values()) {
            if (prod2.getSimilituds().containsKey(idProd)) {
                prod2.getSimilituds().remove(idProd);
            }
        }
    }

    public void modificarSimilitud(int idProd1, int idProd2, double novaSimilitud) throws ExceptionFormat {
        Producte prod1 = getProducte(idProd1);
        Producte prod2 = getProducte(idProd2);

        if (prod1 == null) {
            throw new ExceptionFormat("No existeix producte amb ID: " + idProd1);
        }

        if (prod2 == null) {
            throw new ExceptionFormat("No existeix producte amb ID: " + idProd2);
        }

        if (prod1.getSimilituds().containsKey(idProd2)) {
            prod1.modificarSimilitud(idProd2, novaSimilitud);
            prod2.modificarSimilitud(idProd1, novaSimilitud);
            System.out.println("Similitud actualitzada!");
        }
        else {
            throw new ExceptionFormat("No existeix una similitud entre els productes amb IDs: " + idProd1 + " i " + idProd2);
        }
    }

    public Map<Integer, Double> getSimilituds(int idProd) throws ExceptionFormat {
        Producte prod = getProducte(idProd);
        if (prod == null) {
            throw new ExceptionFormat("No existeix producte amb ID: " + idProd);
        }

        return prod.getSimilituds();
    }


    public double[][] getMatriuSimilituds() {
        int n = productes.size();
        double[][] mat = new double[n][n];

        int idx = 0;
        for (Producte prod : productes.values()) {
            int colx = 0;
            for (Producte prod2 : productes.values()) {
                if (prod.getId() == prod2.getId()) {
                    mat[idx][idx] = 0.0;
                }
                else {
                    mat[idx][colx] = prod.getSimilitud(prod2.getId());
                }
                ++colx;
            }
            ++idx;
        }
        return mat;
    }
    
    public double[][] getMatriuSimilitudsPerIds(int[] idsProds) throws ExceptionFormat {
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
    
    public Producte[][] getMatProductes(Integer[][] idsProds) throws ExceptionFormat {
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
                }
                else {
                    matProds[i][j] = null;
                }
            }
        }

        return matProds;
    }
    
    public Map<Integer, Producte> listToProductes(List<String> producteJsonList) throws ExceptionFormat {
        Gson gson = new Gson();
        Map<Integer, Producte> productesMap = new HashMap<>();

        for (String jsonProducte : producteJsonList) {
            try {
                Map<String, Object> producteData = gson.fromJson(jsonProducte, Map.class);

                int id = ((Double) producteData.get("id")).intValue();
                String nom = (String) producteData.get("nom");

                Map<Integer, Double> similituds = new HashMap<>();
                Map<String, Double> similitudsRaw = (Map<String, Double>) producteData.get("similituds");
                for (Map.Entry<String, Double> entry : similitudsRaw.entrySet()) {
                    Integer key = Integer.parseInt(entry.getKey());
                    similituds.put(key, entry.getValue());
                }

                Map<Integer, Pair<Integer, Integer>> prestatgeriesPos = new HashMap<>();
                Map<String, List<Double>> posPrestatgeriesRaw = (Map<String, List<Double>>) producteData.get("posPrestatgeries");
                for (Map.Entry<String, List<Double>> entry : posPrestatgeriesRaw.entrySet()) {
                    Integer key = Integer.parseInt(entry.getKey());
                    List<Double> value = entry.getValue();
                    if (value.size() == 2) {
                        prestatgeriesPos.put(key, new Pair<>(value.get(0).intValue(), value.get(1).intValue()));
                    }
                }

                Producte prod = new Producte(id, nom, similituds);
                prod.setPosPrestatgeries(prestatgeriesPos);

                productesMap.put(id, prod);
            } catch (NumberFormatException e) {
                throw new ExceptionFormat("Error de format numèric en el JSON: " + jsonProducte);
            } catch (ClassCastException e) {
                throw new ExceptionFormat("Error de tipus de dades en el JSON: " + jsonProducte);
            } catch (Exception e) {
                throw new ExceptionFormat("Error inesperat al processar el JSON: " + e.getMessage());
            }
        }

        return productesMap;
    }
    
    public List<String> productesToList(Map<Integer, Producte> productes) throws ExceptionFormat {
        Gson gson = new Gson();
        List<String> producteList = new ArrayList<>();

        if (productes != null) {
            for (Producte prod : productes.values()) {
                if (prod == null) {
                    throw new ExceptionFormat("Producte nul trobat en el mapa.");
                }
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
                    throw new ExceptionFormat("Error al processar el producte amb ID " + prod.getId() + ": " + e.getMessage());
                }
            }
        }
        return producteList;
    }
    
    public Map<String, Map<String, String>> llistarProductesUsuari() throws ExceptionFormat {
        Map<String, Map<String, String>> llistatProductes = new HashMap<>();
        Gson gson = new Gson();

        for (Producte prod : productes.values()) {
            if (prod == null) {
                throw new ExceptionFormat("Producte nul trobat en el mapa.");
            }

            String productId = String.valueOf(prod.getId());
            Map<String, String> infoProducte = new HashMap<>();

            infoProducte.put("id", productId);
            infoProducte.put("nom", prod.getNom());

            try {
                infoProducte.put("similituds", gson.toJson(prod.getSimilituds()));
                infoProducte.put("posPrestatgeries", gson.toJson(prod.getPosPrestatgeries()));
            } catch (Exception e) {
                throw new ExceptionFormat("Error al convertir el producte amb ID " + productId + " a JSON: " + e.getMessage());
            }

            llistatProductes.put(productId, infoProducte);
        }

        return llistatProductes;
    }
 }