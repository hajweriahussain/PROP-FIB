package Domini;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

class CjtPrestatgeriesTest {

    private CjtPrestatgeries cjtPrestatgeries;

    @Before
    void setUp() {
        cjtPrestatgeries = new CjtPrestatgeries("usuariTest");
    }

    @Test
    void testCrearPrestatgeria() {
        Set<Integer> productes = new HashSet<>();
        productes.add(1);
        productes.add(2);
        try {
            cjtPrestatgeries.crearPrestatgeria(1, "Prestatgeria 1", 2, 3, productes);
            Prestatgeria prestatgeria = cjtPrestatgeries.getPrestatgeria(1);
            assertNotNull(prestatgeria.getNom(), "La prestatgeria debería haberse creado.");
            assertEquals("Prestatgeria 1", prestatgeria.getNom());
            assertEquals(2, prestatgeria.getNumFilas());
            assertEquals(3, prestatgeria.getNumColumnas());
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción al crear la prestatgeria.");
        }
    }

    @Test
    void testEditarPrestatgeria() {
        Set<Integer> productes = new HashSet<>();
        productes.add(1);
        productes.add(2);
        try {
            cjtPrestatgeries.crearPrestatgeria(1, "Prestatgeria 1", 2, 3, productes);
            cjtPrestatgeries.editarPrestatgeria(1, "Prestatgeria Editada", 4, 5, productes);
            Prestatgeria prestatgeria = cjtPrestatgeries.getPrestatgeria(1);
            assertNotNull(prestatgeria);
            assertEquals("Prestatgeria Editada", prestatgeria.getNom());
            assertEquals(4, prestatgeria.getNumFilas());
            assertEquals(5, prestatgeria.getNumColumnas());
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción al editar la prestatgeria.");
        }
    }

    @Test
    void testEliminarPrestatgeria() {
        Set<Integer> productes = new HashSet<>();
        productes.add(1);
        try {
            cjtPrestatgeries.crearPrestatgeria(1, "Prestatgeria 1", 2, 3, productes);
            cjtPrestatgeries.eliminarPrestatgeria(1);
            assertNull(cjtPrestatgeries.getPrestatgeria(1).getNom(), "La prestatgeria debería haberse eliminado.");
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción al eliminar la prestatgeria.");
        }
    }

    @Test
    void testIntercanviarDosProductes() {
        Set<Integer> productes = new HashSet<>();
        productes.add(1);
        productes.add(2);
        try {
            cjtPrestatgeries.crearPrestatgeria(1, "Prestatgeria 1", 2, 2, productes);
            Producte[][] layout = new Producte[2][2];
            layout[0][0] = new Producte(1, "Producte 1");
            layout[1][1] = new Producte(2, "Producte 2");
            cjtPrestatgeries.setLayout(layout, 1);
            cjtPrestatgeries.intercanviarDosProductes(1, 0, 0, 1, 1);
            Producte[][] resultat = cjtPrestatgeries.getLayout(1);
            assertEquals(2, resultat[0][0].getId());
            assertEquals(1, resultat[1][1].getId());
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción al intercanviar productos.");
        }
    }

    @Test
    void testLlistarPrestatgeriesUsuari() {
        Set<Integer> productes = new HashSet<>();
        productes.add(1);
        try {
            cjtPrestatgeries.crearPrestatgeria(1, "Prestatgeria 1", 2, 3, productes);
            cjtPrestatgeries.crearPrestatgeria(2, "Prestatgeria 2", 2, 3, productes);
            String prestatgeriesUsuari = "" + cjtPrestatgeries.llistarPrestatgeriesUsuari().size();
            assertEquals("2", prestatgeriesUsuari, "Debería haber dos prestatgeries.");
        } catch (Exception e) {
            fail("No debería haber lanzado una excepción al listar prestatgeries.");
        }
    }
}
