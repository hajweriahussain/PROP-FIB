package Domini;

public class Main {
    public static void main(String[] args) {
        // Crear la matriz de similitudes (4x4) con valores aleatorios entre 0 y 1
        float[][] matSimilituds = {
            {0.0f, 0.2f, 0.1f, 0.9f},
            {0.2f, 0.0f, 0.1f, 0.6f},
            {0.1f, 0.1f, 0.0f, 0.7f},
            {0.9f, 0.6f, 0.7f, 0.0f}
        };

        // Crear los productos
        Producte[] vecProductes = new Producte[4];
        vecProductes[0] = new Producte(0, "Producto A", 0, 0);
        vecProductes[1] = new Producte(1, "Producto B", 1, 1);
        vecProductes[2] = new Producte(2, "Producto C", 2, 2);
        vecProductes[3] = new Producte(3, "Producto D", 3, 3);

        // Crear la instancia del algoritmo BruteForceAlg
        BruteForceAlg bruteForceAlg = new BruteForceAlg(matSimilituds, vecProductes);

        // Ejecutar el cálculo
        bruteForceAlg.algoritme();

        // Obtener los resultados
        Producte[] millorPermutacio = bruteForceAlg.getResultat();
        float millorSimilitud = bruteForceAlg.getMillorSimilitud();

        // Mostrar los resultados
        System.out.println("La mejor dist es:");
        for (Producte prod : millorPermutacio) {
            System.out.print("Producto " + prod.getId() + " ");
        }
        System.out.println("\nCon una similitud total de: " + millorSimilitud);
    }
}
