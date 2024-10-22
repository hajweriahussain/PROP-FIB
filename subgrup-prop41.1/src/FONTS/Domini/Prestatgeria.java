package Domini

public Class Prestatgeria {
    private Producte[][] layout;
    private Integer id;
    private int Files;
    private int Columnes;


    public Prestatgeria(int id, int nF, int nC){
        this.id = id;
        this.files = nF;
        this.Columnes = nC;
        this.layout = new Producte[files][Columnes];
    }

    public void canviarLayout(Producte[][] disp){
        for (int i = 0; i < files;++i){
            for (int j = 0; j < columnes; ++j){
                layout[i][j] = disp[i][j];
            }
        }
    }

    public Producte[][] getLayout(){
        return layout;
    }
    public Integer getId(){
        return id;
    }
    public int getNumF(){
        return files;
    }
    public int getnumC(){
        return Columnes;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setNumF(int nF){
        this.files = nF;
    }
    public void setnumC(int nC){
        this.Columnes = nC;
    }


}