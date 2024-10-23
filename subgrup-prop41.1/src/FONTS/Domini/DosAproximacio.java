
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class Aresta 
{
    int V1, V2;
    double similitud;

    //Creadora

    Aresta(int v1, int v2, double sim)
    {
        this.V1 = v1;
        this.V2 = v2;
        this.similitud = sim;
    }
}

public class DosAproximacio
{
    private int n_productes;
    private Aresta[] llista_arestes;
    private int[] pare;
    private int[] mida;

    //Constructora

    public DosAproximacio(double[][] matSim, int n)
    {
        this.n_productes = n;
        this.llista_arestes = new Aresta[0];
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

    private void MergeSort(Aresta vec[], int l, int r)
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

    private int Find(int v)
    {
        if (pare[v] != v) return Find(pare[v]);
        return v;
    }

    private void Uneix(int v1, int v2)
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
    
    private List<Aresta> MST()
    {
        MergeSort(llista_arestes, 0, llista_arestes.length - 1);
        List<Aresta> mst = new ArrayList<>();
        int i = 0;
        while (mst.size() < this.n_productes - 1)
        {
            if (Find(llista_arestes[i].V1) != Find(llista_arestes[i].V2))
            {
                mst.add(llista_arestes[i]);
                mst.add(new Aresta(llista_arestes[i].V2, llista_arestes[i].V1, llista_arestes[i].similitud)); //graf dirigit
                Uneix(llista_arestes[i].V1, llista_arestes[i].V2);
            }
        }
        return mst;
    }

    private void dfs(int vertex, bool[] visitat, List<Integer>[] adjacencies, List<Integer> resultat) 
    {
        visitat[vertex] = true;
        resultat.add(vertex);
        
        for (int v = 0; v < adjacencies[vertex].size; ++i) {
            if (!visitat[v]) {
                dfs(v, visitat, adjacencies, resultat);
            }
        }
    }

    public int[] Estanteria2Aproximacio()
    {
        List<Aresta> mst = MST();

        List<Integer>[] adjacencies = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjacencies[i] = new ArrayList<>();
        }
        for(Arista ar : mst)
        {
            djacencies[ar.V1].add(ar.V2);
            adjacencies[ar.V2].add(ar.V1);
        }

        List<Integer> cicleEuleria = new ArrayList<>();
        boolean[] visitat = new boolean[n];
        dfs(0, visitat, adjacencies, cicleEuleria);

        visitat = new boolean[n];
        int[] estanteria_res = new int[0];
        for(int n : cicleEuleria)
        {
            if(!visitat[n])
            {
                visitat[n] = true;
                estanteria_res.add(node);
            }
        }
        return estanteria_res;
    }
}