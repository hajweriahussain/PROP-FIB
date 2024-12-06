package Persistencia;

public class CtrlPersistencia {

    public CtrlPersistencia() {

    }
    
    public boolean existeixUsuari(String user) {
        return GestorUsuaris.existeixUsuari(user);
    }
    
    public void afegirUsuari(String user, String password){
        GestorUsuaris.afegirUsuari(user,password);
    }
    
    public void eliminarUsuari(String user) { //falta completar para los otros gestores
        GestorUsuaris.eliminarUsuari(user);
        //......
    }
    
    public boolean verificarContrasenya(String user, String password){
        return GestorUsuaris.verificarContrasenya(user,password);
    }

}