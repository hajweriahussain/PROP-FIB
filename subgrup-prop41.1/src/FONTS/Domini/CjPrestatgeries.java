package Domini;

import java.util.HashMap;
import java.util.Map;

public class CjPrestatgeries{
    Private Usuari usuari; //cada conj de prestatgeries pertany a un usuari
    Private Map<Integer, Prestatgeria> prestatgeries;

    //constructores
    public CjPrestatgeries(Usuari usuari){
        this.usuari = usuari;
        this.prestatgeries = new HashMap <>();
    }

    public CjPrestatgeries (Usuari u,Integer id_prestatgeria, Prestatgeria p){
        this.usuari = u;
        this.prestatgeries = new HashMap<>();
        this.prestatgeries.put(id_prestatgeria, p);
    }

    public void addPrestatgeria(Prestatgeria p){
        prestatgeries.put(p.getId(), p);
    }

    public Prestatgeria getPrestatgeria(int id) {
        return prestatgeries.get(id);
    }

    public void eliminarPrestatgeria(int id) {
        prestatgeries.remove(id);
    }

    public boolean existeixPrestatgeria(int id) {
        return prestatgeries.containsKey(id);
    }


}

