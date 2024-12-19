package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;

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
    
    public void setLayout(Producte[][] mat, Integer prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.setLayout(mat);
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
    }
    
    public Set<Integer> getProductes(Integer prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getProductes();
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
        return null;
    }
    
    public int getNumCols(Integer prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getNumColumnas();
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
        return 0;
    }
    
    public Integer[] getIdsPrestatgeriesSame(Map<Integer, Pair<Integer, Integer>> parella1, 
                                             Map<Integer, Pair<Integer, Integer>> parella2) {
        List<Integer> list = new ArrayList<>();
        for (Integer key : parella1.keySet()) {
            if (parella2.containsKey(key)) {
                list.add(key);
            }
        }
        Integer[] res = new Integer[list.size()];
        int i = 0;
        for (Integer elem : list) {
            res[i] = elem;
            ++i;
        }
        return res;
    }

    public void intercanviarDosProductes(int prestatgeID, int filaProd1, int colProd1, int filaProd2, int colProd2) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.intercanviarDosProductes(filaProd1, colProd1, filaProd2, colProd2);
        else System.out.println("Error: No es pot intercanviar productes.");
    }
/*
    public void afegirPrestatge(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.afegirPrestatge();
        else System.out.println("Error: No es pot afegir un nou prestatge.");
    }

    public void esborrarPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.esborrarPrestatge(indexFila);
        else System.out.println("Error: No es pot esborrar el prestatge.");
    }*/

    public void esborrarPrestatgeria(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.esborrarPrestatgeria();
        else System.out.println("Error: No es pot esborrar la prestatgeria.");
    }

    public List<String> prestatgeriesToList(Map<Integer, Prestatgeria> prestatgeriesMap) {
        Gson gson = new Gson();
        List<String> prestatgeriaList = new ArrayList<>();

        if (prestatgeriesMap != null) {
            for (Prestatgeria prestatgeria : prestatgeriesMap.values()) {
                Map<String, Object> prestatgeriaData = Map.of(
                    "id", prestatgeria.getId(),
                    "nom", prestatgeria.getNom(),
                    "numFilas", prestatgeria.getNumFilas(),
                    "numColumnas", prestatgeria.getNumColumnas(),
                    "productes", prestatgeria.getProductes(),
                    "layout", prestatgeria.getLayout()
                );
                String jsonPrestatgeria = gson.toJson(prestatgeriaData);
                prestatgeriaList.add(jsonPrestatgeria);
            }
        }
        return prestatgeriaList;
    }

    
    public Map<Integer, Prestatgeria> listToPrestatgeries(List<String> presJsonList) {
        Gson gson = new Gson();
        Map<Integer, Prestatgeria> prestatgeriesMap = new HashMap<>();

        for (String jsonPrestatgeria : presJsonList) {
            Map<String, Object> prestatgeriaData = gson.fromJson(jsonPrestatgeria, Map.class);

            int id = ((Double) prestatgeriaData.get("id")).intValue();
            String nom = (String) prestatgeriaData.get("nom");
            int numFilas = ((Double) prestatgeriaData.get("numFilas")).intValue();
            int numColumnas = ((Double) prestatgeriaData.get("numColumnas")).intValue();
            Set<Integer> setP = new HashSet<>();
            
            Prestatgeria prestatgeria = new Prestatgeria(id, nom, numFilas, numColumnas, setP);
            List<List<Map<String, Object>>> dispData = (List<List<Map<String, Object>>>) prestatgeriaData.get("layout");
            if (dispData != null) {
                Producte[][] layout = new Producte[numFilas][numColumnas];
                for (int i = 0; i < dispData.size(); i++) {
                    List<Map<String, Object>> filaData = dispData.get(i);
                    for (int j = 0; j < filaData.size(); j++) {
                        Map<String, Object> prodData = filaData.get(j);
                        if (prodData != null) {
                            int prodId = ((Double) prodData.get("id")).intValue();
                            String prodNom = (String) prodData.get("nom");
                            Producte producte = new Producte(prodId, prodNom);
                            layout[i][j] = producte;
                            setP.add(prodId);
                        }
                    }
                }
                prestatgeria.setLayout(layout);
            }
            prestatgeriesMap.put(id, prestatgeria);
        }
        return prestatgeriesMap;
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

    
    public void setMapPrestatgeries(Map<Integer, Prestatgeria> llistaPrest) {
        map_prest = llistaPrest;
    }
    
    //dado dos maps, ver si coinciden 2 ids -> return vector de ids (integers). 
    public void eliminarPrestatgeria(int id) {
        if (map_prest.containsKey(id)) map_prest.remove(id);
        else System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }
}
