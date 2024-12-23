package Domini;

import Exceptions.DominiException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
/**
 * Classe que representa una prestatgeria.
 * 
 * Aquesta classe gestiona una prestatgeria amb un layout de productes
 * organitzats en files i columnes. Permet gestionar productes, actualitzar
 * el layout i intercanviar productes.
 * 
 * @author [hajweria.hussain]
 * @version 1.0
 */
public class Prestatgeria {
    private LinkedList<Producte[]> layout;
    private List<List<Pair<String, Integer>>> disp;
    private Set<Integer> productes;
    private int id;
    private String nom;
    private int numFilas;
    private int numColumnas;
    

    /**
     * Constructor per crear una prestatgeria amb les configuracions inicials.
     * 
     * @param id         L'identificador únic de la prestatgeria.
     * @param nom        El nom de la prestatgeria.
     * @param filas      El nombre de files de la prestatgeria.
     * @param columnas   El nombre de columnes de la prestatgeria.
     * @param productes  Un conjunt d'identificadors dels productes presents a la prestatgeria.
     */
    public Prestatgeria(int id, String nom,  int filas, int columnas, Set<Integer> productes){
    	if (id <= 0) {
            System.out.println("L'ID ha de ser un valor positiu.");
        }
        if (filas <= 0) {
            System.out.println("El número de files ha de ser major que zero.");
        }
        if (columnas <= 0) {
            System.out.println("El número de columnes ha de ser major que zero.");
        }
        if (productes == null) {
            System.out.println("El conjunt de productes no pot ser null.");
        }
        if (nom == null || nom.trim().isEmpty() || nom.length() <= 4) {
            System.out.println("El nom de la prestatgeria no pot estar buit.");
        }
        if (productes.isEmpty()) {
            System.out.println("Hi ha d'haver al menys 1 producte a afegir a la prestatgeria");
        }

        this.id = id;
        this.numFilas = filas;
        this.numColumnas = columnas;
        this.nom = nom;
        this.productes = productes;

        // Inicialización de layout
        this.layout = new LinkedList<>();
        this.disp = new ArrayList<>();
        for (int i = 0; i < this.numFilas; i++) {
            layout.add(new Producte[this.numColumnas]);
        }
    }
    
    /**
     * Obté un prestatge (fila) de la prestatgeria.
     * 
     * @param indexFila L'índex de la fila que es vol obtenir.
     * @return Un array de productes corresponent a la fila especificada,
     *         o null si l'índex està fora de rang.
     */
    public Producte[] getPrestatge(int indexFila) {
        if (indexFila < 0 || indexFila >= layout.size()) {
            System.out.println("Error: Índex fora de rang.");
            return null;
        }
        return layout.get(indexFila);
    }
    
    /**
     * Obté l'identificador únic de la prestatgeria.
     * 
     * @return L'ID de la prestatgeria.
     */
    public int getId() {
        return id;
    }
    
    /**
    * Obté el nom de la prestatgeria.
    * 
    * @return El nom de la prestatgeria.
    */
    public String getNom() {
        return nom;
    }
    
    /**
    * Obté el nombre de files de la prestatgeria.
    * 
    * @return El nombre de files.
    */
    public int getNumFilas() {
        return numFilas;
    }
    
    /**
     * Obté el nombre de columnes de la prestatgeria.
     *
     * @return El nombre de columnes.
     */
    public int getNumColumnas() {
        return numColumnas;
    }
    
    /**
     * Obté el conjunt d'identificadors dels productes presents a la
     * prestatgeria.
     *
     * @return Un conjunt d'IDs de productes.
     */
    public Set<Integer> getProductes() {
        return productes;
    }
    
    /**
     * Obté el layout complet de la prestatgeria en forma de matriu.
     *
     * @return Una matriu 2D de productes que representa el layout actual.
     */
    public Producte[][] getLayout() {
        Producte[][] disposicio = new Producte[numFilas][numColumnas];

        // Rellenar la matriz con el contenido de la LinkedList
        for (int i = 0; i < numFilas; i++) {
            Producte[] fila = layout.get(i);
            for (int j = 0; j < numColumnas; j++) {
                disposicio[i][j] = fila[j];
            }
        }

        return disposicio;
    }
    
    /**
     * Obté la disposició actual dels productes en forma de llista de llistes.
     *
     * Cada llista interna representa una fila de la prestatgeria, i cada parell
     * (Pair) conté el nom del producte i el seu identificador únic.
     *
     * @return Una llista de llistes amb parells (nom del producte, ID).
     */
    public List<List<Pair<String, Integer>>> getDisp() {
        return disp;
    }
    
    
    public void setId(int id){
    	if (id <= 0) {
            System.out.println("Error: L'ID ha de ser un valor positiu. No es canvia l'ID.");
        } else {
            this.id = id;
        }
    }
    
    
    
