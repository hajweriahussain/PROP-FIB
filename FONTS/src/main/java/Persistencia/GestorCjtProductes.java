package Persistencia;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Exceptions.ExceptionFormat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GestorCjtProductes {

    public static List<String> importarProductes(String usuari) throws ExceptionFormat {
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
            throw new ExceptionFormat("Error en accedir al arxiu: " + e.getMessage());
        } catch (ParseException e) {
            throw new ExceptionFormat("Error en parsear l'arxiu JSON: " + e.getMessage());
        } catch (Exception e) {
            throw new ExceptionFormat("Error inesperat: " + e.getMessage());
        }

        return productes;
    }

    public static void guardarProductes(List<String> productes, String usuari) throws ExceptionFormat {
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
                    throw new ExceptionFormat("Error al parsear el JSON: " + e.getMessage());
                } catch (Exception e) {
                    throw new ExceptionFormat("Error inesperat al guardar productes");
                }
            }

            esborrarProductes(usuari);
            String ruta = getRuta(usuari);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter file = new FileWriter(ruta)) {
                file.write(gson.toJson(jsonProductes));
                file.flush();
            } catch (IOException e) {
                throw new ExceptionFormat("Error al guardar l'arxiu: " + e.getMessage());
            }
        } else {
            esborrarProductes(usuari);
        }
    }

    public static boolean esborrarProductes(String usuari) throws ExceptionFormat {
        String ruta = getRuta(usuari);
        boolean borrat = false;

        try {
            File arxiu = new File(ruta);
            if (arxiu.exists() && arxiu.isFile()) {
                borrat = arxiu.delete();
            }
        }
        catch (Exception e) {
            throw new ExceptionFormat("Error inesperat al esborrar productes");
        }

        return borrat;
    }

    private static String getRuta(String usuari) throws ExceptionFormat {
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
                throw new ExceptionFormat("Error en accedir al arxiu: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
    
    public static List<String> importarFitxerProducte(String path) throws ExceptionFormat {
        List<String> producteData = new ArrayList<>();

        try (FileReader fr = new FileReader(path);
            Scanner sc = new Scanner(fr)) {

            if (!sc.hasNextLine()) {
                throw new ExceptionFormat("Error: El fitxer està buit.");
            }

            String linea = sc.nextLine();
            String[] parts = linea.split("\\s+");

            if (parts.length < 2) {
                throw new ExceptionFormat("Error: La línia no conté suficients dades (ID i nom).");
            }

            int id;
            try {
                id = Integer.parseInt(parts[0]);
                if (id <= 0) {
                    throw new ExceptionFormat("Error: ID no vàlid (ha de ser major que 0): " + parts[0]);
                }
            } catch (NumberFormatException e) {
                throw new ExceptionFormat("Error: Format incorrecte per a l'ID: " + parts[0]);
            }

            String nom = parts[1];

            producteData.add(parts[0]);
            producteData.add(nom);

            while (sc.hasNextLine()) {
                String similitudLine = sc.nextLine();
                String[] similitudParts = similitudLine.split("\\s+");

                if (similitudParts.length != 2) {
                    throw new ExceptionFormat("Error: La línia de similitud no és vàlida: " + similitudLine);
                }

                try {
                    int similitudId = Integer.parseInt(similitudParts[0]);
                    double similitudValue = Double.parseDouble(similitudParts[1]);

                    if (similitudValue < 0 || similitudValue > 1) {
                        throw new ExceptionFormat("Error: La similitud ha de ser un valor entre 0 i 1: " + similitudValue);
                    }

                    producteData.add(similitudId + ": " + similitudValue);
                } catch (NumberFormatException e) {
                    throw new ExceptionFormat("Error: Format incorrecte per a la similitud: " + similitudLine);
                }
            }

            sc.close();
            fr.close();

        } catch (IOException e) {
            throw new ExceptionFormat("Error: No s'ha pogut llegir el fitxer: " + e.getMessage());
        }

        return producteData;
    }
}
