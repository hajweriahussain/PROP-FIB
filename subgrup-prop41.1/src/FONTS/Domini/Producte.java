package Domini;

import java.util.HashMap;
import java.util.Map;

public class Producte {
    private int id;
    private String nom;
    private int fila;
    private int columna;
    private Map<Integer, Float> similituts;


    public Producte(int id, String nom, int fila, int columna) {
        this.id = id;
        this.nom = nom;
        this.similituts = new HashMap<>();
        this.fila = fila;
        this.columna = columna;
    }


    public int getId() {
        return this.id;
    }

    public String getNom() {
        return nom;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Map<Integer, Float> getSimilituts() {
        return similituts;
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

    public void setSimilituts(Map<Integer, Float> similituts) {
        this.similituts = similituts;
    }


    public void afegirSimilitut(int id, float similitut) {
        similituts.put(id, similitut);
    }

    public void modificarSimilitut(int id, float nova_similitut) {
        if (similituts.containsKey(id)) {
            similituts.put(id, nova_similitut);
        }
        else {
            System.out.println("El producte amb id: " + id + " no existeix");
        }
    }

    public float obtenirSimilitut(int id) {
        for (Map.Entry<Integer, Float> entry : similituts.entrySet()) {
            if (entry.getKey() == id) {
                return entry.getValue();
            }
        }
        return 0.0f;
    }

    public Integer obtenirIdProducteMillorSimilitut() {
        Integer millorSimilitut = null;
        float maxSimilitut = -1.0f;
        for (Map.Entry<Integer, Float> entry : similituts.entrySet()) {
            if (entry.getValue() > maxSimilitut) {
                maxSimilitut = entry.getValue();
                millorSimilitut = entry.getKey();
            }
        }
        return millorSimilitut;
    }

    public void imprimirtSimilituts() {
        for (Map.Entry<Integer, Float> entry : similituts.entrySet()) {
            System.out.println("Producte id: " + entry.getKey() + ", similitut: " + entry.getValue());
        }
    }
}
