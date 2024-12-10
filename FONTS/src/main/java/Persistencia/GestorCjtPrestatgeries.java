package Persistencia;

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
 *
 * @author hajweria
 */
public class GestorCjtPrestatgeries {
    public static List<String> importarPrestatgeria(String usuari) {
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
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return prestatgeries;

    }
    
    public static void guardarPrestatgeries(List<String> prestatgeries, String usuari) {
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
                    System.err.println("Error al parsear la estantería: " + p);
                    e.printStackTrace();
                }
            }

            borrarPrestatgeriesUsuari(usuari);
            String ruta = getRuta(usuari);
            try (FileWriter file = new FileWriter(ruta)) {
                file.write(jsonPrestatgeries.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            borrarPrestatgeriesUsuari(usuari);
        }
}
    
    public static boolean borrarPrestatgeriesUsuari(String usuari) {

       String ruta = getRuta(usuari);

        boolean borrat = false;
        try {
            File arxiu = new File(ruta);
            if (arxiu.exists() && arxiu.isFile()) {
                borrat = arxiu.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return borrat;
    }

    
    private static String getRuta(String usuari) {
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
                System.err.println("Error en crear l'arxiu: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
    
    

    
    
}
