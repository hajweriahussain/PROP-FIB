package Persistencia;

import java.util.List;
import Exceptions.ExceptionFormat;

public class CtrlPersistencia {

    public CtrlPersistencia() {

    }
    
    public boolean existeixUsuari(String user) throws ExceptionFormat{
        return GestorUsuaris.existeixUsuari(user);
    }
    
    public void afegirUsuari(String user, String password) throws ExceptionFormat{
        GestorUsuaris.afegirUsuari(user,password);
    }
    
    public void eliminarUsuari(String user) throws ExceptionFormat{ 
        GestorUsuaris.eliminarUsuari(user);
        GestorCjtProductes.esborrarProductes(user);
        GestorCjtPrestatgeries.esborrarPrestatgeriesUsuari(user);
    }

    public void canviarContrasenya(String user, String novaPassword) throws ExceptionFormat{
        GestorUsuaris.canviarContrasenya(user,novaPassword);
    }
    
    public boolean verificarContrasenya(String user, String password) throws ExceptionFormat{
        return GestorUsuaris.verificarContrasenya(user,password);
    }
    
    public List<String> importarPrestatgeries(String usuari) throws ExceptionFormat{
        return GestorCjtPrestatgeries.importarPrestatgeries(usuari);
    }
    
    public void guardarPrestatgeries(List<String> prestatgeries, String usuari) throws ExceptionFormat{
        GestorCjtPrestatgeries.guardarPrestatgeries(prestatgeries,usuari);
    }

    public List<String> importarFitxerPrestatgeria(String path) throws ExceptionFormat{
        return GestorCjtPrestatgeries.importarFitxerPrestatgeria(path);
    }
    
    public List<String> importarProductes(String usuari) throws ExceptionFormat{
        return GestorCjtProductes.importarProductes(usuari);
    }
    
    public void guardarProductes(List<String> productes, String usuari) throws ExceptionFormat{
        GestorCjtProductes.guardarProductes(productes, usuari);
    }
    
    public List<String> importarFitxerProducte(String path) throws ExceptionFormat{
        return GestorCjtProductes.importarFitxerProducte(path);
    }
   

}
