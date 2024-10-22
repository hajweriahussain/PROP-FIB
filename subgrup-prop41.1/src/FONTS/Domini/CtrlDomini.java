package Domini

public Class CtrlDo

import org.w3c.dom.Text;mini {
    private Usuari UsuariActual;
    private CjUsuaris cjUsuaris;
    private CjProductes CjProductes;
    //habra cjPrestatgerias?
    private cjPrestatgerias cjPrestatgerias;
    private Prestatgeria Prestatgeria;
    private tspAproximator tsp;
    private BruteForceAlg bf;
    private float[][] MatSimilituds;
    private Producte[][] MatProductes;

    private static CtrlDomini singletonObject;
    
    public CtrlDomini() {
        inicialitzarCtrlDomini();
    }

    public void inicialitzarCtrlDomini() {
        cjUsuaris = new CjUsuaris(); //FALTA PARAMETRE
        bf = new BruteForceAlg();
    }

    public void iniciarSessio(String username, String pwd){
        if (cjUsuaris.getUsuari(username) == null){
            nouUsuari(username, pwd);
        }
        cjProductes = new cjProductes(username);
        //cjPrestagerias si lo hay
        cjPrestatgerias = new cjPrestatgerias(username);
        Prestageria = novaPrestatgeria(); //LOS PARAMETROS DEBERIAN SER NFILES Y NCOLS. PERO DE DONDE LOS SACO
        UsuariActual = cjUsuaris.getUsuari(username);
    }

    
    public void nouUsuari(String name, String pwd){
        cjUsuaris.crearUsuari(name, pwd);
        iniciarSessio(name, pwd);
    }

    public void canviarUsuari(String nomUsuari, String pwd) {
        //per a aquesta entrega només crida a iniciar sessió, però per les següents haurà de cridar a la base de dades i guardar el que calgui
        iniciarSessio(nomUsuari, pwd); //canviem l'usuari actual
    }
    
    public int novaPrestatgeria(int nf, int nc){
        int id = cjPrestatgerias.crearPrestatgeria(nf, nc);

    }

    public void eliminarPrestatgeria(int id){
        Prestatgeria p = cjPrestatgerias.getPrestatgeria(id);
        cjPrestatgerias.esborrarPrestatgeria(p);
    }

    public String getUsuariActual() {
        return UsuariActual.getUsername();
    }

    public void eliminarUsuari(){
        cjUsuaris.esborrarUsuari(UsuariActual.getUsername());
        tancarSessio(); //por implementar
    }
}