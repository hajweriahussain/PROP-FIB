package Domini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class Aresta 
{
    int V1, V2;
    double similitud;

    //Constructora

    Aresta(int v1, int v2, double sim)
    {
        this.V1 = v1;
        this.V2 = v2;
        this.similitud = sim;
    }
}

public class DosAproximacio
{
    private int n;
    public List<Aresta> llista_arestes; //posar a priv
    private int[] pare;
    private int[] mida;

    //Constructora

    public DosAproximacio(double[][] matSim)
    {
        this.n = matSim.length;
        this.llista_arestes = new ArrayList<Aresta>();
        this.pare = new int[n];
        this.mida = new int[n];

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

    public void MergeSort(Aresta vec[], int l, int r)
    {
        if (l < r)
        {
            int centre = (l + r) / 2;
            MergeSort(vec, l, centre);
            MergeSort(vec, centre+1, r);

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

    public int Find(int v)
    {
        if (pare[v] == v) return v;
        else return Find(pare[v]);
    }

    public void Uneix(int v1, int v2)
    {
        int pare1 = Find(v1);
        int pare2 = Find(v2);

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

    public List<Aresta> MST()
    {
        Aresta[] array_arestes = llista_arestes.toArray(new Aresta[llista_arestes.size()]);
        MergeSort(array_arestes, 0, array_arestes.length - 1);
        List<Aresta> mst = new ArrayList<>();
        int i = 0;

        while (mst.size() < n-1 && i < array_arestes.length)
        {
            if (Find(array_arestes[i].V1) != Find(array_arestes[i].V2))
            {
                mst.add(array_arestes[i]);
                //mst.add(new Aresta(array_arestes[i].V2, array_arestes[i].V1, array_arestes[i].similitud)); //graf dirigit
                Uneix(array_arestes[i].V1, array_arestes[i].V2);
            }
            ++i;
        }
        return mst;
    }

    public List<Integer> findEuleria(int start, List<Integer>[] adjacencies) 
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
     

    public int[] Estanteria2Aproximacio()
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
        return estanteria_res;
    }
}