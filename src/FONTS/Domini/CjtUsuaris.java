package Domini;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CjtUsuaris {
    private String idCjtUsuaris;
    private Map<String, Usuari> usuaris;

    public CjtUsuaris(String id) {
        idCjtUsuaris = id;
        usuaris = new HashMap<>();
    }

    public CjtUsuaris(String id, String nomUsuari) {
        idCjtUsuaris = id;
        usuaris = new HashMap<>();
        usuaris.put(nomUsuari, null);
    }

    public CjtUsuaris(String id, String nomUsuari, String contrasenya) {
        idCjtUsuaris = id;
        Usuari usr = new Usuari(nomUsuari, contrasenya);
        usuaris = new HashMap<>();
        usuaris.put(nomUsuari, usr);
    }

    public CjtUsuaris(String id, Usuari usr) {
        idCjtUsuaris = id;
        String nomUsuari = usr.getUsername();
        usuaris = new HashMap<>();
        usuaris.put(nomUsuari, usr);
    }


    public String getIdCjtUsuaris() {
        return idCjtUsuaris;
    }

    public Set<String> getUsuaris() {
        return usuaris.keySet();
    }

    public Usuari getUsuari(String nomUsuari) {
        return usuaris.get(nomUsuari);
    }

    public void setIdCjtUsuaris(String id) {
        this.idCjtUsuaris = id;
    }


    public void crearUsuari(String nomUsuari, String contrasenya) {
        Usuari usr = new Usuari(nomUsuari, contrasenya);
        usuaris.put(nomUsuari, usr);
    }

    public void crearUsuari(Usuari usr) {
        String nomUsuari = usr.getUsername();
        usuaris.put(nomUsuari, usr);
    }

    public void modificarUsuari(String antic_nomUsuari, String nou_nomUsuari, String nova_contrasenya) {
        Usuari usr = getUsuari(antic_nomUsuari);
        usr.canviaUsername(nou_nomUsuari);
        usr.canviaContrasenya(nova_contrasenya);
        usuaris.remove(antic_nomUsuari);
        usuaris.put(nou_nomUsuari, usr);
    }

    public void modificarNomUsuari(String antic_nomUsuari, String nou_nomUsuari) {
        Usuari usr = getUsuari(antic_nomUsuari);
        usr.canviaUsername(nou_nomUsuari);
        usuaris.remove(antic_nomUsuari);
        usuaris.put(nou_nomUsuari, usr);
    }

    public void modificarContrasenya(String nomUsuari, String nova_contrasenya) {
        Usuari usr = getUsuari(nomUsuari);
        usr.canviaContrasenya(nova_contrasenya);
    }

    public void eliminarUsuari(String nomUsuari) {
        usuaris.remove(nomUsuari);
    }

    public void eliminarTotsUsuaris() {
        usuaris.clear();
    }
}
