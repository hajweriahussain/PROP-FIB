package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Producte[] getProductes() {
        List<Producte> producteList = new ArrayList<>(productes.values());
        return producteList.toArray(Producte[]::new);
    }

    public boolean existeixProducte(int idProd) {
        return productes.containsKey(idProd);
    }

    public void afegirProducte(Producte p) {
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
            prod.setColumna(p.getColumna());
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

    public void editarNomProducte(int idProd, String nou_nom) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            prod.setNom(nou_nom);
        }
        else {
            System.out.println("Error: No existeix producte a editar");
        }
    }

    // de moment editem l'atribut columna del producte
    public void editarPosProducte(int idProd, int nova_pos) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            prod.setColumna(nova_pos);
        }
        else {
            System.out.println("Error: No existeix producte a editar");
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

    public void modificarSimilitud(int idProd1, int idProd2, double nova_similitud) {
        Producte prod1 = getProducte(idProd1);
        Producte prod2 = getProducte(idProd2);

        if (prod1 != null && prod2 != null) {
            if (prod1.getSimilituds().containsKey(idProd2)) {
                prod1.modificarSimilitud(idProd2, nova_similitud);
                prod2.modificarSimilitud(idProd1, nova_similitud);
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
}