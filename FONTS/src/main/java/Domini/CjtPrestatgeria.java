package Domini;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CjtPrestatgeria {
    private String user;
    private Map<Integer, Prestatgeria> map_prest;

    public CjtPrestatgeria(String u) {
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

    public Producte[] getLayout(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return (map_prest.get(prestatgeID).getLayout());
        //llença excepció
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    public int numProductes(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return (map_prest.get(prestatgeID)).getNumProductes();
        //llença excepció
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return -1;
    }

    public void afegeixPrestatgeria(Prestatgeria p) {
        if (!map_prest.containsKey(p.getId())) {
            map_prest.put(p.getId(), p);
        }
        System.out.println("Error: L'usuari ja té una prestatgeria amb aquest ID.");
    }

    public void editarPrestatgeria(Prestatgeria p) {
        if (map_prest.containsKey(p.getId())) {
            map_prest.remove(p.getId());
            map_prest.put(p.getId(), p);
        }
        System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }

    public void eliminarPrestatgeria(Prestatgeria p) {
        if (map_prest.containsKey(p.getId())) {
            map_prest.remove(p.getId());
        }
        System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }
}