package Domini;

public class Prestatgeria {
    private Producte[] layout;
    private int id;
    private int numProductes;


    public Prestatgeria(int id, int nProd){
        this.id = id;
        this.numProductes = nProd;
        this.layout = new Producte[numProductes];
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
        this.id = id;
    }
    public void setNumProductes(int nProd){
        this.numProductes = nProd;
    }
    public void setLayout(Producte[] disposicio){
        for (int i = 0; i < numProductes; ++i){
            layout[i] = disposicio[i];
        }
    }
    public void intercanviarDosProductes(int posProd1, int posProd2) {
        if (posProd1 >= 0 && posProd1 < numProductes && posProd2 >= 0 && posProd2 < numProductes) {
            Producte prod1Temp = layout[posProd1];
            layout[posProd1] = layout[posProd2];
            layout[posProd2] = prod1Temp;
            System.out.println("S'han intercambiat els productes de " + posProd1 + " i " + posProd2);
        } else {
            System.out.println("Posicions fora de rang");
        }
    }
    public void eliminarPrestatgeria() {
        for (int i = 0; i < numProductes; ++i) {
            layout[i] = null;
        }
        layout = null;
        numProductes = 0;
        System.out.println("Prestatgeria esborrada");
    }


}