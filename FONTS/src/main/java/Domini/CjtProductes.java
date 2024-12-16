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

    public void editarIdProducte(int idProd, int nou_idProd) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            if (!existeixProducte(nou_idProd)) {
                for (Producte prod2 : productes.values()) {
                    if (prod2.getSimilituds().containsKey(idProd)) {
                        double similitud = prod2.getSimilitud(idProd);
                        prod2.afegirSimilitud(nou_idProd, similitud);
                        prod2.getSimilituds().remove(idProd);
                    }
                }

                prod.setId(nou_idProd);
                productes.remove(idProd);
                productes.put(nou_idProd, prod);
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
    
    public double[][] getMatriuSimilitudsPerIds(Producte[] idsProds) {
        int n = idsProds.length;
        double[][] mat = new double[n][n];

        for (int i = 0; i < n; i++) {
            Producte prod1 = idsProds[i];
            for (int j = 0; j < n; j++) {
                Producte prod2 = idsProds[j];
                if (prod1.getId() == prod2.getId()) {
                    mat[i][j] = 0.0;
                } else {
                    mat[i][j] = prod1.getSimilitud(prod2.getId());
                }
            }
        }
        return mat;
    }
    
    public Map<Integer, Producte> listToProductes(List<String> producteJsonList) {
        Gson gson = new Gson();
        Map<Integer, Producte> productesMap = new HashMap<>();

        for (String jsonProducte : producteJsonList) {
            Map<String, Object> producteData = gson.fromJson(jsonProducte, Map.class);
            
            int id = ((Double) producteData.get("id")).intValue();
            String nom = (String) producteData.get("nom");
            Map<Integer, Double> similituds = (Map<Integer, Double>) producteData.get("similituds");
            Map<Integer, Pair<Integer, Integer>> prestatgeriesPos = (Map<Integer, Pair<Integer, Integer>>) producteData.get("posPrestatgeries");

            Producte prod = new Producte(id, nom, similituds);
            prod.setPosPrestatgeries(prestatgeriesPos);

            productesMap.put(id, prod);
        }

        return productesMap;
    }
    
    public List<String> productesToList(Map<Integer, Producte> productes) {
        Gson gson = new Gson();
        List<String> producteList = new ArrayList<>();

        if (productes != null) {
            for (Producte prod : productes.values()) {
                Map<String, Object> producteData = Map.of(
                    "id", prod.getId(),
                    "nom", prod.getNom(),
                    "similituds", prod.getSimilituds(),
                    "posPrestatgeries", prod.getPosPrestatgeries()
                );
                String jsonProducte = gson.toJson(producteData);
                producteList.add(jsonProducte);
            }
        }
        return producteList;
    }
    
    public Map<String, Map<String, String>> llistarProductesUsuari() {
        Map<String, Map<String, String>> llistatProductes = new HashMap<>();
        Gson gson = new Gson();

        for (Producte prod : productes.values()) {
            Map<String, String> infoProducte = new HashMap<>();
            infoProducte.put("id", String.valueOf(prod.getId()));
            infoProducte.put("nom", prod.getNom());
            infoProducte.put("similituds", gson.toJson(prod.getSimilituds()));
            infoProducte.put("posPrestatgeries", gson.toJson(prod.getPosPrestatgeries()));

            llistatProductes.put(String.valueOf(prod.getId()), infoProducte);
        }

        return llistatProductes;
    }
 }