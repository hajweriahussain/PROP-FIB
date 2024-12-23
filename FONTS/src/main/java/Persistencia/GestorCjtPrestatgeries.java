package Persistencia;

import Exceptions.PersistenciaException;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Classe GestorCjtPrestatgeries
 * 
 * Aquesta classe gestiona la persistència de les prestatgeries associades als usuaris.
 * Proporciona mètodes per importar, guardar i esborrar prestatgeries en format JSON,
 * així com per gestionar fitxers de prestatgeries.
 * 
 * @author [hajweria.hussain]
 * @version 1.0
 */
public class GestorCjtPrestatgeries {
    
    /**
     * Importa les prestatgeries associades a un usuari des d'un fitxer JSON.
     *
     * @param usuari Nom de l'usuari.
     * @return Llista de prestatgeries en format JSON com a cadenes.
     * @throws PersistenciaException Si ocorre un error durant la lectura o el
     * parseig del fitxer.
     */
    public static List<String> importarPrestatgeries(String usuari) throws PersistenciaException{
        List<String> prestatgeries = new ArrayList<>();
        String ruta = getRuta(usuari);
        try(FileReader fr = new FileReader(ruta)) {
            if (new java.io.File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                JSONObject jsonPrestatgeries = (JSONObject) parser.parse(fr);

                for (Object key : jsonPrestatgeries.keySet()) {
                    String prestatgeriaId = (String) key; 
                    JSONObject jsonPrestatgeria = (JSONObject) jsonPrestatgeries.get(prestatgeriaId);

                    
                    Map<String, Object> prestatgeriaData = new HashMap<>();
                    prestatgeriaData.put("id", prestatgeriaId);
                    prestatgeriaData.put("nom", jsonPrestatgeria.get("nom"));
                    prestatgeriaData.put("numFilas", jsonPrestatgeria.get("numFilas"));
                    prestatgeriaData.put("numColumnas", jsonPrestatgeria.get("numColumnas"));

                    
                    JSONArray layoutArray = (JSONArray) jsonPrestatgeria.get("layout");
                    List<List<Map<String, Object>>> layout = new ArrayList<>();
                    for (Object fila : layoutArray) {
                        JSONArray filaArray = (JSONArray) fila;
                        List<Map<String, Object>> filaLayout = new ArrayList<>();
                        for (Object celda : filaArray) {
                            if (celda != null) {
                                JSONObject celdaJson = (JSONObject) celda;
                                Map<String, Object> celdaData = new HashMap<>();
                                celdaData.put("key", celdaJson.get("key")); // Nombre del producto
                                celdaData.put("value", celdaJson.get("value")); // ID del producto
                                filaLayout.add(celdaData);
                            } else {
                                filaLayout.add(null);
                            }
                        }
                        layout.add(filaLayout);
                    }
                    prestatgeriaData.put("layout", layout);

                    Gson gson = new Gson();
                    String prestatgeriaJson = gson.toJson(prestatgeriaData);
                    prestatgeries.add(prestatgeriaJson);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de prestatgeries: " + e.getMessage());
        } catch (ParseException e) {
            throw new PersistenciaException("Error al parsear el archivo JSON de prestatgeries: " + e.getMessage());
        }
        catch(Exception e) {
            throw new PersistenciaException("Error inesperat: " + e.getMessage());
        }
        return prestatgeries;

    }
    
    /**
     * Guarda les prestatgeries en un fitxer JSON associat a un usuari.
     * @param prestatgeries Llista de prestatgeries en format JSON com a cadenes.
     * @param usuari Nom de l'usuari.
     * @throws PersistenciaException Si ocorre un error durant el guardat del fitxer.
     */
    public static void guardarPrestatgeries(List<String> prestatgeries, String usuari) throws PersistenciaException {
        if (prestatgeries != null) {
            JSONObject jsonPrestatgeries = new JSONObject();

            for (String p : prestatgeries) {
                try {
                    JSONObject jsonPres = (JSONObject) new JSONParser().parse(p);

                    String id = (String) jsonPres.get("id");

                    Object disp = jsonPres.get("disp");
                    if (disp instanceof List) {
                        JSONArray jsonDisp = new JSONArray();

                        for (Object fila : (List<?>) disp) {
                            JSONArray jsonFila = new JSONArray();
                            for (Object celda : (List<?>) fila) {
                                if (celda instanceof Map) {
                                    jsonFila.add(new JSONObject((Map<?, ?>) celda));
                                } else {
                                    jsonFila.add(null);
                                }
                            }
                            jsonDisp.add(jsonFila);
                        }

                        jsonPres.put("disp", jsonDisp); // Reemplazar `disp` en el JSON
                    }

                    jsonPrestatgeries.put(id, jsonPres);
                } catch (ParseException e) {
                    throw new PersistenciaException("Error al parsear una prestatgeria en formato JSON: " + e.getMessage());
                }
                catch (Exception e) {
                    throw new PersistenciaException("Error inesperat: " + e.getMessage());
                }
                
            }

            esborrarPrestatgeriesUsuari(usuari);
            String ruta = getRuta(usuari);
            try (FileWriter file = new FileWriter(ruta)) {
                file.write(jsonPrestatgeries.toJSONString());
                file.flush();
            } catch (IOException e) {
                throw new PersistenciaException("Error al guardar el archivo de prestatgeries: " + e.getMessage());
            }
            catch (Exception e) {
                throw new PersistenciaException("Error inesperat: " + e.getMessage());
            }
        } else {
            esborrarPrestatgeriesUsuari(usuari);
        }
    }
    
    /**
     * Esborra el fitxer de prestatgeries associat a un usuari.
     * @param usuari Nom de l'usuari.
     * @return Cert si el fitxer s'ha esborrat amb èxit, fals altrament.
     * @throws PersistenciaException Si ocorre un error durant l'eliminació del fitxer.
     */
    public static boolean esborrarPrestatgeriesUsuari(String usuari) throws PersistenciaException{

       String ruta = getRuta(usuari);

        boolean borrat = false;
        try {
            File arxiu = new File(ruta);
            if (arxiu.exists() && arxiu.isFile()) {
                borrat = arxiu.delete();
            }
        }
        catch (Exception e) {
            throw new PersistenciaException("Error al intentar borrar el archivo de prestatgeries: " + e.getMessage());
        }
        return borrat;
    }

    /**
     * Obté la ruta del fitxer de prestatgeries associat a un usuari.
     * Si el fitxer o la carpeta no existeixen, els crea.
     * @param usuari Nom de l'usuari.
     * @return Ruta del fitxer de prestatgeries.
     * @throws PersistenciaException Si ocorre un error durant la creació del fitxer o la carpeta.
     */
    private static String getRuta(String usuari) throws PersistenciaException{
        String rutaCarpeta ="src/main/resources/persistencia";
        String rutaArxiu = rutaCarpeta + "/" + usuari + "_prestatgeries.json";


        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // Crea la carpeta
        }


        File arxiu = new File(rutaArxiu);
        if (!arxiu.exists()) {
            try {
                arxiu.createNewFile(); // Crea l'arxiu
            } catch (IOException e) {
                throw new PersistenciaException("Error en crear el archivo: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
    
    /**
     * Importa un fitxer de prestatgeria des d'una ruta específica.
     * @param path Ruta del fitxer a importar.
     * @return Llista d'IDs de prestatgeries vàlids.
     * @throws PersistenciaException Si ocorre un error durant la lectura o el processament del fitxer.
     */
    public static List<String> importarFitxerPrestatgeria(String path) throws PersistenciaException {
        List<String> prestatgeriaInfo = new ArrayList<>();

        try {
            FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr);
            if (!sc.hasNextLine()) {
                throw new PersistenciaException("Error: El fitxer està buit.");
            }
            String linea = sc.nextLine();

            if (sc.hasNextLine()) {
                throw new PersistenciaException("Error: El fitxer conté més d'una línia.");
            }

            String[] parts = linea.split("\\s+"); 

            if (parts.length < 3) {
                throw new PersistenciaException("Error: El fitxer no té prou dades. Format esperat: ID Nom NumColumnes Productes...");
            }

            try {
                int id = Integer.parseInt(parts[0]); 
                if (id <= 0) {
                    throw new PersistenciaException("Error: L'ID ha de ser un enter positiu.");
                }
                prestatgeriaInfo.add(String.valueOf(id));

            } catch (NumberFormatException e) {
                throw new PersistenciaException("Error: El camp ID no és vàlid: " + parts[0]);
            }

            prestatgeriaInfo.add(parts[1]);

            try {
                int numCols = Integer.parseInt(parts[2]); 
                if (numCols <= 0) {
                    throw new PersistenciaException("Error: El nombre de columnes ha de ser un enter positiu.");
                }
                prestatgeriaInfo.add(String.valueOf(numCols)); 

            } catch (NumberFormatException e) {
                throw new PersistenciaException("Error: El camp nombre de columnes no és vàlid: " + parts[2]);
            }

            for (int i = 3; i < parts.length; i++) {
                try {
                    int producteId = Integer.parseInt(parts[i]);
                    if (producteId <= 0) {
                        throw new PersistenciaException("Error: L'ID del producte ha de ser un enter positiu: " + parts[i]);
                    }
                    prestatgeriaInfo.add(String.valueOf(producteId)); 
                } catch (NumberFormatException e) {
                    throw new PersistenciaException("Error: Format d'ID de producte no vàlid: " + parts[i]);
                }
            }

            sc.close();
            fr.close();

        } catch (IOException e) {
            throw new PersistenciaException("Error: No s'ha pogut llegir el fitxer: " + e.getMessage());
        }

        return prestatgeriaInfo; 
    }

}
