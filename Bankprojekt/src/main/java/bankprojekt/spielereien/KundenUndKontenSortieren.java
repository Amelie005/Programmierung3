package bankprojekt.spielereien;

import java.time.LocalDate;
import bankprojekt.basisdaten.*;

import java.util.Arrays;
import java.util.Scanner;

import static java.util.Arrays.sort;

/**
 * Klasse, um ein paar Konten und Kunden zu sortieren und Arrays.sort
 * zu nutzen
 */
public class KundenUndKontenSortieren {
	/**
	 * sortiert ein paar Kunden und Konten
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		Scanner tastatur = new Scanner(System.in);
		System.out.println("Bitte Bankleitzahl eingeben: ");
		long blz = tastatur.nextLong();  //ohne große Fehlerbehandlung...

		Kunde hans = new Kunde("Hans", "Meier", "Unterm Regenbogen 19", LocalDate.of(1990, 1, 5));
		Kunde otto = new Kunde("Otto", "Kar", "Hoch über den Wolken 7", LocalDate.of(1992, 2, 25));
		Kunde sabrina = new Kunde("Sabrina", "August", "Im Wald 15", LocalDate.of(1988, 3, 21));
		Kunde doro = new Kunde("Doro", "Hubrich", "Zuhause", LocalDate.of(1976, 7, 13));
		Kunde miriam = new Kunde("Miriam", "Andres", "Woanders 17", LocalDate.of(2002, 7, 15));
		Kunde ernst = new Kunde("Ernst", "August", "Im Wald 15", LocalDate.of(1989, 3, 1));
		
		Konto eins = new Girokonto(hans, 123, Geldbetrag.NULL_EURO);
		Konto zwei = new Sparbuch(otto, 234);
		Konto drei = new Sparbuch(doro, 333);
		Konto vier = new Girokonto(sabrina, 432, Geldbetrag.NULL_EURO);
		Konto fuenf = new Sparbuch(miriam, 987);
		Konto sechs = new Girokonto(ernst, 945, Geldbetrag.NULL_EURO);
		
		Kunde[] kunden = {hans, otto, sabrina, doro, miriam, ernst};
		Konto[] konten = {eins, zwei, drei, vier, fuenf, sechs};

		//Kunden werden sortiert nach Vorname -> Nachname -> Adresse -> Geburtstag
		System.out.println("Kunden:");
		Arrays.sort(kunden);
		System.out.println(Arrays.toString(kunden));
		System.out.println();

		System.out.println("Konten nach Kontonummer:");
		//Konten werden nach Kontonummer sortiert (aufsteigend, "natürliche Ordnung")
		Arrays.sort(konten);
		System.out.println(Arrays.toString(konten));
		System.out.println();

		//Konten werden nach Geburtsdatum des Inhabers sortiert (aufsteigend)
		System.out.println("Konten nach Geburtstag des Inhabers:");
		Arrays.sort(konten, Konto.BY_GEBURTSTAG);
		System.out.println(Arrays.toString(konten));
		System.out.println();

		//Konten werden nach Iban sortiert
		System.out.println("Konten nach Iban:");
		Arrays.sort(konten, Konto.byIban(blz));
		System.out.println(Arrays.toString(konten));


    }
}
