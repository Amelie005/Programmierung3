package Uhrzeit;

import java.time.LocalTime;

/**
 * Speichert eine Uhrzeit bestehend aus Stunden, Minuten und Sekunden
 * @author Amelie SOphie Dzierzawa, 599428
 */
public class Zeit {

    private final int stunden;
    private final int minuten;
    private final int sekunden;

    /**
     * Konstruktor 1: Erstellt eine Zeit mit den angegebenen Werten
     * @param stunde Stunden (0-23)
     * @param minute Minuten (0-59)
     * @param sekunde Sekunden (0-59)
     * @throws IllegalArgumentException wenn einer der Werte außerhalb des gültigen Bereichs liegt (siehe jeweiligen Parameter)
     */
    public Zeit(int stunde, int minute, int sekunde) {
        if (stunde < 0 || stunde > 23)
            throw new IllegalArgumentException("Stunden müssen zwischen 0 und 23 liegen!");
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException("Minuten müssen zwischen 0 und 59 liegen!");
        if (sekunde < 0 || sekunde > 59)
            throw new IllegalArgumentException("Sekunden müssen zwischen 0 und 59 liegen!");

        this.stunden = stunde;
        this.minuten = minute;
        this.sekunden = sekunde;
    }

    /**
     * Konstruktor 2: Erstellt eine Zeit mit der aktuellen Systemzeit des Betriebssystems
     */
    public Zeit() {
        LocalTime now = LocalTime.now();
        this.stunden = now.getHour();
        this.minuten = now.getMinute();
        this.sekunden = now.getSecond();
    }

    /**
     * Gibt die Zeit im deutschen Standardformat aus: HH:MM:SS
     */
    public void ausgebenDeutsch() {
        System.out.printf("%02d:%02d:%02d%n", stunden, minuten, sekunden);
    }

    /**
     * Gibt die Zeit im englischen Standardformat aus: HH:MM:SS am/pm
     * Besonderheiten: 0 Uhr = 12:xx am, 12 Uhr = 12:xx pm
     */
    public void ausgebenEnglish() {
        String amPm;
        int stunde12;

        if (stunden == 0) { //Mitternacht, also 12 "am"
            stunde12 = 12;
            amPm = "am";
        } else if (stunden < 12) { //Vormittag, also generell noch "am"
            stunde12 = stunden;
            amPm = "am";
        } else if (stunden == 12) { //Mittags, also 12 "pm"
            stunde12 = 12;
            amPm = "pm";
        } else { //Nachmittags, also generell "pm"
            stunde12 = stunden - 12;  //bspw. kein 19 Uhr, sondern entsprechend 7 Uhr
            amPm = "pm";
        }

        System.out.printf("%02d:%02d:%02d %s%n", stunde12, minuten, sekunden, amPm);
    }

    /**
     * Berechnet die Differenz in Sekunden zwischen this und t2
     * @param t2 zweite Uhrzeit
     * @return absolute Differenz in Sekunden
     * @throws NullPointerException wenn t2 null ist
     */
    public int differenz(Zeit t2) {
        if (t2 == null)
            throw new NullPointerException("Zeit darf nicht null sein");

        int thisSekunden = stunden * 3600 + minuten * 60 + sekunden;
        int t2Sekunden = t2.stunden * 3600 + t2.minuten * 60 + t2.sekunden;

        return Math.abs(thisSekunden - t2Sekunden);
    }
}
