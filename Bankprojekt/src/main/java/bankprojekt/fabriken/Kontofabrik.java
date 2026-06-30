package bankprojekt.fabriken;

import bankprojekt.basisdaten.Konto;
import bankprojekt.basisdaten.Kunde;

/**
 * Interface zur erstellung eines Kontos.
 * @author Amelie Dzierzawa, 599428
 */
public interface Kontofabrik {

    /**
     * Erstellt ein Konto-Objekt.
     * @param inhaber Inhaber des zu erstellenden Kontos
     * @param nummer Kontonummer des zu erstellenden Kontos
     * @return das erstellte Konto-Objekt
     */
    Konto erstelleKonto(Kunde inhaber, long nummer);
}
