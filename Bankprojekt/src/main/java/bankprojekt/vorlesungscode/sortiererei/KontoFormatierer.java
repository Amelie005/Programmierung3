package bankprojekt.vorlesungscode.sortiererei;

import bankprojekt.basisdaten.Konto;

/**
 * gibt Konto-Objekte in der Form Kontonummer: Kontostand aus
 */
public class KontoFormatierer implements Formatierer {

	@Override
	public String formatieren(Konto k) {
			return k.getKontonummer() + ": " + k.getKontostand();
	}

}
