package bankprojekt.basisdaten;

/**
 * Repräsentiert ein Festgeldkonto, bei dem Abhebungen nur nach Kündigung eines Betrags möglich sind.
 * @author Amelie Dzierzawa, 599428
 */
public class Festgeldkonto extends Konto{
    private Geldbetrag gekuendigterBetrag = Geldbetrag.NULL_EURO;

    /**
     * Kündigt den angegebenen Betrag vom Festgeldkonto.
     * @param betrag Betrag der gekündigt werden soll
     */
    public void kuendigen(Geldbetrag betrag) {
        if (betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("betrag ungültig.");
        }

        //höchstens der aktuelle Kontostand ist kündbar
        if (betrag.compareTo(getKontostand()) > 0) {
            this.gekuendigterBetrag = getKontostand();
        } else {
            this.gekuendigterBetrag = betrag;
        }
    }

    @Override
    protected boolean pruefeDeckungUndRegeln(Geldbetrag betrag) {
        //Abhebung nur erlaubt, wenn durhc gekündigten Betrag gedeckt
        return betrag.compareTo(gekuendigterBetrag) <= 0;
    }

    @Override
    protected void nachAbhebenHook(Geldbetrag betrag) {
        //nach Abhebung den verfügbaren gekündigten Betrag reduzieren
        this.gekuendigterBetrag = this.gekuendigterBetrag.minus(betrag);
    }
}
