package Domini;

import java.util.Map;

public class CtrlDomini {
    private Usuari UsuariActual;
    private CjtUsuaris cjtUsuaris;
    private CjtProductes cjtProductes;
    private Prestatgeria prestatgeria;
    private double[][] matSimilituds;


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
//        Boolean bruteForce;
//        crearPrestatgeria(bruteForce);
        UsuariActual = cjtUsuaris.getUsuari(username);
    }

    public void crearUsuari(String name, String pwd){
        cjtUsuaris.crearUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }

    public void canviarUsuari(String nomUsuari, String pwd) {
        //per a aquesta entrega només crida a iniciar sessió, però per les següents haurà de cridar a la base de dades i guardar el que calgui
        iniciarSessio(nomUsuari, pwd); //canviem l'usuari actual
    }

    public void crearProducte(int id, String nom, Map<Integer, Double> similituds){
        Producte p = new Producte(id, nom, similituds);
        cjtProductes.afegirProducte(p);
    }

    public void crearPrestatgeria(Boolean bruteForce){
        double[][] matSimilituds = cjtProductes.obtenirMatriuSimilituts();

        Producte[] vecProd = cjtProductes.obtenirProductes();
        int numProductes = vecProd.length;
        prestatgeria = new Prestatgeria(1, numProductes);

        //Interface
        GenerarSolucio generadorInicial;

        if (bruteForce) {
            generadorInicial = new BruteForce(matSimilituds, vecProd);
        } else {
            generadorInicial = new DosAproximacio(matSimilituds, vecProd);
        }

        Producte[] solucio = generadorInicial.generarLayout();
        prestatgeria.setLayout(solucio);
        
        for (int i = 0; i < solucio.length; i++) {
            solucio[i].setColumna(i);
        }

    }

    void modificarProducte(Integer idProdActual1, Integer idProdActual2, double novaSim, Integer nouId, String nouNom, Integer novaColumna) {
    	if (cjtProductes.getProducte(idProdActual1) != null) {
	    	if (nouId != null) {
	    		cjtProductes.editarIdProducte(idProdActual1, nouId);
	    	}
	    	if (nouNom != null) {
	    		cjtProductes.editarNomProducte(idProdActual1, nouNom);
	    	}
	    	if (novaColumna != null) {
	    		cjtProductes.editarPosProducte(idProdActual1, novaColumna);
	    	}
	    	if ((cjtProductes.getProducte(idProdActual2) != null) && (novaSim > 0)) {
	    		cjtProductes.modificarSimilitud(idProdActual1, idProdActual2, novaSim);
	    	}
    	}
    	else System.out.println("No existeix productes amb id = idProdActual");
    }

    void modificarPrestatgeria(int pos1, int pos2){
        prestatgeria.intercanviarDosProductes(pos1, pos2);
    }

    public void esborrarProducte(int id){
        cjtProductes.eliminarProducte(id);
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