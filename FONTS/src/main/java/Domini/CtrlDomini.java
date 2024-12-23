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

/**
 * Classe CtrlDomini
 * 
 * Aquesta classe actua com a controlador principal per a la gestió de la lògica de domini
 * de l'aplicació. Gestiona usuaris, productes i prestatgeries, i coordina la comunicació
 * amb la capa de persistència. Implementa el patró Singleton.
 * @author [hajweria.hussain]
 * @version 1.0
 */

public class CtrlDomini {
    private Usuari UsuariActual;
    private CjtProductes cjtProductes;
    private CjtPrestatgeries cjtPrestatgeries;
    private CtrlPersistencia cp;


    private static CtrlDomini singletonObject;
    
    /**
     * Constructor de la classe CtrlDomini. Inicialitza el controlador de
     * domini.
     */
    public CtrlDomini() {
        inicialitzarCtrlDomini();
    }
    
    /**
     * Inicialitza els components necessaris del controlador de domini.
     */
    public void inicialitzarCtrlDomini() {
    	cp = new CtrlPersistencia();
    }

    public static CtrlDomini getInstance() {
        if(singletonObject == null) {
            singletonObject = new CtrlDomini();
        }
        return singletonObject;
    }
    
    /**
     * Converteix una llista de JSON de productes en objectes de producte i els
     * afegeix al conjunt de productes.
     *
     * @param producteJsonList Llista de JSON de productes.
     * @throws DominiException Si ocorre un error en el procés.
     */
    public void listToProductes(List<String> producteJsonList) throws DominiException{
         cjtProductes.setMapProductes(cjtProductes.listToProductes(producteJsonList));
    }
    
    /**
     * Converteix una llista de JSON de prestatgeries en objectes de
     * prestatgeria i les afegeix al conjunt de prestatgeries.
     *
     * @param presJsonList Llista de JSON de prestatgeries.
     * @throws DominiException Si ocorre un error en el procés.
     */
    public void listToPrestatgeries(List<String> presJsonList) throws DominiException {
        try{
            cjtPrestatgeries.setMapPrestatgeries(cjtPrestatgeries.listToPrestatgeries(presJsonList));
        }catch(DominiException e){
            throw new DominiException("Error al guarda la estanteria " + e.getMessage());
        }
        //ARREGLAR, tiene que devolver lo que devuelve la funcion de productos, y necesito un setMapPrestatgeries.
    }
    
     /**
     * Inicia una sessió per a un usuari especificat.
     * @param username Nom d'usuari.
     * @param pwd Contrasenya de l'usuari.
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error de domini.
     */
    public void iniciarSessio(String username, String pwd) throws PersistenciaException, DominiException{
        cjtProductes = new CjtProductes(username);
        listToProductes(cp.importarProductes(username));
        cjtPrestatgeries = new CjtPrestatgeries(username);
        try{
            listToPrestatgeries(cp.importarPrestatgeries(username)); //ARREGLAR es importar prestatgeries, PLURAL
        }catch(DominiException e){
            throw new DominiException("Error al guardarrrrr la estanteria " + e.getMessage());
        }
        UsuariActual = new Usuari(username, pwd);
    }
    
    /**
     * Crea un nou usuari i inicia la sessió amb aquest usuari.
     *
     * @param name Nom d'usuari.
     * @param pwd Contrasenya de l'usuari.
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error de domini.
     */
    public void crearUsuari(String name, String pwd) throws PersistenciaException, DominiException{
    	cp.afegirUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }
    
    /**
     * Llista els productes associats a l'usuari actual.
     *
     * @return Mapa de productes amb la seva informació.
     * @throws DominiException Si ocorre un error de domini.
     */
    public Map<String, Map<String,String>> llistarProductesUsuari() throws DominiException{
    	if (cjtProductes == null) {
            System.out.println("Error: No hi ha cap conjunt de productes associat a l'usuari.");
            return null;
        }
        return cjtProductes.llistarProductesUsuari();
    }
    
