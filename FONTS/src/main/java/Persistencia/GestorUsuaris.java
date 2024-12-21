package Persistencia;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Iterator;
import java.io.*;
import Exceptions.ExceptionFormat;

/**
 * Gestor d'usuaris que gestiona les operacions de creació, eliminació, verificació i modificació de les dades
 * d'usuaris emmagatzemades en un arxiu JSON. Aquesta classe permet afegir nous usuaris, eliminar-los, verificar les
 * contrasenyes i canviar-les.
 * 
 * Tots els mètodes d'aquesta classe poden llançar excepcions del tipus {@link ExceptionFormat} si hi ha un error 
 * relacionat amb l'accés a l'arxiu o amb el format de les dades.
 */
public class GestorUsuaris {

    /**
     * Afegeix un nou usuari amb el nom d'usuari i la contrasenya especificats a l'arxiu JSON.
     * 
     * @param user El nom d'usuari a afegir.
     * @param password La contrasenya de l'usuari.
     * @throws ExceptionFormat Si hi ha un error en l'accés a l'arxiu o en el format de les dades.
     */
    public static void afegirUsuari(String user, String password) throws ExceptionFormat {
        String ruta = getRuta();
        JSONArray jsonArray = new JSONArray();
        
        try (FileReader fr = new FileReader(ruta)) {
            if (new java.io.File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(fr);
                
                if (obj instanceof JSONArray) jsonArray = (JSONArray) obj;
            }

            JSONObject jsonUsuario = new JSONObject();
            jsonUsuario.put("Username", user);
            jsonUsuario.put("Contrasenya", password);
            jsonArray.add(jsonUsuario);

            try (FileWriter fileWriter = new FileWriter(ruta)) {
                fileWriter.write(jsonArray.toJSONString());
            }    
        } catch (IOException e) {
            throw new ExceptionFormat("Error en accedir al arxiu");
        } catch (ParseException e) {
            throw new ExceptionFormat("Error en parsear l'arxiu JSON");
        } catch (Exception e) {
            throw new ExceptionFormat("Error inesperat");
        }
    }
    
    /**
     * Elimina un usuari específic de l'arxiu JSON.
     * 
     * @param user El nom de l'usuari a eliminar.
     * @throws ExceptionFormat Si hi ha un error en l'accés a l'arxiu o en el format de les dades.
     */
    public static void eliminarUsuari(String user) throws ExceptionFormat {
        try {
            String ruta = getRuta();
            JSONParser parser = new JSONParser();
            JSONArray usuarisJson = (JSONArray) parser.parse(new FileReader(ruta));
            
            Iterator<JSONObject> iterator = usuarisJson.iterator();
            while (iterator.hasNext()) {
                JSONObject usuariActual = iterator.next();
                String nomUsuariActual = (String) usuariActual.get("Username");
                
                if (nomUsuariActual != null && nomUsuariActual.equals(user)) {
                    iterator.remove();
                    break;
                }
            }
            
            try (FileWriter file = new FileWriter(ruta)) {
                file.write(usuarisJson.toJSONString());
            }
            
        } catch (IOException e) {
            throw new ExceptionFormat("Error en accedir al arxiu");
        } catch (ParseException e) {
            throw new ExceptionFormat("Error en parsear l'arxiu JSON");
        } catch (Exception e) {
            throw new ExceptionFormat("Error inesperat");
        }
    }
    
    /**
     * Verifica si un usuari existeix a l'arxiu JSON.
     * 
     * @param user El nom de l'usuari a verificar.
     * @return true si l'usuari existeix, false en cas contrari.
     * @throws ExceptionFormat Si hi ha un error en l'accés a l'arxiu o en el format de les dades.
     */
    public static boolean existeixUsuari(String user) throws ExceptionFormat { 
        String ruta = getRuta();
        JSONArray usuarisJson = new JSONArray();
        
        try (FileReader fr = new FileReader(ruta)) {
            if (new java.io.File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(fr);
                
                if (obj instanceof JSONArray) usuarisJson = (JSONArray) obj;
                
                for (Object userJSON : usuarisJson) {
                        JSONObject usuari = (JSONObject) userJSON;
                        String usernameActual = (String) usuari.get("Username");

                        if (usernameActual.equals(user)) {
                            return true;
                        }
                }   
            }
            else return false;
            
        } catch (IOException e) {
            throw new ExceptionFormat("Error en accedir al arxiu");
        } catch (ParseException e) {
            throw new ExceptionFormat("Error en parsear l'arxiu JSON");
        } catch (Exception e) {
            throw new ExceptionFormat("Error inesperat");
        }
        return false;    
    }
    
    /**
     * Verifica si la contrasenya proporcionada és correcta per a un usuari determinat.
     * 
     * @param user El nom de l'usuari per verificar la contrasenya.
     * @param password La contrasenya a verificar.
     * @return true si la contrasenya és correcta, false en cas contrari.
     * @throws ExceptionFormat Si hi ha un error en l'accés a l'arxiu o en el format de les dades.
     */
    public static boolean verificarContrasenya(String user, String password) throws ExceptionFormat {
        String ruta = getRuta();
        JSONArray usuarisJson = new JSONArray();
        
        try (FileReader fr = new FileReader(ruta)) {
            if (new java.io.File(ruta).length() > 0) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(fr);
                
                if (obj instanceof JSONArray) usuarisJson = (JSONArray) obj;
                
                for (Object userJSON : usuarisJson) {
                        JSONObject usuari = (JSONObject) userJSON;
                        String usernameActual = (String) usuari.get("Username");

                        if (usernameActual.equals(user)) {
                            String passwordActual = (String) usuari.get("Contrasenya");
                            if (passwordActual.equals(password)) return true;           
                            else return false;
                        }
                }   
            }
            else return false;
            
        } catch (IOException e) {
            throw new ExceptionFormat("Error en accedir al arxiu");
        } catch (ParseException e) {
            throw new ExceptionFormat("Error en parsear l'arxiu JSON");
        } catch (Exception e) {
            throw new ExceptionFormat("Error inesperat");
        }
        return false;    
    }
    
    /**
     * Canvia la contrasenya d'un usuari. Primer elimina l'usuari existent i després el torna a afegir amb la nova contrasenya.
     * 
     * @param user El nom de l'usuari per al qual es vol canviar la contrasenya.
     * @param novaPassword La nova contrasenya per a l'usuari.
     */
    public static void canviarContrasenya(String user, String novaPassword) {
        try {
            eliminarUsuari(user);
            afegirUsuari(user, novaPassword);
        } catch (ExceptionFormat e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Obté la ruta de l'arxiu JSON que emmagatzema els usuaris.
     * Si l'arxiu no existeix, el crea.
     * 
     * @return La ruta absoluta de l'arxiu JSON d'usuaris.
     */
    private static String getRuta() {
        String rutaCarpeta = "src/main/resources/persistencia";
        String rutaArxiu = rutaCarpeta + "/usuaris.json";

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) carpeta.mkdirs(); // Crea la carpeta
        
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
