package bankprojekt.basisdaten;

import java.util.Objects;
import org.decimal4j.util.DoubleRounder;

/**
 * Ein Geldbetrag mit Währung
 */
public class Geldbetrag implements Comparable<Geldbetrag>{
	/**
	 * 0 €
	 */
	public static final Geldbetrag NULL_EURO = new Geldbetrag(0);

	/**
	 * Währung die genutzt werden soll
	 */
	public Waehrung waehrung;

	/**
	 * Betrag in der in waehrung angegebenen Währung
	 */
	private final double betrag;
	
	/**
	 * erstellt einen Geldbetrag in der Währung Euro
	 * @param betrag Betrag in €
	 * @throws IllegalArgumentException wenn betrag unendlich oder NaN ist
	 */
	public Geldbetrag(double betrag)
	{
		if(!Double.isFinite(betrag))
			throw new IllegalArgumentException("Betrag muss endlich sein!");
		this.betrag = betrag;
		this.waehrung = Waehrung.EURO;
	}

	/**
	 * erstellt einen Geldbetrag in der angegebenen Währung.
	 * @param betrag gewünschter Geldbetrag
	 * @param w gewünschte Währung
	 * @throws IllegalArgumentException  wenn betrag unendlich oder NaN ist
	 */
	public Geldbetrag(double betrag, Waehrung w) {
		if(!Double.isFinite(betrag))
			throw new IllegalArgumentException("Betrag muss endlich sein!");
		if(w == null) {
			throw new IllegalArgumentException("Währung darf nicht null sein!");
		}
		this.betrag = betrag;
		this.waehrung = w;
	}

	/**
	 * Rechnet den aktuellen Geldbetrag in eine andere Währung um.
	 * @param zielWaehrung Währung, in die der akteulle Betrag umgerechnet werden soll
	 * @return umgerechneter Betrag
	 */
	public Geldbetrag umrechnen (Waehrung zielWaehrung) {
		if (this.waehrung == zielWaehrung) {
			//Betrag würde sich nicht ändern
			return new Geldbetrag(this.betrag, this.waehrung);
		} else {
			//zuerst in Euro umrechnen, dann in Zielwährung
			double betragInEuro = this.betrag / this.waehrung.getUmrechnungskursZuEuro();
			double zielBetrag = betragInEuro * zielWaehrung.getUmrechnungskursZuEuro();
			//auf 2 Nachkommastellen runden
			double gerundet = DoubleRounder.round(zielBetrag, 2);
			return new Geldbetrag(gerundet, zielWaehrung);
		}
	}

	/**
	 * Betrag von this
	 * @return Betrag in der Währung von this
	 */
	public double getBetrag() {
		return betrag;
	}
	
	/**
	 * prüft, ob this einen negativen Betrag darstellt
	 * @return true, wenn this negativ ist
	 */
	public boolean isNegativ()
	{
		return this.betrag < 0;
	}
	
	/**
	 * rechnet this + summand
	 * @param summand zu addierender Betrag
	 * @return this + summand in der Währung von this
	 * @throws IllegalArgumentException wenn summand null ist
	 */
	public Geldbetrag plus(Geldbetrag summand)
	{
		if(summand == null)
			throw new IllegalArgumentException();
		//Summand in eigene Währung umrechnen
		Geldbetrag umgerechnet = summand.umrechnen(this.waehrung);
		//Konstruktor mit Währung nutzen
		return new Geldbetrag(this.betrag + umgerechnet.getBetrag(), this.waehrung);
	}
	
	/**
	 * rechnet this - subtrahend
	 * @param subtrahend abzuziehender Betrag
	 * @return this - subtrahend in der Währung von this
	 * @throws IllegalArgumentException wenn subtrahend null ist
	 */
	public Geldbetrag minus(Geldbetrag subtrahend)
	{
		if(subtrahend == null)
			throw new IllegalArgumentException();
		//Subtrahend in eigene Währung umrechnen
		Geldbetrag umgerechnet = subtrahend.umrechnen(this.waehrung);
		//Konstruktor mit Währung nutzen
		return new Geldbetrag(this.betrag - umgerechnet.getBetrag(), this.waehrung);
	}

	/**
	 * multipliziert this mit faktor
	 * @param faktor Faktor der Multiplikation
	 * @return das faktor-Fache von this
	 * @throws IllegalArgumentException wenn faktor nicht finit ist
	 */
	public Geldbetrag mal(double faktor)
	{
		if(!Double.isFinite(faktor))
			throw new IllegalArgumentException();
		//Konstruktor mit Währung nutzen
		return new Geldbetrag(this.betrag * faktor, this.waehrung);
	}

	/**
	 * Vergleicht zwei Beträge miteinander.
	 * @param o der Betrag der verglichen werden soll
	 * @return -1, wenn this größer ist, 0, wenn beide gleich sind, 1, wenn o größer ist
	 * @throws NullPointerException  wenn der zu vergleichende Betrag null ist
	 */
	@Override
	public int compareTo(Geldbetrag o) {
		if (o == null) {
			throw new NullPointerException("Betrag darf nicht null sein!");
		}
		//Umrechnen vor dem Vergleichen
		Geldbetrag umgerechnet = o.umrechnen(this.waehrung);
		return Double.compare(this.betrag, umgerechnet.getBetrag());
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Geldbetrag)) return false;
		if(o == this) return true;
		Geldbetrag other  = (Geldbetrag)o;
		//Gleichheit bei Währung und Betrag
		return this.waehrung == other.waehrung && Double.compare(this.betrag, other.betrag) == 0;
	}

	@Override
	public int hashCode() {
		//HashCode bezieht Betrag und Währung ein
		return Objects.hash(this.getBetrag(), this.waehrung);
	}
	
	@Override
	public String toString()
	{
		//dynamische Währungsausgabe
		return String.format("%,.2f %s", this.betrag, this.waehrung.toString());
	}
}
