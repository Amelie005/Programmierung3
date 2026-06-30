package bankprojekt.spielereien;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.List;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.exceptions.GesperrtException;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.exceptions.UngueltigeKontonummerException;
import bankprojekt.fabriken.GirokontoFabrik;
import bankprojekt.verwaltung.Bank;

/**
 * probiert die Stream-Methoden der Bank-Klasse aus
 */
public class Bankspielereien {
	/**
	 * Methoden zur Stream-Aufgabe ausprobieren
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) throws GesperrtException {
		Bank bank = new Bank(12345);
		int aktuellesJahr = LocalDate.now().getYear();
		Kunde opa = new Kunde("Opa", "Otto", "Altersheim", LocalDate.of(aktuellesJahr - 70, 3, 1));
		Kunde oma = new Kunde("Oma", "Emma", "Altersheim", LocalDate.of(aktuellesJahr - 67, 12, 5));
		Kunde kind = new Kunde("Kind", "Klein", "zuhause", LocalDate.of(aktuellesJahr - 8, 2, 28));
		Kunde teenager = new Kunde("Teenager", "Mittel", "zuhause", LocalDate.of(aktuellesJahr - 15, 6, 18));
		Kunde geradeErwachsen = new Kunde("Erwachsen", "Neu", "zuhause", LocalDate.of(aktuellesJahr - 18, 1, 1));
		Kunde nochNichtGanzErwachsen = new Kunde("Erwachsen", "Fast", "zuhause", LocalDate.of(aktuellesJahr - 18, 12, 31));
		Kunde mama = new Kunde("Mama", "Erna", "zuhause", LocalDate.of(aktuellesJahr - 42, 3, 5));
		Kunde papa = new Kunde("Papa", "Hugo", "zuhause", LocalDate.of(aktuellesJahr - 43, 7, 15));
		Kunde senior = new Kunde("Uropa", "Heinz", "Neben dem Friedhof", LocalDate.of(aktuellesJahr - 95, 2, 28));

		long nrOpa1 = bank.kontoErstellen(new GirokontoFabrik(), opa);
		long nrOpa2 = bank.kontoErstellen(new GirokontoFabrik(), opa);
		long nrOpa3 = bank.kontoErstellen(new GirokontoFabrik(), opa);
		long nrOma = bank.kontoErstellen(new GirokontoFabrik(), oma);
		long nrKind1 = bank.kontoErstellen(new GirokontoFabrik(), kind);
		long nrKind2 = bank.kontoErstellen(new GirokontoFabrik(), kind);
		long nrTeenager = bank.kontoErstellen(new GirokontoFabrik(), teenager);
		long nrGeradeErwachsen1 = bank.kontoErstellen(new GirokontoFabrik(), geradeErwachsen);
		long nrGeradeErwachsen2 = bank.kontoErstellen(new GirokontoFabrik(), geradeErwachsen);
		long nrNochNichtGanzErwachsen = bank.kontoErstellen(new GirokontoFabrik(), nochNichtGanzErwachsen);
		long nrMama1 = bank.kontoErstellen(new GirokontoFabrik(), mama);
		long nrMama2 = bank.kontoErstellen(new GirokontoFabrik(), mama);
		long nrMama3 = bank.kontoErstellen(new GirokontoFabrik(), mama);
		long nrMama4 = bank.kontoErstellen(new GirokontoFabrik(), mama);
		long nrPapa1 = bank.kontoErstellen(new GirokontoFabrik(), papa);
		long nrPapa2 = bank.kontoErstellen(new GirokontoFabrik(), papa);
		long nrSenior = bank.kontoErstellen(new GirokontoFabrik(), senior);

		//getBankleitzahl()
		System.out.println("Bankleitzahl dieser Bank: " + bank.getBankleitzahl());

		//Einzahlen & Abheben
		System.out.println("Tests Einzahlen & Abheben:");
		bank.geldEinzahlen(nrOpa1, new Geldbetrag(1000.0));
		bank.geldEinzahlen(nrOpa1, new Geldbetrag(500.0)); //Kontostand: 1500
		System.out.println("Kontostand Opa1 (erwartet 1500): " + bank.getKontostand(nrOpa1));

		boolean abhebenErfolgreich = bank.geldAbheben(nrOpa1, new Geldbetrag(200.0));
		System.out.println("Abheben von 200€ erfolgreich: " + abhebenErfolgreich);
		System.out.println("Kontostand Opa1 (erwartet 1300): " + bank.getKontostand(nrOpa1));

		//Fehlerfall: Abheben von nicht existierendem Konto
		try {
			bank.geldAbheben(999999L, new Geldbetrag(10.0));
			System.out.println("Abheben von nicht existierendem Konto (erwartet Exception): keine Exception geworfen!");
		} catch (UngueltigeKontonummerException e) {
			System.out.println("Abheben von nicht existierendem Konto wirft erwartungsgemäß UngueltigeKontonummerException ✓");
		}

		//Überweisungen
		System.out.println("\nTests Überweisung:");
		bank.geldEinzahlen(nrMama1, new Geldbetrag(100.0));

		//Mama überweist an Papa
		boolean ueberweisung = bank.geldUeberweisen(nrMama1, nrPapa1, new Geldbetrag(50.0), "Essen");

		System.out.println("Überweisung erfolgreich: " + ueberweisung);
		System.out.println("Mamas Kontostand (erwartet 50): " + bank.getKontostand(nrMama1));
		System.out.println("Papas Kontostand (erwartet 50): " + bank.getKontostand(nrPapa1));

		//Map
		System.out.println("\nTests Gesamt-Kontostände Map:");

		//Opa auf weiteres Konto Geld einzahlen
		bank.geldEinzahlen(nrOpa2, new Geldbetrag(100.0));
		//Opa hat nun: nrOpa1 (1300) + nrOpa2 (100) = 1400

		Map<Kunde, Geldbetrag> stand = bank.getGesamtKontostaende();
		System.out.println("Gesamtstand Opa (erwartet 1400): " + stand.get(opa));
		System.out.println("Anzahl Kunden in der Map: " + stand.size());

		//Löschen
		System.out.println("\nTests Löschen:");

		//ein einziges Konto löschen
		boolean geloescht = bank.kontoLoeschen(nrSenior);
		System.out.println("Konto von Senior gelöscht: " + geloescht);
		System.out.println("Existiert es noch?(sollte Exception werfen):");
		try {
			bank.getKontostand(nrSenior);
		} catch (UngueltigeKontonummerException e) {
			System.out.println("Konto existiert nicht mehr.");
		}

		//alle Konten von Mama löschen
		int anzahlMama = bank.kontenEinesKundenLoeschen(mama);
		System.out.println("Anzahl gelöschter Konten von Mama (erwartet 4): " + anzahlMama);

		//Dispo Limit überschritten
		System.out.println(System.lineSeparator() + "Test Dispo Limit:");
		boolean zuViel = bank.geldAbheben(nrPapa1, new Geldbetrag(10000.0));
		System.out.println("Abheben von 10.000€ (erwartet false): " + zuViel);

		//Sortierung der Kunden überprüfen
		System.out.println( System.lineSeparator() + "Test Kundensortierung:");
		SortedSet<Kunde> kunden = bank.getAlleKunden();
		System.out.println("Kunden absteigend nach Geburtstag:");
		for(Kunde k : kunden) {
			System.out.println(k.getGeburtstag() + " : " + k.getName());
		}

		//getKundenMitLeeremKonto
		System.out.println(System.lineSeparator() + "Test getKundenMitLeeremKonto:");

		bank.geldAbheben(nrKind1, new Geldbetrag(50.0));
		bank.geldAbheben(nrKind1, new Geldbetrag(50.0));
		bank.geldAbheben(nrPapa1, new Geldbetrag(150.0));
		bank.geldAbheben(nrPapa2, new Geldbetrag(200.0));

		List<Kunde> kundenMitLeeremKonto = bank.getKundenMitLeeremKonto();
		System.out.println("Kunden mit leerem Konto (erwartet: 2): " + kundenMitLeeremKonto.size());
		if(!kundenMitLeeremKonto.isEmpty()) {
			for (Kunde k : kundenMitLeeremKonto) {
				System.out.println(k.getName());
			}
		}

		//getKundenGeburtstage
		System.out.println(System.lineSeparator() + "Test getKundengeburtstage:");
		String geburtstageAusgabe = bank.getKundengeburtstage();
		System.out.println("Sortiert nach Monat + Tag und dann Name:");
		System.out.print(geburtstageAusgabe);

		//getAnzahlSenioren
		System.out.println(System.lineSeparator() + "Test getAnzahlSenioren:");
		//nrSenior wurde weiter oben schon gelöscht
		long anzahlSenioren = bank.getAnzahlSenioren();
		System.out.println("Anzahl Senioren (erwartet: 1): " + anzahlSenioren);

		//schenkungAnNeueErwachsene
		System.out.println(System.lineSeparator() + "Test schenkungAnNeueErwachsene:");
		System.out.println("Vor der Schenkung:");
		System.out.println("GeradeErwachsen1: " + bank.getKontostand(nrGeradeErwachsen1));
		System.out.println("NochNichtGanzErwachsen: " + bank.getKontostand(nrNochNichtGanzErwachsen));
		System.out.println("Teenager (wird erst 15): " + bank.getKontostand(nrTeenager));

		bank.schenkungAnNeueErwachsene(new Geldbetrag(100.0));

		System.out.println(System.lineSeparator() + "Nach der Schenkung:");
		System.out.println("GeradeErwachsen1 (erwartet: 100.0 oder auf nrGeradeErwachsen2 eingezahlt): "
				+ bank.getKontostand(nrGeradeErwachsen1));
		System.out.println("GeradeErwachsen2: " + bank.getKontostand(nrGeradeErwachsen2));
		System.out.println("NochNichtGanzErwachsen (erwartet: 100.0): " + bank.getKontostand(nrNochNichtGanzErwachsen));
		System.out.println("Teenager (erwartet: 0.0, falsches Alter): " + bank.getKontostand(nrTeenager));
	}
}