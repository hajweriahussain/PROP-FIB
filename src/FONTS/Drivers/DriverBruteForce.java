package Drivers;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

import Domini.BruteForce;
import Domini.Producte;

public class DriverBruteForce {

    private BruteForce bf;

    //constructora per llegir d'un arxiu
    public DriverBruteForce(String dataFile) throws FileNotFoundException {
        FileReader fr = new FileReader(dataFile);
        Scanner scanner = new Scanner(fr);

        // Llegeix num. productes i els productes
        String lineaNumProductes = scanner.nextLine(); // Llegeix "Número de productes: 4"
        int numProductes = Integer.parseInt(lineaNumProductes.split(":")[1].trim());

        Producte[] productes = new Producte[numProductes];
        for (int i = 0; i < numProductes; i++) {
            String lineaProducte = scanner.nextLine();  // Ex: "1 A"
            String[] prod = lineaProducte.split("\\s+");

            try {
                int idProducte = Integer.parseInt(prod[0]);
                String nomProducte = prod[1];
                productes[i] = new Producte(idProducte, nomProducte);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El format del producte no és correcte. Introdueix un Id numèric seguit d'un nom");

            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("El format del producte no és correcte. Cal indicar tant l'ID com el nom");
            }

        }

        //Llegeix la matriu de similituds
        scanner.nextLine();
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

         this.bf = new BruteForce(matriuSimilituds, productes);
         scanner.close();
    }

    //constructora per llegir des de terminal
     public DriverBruteForce() {
        Scanner scanner = new Scanner(System.in);

        // Llegeix num. productes i els productes
        System.out.print("Número de productes: ");
        int numProductes = Integer.parseInt(scanner.nextLine());

        Producte[] productes = new Producte[numProductes];
        for (int i = 0; i < numProductes; i++) {
            System.out.print("Introdueix l'ID y el nom del producte " + (i + 1) + ": ");
            String lineaProducte = scanner.nextLine();
            String[] prod = lineaProducte.split("\\s+");

            try {
                int idProducte = Integer.parseInt(prod[0]);
                String nomProducte = prod[1];
                productes[i] = new Producte(idProducte, nomProducte);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El format del producte no és correcte. Introdueix un ID numèric seguit d'un nom");

            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("El format del producte no és correcte. Cal indicar tant l'ID com el nom");
            }
        }

        //Llegeix la matriu de similituds
        System.out.println("Indrodueix la matriu de similituds:");
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

        this.bf = new BruteForce(matriuSimilituds, productes);
        scanner.close();
    }

    private double[][] llegirMatriu(Scanner scanner, int numProductes) {
        double[][] matriuSimilituds = new double[numProductes][numProductes];

        for (int i = 0; i < numProductes; i++) {
            String lineaSimilituds = scanner.nextLine();
            String[] sim = lineaSimilituds.split("\\s+");

            for (int j = 0; j < numProductes; j++) {
                double valor = Double.parseDouble(sim[j]);
                if(valor < 0.0) throw new IllegalArgumentException("La matriu de similituds no pot contenir valors negatius");
                if(valor > 1.0) throw new IllegalArgumentException("La matriu de similituds no pot contenir valors més grans que 1.0");
                matriuSimilituds[i][j] = valor;
            }
        }

        return matriuSimilituds;
    }

    public void generar_Layout() {
        System.out.println("\nIniciando la generación del layout...");

        Producte[] resultat = bf.generarLayout();

        System.out.println("+---------------------------------+");
        System.out.println("|           Prestatgeria          |");
        System.out.println("+---------------------------------+");

        for (int i = 0; i < resultat.length; i++) {
            String id = String.format("%-4d", resultat[i].getId());
            String nombre = String.format("%-9s", resultat[i].getNom());
            System.out.println("|  ID: " + id + " | Producte: " + nombre + " |");
            System.out.println("+---------------------------------+");
        }

        System.out.println("\nSimilitud más alta: " + String.format("%.2f", bf.getMillorSimilitud()));
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Escull el mètode d'entrada de les dades:");
        System.out.println("1 - Entrada per terminal");
        System.out.println("2 - Entrada per fitxer de text");

        int op = sc.nextInt();
        sc.nextLine();

        if(op == 1){
            try {
                DriverBruteForce driverBF = new DriverBruteForce();
                driverBF.generar_Layout();
            } catch (IllegalArgumentException e) {
                System.out.println("Error en les dades: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("Error al generar el layout: " + e.getMessage());
            }
        }

        else if(op == 2) {
            System.out.println("Introdueix el nom del fitxer (que ha d'estar al directori DATA del projecte)");
            String filename = sc.nextLine();
            String filePath = "../DATA/" + filename;

            try {
                DriverBruteForce driverBF = new DriverBruteForce(filePath);
                driverBF.generar_Layout();

            } catch (FileNotFoundException e) {
                System.out.println("L'arxiu no s'ha trobat: " + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.out.println("Error en la matriu de similituds: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("Error al generar el layout: " + e.getMessage());
            }

        }
        else{
            System.out.println("Opció no vàlida.");
            return;
        }

        sc.close();
    }

}
