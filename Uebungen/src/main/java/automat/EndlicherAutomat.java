package automat;

/**
 * Beschreibt einen Endlichen Automaten aus der Theoretischen Informatik.
 * @author Amelie Dzierzawa
 */
public interface EndlicherAutomat {

    /**
     * Legt fest bei welchem Zustand der endliche Automat startet.
     * @return Nummer des Startzustands
     */
    int getStartzustand();


    /**
     * Die Übergangsfunktion des endlichen Automaten (also Delta).
     * @param aktuellerZustand der Zustand in dem sich der Automat gerade befindet
     * @param symbol das Zeichen, das gerade gelesen wird
     * @return der Folgezustand oder -1 wenn das Symbol ungültig ist
     */
    int getNaechstenZustand(int aktuellerZustand, char symbol) ;


    /**
     * Prüft ob der übergebende Zustand ein aktzeptierender Endzustand ist.
     * @param aktuellerZustand die Nummer des zu prüfenden Zustands
     * @return true, wenn der Zustand in die Menge des Endzustände gehört
     */
    boolean istEndzustand(int aktuellerZustand);


    /**
     * Prüft ob der Zustand ein gültiger Zustand  ist.
     * @param zustand zu prüfender Zustand
     * @return true, wenn der Zustand gültig ist
     */
    boolean istGueltigerZustand(int zustand);


    /**
     * Läuft die ganze Zeichenkette durch und überprüft ob der Automat am Ende in
     * einem Endzustand landet.
     * @param zeichenkette zu überprüfende Zeichenkette
     * @return true, wenn der Automat in einem aktzeptierenden Endzustand landet
     * @throws IllegalArgumentException wenn die Zeichenkette null ist
     * @throws RuntimeException wenn der Automat in einen ungültigen Zustand springt
     */
    default boolean testen(String zeichenkette) throws IllegalArgumentException, RuntimeException {
        if (zeichenkette == null) {
            throw new IllegalArgumentException("Zeichenkette darf nicht null sein!");
        }

        //Startet den Automaten beim Startzustand
        int zustand = getStartzustand();

        //Zeichenkette wird Symbol für Symbol durchgegangen

        for (int i = 0; i < zeichenkette.length(); i++) {
            char symbol = zeichenkette.charAt(i);

            //nächster Zustand basierend auf dem aktuellen Symbol berechnet
            zustand = getNaechstenZustand(zustand, symbol);

            //prüft jedes Mal ob es auch ein erlaubter Zustand ist
            if (!istGueltigerZustand(zustand)) {
                throw new RuntimeException("Fehler: Der Automat ist in einen ungültigen Zustand "
                        + zustand + " gesprungen!");
            }
        }

        //am Ende wird geprüft, ob man in ein aktzeptierenden Endzustand gelangt ist
        return istEndzustand(zustand);
    }

}
