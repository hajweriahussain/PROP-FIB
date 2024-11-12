package Drivers;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

import Domini.BruteForce;

public class DriverBruteForce {

    private DriverBruteForce bf;

    public void generar_Layout() {
        System.out.println("Iniciando la generación del layout...");
        Producte[] resultado = bf.generarLayout();
        for (Producte prod : resultado) {
            System.out.print("Producto " + prod.getId() + " ");
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Escull el mètode d'entrada de les dades:");
        System.out.println("1 - Entrada per terminal");
        System.out.println("2 - Entrada per fitxer de text");

        int op = scanner.nextInt();

        if(op == 1){
            bf.matSimilituds = llegirMatriuSim(scanner);
            bf.vecProductes = llegirVecProd(scanner);
        }
        else if(op == 2) {
            System.out.println("Introdueix el nom del fitxer (que ha d'estar al directori DATA del projecte)");
            String filename = sc.nextLine();
            String filePath = "../../DATA/" + filename;

            try {
                DriverBF driverBF = new DriverBF(filePath);
                driverBF.generar_Layout();

            } catch (FileNotFoundException e) {
                System.out.println("El archivo no se ha encontrado: " + e.getMessage());
            }

        }
        else{
            System.out.println("Opción no válida.");
            return;
        }
    }

}
