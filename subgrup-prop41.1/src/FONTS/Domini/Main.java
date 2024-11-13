package Domini;

public class Main {
    public static void main(String[] args) {
        // Crear la matriz de similitudes (4x4) con valores aleatorios entre 0 y 1
        double[][] matSimilituds = {
            {0.0, 0.5, 0.8, 0.1},
            {0.5, 0.0, 0.3, 0.4},
            {0.8, 0.3, 0.0, 0.6},
            {0.1, 0.4, 0.6, 0.0}
        };

        // Crear los productos
        Producte[] vecProductes = new Producte[4];
        vecProductes[0] = new Producte(1, "Producto A");
        vecProductes[1] = new Producte(2, "Producto B");
        vecProductes[2] = new Producte(3, "Producto C");
        vecProductes[3] = new Producte(4, "Producto D");

        // Crear la instancia del algoritmo BruteForceAlg
        BruteForce bruteForce = new BruteForce(matSimilituds, vecProductes);

        // Obtener los resultados
        Producte[] millorPermutacio = bruteForce.generarLayout();
        double millorSimilitud = bruteForce.getMillorSimilitud();

        // Mostrar los resultados
        System.out.println("La mejor dist es:");
        for (Producte prod : millorPermutacio) {
            System.out.print("Producto " + prod.getId() + " ");
        }
        System.out.println("\nCon una similitud total de: " + millorSimilitud);
    }
}
