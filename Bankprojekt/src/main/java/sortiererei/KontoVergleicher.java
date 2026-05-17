package sortiererei;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Konto;

/**
* ein Vergleicher für zwei Konten
*/
public class KontoVergleicher implements Vergleicher<Konto> {

	@Override
	public int vergleichen(Konto ka, Konto kb) {

		//kein instanceof und keine Exceptions mehr nötig, da Vergleicher Interface jetzt generisch ist
		Geldbetrag standA = ka.getKontostand();
		Geldbetrag standB = kb.getKontostand();
		if(standA.compareTo(standB) > 0)
			return 1;
		else if(standA.compareTo(standB) < 0)
			return -1;
		else
			return 0;
		}
}