    /**
     * Estableix un nou layout per a la prestatgeria.
     *
     * @param disposicio Una matriu 2D de productes que representa el nou
     * layout. La matriu ha de ser vàlida i no nul·la.
     */
    public void setLayout(Producte[][] disposicio) {
        if (disposicio == null || disposicio.length == 0 || disposicio[0].length == 0) {
            System.out.println("La matriu proporcionada és nul·la o buida. No es pot assignar el layout.");
        }

        int novesFiles = disposicio.length;
        int novesColumnes = disposicio[0].length;
        this.numFilas = novesFiles;
        this.numColumnas = novesColumnes;

        // Actualizar layout
        layout.clear();
        for (int i = 0; i < novesFiles; i++) {
            Producte[] novaFila = new Producte[novesColumnes];
            System.arraycopy(disposicio[i], 0, novaFila, 0, novesColumnes);
            layout.add(novaFila);
        }

        // Actualizar disposició (disp)
        disp = new ArrayList<>();
        for (int i = 0; i < novesFiles; i++) {
            List<Pair<String, Integer>> fila = new ArrayList<>();
            for (int j = 0; j < novesColumnes; j++) {
                Producte producte = disposicio[i][j];
                Pair<Integer, Integer> p = new Pair<>(i, j);

                if (producte != null) {
                    producte.afegirPosPrestatgeria(id, p); // Asignar posición del producto en prestatgería
                    fila.add(new Pair<>(producte.getNom(), producte.getId()));
                }
            }
            disp.add(fila);
        }
    }
    
    /**
     * Intercanvia dos productes dins de la prestatgeria.
     *
     * @param filaProd1 La fila del primer producte.
     * @param colProd1 La columna del primer producte.
     * @param filaProd2 La fila del segon producte.
     * @param colProd2 La columna del segon producte.
     */
    public void intercanviarDosProductes(int filaProd1, int colProd1, int filaProd2, int colProd2) {
    	if (filaProd1 < 0 || colProd1 < 0 || filaProd1 >= numFilas || colProd1 >= numColumnas
                || filaProd2 < 0 || colProd2 < 0 || filaProd2 >= numFilas || colProd2 >= numColumnas) {
            System.out.println("Posicions fora de rang.");
        }

        // Validación de posiciones iguales
        if (filaProd1 == filaProd2 && colProd1 == colProd2) {
            System.out.println("Posicio producte 1: " + filaProd1 + ", " + colProd1);
            System.out.println("Posicio producte 2: " + filaProd2 + ", " + colProd2);
            System.out.println("Les posicions són iguals, no es pot realitzar l'intercanvi.");
        }

        // Obtener prestatges y validar productos no nulos
        Producte[] prestatge1 = layout.get(filaProd1);
        Producte[] prestatge2 = layout.get(filaProd2);

        if (prestatge1[colProd1] == null || prestatge2[colProd2] == null) {
            System.out.println("Un o ambdós productes són null, no es pot realitzar l'intercanvi.");
        }

        Producte temp = prestatge1[colProd1];
        prestatge1[colProd1] = prestatge2[colProd2];
        prestatge2[colProd2] = temp;
        
        Pair<Integer, Integer> pos1 = new Pair<>(filaProd1, colProd1);
        Pair<Integer, Integer> pos2 = new Pair<>(filaProd2, colProd2);
        
        prestatge1[colProd1].setPosPrestatgeria(id, pos1);
        prestatge1[colProd2].setPosPrestatgeria(id, pos2);
//        
        List<Pair<String, Integer>> fila1 = disp.get(filaProd1);
        List<Pair<String, Integer>> fila2 = disp.get(filaProd2);

        Pair<String, Integer> tempDisp = fila1.get(colProd1);
        fila1.set(colProd1, fila2.get(colProd2));
        fila2.set(colProd2, tempDisp);
    }
//    public void afegirPrestatge() {
//        layout.add(new Producte[numColumnas]);
//        numFilas++;
//        System.out.println("S'ha afegit un nou estant. Total files: " + numFilas);
//    }

//    public void esborrarPrestatge(int indexFila) {
//        if (indexFila < 0 || indexFila >= layout.size()) {
//            System.out.println("Error: Índex d'estant fora de rang.");
//            return;
//        }
//            
//        layout.remove(indexFila);
//        numFilas--;
//        System.out.println("Estant " + indexFila + " eliminat. Total files: " + numFilas);
//    }
    
    /**
     * Esborra tot el contingut de la prestatgeria, incloent-hi el layout i els
     * productes associats.
     */
    public void esborrarPrestatgeria() {
        layout.clear();
        disp = new ArrayList<>();
        numFilas = 0;
        numColumnas = 0;
    }



}