package Domini;

import java.util.HashMap;
import java.util.Map;

public class Producte {
    private Integer id;
    private String nom;
    private Map<Integer, Double> similituds;
    private Map<Integer, Pair<Integer, Integer>> posPrestatgeries;

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Error: L'id del producte ha de ser superior a 0");
        }
    }

    private void validarSimilitud(double similitud) {
        if (similitud < 0 || similitud > 1) {
            throw new IllegalArgumentException("Error: La similitud ha de ser un valor positiu");
        }
    }

    public Producte(int id, String nom) {
        validarId(id);
        this.id = id;
        this.nom = nom;
        this.similituds = new HashMap<>();
        this.posPrestatgeries = new HashMap<>();
    }

    public Producte(int id, String nom, Map<Integer, Double> similituds) {
        validarId(id);
        this.id = id;
        this.nom = nom;
        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            validarSimilitud(entry.getValue());
        }
        this.similituds = similituds;
        this.posPrestatgeries = new HashMap<>();
    }


    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public Map<Integer, Double> getSimilituds() {
        return this.similituds;
    }
    
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeries() {
        return this.posPrestatgeries;
    }
    
    public Pair<Integer, Integer> getPosPrestatgeria(int idPres) {
        if (posPrestatgeries.containsKey(idPres)) {
            return posPrestatgeries.get(idPres);
        }
        else {
            return null;
        }
    }

    public void setId(int id) {
        validarId(id);
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSimilituds(Map<Integer, Double> similituds) {
        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            validarSimilitud(entry.getValue());
        }
        this.similituds = similituds;
    }
    
    public void setPosPrestatgeries(Map<Integer, Pair<Integer, Integer>> posPres) {
        this.posPrestatgeries = posPres;
    }
    
    public void setPosPrestatgeria(int idPres, Pair<Integer, Integer> novaPos) {
        posPrestatgeries.put(idPres, novaPos);
    }


    public void afegirSimilitud(int id, double similitud) {
        validarSimilitud(similitud);
        similituds.put(id, similitud);
    }

    public void modificarSimilitud(int id, double novaSimilitud) {
        validarSimilitud(novaSimilitud);
        if (!similituds.containsKey(id)) {
            System.out.println("Error: El producte amb id: " + id + " no existeix");
            return;
        }

        similituds.put(id, novaSimilitud);
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
        
    public void afegirPosPrestatgeria(int idPres, Pair<Integer, Integer> pos) {
        posPrestatgeries.put(idPres, pos);
    }
    
    public void modificarPosPrestatgeria(int idPres, Pair<Integer, Integer> novaPos) {
        if (!posPrestatgeries.containsKey(idPres)) {
            System.out.println("Error: La prestatgeria amb id: " + idPres + "no existeix");
            return;
        }
        
        posPrestatgeries.put(idPres, novaPos);
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