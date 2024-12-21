package Drivers;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

import Domini.BruteForce;

public class DriverBruteForce {

    private BruteForce bf;
    private static int numCol;

    // Constructora per llegir d'un arxiu
    public DriverBruteForce(String dataFile) throws FileNotFoundException {
        FileReader fr = new FileReader(dataFile);
        Scanner scanner = new Scanner(fr);

        // Llegeix num. productes i els IDs dels productes
        String lineaNumProductes = scanner.nextLine(); // Llegeix "Número de productes: 4"
        int numProductes = Integer.parseInt(lineaNumProductes.split(":")[1].trim());

        int[] productes = new int[numProductes];
        for (int i = 0; i < numProductes; i++) {
            String lineaProducte = scanner.nextLine();  // Ex: "1 A"
            String[] prod = lineaProducte.split("\\s+");

            try {
                int idProducte = Integer.parseInt(prod[0]);
                productes[i] = idProducte;

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El format del producte no és correcte. Introdueix un Id numèric.");
            }
        }

        // Llegeix la matriu de similituds
        scanner.nextLine();
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

        this.bf = new BruteForce(matriuSimilituds, productes, numCol);
        scanner.close();
    }

    // Constructora per llegir des de terminal
    public DriverBruteForce() {
        Scanner scanner = new Scanner(System.in);

        // Llegeix num. productes i els IDs dels productes
        System.out.print("Número de productes: ");
        int numProductes = Integer.parseInt(scanner.nextLine());

        int[] productes = new int[numProductes];
        for (int i = 0; i < numProductes; i++) {
            System.out.print("Introdueix l'ID del producte " + (i + 1) + ": ");
            productes[i] = Integer.parseInt(scanner.nextLine());
        }

        // Llegeix la matriu de similituds
        System.out.println("Indrodueix la matriu de similituds:");
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

        this.bf = new BruteForce(matriuSimilituds, productes, numCol);
        scanner.close();
    }

    private double[][] llegirMatriu(Scanner scanner, int numProductes) {
        double[][] matriuSimilituds = new double[numProductes][numProductes];

        for (int i = 0; i < numProductes; i++) {
            String lineaSimilituds = scanner.nextLine();
            String[] sim = lineaSimilituds.split("\\s+");

            // Asegúrate de que los índices no salgan del rango
            for (int j = 0; j < numProductes; j++) {
                if (j >= sim.length) {
                    System.out.println("Advertencia: faltan valores en la fila de la matriz de similitudes.");
                    break;
                }

                double valor = Double.parseDouble(sim[j]);
                if (valor < 0.0) throw new IllegalArgumentException("La matriu de similituds no pot contenir valors negatius");
                if (valor > 1.0) throw new IllegalArgumentException("La matriu de similituds no pot contenir valors més grans que 1.0");

                matriuSimilituds[i][j] = valor;
            }
        }

        return matriuSimilituds;
    }

    public void generar_Layout() {
        System.out.println("\nIniciando la generación del layout...");
        
        // Obtener el resultado como una matriz de enteros
        Integer[][] resultat = bf.generarLayout();
        System.out.println("\nIniciando la generación del layout 2222222222...");
        // Imprimir la matriz resultante
        System.out.println("\nMatriz Resultante (mat_res):");
        for (int i = 0; i < resultat.length; i++) {
            for (int j = 0; j < resultat[i].length; j++) {
                if (resultat[i][j] != 0) {
                    System.out.print("| ID: " + String.format("%-4d", resultat[i][j]) + " ");
                } else {
                    System.out.print("| ----- ");
                }
            }
            System.out.println();
        }

        System.out.println("\nSimilitud más alta: " + String.format("%.2f", bf.getMillorSimilitud()));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Escull el número de columnes de la estanteria:");
        int entrada = sc.nextInt();
        numCol = entrada;

        System.out.println("Escull el mètode d'entrada de les dades:");
        System.out.println("1 - Entrada per terminal");
        System.out.println("2 - Entrada per fitxer de text");

        int op = sc.nextInt();
        sc.nextLine();

        if (op == 1) {
            try {
                DriverBruteForce driverBF = new DriverBruteForce();
                driverBF.generar_Layout();
            } catch (IllegalArgumentException e) {
                System.out.println("Error en les dades: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("Error al generar el layout: " + e.getMessage());
            }
        } else if (op == 2) {
            System.out.println("Introdueix el nom del fitxer (que ha d'estar al directori JocsAlgoritmes (../EXE/Drivers/JocsAlgoritmes) del projecte)");
            String filename = sc.nextLine();
            String filePath = "../EXE/Drivers/JocsAlgoritmes/" + filename;

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

        } else {
            System.out.println("Opció no vàlida.");
        }

        sc.close();
    }
}
