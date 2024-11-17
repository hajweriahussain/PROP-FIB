package Drivers;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

import Domini.DosAproximacio;
import Domini.Producte;

public class DriverDosAproximacio {

    private DosAproximacio dosAprox;

    public DriverDosAproximacio(String dataFile) throws FileNotFoundException {
        FileReader fr = new FileReader(dataFile);
        Scanner scanner = new Scanner(fr);

        String lineaNumProductes = scanner.nextLine(); // Ej: "Número de productes: 4"
        int numProductes = Integer.parseInt(lineaNumProductes.split(":")[1].trim());

        Producte[] productes = new Producte[numProductes];
        for (int i = 0; i < numProductes; i++) {
            String lineaProducte = scanner.nextLine();  // Ej: "1 A"
            String[] prod = lineaProducte.split("\\s+");
            int idProducte = Integer.parseInt(prod[0]);
            String nomProducte = prod[1];

            productes[i] = new Producte(idProducte, nomProducte);
        }

        scanner.nextLine();
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

        this.dosAprox = new DosAproximacio(matriuSimilituds, productes);
        scanner.close();
    }

    public DriverDosAproximacio() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Número de productes: ");
        int numProductes = Integer.parseInt(scanner.nextLine());

        Producte[] productes = new Producte[numProductes];
        for (int i = 0; i < numProductes; i++) {
            System.out.print("Introdueix l'ID y el nom del producte " + (i + 1) + ": ");
            String lineaProducte = scanner.nextLine();
            String[] prod = lineaProducte.split("\\s+");
            int idProducte = Integer.parseInt(prod[0]);
            String nomProducte = prod[1];

            productes[i] = new Producte(idProducte, nomProducte);
        }

        System.out.println("Introdueix la matriu de similituds:");
        double[][] matriuSimilituds = llegirMatriu(scanner, numProductes);

        this.dosAprox = new DosAproximacio(matriuSimilituds, productes);
        scanner.close();
    }

    private double[][] llegirMatriu(Scanner scanner, int numProductes) {
        double[][] matriuSimilituds = new double[numProductes][numProductes];

        for (int i = 0; i < numProductes; i++) {
            String lineaSimilituds = scanner.nextLine();
            String[] sim = lineaSimilituds.split("\\s+");

            for (int j = 0; j < numProductes; j++) {
                matriuSimilituds[i][j] = Double.parseDouble(sim[j]);
            }
        }
        return matriuSimilituds;
    }

    public void generar_Layout() {
        System.out.println("Iniciando la generación del layout con 2-Aproximació...");

        Producte[] resultat = dosAprox.generarLayout();

        for (Producte prod : resultat) {
            System.out.println("Producto " + prod.getId() + ": " + prod.getNom());
        }
        System.out.println("Millor Similitud: " + dosAprox.getMillorSimilitud());
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Escull el mètode d'entrada de les dades:");
        System.out.println("1 - Entrada per terminal");
        System.out.println("2 - Entrada per fitxer de text");
        int entrada = sc.nextInt();
        sc.nextLine();

        if(entrada == 1){
            try {
                DriverDosAproximacio driver2A = new DriverDosAproximacio();
                driver2A.generar_Layout();
            } catch (Exception e) {
                System.out.println("Error al generar el layout: " + e.getMessage());
            }
        }
        else if(entrada == 2) {
            System.out.println("Introdueix el nom del fitxer (que ha d'estar al directori DATA del projecte)");
            String filename = sc.nextLine();
            String filePath = "../DATA/" + filename;

            try {
                DriverDosAproximacio driver2A = new DriverDosAproximacio(filePath);
                driver2A.generar_Layout();
            } catch (FileNotFoundException e) {
                System.out.println("L'arxiu no s'ha trobat: " + e.getMessage());
            }
        }
        else{
            System.out.println("Opció no vàlida.");
            sc.close();
            return;
        }
        sc.close();
    }
}
