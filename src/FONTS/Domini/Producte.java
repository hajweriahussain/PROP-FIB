package Domini;

import java.util.HashMap;
import java.util.Map;

public class Producte {
    private Integer id;
    private String nom;
    private Integer fila;       // en aquesta entrega, els productes estan a la mateixa fila
    private Integer columna;
    private Map<Integer, Double> similituds;

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Error: L'id del producte ha de ser superior a 0");
        }
    }

    private void validarSimilitud(double similitud) {
        if (similitud < 0) {
            throw new IllegalArgumentException("Error: La similitud ha de ser un valor positiu");
        }
    }

    public Producte(int id, String nom) {
        validarId(id);
        this.id = id;
        this.nom = nom;
        this.fila = 0;
        this.columna = -1;
        this.similituds = new HashMap<>();
    }

    public Producte(int id, String nom, Map<Integer, Double> similituds) {
        validarId(id);
        this.id = id;
        this.nom = nom;
        this.fila = 0;
        this.columna = -1;

        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            validarSimilitud(entry.getValue());
        }
        this.similituds = similituds;
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

    public Map<Integer, Double> getSimilituds() {
        return this.similituds;
    }


    public void setId(int id) {
        validarId(id);
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

    public void setSimilituds(Map<Integer, Double> similituds) {
        this.similituds = similituds;
    }


    public void afegirSimilitud(int id, double similitud) {
        validarSimilitud(similitud);
        similituds.put(id, similitud);
    }

    public void modificarSimilitud(int id, double nova_similitud) {
        validarSimilitud(nova_similitud);
        if (!similituds.containsKey(id)) {
            System.out.println("Error: El producte amb id: " + id + " no existeix");
            return;
        }

        similituds.put(id, nova_similitud);
    }

    public double getSimilitud(int id) {
        return this.similituds.get(id);
    }

    public int getIdProducteMillorSimilitud() {
        int idMillorSimilitud = 0;
        double maxSimilitud = -1.0f;
        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            if (entry.getValue() > maxSimilitud) {
                maxSimilitud = entry.getValue();
                idMillorSimilitud = entry.getKey();
            }
        }
        return idMillorSimilitud;
    }

    public void imprimirSimilituds() {
        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            System.out.println("Producte id: " + entry.getKey() + ", similitud: " + entry.getValue());
        }
    }

    @Override
    public String toString() {
        return "[ " + id + " : " + nom + " ]";
    }
}