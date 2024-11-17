package Domini;

public class BruteForce implements GeneradorSolucio {

    private double[][] matSimilituds;
    private Producte[] vecProductes;
    private double millorSimilitud;
    private Producte[] vecResultat;

    // Constructor
    public BruteForce(double[][] matSim, Producte[] vecPrd) {
        matSimilituds = matSim;
        vecProductes = vecPrd;
        millorSimilitud = -1.0;
        vecResultat = new Producte[vecProductes.length];
    }

    private static int factorial(int n) {
        int resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
     }

    private double calcularSimilitudTotal(int[] vecActualProd) {
        double total = 0.0;
        int n = vecActualProd.length;

        for (int i = 0; i < n - 1; i++) {
            int index1 = vecActualProd[i];
            int index2 = vecActualProd[i + 1];
            total += matSimilituds[index1][index2];
        }
        int index1 = vecActualProd[n - 1];
        int index2 = vecActualProd[0];
        total += matSimilituds[index1][index2];

        return total;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void permutacions(int[] vProd, int L, int R, int[] numPermutacions) {
        if(numPermutacions[0] == 0) return;
        if (L == R) {
            double simActual = calcularSimilitudTotal(vProd);
            if (simActual > millorSimilitud) {
                millorSimilitud = simActual;
                for(int i=0; i<vecResultat.length; ++i){
                    vecResultat[i] = vecProductes[vProd[i]];
                }
            }
            numPermutacions[0]--;


        } else {
            for (int i = L; i <= R; i++) {
                swap(vProd, L, i);
                permutacions(vProd, L + 1, R,numPermutacions);
                swap(vProd, L, i);
            }
        }

    }

    public Producte[] generarLayout(){
        int r = vecProductes.length - 1;
        int[] numPermutacions = {factorial(r)};
        int[] vP = new int[r+1];
        for(int i=0;i<= r; ++i){
            vP[i] = i;
        }
        //llamamos con l=1 para que siempre empieze con el mismo producto
        permutacions(vP,0,r,numPermutacions);
        return vecResultat;
    }

    public Producte[] getResultat(){
        return vecResultat;
    }

    public double getMillorSimilitud(){
        return millorSimilitud;
    }


}
