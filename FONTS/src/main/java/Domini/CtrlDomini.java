package Domini;

import java.util.Map;
import java.util.Set;

public class CtrlDomini {
    private Usuari UsuariActual;
    private CjtUsuaris cjtUsuaris;
    private CjtProductes cjtProductes;
    private CjtPrestatgeries cjtPrestatgeries;
    private double[][] matSimilituds;
    private Producte[] vecProductes;


    private static CtrlDomini singletonObject;
    
    public CtrlDomini() {
        inicialitzarCtrlDomini();
    }

    public void inicialitzarCtrlDomini() {
        cjtUsuaris = new CjtUsuaris("");
    }

    public static CtrlDomini getInstance() {
        if(singletonObject == null) {
            singletonObject = new CtrlDomini();
        }
        return singletonObject;
    }

    public void iniciarSessio(String username, String pwd){
    	if (username == null || pwd == null) {
            System.out.println("Error: Nom d'usuari o contrasenya no vàlids.");
            return;
        }
        if (cjtUsuaris.getUsuari(username) == null) {
            System.out.println("L'usuari no existeix. Creant nou usuari.");
            crearUsuari(username, pwd);
        } else {
            System.out.println("Usuari trobat. Iniciant sessió...");
        }

        cjtProductes = new CjtProductes(username);
        cjtPrestatgeries = new CjtPrestatgeries(username);
        UsuariActual = cjtUsuaris.getUsuari(username);
    }

    public void crearUsuari(String name, String pwd){
        cjtUsuaris.crearUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }

    public void canviarUsuari(String nomUsuari, String pwd) {
        iniciarSessio(nomUsuari, pwd); //canviem l'usuari actual
    }
    
    public Producte[] llistarProductesUsuari() {
    	if (cjtProductes == null) {
            System.out.println("Error: No hi ha cap conjunt de productes associat a l'usuari.");
            return null;
        }
        return cjtProductes.getVecProductes();
    }
    
    public Producte[][] llistarPrestatgeriaUsuari(int id) {
    	if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria creada.");
            return null;
        }
        Prestatgeria p = cjtPrestatgeries.getPrestatgeria(id);
        return p.getLayout();
    }
    
    public Set<String> llistarUsuaris(){
    	return cjtUsuaris.getUsuaris();
    }
    
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds, Boolean bruteForce){
    	if (id <= 0 || nom == null) {
            System.out.println("Error: Dades del producte no vàlides.");
            return;
        }
        Producte p = new Producte(id, nom, similituds);
        cjtProductes.afegirProducte(p);
        //cjtPrestatgeries.crearPrestatgeria(bruteForce);
        
        //Modificacions aqui
    }

    public void crearPrestatgeria(Boolean bruteForce){
    	if (cjtProductes == null || cjtProductes.getVecProductes().length == 0) {
            System.out.println("Error: No hi ha productes per crear la prestatgeria.");
            return;
        }

        matSimilituds = cjtProductes.getMatriuSimilituds();
        vecProductes = cjtProductes.getVecProductes();
        int numProductes = vecProductes.length;
        prestatgeria = new Prestatgeria(1, numProductes);

        GeneradorSolucio generadorInicial;

        if (bruteForce) {
            generadorInicial = new BruteForce(matSimilituds, vecProductes);
            System.out.println("Creant prestatgeria amb algoritme de força bruta.");
        } else {
            generadorInicial = new DosAproximacio(matSimilituds, vecProductes);
            System.out.println("Creant prestatgeria amb algoritme de 2-Aproximació.");
        }

        Producte[][] solucio = generadorInicial.generarLayout();
        prestatgeria.setLayout(solucio);

        for (int i = 0; i < solucio.length; i++) {
            solucio[i].setColumna(i);
        }
        
    }

    public void modificarProducte(Integer idProdActual1, Integer nouId, String nouNom) {
        if (cjtProductes.getProducte(idProdActual1) != null) {
            
            if (nouId != null) {
                cjtProductes.editarIdProducte(idProdActual1, nouId);
                System.out.println("S'ha modificat el id del producte amb id " + idProdActual1);
            }

            if (nouNom == "-") {
                cjtProductes.editarNomProducte(idProdActual1, nouNom);
                System.out.println("S'ha modificat el nom del producte amb id " + idProdActual1);
            }
            
        } else {
            System.out.println("No existeix producte amb id = " + idProdActual1);
        }
    }
    
    public void modificarSimilituds(Integer idProdActual1, Integer idProdActual2, double novaSim, Boolean bruteForce) {
    	if (cjtProductes.getProducte(idProdActual1) != null && cjtProductes.getProducte(idProdActual2) != null) {
    		cjtProductes.modificarSimilitud(idProdActual1, idProdActual2, novaSim);
            crearPrestatgeria(bruteForce);
            System.out.println("S'ha modificat la similitud entre el producte amb id " 
                                + idProdActual1 + " i el producte amb id " + idProdActual2);
    	}
    	else {
            System.out.println("No existeixen un dels productes amb id = " + idProdActual1 + " o id= " + idProdActual2 );
        }
    }

	public void modificarPrestatgeria(int fila1, int col1, int fila2, int col2){
		if (prestatgeria == null) {
            System.out.println("Error: No hi ha cap prestatgeria per modificar.");
            return;
        }
        prestatgeria.intercanviarDosProductes(fila1, col1, fila2, col2);
    }

    public void esborrarProducte(int id, Boolean bruteForce){
        cjtProductes.eliminarProducte(id);
        crearPrestatgeria(bruteForce);
    }

    public void esborrarPrestatgeria(int id){
    	if (prestatgeria == null) {
            System.out.println("La prestatgeria no existeix.");
            return;
        }
        prestatgeria.esborrarPrestatgeria();
    }

    public String getUsuariActual(){
        return UsuariActual.getUsername();
    }

    public void esborrarUsuari(){
    	if (UsuariActual == null) {
            System.out.println("No hi ha usuari actual.");
            return;
        }
        cjtUsuaris.eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }

    public void tancarSessio(){
        UsuariActual = new Usuari(null, null);
    }
}