package Persistencia;

import Domini.Producte;
import Domini.CjtProductes;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GestorCjtProductes {

    public static CjtProductes importarProductes(String usuari) {
        CjtProductes cjtProductes = new CjtProductes(usuari);
        String ruta = getRuta(usuari);  // Obtenir la ruta de l'arxiu JSON corresponent a l'usuari

        try (FileReader fr = new FileReader(ruta)) {
            if (new File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                JSONObject jsonProductes = (JSONObject) parser.parse(fr);

                for (Object key : jsonProductes.keySet()) {
                    String idString = (String) key; // Clau del producte en format String
                    JSONObject jsonProducte = (JSONObject) jsonProductes.get(idString);

                    // Extreure les dades del producte del JSON
                    int id = Integer.parseInt(idString);
                    String nom = (String) jsonProducte.get("nom");
                    JSONArray similitudsArray = (JSONArray) jsonProducte.get("similituds");

                    Map<Integer, Double> similituds = new HashMap<>();
                    for (Object obj : similitudsArray) {
                        JSONObject entry = (JSONObject) obj;
                        int idSimilitud = ((Long) entry.get("id")).intValue();
                        double valor = (Double) entry.get("valor");
                        similituds.put(idSimilitud, valor);
                    }

                    Producte producte = new Producte(id, nom, similituds);
                    cjtProductes.afegirProducte(producte);
                }
            }
        } catch (IOException e) {
            System.err.println("Error en accedir al arxiu: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error en parsear l'arxiu JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperat: " + e.getMessage());
        }

        return cjtProductes;
    }

    public static void guardarProductes(CjtProductes cjtProductes, String usuari) {
        if (cjtProductes != null) {
            JSONObject jsonProductes = new JSONObject();

            for (Producte prod : cjtProductes.getVecProductes()) {
                JSONObject jsonProducte = new JSONObject();
                jsonProducte.put("nom", prod.getNom());

                JSONArray similitudsArray = new JSONArray();
                for (Map.Entry<Integer, Double> entry : prod.getSimilituds().entrySet()) {
                    JSONObject similitud = new JSONObject();
                    similitud.put("id", entry.getKey());
                    similitud.put("valor", entry.getValue());
                    similitudsArray.add(similitud);
                }
                jsonProducte.put("similituds", similitudsArray);

                jsonProductes.put(String.valueOf(prod.getId()), jsonProducte);
            }

            esborrarProductes(usuari);
            String ruta = getRuta(usuari);

            try (FileWriter file = new FileWriter(ruta)) {
                file.write(jsonProductes.toJSONString());
            } catch (IOException e) {
                System.err.println("Error al guardar l'arxiu: " + e.getMessage());
            }
        } else {
            esborrarProductes(usuari);
        }
    }

    public static boolean esborrarProductes(String usuari) {
        String ruta = getRuta(usuari);
        File arxiu = new File(ruta);

        if (!arxiu.exists()) {
            System.err.println("L'arxiu no existeix: " + ruta);
            return false;
        }

        try {
            if (arxiu.delete()) {
                return true;
            }
            else {
                System.err.println("No s'ha pogut eliminar l'arxiu: " + ruta);
                return false;
            }
        } catch (SecurityException e) {
            System.err.println("Permissos insuficients per eliminar l'arxiu: " + ruta);
            return false;
        }
    }

    private static String getRuta(String usuari) {
        String rutaCarpeta = "src/main/java/persistencia";
        String rutaArxiu = rutaCarpeta + "/" + usuari + "_Productes.json";

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                System.err.println("No s'ha pogut crear la carpeta: " + rutaCarpeta);
            }
        }

        File arxiu = new File(rutaArxiu);
        if (!arxiu.exists()) {
            try {
                if (arxiu.createNewFile()) {
                    System.out.println("Arxiu creat: " + rutaArxiu);
                }
            } catch (IOException e) {
                System.err.println("Error en crear l'arxiu: " + e.getMessage());
            }
        }

        return rutaArxiu;
    }
}