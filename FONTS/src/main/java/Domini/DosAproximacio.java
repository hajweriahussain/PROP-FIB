package Domini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DosAproximacio implements GeneradorSolucio {
    private int n;
    private List<Aresta> llista_arestes;
    private int[] pare;
    private int[] mida;
    private int[] prods;
    private double sumaSimilitud;
    private int[] res_productes;
    private double[][] matS;
    private int columnes;
    private int files;
    private Integer[][] mat_res;

    //Constructora

    public DosAproximacio(double[][] matSim, int[] vecPrd, int numCol) {
        this.n = matSim.length;
        this.matS = matSim;
        this.res_productes = new int[n];
        this.llista_arestes = new ArrayList<Aresta>();
        this.pare = new int[n];
        this.mida = new int[n];
        this.prods = vecPrd;
        this.sumaSimilitud = -1; // n -> -1
        this.columnes = numCol;
        if (n%columnes == 0) this.files = n/columnes;
        else this.files = n/columnes + 1;
        this.mat_res = new Integer[files][columnes];

        for(int i = 0; i < n; ++i) {
            this.pare[i] = i;
            this.mida[i] = 1;

        }

        int aux = 0;

        for(int i = 0; i < n; ++i) {
            ++aux;
            for(int j = aux; j < n; ++j) {
                llista_arestes.add(new Aresta(i, j, matSim[i][j]));
            }
        }
    }

    private void mergeSort(Aresta vec[], int l, int r) {
        if (l < r) {
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

            while(i < n1 && j < n2) {
                if(lvec[i].similitud >= rvec[j].similitud) { // <= -> >=
                    vec[k] = lvec[i];
                    ++i;
                }
                else {
                    vec[k] = rvec[j];
                    ++j;
                }
                ++k;
            }

            while (i < n1) {
                vec[k] = lvec[i];
                ++i;
                ++k;
            }
            while (j < n2) {
                vec[k] = rvec[j];
                ++j;
                ++k;
            }
        }
    }

    private int find(int v) {
        if (pare[v] == v) return v;
        else return find(pare[v]);
    }

    private void uneix(int v1, int v2) {
        int pare1 = find(v1);
        int pare2 = find(v2);

        if (pare1 != pare2) {
            if (mida[pare1] <= mida[pare2]) {
               pare[pare1] = pare2;
               mida[pare2] += mida[pare1]; 
            }
            else {
                pare[pare2] = pare1;
                mida[pare1] += mida[pare2]; 
            }
        }
    }

    private List<Aresta> MST() {
        Aresta[] array_arestes = llista_arestes.toArray(new Aresta[llista_arestes.size()]);
        mergeSort(array_arestes, 0, array_arestes.length - 1);
        List<Aresta> mst = new ArrayList<>();
        int i = 0;

        while (mst.size() < n-1 && i < array_arestes.length) {
            if (find(array_arestes[i].V1) != find(array_arestes[i].V2)) {
                mst.add(array_arestes[i]);
                uneix(array_arestes[i].V1, array_arestes[i].V2);
            }
            ++i;
        }
        return mst;
    }
/*
    private void calculaSuma(List<Integer> cicleEuleria, int i, int[] cont, boolean[] visitat, int[] estanteria_res, int contador) {
        int mida = cicleEuleria.size();
        double newSum = 0;
        Producte[] new_producte = new Producte[n];
    
        while (contador < n) {
            if (!visitat[cicleEuleria.get(i)]) {
                if (cont[cicleEuleria.get(i)] > 1) { //hi ha repeticions d'aquest número
                    //recurssió omitint aquest valor
                    int auxi = i;
                    int[] auxcont = Arrays.copyOf(cont, cont.length);
                    boolean[] auxvisitat = Arrays.copyOf(visitat, visitat.length);
                    int[] auxres = Arrays.copyOf(estanteria_res, estanteria_res.length);
                    int auxcontador = contador;
                    double sumaux = newSum;
                    Producte[] newPaux = new_producte;
    
                    cont[cicleEuleria.get(i)] -= 1;
                    i = (i + 1) % mida;
    
                    calculaSuma(cicleEuleria, i, cont, visitat, estanteria_res, contador);
    
                    i = auxi;
                    cont = auxcont;
                    visitat = auxvisitat;
                    estanteria_res = auxres;
                    contador = auxcontador;
                    newSum = sumaux;
                    new_producte = newPaux;
    
                    cont[cicleEuleria.get(i)] = 1;
                    visitat[cicleEuleria.get(i)] = true;
                    estanteria_res[contador] = cicleEuleria.get(i);
                    new_producte[contador] = prods[estanteria_res[contador]];
                    if (contador > 0) {
                        //if (contador == n) newSum += matS[estanteria_res[0]][estanteria_res[n-1]];
                        newSum += matS[estanteria_res[contador - 1]][cicleEuleria.get(i)];
                    }
                    contador++;              
                } 
                else { //només apareix 1 cop, l'afegim
                    visitat[cicleEuleria.get(i)] = true;
                    estanteria_res[contador] = cicleEuleria.get(i);
                    new_producte[contador] = prods[estanteria_res[contador]];
                    if (contador > 0) {
                        //if (contador == n) newSum += matS[estanteria_res[0]][estanteria_res[n-1]];
                        newSum += matS[estanteria_res[contador]][estanteria_res[contador - 1]];
                    }
                    contador++;
                }
            }
            i = (i + 1) % mida;
        }
        newSum += matS[estanteria_res[0]][estanteria_res[n-1]];
    /*
        for (int k = 0; k < n; ++k) {
            new_producte[k] = prods[estanteria_res[k]];
            if (k == n - 1) newSum += matS[estanteria_res[k]][estanteria_res[0]];
            else newSum += matS[estanteria_res[k]][estanteria_res[k + 1]];
        }
    
        if (newSum > sumaSimilitud) { // < -> >
            sumaSimilitud = newSum;
            res_productes = new_producte;
        }
    }
    */
    
     private void calculaSuma(List<Integer> cicleEuleria, int i, int[] cont, boolean[] visitat, int[] estanteria_res, int contador) {
        int mida = cicleEuleria.size();
    
        while (contador < n) {
            if (!visitat[cicleEuleria.get(i)]) {
                if (cont[cicleEuleria.get(i)] > 1) { //hi ha repeticions d'aquest número
                    //recurssió omitint aquest valor
                    int auxi = i;
                    int[] auxcont = Arrays.copyOf(cont, cont.length);
                    boolean[] auxvisitat = Arrays.copyOf(visitat, visitat.length);
                    int[] auxres = Arrays.copyOf(estanteria_res, estanteria_res.length);
                    int auxcontador = contador;
    
                    cont[cicleEuleria.get(i)] -= 1;
                    i = (i + 1) % mida;
    
                    calculaSuma(cicleEuleria, i, cont, visitat, estanteria_res, contador);
    
                    i = auxi;
                    cont = auxcont;
                    visitat = auxvisitat;
                    estanteria_res = auxres;
                    contador = auxcontador;
    
                    cont[cicleEuleria.get(i)] = 1;
                    visitat[cicleEuleria.get(i)] = true;
                    estanteria_res[contador] = cicleEuleria.get(i);
                    contador++;                
                } 
                else { //només apareix 1 cop, l'afegim
                    visitat[cicleEuleria.get(i)] = true;
                    estanteria_res[contador] = cicleEuleria.get(i);
                    contador++;
                }
            }
            i = (i + 1) % mida;
        }
    
        double newSum = 0;
       int[] new_producte = new int[n];
        for (int k = 0; k < n; ++k) {
            new_producte[k] = prods[estanteria_res[k]];
            if (k == n - 1) newSum += matS[estanteria_res[k]][estanteria_res[0]];
            else newSum += matS[estanteria_res[k]][estanteria_res[k + 1]];
        }
    
        if (newSum > sumaSimilitud) { // < -> >
            sumaSimilitud = newSum;
            res_productes = new_producte;
        }
    }


    private List<Integer> findEuleria(List<List<Integer>> adjacencies) {
        List<Integer> cicleEuleria = new ArrayList<>();
        Stack<Integer> pila = new Stack<>();
        pila.push(0);
    
        while (!pila.isEmpty()) {
            int u = pila.peek();
            if (adjacencies.get(u).isEmpty()) {
                cicleEuleria.add(u);
                pila.pop();
            } else {
                int v = adjacencies.get(u).remove(0);
                pila.push(v);
            }
        }
        return cicleEuleria;
    }

    public Integer[][] generarLayout() {
        //Construir mst
        List<Aresta> mst = MST();
        //Construir llista d'adjacències amb doble aresta
        List<List<Integer>> adjacencies = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacencies.add(new ArrayList<>());
        }

        for (Aresta ar : mst) {
            adjacencies.get(ar.V1).add(ar.V2);
            adjacencies.get(ar.V2).add(ar.V1);
        }
        //Cicle Eulerià (algorisme de Hierholzer)
        List<Integer> cicleEuleria = findEuleria(adjacencies);
        int[] contador = new int[n];
        Arrays.fill(contador, 0);

        for (int v = 0; v < cicleEuleria.size(); ++v) {
            contador[cicleEuleria.get(v)] += 1;
        }

        boolean[] visitat = new boolean[n];
        Arrays.fill(visitat, false);
        int[] estanteria_res = new int[n];
        calculaSuma(cicleEuleria, 0, contador, visitat, estanteria_res, 0);

        //muntem la matriu resultat
        int k = 0;
        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes && k < n; ++j) {
                if(i%2 == 0) { //del dret
                    mat_res[i][j] = res_productes[k];
                }
                else { //del reves
                    mat_res[i][(columnes-1)-j] = res_productes[k];
                }
                ++k;
            }
        }

        return mat_res;

    }

    public double getMillorSimilitud() {
        return sumaSimilitud;

    }

    public Integer[][] getResultat() {
        return mat_res;
    }
}