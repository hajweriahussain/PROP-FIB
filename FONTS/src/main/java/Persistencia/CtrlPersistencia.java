package Persistencia;

import java.util.List;

public class CtrlPersistencia {

    public CtrlPersistencia() {

    }
    
    public boolean existeixUsuari(String user) {
        return GestorUsuaris.existeixUsuari(user);
    }
    
    public void afegirUsuari(String user, String password){
        GestorUsuaris.afegirUsuari(user,password);
    }
    
    public void eliminarUsuari(String user) { 
        GestorUsuaris.eliminarUsuari(user);
        GestorCjtProductes.esborrarProductes(user);
        GestorCjtPrestatgeries.esborrarPrestatgeriesUsuari(user);
    }

    public void canviarContrasenya(String user, String novaPassword){
        GestorUsuaris.canviarContrasenya(user,novaPassword);
    }
    
    public boolean verificarContrasenya(String user, String password){
        return GestorUsuaris.verificarContrasenya(user,password);
    }
    
    public List<String> importarPrestatgeria(String usuari){
        return GestorCjtPrestatgeries.importarPrestatgeries(usuari);
    }
    
    public void guardarPrestatgeries(List<String> prestatgeries, String usuari){
        GestorCjtPrestatgeries.guardarPrestatgeries(prestatgeries,usuari);
    }

    public List<String> importarFitxerPrestatgeria(String path){
        return GestorCjtPrestatgeries.importarFitxerPrestatgeria(path);
    }
    
    public List<String> importarProductes(String usuari){
        return GestorCjtProductes.importarProductes(usuari);
    }
    
    public void guardarProductes(List<String> productes, String usuari){
        GestorCjtProductes.guardarProductes(productes, usuari);
    }
   /* 
    public List<String> importarFitxerProductes(String path){
        return GestorCjtProductes.importarFitxerProductes(path);
    }
    */

}
