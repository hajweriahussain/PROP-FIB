package Domini;

public class Prestatgeria {
    private Producte[] layout;
    private int id;
    private int numProductes;


    public Prestatgeria(int id, int nProd){
    	if (id <= 0) {
            System.out.println("Error: L'ID ha de ser un valor positiu. Assignant ID per defecte 1.");
            this.id = 1;
        } else this.id = id;
        
    	if (nProd <= 0) {
            System.out.println("Error: El número de productes ha de ser major que zero. Assignant 1 producte per defecte.");
            this.numProductes = 1;
        } else {
            this.numProductes = nProd;
        }
    	this.layout = new Producte[this.numProductes];
    }
    public Producte[] getLayout(){
        return layout;
    }
    public int getId(){
        return id;
    }
    public int getNumProductes(){
        return numProductes;
    }
    public void setId(int id){
    	if (id <= 0) {
            System.out.println("Error: L'ID ha de ser un valor positiu. No es canvia l'ID.");
        } else {
            this.id = id;
        }
    }
    public void setNumProductes(int nProd){
        this.numProductes = nProd;
    }
    public void setLayout(Producte[] disposicio){
    	if (disposicio == null) {
            System.out.println("Error: La disposició és null. No es pot assignar el layout.");
            return;
        }
        if (disposicio.length != numProductes) {
            System.out.println("Error: La longitud de la disposició no coincideix amb el número de productes. No es pot assignar el layout.");
            return;
        }
        for (int i = 0; i < numProductes; ++i) {
            layout[i] = disposicio[i];
        }
    }
    public void intercanviarDosProductes(int posProd1, int posProd2) {
    	if (posProd1 < 0 || posProd1 >= numProductes || posProd2 < 0 || posProd2 >= numProductes) {
            System.out.println("Posicions fora de rang");
            return;
        }

        if (posProd1 == posProd2) {
            System.out.println("Les posicions són iguals, no es realitza l'intercanvi");
            return;
        }

        if (layout[posProd1] == null || layout[posProd2] == null) {
            System.out.println("Un o ambdós productes són null, no es pot realitzar l'intercanvi");
            return;
        }

        Producte temp = layout[posProd1];
        layout[posProd1] = layout[posProd2];
        layout[posProd2] = temp;

        layout[posProd1].setColumna(posProd1);
        layout[posProd2].setColumna(posProd2);

        System.out.println("S'han intercanviat els productes de " + posProd1 + " i " + posProd2);
    }
    public void eliminarPrestatgeria() {
    	if (layout == null) {
            System.out.println("La prestatgeria ja està esborrada.");
            return;
        }
        for (int i = 0; i < numProductes; ++i) {
            layout[i] = null;
        }
        layout = null;
        numProductes = 0;
        System.out.println("Prestatgeria esborrada");
    }


}