package Domini;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProducteTest {

    private Producte prod;
    private Producte prodS;

    @Before
    public void setUp() {
        Map<Integer, Double> sims = new HashMap<>();
        sims.put(2, 0.4);

        prod = new Producte(1, "Producte A");
        prodS = new Producte(10, "Producte S", sims);
    }

    @Test
    public void testConstructorSenseSimilituds() {
        assertEquals(1, prod.getId());
        assertEquals("Producte A", prod.getNom());
        assertEquals(0, prod.getFila());
        assertEquals(-1, prod.getColumna());
        assertTrue(prod.getSimilituds().isEmpty());
    }

    @Test
    public void testConstructorAmbSimilituds() {
        assertEquals(10, prodS.getId());
        assertEquals("Producte S", prodS.getNom());
        assertEquals(0, prodS.getFila());
        assertEquals(-1, prodS.getColumna());
        assertEquals(Double.valueOf(0.4), prodS.getSimilituds().get(2));
    }

    @Test
    public void testGetId() {
        assertEquals(1, prod.getId());
        assertEquals(10, prodS.getId());
    }

    @Test
    public void testGetNom() {
        assertEquals("Producte A", prod.getNom());
        assertEquals("Producte S", prodS.getNom());
    }

    @Test
    public void testGetFila() {
        assertEquals(0, prod.getFila());
        assertEquals(0, prodS.getFila());
    }

    @Test
    public void testGetColumna() {
        assertEquals(-1, prod.getColumna());
        assertEquals(-1, prodS.getColumna());
    }

    @Test
    public void testGetSimilituds() {
        assertTrue(prod.getSimilituds().isEmpty());
        assertFalse(prodS.getSimilituds().isEmpty());
    }

    @Test
    public void testSetId() {
        prod.setId(2);
        assertEquals(2, prod.getId());
        prodS.setId(14);
        assertEquals(14, prodS.getId());
    }

    @Test
    public void testSetNom() {
        prod.setNom("Producte B");
        assertEquals("Producte B", prod.getNom());
        prodS.setNom("Producte X");
        assertEquals("Producte X", prodS.getNom());
    }

    @Test
    public void testSetFila() {
        prod.setFila(3);
        assertEquals(3, prod.getFila());
        prodS.setFila(2);
        assertEquals(2, prodS.getFila());
    }

    @Test
    public void testSetColumna() {
        prod.setColumna(5);
        assertEquals(5, prod.getColumna());
        prodS.setColumna(7);
        assertEquals(7, prodS.getColumna());
    }

    @Test
    public void testSetSimilituds() {
        Map<Integer, Double> sims2 = new HashMap<>();
        sims2.put(10, 0.6);
        prod.setSimilituds(sims2);
        assertFalse(prod.getSimilituds().isEmpty());
    }

    @Test
    public void testAfegirSimilitud() {
        prod.afegirSimilitud(2, 0.8);
        assertEquals(0.8, prod.obtenirSimilitud(2), 0.001);
    }

    @Test
    public void modificarSimilitutExistent() {
        prod.afegirSimilitud(2, 0.8);
        prod.modificarSimilitud(2, 0.9);
        assertEquals(0.9, prod.obtenirSimilitud(2), 0.001);
    }

    @Test
    public void modificarSimilitutNoExistent() {

    }

    @Test
    public void testObtenirSimilitut() {
        prod.afegirSimilitud(3, 0.7);
        assertEquals(0.7, prod.obtenirSimilitud(3), 0.001);
    }

    @Test
    public void testObtenirIdProducteMillorSimilitut() {
        prod.afegirSimilitud(2, 0.5);
        prod.afegirSimilitud(3, 0.9);
        prod.afegirSimilitud(4, 0.7);
        assertEquals(3, prod.obtenirIdProducteMillorSimilitud());
    }

    @Test
    public void testToString() {
        assertEquals("[ 1 : Producte A ]", prod.toString());
        assertEquals("[ 10 : Producte S ]", prodS.toString());
    }
}