package Domini;

import java.math.BigInteger;

public class BruteForce implements GeneradorSolucio {

    private double[][] matSimilituds;
    private int[] vecProductes;
    private double millorSimilitud;
    private int[] vecResultat; 
    private Integer[][] matResultat;
    private int columnes;
    private int files;

    // Constructor
    public BruteForce(double[][] matSim, int[] vecPrd, int numColumnes) {
        matSimilituds = matSim;
        vecProductes = vecPrd;
        millorSimilitud = -1.0;
        columnes = numColumnes;
        int n = vecProductes.length;
        vecResultat = new int[n];
        if (n%columnes == 0) this.files = n/columnes;
        else this.files = n/columnes + 1;
        this.matResultat = new Integer[files][columnes];
    }

    private static BigInteger factorial(int n) {
        BigInteger resultado = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            resultado = resultado.multiply(BigInteger.valueOf(i));
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

    private void permutacions(int[] vProd, int L, int R, BigInteger[] numPermutacions) {
        if (numPermutacions[0].equals(BigInteger.ZERO)) return;
        if (L == R) {
            double simActual = calcularSimilitudTotal(vProd);
            if (simActual > millorSimilitud) {
                millorSimilitud = simActual;
                for(int i=0; i<vecResultat.length; ++i){
                    vecResultat[i] = vecProductes[vProd[i]];
                }
            }
            numPermutacions[0] = numPermutacions[0].subtract(BigInteger.ONE);


        } else {
            for (int i = L; i <= R; i++) {
                swap(vProd, L, i);
                permutacions(vProd, L + 1, R,numPermutacions);
                swap(vProd, L, i);
            }
        }

    }

    public Integer[][] generarLayout(){
        int r = vecProductes.length - 1;
        BigInteger[] numPermutacions = {factorial(r)};
        int[] vP = new int[r+1];
        for(int i=0;i<= r; ++i){
            vP[i] = i;
        }

        permutacions(vP,0,r,numPermutacions);
        
        //muntem la matriu resultat
        int k = 0;
        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes && k < vecProductes.length; ++j) {
                if(i%2 == 0) { //del dret
                    matResultat[i][j] = vecResultat[k];
                }
                else { //del reves
                    matResultat[i][(columnes-1)-j] = vecResultat[k];
                }
                ++k;
            }
        }
        return matResultat;
    }

    public Integer[][] getResultat(){
        return matResultat;
    }

    public double getMillorSimilitud(){
        return millorSimilitud;
    }


}
