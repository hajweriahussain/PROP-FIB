package Domini;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de prova per la classe BruteForce.
 * Aquesta classe conté mètodes de prova per verificar la correcta
 * implementació de la classe BruteForce.
 */
public class BruteForceTest {

    private int[] productes;
    private BruteForce bruteForce1;
    private BruteForce bruteForce2;
    private BruteForce bruteForce3;
    private BruteForce bruteForce4;

    /**
     * Configura les dades per als tests.
     * Aquest mètode inicialitza els productes i les matrius de similitud per als tres casos de prova.
     */
    @Before
    public void setUp() {
        productes = new int[]{ 1, 2 , 3, 4, 5, 6, 7, 8};

        // Crear matriu de similituds (simètrica)
        double[][] matSimilituds1 = new double[][]{
            {0.0, 0.0, 0.9, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.9, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        };

        double[][] matSimilituds2 = new double[][]{
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0, 1.0,1.0, 1.0, 1.0, 1.0}
        };

        double[][] matSimilituds3 = new double[][]{
            {0.0, 0.5, 0.2, 0.9, 0.6, 0.4, 0.3, 0.7},
            {0.5, 0.0, 0.8, 0.1, 0.4, 0.2, 0.9, 0.3},
            {0.2, 0.8, 0.0, 0.1, 0.5, 0.7, 0.3, 0.6},
            {0.9, 0.1, 0.1, 0.0, 0.8, 0.5, 0.2, 0.4},
            {0.6, 0.4, 0.5, 0.8, 0.0, 0.9, 0.7, 0.2},
            {0.4, 0.2, 0.7, 0.5, 0.9, 0.0, 0.6, 0.8},
            {0.3, 0.9, 0.3, 0.2, 0.7, 0.6, 0.0, 1.0},
            {0.7, 0.3, 0.6, 0.4, 0.2, 0.8, 1.0, 0.0}
        };
        
        double[][] matSimilituds4 = new double[][] {{0.0}};

        // Instanciar BruteForce
        bruteForce1 = new BruteForce(matSimilituds1, productes, 4);
        bruteForce2 = new BruteForce(matSimilituds2, productes, 4);
        bruteForce3 = new BruteForce(matSimilituds3, productes, 4);
        bruteForce4 = new BruteForce(matSimilituds4, new int[]{1}, 1);
    }

    /**
     * Test per verificar la correcta inicialització de la classe BruteForce.
     * Assegura que l'objecte BruteForce es crea correctament i que els valors inicials són els esperats.
     */
    @Test
    public void testConstructora() {
        assertNotNull(bruteForce1);
        assertEquals(-1.0, bruteForce1.getMillorSimilitud(), 0.0001);
        
        assertNotNull(bruteForce2);
        assertEquals(-1.0, bruteForce2.getMillorSimilitud(), 0.0001);
    }

    /**
     * Test per verificar el funcionament de la generació de layouts a BruteForce,
     * utilitzant la primera matriu de similitud.
     * Aquest test és molt simple i esta fet per veure clarament que el producte 1 amb el tres que tenen una similitud de 0.9
     * estan junts, i la resta com tenen una similitud de 0 no importa la posició.
     * Si tots tenen una similitud de 0.0 excepte el producte 1 i 3, la suma total serà 0.9
     */
    @Test
    public void testGenerarLayout1() {
        Integer[][] resultat1 = bruteForce1.generarLayout();
        assertNotNull(resultat1);

        // Poden haver 2 resultats possibles, en els dos el producte A i C han d'estar junts
        Integer[][] resultatEsperat11 = new Integer[][]{
            {1,3,4,2}, {8,7,6,5}
        };

        Integer[][] resultatEsperat12 = new Integer[][]{
            {1,2,4,5}, {3,8,7,6}
        };

        // Si tots tenen una similitud de 0.0 excepte el producte A i B, la suma total serà 0.9
        assertEquals(0.9, bruteForce1.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat1, resultatEsperat11) || sonIguals(resultat1, resultatEsperat12));
        assertTrue(sonIguals(resultatEsperat12, bruteForce1.getResultat()));
    }

    /**
     * Test per verificar el funcionament de la generació de layouts a BruteForce,
     * utilitzant la segona matriu de similitud.
     * Aquest test està fet per veure com es comporta l'algoritme en una situació extrema on totes les similituds
     * són igual a 1. No s'ha considerat el cas de que tots siguin iguals a 0, ja que pensem que es el mateix probar-ho amb
     * similituds de 1 que de 0.
     * Si tots tenen una similitud de 1.0, la suma total serà 8.0
     */
    @Test
    public void testGenerarLayout2() {
        Integer[][] resultat2 = bruteForce2.generarLayout();
        assertNotNull(resultat2);

        Integer[][] resultatEsperat2 = new Integer[][]{
            {1,2,3,4},{8,7,6,5}
        };

        // Si tots tenen una similitud de 1.0, la suma total serà 8.0
        assertEquals(8.0, bruteForce2.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat2, resultatEsperat2));
        assertTrue(sonIguals(resultatEsperat2, bruteForce2.getResultat()));
    }

    /**
     * Test per verificar el funcionament de la generació de layouts a BruteForce,
     * utilitzant la tercera matriu de similitud.
     * Aquest test està fet amb valors de ssimilituds aleatoris, per veure que l'algoritme funciona bé
     * en general amb qualsevol valor.
     */
    @Test
    public void testGenerarLayout3() {
        Integer[][] resultat3 = bruteForce3.generarLayout();
        assertNotNull(resultat3);

        Integer[][] resultatEsperat3 = new Integer[][]{
            {1,8,7,2},{4,5,6,3}
        };

        assertEquals(6.7, bruteForce3.getMillorSimilitud(), 0.0001);
        assertTrue(sonIguals(resultat3, resultatEsperat3));
        assertTrue(sonIguals(resultatEsperat3, bruteForce3.getResultat()));
    }

     /**
     * Test per verificar el comportament de la classe BruteForce quan només hi ha un producte.
     * Aquest test comprovem un altre cas extrem per veure que, amb només un producte, no es genera 
     * écap combinació, i que la similitud és 0.
     */
    @Test
    public void testGenerarLayoutUnProducto() {
        Integer[][] resultat = bruteForce4.generarLayout();
        assertNotNull(resultat);

        // Només hi ha un producte, així que la matriu de resultats ha de ser [[1]].
        Integer[][] resultatEsperat = new Integer[][]{{1}};

        // La millor similitud ha de ser 0, ja que no hi ha altres productes per comparar.
        assertEquals(0.0, bruteForce4.getMillorSimilitud(), 0.0001);
        
        // Verifiquem que el resultat generat coincideixi amb l'esperat.
        assertTrue(sonIguals(resultat, resultatEsperat));
        assertTrue(sonIguals(resultatEsperat, bruteForce4.getResultat()));
    }
    
    /**
     * Mètode auxiliar per verificar si dues matrius són iguals.
     * 
     * @param r1 La primera matriu a comparar.
     * @param r2 La segona matriu a comparar.
     * @return true si les matrius són iguals, false en cas contrari.
     */
    private boolean sonIguals(Integer[][] r1, Integer[][] r2) {
        if (r1.length != r2.length) return false;
        for (int i = 0; i < r1.length; i++) {
            for (int j = 0; j < r1[i].length; j++) {
                if (!r1[i][j].equals(r2[i][j])) return false;
            }
        }
        return true;
    }
}