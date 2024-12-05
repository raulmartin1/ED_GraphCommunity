package cat.urv.deim;

import java.io.FileWriter;
import java.util.Scanner;

import javax.swing.SortOrder;
import javax.swing.text.html.parser.Element;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.PosicioForaRang;

public class Main {

    public Main() {}

    public static void main(String[] args) throws ElementNoTrobat, PosicioForaRang{

        Scanner scanner = new Scanner(System.in);
        GrafPersones graf1 = new GrafPersones(100, "graf1.graphml");
        GrafPersones graf2 = new GrafPersones(100, "graf2.graphml");
        GrafPersones graf3 = new GrafPersones(100, "graf3.graphml");
        GrafPersones graf4 = new GrafPersones(100, "jazz.graphml");

        boolean fi = false;

        while (!fi) {
            System.out.println("===Menu d'opcions===");
            System.out.println("1. Calcul de Modularitat i Optimitzacio graf1");
            System.out.println("2. Calcul de Modularitat i Optimitzacio graf2");
            System.out.println("3. Calcul de Modularitat i Optimitzacio graf3");
            System.out.println("4. Calcul de Modularitat i Optimitzacio graf4 ( Triga una mica mes)");
            System.out.println("5. Escriure graf1 en grafSortida.graphml");
            System.out.println("6. Escriure graf2 en grafSortida.graphml");
            System.out.println("7. Escriure graf3 en grafSortida.graphml");
            System.out.println("8. Buidar fitxer grafSortida.graphml");
            System.out.println("9. Sortir del programa");
            System.out.print("Introdueix un numero: ");
            int opcio = scanner.nextInt();

            switch (opcio) {
                case 1:
                    System.out.println("Modularitat inicial del graf1: " +graf1.Modularitat());
                    graf1.optimitzarModularitat();
                    System.out.println("Modularitat del graf1 optimitzat: " +graf1.Modularitat());
                    break;
                case 2:
                    System.out.println("Modularitat inicial del graf2: " +graf2.Modularitat());
                    graf2.optimitzarModularitat();
                    System.out.println("Modularitat del graf2 optimitzat: " +graf2.Modularitat());
                    break;
                case 3:
                    System.out.println("Modularitat inicial del graf3: " +graf3.Modularitat());
                    graf3.optimitzarModularitat();
                    System.out.println("Modularitat del graf3 optimitzat: " +graf3.Modularitat());
                    break;
                case 4:
                    System.out.println("Modularitat inicial del graf4: " +graf4.Modularitat());
                    System.out.println("Optimitzant...");
                    graf4.optimitzarModularitat();
                    System.out.println("Modularitat del graf4 optimitzat: " +graf4.Modularitat());
                    break;
                case 5:
                    graf1.escriureDades("grafSortida.graphml");
                    System.out.println("S'ha escrit el graf1 a grafSortida.graphml");
                    break;
                case 6:
                    graf2.escriureDades("grafSortida.graphml");
                    System.out.println("S'ha escrit el graf2 a grafSortida.graphml");
                    break;
                case 7:
                    graf3.escriureDades("grafSortida.graphml");
                    System.out.println("S'ha escrit el graf3 a grafSortida.graphml");
                    break;
                case 8:
                    try {
                        FileWriter fitxer = new FileWriter("grafSortida.graphml", false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    System.out.println("El programa s'ha tancat");
                    fi = true;
                    break;
                default:
                    System.out.println("introdueix una opcio valida.");
            }
            System.out.println();
        }
    }
}
