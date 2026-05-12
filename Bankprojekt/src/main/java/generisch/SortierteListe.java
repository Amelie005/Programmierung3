package generisch;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.JapaneseDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Girokonto;
import bankprojekt.basisdaten.Konto;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.basisdaten.Sparbuch;

/**
 * eine sehr primitive Liste
 * @author Doro
 * @param <T> Datentyp der Elemente in der Liste
 */
public class SortierteListe<T extends Comparable<? super T>> {
	/**
	 * ein Element der Liste
	 *
	 * @author Doro
	 */
	private class Element {
		private T wert;
		private Element naechster;

		/**
		 * erstellt ein Element der Liste mit Inhalt w und
		 * Nachfolger n
		 *
		 * @param w Inhalt des neuen Elementes
		 * @param n sein Nachfolger
		 */
		public Element(T w, Element n) {
			wert = w;
			naechster = n;
		}
	}

	/**
	 * "Zeiger" auf das erste Element der Liste
	 */
	private Element anfang = null;

	/**
	 * Die von allen Elementen zu erfüllende Bedingung
	 */
	private Probe<? super T> bedingung;

	/**
	 * erstellt eine leere Liste, deren Elemente die bedingung erfüllen müssen
	 *
	 * @param bedingung zu überprüfende bedingung für alle Elemente in der Liste
	 * @throws IllegalArgumentException wenn bedingung null ist
	 */
	public SortierteListe(Probe<? super T> bedingung) {
		if (bedingung == null)
			throw new IllegalArgumentException();
		this.bedingung = bedingung;
	}

	/**
	 * Fügt den angegebenen Wert in die Liste ein, wenn es die Bedingung erfüllt
	 *
	 * @param w einzufügender Wert
	 * @throws NullPointerException wenn w null ist
	 */
	public void add(T w) {
		if (bedingung.isOk(w)) {

			Element neu = new Element(w, null);
			Element zeiger = anfang;
			if (zeiger == null) {
				anfang = neu;
			} else if (zeiger.wert.compareTo(w) > 0) {
				neu.naechster = anfang;
				anfang = neu;
			} else {
				while (zeiger.naechster != null
						&& zeiger.naechster.wert.compareTo(w) < 0) {
					zeiger = zeiger.naechster;
				}
				neu.naechster = zeiger.naechster;
				zeiger.naechster = neu;
			}
		}
	}



	@Override
	public String toString()
	{
		String ausgabe = "[";
		Element zeiger = anfang;
		while(zeiger != null)
		{
			ausgabe += zeiger.wert + ", ";
			zeiger = zeiger.naechster;
		}
		ausgabe += "]";
		return ausgabe;
	}

	/**
	 * liefert das erste Element dieser Liste zurück und entfernt es gleichzeitig
	 * @return das erste Element aus dieser Liste
	 * @throws LeerException wenn this keine Elemente enthält.
	 */
	public T getSpitze()
	{
		if(anfang == null)
			throw new LeerException();
		T erstes = anfang.wert;
		anfang = anfang.naechster;
		return erstes;

	}

	/**
	 * Fügt alle Elemente aus neueMitglieder zu this hinzu, sofern sie die
	 * Probe von this erfüllen.
	 * @param neueMitglieder Collection mit Elementen vom Typ T oder dessen Untertypen
	 */
	public void addAll(Collection<? extends T> neueMitglieder) {
		if (neueMitglieder == null) return;
		for (T element : neueMitglieder) {
			this.add(element);
		}
	}

	/**
	 * Entfernt alle Elemente aus this, die die Bedingung erfüllen.
	 * @param bedingung die Probe, die entscheidet, welche Elemente gelöscht werden
	 */
	public void remove(Probe<? super T> bedingung) {
		if (bedingung == null || anfang == null) return;

		//Sonderfall: Anfangselemente entfernen
		while (anfang != null && bedingung.isOk(anfang.wert)) {
			anfang = anfang.naechster;
		}

		//restliche Liste durchlaufen
		if (anfang == null) return;
		Element zeiger = anfang;
		while (zeiger.naechster != null) {
			if (bedingung.isOk(zeiger.naechster.wert)) {
				//Element wird gelöscht
				zeiger.naechster = zeiger.naechster.naechster;
			} else {
				zeiger = zeiger.naechster;
			}
		}
	}

