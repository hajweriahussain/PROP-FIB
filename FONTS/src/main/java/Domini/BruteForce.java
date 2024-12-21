package Domini;

import java.math.BigInteger;

/**
 * Implementa el generador de solucions BruteForce per trobar la millor disposició de productes
 * basada en una matriu de similituds. Utilitza l'algorisme de permutacions per explorar totes les 
 * possibles disposicions i calcular la millor disposició amb la major similitud total.
 */
public class BruteForce implements GeneradorSolucio {

    private double[][] matSimilituds;
    private int[] vecProductes;
    private double millorSimilitud;
    private int[] vecResultat;
    private Integer[][] matResultat;
    private int columnes;
    private int files;

    /**
     * Constructor de la classe BruteForce.
     * 
     * @param matSim Matriu de similituds entre productes.
     * @param vecPrd Vector de ints (identificadors dels productes) a disposar.
     * @param numColumnes Nombre de columnes per al layout resultant.
     */
    public BruteForce(double[][] matSim, int[] vecPrd, int numColumnes) {
        matSimilituds = matSim;
        vecProductes = vecPrd;
        millorSimilitud = -1.0;
        columnes = numColumnes;
        int n = vecProductes.length;
        vecResultat = new int[n];
        if (n % columnes == 0) this.files = n / columnes;
        else this.files = n / columnes + 1;
        this.matResultat = new Integer[files][columnes];
    }

    /**
     * Calcula el factorial d'un nombre enter.
     * 
     * @param n El número del qual calcular el factorial.
     * @return El factorial de n com a un BigInteger.
     */
    private static BigInteger factorial(int n) {
        BigInteger resultat = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            resultat = resultat.multiply(BigInteger.valueOf(i));
        }
        return resultat;
    }

    /**
     * Calcula la similitud total per una disposició específica de productes.
     * 
     * @param vecActualProd Vector que representa la disposició actual de productes.
     * @return La similitud total de la disposició.
     */
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

    /**
     * Intercanvia dos elements en un array.
     * 
     * @param arr L'array en el qual es volen intercanviar els elements.
     * @param i L'índex del primer element.
     * @param j L'índex del segon element.
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Genera totes les permutacions possibles del vector de productes i avalua la millor disposició
     * basada en la similitud total.
     * 
     * @param vProd El vector de productes que es vol permutar.
     * @param L L'índex esquerre de la permutació.
     * @param R L'índex dret de la permutació.
     * @param numPermutacions Array que emmagatzema el nombre de permutacions restants.
     */
    private void permutacions(int[] vProd, int L, int R, BigInteger[] numPermutacions) {
        if (numPermutacions[0].equals(BigInteger.ZERO)) return;
        if (L == R) {
            double simActual = calcularSimilitudTotal(vProd);
            if (simActual > millorSimilitud) {
                millorSimilitud = simActual;
                for (int i = 0; i < vecResultat.length; ++i) {
                    vecResultat[i] = vecProductes[vProd[i]];
                }
            }
            numPermutacions[0] = numPermutacions[0].subtract(BigInteger.ONE);

        } else {
            for (int i = L; i <= R; i++) {
                swap(vProd, L, i);
                permutacions(vProd, L + 1, R, numPermutacions);
                swap(vProd, L, i);
            }
        }
    }

    /**
     * Genera el layout (disposició) òptima dels productes en funció de les similituds calculades.
     * La disposició es dona en forma d'una matriu d'enters on els productes es distribueixen
     * en files i columnes.
     * 
     * @return La matriu de disposició de productes.
     */
    public Integer[][] generarLayout() {
        int r = vecProductes.length - 1;
        BigInteger[] numPermutacions = {factorial(r)};
        int[] vP = new int[r + 1];
        for (int i = 0; i <= r; ++i) {
            vP[i] = i;
        }

        permutacions(vP, 0, r, numPermutacions);

        // Construir la matriu de resultat
        int k = 0;
        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes && k < vecProductes.length; ++j) {
                if (i % 2 == 0) { // De dreta a esquerra
                    matResultat[i][j] = vecResultat[k];
                } else { // D'esquerra a dreta
                    matResultat[i][(columnes - 1) - j] = vecResultat[k];
                }
                ++k;
            }
        }
        return matResultat;
    }

    /**
     * Obté el resultat de la disposició generada.
     * 
     * @return La matriu amb la disposició final dels productes.
     */
    public Integer[][] getResultat() {
        return matResultat;
    }

    /**
     * Obté la millor similitud total trobada durant la generació del layout.
     * 
     * @return La millor similitud total.
     */
    public double getMillorSimilitud() {
        return millorSimilitud;
    }
}
