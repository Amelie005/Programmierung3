package bankprojekt.vorlesungscode.sortiererei;

import bankprojekt.basisdaten.Konto;

/**
 * formatiert Konto-Objekte
 */
public interface Formatierer {

	/**
	* formatiert k
	* @param k das zu formatierende Konto
	* @return String mit der Darstellung der Daten von k
	* @throws NullPointerException wenn k null ist
	*/
	public String formatieren(Konto k);

}
