package Domini;

import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class CjtProductesTest {

    private CjtProductes cjtProductes;

    @Before
    public void setUp() {
        cjtProductes = new CjtProductes("usuari1");

        Map<Integer, Double> similitud1 = new HashMap<>();
        similitud1.put(2, 0.5);
        cjtProductes.afegirProducte(1, "Producte A", similitud1);
    }
    
    @Test
    public void testGetProductes() {
        assertNotNull(cjtProductes.getProductes("usuari1"));
        assertNull(cjtProductes.getProductes("usuariInexistent"));
    }

    @Test
    public void testGetVecProductes() {
        Producte[] productes = cjtProductes.getVecProductes();
        assertEquals(1, productes.length);
        assertEquals("Producte A", productes[0].getNom());
    }

    @Test
    public void testGetProducte() {
        Producte result1 = cjtProductes.getProducte(1);

        assertNotNull(result1);
        assertEquals(1, result1.getId());
        assertEquals("Producte A", result1.getNom());
    }

    @Test
    public void testGetProducteNoExistent() {
        Producte result99 = cjtProductes.getProducte(99);
        assertNull(result99);
    }
    
    @Test
    public void testGetPosProducte() {
        cjtProductes.getProducte(1).afegirPosPrestatgeria(1, new Pair<>(1, 1));
        assertEquals(new Pair<>(1, 1), cjtProductes.getPosProducte(1, 1));
        assertNull(cjtProductes.getPosProducte(1, 2));
        assertNull(cjtProductes.getPosProducte(99, 1));
    }
    
    @Test
    public void testSetMapProductes() {
        Map<Integer, Producte> nouMap = new HashMap<>();
        nouMap.put(2, new Producte(2, "Producte B", new HashMap<>()));
        cjtProductes.setMapProductes(nouMap);
        assertEquals(1, cjtProductes.getProductes("usuari1").size());
        assertTrue(cjtProductes.existeixProducte(2));
    }

    @Test
    public void testGetPosPrestatgeriesProducte() {
        cjtProductes.getProducte(1).afegirPosPrestatgeria(1, new Pair<>(1, 1));
        assertNotNull(cjtProductes.getPosPrestatgeriesProducte(1));
        assertNull(cjtProductes.getPosPrestatgeriesProducte(99));
    }

    @Test
    public void testExisteixProducte() {
        assertTrue(cjtProductes.existeixProducte(1));
        assertFalse(cjtProductes.existeixProducte(99));
    }

    @Test
    public void testAfegirProducte() {
        Map<Integer, Double> similituds = new HashMap<>();
        similituds.put(1, 0.7);
        cjtProductes.afegirProducte(2, "Producte B", similituds);
        assertTrue(cjtProductes.existeixProducte(2));
        assertEquals(0.7, cjtProductes.getProducte(1).getSimilitud(2), 0.001);
    }

    @Test
    public void testAfegirProducteAmbIdExistent() {
        Map<Integer, Double> similituds = new HashMap<>();
        similituds.put(1, 0.7);
        cjtProductes.afegirProducte(2, "Producte B", similituds);
        assertEquals(2, cjtProductes.getProductes("usuari1").size());
    }

    @Test
    public void testAfegirProducteSimilitud() {
        Map<Integer, Double> similituds = new HashMap<>();
        similituds.put(1, 0.7);
        cjtProductes.afegirProducte(2, "Producte B", similituds);
        
        assertEquals(0.5, cjtProductes.getProducte(1).getSimilitud(2), 0.01);
        assertEquals(0.7, cjtProductes.getProducte(2).getSimilitud(1), 0.01);
    }

    @Test
    public void testEditarProducte() {
        Producte nouProducte = new Producte(1, "Producte A Editat", new HashMap<>());
        cjtProductes.editarProducte(nouProducte);
        assertEquals("Producte A Editat", cjtProductes.getProducte(1).getNom());
    }

    @Test
    public void testEditarProducteNoExistent() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        cjtProductes.editarProducte(new Producte(99, "Producte NE"));
        System.setOut(System.out);
        assertEquals("Error: No existeix producte a editar\n", baos.toString());
    }

    @Test
    public void testEditarIdProducte() {
        cjtProductes.editarIdProducte(1, 3);
        assertFalse(cjtProductes.existeixProducte(1));
        assertTrue(cjtProductes.existeixProducte(3));
    }

    @Test
    public void testEditarNomProducte() {
        cjtProductes.editarNomProducte(1, "Nou Nom");
        assertEquals("Nou Nom", cjtProductes.getProducte(1).getNom());
    }

    @Test
    public void testEditarNomProducteNoExistent() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        cjtProductes.editarNomProducte(99, "Nou Producte X");
        System.setOut(System.out);
        assertEquals("Error: No existeix producte a editar\n", baos.toString());
    }

    @Test
    public void testEditarPosProducte() {
        cjtProductes.editarPosProducte(1, 1, new Pair<>(2, 2));
        assertEquals(new Pair<>(2, 2), cjtProductes.getPosProducte(1, 1));
    }

    @Test
    public void testEditarPosProducteNoExistent() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        cjtProductes.editarPosProducte(99, 1, new Pair<>(1, 2));
        System.setOut(System.out);
        assertEquals("Error: No existeix producte a editar\n", baos.toString());
    }

    @Test
    public void testEliminarProducte() {
        cjtProductes.eliminarProducte(1);
        assertFalse(cjtProductes.existeixProducte(1));
    }

    @Test
    public void testEliminarProducteNoExistent() {
        int mida = cjtProductes.getProductes("usuari1").size();
        cjtProductes.eliminarProducte(99);
        assertEquals(mida, cjtProductes.getProductes("usuari1").size());
    }

    @Test
    public void testModificarSimilitudIdNoExistent() {
        cjtProductes.modificarSimilitud(1, 99, 0.8);
        assertNull(cjtProductes.getProducte(1).getSimilituds().get(99));
    }

    @Test
    public void testGetSimilitudProducteNoExistent() {
        assertNull(cjtProductes.getSimilituds(99));
    }

    @Test
    public void testGetSimilituds() {
        assertNotNull(cjtProductes.getSimilituds(1));
        assertNull(cjtProductes.getSimilituds(99));
    }

    @Test
    public void testGetSimilitudsBuida() {
        cjtProductes.afegirProducte(3, "Producte C", new HashMap<>());
        assertTrue(cjtProductes.getSimilituds(3).isEmpty());
    }

    @Test
    public void testGetMatriuSimilituds() {
        Map<Integer, Double> similituds2 = new HashMap<>();
        similituds2.put(1, 0.5);
        cjtProductes.afegirProducte(2, "Producte B", similituds2);

        Map<Integer, Double> similituds3 = new HashMap<>();
        similituds3.put(1, 0.7);
        similituds3.put(2, 0.6);
        cjtProductes.afegirProducte(3, "Producte C", similituds3);

        double[][] matriu = cjtProductes.getMatriuSimilituds();

        assertEquals(3, matriu.length);
        assertEquals(3, matriu[0].length);

        assertEquals(0.0, matriu[0][0], 0.001);
        assertEquals(0.5, matriu[0][1], 0.001);
        assertEquals(0.7, matriu[0][2], 0.001);

        assertEquals(0.5, matriu[1][0], 0.001);
        assertEquals(0.0, matriu[1][1], 0.001);
        assertEquals(0.6, matriu[1][2], 0.001);

        assertEquals(0.7, matriu[2][0], 0.001);
        assertEquals(0.6, matriu[2][1], 0.001);
        assertEquals(0.0, matriu[2][2], 0.001);
    }

    @Test
    public void testGetMatriuSimilitudsBuida() {
        CjtProductes cjtBuit = new CjtProductes("usuariBuit");
        double[][] matriu = cjtBuit.getMatriuSimilituds();

        assertEquals(0, matriu.length);
    }
    
    @Test
    public void testGetProductesPerIds() {
        Set<Integer> ids = new HashSet<>(Arrays.asList(1, 99));
        Producte[] productes = cjtProductes.getProductesPerIds(ids);
        assertEquals(1, productes.length);
        assertEquals("Producte A", productes[0].getNom());
    }

    @Test
    public void testGetMatriuSimilitudsPerIds() {
        Map<Integer, Double> similituds2 = new HashMap<>();
        similituds2.put(1, 0.5);
        cjtProductes.afegirProducte(2, "Producte B", similituds2);

        Map<Integer, Double> similituds3 = new HashMap<>();
        similituds3.put(1, 0.7);
        similituds3.put(2, 0.6);
        cjtProductes.afegirProducte(3, "Producte C", similituds3);

        Integer[] ids = {1, 3};
        double[][] matriu = cjtProductes.getMatriuSimilitudsPerIds(ids);

        assertEquals(2, matriu.length);
        assertEquals(2, matriu[0].length);

        assertEquals(0.0, matriu[0][0], 0.001);
        assertEquals(0.7, matriu[0][1], 0.001);
        assertEquals(0.7, matriu[1][0], 0.001);
        assertEquals(0.0, matriu[1][1], 0.001);
    }
    
    @Test
    public void testGetMatProductes() {
        Map<Integer, Double> similitud2 = new HashMap<>();
        cjtProductes.afegirProducte(2, "Producte B", similitud2);

        Integer[][] ids = {{1, 2}, {2, 1}};
        Producte[][] matriu = cjtProductes.getMatProductes(ids);

        assertEquals(2, matriu.length);
        assertEquals(2, matriu[0].length);
        assertEquals("Producte A", matriu[0][0].getNom());
        assertEquals("Producte B", matriu[0][1].getNom());
        assertEquals("Producte B", matriu[1][0].getNom());
        assertEquals("Producte A", matriu[1][1].getNom());
    }
    
    @Test
    public void testGetMatProductesAmbIdInexistent() {
        Integer[][] ids = {{1, 99}, {null, 1}};
        Producte[][] matriu = cjtProductes.getMatProductes(ids);

        assertEquals(2, matriu.length);
        assertEquals(2, matriu[0].length);
        assertEquals("Producte A", matriu[0][0].getNom());
        assertNull(matriu[0][1]);
        assertNull(matriu[1][0]);
        assertEquals("Producte A", matriu[1][1].getNom());
    }

    @Test
    public void testGetMatProductesAmbEntradaNulla() {
        Producte[][] matriu = cjtProductes.getMatProductes(null);
        assertNull(matriu);
    }

    @Test
    public void testListToProductes() {
        List<String> jsonList = new ArrayList<>();
        jsonList.add("{\"id\":3,\"nom\":\"Producte C\",\"similituds\":{\"1\":0.6},\"posPrestatgeries\":{\"1\":[1,1]}}");
        Map<Integer, Producte> productes = cjtProductes.listToProductes(jsonList);
        assertTrue(productes.containsKey(3));
        assertEquals("Producte C", productes.get(3).getNom());
    }

    @Test
    public void testProductesToList() {
        List<String> jsonList = cjtProductes.productesToList(cjtProductes.getProductes("usuari1"));
        assertEquals(1, jsonList.size());
        assertTrue(jsonList.get(0).contains("Producte A"));
    }

    @Test
    public void testLlistarProductesUsuari() {
        Map<String, Map<String, String>> llistat = cjtProductes.llistarProductesUsuari();
        assertTrue(llistat.containsKey("1"));
        assertEquals("Producte A", llistat.get("1").get("nom"));
    }
}