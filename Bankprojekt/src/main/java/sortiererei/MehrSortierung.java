package sortiererei;

import java.util.Arrays;

/**
 * Mehr oder eher weniger sinnvolle Sortierungen
 */
public class MehrSortierung {
	/**
	 * probiert ungewöhnliche Sortierungen aus
	 * @param args wird nicht verwendet.
	 */
	public static void main(String[] args)
	{
		Komisch[] komischeObjekte = {new Komisch(4), new Komisch(27), new Komisch(1)};
		//Sortieren.sortiere(komischeObjekte); kompiliert nicht mehr!
		Vergleicher<Komisch> vergleich = new AllesVergleicher();
		vergleich.sortiere(komischeObjekte);
		System.out.println(Arrays.toString(komischeObjekte));

		Allgemein[] allgemeineObjekte = { new Allgemein(), new Allgemein(), new Allgemein()};
		Sortieren.sortiere(allgemeineObjekte);
		System.out.println(Arrays.toString(allgemeineObjekte));

		vergleich.sortiere(komischeObjekte);
		System.out.println(Arrays.toString(komischeObjekte));
	}
}
