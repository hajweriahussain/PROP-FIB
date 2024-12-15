package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    public Producte[][] getLayout(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return map_prest.get(prestatgeID).getLayout();
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    public void crearPrestatgeria(int id, String nom, int filas, int columnas, Set<Integer> setProds) {
        if (!map_prest.containsKey(id)) map_prest.put(id, new Prestatgeria(id, nom, filas, columnas, setProds));
        else System.out.println("Error: L'usuari ja té una prestatgeria amb aquest ID.");
    }

    public void editarPrestatgeria(int id, String nom, int filas, int columnas, Set<Integer> setP) {
        if (map_prest.containsKey(id))map_prest.put(id, new Prestatgeria(id, nom, filas, columnas, setP));
        else System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }

    public Producte[] getPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getPrestatge(indexFila);
        else {
          System.out.println("Error: No es pot obtenir el prestatge.");
          return null;
        }
    }

    public void intercanviarDosProductes(int prestatgeID, int filaProd1, int colProd1, int filaProd2, int colProd2) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.intercanviarDosProductes(filaProd1, colProd1, filaProd2, colProd2);
        else System.out.println("Error: No es pot intercanviar productes.");
    }

    public void afegirPrestatge(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.afegirPrestatge();
        else System.out.println("Error: No es pot afegir un nou prestatge.");
    }

    public void esborrarPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.esborrarPrestatge(indexFila);
        else System.out.println("Error: No es pot esborrar el prestatge.");
    }

    public void esborrarPrestatgeria(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.esborrarPrestatgeria();
        else System.out.println("Error: No es pot esborrar la prestatgeria.");
    }

    public Map<Integer, Prestatgeria> prestatgeriesToList (List<String> presJsonList) {
        Gson gson = new Gson();
        Map<Integer, Prestatgeria> prestatgeriesMap = new HashMap<>();

        for (String jsonPrestatgeria : presJsonList) {
            Map<String, Object> prestatgeriaData = gson.fromJson(jsonPrestatgeria, Map.class);

            int id = ((Double) prestatgeriaData.get("id")).intValue();
            String nom = (String) prestatgeriaData.get("nom");
            int numFilas = ((Double) prestatgeriaData.get("numFilas")).intValue();
            int numColumnas = ((Double) prestatgeriaData.get("numColumnas")).intValue();
            Set<Integer> productes = (Set<Integer>) prestatgeriaData.get("productes"); 

            Prestatgeria prestatgeria = new Prestatgeria(id, nom, numFilas, numColumnas, productes);

            prestatgeriesMap.put(id, prestatgeria);
        }
        return prestatgeriesMap;
    }

    
    public void listToPrestatgeries(List<String> presJasonList) {
        Gson gson = new Gson();
 
        for (String jsonPrestatgeria : presJasonList) {
            Map<String, Object> prestatgeriaData = gson.fromJson(jsonPrestatgeria, Map.class);
 
            Integer id = ((Double) prestatgeriaData.get("id")).intValue();
            String nom = (String) prestatgeriaData.get("nom");
            Integer numFilas = ((Double) prestatgeriaData.get("numFilas")).intValue();
            Integer numColumnas = ((Double) prestatgeriaData.get("numColumnas")).intValue();
            Set<Integer> setP = new HashSet<>();
            
 
            List<List<Map<String, Object>>> dispData = (List<List<Map<String, Object>>>) prestatgeriaData.get("layout");
            Prestatgeria prestatgeria = new Prestatgeria(id, nom, numFilas, numColumnas, setP);
 
            if (dispData != null) {
                Producte[][] layout = new Producte[numFilas][numColumnas];
                for (int i = 0; i < dispData.size(); i++) {
                    List<Map<String, Object>> filaData = dispData.get(i);
                    for (int j = 0; j < filaData.size(); j++) {
                        Map<String, Object> prodData = filaData.get(j);
                        if (prodData != null) {
                            Integer prodId = ((Double) prodData.get("id")).intValue();
                            String prodNom = (String) prodData.get("nom");
                            Producte producte = new Producte(prodId, prodNom);
                            layout[i][j] = producte;
                         }
                    }
                }
                prestatgeria.setLayout(layout);
            }
            map_prest.put(id, prestatgeria);
        }
    }
    
    public Map<String, Map<String, String>> llistarPrestatgeriesUsuari() {
        Map<String, Map<String, String>> llistatPrestatgeries = new HashMap<>();

        if (map_prest.isEmpty()) {
            System.out.println("No hi ha prestatgeries per a l'usuari.");
            return llistatPrestatgeries;
        }

        for (Prestatgeria prestatgeria : map_prest.values()) {
            Map<String, String> infoPrestatgeria = new HashMap<>();
            infoPrestatgeria.put("id", String.valueOf(prestatgeria.getId()));
            infoPrestatgeria.put("nom", prestatgeria.getNom());
            infoPrestatgeria.put("files", String.valueOf(prestatgeria.getNumFilas()));
            infoPrestatgeria.put("columnes", String.valueOf(prestatgeria.getNumColumnas()));
            infoPrestatgeria.put("productes", prestatgeria.getProductes().toString());

            llistatPrestatgeries.put(String.valueOf(prestatgeria.getId()), infoPrestatgeria);
        }

        return llistatPrestatgeries;
    }

    
    //dado dos maps, ver si coinciden 2 ids -> return vector de ids (integers). 
    public void eliminarPrestatgeria(int id) {
        if (map_prest.containsKey(id)) map_prest.remove(id);
        else System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }
}
