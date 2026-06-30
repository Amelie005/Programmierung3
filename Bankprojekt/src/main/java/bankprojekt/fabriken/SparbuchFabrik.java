package bankprojekt.fabriken;

import bankprojekt.basisdaten.Konto;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.basisdaten.Sparbuch;

/**
 * Erstellt ein Sparbuch.
 * @author Amelie Dzierzawa, 599428
 */
public class SparbuchFabrik implements Kontofabrik {

    @Override
    public Konto erstelleKonto(Kunde inhaber, long nummer) {
        return new Sparbuch(inhaber, nummer);
    }
}
