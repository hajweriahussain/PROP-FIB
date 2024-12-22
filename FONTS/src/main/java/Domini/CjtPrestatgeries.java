/**
 * La classe CjtPrestatgeries gestiona un conjunt de prestatgeries associades a un usuari.
 * Permet crear, editar, consultar, i eliminar prestatgeries, així com gestionar
 * els seus layouts i els productes que contenen.
 */
package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;
import Exceptions.DominiException;

public class CjtPrestatgeries {
    private String user;
    private Map<Integer, Prestatgeria> map_prest;
    
    /**
     * Constructor de CjtPrestatgeries.
     * 
     * @param u Nom de l'usuari propietari del conjunt de prestatgeries.
     */
    public CjtPrestatgeries(String u) {
        this.user = u;
        this.map_prest = new HashMap<>();
    }

    /**
     * Obté el conjunt de prestatgeries associades a l'usuari si coincideix el seu ID.
     *
     * @param userID ID de l'usuari.
     * @return Mapa d'ID a prestatgeria, o null si l'usuari no coincideix.
     */
    public Map<Integer, Prestatgeria> getConjPrestatges(String userID) {
        if (userID != null && user.equals(userID)) {
            return map_prest;
        }
        else return null;
    }

    /**
     * Obté una prestatgeria pel seu ID.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @return La prestatgeria si existeix, o null amb un missatge d'error si no existeix.
     */
    public Prestatgeria getPrestatgeria(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return map_prest.get(prestatgeID);
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    /**
     * Obté el layout de productes d'una prestatgeria pel seu ID.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @return Matriu de productes si existeix, o null amb un missatge d'error si no existeix.
     */
    public Producte[][] getLayout(int prestatgeID) {
        if (map_prest.containsKey(prestatgeID)) return map_prest.get(prestatgeID).getLayout();
        System.out.println("Error: L'usuari no té cap prestatge amb aquesta id");
        return null;
    }

    /**
     * Crea una nova prestatgeria amb l'ID i característiques proporcionades.
     *
     * @param id      ID de la prestatgeria.
     * @param nom     Nom de la prestatgeria.
     * @param filas   Nombre de files del layout.
     * @param columnas Nombre de columnes del layout.
     * @param setProds Conjunt d'IDs de productes assignats a la prestatgeria.
     */
    public void crearPrestatgeria(int id, String nom, int filas, int columnas, Set<Integer> setProds) throws DominiException{
        if (!map_prest.containsKey(id)) map_prest.put(id, new Prestatgeria(id, nom, filas, columnas, setProds));
        else System.out.println("Error: L'usuari ja té una prestatgeria amb aquest ID.");
    }

    /**
     * Edita les característiques d'una prestatgeria existent.
     *
     * @param id    ID de la prestatgeria.
     * @param nom   Nou nom de la prestatgeria.
     * @param filas Nou nombre de files.
     * @param columnas Nou nombre de columnes.
     * @param setP  Nou conjunt d'IDs de productes.
     */
    public void editarPrestatgeria(int id, String nom, int filas, int columnas, Set<Integer> setP) throws DominiException {
        if (map_prest.containsKey(id))map_prest.put(id, new Prestatgeria(id, nom, filas, columnas, setP));
        else System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }

    /**
     * Obté una fila del layout d'una prestatgeria.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @param indexFila   Índex de la fila al layout.
     * @return Array de productes si existeix la prestatgeria i la fila, o null amb un error.
     */
    public Producte[] getPrestatge(int prestatgeID, int indexFila) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getPrestatge(indexFila);
        else {
          System.out.println("Error: No es pot obtenir el prestatge.");
          return null;
        }
    }
    
    /**
     * Estableix un nou layout de productes per a una prestatgeria.
     *
     * @param mat           Nova matriu de productes.
     * @param prestatgeID   ID de la prestatgeria.
     */
    public void setLayout(Producte[][] mat, Integer prestatgeID) throws DominiException {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.setLayout(mat);
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
    }
    
    /**
     * Obté el conjunt de productes d'una prestatgeria específica.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @return Conjunt d'IDs de productes de la prestatgeria, o null amb un error.
     */
    public Set<Integer> getProductes(Integer prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getProductes();
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
        return null;
    }
    
    /**
     * Obté el nombre de columnes d'una prestatgeria específica.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @return Nombre de columnes de la prestatgeria, o 0 amb un error.
     */
    public int getNumCols(Integer prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) return prestatgeria.getNumColumnas();
        else System.out.println("Error: No hi ha una prestatgeria amb aquest id.");
        return 0;
    }
    
    /**
     * Obté els IDs comuns entre dues estructures de prestatgeries.
     *
     * @param parella1 Primer mapa d'IDs.
     * @param parella2 Segon mapa d'IDs.
     * @return Array d'IDs comuns entre els dos mapes.
     */
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

    /**
     * Intercanvia dos productes dins el layout d'una prestatgeria.
     *
     * @param prestatgeID ID de la prestatgeria.
     * @param filaProd1   Fila del primer producte.
     * @param colProd1    Columna del primer producte.
     * @param filaProd2   Fila del segon producte.
     * @param colProd2    Columna del segon producte.
     */
    public void intercanviarDosProductes(int prestatgeID, int filaProd1, int colProd1, int filaProd2, int colProd2) throws DominiException{
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

    /**
     * Esborra una prestatgeria del conjunt.
     *
     * @param prestatgeID ID de la prestatgeria a eliminar.
     */
    public void esborrarPrestatgeria(int prestatgeID) {
        Prestatgeria prestatgeria = getPrestatgeria(prestatgeID);
        if (prestatgeria != null) prestatgeria.esborrarPrestatgeria();
        else System.out.println("Error: No es pot esborrar la prestatgeria.");
    }

    /**
     * Converteix un mapa de prestatgeries en una llista de representacions JSON.
     *
     * @param prestatgeriesMap Mapa d'ID a prestatgeria.
     * @return Llista de JSONs amb les dades de les prestatgeries.
     */
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
                    "layout", prestatgeria.getDisp()
                );
                String jsonPrestatgeria = gson.toJson(prestatgeriaData);
                prestatgeriaList.add(jsonPrestatgeria);
            }
        }
        return prestatgeriaList;
    }

    /**
     * Converteix una llista de representacions JSON a un mapa de prestatgeries.
     *
     * @param presJsonList Llista de JSONs amb les dades de prestatgeries.
     * @return Mapa d'ID a prestatgeria.
     */
    public Map<Integer, Prestatgeria> listToPrestatgeries(List<String> presJsonList) throws DominiException {
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
    
    /**
     * Llista totes les prestatgeries de l'usuari amb les seves característiques.
     *
     * @return Mapa d'ID de prestatgeria a les seves dades.
     */
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
            infoPrestatgeria.put("layout", prestatgeria.getDisp().toString());
            
            llistatPrestatgeries.put(String.valueOf(prestatgeria.getId()), infoPrestatgeria);
        }

        return llistatPrestatgeries;
    }

    /**
     * Estableix un mapa de prestatgeries.
     *
     * @param llistaPrest Mapa d'ID a prestatgeria a assignar.
     */
    public void setMapPrestatgeries(Map<Integer, Prestatgeria> llistaPrest) {
        map_prest = llistaPrest;
    }
    
    /**
     * Elimina una prestatgeria del conjunt.
     *
     * @param id ID de la prestatgeria a eliminar.
     */ 
    public void eliminarPrestatgeria(int id) {
        if (map_prest.containsKey(id)) map_prest.remove(id);
        else System.out.println("Error: L'usuari no té una prestatgeria amb aquest ID.");
    }
}
