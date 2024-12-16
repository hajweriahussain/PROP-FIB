//package Domini;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
//public class CjtProductesTest {
//
//    private CjtProductes cjtProductes;
//    private Producte prod1;
//    private Producte prod2;
//    private Producte prod3;
//
//    @Before
//    public void setUp() {
//        cjtProductes = new CjtProductes("usuari1");
//
//        Map<Integer, Double> similitud1 = new HashMap<>();
//        similitud1.put(2, 0.5);
//        prod1 = new Producte(1, "Producte 1", similitud1);
//
//        Map<Integer, Double> similitud2 = new HashMap<>();
//        similitud2.put(1, 0.5);
//        prod2 = new Producte(2, "Producte 2", similitud2);
//
//        prod3 = new Producte(3, "Producte 3");
//    }
//
//    @Test
//    public void testGetVecProductes() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//        Producte[] productes = cjtProductes.getVecProductes();
//        assertEquals(2, productes.length);
//    }
//
//    @Test
//    public void testGetProducte() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//
//        Producte result1 = cjtProductes.getProducte(1);
//        Producte result2 = cjtProductes.getProducte(2);
//
//        assertNotNull(result1);
//        assertEquals(1, result1.getId());
//        assertEquals("Producte 1", result1.getNom());
//        assertNotNull(result2);
//        assertEquals(2, result2.getId());
//        assertEquals("Producte 2", result2.getNom());
//    }
//
//    @Test
//    public void testGetProducteNoExistent() {
//        Producte result99 = cjtProductes.getProducte(99);
//        assertNull(result99);
//    }
//
//    @Test
//    public void testExisteixProducte() {
//        cjtProductes.afegirProducte(prod1);
//        assertTrue(cjtProductes.existeixProducte(1));
//        assertFalse(cjtProductes.existeixProducte(99));
//    }
//
//    @Test
//    public void testAfegirProducte() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//        assertEquals(2, cjtProductes.getProductes("usuari1").size());
//        assertNotNull(cjtProductes.getProducte(1));
//        assertNotNull(cjtProductes.getProducte(2));
//    }
//
//    @Test
//    public void testAfegirProducteAmbIdExistent() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//
//        cjtProductes.afegirProducte(prod2);
//        assertEquals(2, cjtProductes.getProductes("usuari1").size());
//    }
//
//    @Test
//    public void testAfegirProducteSimilitud() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//        assertEquals(0.5, cjtProductes.getProducte(1).getSimilitud(2), 0.01);
//        assertEquals(0.5, cjtProductes.getProducte(2).getSimilitud(1), 0.01);
//    }
//
//    @Test
//    public void testAfegirProducteSenseSimilituds() {
//        cjtProductes.afegirProducte(prod3);
//        assertNull(cjtProductes.getProducte(3));
//    }
//
//    @Test
//    public void testEditarProducte() {
//        cjtProductes.afegirProducte(prod1);
//        prod1.setNom("Nou Producte 1");
//        cjtProductes.editarProducte(prod1);
//        assertEquals("Nou Producte 1", cjtProductes.getProducte(1).getNom());
//    }
//
//    @Test
//    public void testEditarProducteNoExistent() {
//        cjtProductes.afegirProducte(prod1);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(baos);
//        System.setOut(printStream);
//
//        cjtProductes.editarProducte(new Producte(99, "Producte NE"));
//        System.setOut(System.out);
//        assertEquals("Error: No existeix producte a editar\n", baos.toString());
//    }
//
//    @Test
//    public void testEditarIdProducte() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//
//        cjtProductes.editarIdProducte(1, 4);
//        assertNull(cjtProductes.getProducte(1));
//        assertNotNull(cjtProductes.getProducte(4));
//    }
//
//    @Test
//    public void testEditarNomProducte() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.editarNomProducte(1, "Nou Producte 1");
//        Producte result = cjtProductes.getProducte(1);
//        assertNotNull(result);
//        assertEquals("Nou Producte 1", result.getNom());
//    }
//
//    @Test
//    public void testEditarNomProducteNoExistent() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(baos);
//        System.setOut(printStream);
//
//        cjtProductes.editarNomProducte(99, "Nou Producte X");
//        System.setOut(System.out);
//        assertEquals("Error: No existeix producte a editar\n", baos.toString());
//    }
//
//    @Test
//    public void testEditarPosProducte() {
//        prod1.setColumna(3);
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.editarPosProducte(1, 5);
//
//        Producte result = cjtProductes.getProducte(1);
//        assertNotNull(result);
//        assertEquals(5, result.getColumna());
//    }
//
//    @Test
//    public void testEditarPosProducteNoExistent() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(baos);
//        System.setOut(printStream);
//
//        cjtProductes.editarPosProducte(99, 5);
//        System.setOut(System.out);
//        assertEquals("Error: No existeix producte a editar\n", baos.toString());
//    }
//
//    @Test
//    public void testEliminarProducte() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//
//        cjtProductes.eliminarProducte(1);
//        assertNull(cjtProductes.getProducte(1));
//        assertNotNull(cjtProductes.getProducte(2));
//    }
//
//    @Test
//    public void testEliminarProducteNoExistent() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.eliminarProducte(999);
//        assertNotNull(cjtProductes.getProducte(1));
//    }
//
//    @Test
//    public void testModificarSimilitudIdNoExistent() {
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream printStream = new PrintStream(baos);
//        System.setOut(printStream);
//
//        cjtProductes.modificarSimilitud(1, 99, 0.8);
//        System.setOut(System.out);
//        assertEquals("Error: Un o ambdós productes no existeixen\n", baos.toString());
//    }
//
//    @Test
//    public void testGetSimilitudProducteNoExistent() {
//        cjtProductes.afegirProducte(prod1);
//        Map<Integer, Double> similitud = cjtProductes.getSimilituds(99);
//        assertNull(similitud);
//    }
//
//    @Test
//    public void testGetSimilituds() {
//        Map<Integer, Double> similitudsProd1 = prod1.getSimilituds();
//        assertNotNull(similitudsProd1);
//        assertTrue(similitudsProd1.containsKey(2));
//        assertEquals(0.5, similitudsProd1.get(2), 0.001);
//
//        Map<Integer, Double> similitudsProd2 = prod2.getSimilituds();
//        assertNotNull(similitudsProd2);
//        assertTrue(similitudsProd2.containsKey(1));
//        assertEquals(0.5, similitudsProd2.get(1), 0.001);
//    }
//
//    @Test
//    public void testGetSimilitudsBuida() {
//        assertTrue(prod3.getSimilituds().isEmpty());
//    }
//
//    @Test
//    public void testGetMatriuSimilituds() {
//        Map<Integer, Double> similitud4 = new HashMap<>();
//        similitud4.put(1, 0.3);
//        similitud4.put(2, 0.7);
//        Producte prod4 = new Producte(4, "Producte 4", similitud4);
//
//        cjtProductes.afegirProducte(prod1);
//        cjtProductes.afegirProducte(prod2);
//        cjtProductes.afegirProducte(prod4);
//
//        double[][] mat = cjtProductes.getMatriuSimilituds();
//        assertNotNull(mat);
//        assertEquals(3, mat.length);
//        assertEquals(3, mat[0].length);
//
//        assertEquals(0.0, mat[0][0], 0.001);
//        assertEquals(0.5, mat[0][1], 0.001);
//        assertEquals(0.3, mat[0][2], 0.001);
//
//        assertEquals(0.5, mat[1][0], 0.001);
//        assertEquals(0.0, mat[1][1], 0.001);
//        assertEquals(0.7, mat[1][2], 0.001);
//
//        assertEquals(0.3, mat[2][0], 0.001);
//        assertEquals(0.7, mat[2][1], 0.001);
//        assertEquals(0.0, mat[2][2], 0.001);
//    }
//
//    @Test
//    public void testGetMatriuSimilitudBuida() {
//        CjtProductes cjt2 = new CjtProductes("usuari2");
//        double[][] mat2 = cjt2.getMatriuSimilituds();
//        assertEquals(0, mat2.length);
//    }
//}