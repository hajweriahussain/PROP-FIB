package Domini;

public class BruteForceAlg {

    private float[][] matSimilituds;
    private Producte[] vecProductes;
    private float millorSimilitud;
    private Producte[] vecResultat;

    // Constructor
    public BruteForceAlg(float[][] matSim, Producte[] vecPrd) {
        matSimilituds = matSim;
        vecProductes = vecPrd;
        millorSimilitud = -1.0f;
        vecResultat = new Producte[matSimilituds.length];
    }

    private static int factorial(int n) {
        int resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
}

    private float calcularSimilitudTotal(Producte[] vecActualProd) {
        float total = 0.0f;
        int n = vecActualProd.length;
        for (int i = 0; i < n - 1; i++) {
            Producte producte1 = vecActualProd[i];
            Producte producte2 = vecActualProd[i + 1];
            total += matSimilituds[producte1.getId()][producte2.getId()];
        }
        // como es estanteria circular miramos el ultimo producto con el primero
        Producte producte1 = vecActualProd[n - 1];
        Producte producte2 = vecActualProd[0];
        total += matSimilituds[producte1.getId()][producte2.getId()];

        return total;
    }

    private void swap(Producte[] arr, int i, int j) {
        Producte temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void permutacions(Producte[] vProd, int L, int R, float millorSim, Producte[] vecRes, int[] numPermutacions) {
        if(numPermutacions[0] == 0) return;
        if (L == R) {

            float simActual = calcularSimilitudTotal(vProd);
            if (simActual > millorSimilitud) {
                millorSimilitud = simActual;
                System.arraycopy(vProd, 0, vecResultat, 0, vProd.length);
            }
            numPermutacions[0]--;


        } else {
            for (int i = L; i <= R; i++) {
                swap(vProd, L, i);
                permutacions(vProd, L + 1, R, millorSimilitud, vecResultat,numPermutacions);
                swap(vProd, L, i);
            }
        }

    }

    public void algoritme(){
        int r = vecProductes.length - 1;
        int[] numPermutacions = {factorial(r)/2};
        //llamamos con l=1 para que siempre empieze con el mismo producto
        permutacions(vecProductes,1,r,millorSimilitud,vecResultat,numPermutacions);
    }

    public Producte[] getResultat(){
        return vecResultat;
    }

    public float getMillorSimilitud(){
        return millorSimilitud;
    }


}
