package bankprojekt.aktienhandel;

import java.util.Random;
import java.util.concurrent.*;
import java.util.Map;

import bankprojekt.basisdaten.Geldbetrag;

/**
 * Eine Aktie, die ständig ihren Kurs verändert
 * @author Doro
 *
 */
public class Aktie {
	
	private static Map<String, Aktie> alleAktien = new ConcurrentHashMap<>();
	private String wkn;
	private Geldbetrag kurs;
	private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

	/**
	 * gibt die Aktie mit der gewünschten Wertpapierkennnummer zurück
	 * @param wkn Wertpapierkennnummer
	 * @return Aktie mit der angegebenen Wertpapierkennnummer oder null, wenn es diese WKN
	 * 			nicht gibt.
	 */
	public static Aktie getAktie(String wkn)
	{
		return alleAktien.get(wkn);
	}
	
	/**
	 * erstellt eine neu Aktie mit den angegebenen Werten
	 * @param wkn Wertpapierkennnummer
	 * @param k aktueller Kurs
	 * @throws IllegalArgumentException wenn einer der Parameter null bzw. negativ ist
	 * 		                            oder es eine Aktie mit dieser WKN bereits gibt
	 */
	public Aktie(String wkn, Geldbetrag k) {
		if(wkn == null || k == null || k.isNegativ() || alleAktien.containsKey(wkn))
			throw new IllegalArgumentException();	
		this.wkn = wkn;
		this.kurs = k;
		alleAktien.put(wkn, this);
		starteKursAktualisierung(); //startet die fortlaufende Aktualisierung der Aktie
	}

	/**
	 * Wertpapierkennnummer
	 * @return WKN der Aktie
	 */
	public String getWkn() {
		return wkn;
	}

	/**
	 * aktueller Kurs
	 * @return Kurs der Aktie
	 */
	public Geldbetrag getKurs() {
		return kurs;
	}

	/**
	 * Aktualisiert fortlaufend den Kurs einer Aktie.
	 * Erzeugt eine zufällige Zahl "zeit" zwischen 1 und 5 (inklusive). Der Kurs der AKtie wird
	 * in dem Abstand von "zeit" um eine zufällige Zahl "prozent" zwischen -3 und 3 (inklusive) verringert bzw. erhöht.
	 */
	private void starteKursAktualisierung() {
		long zeit = ThreadLocalRandom.current().nextLong(1,6); //Zufallszahl zwischen 1 und 5

		//Wiederholbare Aufgabe definieren
		Runnable kursAktualisierung = () -> {
			double prozent = ThreadLocalRandom.current().nextDouble(-3.0,3.0); //zufallszahl zwischen -3 und 3
			this.kurs = this.kurs.mal(1 + (prozent / 100.0)); //kurs um den prozentualen Wert erhöhen/senken
		};

		//Scheduler sagen, dass er die Aktualisierung alle "zeit" Sekunden ausführen soll
		SCHEDULER.scheduleAtFixedRate(kursAktualisierung, zeit, zeit, TimeUnit.SECONDS);
	}

	/**
	 * Methode zum Saubermachen, falls die Aktie nicht mehr gebraucht wird.
	 */
	public void aktieShutdown() {
		SCHEDULER.shutdown();
	}
}
