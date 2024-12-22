package Domini;


import Exceptions.DominiException;
import Exceptions.PersistenciaException;
import Persistencia.CtrlPersistencia;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    
   public void listToProductes(List<String> producteJsonList) throws DominiException{
        cjtProductes.setMapProductes(cjtProductes.listToProductes(producteJsonList));
   }
   public void listToPrestatgeries(List<String> presJsonList) {
       cjtPrestatgeries.setMapPrestatgeries(cjtPrestatgeries.listToPrestatgeries(presJsonList));
       //ARREGLAR, tiene que devolver lo que devuelve la funcion de productos, y necesito un setMapPrestatgeries.
   }

    public void iniciarSessio(String username, String pwd) throws PersistenciaException, DominiException{
        cjtProductes = new CjtProductes(username);
        listToProductes(cp.importarProductes(username));
        cjtPrestatgeries = new CjtPrestatgeries(username);
        listToPrestatgeries(cp.importarPrestatgeries(username)); //ARREGLAR es importar prestatgeries, PLURAL
        UsuariActual = new Usuari(username, pwd);
    }

    public void crearUsuari(String name, String pwd) throws PersistenciaException, DominiException{
    	cp.afegirUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }
    
    public Map<String, Map<String,String>> llistarProductesUsuari() throws DominiException{
    	if (cjtProductes == null) {
            System.out.println("Error: No hi ha cap conjunt de productes associat a l'usuari.");
            return null;
        }
        return cjtProductes.llistarProductesUsuari();
    }
    
    public Map<String, Map<String,String>> llistarPrestatgeriesUsuari() {
    	if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria creada.");
            return null;
        }
        return cjtPrestatgeries.llistarPrestatgeriesUsuari();
    }
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds) throws DominiException{
    	if (id <= 0 || nom == null) {
            System.out.println("Error: Dades del producte no vàlides.");
            return;
        }
        cjtProductes.afegirProducte(id, nom, similituds);
    }
    
    public int[] setIdsToVecIds(Set<Integer> productes){
        if (productes == null || productes.isEmpty()) {
            return new int[0]; // Devuelve un array vacío
        }

        return productes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    
    public void obtenirLayout(int id, Set<Integer> productes, Boolean bruteForce, int numCols) throws DominiException{
        int[] vecProductes = setIdsToVecIds(productes);
        double[][] matSimilituds = cjtProductes.getMatriuSimilitudsPerIds(vecProductes);

        GeneradorSolucio generadorInicial;

        if (bruteForce) {
            generadorInicial = new BruteForce(matSimilituds, vecProductes, numCols);
            System.out.println("Creant prestatgeria amb algoritme de força bruta.");
        } else {
            generadorInicial = new DosAproximacio(matSimilituds, vecProductes, numCols);
            System.out.println("Creant prestatgeria amb algoritme de 2-Aproximació.");
        }

        Integer[][] solucio = generadorInicial.generarLayout();
        cjtPrestatgeries.setLayout(cjtProductes.getMatProductes(solucio), id);

        for (int i = 0; i < solucio.length; i++) {
            for(int j = 0; j < solucio[0].length; ++j){
                Pair<Integer, Integer> p = new Pair<>(i, j);
                cjtProductes.editarPosProducte(solucio[i][j], id, p);
            }
        }
    }

    public void crearPrestatgeria(int id, String nom, int numCols, Set<Integer> productes, Boolean bruteForce) throws DominiException{
    	if (cjtProductes == null || cjtProductes.getVecProductes().length == 0) {
            System.out.println("Error: No hi ha productes per crear la prestatgeria.");
            return;
        }
        
        int numProductes = productes.size();
        int numFilas = numProductes / numCols;
        cjtPrestatgeries.crearPrestatgeria(id, nom, numFilas, numCols, productes);
        obtenirLayout(id, productes,bruteForce, numCols);
    }

    public void modificarProducte(Integer idProdActual1, Integer nouId, String nouNom) throws DominiException{
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
    
    public void modificarSimilituds(Integer idProdActual1, Integer idProdActual2, double novaSim, Boolean bruteForce) throws DominiException {
    	if (cjtProductes.getProducte(idProdActual1) != null && cjtProductes.getProducte(idProdActual2) != null) {
            cjtProductes.modificarSimilitud(idProdActual1, idProdActual2, novaSim);
            
            cjtProductes.getPosPrestatgeriesProducte(idProdActual1);
            cjtProductes.getPosPrestatgeriesProducte(idProdActual2);
            Integer[] idsPres = cjtPrestatgeries.getIdsPrestatgeriesSame(cjtProductes.getPosPrestatgeriesProducte(idProdActual1), 
                                                    cjtProductes.getPosPrestatgeriesProducte(idProdActual2) );
             
            if( idsPres.length != 0){
                for(Integer i : idsPres){
                    Set<Integer> productes = cjtPrestatgeries.getProductes(idsPres[i]);
                    int cols = cjtPrestatgeries.getNumCols(idsPres[i]);
                    obtenirLayout(idsPres[i], productes, bruteForce, cols);
                }
            }
            System.out.println("S'ha modificat la similitud entre el producte amb id " 
                                + idProdActual1 + " i el producte amb id " + idProdActual2);
    	}
    	else {
            System.out.println("No existeixen un dels productes amb id = " + idProdActual1 + " o id= " + idProdActual2 );
        }
    }

    public void modificarPrestatgeria(int id, int idp1, int idp2) throws DominiException{
        if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria per modificar.");
            return;
        }
        Pair<Integer, Integer> pos1 = cjtProductes.getPosProducte(id, idp1);
        Pair<Integer, Integer> pos2 = cjtProductes.getPosProducte(id, idp2);
        cjtPrestatgeries.intercanviarDosProductes(id, pos1.clau, pos1.valor, pos2.clau, pos2.valor);
        cjtProductes.editarPosProducte(id, idp1, pos2);
        cjtProductes.editarPosProducte(id, idp2, pos1);
    }

    public void esborrarProducte(int id) throws DominiException{
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


    public void esborrarUsuari() throws PersistenciaException, DominiException{
    	if (UsuariActual == null) {
            System.out.println("No hi ha usuari actual.");
            return;
        }
        cp.eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }
    
    
    public boolean existeixUsuari(String us) throws PersistenciaException {
        return cp.existeixUsuari(us);
    }
    
    public Boolean comprovarUsuari(String username, String pwd) throws PersistenciaException{
        return cp.verificarContrasenya(username, pwd);
    }
    
    public String getUsuariActual(){
        return UsuariActual.getUsername();
    }
    
    public void LlegirProducteFitxer(String path) throws PersistenciaException, DominiException{
        List<String> prodInfo = cp.importarFitxerProducte(path);
        afegirProducteFitxer(prodInfo);

    }
    public void LlegirPrestatgeriaFitxer(String nom, String id, String cols, String path) throws PersistenciaException, DominiException{
        List<String> pres = cp.importarFitxerPrestatgeria(path);
        afegirPrestatgeriaFitxer(nom, id, cols, pres);

    }
    
    private void afegirProducteFitxer(List<String> prodInfo) throws DominiException{
        int idProd = Integer.parseInt(prodInfo.get(0));
         Map<Integer, Double> mapSims = prodInfo.subList(2, prodInfo.size()).stream() // Tomar los elementos desde el índice 2
            .map(entry -> entry.split(":")) // Dividir cada elemento por ":"
            .collect(Collectors.toMap(
                parts -> Integer.valueOf(parts[0]), // Clave: convertir la primera parte a Integer
                parts -> Double.valueOf(parts[1])  // Valor: convertir la segunda parte a Double
            ));
        
        cjtProductes.comprovarSims(mapSims);
        // Crear el producto con los datos procesados
        crearProducte(idProd, prodInfo.get(1), mapSims);
       
    }

    private void afegirPrestatgeriaFitxer(String nomPres, String id, String cols, List<String> pres) throws DominiException{
        int idPres = Integer.parseInt(id);
        int numCols = Integer.parseInt(cols);
        Set<Integer> prods = pres.stream()
                                        .map(Integer::valueOf) // Convertir cada String a Integer
                                        .collect(Collectors.toSet());
        
        crearPrestatgeria(idPres, nomPres, numCols, prods, true);
    }
    
    public List<String> productesToList() throws DominiException{
        return cjtProductes.productesToList(cjtProductes.getProductes(UsuariActual.getUsername()));
    }
    
    public List<String> prestatgeriesToList(){
        //   ARREGLAR, FALTA FUNCION
        return cjtPrestatgeries.prestatgeriesToList(cjtPrestatgeries.getConjPrestatges(UsuariActual.getUsername()));
    }
    
    public void canviarContrasenya(String username, String novaContra) throws PersistenciaException{
        cp.canviarContrasenya(username, novaContra);
    }

    public void tancarSessio() throws PersistenciaException, DominiException{
        List<String> prestatgeries =  prestatgeriesToList();
        List<String> productes = productesToList();
        cp.guardarPrestatgeries(prestatgeries, UsuariActual.getUsername());
        cp.guardarProductes(productes, UsuariActual.getUsername());
        UsuariActual = new Usuari(null, null);
    }
}
