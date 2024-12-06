package Domini;

import java.util.HashMap;
import java.util.Map;

public class CjtPrestatgeries {
    private String user;
    private Map<Integer, Prestatgeria> map_prest;

    public CjtPrestatgeries(String u) {
        this.user = u;
        this.map_prest = new HashMap<>();
    }

    public Map<Integer, Prestatgeria> getConjPrestatges(String userID) {
        if (userID != null && user.equals(userID)) {
            return map_prest;
        }
        else return null;
    }

    public Prestatgeria getPrestatgeria(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return map_prest.get(prestatgeID);
        //llença excepció
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    public Producte[][] getLayout(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return (map_prest.get(prestatgeID).getLayout());
        //llença excepció
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    public void crearPrestatgeria(int id, int filas, int columnas) { //crear
        if (!map_prest.containsKey(id)) {
            map_prest.put(id, new Prestatgeria(id, filas, columnas));
        }
        System.out.println("Error: L'usuari ja té una prestatgeria amb aquest ID.");
    }

    public void editarPrestatgeria(int id, int filas, int columnas) {
        if (map_prest.containsKey(id)) {
            map_prest.remove(id);
            map_prest.put(id, new Prestatgeria(id, filas, columnas));
        }
        System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }

    public Producte[] getPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) {
            return prestatgeria.getPrestatge(indexFila);
        } 
        else {
            System.out.println("Error: No es pot obtenir el prestatge.");
            return null;
        }
    }

    public void intercanviarDosProductes(int prestatgeID, int filaProd1, int colProd1, int filaProd2, int colProd2) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) {
            prestatgeria.intercanviarDosProductes(filaProd1, colProd1, filaProd2, colProd2);
        } 
        else {
            System.out.println("Error: No es pot intercanviar productes.");
        }
    }

    public void afegirPrestatge(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) {
            prestatgeria.afegirPrestatge();
        } 
        else {
            System.out.println("Error: No es pot afegir un nou prestatge.");
        }
    }

    public void esborrarPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) {
            prestatgeria.esborrarPrestatge(indexFila);
        } 
        else {
            System.out.println("Error: No es pot esborrar el prestatge.");
        }
    }

    public void esborrarPrestatgeria(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) {
            prestatgeria.esborrarPrestatgeria();
        } 
        else {
            System.out.println("Error: No es pot esborrar la prestatgeria.");
        }
    }

    public void eliminarPrestatgeria(int id) {
        if (map_prest.containsKey(id)) {
            map_prest.remove(id);
        }
        System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }
}