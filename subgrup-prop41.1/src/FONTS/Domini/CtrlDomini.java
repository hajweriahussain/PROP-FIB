package Domini;

import java.util.Map;

public class CtrlDomini {
    private Usuari UsuariActual;
    private CjtUsuaris cjtUsuaris;
    private CjtProductes cjtProductes;
    private Prestatgeria prestatgeria;
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
        if (cjtUsuaris.getUsuari(username) == null){
            crearUsuari(username, pwd);
        }
        cjtProductes = new CjtProductes(username);
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
    	return cjtProductes.getVecProductes();
    }
    
    public Producte[] llistarPrestatgeriaUsuari() {
    	return prestatgeria.getLayout();
    }
    
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds, Boolean bruteForce){
        Producte p = new Producte(id, nom, similituds);
        cjtProductes.afegirProducte(p);
        crearPrestatgeria(bruteForce);
    }

    public void crearPrestatgeria(Boolean bruteForce){
        matSimilituds = cjtProductes.getMatriuSimilituds();
        vecProductes = cjtProductes.getVecProductes();
        int numProductes = vecProductes.length;
        prestatgeria = new Prestatgeria(1, numProductes);
        
        GenerarSolucio generadorInicial;

        if (bruteForce) {
            generadorInicial = new BruteForce(matSimilituds, vecProductes);
        } else {
            generadorInicial = new DosAproximacio(matSimilituds, vecProductes);
        }

        Producte[] solucio = generadorInicial.generarLayout();
        prestatgeria.setLayout(solucio);
        
        for (int i = 0; i < solucio.length; i++) {
            solucio[i].setColumna(i);
        }
    }

    public void modificarProducte(Integer idProdActual1, Integer idProdActual2, double novaSim, Integer nouId, String nouNom, Integer novaColumna, Boolean bruteForce) {
    	if (cjtProductes.getProducte(idProdActual1) != null) {
	    	if (nouId != null) {
	    		cjtProductes.editarIdProducte(idProdActual1, nouId);
	    		System.out.println("S'ha modificat el id del producte amb id " + idProdActual1);
	    	}
	    	if (nouNom != null) {
	    		cjtProductes.editarNomProducte(idProdActual1, nouNom);
	    		System.out.println("S'ha modificat el nom del producte amb id " + idProdActual1);
	    	}
	    	if ((cjtProductes.getProducte(idProdActual2) != null) && (novaSim > 0)) {
	    		cjtProductes.modificarSimilitud(idProdActual1, idProdActual2, novaSim);
	    		crearPrestatgeria(bruteForce);
	    		System.out.println("S'ha modificat la similitud entre el producte amb id " 
	    							+ idProdActual1 + " i el producte amb id " + idProdActual2);
	    	}
    	}
    	else System.out.println("No existeix producte amb id = idProdActual1");
    }

	public void modificarPrestatgeria(int pos1, int pos2){
        prestatgeria.intercanviarDosProductes(pos1, pos2);
    }

    public void esborrarProducte(int id, Boolean bruteForce){
        cjtProductes.eliminarProducte(id);
        crearPrestatgeria(bruteForce);
    }

    public void esborrarPrestatgeria(){
        prestatgeria.eliminarPrestatgeria();
    }

    public String getUsuariActual(){
        return UsuariActual.getUsername();
    }

    public void esborrarUsuari(){
        cjtUsuaris.eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }

    public void tancarSessio(){
        UsuariActual = new Usuari(null, null);
    }
}