package JUnit;

import Domini.BruteForce;
import Domini.Producte;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BruteForceTest {

    private Producte[] productes;
    private BruteForce bruteForce1;
    private BruteForce bruteForce2;
    private BruteForce bruteForce3;

    @Before
    public void setUp() {
        productes = new Producte[]{
            new Producte(1, "Producte A"),
            new Producte(2, "Producte B"),
            new Producte(3, "Producte C"),
            new Producte(4, "Producte D")
        };

        // Crear matriz de similitudes (simétrica)
        double[][] matSimilituds1 = new double[][]{
            {0.0, 0.0, 0.9, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.9, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0}
        };

        double[][] matSimilituds2 = new double[][]{
            {1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0}
        };

        double[][] matSimilituds3 = new double[][]{
            {0.0, 0.5, 0.2, 0.9},
            {0.5, 0.0, 0.8, 0.1},
            {0.2, 0.8, 0.0, 1.0},
            {0.9, 0.1, 1.0, 0.0}
        };

        // Instanciar BruteForce
        bruteForce1 = new BruteForce(matSimilituds1, productes);
        bruteForce2 = new BruteForce(matSimilituds2, productes);
        bruteForce3 = new BruteForce(matSimilituds3, productes);
    }

    @Test
    public void testConstructora() {
        assertNotNull(bruteForce1);
        assertEquals(-1.0, bruteForce1.getMillorSimilitud(), 0.0001);
        assertEquals(productes.length, bruteForce1.getResultat().length);

        assertNotNull(bruteForce2);
        assertEquals(-1.0, bruteForce2.getMillorSimilitud(), 0.0001);
        assertEquals(productes.length, bruteForce2.getResultat().length);

    }

    @Test
    public void testGenerarLayout() {
        //resultat per a la primera matriu
        Producte[] resultat1 = bruteForce1.generarLayout();
        assertNotNull(resultat1);

        //poden haver 2 resultats possibles, en els 2 el producte A i C han d'estar junts
        Producte[] resultatEsperat11 = new Producte[]{
            new Producte(1, "Producte A"),
            new Producte(3, "Producte C"),
            new Producte(2, "Producte B"),
            new Producte(4, "Producte D")
        };

        Producte[] resultatEsperat12 = new Producte[]{
            new Producte(1, "Producte A"),
            new Producte(2, "Producte B"),
            new Producte(4, "Producte D"),
            new Producte(3, "Producte C")
        };

        //si tots tenen una similitud de 0.0 excepte el producte A i B, llavors la suma total serà 0.9
        assertEquals(0.9, bruteForce1.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat1, resultatEsperat11) || sonIguals(resultat1, resultatEsperat12));
        assertTrue(sonIguals(resultatEsperat12, bruteForce1.getResultat()));


        //resultat per a la segona matriu
        Producte[] resultat2 = bruteForce2.generarLayout();
        assertNotNull(resultat2);

        Producte[] resultatEsperat2 = new Producte[]{
            new Producte(1, "Producte A"),
            new Producte(2, "Producte B"),
            new Producte(3, "Producte C"),
            new Producte(4, "Producte D")
        };

        //si tot tenen una similitud de 1.0 la suma total serà 4.0
        assertEquals(4.0, bruteForce2.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat2, resultatEsperat2));
        assertTrue(sonIguals(resultatEsperat2, bruteForce2.getResultat()));


        //resultat per a la tercera matriu
        Producte[] resultat3 = bruteForce3.generarLayout();
        assertNotNull(resultat3);

        Producte[] resultatEsperat3 = new Producte[]{
            new Producte(1, "Producte A"),
            new Producte(4, "Producte D"),
            new Producte(3, "Producte C"),
            new Producte(2, "Producte B")

        };

        //si tot tenen una similitud de 1.0 la suma total serà 0.9 + 1.0 + 0.8 + 0.5 = 3.2
        assertEquals(3.2, bruteForce3.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat3, resultatEsperat3));
        assertTrue(sonIguals(resultatEsperat3, bruteForce3.getResultat()));


    }

    private boolean sonIguals(Producte[] r1, Producte[] r2) {
        if (r1.length != r2.length) return false;
        for (int i = 0; i < r1.length; i++) {
             if (r1[i].getId() != r2[i].getId() || !r1[i].getNom().equals(r2[i].getNom())) return false;
        }
        return true;
    }

}



