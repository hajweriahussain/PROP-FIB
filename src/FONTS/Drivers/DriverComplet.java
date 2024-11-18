package Drivers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import Domini.*;

public class DriverComplet {
    private CtrlDomini cD = CtrlDomini.getInstance();
    private Scanner scanner;
    
    public void llegirComandesTerminal(DriverComplet dC) {
        System.out.println("Introdueix les teves comandes. Escriu 'sortir' per acabar.");

        while (true) {
            String linea = scanner.nextLine();
            if (linea.equalsIgnoreCase("sortir")) {
                System.out.println("Sortint del driver...");
                break;
            }

            comanda(dC, linea);
        }
    }

    public void llegirComandesFitxer(DriverComplet dC) {
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine();
            comanda(dC, linea);
        }
    }

    public void comanda(DriverComplet dC, String comanda) {
        String[] vec = comanda.split(" ");
        switch (vec[0]) {
        	case "iniciarSessio":
	            dC.iniciarSessio(vec[1], vec[2]);
	            break;
            case "crearUsuari":
                dC.crearUsuari(vec[1], vec[2]);
                break;
            case "canviarUsuari":
            	dC.canviarUsuari(vec[1], vec[2]);
                break;
            case "llistarProductesUsuari":
            	dC.llistarProductesUsuari();
                break;
            case "llistarPrestatgeriaUsuari":
            	dC.llistarPrestatgeriaUsuari();
                break;
            case "crearProducte":
            	int id = Integer.parseInt(vec[1]);
            	String nom = vec[2];
            	Map<Integer, Double> similituds = stringToMap(vec[3]);
            	Boolean bF = Boolean.parseBoolean(vec[4]);
            	dC.crearProducte(id, nom, similituds, bF);
                break;
            case "crearPrestatgeria":
            	boolean bruteForce = Boolean.parseBoolean(vec[1]);
            	dC.crearPrestatgeria(bruteForce);
                break;
            case "modificarProducte":
            	int idP1 = Integer.parseInt(vec[1]);
            	int idP2 = Integer.parseInt(vec[2]);
            	double novaSim = Double.parseDouble(vec[3]);
            	int nouIdP1 = Integer.parseInt(vec[4]);
            	String nouNomP1 = vec[5];
            	int novaPos = Integer.parseInt(vec[6]);
            	Boolean bForce = Boolean.parseBoolean(vec[7]);
            	dC.modificarProducte(idP1, idP2, novaSim, nouIdP1, nouNomP1, novaPos, bForce);
                break;
            case "modificarPrestatgeria":
            	int pos1 = Integer.parseInt(vec[1]);
            	int pos2 = Integer.parseInt(vec[2]);
            	dC.modificarPrestatgeria(pos1, pos2);
                break;
            case "esborrarProducte":
            	int id1 = Integer.parseInt(vec[1]);
            	Boolean bF1 = Boolean.parseBoolean(vec[2]);
            	dC.esborrarProducte(id1, bF1);
                break;
            case "esborrarPrestatgeria":
            	dC.esborrarPrestatgeria();
                break;
            case "getUsuariActual":
            	dC.getUsuariActual();
                break;
            case "esborrarUsuari":
            	dC.esborrarUsuari();
                break;
            case "tancarSessio":
            	dC.tancarSessio();
                break;
            case "sortir":
                System.out.println("Sortint del driver...");
                return;
            default:
                System.out.println("Comanda no existeix: " + comanda);
        }
    }

	public Map<Integer, Double> stringToMap(String s) {
		Map<Integer, Double> similituds = new HashMap<>();
        String[] pars = s.split(",");
        for (String par : pars) {
            String[] valor = par.split(":");
            try {
                Integer id = Integer.parseInt(valor[0]);
                Double sim = Double.parseDouble(valor[1]);
                similituds.put(id, sim);
            } catch (NumberFormatException e) {
                System.out.println("Error en el formato del par: " + par);
            }
        }
        return similituds;
	}

	public void crearUsuari(String username, String pwd) {
        cD.crearUsuari(username, pwd);
        System.out.println("Usuari " + username + " creat");
    }

    public void iniciarSessio(String username, String pwd) {
        cD.iniciarSessio(username, pwd);
        System.out.println("L'usuari " + username + " ha iniciat sessió");
    } 
    
    public void canviarUsuari(String username, String pwd) {
    	cD.canviarUsuari(username, pwd);
        System.out.println("Sessió canviada correctament");
	}
    
    public void llistarProductesUsuari() {
    	Producte[] vecP = cD.llistarProductesUsuari();
    	
    	if (vecP == null || vecP.length == 0) {
            System.out.println("L'usuari no te productes.");
            return;
        }

        System.out.println("Llistat de productes:");
        for (Producte producte : vecP) {
            System.out.println("ID: " + producte.getId() + 
            					", Nom: " + producte.getNom() + 
                                ", Posició: " + producte.getColumna());
        }
	}
    
    public void llistarPrestatgeriaUsuari() {
    	Producte[] layout = cD.llistarPrestatgeriaUsuari();
    	
    	if (layout == null || layout.length == 0) {
            System.out.println("La prestatgeria está buida.");
            return;
        }

        System.out.println("Prestatgeria:");
        for (Producte producte : layout) {
            if (producte != null) {
                System.out.println("| " + producte.getId() + " |");
            }
        }
    	
	}
    
    public void crearProducte(int id, String nom, Map<Integer, Double> similituds, Boolean bruteForce) {
    	cD.crearProducte(id, nom, similituds, bruteForce);
    	System.out.println("S'ha creat el producte amb id " + id + " i nom " + nom);
    }
    
    public void crearPrestatgeria(boolean bruteForce) {
		cD.crearPrestatgeria(bruteForce);
		if (bruteForce) System.out.println("S'ha creat la prestatgeria i s'ha trobat el layout amb l'algoritme de força bruta");
		else System.out.println("S'ha creat la prestatgeria i s'ha trobat el layout amb l'algoritme 2-Aproximació");		
	}
    
    public void modificarProducte(Integer idP1, Integer idP2, double novaSim, Integer nouIdP1, String nouNomP1, Integer novaPos, Boolean bForce) {
    	cD.modificarProducte(idP1, idP2, novaSim, nouIdP1, nouNomP1, novaPos, bForce);
    }
    
    public void modificarPrestatgeria(int pos1, int pos2) {
    	cD.modificarPrestatgeria(pos1, pos2);
    	System.out.println("S'han cambiat els productes amb posicio " + pos1 + " i posició " + pos2);
    }
    
    public void esborrarProducte(int id, Boolean bruteForce) {
    	cD.esborrarProducte(id, bruteForce);
    	System.out.println("S'han esborrrat el producte amb id " + id);
    }
    
    public void esborrarPrestatgeria() {
    	cD.esborrarPrestatgeria();
    	System.out.println("S'ha esborrat la prestatgeria");
    }
    
    public void getUsuariActual() {
    	String user = cD.getUsuariActual();
    	System.out.println("L'usuari actual té username " + user);
    }
    
    public void esborrarUsuari() {
    	cD.esborrarUsuari();
    	System.out.println("S'ha esborrat l'usuari actual");
    }
    
    public void tancarSessio() {
    	cD.tancarSessio();
    	System.out.println("S'ha tancat la sessió");
    }
    
	public static void main(String[] args) {
        DriverComplet dC = new DriverComplet();
        dC.cD.inicialitzarCtrlDomini();

        Scanner sc = new Scanner(System.in);
        System.out.println("Escull el mètode d'entrada de les dades:");
        System.out.println("1 - Entrada per terminal");
        System.out.println("2 - Entrada per fitxer de text");
        
        int opcion = sc.nextInt();
        sc.nextLine(); 

        if (opcion == 1) {
            dC.scanner = new Scanner(System.in);
            dC.llegirComandesTerminal(dC);;
        } else if (opcion == 2) {
        	System.out.println("Introduce el nombre del archivo (debe estar en el mismo directorio que el Driver):");
        	if (sc.hasNextLine()) {
        	    String filename = sc.nextLine().trim();
        	    String filePath = "../EXE/Drivers/" + filename;

        	    try {
        	        dC.scanner = new Scanner(new FileReader(filePath));
        	        dC.llegirComandesFitxer(dC);
        	    } catch (FileNotFoundException e) {
        	        System.out.println("El archivo no se ha encontrado: " + e.getMessage());
        	    }
        	} else {
        	    System.out.println("Error al leer el nombre del archivo.");
        	}
        } else {
            System.out.println("Opción no válida.");
        }

        sc.close();
    }
    

}