	/**
	 * Entfernt alle Elemente aus this, die größer sind als element.
	 * @param element Vergleichselement
	 * @param <E> Typ des Vergleichselements
	 */
	public <E> void removeAllBigger(E element) {
		if (element == null || anfang == null) return;

		//auf Comparable casten, damit man Typen die vom gleichen Obertyp erben vergleichen kann
		if (((Comparable)anfang.wert).compareTo(element) > 0) {
			anfang = null;
			return;
		}

		Element zeiger = anfang;
		while (zeiger.naechster != null) {
			if (((Comparable)zeiger.naechster.wert).compareTo(element) > 0) {
				zeiger.naechster = null;
				return;
			}
			zeiger = zeiger.naechster;
		}
	}

	
	/**
	 * EigeneListe ausprobieren
	 * @param args wird nicht verwendet
	 */
	public static void main(String args[])
	{
		SortierteListe<Integer> l = 
				new SortierteListe<Integer>(new LaesstAllesZu());
				//EigeneListe(Probe<Integer> bedingung)
		l.add(1);
		l.add(2);
		System.out.println(l);
		l.add(5);
		System.out.println(l);



		SortierteListe<String> woerter = 
				new SortierteListe<>(new FaengtAnMitAProbe());
		woerter.add("Adalbert");
		woerter.add("Bertha");
		woerter.add("Anna");
		woerter.add("Anton");
		woerter.add("Zacharias");
		System.out.println(woerter);



/*		SortierteListe<Exception> dasGingAllesSchief = 
				new SortierteListe<>(new FehlerNiedrigsterEbene());
		dasGingAllesSchief.add(new IllegalArgumentException());
		dasGingAllesSchief.add(new ArithmeticException());
		System.out.println(dasGingAllesSchief);
*/

		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", "13.07.76");
		Girokonto g1 = new Girokonto(ich, 1234, new Geldbetrag(0));
		Girokonto g2 = new Girokonto(ich, 999, new Geldbetrag(1000));
		Girokonto g3 = new Girokonto(ich, 34567, new Geldbetrag(70));
		
		SortierteListe<Girokonto> kontoliste = 
				new SortierteListe<>(new LaesstAllesZu());
		kontoliste.add(g1);
		kontoliste.add(g2);
		kontoliste.add(g3);
		System.out.println(kontoliste);



		//Collection von LocalDate erstellen
		Collection<LocalDate> localDatesCollection = new LinkedList<>();
		localDatesCollection.add(LocalDate.of(2000, 01, 01));
		localDatesCollection.add(LocalDate.of(2010, 01, 01));
		localDatesCollection.add(LocalDate.of(2020, 01, 01));

		//SortierteListe von ChronoLocalDate erstellen und Collection hinzufügen
		SortierteListe<ChronoLocalDate> chronoLocalDates = new SortierteListe<>(new LaesstAllesZu());
		chronoLocalDates.addAll(localDatesCollection);
		System.out.println("ChronoLocalDates: "  + chronoLocalDates);

		//Elemente mit LaesstAllesZu() Probe löschen
		//durch Probe<? super T> werden auch Proben für Object aktzeptiert
		chronoLocalDates.remove(new LaesstAllesZu());
		System.out.println("ChronoLocalDates nach remove(): "  + chronoLocalDates);

		//Liste mit LocalDate() erstellen
		SortierteListe<LocalDate> localDates = new SortierteListe<>(new LaesstAllesZu());
		localDates.addAll(localDatesCollection);
		System.out.println("LocalDates: "  + localDates);

		//Wert JapaneseDate erstellen und zum Filtern benutzen
		JapaneseDate japDate = JapaneseDate.now();
		localDates.removeAllBigger(japDate);
		System.out.println("LocalDates nach filtern mit JapaneseDate: "  + localDates);




	}

}
