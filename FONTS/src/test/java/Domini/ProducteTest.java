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
    public void testConstructorValid() {
        Producte p = new Producte(1, "Producte");
        assertEquals(1, p.getId());
        assertEquals("Producte", p.getNom());
        assertTrue(p.getSimilituds().isEmpty());
    }

    @Test
    public void testConstructorSenseSimilituds() {
        assertEquals(1, prod.getId());
        assertEquals("Producte A", prod.getNom());
        assertTrue(prod.getSimilituds().isEmpty());
        assertTrue(prod.getPosPrestatgeries().isEmpty());
    }

    @Test
    public void testConstructorAmbSimilituds() {
        assertEquals(10, prodS.getId());
        assertEquals("Producte S", prodS.getNom());
        assertEquals(1, prodS.getSimilituds().size());
        assertEquals(Double.valueOf(0.4), prodS.getSimilituds().get(2));
        assertTrue(prodS.getPosPrestatgeries().isEmpty());
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
    public void testGetSimilituds() {
        assertTrue(prod.getSimilituds().isEmpty());
        assertFalse(prodS.getSimilituds().isEmpty());
    }
    
    @Test
    public void testGetPosPrestatgeriesInicial() {
        assertTrue(prod.getPosPrestatgeries().isEmpty());
    }
    
    @Test
    public void testGetPosPrestatgeriaExistent() {
        prod.setPosPrestatgeria(1, new Pair<>(2, 3));
        assertEquals(new Pair<>(2, 3), prod.getPosPrestatgeria(1));
    }

    @Test
    public void testGetPosPrestatgeriaInexistent() {
        assertNull(prod.getPosPrestatgeria(99));
    }
    
    @Test
    public void testSetIdPositiva() {
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
    public void testSetSimilituds() {
        Map<Integer, Double> sims2 = new HashMap<>();
        sims2.put(10, 0.6);
        prod.setSimilituds(sims2);
        assertFalse(prod.getSimilituds().isEmpty());
        assertEquals(0.6, prod.getSimilitud(10), 0.001);
    }

    @Test
    public void testIniciSimilituds() {
        assertEquals(1, prodS.getSimilituds().size());
        assertTrue(prodS.getSimilituds().containsKey(2));
        assertEquals(0.4, prodS.getSimilituds().get(2), 0.001);
    }
    
    @Test
    public void testSetPosPrestatgeries() {
        Map<Integer, Pair<Integer, Integer>> novesPosicions = new HashMap<>();
        novesPosicions.put(1, new Pair<>(2, 3));
        novesPosicions.put(2, new Pair<>(4, 5));

        prod.setPosPrestatgeries(novesPosicions);

        assertEquals(2, prod.getPosPrestatgeries().size());
        assertEquals(new Pair<>(2, 3), prod.getPosPrestatgeries().get(1));
        assertEquals(new Pair<>(4, 5), prod.getPosPrestatgeries().get(2));
    }
    
    @Test
    public void testSetPosPrestatgeria() {
        prod.setPosPrestatgeria(1, new Pair<>(2, 3));
        assertEquals(new Pair<>(2, 3), prod.getPosPrestatgeria(1));

        prod.setPosPrestatgeria(1, new Pair<>(4, 5));
        assertEquals(new Pair<>(4, 5), prod.getPosPrestatgeria(1));
    }

    @Test
    public void testAfegirSimilitudPositiva() {
        prod.afegirSimilitud(2, 0.8);
        assertEquals(0.8, prod.getSimilitud(2), 0.001);
    }

    @Test
    public void testAfegirSimilitudAmbClauDuplicada() {
        prod.afegirSimilitud(2, 0.5);
        prod.afegirSimilitud(2, 0.7);
        assertEquals(0.7, prod.getSimilitud(2), 0.001);
    }

    @Test
    public void modificarSimilitudExistent() {
        prod.afegirSimilitud(2, 0.8);
        prod.modificarSimilitud(2, 0.9);
        assertEquals(0.9, prod.getSimilitud(2), 0.001);
    }

    @Test
    public void testGetSimilitud() {
        prod.afegirSimilitud(3, 0.7);
        assertEquals(0.7, prod.getSimilitud(3), 0.001);
    }

    @Test
    public void testGetIdProducteMillorSimilitud() {
        prod.afegirSimilitud(2, 0.5);
        prod.afegirSimilitud(3, 0.9);
        prod.afegirSimilitud(4, 0.7);
        assertEquals(3, prod.getIdProducteMillorSimilitud());
    }

    @Test
    public void testSimilitudBuida() {
        assertEquals(0, prod.getIdProducteMillorSimilitud());
    }
    
    @Test
    public void testAfegirPosPrestatgeria() {
        prod.afegirPosPrestatgeria(1, new Pair<>(2, 3));
        assertEquals(new Pair<>(2, 3), prod.getPosPrestatgeria(1));
    }

    @Test
    public void testModificarPosPrestatgeria() {
        prod.afegirPosPrestatgeria(1, new Pair<>(2, 3));
        prod.modificarPosPrestatgeria(1, new Pair<>(4, 5));
        assertEquals(new Pair<>(4, 5), prod.getPosPrestatgeria(1));
    }
    
    @Test
    public void testEsborrarPosPrestatgeria() {
        prod.setPosPrestatgeria(1, new Pair<>(2, 3));
        assertEquals(new Pair<>(2, 3), prod.getPosPrestatgeria(1));

        prod.esborrarPosPrestatgeria(1);
        assertNull(prod.getPosPrestatgeria(1));
    }

    @Test
    public void testToString() {
        assertEquals("[ 1 : Producte A ]", prod.toString());
        assertEquals("[ 10 : Producte S ]", prodS.toString());
    }
}