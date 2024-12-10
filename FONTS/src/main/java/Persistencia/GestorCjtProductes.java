package Persistencia;

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

public class GestorCjtProductes {

    public static List<String> importarProductes(String usuari) {
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

                    JSONArray similitudsArray = (JSONArray) jsonProducte.get("similituds");
                    List<Map<String, Object>> similituds = new ArrayList<>();

                    for (Object obj : similitudsArray) {
                        JSONObject entry = (JSONObject) obj;
                        Map<String, Object> objData = new HashMap<>();
                        objData.put("id", entry.get("id"));   // ID del producte
                        objData.put("similitud", entry.get("similitud"));   // Similitud amb aquest producte
                        similituds.add(objData);
                    }
                    producteInfo.put("similituds", similituds);

                    JSONArray posPrestatgeriesArray = (JSONArray) jsonProducte.get("posPrestatgeries");
                    List<Map<String, Object>> posPrestatgeries = new ArrayList<>();

                    for (Object obj : posPrestatgeriesArray) {
                        JSONObject pos = (JSONObject) obj;
                        Map<String, Object> posData = new HashMap<>();
                        posData.put("id", pos.get("id"));
                        posData.put("fila", pos.get("fila"));
                        posData.put("columna", pos.get("columna"));
                        posPrestatgeries.add(posData);
                    }
                    producteInfo.put("posPrestatgeries", posPrestatgeries);

                    Gson gson = new Gson();
                    String producteJSON = gson.toJson(producteInfo);
                    productes.add(producteJSON);
                }
            }
        } catch (IOException e) {
            System.err.println("Error en accedir al arxiu: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error en parsear l'arxiu JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperat: " + e.getMessage());
        }

        return productes;
    }

    public static void guardarProductes(List<String> productes, String usuari) {
        if (productes != null) {
            Map<String, Object> jsonProductes = new LinkedHashMap<>();
            JSONParser parser = new JSONParser();

            for (String prod : productes) {
                try {
                    JSONObject jsonProducte = (JSONObject) parser.parse(prod);
                    String id = jsonProducte.get("id").toString();

                    Map<String, Object> producteOrdenat = new LinkedHashMap<>();
                    producteOrdenat.put("id", jsonProducte.get("id"));
                    producteOrdenat.put("nom", jsonProducte.get("nom"));
                    producteOrdenat.put("similituds", jsonProducte.get("similituds"));
                    producteOrdenat.put("posPrestatgeries", jsonProducte.get("posPrestatgeries"));

                    jsonProductes.put(String.valueOf(id), producteOrdenat);
                } catch (ParseException e) {
                    System.err.println("Error al parsear el JSON: " + e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            esborrarProductes(usuari);
            String ruta = getRuta(usuari);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter file = new FileWriter(ruta)) {
                file.write(gson.toJson(jsonProductes));
                file.flush();
            } catch (IOException e) {
                System.err.println("Error al guardar l'arxiu: " + e.getMessage());
            }
        } else {
            esborrarProductes(usuari);
        }
    }

    public static boolean esborrarProductes(String usuari) {
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
        String rutaCarpeta = "src/main/java/persistencia";
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
                System.err.println("Error en accedir al arxiu: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
}
