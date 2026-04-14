package Zensurenspiegel;

/**
 * Verwaltet den Notenspiegel einer Klausur
 * @author Amelie Sophie Dzierzawa, 599428
 */
public class Zensurenspiegel {

    private final int maxNote;
    private final int[] anzahlNoten;

    /**
     * Konstruktor, der die höchste mögliche Note als Parameter nimmt und
     * ein neues Array erstellt, mit denen die Anzahl der Notenvorkommen gespeichert werden kann
     * @param maxNote höchstmögliche Note
     */
    public Zensurenspiegel(int maxNote) {
        this.maxNote = maxNote;
        this.anzahlNoten = new int[maxNote];
    }

    /**
     * Trägt eine Note in den Zensurenspiegel ein
     * @param note Note die eingetragen werden soll (1 - höchste Note)
     * @throws IllegalArgumentException wenn die Note höchste Note + 1 ist
     */
    public void noteEintragen(int note) {
        if (note < 1 || note > maxNote)
            throw new IllegalArgumentException("Note muss zwischen 1 und 5 liegen!");
        anzahlNoten[note - 1]++;  // Note 1 -> Index 0, Note 2 -> Index 1, usw.
    }

    /**
     * Gibt zurück wie oft eine bestimmte Note vorkommt
     * @param note die gesuchte Note (1 - höchste Note)
     * @return Anzahl des Vorkommens der Note
     */
    public int getAnzahlNote(int note) {
        return anzahlNoten[note - 1];
    }

    /**
     * Berechnet den Notendurchschnitt aller eingetragenen Noten
     * @return Durchschnitt aller Noten
     * @throws ArithmeticException wenn noch keine Noten eingetragen wurden
     */
    public double getDurchschnitt() {
        int summe = 0;
        int anzahl = 0;

        for (int i = 0; i < anzahlNoten.length; i++) {
            summe += (i + 1) * anzahlNoten[i];  // Note * Anzahl ihres Vorkommens
            anzahl += anzahlNoten[i];            // Gesamtanzahl aller Studenten
        }

        if (anzahl == 0)
            throw new ArithmeticException("Es wurden noch keine Noten eingetragen!");

        return (double) summe / anzahl;
    }

}
