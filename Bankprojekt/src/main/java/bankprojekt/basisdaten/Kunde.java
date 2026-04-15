package bankprojekt.basisdaten;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Kunde einer Bank
 * @author Dorothea Hubrich
 */
@SuppressWarnings("unused")
public class Kunde implements Comparable<Kunde> {

	/**
	 * Ein Musterkunde
	 */
	public static final Kunde MUSTERMANN = new Kunde("Max", "Mustermann", "zuhause", LocalDate.now());
	
	/**
	 * englische oder deutsche Anrede, je nach den Systemeinstellungen
	 */
	private static final String ANREDE;

	/**
	 * liefert die systemspezifische Anrede
	 * @return systemspezifische Anrede
	 */
	public static String getAnrede() {
		return ANREDE;
	}

	/**
	 * der Vorname
	 */
	private String vorname;
	/**
	 * Der Nachname
	 */
	private String nachname;
	/**
	 * Die Adresse
	 */
	private String adresse;
	/**
	 * Geburtstag
	 */
	private LocalDate geburtstag;

	/**
	 * erzeugt den Standardkunden Max Mustermann
	 */
	public Kunde() {
		this("Max", "Mustermann", "Adresse", LocalDate.now());
	}

	/**
	 * Erzeugt einen Kunden mit den übergebenen Werten
	 * @param vorname Vorname
	 * @param nachname Nachname
	 * @param adresse Adresse
	 * @param gebdat Geburtstag
	 * @throws IllegalArgumentException wenn einer der Parameter null ist
	 */
	public Kunde(String vorname, String nachname, 
			String adresse, LocalDate gebdat) {
		if(vorname == null || nachname == null || adresse == null || gebdat == null)
			throw new IllegalArgumentException("null als Parameter nich erlaubt");
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.geburtstag = gebdat;
		
		//Runtime umgebung = Runtime.getRuntime();
		
	}

	/**
	 * Erzeugt einen Kunden mit den übergebenen Werten
	 * @param vorname Vorname
	 * @param nachname Nachname
	 * @param adresse Adresse
	 * @param gebdat Geburtstag im Format tt.mm.yy
	 * @throws java.time.format.DateTimeParseException wenn das Format des übergebenen Datums nicht korrekt ist
	 * @throws IllegalArgumentException wenn einer der Parameter null ist
	 */
	public Kunde(String vorname, String nachname, 
			String adresse, String gebdat)  {
		this(vorname, nachname, adresse, 
				LocalDate.parse(gebdat, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
	}
	
	/**
	 * Erzeugt einen Kunden mit den übergebenen Werten
	 * @param vorname Vorname
	 * @param nachname Nachname
	 * @param adresse Adresse
	 * @param gebJahr Geburtsjahr
	 * @param gebMonat Geburtsmonat
	 * @param gebTag Tag des Geburtstages
	 * @throws java.time.format.DateTimeParseException wenn die drei Werte für den Geburtstag kein gültiges 
	 *                           Datum ergeben
	 */
	public Kunde(String vorname, String nachname, String adresse, 
			int gebJahr, int gebMonat, int gebTag)  {
		this(vorname, nachname, adresse, LocalDate.of(gebJahr, gebMonat, gebTag));
	}
	
	/**
	 * Adresse des Kunden
	 * 
	 * @return Adresse des Kunden
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * setzt die Adresse auf den angegebenen Wert
	 * @param adresse neue Adresse
	 * @throws IllegalArgumentException wenn adresse null ist
	 */
	public void setAdresse(String adresse) {
		if(adresse == null)
			throw new IllegalArgumentException("Adresse darf nicht null sein");
		this.adresse = adresse;
	}

	/**
	 * Nachname des Kunden
	 * @return Nachname des Kunden
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * setzt den Nachnamen auf den angegebenen Wert
	 * @param nachname neuer Nachname
	 * @throws IllegalArgumentException wenn nachname null ist
	 */
	public void setNachname(String nachname) {
		if(nachname == null)
			throw new IllegalArgumentException("Nachname darf nicht null sein");
		this.nachname = nachname;
	}

	/**
	 * Vorname des Kunden
	 * @return Vorname des Kunden
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * setzt den Vornamen auf den angegebenen Wert
	 * @param vorname neuer Vorname
	 * @throws IllegalArgumentException wenn vorname null ist
	 */
	public void setVorname(String vorname) {
		if(vorname == null)
			throw new IllegalArgumentException("Vorname darf nicht null sein");
		this.vorname = vorname;
	}

	/**
	 * Geburtstag des Kunden
	 * @return Geburtstag des Kunden
	 */
	public LocalDate getGeburtstag() {
		return geburtstag;
	}
	
	/**
	 * vollständiger Name des Kunden in der Form "Nachname, Vorname"
	 * 
	 * @return vollständiger Name des Kunden
	 */
	public String getName() {
		return this.nachname + ", " + this.vorname;
	}

	/**
	 * gibt alle Daten des Kunden aus
	 */
	@Override
	public String toString() {
		String ausgabe;
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		ausgabe = this.vorname + " " + this.nachname +
			" wohnt in " + this.getAdresse() +
			" (" + df.format(this.geburtstag)+")";
		return ausgabe;
	}

	/**
	 * Vergleich von this mit other; Zwei Kunden gelten als gleich,
	 * wen sie den gleichen vollständigen Namen haben
	 * @param other der Vergleichskunde
	 * @return true, wenn beide Kunden den gleichen Namen haben
	 */
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		if(this.getName().equals(((Kunde)other).getName()))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(this.getName());
	}


	static
	{
		if(Locale.getDefault().getCountry().equals("DE"))
			ANREDE = "Hallo Benutzer!";
		else
			ANREDE = "Dear Customer!";
	}

	/**
	 * Vergleicht diesen Kunden mit einem anderen Kunden:
	 * Die Sortierung erfolgt alphabetisch nach dem Namen (Nachname, Vorname).
	 * Diese Implementierung ist konsistent zu {@link #equals(Object)}, da beide
	 * auf der Methode {@link #getName()} basieren
	 * @param other der zu vergleichende Kunde
	 * @return ein negativer Wert, null oder ein positiver Wert, je nach Reihenfolge
	 */
	@Override
	public int compareTo(Kunde other) {
		//da equals() und hashCode() bereits getName() nutzen,
		//ist dies die "natürliche" Sortierung für den Kunden
		return this.getName().compareTo(other.getName());
	}
}