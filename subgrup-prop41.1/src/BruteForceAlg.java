import java.util.Vector;

public class BruteForceAlg {

    private float[][] matSimilituds;
    private int[] vecProductes;
    private float millorSimilitud;
    private int[] vecResultat;

    //int[] es un arreglo y almacena un numero fijo de datos
    //Vector<Integer> es dinamico, por lo tanto su tamaño puede crecer automáticamente a medida que se añaden elementos.

    //Constructora
    public BruteForceAlg(int[][] matSim, int[] vecPrd){
        matSimilituds = matSim;
        vecProductes = vecProd;
        millorSimilitud = -1.0;
        vecResult = new int[matSimilituds.size()]; //new Vector<>();
    }

    private float calcularSimilitudTotal(int[] vecActualProd){
        float total = 0.0;
        int n = vecActualProd.size();
        for(int i=0; i<n; i++){
            float producte1 = vecActualProd[i];
            float producte2 = vecActualProd[i+1];
            total += matSimilituds[producte1][producte2];
        }
        //com que estanteria es circular:
        float producte1 = vecActualProd[n-1];
        float producte2 = vecActualProd[0];
        total += matSimilituds[producte1][producte2];

        return total;
    }

    private void swap(int[] arr´, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void permutacions(int[] vProd, int L, int R, float millorSim, int[] vecRes){
        if (L == R) {
            int simActual = calcularSimilitudTotal(productos, similitudes);
            if (similitudActual > mejorSimilitud[0]) {
                mejorSimilitud[0] = similitudActual;
                System.arraycopy(productos, 0, mejorPerm, 0, productos.length);
            }
        } else {
            for (int i = l; i <= r; i++) {
                // Intercambiar productos[l] con productos[i]
                swap(productos, l, i);
                // Generar recursivamente las permutaciones
                permutar(productos, l + 1, r, similitudes, mejorPerm, mejorSimilitud);
                // Deshacer el intercambio
                swap(productos, l, i);
            }
        }
    }



