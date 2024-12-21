package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import java.util.Set;

public class CjtProductes {
    private String usuari;
    private Map<Integer, Producte> productes;


    public CjtProductes(String usuari) {
        this.usuari = usuari;
        this.productes = new HashMap<>();
    }


    public Map<Integer, Producte> getProductes(String nomUsuari) {
        if (nomUsuari != null && usuari.equals(nomUsuari)) {
            return productes;
        }
        else return null;
    }

    public Producte getProducte(int idProd) {
        return productes.get(idProd);
    }
    
    public Pair<Integer, Integer> getPosProducte(int idProd, int idPres) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            Pair<Integer, Integer> posicio = prod.getPosPrestatgeria(idPres);
            if (posicio != null) {
                return posicio;
            }
            else return null;
        }
        else {
            System.out.println("Error: No existeix un producte amb l'ID " + idProd);
            return null;
        }
    }
    
    public void setMapProductes(Map<Integer, Producte> prods) {
        if (prods != null && !prods.isEmpty()) {
            this.productes = new HashMap<>(prods);
        }
        else {
            System.out.println("Error: El map de productes és nul o buit");
        }
    }
    
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeriesProducte(int idProd) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            return prod.getPosPrestatgeries();
        }
        else {
            System.out.println("Error: No existeix un producte amb l'ID " + idProd);
            return null;
        }
    }

    public Producte[] getVecProductes() {
        List<Producte> producteList = new ArrayList<>(productes.values());
        return producteList.toArray(Producte[]::new);
    }

    public boolean existeixProducte(int idProd) {
        return productes.containsKey(idProd);
    }

    public void afegirProducte(int id, String nom, Map<Integer, Double> similituds) {
        Producte p = new Producte(id, nom, similituds);
        if (!existeixProducte(p.getId())) { //
            if (!p.getSimilituds().isEmpty()) {
                productes.put(p.getId(), p);

                for (Map.Entry<Integer, Double> entry : p.getSimilituds().entrySet()) {
                    int prodVecId = entry.getKey();
                    double similitud = entry.getValue();

                    Producte prodVec = getProducte(prodVecId);
                    if (prodVec != null) {
                        prodVec.afegirSimilitud(p.getId(), similitud);
                    }
                }
            }
            else {
                System.out.println("Error: El producte no té similituds associades.");
            }
        }
        else {
            System.out.println("Error: Ja existeix un producte amb l'id especificat");
        }
    }

    public void editarProducte(Producte p) {
        if (existeixProducte(p.getId())) {
            Producte prod = getProducte(p.getId());
            prod.setNom(p.getNom());
        }
        else {
            System.out.println("Error: No existeix producte a editar");
        }
    }

    public void editarIdProducte(int idProd, int nouIdProd) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            if (!existeixProducte(nouIdProd)) {
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
            else {
                System.out.println("Error: Ja existeix un producte amb el nou id especificat");
            }
        }
        else {
            System.out.println("Error: No existeix producte a editar");
        }
    }

    public void editarNomProducte(int idProd, String nouNom) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            prod.setNom(nouNom);
        }
        else {
            System.out.println("Error: No existeix producte a editar");
        }
    }

    public void editarPosProducte(int idProd, int idPres, Pair<Integer, Integer> novaPos) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            prod.modificarPosPrestatgeria(idPres, novaPos);
            System.out.println("Posició del producte " + idProd + " actualitzada a la prestatgeria " + idPres);
        } else {
            System.out.println("Error: No existeix producte amb ID " + idProd);
        }
    }

    public void eliminarProducte(int idProd) {
        if (existeixProducte(idProd)) {
            productes.remove(idProd);
            for (Producte prod2 : productes.values()) {
                if (prod2.getSimilituds().containsKey(idProd)) {
                    prod2.getSimilituds().remove(idProd);
                }
            }
        }
        else {
            System.out.println("Error: No existeix producte a eliminar");
        }
    }

    public void modificarSimilitud(int idProd1, int idProd2, double novaSimilitud) {
        Producte prod1 = getProducte(idProd1);
        Producte prod2 = getProducte(idProd2);

        if (prod1 != null && prod2 != null) {
            if (prod1.getSimilituds().containsKey(idProd2)) {
                prod1.modificarSimilitud(idProd2, novaSimilitud);
                prod2.modificarSimilitud(idProd1, novaSimilitud);
                System.out.println("Similitud actualitzada!");
            }
            else {
                System.out.println("Error: No existeix una similitud entre els productes");
            }
        }
        else {
            System.out.println("Error: Un o ambdós productes no existeixen");
        }
    }

    public Map<Integer, Double> getSimilituds(int idProd) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            return prod.getSimilituds();
        }
        else return null;
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
    
    public Producte[] getProductesPerIds(Set<Integer> idsProds) {
        List<Producte> prodSeleccionats = new ArrayList<>();
        for (Integer id : idsProds) {
            Producte prod = getProducte(id);
            if (prod != null) {
                prodSeleccionats.add(prod);
            }
        }
        return prodSeleccionats.toArray(Producte[]::new);
    }
    
    public double[][] getMatriuSimilitudsPerIds(Integer[] idsProds) {
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
    
    public Producte[][] getMatProductes(Integer[][] idsProds) {
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
    
    public Map<Integer, Producte> listToProductes(List<String> producteJsonList) {
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
                System.err.println("Error de format numèric en el JSON: " + jsonProducte);
            } catch (ClassCastException e) {
                System.err.println("Error de tipus de dades en el JSON: " + jsonProducte);
            } catch (Exception e) {
                System.err.println("Error inesperat al processar el JSON: " + jsonProducte);
                e.printStackTrace();
            }
        }

        return productesMap;
    }
    
    public List<String> productesToList(Map<Integer, Producte> productes) {
        Gson gson = new Gson();
        List<String> producteList = new ArrayList<>();

        if (productes != null) {
            for (Producte prod : productes.values()) {
                if (prod == null) {
                    System.err.println("Producte nul trobat en el mapa.");
                    continue;
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
                    System.err.println("Error al processar el producte amb ID " + prod.getId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return producteList;
    }
    
    public Map<String, Map<String, String>> llistarProductesUsuari() {
        Map<String, Map<String, String>> llistatProductes = new HashMap<>();
        Gson gson = new Gson();

        for (Producte prod : productes.values()) {
            if (prod == null) {
                System.err.println("Producte nul trobat en el mapa.");
                continue;
            }

            String productId = String.valueOf(prod.getId());
            Map<String, String> infoProducte = new HashMap<>();

            infoProducte.put("id", productId);
            infoProducte.put("nom", prod.getNom());

            try {
                infoProducte.put("similituds", gson.toJson(prod.getSimilituds()));
                infoProducte.put("posPrestatgeries", gson.toJson(prod.getPosPrestatgeries()));
            } catch (Exception e) {
                System.err.println("Error al convertir el producte amb ID " + productId + " a JSON: " + e.getMessage());
                continue;
            }

            llistatProductes.put(productId, infoProducte);
        }

        return llistatProductes;
    }
 }