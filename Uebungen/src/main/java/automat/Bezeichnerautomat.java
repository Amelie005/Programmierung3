package automat;

public class Bezeichnerautomat implements EndlicherAutomat {

    @Override
    public int getStartzustand() {
        return 0; //Laut Diagramm, wird bei Zustand 0 gestartet
    }

    @Override
    public boolean istGueltigerZustand(int zustand) {
        return zustand >= 0 && zustand <= 2;
    }

    @Override
    public int getNaechstenZustand(int aktuellerZustand, char symbol) {
        switch (aktuellerZustand) {
            case 0:
                //Von Zustand 0 aus erlaubt: Buchstabe oder Unterstrich
                if (istBuchstabeOderUnterstrich(symbol)) {
                    return 1; //zu Zustand 1, wenn es ein Buchstabe oder Unterstrich ist
                } else {
                    return 2; //wenn es ein sonstiges Zeichen ist zu Zustand 2
                }

            case 1:
                //Aktzeptierender Zustand, also nur Buchstaben, Ziffern und Unterstriche erlaubt
                if (istBuchstabeOderUnterstrich(symbol) || Character.isDigit(symbol)) {
                    return 1; //bleibe in Zustand 1
                } else {
                    return 2; //gehe weiter zu Zustand 2
                }

            case 2:
                return 2; //Wenn man einmal in Zustand 2 ist, ist man immer hier (laut Diagramm alle Zeichen)
            default:
                return -1; //sollte eigentlich nie erreicht werden
        }
    }

    @Override
    public boolean istEndzustand(int aktuellerZustand) {
        return aktuellerZustand == 1;
    }

    /**
     * Hilfsmethode zur Prüfung der C-Konventionen
     * @param c zu prüfender character
     * @return true, wenn c den C-Komventionen entspricht
     */
    private boolean istBuchstabeOderUnterstrich(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }



}
