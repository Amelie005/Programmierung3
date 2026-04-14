package Zensurenspiegel;

import java.util.Scanner;

/**
 * Einstiegspunkt des Zensurenspiegelprogramms
 */
public class Main {
    /**
     * Startet die Eingabe und gibt den Zensurenspiegel aus
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(".---------------.\n" +
                "|Zensurenspiegel|\n" +
                "'---------------'");

        System.out.println("Was ist die höchste Note?: ");
        int maxNote = scanner.nextInt();

        Zensurenspiegel z = new Zensurenspiegel(maxNote);

        System.out.println("Geben sie die Noten ein (" + (maxNote +1) + " zum beenden): " );

        int note;

        do {
            System.out.print("Note: ");
            note = scanner.nextInt();

            if (note != maxNote + 1) {
                try {
                    z.noteEintragen(note);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ungültige Note! Bitte 1-5 oder 6 zum Beenden.");
                }
            }

        } while (note != maxNote + 1);

        System.out.println("\nZensurenspiegel:");
        for (int i = 1; i <= maxNote; i++) {
            System.out.println("Note " + i + " kam " + z.getAnzahlNote(i) + " mal vor.");
        }

        System.out.println();
        System.out.println("Der Notendurchschnitt beträgt: Ø" + String.format("%.2f", z.getDurchschnitt()));

    }
}
