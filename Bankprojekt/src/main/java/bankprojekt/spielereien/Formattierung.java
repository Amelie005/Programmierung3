package bankprojekt.spielereien;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;

public class Formattierung {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String dateiname = "daten_formattiert.txt";

        //Benutzereingaben erfragen
        System.out.print("Geben Sie eine ganze Zahl ein: ");
        int ganzeZahl = scanner.nextInt();

        System.out.print("Geben Sie eine Kommazahl ein: ");
        double kommazahl = scanner.nextDouble();

        //Aktuelle Zeitdaten ermitteln
        LocalDate heute = LocalDate.now();
        LocalTime jetzt = LocalTime.now();

        System.out.println(System.lineSeparator() + "Daten werden in die Datei " + dateiname + " geschrieben.");

        try (FileWriter fw = new FileWriter(dateiname);
             PrintWriter pw = new PrintWriter(fw)) {

            //1. Ganze Zahl in Standardformattierung
            pw.printf("%d%n", ganzeZahl);

            //2. Ganze Zahl mit 10 Stellen, mit Nullen gefüllt
            pw.printf("%010d%n", ganzeZahl);

            //3. Ganze Zahl mit Tausendertrennzeichen, negative Zahlen in Klammern
            pw.printf("%,(d%n", ganzeZahl);

            //4. Ganze Zahl als Hexadezimalzahl, kleingeschrieben, alternatives Format
            pw.printf("%#x%n", ganzeZahl);

            //5. Kommazahl mit Standardformattierung
            pw.printf("%f%n", kommazahl);

            //6. Kommazahl mit Vorzeichen und 3 Nachkommastellen
            pw.printf("%+.3f%n", kommazahl);

            //7. Kommazahl in wissenschaftlicher Darstellung
            pw.printf("%e%n", kommazahl);

            //8. Kommazahl mit 2 Nachkommastellen, Format der USA
            pw.printf(Locale.US, "%.2f%n", kommazahl);

            //9. Ausgabe wie bei 8., aber mit Prozentzeichen dazu
            pw.printf(Locale.US, "%.2f%%%n", kommazahl);

            //10. aktuelles Datum, beginnend mit Tag (ohne 0),
            //, ausgeschriebendem Monat, vierstelliges Jahr, in Klammern der abgekürzte Wochentag
            pw.printf("%te. %<tB %<tY (%<ta)%n", heute);

            //11. aktuelles Datum, zweistelliger Tages- und Monats- und Jahreszahl,
            //ausgeschriebener französischer Wochentagsname
            pw.printf(Locale.FRANCE, "%td.%<tm.%<ty (%<tA)%n", heute);

            //12. aktuelle Uhrzeit im englischem Format, Stunde:Minute + am/pm,
            //ohne führende Nullen bei der Stundenzahl
            pw.printf(Locale.US, "%tl:%<tM %<tp%n", jetzt);

            System.out.println("Datei erfolgreich erstellt!");
        } catch (IOException e) {
            System.err.println("Fehler beim schreibend er Datei: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
