package Domini;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrestatgeriaTest {
	private Prestatgeria prestatgeria;
	private Producte prodA;
    private Producte prodB;
    private Producte prodC;

	@Before
	public void setUp() {
		prodA = new Producte(1, "Producte A");
		prodB = new Producte(2, "Producte B");
		prodC = new Producte(3, "Producte C");

        prestatgeria = new Prestatgeria(1, 3);
        prestatgeria.setLayout(new Producte[]{prodA, prodB, prodC});
	}

	@Test
    public void testConstructora() {
        Prestatgeria prestatgeriaIncorrecta = new Prestatgeria(-1, -2);
        assertEquals(1, prestatgeriaIncorrecta.getId());
        assertEquals(1, prestatgeriaIncorrecta.getNumProductes());

        Prestatgeria prestatgeriaValida = new Prestatgeria(5, 4);
        assertEquals(5, prestatgeriaValida.getId());
        assertEquals(4, prestatgeriaValida.getNumProductes());
    }
	
	@Test
    public void testGettersPrestatgeria() {
        assertEquals(1, prestatgeria.getId());
        assertEquals(3, prestatgeria.getNumProductes());
        assertEquals(3, prestatgeria.getLayout().length);
        assertNotNull(prestatgeria.getLayout());
    }
	
	@Test
    public void testSetId() {
        prestatgeria.setId(10);
        assertEquals(10, prestatgeria.getId());

        prestatgeria.setId(-5);
        assertEquals(10, prestatgeria.getId());
    }
	
	@Test
    public void testSetNumProductes() {
        prestatgeria.setNumProductes(5);
        assertEquals(5, prestatgeria.getNumProductes());
    }
	
	@Test
    public void testSetLayout() {
        Producte[] nouLayout = new Producte[]{prodC, prodA, prodB};
        prestatgeria.setLayout(nouLayout);

        Producte[] layout = prestatgeria.getLayout();
        assertEquals(prodC, layout[0]);
        assertEquals(prodA, layout[1]);
        assertEquals(prodB, layout[2]);
    }
	
	@Test
    public void testSetLayoutError() {
        prestatgeria.setLayout(null);
        assertArrayEquals(new Producte[]{prodA, prodB, prodC}, prestatgeria.getLayout());

        Producte[] disposicioIncorrecta = new Producte[]{prodA};
        prestatgeria.setLayout(disposicioIncorrecta);
        assertArrayEquals(new Producte[]{prodA, prodB, prodC}, prestatgeria.getLayout());
    }
	
	@Test
    public void testIntercanviarDosProductes() {
		prestatgeria.intercanviarDosProductes(0, 2);
	    Producte[] layout = prestatgeria.getLayout();
	    assertEquals(prodC, layout[0]);
	    assertEquals(prodB, layout[1]);
	    assertEquals(prodA, layout[2]);
    }
	
	@Test
    public void testIntercanviarDosProductesMateixaPos() {
		prestatgeria.intercanviarDosProductes(0, 0);
	    Producte[] layout = prestatgeria.getLayout();
	    assertEquals(prodA, layout[0]);
	    assertEquals(prodB, layout[1]);
	    assertEquals(prodC, layout[2]);
	}
	
	@Test
	public void testIntercanviarDosProductesForaRang() {
		prestatgeria.intercanviarDosProductes(-1, 3);
	    Producte[] layout = prestatgeria.getLayout();
	    assertEquals(prodA, layout[0]);
	    assertEquals(prodB, layout[1]);
	    assertEquals(prodC, layout[2]);
	}
	
	@Test
	public void testIntercanviarDosProductesNulls() {
		prestatgeria.setLayout(new Producte[]{prodA, null, prodC});
	    prestatgeria.intercanviarDosProductes(1, 2);
	    Producte[] layout = prestatgeria.getLayout();
	    assertEquals(prodA, layout[0]);
	    assertNull(layout[1]);
	    assertEquals(prodC, layout[2]);
	}
	
	@Test
    public void testEliminarPrestatgeria() {
        prestatgeria.eliminarPrestatgeria();

        assertNull(prestatgeria.getLayout());
        assertEquals(0, prestatgeria.getNumProductes());
    }
	
	@Test
	public void testEliminarPrestatgeriaJaEsborrada() {
        prestatgeria.eliminarPrestatgeria();
        prestatgeria.eliminarPrestatgeria();

        assertNull(prestatgeria.getLayout());
        assertEquals(0, prestatgeria.getNumProductes());
    }



}
