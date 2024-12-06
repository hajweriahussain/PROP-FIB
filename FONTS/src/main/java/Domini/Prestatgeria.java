package Domini;

import java.util.LinkedList;

public class Prestatgeria {
    private LinkedList<Producte[]> layout;
    private int id;
    private int numFilas;
    private int numColumnas;
    


    public Prestatgeria(int id, int filas, int columnas){
    	if (id <= 0) {
            System.out.println("Error: L'ID ha de ser un valor positiu. Assignant ID per defecte 1.");
            this.id = 1;
        } else this.id = id;
        
    	if (numFilas <= 0) {
            System.out.println("Error: El número de files ha de ser major que zero. Assignant 1 fila per defecte.");
            this.numFilas = 1;
        } else {
            this.numFilas = filas;
        }

        if (numColumnas <= 0) {
            System.out.println("Error: El número de columnes ha de ser major que zero. Assignant 1 columna per defecte.");
            this.numColumnas = 1;
        } else {
            this.numColumnas = columnas;
        }

        this.layout = new LinkedList<>();
        for (int i = 0; i < this.numFilas; i++) {
            layout.add(new Producte[this.numColumnas]);
        }
    }
    
    public Producte[] getPrestatge(int indexFila) {
        if (indexFila < 0 || indexFila >= layout.size()) {
            System.out.println("Error: Índex fora de rang.");
            return null;
        }
        return layout.get(indexFila);
    }
    
    public int getNumFilas() {
        return numFilas;
    }

    public int getNumColumnas() {
        return numColumnas;
    }
    
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
    public int getId(){
        return id;
    }
    public void setId(int id){
    	if (id <= 0) {
            System.out.println("Error: L'ID ha de ser un valor positiu. No es canvia l'ID.");
        } else {
            this.id = id;
        }
    }
    public void setLayout(Producte[][] disposicio) {
        if (disposicio == null || disposicio.length == 0 || disposicio[0].length == 0) {
            System.out.println("Error: La matriz proporcionada és nul·la o buida. No es pot assignar el layout.");
            return;
        }

        int novesFiles = disposicio.length;
        int novesColumnes = disposicio[0].length;

        

        this.numFilas = novesFiles;
        this.numColumnas = novesColumnes;

        layout.clear();
        for (int i = 0; i < novesFiles; i++) {
            Producte[] novaFila = new Producte[novesColumnes];
            System.arraycopy(disposicio[i], 0, novaFila, 0, novesColumnes);
            layout.add(novaFila);
        }

        System.out.println("El layout s'ha actualitzat correctament.");
    }
    
    public void intercanviarDosProductes(int filaProd1, int colProd1, int filaProd2, int colProd2) {
    	if (filaProd1 < 0 || colProd1 < 0 || filaProd1 >= numFilas || colProd1 >= numColumnas
                || filaProd2 < 0 || colProd2 < 0 || filaProd2 >= numFilas || colProd2 >= numColumnas) {
            System.out.println("Posicions fora de rang");
            return;
        }

        if (filaProd1 == filaProd2 && colProd1 == colProd2) {
            System.out.println("Les posicions són iguals, no es realitza l'intercanvi");
            return;
        }
        
        Producte[] prestatge1 = layout.get(filaProd1);
        Producte[] prestatge2 = layout.get(filaProd2);

        if (prestatge1[colProd1] == null || prestatge2[colProd2] == null) {
            System.out.println("Un o ambdós productes són null, no es pot realitzar l'intercanvi.");
            return;
        }

        Producte temp = prestatge1[colProd1];
        prestatge1[colProd1] = prestatge2[colProd2];
        prestatge2[colProd2] = temp;

        prestatge1[colProd1].setColumna(colProd1);
        prestatge2[colProd2].setColumna(colProd2);
        prestatge1[colProd1].setFila(filaProd1);
        prestatge2[colProd2].setFila(filaProd2);

        System.out.println("S'han intercanviat els productes de " + filaProd1 + "," + colProd1 + " i " + filaProd2 + "," + colProd2);
    }
    public void afegirPrestatge() {
        layout.add(new Producte[numColumnas]);
        numFilas++;
        System.out.println("S'ha afegit un nou estant. Total files: " + numFilas);
    }

    public void esborrarPrestatge(int indexFila) {
        if (indexFila < 0 || indexFila >= layout.size()) {
            System.out.println("Error: Índex d'estant fora de rang.");
            return;
        }
        layout.remove(indexFila);
        numFilas--;
        System.out.println("Estant " + indexFila + " eliminat. Total files: " + numFilas);
    }

    public void esborrarPrestatgeria() {
        layout.clear();
        numFilas = 0;
        numColumnas = 0;
        System.out.println("Prestatgeria esborrada.");
    }



}