package Ratespiel;

/**
 * Repräsentiert die Logik des Zahlenratespiels
 * @author Amelie Sophie Dzierzawa, 599428
 */
public class Zahlenratespiel {

    /**
     * Konstante die die zufällig festgelegte gesuchte Zahl repräsentiert
     */
    private final int gesuchtezahl;
    private int versuche;

    /**
     * Konstruktor: Erstellt ein neues Spiel mit einer zufälligen gesuchten Zahl
     * zwischen 1 und 100 (inklusive)
     */
    public Zahlenratespiel() {
        this.gesuchtezahl = (int)(Math.random() * 100) + 1;
        this.versuche = 0;
    }

    /**
     * Wertet einen Versch des Spielers aus die Zahl zu eraten
     * @param geratenezahl die geratene Zahl
     * @return -1 = Zahl ist zu klein, 0 = Zahl ist korrekt, 1 = Zahl ist zu groß
     */
    public int raten(int geratenezahl) {
        versuche++;
        return Integer.compare(geratenezahl, gesuchtezahl);
    }

    /**
     * Gibt die Anzahl der Versuche zurück
     * @return Anzahl der Versuche als int
     */
    public int getVersuche() {
        return versuche;
    }
}
