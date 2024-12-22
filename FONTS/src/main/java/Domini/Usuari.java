package Domini;

/**
 * La classe {@code Usuari} representa un usuari dins del sistema amb un nom d'usuari 
 * i una contrasenya. Permet obtenir i modificar aquestes dades.
 */
public class Usuari {
    private String username;
    private String contrasenya;

    /**
     * Constructor per crear un objecte {@code Usuari} amb un nom d'usuari i una contrasenya.
     *
     * @param name El nom d'usuari per identificar l'usuari dins del sistema.
     * @param contra La contrasenya associada a l'usuari per autenticar-lo.
     */
    public Usuari(String name, String contra) {
        username = name;
        contrasenya = contra;
    }

    /**
     * Retorna el nom d'usuari de l'usuari.
     *
     * @return El nom d'usuari de l'usuari.
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Retorna la contrasenya de l'usuari.
     *
     * @return La contrasenya de l'usuari.
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /**
     * Modifica el nom d'usuari de l'usuari.
     *
     * @param username El nou nom d'usuari per a l'usuari.
     */
    public void canviaUsername(String username) {
        this.username = username;
    }
    
    /**
     * Modifica la contrasenya de l'usuari.
     *
     * @param contra La nova contrasenya per a l'usuari.
     */
    public void canviaContrasenya(String contra) {
        contrasenya = contra;
    }
}
