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
        //cjtProductes.setProductes(cjtProductes.listToProductes(producteJsonList));
        //ARREGLAR
}
   public void listToPrestatgeries(List<String> presJsonList){
       cjtPrestatgeries.listToPrestatgeries(presJsonList);
       //ARREGLAR me tiene que devolver el map de prestatgeries
   }

    public void iniciarSessio(String username, String pwd){
        cjtProductes = new CjtProductes(username);
        listToProductes(cp.importarProductes(username));
        cjtPrestatgeries = new CjtPrestatgeries(username);
        listToPrestatgeries(cp.importarPrestatgeria(username)); //ARREGLAR es importar prestatgeries, PLURAL
        UsuariActual = new Usuari(username, pwd);
    }

    public void crearUsuari(String name, String pwd){
    	cp.afegirUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }
    
    public Map<String, Map<String,String>> llistarProductesUsuari() {
    	if (cjtProductes == null) {
            System.out.println("Error: No hi ha cap conjunt de productes associat a l'usuari.");
            return null;
        }
        return cjtProductes.llistarProductes();
        //ARREGLAR, TIENES QUE AÑADIR LOS MAPS
    }
    
    public Map<String, Map<String,String>> llistarPrestatgeriesUsuari() {
    	if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria creada.");
            return null;
        }
//        return cjtPrestatgeries.llistarPrestatgeriesUsuari();
        //ARREGLAR, no tiene que ser void y imprimir los atributos, tiene que guardar todas las estanterias en un map de maps.
        return new HashMap<>();
    }
    
    
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds, Boolean bruteForce){
    	if (id <= 0 || nom == null) {
            System.out.println("Error: Dades del producte no vàlides.");
            return;
        }
        cjtProductes.afegirProducte(id, nom, similituds);
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
    
    public void obtenirLayout(int id, Set<Integer> productes, Boolean bruteForce, int numCols){
//        Producte[] vecProductes = cjtProductes.getProductesPerIds(productes);
//         ARREGLAR, tiene que coger un set de integers como param, y devuelve un vector de productos.
//        double[][] matSimilituds = cjtProductes.getMatriuSimilitudsPerIds(vecProductes);
        // ARREGLAR, tiene que coger un vector de productos como param

//        GeneradorSolucio generadorInicial;
//
//        if (bruteForce) {
//            generadorInicial = new BruteForce(matSimilituds, vecProductes, numCols);
//            System.out.println("Creant prestatgeria amb algoritme de força bruta.");
//        } else {
//            generadorInicial = new DosAproximacio(matSimilituds, vecProductes, numCols);
//            System.out.println("Creant prestatgeria amb algoritme de 2-Aproximació.");
//        }
//
//        Producte[][] solucio = generadorInicial.generarLayout();
//        //PENSANDO EN QUITAR EL LAYOUT CON LAS INSTANCIAS DE PRODUCTOS, SOBRA.
//        cjtPrestatgeries.setLayout(solucio, id);
//
//        for (int i = 0; i < solucio.length; i++) {
//            for(int j = 0; j < solucio[0].length; ++j){
////                Pair<Integer, Integer> p = Pair<>(i, j);
//                cjtProductes.editarPosProducte(solucio[i][j].getId(), id, p);
//            }
//        }
    }

    public void crearPrestatgeria(int id, String nom, int numCols, Set<Integer> productes, Boolean bruteForce){
    	if (cjtProductes == null || cjtProductes.getVecProductes().length == 0) {
            System.out.println("Error: No hi ha productes per crear la prestatgeria.");
            return;
        }
        
        int numProductes = productes.size();
        int numFilas = numProductes / numCols;
        cjtPrestatgeries.crearPrestatgeria(id, nom, numFilas, numCols, productes);
        obtenirLayout(id, productes,bruteForce, numCols);
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
            
            cjtProductes.getPosPrestatgeriesProducte(idProdActual1);
            cjtProductes.getPosPrestatgeriesProducte(idProdActual2);
             //ARREGLAR, cjtprest haura de crear funcio per a buscar estanteries en comu en un map.
            //buscar si tienen estanterias en comun
            // si si tiene, sacar id de la estanteria, y numcolumnas de la estanteria y 
            //volver a calcular la disposicion de todas las estanterias en que se encuentran estos productos juntos.
            //si me devuelbve un set de ids de estanterias, pues un for que vaya llamando a obtenirlayout para todas.
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
        Map<Integer, Pair<Integer, Integer>> pres = cjtProductes.getPosPrestatgeriesProducte(id);
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : pres.entrySet()) {
            Integer key = entry.getKey();
            esborrarPrestatgeria(key);
        }
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
        cp.eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }
    
    
    public boolean existeixUsuari(String us) {
        return cp.existeixUsuari(us);
    }
    
    public Boolean comprovarUsuari(String username, String pwd) {
        return cp.verificarContrasenya(username, pwd);
    }
    
    public String getUsuariActual(){
        return UsuariActual.getUsername();
    }
    
    public List<String> productesToList(){
        return cjtProductes.productesToList(cjtProductes.getProductes(UsuariActual.getUsername()));
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
                    "layout", p.getDisp()
                );
                String jsonPrestatgeria = gson.toJson(prestatgeriaData);
                prestatgeriesList.add(jsonPrestatgeria);
            }
        }
        return prestatgeriesList;
        //   ARREGLAR, FALTA FUNCION
//        return cjtPrestatgeries.prestatgeriesToList();
    }

    public void tancarSessio() {
        List<String> prestatgeries =  prestatgeriesToList();
        List<String> productes = productesToList();
        cp.guardarPrestatgeries(prestatgeries, UsuariActual.getUsername());
        cp.guardarProductes(productes, UsuariActual.getUsername());
        UsuariActual = new Usuari(null, null);
    }
}
