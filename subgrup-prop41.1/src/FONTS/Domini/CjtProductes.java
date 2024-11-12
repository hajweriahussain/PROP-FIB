package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public Producte[] obtenirProductes() {
        List<Producte> producteList = new ArrayList<>(productes.values());
        return producteList.toArray(Producte[]::new);
    }

    public boolean existeixProducte(int idProd) {
        return productes.containsKey(idProd);
    }

    public void afegirProducte(Producte p) {
        if (!existeixProducte(p.getId())) {
            productes.put(p.getId(), p);

            for (Map.Entry<Integer, Double> entry : p.getSimilituts().entrySet()) {
                int prodVecId = entry.getKey();
                double similitut = entry.getValue();

                Producte prodVec = getProducte(prodVecId);
                if (prodVec != null) {
                    prodVec.afegirSimilitut(p.getId(), similitut);
                }
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
                    if (prod2.getSimilituts().containsKey(idProd)) {
                        double similitut = prod2.obtenirSimilitut(idProd);
                        prod2.afegirSimilitut(nou_idProd, similitut);
                        prod2.getSimilituts().remove(idProd);
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
                if (prod2.getSimilituts().containsKey(idProd)) {
                    prod2.getSimilituts().remove(idProd);
                }
            }
        }
        else {
            System.out.println("Error: No existeix producte a eliminar");
        }
    }

    public void establirSimilituts(Scanner scanner) {
        boolean validMat = false;

        while (!validMat) {
            System.out.println("Introdueix les similituts (una línia per producte):");
        
            int n = productes.size();
            double[][] matSimilituts = new double[n][n];
            List<Producte> prodList = new ArrayList<>(productes.values());
    
            for (int i = 0; i < prodList.size(); i++) {
                for (int j = 0; j < prodList.size(); j++) {
                    if (i == j) {
                        matSimilituts[i][j] = 0.0;
                    }
                    else {
                        System.out.print("Similitut entre producte " + prodList.get(i).getId() + " i producte " + prodList.get(j).getId() + ": ");
                        matSimilituts[i][j] = scanner.nextDouble();
                    }
                }
            }
    
            if (validarSimetria(matSimilituts)) {
                for (int i = 0; i < prodList.size(); i++) {
                    for (int j = 0; j < prodList.size(); j++) {
                        if (i != j) {
                            prodList.get(i).afegirSimilitut(prodList.get(j).getId(), matSimilituts[i][j]);
                        }
                    }
                }
                System.out.println("Similituts establertes correctament.");
            }
            else {
                System.out.println("Error: La matriu de similitud no és simètrica. Torna a introduir les similituds.");
                establirSimilituts(scanner);
            }
        }
    }

    private boolean validarSimetria(double[][] mat) {
        int n = mat.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (mat[i][j] != mat[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<Integer, Double> obtenirSimilituts(int idProd) {
        Producte prod = getProducte(idProd);
        if (prod != null) {
            return prod.getSimilituts();
        }
        else return null;
    }

    public double[][] obtenirMatriuSimilituts() {
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
                    mat[idx][colx] = prod.obtenirSimilitut(prod2.getId());
                }
                ++colx;
            }
            ++idx;
        }
        return mat;
    }
}