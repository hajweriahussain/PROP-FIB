package Domini;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe que representa un producte amb una identificació única, un nom, 
 * una llista de similituds amb altres productes i les posicions en prestatgeries.
 */
public class Producte {
    /**
     * Identificador únic del producte.
     */
    private Integer id;

    /**
     * Nom del producte.
     */
    private String nom;

    /**
     * Mapa que emmagatzema les similituds del producte amb altres productes, 
     * identificats pel seu id i associats a un valor de similitud (0 a 1).
     */
    private Map<Integer, Double> similituds;

    /**
     * Mapa que emmagatzema les posicions del producte a diferents prestatgeries, 
     * identificades pel seu id i associades a una parella (x, y) de coordenades.
     */
    private Map<Integer, Pair<Integer, Integer>> posPrestatgeries;

    /**
     * Constructor d'un producte amb id i nom.
     * 
     * @param id identificador únic del producte.
     * @param nom nom del producte.
     * @throws IllegalArgumentException si l'id no és vàlid.
     */
    public Producte(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.similituds = new HashMap<>();
        this.posPrestatgeries = new HashMap<>();
    }

    /**
     * Constructor d'un producte amb id, nom i similituds.
     * 
     * @param id identificador únic del producte.
     * @param nom nom del producte.
     * @param similituds mapa de similituds del producte.
     * @throws IllegalArgumentException si l'id o alguna similitud no és vàlida.
     */
    public Producte(int id, String nom, Map<Integer, Double> similituds) {
        this.id = id;
        this.nom = nom;
        this.similituds = similituds;
        this.posPrestatgeries = new HashMap<>();
    }

    /**
     * Obté l'identificador del producte.
     * 
     * @return id del producte.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Obté el nom del producte.
     * 
     * @return nom del producte.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Obté el mapa de similituds del producte.
     * 
     * @return mapa de similituds.
     */
    public Map<Integer, Double> getSimilituds() {
        return this.similituds;
    }

    /**
     * Obté el mapa de posicions en prestatgeries del producte.
     * 
     * @return mapa de posicions en prestatgeries.
     */
    public Map<Integer, Pair<Integer, Integer>> getPosPrestatgeries() {
        return this.posPrestatgeries;
    }

    /**
     * Obté la posició del producte en una prestatgeria específica.
     * 
     * @param idPres identificador de la prestatgeria.
     * @return parella (x, y) de coordenades o null si no hi ha posició registrada.
     */
    public Pair<Integer, Integer> getPosPrestatgeria(int idPres) {
        if (posPrestatgeries.containsKey(idPres)) {
            return posPrestatgeries.get(idPres);
        } else {
            return null;
        }
    }

    /**
     * Estableix un nou id per al producte.
     * 
     * @param id nou identificador del producte.
     * @throws IllegalArgumentException si l'id no és vàlid.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Estableix un nou nom per al producte.
     * 
     * @param nom nou nom del producte.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Estableix un nou mapa de similituds per al producte.
     * 
     * @param similituds nou mapa de similituds.
     * @throws IllegalArgumentException si alguna similitud no és vàlida.
     */
    public void setSimilituds(Map<Integer, Double> similituds) {
        this.similituds = similituds;
    }

    /**
     * Estableix un nou mapa de posicions en prestatgeries.
     * 
     * @param posPres nou mapa de posicions.
     */
    public void setPosPrestatgeries(Map<Integer, Pair<Integer, Integer>> posPres) {
        this.posPrestatgeries = posPres;
    }

    /**
     * Estableix una nova posició per al producte en una prestatgeria específica.
     * 
     * @param idPres identificador de la prestatgeria.
     * @param novaPos nova parella (x, y) de coordenades.
     */
    public void setPosPrestatgeria(int idPres, Pair<Integer, Integer> novaPos) {
        posPrestatgeries.put(idPres, novaPos);
    }

    /**
     * Afegeix una nova similitud al producte.
     * 
     * @param id identificador del producte amb el qual es relaciona.
     * @param similitud valor de similitud.
     * @throws IllegalArgumentException si la similitud no és vàlida.
     */
    public void afegirSimilitud(int id, double similitud) {
        similituds.put(id, similitud);
    }

    /**
     * Modifica una similitud existent del producte.
     * 
     * @param id identificador del producte amb el qual es relaciona.
     * @param novaSimilitud nou valor de similitud.
     * @throws IllegalArgumentException si la similitud no és vàlida.
     */
    public void modificarSimilitud(int id, double novaSimilitud) {
        if (!similituds.containsKey(id)) {
            System.err.println("Error: El producte amb id: " + id + " no existeix");
        }

        similituds.put(id, novaSimilitud);
    }

    /**
     * Obté el valor de similitud amb un altre producte.
     * 
     * @param id identificador del producte.
     * @return valor de similitud.
     */
    public double getSimilitud(int id) {
        return this.similituds.get(id);
    }

    /**
     * Obté l'id del producte amb la millor similitud.
     * 
     * @return id del producte amb la màxima similitud.
     */
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

    /**
     * Afegeix una nova posició per al producte en una prestatgeria específica.
     * 
     * @param idPres identificador de la prestatgeria.
     * @param pos parella (x, y) de coordenades.
     */
    public void afegirPosPrestatgeria(int idPres, Pair<Integer, Integer> pos) {
        posPrestatgeries.put(idPres, pos);
    }

    /**
     * Modifica la posició del producte en una prestatgeria específica.
     * 
     * @param idPres identificador de la prestatgeria.
     * @param novaPos nova parella (x, y) de coordenades.
     */
    public void modificarPosPrestatgeria(int idPres, Pair<Integer, Integer> novaPos) {
        if (!posPrestatgeries.containsKey(idPres)) {
            System.err.println("Error: El producte no es troba a la prestatgeria amb id: " + idPres);
        }

        setPosPrestatgeria(idPres, novaPos);
    }

    /**
     * Imprimeix per consola les similituds del producte amb altres productes.
     */
    public void imprimirSimilituds() {
        for (Map.Entry<Integer, Double> entry : similituds.entrySet()) {
            System.out.println("Producte id: " + entry.getKey() + ", similitud: " + entry.getValue());
        }
    }

    /**
     * Retorna una representació textual del producte.
     * 
     * @return representació textual del producte.
     */
    @Override
    public String toString() {
        return "[ " + id + " : " + nom + " ]";
    }
}
