package JUnit;

import java.beans.Transient;

import Domini.DosAproximacio;
import Domini.Producte;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
//import static org.junit.runners.*;

public class DosAproximacioTest {
    private DosAproximacio da1;
    private DosAproximacio da2;
    private DosAproximacio da3;
    private Producte[] prods;

    @Before
    public void setUp() {
        prods = new Producte[] {
            new Producte(1, "a"),
            new Producte(2, "b"),
            new Producte(3, "c"),
            new Producte(4, "d")
        };
        double[][] matZero = {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        double[][] matUn = {
            {0, 0, 0, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 0},
            {0, 1, 0, 0}
        };
        double[][] matTree = {
            {0, 1, 1, 1},
            {1, 0, 0, 0},
            {1, 0, 0, 0.5},
            {1, 0, 0.5, 0}
        };
        da1 = new DosAproximacio(matZero, prods);
        da2 = new DosAproximacio(matUn, prods);
        da3 = new DosAproximacio(matTree, prods);
    }

    @Test
    public void testMatZero() {
        Producte[] actual = da1.generarLayout();
        assertEquals(0, da1.getMillorSimilitud(), 0.01);
    }
    
    @Test
    public void testMatUn() {
        Producte[] expected = new Producte[] {
            prods[0], prods[2], prods[1], prods[3]
        };
        Producte[] actual = da2.generarLayout();
        assertEquals(1, da2.getMillorSimilitud(), 0.01);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMatTree() {
        Producte[] expected = new Producte[] {
            prods[0], prods[3], prods[2], prods[1]
        };
        Producte[] actual = da3.generarLayout();
        assertEquals(2.5, da3.getMillorSimilitud(), 0.01);
        assertArrayEquals(expected, actual);
    }
}