    /**
     * Llista les prestatgeries associades a l'usuari actual.
     *
     * @return Mapa de prestatgeries amb la seva informació.
     */
    public Map<String, Map<String,String>> llistarPrestatgeriesUsuari() {
    	if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria creada.");
            return null;
        }
        return cjtPrestatgeries.llistarPrestatgeriesUsuari();
    }
    
    /**
     * Crea un nou producte amb l'identificador, el nom i les similituds
     * especificades.
     *
     * @param id Identificador del producte.
     * @param nom Nom del producte.
     * @param similituds Mapa de similituds amb altres productes.
     * @throws DominiException Si ocorre un error de domini.
     */
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds) throws DominiException{
    	if (id <= 0 || nom == null) {
            System.out.println("Error: Dades del producte no vàlides.");
            return;
        }
        cjtProductes.afegirProducte(id, nom, similituds);
    }
    
    /**
     * Assigna identificadors a un vector d'identificadors de productes.
     * @param productes Conjunt d'identificadors de productes.
     * @return Vector d'identificadors de productes.
     */
    public int[] setIdsToVecIds(Set<Integer> productes){
        if (productes == null || productes.isEmpty()) {
            return new int[0]; // Devuelve un array vacío
        }

        return productes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Genera un layout per a una prestatgeria especificada.
     *
     * @param id Identificador de la prestatgeria.
     * @param productes Conjunt de productes per a la prestatgeria.
     * @param bruteForce Si es vol utilitzar l'algoritme de força bruta.
     * @param numCols Nombre de columnes de la prestatgeria.
     * @throws DominiException Si ocorre un error de domini.
     */
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
                if(solucio[i][j] != null){
                    Pair<Integer, Integer> p = new Pair<>(i, j);
                    cjtProductes.editarPosProducte(solucio[i][j], id, p);
                }
            }
        }
    }
    
    /**
     * Crea una nova prestatgeria amb els paràmetres especificats.
     *
     * @param id Identificador de la prestatgeria.
     * @param nom Nom de la prestatgeria.
     * @param numCols Nombre de columnes de la prestatgeria.
     * @param productes Conjunt de productes a afegir a la prestatgeria.
     * @param bruteForce Si es vol utilitzar l'algoritme de força bruta.
     * @throws DominiException Si ocorre un error de domini.
     */
    public void crearPrestatgeria(int id, String nom, int numCols, Set<Integer> productes, Boolean bruteForce) throws DominiException{
    	if (productes.isEmpty()){
            throw new DominiException("Error: No s'han seleccionat productes");
        }
        
        int numProductes = productes.size();
        int numFilas = numProductes / numCols;
        if (numFilas == 0){
            if (numCols != numProductes) throw new DominiException("Error: Num columnes no valid");
        }
        cjtPrestatgeries.crearPrestatgeria(id, nom, numFilas, numCols, productes);
        obtenirLayout(id, productes,bruteForce, numCols);
    }
    
    /**
     * Modifica un producte existent amb els nous valors especificats.
     *
     * @param idProdActual1 Identificador actual del producte.
     * @param nouId Nou identificador per al producte (pot ser null).
     * @param nouNom Nou nom per al producte (pot ser "-").
     * @throws DominiException Si ocorre un error de domini.
     */
    public void modificarProducte(Integer idProdActual1, Integer nouId, String nouNom) throws DominiException{
        if (cjtProductes.getProducte(idProdActual1) != null) {
            
            if (nouId != null) {
                cjtProductes.editarIdProducte(idProdActual1, nouId);
                System.out.println("S'ha modificat el id del producte amb id " + idProdActual1);
            }

            if (nouNom != "-") {
                cjtProductes.editarNomProducte(idProdActual1, nouNom);
                System.out.println("S'ha modificat el nom del producte amb id " + idProdActual1);
            }
            
        } else {
            System.out.println("No existeix producte amb id = " + idProdActual1);
        }
    }
    
    /**
     * Modifica la similitud entre dos productes especificats.
     *
     * @param idProdActual1 Identificador del primer producte.
     * @param idProdActual2 Identificador del segon producte.
     * @param novaSim Nova similitud entre els productes.
     * @param bruteForce Si es vol utilitzar l'algoritme de força bruta per al
     * càlcul.
     * @throws DominiException Si ocorre un error durant el procés.
     */
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
    
    /**
     * Modifica la configuració d'una prestatgeria intercanviant dos productes
     * especificats.
     *
     * @param id Identificador de la prestatgeria.
     * @param idp1 Identificador del primer producte.
     * @param idp2 Identificador del segon producte.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void modificarPrestatgeria(int id, int idp1, int idp2) throws DominiException{
        if (cjtPrestatgeries == null) {
            System.out.println("Error: No hi ha cap prestatgeria per modificar.");
            return;
        }
        Pair<Integer, Integer> pos1 = cjtProductes.getPosProducte(idp1, id);
        Pair<Integer, Integer> pos2 = cjtProductes.getPosProducte(idp2, id);
        System.out.println("pos1 " + pos1 + " " + " pos2 " + pos2);
        cjtPrestatgeries.intercanviarDosProductes(id, pos1.clau, pos1.valor, pos2.clau, pos2.valor);
//        cjtProductes.editarPosProducte(idp1, id, pos2);
//        cjtProductes.editarPosProducte(idp2, id, pos1);
    }
    
    /**
     * Elimina un producte i totes les prestatgeries associades a aquest
     * producte.
     *
     * @param id Identificador del producte a eliminar.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void esborrarProducte(int id) throws DominiException{
        Map<Integer, Pair<Integer, Integer>> pres = cjtProductes.getPosPrestatgeriesProducte(id);
        cjtProductes.eliminarProducte(id);
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : pres.entrySet()) {
            Integer key = entry.getKey();
            esborrarPrestatgeria(key);
        }
    }
    
    /**
     * Elimina una prestatgeria especificada.
     *
     * @param id Identificador de la prestatgeria a eliminar.
     */
    public void esborrarPrestatgeria(int id) throws DominiException{
    	if (cjtPrestatgeries == null) {
            System.out.println("La prestatgeria no existeix.");
            return;
        }
        Set<Integer> prods = cjtPrestatgeries.getProductes(id);
        
        for (Integer idP : prods){
            cjtProductes.esborrarPosPrestatgeria(id, idP);
        }
        cjtPrestatgeries.esborrarPrestatgeria(id);
    }

    /**
     * Elimina l'usuari actual del sistema.
     *
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void esborrarUsuari() throws PersistenciaException, DominiException{
    	if (UsuariActual == null) {
            System.out.println("No hi ha usuari actual.");
            return;
        }
        cp.eliminarUsuari(UsuariActual.getUsername());
        tancarSessio(); 
    }
    
    /**
     * Comprova si un usuari existeix al sistema.
     *
     * @param us Nom d'usuari a comprovar.
     * @return Cert si l'usuari existeix, fals altrament.
     * @throws PersistenciaException Si ocorre un error de persistència.
     */
    public boolean existeixUsuari(String us) throws PersistenciaException {
        return cp.existeixUsuari(us);
    }
    
    /**
     * Comprova les credencials d'un usuari.
     *
     * @param username Nom d'usuari.
     * @param pwd Contrasenya.
     * @return Cert si les credencials són correctes, fals altrament.
     * @throws PersistenciaException Si ocorre un error de persistència.
     */
    public Boolean comprovarUsuari(String username, String pwd) throws PersistenciaException{
        return cp.verificarContrasenya(username, pwd);
    }
    
    /**
     * Obté el nom de l'usuari actual.
     *
     * @return Nom de l'usuari actual.
     */
    public String getUsuariActual(){
        return UsuariActual.getUsername();
    }
    
    /**
     * Llegeix un fitxer de productes i afegeix els productes al conjunt de
     * productes.
     *
     * @param path Ruta del fitxer de producte.
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void LlegirProducteFitxer(String path) throws PersistenciaException, DominiException{
        List<String> prodInfo = cp.importarFitxerProducte(path);
        afegirProducteFitxer(prodInfo);

    }
    
    /**
     * Llegeix un fitxer de prestatgeries i afegeix la prestatgeria al conjunt
     * de prestatgeries.
     *
     * @param path Ruta del fitxer de prestatgeria.
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void LlegirPrestatgeriaFitxer(String path) throws PersistenciaException, DominiException{
        List<String> pres = cp.importarFitxerPrestatgeria(path);
        afegirPrestatgeriaFitxer(pres);

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
        crearProducte(idProd, prodInfo.get(1), mapSims);
       
    }

    private void afegirPrestatgeriaFitxer(List<String> presInfo) throws DominiException{
        int id = Integer.parseInt(presInfo.get(0));
        int numCols = Integer.parseInt(presInfo.get(2));
        Set<Integer> prods = presInfo.subList(3, presInfo.size()).stream()
                                        .map(Integer::valueOf) // Convertir cada String a Integer
                                        .collect(Collectors.toSet());
        
        crearPrestatgeria(id, presInfo.get(1), numCols, prods, true);
    }
    
    /**
     * Converteix els productes associats a l'usuari actual en una llista de
     * cadenes JSON.
     *
     * @return Llista de cadenes JSON representant els productes.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public List<String> productesToList() throws DominiException{
        return cjtProductes.productesToList(cjtProductes.getProductes(UsuariActual.getUsername()));
    }
    
    /**
     * Converteix les prestatgeries associades a l'usuari actual en una llista
     * de cadenes JSON.
     *
     * @return Llista de cadenes JSON representant les prestatgeries.
     */
    public List<String> prestatgeriesToList() throws DominiException{
        try{
            return cjtPrestatgeries.prestatgeriesToList(cjtPrestatgeries.getConjPrestatges(UsuariActual.getUsername()));
        } catch (DominiException e){
            throw new DominiException("Error al guardar les prestatgeries del usuari." + e.getMessage());
        }
    }
    
    /**
     * Canvia la contrasenya d'un usuari.
     * @param username Nom d'usuari.
     * @param novaContra Nova contrasenya.
     * @throws PersistenciaException Si ocorre un error de persistència.
     */
    public void canviarContrasenya(String username, String novaContra) throws PersistenciaException{
        cp.canviarContrasenya(username, novaContra);
    }
    
    /**
     * Tanca la sessió de l'usuari actual, guardant les dades de productes i prestatgeries.
     * @throws PersistenciaException Si ocorre un error de persistència.
     * @throws DominiException Si ocorre un error durant el procés.
     */
    public void tancarSessio() throws PersistenciaException, DominiException{
        List<String> prestatgeries =  prestatgeriesToList();
        List<String> productes = productesToList();
        cp.guardarPrestatgeries(prestatgeries, UsuariActual.getUsername());
        cp.guardarProductes(productes, UsuariActual.getUsername());
        UsuariActual = new Usuari(null, null);
    }
}
    