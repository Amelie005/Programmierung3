package sortiererei;

/**
 * vergleicht Objekte anhand ihrer String-Darstellung
 */
public class AllesVergleicher implements Vergleicher<Object> { //Raw-Type eliminiert

	@Override
	public int vergleichen(Object a, Object b) {
		return a.toString().compareToIgnoreCase(b.toString());
	}

}
