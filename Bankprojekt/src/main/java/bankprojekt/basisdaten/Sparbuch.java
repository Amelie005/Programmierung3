package bankprojekt.basisdaten;

import java.time.LocalDate;

import bankprojekt.exceptions.GesperrtException;
import bankprojekt.exceptions.UngueltigeKontonummerException;
import bankprojekt.nuetzliches.Kalender;

/**
 * ein Sparbuch, d.h. ein Konto, das nur recht eingeschränkt genutzt
 * werden kann. Insbesondere darf man monatlich nur höchstens 2000€
 * abheben, wobei der Kontostand nie unter 0,50€ fallen darf. Zur Abfrage
 * des heutigen Datums (was die Zuordnung zweier Abhebungen zu einem gemeinsamen
 * Monat bestimmt) kann ein Kalender-Objekt angegeben werden. 
 * @author Doro
 */
public class Sparbuch extends Konto {
	/**
	 * Monatlich erlaubter Gesamtbetrag für Abhebungen
	 */
	public static final Geldbetrag ABHEBESUMME = new Geldbetrag(2000);

	/**
	 * Betrag, der nach einer Abhebung mindestens auf dem Konto bleiben muss
	 */
	public static final Geldbetrag MINIMUM = new Geldbetrag(0.5);
	
	/**
	 * Zinssatz, mit dem das Sparbuch verzinst wird. 0,03 entspricht 3%
	 */
	private double zinssatz;
	
	/**
	 * Betrag, der im aktuellen Monat bereits abgehoben wurde
	 */
	private Geldbetrag bereitsAbgehoben = Geldbetrag.NULL_EURO;

	/**
	 * Kalender zur Abfrage des heutigen Datums
	 */
	private Kalender kalender;

	/**
	 * Monat und Jahr der letzten Abhebung
	 */
	private LocalDate zeitpunkt; 
	
	/**
	* ein Standard-Sparbuch
	*/
	public Sparbuch() {
		this.zinssatz = 0.03;
		this.kalender = new Kalender();
		zeitpunkt = kalender.getHeutigesDatum();
	}

	/**
	* ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	* Das heutige Datum wird an allen Stellen, wo es benötigt wird, vom Betriebssystem
	* abgefragt.
	* @param inhaber der Kontoinhaber
	* @param kontonummer die Wunsch-Kontonummer
	* @throws IllegalArgumentException wenn inhaber null ist 
	* @throws UngueltigeKontonummerException wenn kontonummer ungültig ist
	*/
	public Sparbuch(Kunde inhaber, long kontonummer) {
		this(inhaber, kontonummer, new Kalender());
	}
	
	/**
	* ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	* @param inhaber der Kontoinhaber
	* @param kontonummer die Wunsch-Kontonummer
	* @param kalender der Kalender, der für die Abfrage des heutigen Datums benutzt wird
	* @throws IllegalArgumentException wenn inhaber oder kalender null ist 
	* @throws UngueltigeKontonummerException wenn kontonummer ungültig ist
	*/
	public Sparbuch(Kunde inhaber, long kontonummer, Kalender kalender) {
		super(inhaber, kontonummer);
		this.zinssatz = 0.03;
		if(kalender == null)
			throw new IllegalArgumentException();
		this.kalender = kalender;
		zeitpunkt = kalender.getHeutigesDatum();
	}

	@Override
	protected boolean pruefeDeckungUndRegeln(Geldbetrag betrag) {
		LocalDate heute = kalender.getHeutigesDatum();

		//aktuellen Stand ermitteln
		Geldbetrag aktuelleSumme = (heute.getMonth() != zeitpunkt.getMonth() ||
				heute.getYear() != zeitpunkt.getYear())
				? Geldbetrag.NULL_EURO
				: bereitsAbgehoben;

		Geldbetrag neu = getKontostand().minus(betrag);

		//prüfen ob möglich
		return neu.compareTo(Sparbuch.MINIMUM) >= 0 &&
				aktuelleSumme.plus(betrag).compareTo(Sparbuch.ABHEBESUMME) <= 0;
	}

	@Override
	protected void nachAbhebenHook(Geldbetrag betrag) {
		//erst hier Zustand vom Sparbuch ändern
		LocalDate heute = kalender.getHeutigesDatum();

		//Monat/Jahreswechsel prüfen
		if (heute.getMonth() != zeitpunkt.getMonth() || heute.getYear() != zeitpunkt.getYear()) {
			this.bereitsAbgehoben = Geldbetrag.NULL_EURO;
		}

		this.bereitsAbgehoben = this.bereitsAbgehoben.plus(betrag);
		this.zeitpunkt = heute;
	}
	
	@Override
	public String toString()
	{
    	String ausgabe = "-- SPARBUCH --" + System.lineSeparator() +
    	super.toString()+ System.lineSeparator()
    	+ "Zinssatz: " + this.zinssatz * 100 +"%";
    	return ausgabe;
	}
}
