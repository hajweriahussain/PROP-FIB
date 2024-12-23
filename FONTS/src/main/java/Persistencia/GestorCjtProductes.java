package Persistencia;

import Domini.Pair;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Exceptions.PersistenciaException;

/**
 * Aquesta classe gestiona les operacions relacionades amb el conjunt de productes 
 * d'un usuari, incloent la importació, l'emmagatzematge i l'esborrat dels productes 
 * en un arxiu JSON. També permet la manipulació de dades de productes des d'un fitxer 
 * extern, com la seva lectura i validació.
 */
public class GestorCjtProductes {

    /**
     * Importa els productes d'un usuari des d'un arxiu JSON. Cada producte es desa com a 
     * una cadena JSON que conté la seva informació detallada, incloent-hi les similituds 
     * i la posició a les prestatgeries.
     *
     * @param usuari El nom de l'usuari per al qual es vol importar el conjunt de productes.
     * @return Una llista de cadenes JSON que representen els productes importats.
     * @throws PersistenciaException Si hi ha un error en accedir a l'arxiu, en parsejar el JSON,
     *                               o qualsevol altre error inesperat durant el procés d'importació.
     */
    public static List<String> importarProductes(String usuari) throws PersistenciaException {
        List<String> productes = new ArrayList<>();
        String ruta = getRuta(usuari);  // Obtenir la ruta de l'arxiu JSON corresponent a l'usuari

        try (FileReader fr = new FileReader(ruta)) {
            if (new File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                JSONObject jsonProductes = (JSONObject) parser.parse(fr);

                for (Object key : jsonProductes.keySet()) {
                    String idProd = (String) key; // Clau del producte en format String
                    JSONObject jsonProducte = (JSONObject) jsonProductes.get(idProd);

                    Map<String, Object> producteInfo = new HashMap<>();
                    producteInfo.put("id", idProd);
                    producteInfo.put("nom", jsonProducte.get("nom"));

                    // Manejo de similituds
                    JSONObject similitudsObj = (JSONObject) jsonProducte.get("similituds");
                    Map<String, Double> similitudsMap = new HashMap<>();

                    // Si similituds está vacío, simplemente se queda vacío el mapa
                    if (similitudsObj != null && !similitudsObj.isEmpty()) {
                        for (Object simKey : similitudsObj.keySet()) {
                            String simId = (String) simKey;
                            Double simValue = Double.parseDouble(similitudsObj.get(simId).toString());
                            similitudsMap.put(simId, simValue);
                        }
                    }
                    producteInfo.put("similituds", similitudsMap);

                    // Manejo de posPrestatgeries
                    JSONObject posPrestatgeriesObj = (JSONObject) jsonProducte.get("posPrestatgeries");
                    Map<String, Pair<Integer, Integer>> posPrestatgeriesMap = new HashMap<>();

                    // Si posPrestatgeries está vacío, simplemente se queda vacío el mapa
                    if (posPrestatgeriesObj != null && !posPrestatgeriesObj.isEmpty()) {
                        for (Object posKey : posPrestatgeriesObj.keySet()) {
                            String posId = (String) posKey;
                            JSONObject posObject = (JSONObject) posPrestatgeriesObj.get(posId);

                            // Asegúrate de que contiene las claves "clau" y "valor"
                            if (posObject.containsKey("clau") && posObject.containsKey("valor")) {
                                int fila = Integer.parseInt(posObject.get("clau").toString());
                                int columna = Integer.parseInt(posObject.get("valor").toString());
                                posPrestatgeriesMap.put(posId, new Pair<>(fila, columna));
                            }
                        }
                    }
                    producteInfo.put("posPrestatgeries", posPrestatgeriesMap);

                    Gson gson = new Gson();
                    String producteJSON = gson.toJson(producteInfo);
                    productes.add(producteJSON);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error en accedir al arxiu: " + e.getMessage());
        } catch (ParseException e) {
            throw new PersistenciaException("Error en parsear l'arxiu JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new PersistenciaException("Error inesperatttttttttt: " + e.getMessage());
        }

        return productes;
    }

    /**
     * Desa la llista de productes en un arxiu JSON associat a l'usuari. Si ja existeix 
     * un arxiu, es sobrescriurà. En cas que la llista sigui null, l'arxiu es borraran.
     *
     * @param productes La llista de productes a desar, cada producte representat com a 
     *                  cadena JSON.
     * @param usuari El nom de l'usuari associat a l'arxiu.
     * @throws PersistenciaException Si hi ha un error en parsejar el JSON dels productes,
     *                               en escriure l'arxiu, o qualsevol altre error inesperat
     *                               durant el procés de guardar els productes.
     */
    public static void guardarProductes(List<String> productes, String usuari) throws PersistenciaException {
        if (productes != null) {
            Map<String, Object> jsonProductes = new LinkedHashMap<>();
            JSONParser parser = new JSONParser();

            for (String prod : productes) {
                try {
                    System.out.println("Processant producte: " + prod);
                    JSONObject jsonProducte = (JSONObject) parser.parse(prod);
                    String id = jsonProducte.get("id").toString();

                    Map<String, Object> producteOrdenat = new LinkedHashMap<>();
                    producteOrdenat.put("id", id);
                    producteOrdenat.put("nom", jsonProducte.get("nom"));
                    
                    // Manejo específico para similituds y posPrestatgeries
                    Object similituds = jsonProducte.get("similituds");
                    producteOrdenat.put("similituds", (similituds instanceof JSONObject) ? similituds : new JSONObject());

                    Object posPrestatgeries = jsonProducte.get("posPrestatgeries");
                    producteOrdenat.put("posPrestatgeries", (posPrestatgeries instanceof JSONObject) ? posPrestatgeries : new JSONObject());

                    jsonProductes.put(id, producteOrdenat);
                    System.out.println("Producte processat correctament: " + id); // Log para depuración
                } catch (ParseException e) {
                    throw new PersistenciaException("Error al parsear el JSON: " + e.getMessage());
                } catch (Exception e) {
                    throw new PersistenciaException("Error inesperat al guardar productes");
                }
            }

            String ruta = getRuta(usuari);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            
            try (FileWriter file = new FileWriter(ruta)) {
                String jsonString = gson.toJson(jsonProductes);
                file.write(jsonString);
                file.flush();
            } catch (IOException e) {
                throw new PersistenciaException("Error al guardar l'arxiu: " + e.getMessage());
            }
        } else {
            System.out.println("No hi ha productes per guardar. S'esborrarà l'arxiu existent."); // Log para depuración
            esborrarProductes(usuari);
        }
    }

    /**
     * Elimina l'arxiu de productes associat a un usuari. Retorna true si s'ha pogut 
     * esborrar correctament.
     *
     * @param usuari El nom de l'usuari per al qual es vol eliminar l'arxiu.
     * @return Un valor booleà que indica si l'arxiu s'ha esborrat correctament.
     * @throws PersistenciaException Si hi ha un error inesperat durant el procés d'esborrat de l'arxiu.
     */
    public static boolean esborrarProductes(String usuari) throws PersistenciaException {
        String ruta = getRuta(usuari);
        boolean borrat = false;

        try {
            File arxiu = new File(ruta);
            if (arxiu.exists() && arxiu.isFile()) {
                borrat = arxiu.delete();
            }
        }
        catch (Exception e) {
            throw new PersistenciaException("Error inesperat al esborrar productes");
        }

        return borrat;
    }

    /**
     * Obté la ruta de l'arxiu associat a l'usuari per emmagatzemar els productes.
     * Si l'arxiu o la carpeta no existeixen, els crea.
     *
     * @param usuari El nom de l'usuari per al qual es vol obtenir la ruta.
     * @return La ruta completa de l'arxiu JSON associat a l'usuari.
     * @throws PersistenciaException Si hi ha un error en crear l'arxiu o la carpeta.
     */
    private static String getRuta(String usuari) throws PersistenciaException {
        String rutaCarpeta = "src/main/resources/persistencia";
        String rutaArxiu = rutaCarpeta + "/" + usuari + "_productes.json";

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();   // Crea la carpeta
        }

        File arxiu = new File(rutaArxiu);
        if (!arxiu.exists()) {
            try {
                arxiu.createNewFile();  // Crea l'arxiu
            } catch (IOException e) {
                throw new PersistenciaException("Error en accedir al arxiu: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
    
    /**
     * Importa les dades d'un producte des d'un fitxer extern. La primera línia del fitxer 
     * ha de contenir l'ID i el nom del producte, i les línies següents les similituds 
     * amb altres productes.
     *
     * @param path La ruta del fitxer a importar.
     * @return Una llista amb les dades del producte.
     * @throws PersistenciaException Si hi ha errors en el format del fitxer, en la lectura de les dades,
     *                               o si les dades no compleixen amb els requisits esperats (per exemple,
     *                               ID no vàlid, similituds fora de rang, etc.).
     */
    public static List<String> importarFitxerProducte(String path) throws PersistenciaException {
        List<String> producteData = new ArrayList<>();

        try (FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr)) {

            if (!sc.hasNextLine()) {
                throw new PersistenciaException("Error: El fitxer està buit.");
            }

            String linea = sc.nextLine();
            String[] parts = linea.split("\\s+");

            if (parts.length < 2) {
                throw new PersistenciaException("Error: La línia no conté suficients dades (ID i nom).");
            }

            int id;
            try {
                id = Integer.parseInt(parts[0]);
                if (id <= 0) {
                    throw new PersistenciaException("Error: ID no vàlid (ha de ser major que 0): " + parts[0]);
                }
            } catch (NumberFormatException e) {
                throw new PersistenciaException("Error: Format incorrecte per a l'ID: " + parts[0]);
            }

            String nom = parts[1];

            producteData.add(parts[0]);
            producteData.add(nom);

            while (sc.hasNextLine()) {
                String similitudLine = sc.nextLine();
                String[] similitudParts = similitudLine.split("\\s+");

                if (similitudParts.length != 2) {
                    throw new PersistenciaException("Error: La línia de similitud no és vàlida: " + similitudLine);
                }

                try {
                    int similitudId = Integer.parseInt(similitudParts[0]);
                    double similitudValue = Double.parseDouble(similitudParts[1]);

                    if (similitudValue < 0 || similitudValue > 1) {
                        throw new PersistenciaException("Error: La similitud ha de ser un valor entre 0 i 1: " + similitudValue);
                    }

                    producteData.add(similitudId + ": " + similitudValue);
                } catch (NumberFormatException e) {
                    throw new PersistenciaException("Error: Format incorrecte per a la similitud: " + similitudLine);
                }
            }

            sc.close();
            fr.close();

        } catch (IOException e) {
            throw new PersistenciaException("Error: No s'ha pogut llegir el fitxer: " + e.getMessage());
        }

        return producteData;
    }
}
