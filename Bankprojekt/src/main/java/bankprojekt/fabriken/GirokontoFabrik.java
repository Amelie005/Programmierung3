package bankprojekt.fabriken;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Girokonto;
import bankprojekt.basisdaten.Konto;
import bankprojekt.basisdaten.Kunde;

/**
 * Erstellt ein Girokonto.
 * @author Amelie Dzierzawa, 599428
 */
public class GirokontoFabrik implements Kontofabrik {

    @Override
    public Konto erstelleKonto(Kunde inhaber, long nummer) {
        return new Girokonto(inhaber, nummer, new Geldbetrag(500));
    }
}
