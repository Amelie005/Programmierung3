package Ratespiel;

import java.util.Scanner;

/**
 * Einstiegspunkt des Zahlenratespiels
 * @author Amelie Sophie Dzierzawa, 599428
 */
public class Main {
    /**
     * Startet das Zahlenratespiel
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        String neuesspiel;

        do {
            Zahlenratespiel spiel = new Zahlenratespiel();  // neues Spiel bei jedem Durchlauf
            int ergebnis;


            System.out.println("\n" +
                    "███████╗░█████╗░██╗░░██╗██╗░░░░░███████╗███╗░░██╗██████╗░░█████╗░████████╗███████╗░██████╗██████╗░██╗███████╗██╗░░░░░\n" +
                    "╚════██║██╔══██╗██║░░██║██║░░░░░██╔════╝████╗░██║██╔══██╗██╔══██╗╚══██╔══╝██╔════╝██╔════╝██╔══██╗██║██╔════╝██║░░░░░\n" +
                    "░░███╔═╝███████║███████║██║░░░░░█████╗░░██╔██╗██║██████╔╝███████║░░░██║░░░█████╗░░╚█████╗░██████╔╝██║█████╗░░██║░░░░░\n" +
                    "██╔══╝░░██╔══██║██╔══██║██║░░░░░██╔══╝░░██║╚████║██╔══██╗██╔══██║░░░██║░░░██╔══╝░░░╚═══██╗██╔═══╝░██║██╔══╝░░██║░░░░░\n" +
                    "███████╗██║░░██║██║░░██║███████╗███████╗██║░╚███║██║░░██║██║░░██║░░░██║░░░███████╗██████╔╝██║░░░░░██║███████╗███████╗\n" +
                    "╚══════╝╚═╝░░╚═╝╚═╝░░╚═╝╚══════╝╚══════╝╚═╝░░╚══╝╚═╝░░╚═╝╚═╝░░╚═╝░░░╚═╝░░░╚══════╝╚═════╝░╚═╝░░░░░╚═╝╚══════╝╚══════╝");

            System.out.println();
            System.out.println("Willkommen beim Zahlenratespiel!");
            System.out.println("Errate die ganze Zahl die inklusive zwischen 1 und 100 liegt!");

            do {
                System.out.print("Gebe deine Zahl ein: ");
                int geratenezahl = scanner.nextInt();
                ergebnis = spiel.raten(geratenezahl);

                if (ergebnis < 0)
                    System.out.println("Diese Zahl ist zu klein. Versuche es noch einmal!");
                else if (ergebnis > 0)
                    System.out.println("Diese Zahl ist zu groß. Versuche es noch einmal!");
                else
                    System.out.println("Korrekt! Du hast " + spiel.getVersuche() + " gebraucht.");

            } while (ergebnis != 0);

            System.out.print("Möchtest du erneut spielen? (j/n): ");
            neuesspiel = scanner.next();

        } while (neuesspiel.equalsIgnoreCase("j"));

        System.out.println("Bis dann!");
    }
}