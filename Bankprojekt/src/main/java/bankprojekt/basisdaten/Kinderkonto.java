package bankprojekt.basisdaten;

/**
 * Repräsentiert ein Kinderkonto, bei dem Abhebungen nur bis zu einem Limit von 50 Euro möglich sind.
 * @author Amelie Dzierzawa, 599428
 */
public class Kinderkonto  extends Konto {
    private static final Geldbetrag LIMIT = new Geldbetrag(50.0);

    @Override
    protected boolean pruefeDeckungUndRegeln(Geldbetrag betrag) {
        //Deckung prüfen
        return !getKontostand().minus(betrag).isNegativ() &&
                betrag.compareTo(LIMIT) <= 0;
    }
}
