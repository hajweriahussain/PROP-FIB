package domain;

import java.util.Vector;
import java.util.HashMap;

public class Estanteria
{
    //Datos
   // private Map<Int, Map<Int, Int>> mapSimilituds;  // Map<idProd, Map<Similitud, idProd>> Mapa con el id del producto 
                                                    //como clave,cada clave contiene un mapa con todas las similitudes 
                                                    //de los demás productos con su respectivo id;
    private int[] vecEstanteria; //vector amb la estanteria resultant, cada hueco té el id del producte

    private int[] idProductes; //vector de les relacións on es guarden els ids dels productes
    
    //Constructor ?
    public Estanteria() {
		this.mapSimilituds = new HashMap<Int, HashMap<Int, Int>>();
        this.vecEstanteria = new String[0];
        this.idProductes = new int[0];
	}

    //Mètodes Públics

    public AfegirProducte(Producte p)
    {
        for(int i = 0; i < idProductes.size(); ++i)
        {
            mapSimilituds[idProductes[i]].put(p.mapSim[idProductes[i]], p.id);
        }
        idProductes.add(p.id);
        mapSimilituds.put(p.mapSim, p.id);
    }

    //veure similitud (1 prod vs tots)

    //veure similitud (2 prods)

    public Intercanviar2Productes (int id1, int id2)
    {
        int i_aux;
        int done = 0;
        for (int i = 0; i < vecEstanteria.size(x    ); ++i)
        {
            if(done == 0) 
            {
                if (vecEstanteria[i] == id1 || vecEstanteria[i] == id2) 
                {
                aux = i;
                done = 1;
                }
            }
            else
            {
                if (vecEstanteria[i] == id1 || vecEstanteria[i] == id2) 
                {
                int id_aux = vecEstanteria[i];
                vecEstanteria[i] = vecEstanteria[aux];
                vecEstanteria[aux] = id_aux;
                break;
                }  
            }           
        }
    }

}