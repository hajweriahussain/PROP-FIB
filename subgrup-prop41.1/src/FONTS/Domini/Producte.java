package Domini;

import java.util.HashMap;
import java.util.Map;

public class Producte {
    private int id;
    private String nom;
    private int fila;
    private int columna;
    private Map<Integer, Double> similituts;


    public Producte(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.fila = 0;  // en aquesta entrega, els productes estan a la mateixa fila
        this.columna = -1;
        this.similituts = new HashMap<>();
    }


    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public int getFila() {
        return this.fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public Map<Integer, Double> getSimilituts() {
        return this.similituts;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setSimilituts(Map<Integer, Double> similituts) {
        this.similituts = similituts;
    }


    public void afegirSimilitut(int id, double similitut) {
        similituts.put(id, similitut);
    }

    public void modificarSimilitut(int id, double nova_similitut) {
        if (!similituts.containsKey(id)) {
            System.out.println("El producte amb id: " + id + " no existeix");
            return;
        }

        similituts.put(id, nova_similitut);
    }

    public double obtenirSimilitut(int id) {
        return this.similituts.get(id);
    }

    public int obtenirIdProducteMillorSimilitut() {      
        int idMillorSimilitut = 0;
        double maxSimilitut = -1.0f;
        for (Map.Entry<Integer, Double> entry : similituts.entrySet()) {
            if (entry.getValue() > maxSimilitut) {
                maxSimilitut = entry.getValue();
                idMillorSimilitut = entry.getKey();
            }
        }
        return idMillorSimilitut;
    }

    public void imprimirSimilituts() {
        for (Map.Entry<Integer, Double> entry : similituts.entrySet()) {
            System.out.println("Producte id: " + entry.getKey() + ", similitut: " + entry.getValue());
        }
    }

    // de moment, la posició del producte es diferencia pel número de columna en la prestatgeria
    @Override
    public String toString() {
        return "Producte {id= " + id + ", nom= '" + nom + "', pos= " + columna +"}";
    }
}
