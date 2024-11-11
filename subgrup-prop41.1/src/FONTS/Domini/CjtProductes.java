package Domini;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public boolean existeixProducte(int idProd) {
        return productes.containsKey(idProd);
    }

    public boolean afegirProducte(Producte p) {
        if (!productes.containsKey(p.getId())) {
            productes.put(p.getId(), p);
            return true;
        }
        System.out.println("Ja existeix un producte amb l'id especificat");
        return false;
    }

    public boolean editarProducte(Producte p) {
        if (productes.containsKey(p.getId())) {
            productes.put(p.getId(), p);
            return true;
        }
        System.out.println("No existeix producte a editar");
        return false;
    }

    public boolean eliminarProducte(int idProd) {
        if (productes.containsKey(idProd)) {
            productes.remove(idProd);
            return true;
        }
        System.out.println("No existeix producte a eliminar");
        return false;
    }

    public void establirSimilituts(Scanner scanner) {
        System.out.println("Introdueix les similituts (una línia per producte):");
        
        for (Producte prod : productes.values()) {
            for (Producte prod2 : productes.values()) {
                if (prod.getId() == prod2.getId()) {
                    productes.get(prod.getId()).afegirSimilitut(prod2.getId(), 0.0);
                }
                else {
                    double similitut = scanner.nextDouble();
                    productes.get(prod.getId()).afegirSimilitut(prod2.getId(), similitut);
                }
            }
        }
    }

    public Map<Integer, Double> obtenirSimilituts(int idProd) {
        Producte prod = productes.get(idProd);
        if (prod != null) {
            return prod.getSimilituts();
        }
        else return null;
    }

    public double[][] obtenirMatriuSimilituts() {
        int n = productes.size();
        double[][] matriu = new double[n][n];

        int idx = 0;
        for (Producte prod : productes.values()) {
            int colx = 0;
            for (Producte prod2 : productes.values()) {
                if (prod.getId() == prod2.getId()) {
                    matriu[idx][idx] = 0.0;
                }
                else {
                    matriu[idx][colx] = prod.obtenirSimilitut(prod2.getId());
                }
                ++colx;
            }
            ++idx;
        }
        return matriu;
    }
}