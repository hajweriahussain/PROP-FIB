package Domini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class DosAproximacio implements GenerarSolucio
{
    private int n;
    private List<Aresta> llista_arestes;
    private int[] pare;
    private int[] mida;
    private Producte[] prods;
    private double sumaSimilitud;
    private Producte[] res_productes;

    //Constructora

    public DosAproximacio(double[][] matSim, Producte[] vecPrd)
    {
        this.n = matSim.length;
        this.res_productes = new Producte[n];
        this.llista_arestes = new ArrayList<Aresta>();
        this.pare = new int[n];
        this.mida = new int[n];
        this.prods = vecPrd;
        this.sumaSimilitud = 0;

        for(int i = 0; i < n; ++i)
        {
            this.pare[i] = i;
            this.mida[i] = 1;

        }

        int aux = 0;

        for(int i = 0; i < n; ++i)
        {
            ++aux;
            for(int j = aux; j < n; ++j)
            {
                llista_arestes.add(new Aresta(i, j, matSim[i][j]));
            }
        }
    }

    private void mergeSort(Aresta vec[], int l, int r)
    {
        if (l < r)
        {
            int centre = (l + r) / 2;
            mergeSort(vec, l, centre);
            mergeSort(vec, centre+1, r);

            int n1 = centre - l + 1;
            int n2 = r - centre;

            Aresta lvec[] = new Aresta [n1];
            Aresta rvec[] = new Aresta [n2];

            for(int i = 0; i < n1; ++i) lvec[i] = vec[l+i];
            for(int i = 0; i < n2; ++i) rvec[i] = vec[centre+i+1];

            int i = 0;
            int j = 0;
            int k = l;
            while(i < n1 && j < n2)
            {
                if(lvec[i].similitud <= rvec[j].similitud)
                {
                    vec[k] = lvec[i];
                    ++i;
                }
                else
                {
                    vec[k] = rvec[j];
                    ++j;
                }
                ++k;
            }

            while (i < n1)
            {
                vec[k] = lvec[i];
                ++i;
                ++k;
            }
            while (j < n2)
            {
                vec[k] = rvec[j];
                ++j;
                ++k;
            }
        }
    }

    private int find(int v)
    {
        if (pare[v] == v) return v;
        else return find(pare[v]);
    }

    private void uneix(int v1, int v2)
    {
        int pare1 = find(v1);
        int pare2 = find(v2);

        if (pare1 != pare2)
        {
            if (mida[pare1] <= mida[pare2])
            {
               pare[pare1] = pare2;
               mida[pare2] += mida[pare1]; 
            }
            else 
            {
                pare[pare2] = pare1;
                mida[pare1] += mida[pare2]; 
            }
        }
    }

    private List<Aresta> MST()
    {
        Aresta[] array_arestes = llista_arestes.toArray(new Aresta[llista_arestes.size()]);
        mergeSort(array_arestes, 0, array_arestes.length - 1);
        List<Aresta> mst = new ArrayList<>();
        int i = 0;

        while (mst.size() < n-1 && i < array_arestes.length)
        {
            if (find(array_arestes[i].V1) != find(array_arestes[i].V2))
            {
                mst.add(array_arestes[i]);
                //mst.add(new Aresta(array_arestes[i].V2, array_arestes[i].V1, array_arestes[i].similitud)); //graf dirigit
                uneix(array_arestes[i].V1, array_arestes[i].V2);
            }
            ++i;
        }
        return mst;
    }

    private double obteSimilitud(int v1, int v2) {
        for (Aresta ar : llista_arestes) {
            if ((ar.V1 == v1 && ar.V2 == v2) || (ar.V1 == v2 && ar.V2 == v1)) {
                return ar.similitud;
            }
        }
        return 0; //en principi mai ha de retornar 0
    }

    private List<Integer> findEuleria(int start, List<Integer>[] adjacencies) 
    {
        List<Integer> cicleEuleria = new ArrayList<>();
        Stack<Integer> pila = new Stack<>();
        pila.push(start);
    
        while (!pila.isEmpty()) {
            int u = pila.peek();
            if (adjacencies[u].isEmpty()) {
                cicleEuleria.add(u);
                pila.pop();
            } else {
                int v = adjacencies[u].remove(0);
                adjacencies[v].remove(Integer.valueOf(u));
                pila.push(v);
            }
        }
        return cicleEuleria;
    }
     

    public Producte[] generarLayout()
    {
        //Construir mst
        List<Aresta> mst = MST();

        //Construir llista d'adjacències amb doble aresta
        List<Integer>[] adjacencies = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjacencies[i] = new ArrayList<>();
        }
        for(Aresta ar : mst)
        {
            adjacencies[ar.V1].add(ar.V2);
            adjacencies[ar.V2].add(ar.V1);
        }

        //Cicle Eulerià (algorisme de Hierholzer)
        List<Integer> cicleEuleria = findEuleria(0, adjacencies);

        //Eliminar repetits
        boolean[] visitat = new boolean[n];
        int[] estanteria_res = new int[n];
        int i = 0;
        for (Integer v : cicleEuleria) {
            if (!visitat[v]) {
                visitat[v] = true;
                estanteria_res[i] = v;
                ++i;
            }
        }
        for (int k = 0; k < n; ++k)
        {
            res_productes[k] = prods[estanteria_res[k]];
            if(k == n-1) sumaSimilitud += obteSimilitud(estanteria_res[k], estanteria_res[0]);
            else sumaSimilitud += obteSimilitud(estanteria_res[k], estanteria_res[k+1]);
        }
        return res_productes;
    }

    public double getMillorSimilitud()
    {
        return sumaSimilitud;

    }

    public Producte[] getEstanteria()
    {
        return res_productes;
    }
}