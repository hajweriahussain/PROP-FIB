package Domini;

public class Usuari {
    private String username;
    private String contrasenya;

    public Usuari (String name, String contra){
        username = name;
        contrasenya = contra;
    }

    public Usuari getUsuari(){
        return this;
    }

    public String getUsername(){
        return username;
    }
    public String getContrasenya(){
        return contrasenya;
    }

    public void canviaUsername(String username){
        this.username = username;
    }
    public void canviaContrasenya(String contra){
        contrasenya = contra;
    }
}

