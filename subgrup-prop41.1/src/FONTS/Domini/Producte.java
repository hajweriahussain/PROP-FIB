package Domini;

import java.util.Map;

public class Producte {
    private Integer id;
    private String nom;
    private Integer fila;
    private Integer columna;
    private Map<Integer, Double> similituds;


    public Producte(int id, String nom, Map<Integer, Double> similituds) {
        this.id = id;
        this.nom = nom;
        this.fila = 0;  // en aquesta entrega, els productes estan a la mateixa fila
        this.columna = -1;
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
        similituds.put(id, similitud);
    }

    public void modificarSimilitud(int id, double nova_similitud) {
        if (!similituds.containsKey(id)) {
            System.out.println("El producte amb id: " + id + " no existeix");
            return;
        }

        similituds.put(id, nova_similitud);
    }

    public double obtenirSimilitud(int id) {
        return this.similituds.get(id);
    }

    public int obtenirIdProducteMillorSimilitud() {      
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

    // de moment, la posició del producte es diferencia pel número de columna en la prestatgeria
    @Override
    public String toString() {
        return "Producte {id= " + id + ", nom= '" + nom + "', pos= " + columna +"}";
    }
}
