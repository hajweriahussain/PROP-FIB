package Domini;


import Persistencia.CtrlPersistencia;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CtrlDomini {
    private Usuari UsuariActual;
    private CjtProductes cjtProductes;
    private CjtPrestatgeries cjtPrestatgeries;
    private CtrlPersistencia cp;


    private static CtrlDomini singletonObject;
    
    public CtrlDomini() {
        inicialitzarCtrlDomini();
    }

    public void inicialitzarCtrlDomini() {
    	cp = new CtrlPersistencia();
    }

    public static CtrlDomini getInstance() {
        if(singletonObject == null) {
            singletonObject = new CtrlDomini();
        }
        return singletonObject;
    }
   public void listToProductes(List<String> producteJsonList) {
        Gson gson = new Gson();
        Map<Integer, Producte> productes = new HashMap<>();

        for (String jsonProducte : producteJsonList) {
            Map<String, Object> producteData = gson.fromJson(jsonProducte, Map.class);

            Integer id = ((Double) producteData.get("id")).intValue(); // Gson usa Double para números en JSON
            String nom = (String) producteData.get("nom");
            Integer fila = ((Double) producteData.get("fila")).intValue();
            Integer columna = ((Double) producteData.get("columna")).intValue();

            Map<Double, Double> rawSimilituds = (Map<Double, Double>) producteData.get("similituds");
            Map<Integer, Double> similituds = new HashMap<>();
            if (rawSimilituds != null) {
                for (Map.Entry<Double, Double> entry : rawSimilituds.entrySet()) {
                    similituds.put(entry.getKey().intValue(), entry.getValue());
                }
            }
            cjtProductes.afegirProducte(id, nom, similituds);
            cjtProductes.getProducte(id).setColumna(columna);
            cjtProductes.getProducte(id).setColumna(fila);
            //MODIFICACIONS
        }

}

    public void iniciarSessio(String username, String pwd){
        cjtProductes = new CjtProductes(username);
        //ListToMapProductes(cp.importarProductes(username));
        cjtPrestatgeries = new CjtPrestatgeries(username);
        //ListToMapPrestatgeries(cp.importarPrestatgeries(username));
        UsuariActual = new Usuari(username, pwd);
    }

    public void crearUsuari(String name, String pwd){
    	//cp.afegirUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }
    
    public Producte[] llistarProductesUsuari() {
    	if (cjtProductes == null) {
            System.out.println("Error: No hi ha cap conjunt de productes associat a l'usuari.");
            return null;
        }
        return cjtProductes.getVecProductes();
    }
    
    public Map<String, Producte[][]> llistarPrestatgeriesUsuari() {
    	if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria creada.");
            return null;
        }
    	Map<String, Prestatgeria> prestatgeries = new HashMap<> (cjtPrestatgeries.getPrestatgeries(UsuariActual.getUsername()));
        Map<String, Producte[][]> pres = new HashMap<>();
        if(prestatgeries.isEmpty()) //Si no té teclats enviarà un missatge de que no té teclats
            return null;
        else {
            for(String clau : prestatgeries.keySet()) {
                pres.put(clau, prestatgeries.get(clau).getLayout());
            }
        }
        return pres;
    }
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds, Boolean bruteForce){
    	if (id <= 0 || nom == null) {
            System.out.println("Error: Dades del producte no vàlides.");
            return;
        }
        cjtProductes.afegirProducte(id, nom, similituds);
        
        //Modificacions aqui
    }

    public double[][] getMatSimilituds(Producte[] productes) {
        int n = productes.length;
        double[][] mat = new double[n][n];

        int idx = 0;
        for (Producte prod : productes) {
            int colx = 0;
            for (Producte prod2 : productes) {
                if (prod.getId() == prod2.getId()) {
                    mat[idx][idx] = 0.0;
                }
                else {
                    mat[idx][colx] = prod.getSimilitud(prod2.getId());
                }
                ++colx;
            }
            ++idx;
        }
        return mat;
    }
    public Producte[] getVecProductes(Set<Integer> productes){
        List<Producte> producteList = new ArrayList<>();

        for (Integer id : productes) {
            Producte p = cjtProductes.getProducte(id);
            if (p != null) {
                producteList.add(p);
            }
        }
        return producteList.toArray(new Producte[0]);
    }
    
    public void obtenirLayout(Set<Integer> productes, Boolean bruteForce){
        Producte[] vecProductes = getVecProductes(productes);
        double[][] matSimilituds = getMatSimilituds(vecProductes);
        GeneradorSolucio generadorInicial;

        if (bruteForce) {
            generadorInicial = new BruteForce(matSimilituds, vecProductes, numCols);
            System.out.println("Creant prestatgeria amb algoritme de força bruta.");
        } else {
            generadorInicial = new DosAproximacio(matSimilituds, vecProductes, numCols);
            System.out.println("Creant prestatgeria amb algoritme de 2-Aproximació.");
        }

        Producte[][] solucio = generadorInicial.generarLayout();
        cjtPrestatgeries.setLayout(solucio, id);

        for (int i = 0; i < solucio.length; i++) {
            for(int j = 0; j < solucio[0].length; ++j){
                solucio[i][j].setFila(i);
                solucio[i][j].setColumna(j);
            }
        }
    }

    public void crearPrestatgeria(int id, String nom, int numCols, Set<Integer> productes, Boolean bruteForce){
    	if (cjtProductes == null || cjtProductes.getVecProductes().length == 0) {
            System.out.println("Error: No hi ha productes per crear la prestatgeria.");
            return;
        }
        
        int numProductes = productes.size();
        int numFilas = numProductes / numCols;
        cjtPrestatgeries.crearPrestatgeria(id, nom, numFilas, numCols, productes);
        obtenirLayout(productes,bruteForce);
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
            //obtener las estanterias en que esta prod1
            //obtener las estanterias en que esta prod2
            //buscar si tienen estanterias en comun
            // si si tiene, sacar id de la estanteria, y numcolumnas de la estanteria y 
            //volver a calcular la disposicion de todas las estanterias en que se encuentran estos productos juntos.
            System.out.println("S'ha modificat la similitud entre el producte amb id " 
                                + idProdActual1 + " i el producte amb id " + idProdActual2);
    	}
    	else {
            System.out.println("No existeixen un dels productes amb id = " + idProdActual1 + " o id= " + idProdActual2 );
        }
    }

    public void modificarPrestatgeria(int id, int fila1, int col1, int fila2, int col2){
        if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria per modificar.");
            return;
        }
        cjtPrestatgeries.intercanviarDosProductes(id, fila1, col1, fila2, col2);
    }

    public void esborrarProducte(int id, Boolean bruteForce){
        cjtProductes.eliminarProducte(id);
        //elimino todas las estanterias en las que haya este producto
        //cjtpres.getprestatgeriesConProd(id);
        //llamar esborrarPrestageria sobre todas las estanterias
    }

    public void esborrarPrestatgeria(int id){
    	if (cjtPrestatgeries == null) {
            System.out.println("La prestatgeria no existeix.");
            return;
        }
        cjtPrestatgeries.esborrarPrestatgeria(id);
    }


    public void esborrarUsuari(){
    	if (UsuariActual == null) {
            System.out.println("No hi ha usuari actual.");
            return;
        }
//        eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }
    
    public List<String> productesToList(){
        Gson gson = new Gson();
        List<String> producteList = new ArrayList<>();
        Map<Integer, Producte> productes = cjtProductes.getProductes(UsuariActual.getUsername());

        if (productes != null) {
            for (Producte producte : productes.values()) {
                Map<String, Object> producteData = Map.of(
                    "id", producte.getId(),
                    "nom", producte.getNom(),
                    "fila", producte.getFila(),
                    "columna", producte.getColumna(),
                    "similituds", producte.getSimilituds()
                );
                String jsonProducte = gson.toJson(producteData);
                producteList.add(jsonProducte);
            }
        }
        return producteList;
    }
    
    public List<String> prestatgeriesToList(){
        Gson gson = new Gson();
        List<String> prestatgeriesList = new ArrayList<>();
        Map<Integer, Prestatgeria> prestatgeries = cjtPrestatgeries.getConjPrestatges(UsuariActual.getUsername());

        if (prestatgeries != null) {
            for (Prestatgeria p : prestatgeries.values()) {
                Map<String, Object> prestatgeriaData = Map.of(
                    "id", p.getId(),
                    "nom", p.getNom(),
                    "numFilas", p.getNumFilas(),
                    "numColumnas", p.getNumColumnas(),
                    "productes", p.getProductes(),
                    "layout", p.getLayout() //CANVIAR
                );
                String jsonProducte = gson.toJson(prestatgeriaData);
                prestatgeriesList.add(jsonProducte);
            }
        }
        return prestatgeriesList;
    }

    public void tancarSessio() {
        List<String> prestatgeries =  prestatgeriesToList();
        List<String> productes = productesToList();

        cp.guardarPrestageries(prestatgeries, UsuariActual.getUsername());
        cp.guardarProductes(productes, UsuariActual.getUsername());
        UsuariActual = new Usuari(null, null);
    }
}