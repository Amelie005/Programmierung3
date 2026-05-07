package bankprojekt.verwaltung;

import bankprojekt.basisdaten.*;
import bankprojekt.exceptions.UngueltigeKontonummerException;

import java.util.*;

public class Bank {


    private long bankleitzahl;
    private List<Konto> konten = new ArrayList<>();


    /**
     * Erstellt eine Bank mit der angegebenen Bankleitzahl.
     * @param bankleitzahl
     * @throws IllegalArgumentException wenn die BLZ nicht positiv ist oder mehr als 12 Ziffern hat
     */
    public Bank(long bankleitzahl) {
        //BLZ sollte nicht negativ oder null sein (nicht üblich in Banken)
        if (bankleitzahl <= 0) {
            throw new IllegalArgumentException("Die Bankleitzahl muss eine positive Zahl sein!");
        }
        //BLZ sollte nicht mehr als 12 Ziffern haben (guter Puffer für jedes Land)
        //Long.MAX_VALUE hätte 19 Stellen, aber kein Land der Welt hat BLZ mit 19 Stellen
        if (bankleitzahl > 999999999999L) {
            throw new IllegalArgumentException("Die Bankleitzahl ist zu lang!");
        }
        this.bankleitzahl = bankleitzahl;
    }


    /**
     * Liefert die Bankleitzahl zurück.
     * @return
     */
    public long getBankleitzahl() {
        return bankleitzahl;
    }


    /**
     * Erstellt ein Girokonto für den angegebenen Kunden. Es wird eine beliebige neue
     * nicht vergebene Kontonummer erzeugt. Das Girokonto wird mit dieser Nummer in der Kontoliste
     * gespeichert und die vergebene Kontonummer wird zurückgegeben.
     * @param inhaber Kunde für den das Girokonto erstellt werden soll
     * @return neu vergebene Kontonummer
     */
    public long girokontoErstellen(Kunde inhaber) {
        Random rand = new Random();
        long neueKontonummer;
        boolean gespeichert = false;

        do {
            //erstellt neue zufällige Kontonummer
            neueKontonummer = Math.abs(rand.nextLong()) % 10000000000L;

            boolean kontonummerExistiertBereits = false;

            //prüft ob Kontonummer bereits bei der Bank existiert
            for (Konto k : konten) {
                if (k.getKontonummer() == neueKontonummer) {
                    kontonummerExistiertBereits = true;
                    break; //falls die Kontonummer bereits verwendet wird
                }
            }

            //wenn die Kontonummer noch nicht verwendet wird
            if (!kontonummerExistiertBereits) {
                try {

                    //neues Girkonto mit inhaber und der neuen Kontonummer erstellt
                    Girokonto neusGirokonto =  new Girokonto(inhaber, neueKontonummer, new Geldbetrag(0.0));

                    //wird zur Liste der Konten bei dieser Bank hinzugefügt
                    this.konten.add(neusGirokonto);
                    gespeichert = true;
                } catch (UngueltigeKontonummerException e) {
                    //Schleife läuft weiter und würfelt neue Nummer
                }
            }
        } while (!gespeichert);

        return neueKontonummer;
    }


    /**
     * Erstellt ein Sparbuch für den angegebenen Kunden. Es wird eine beliebige neue
     * nicht vergebene Kontonummer erzeugt. Das Sparbuch wird mit dieser Nummer in der Kontoliste
     * gespeichert und die vergebene Kontonummer wird zurückgegeben.
     * @param inhaber Kunde für den das Sparbuch erstellt werden soll
     * @return neu vergebene Kontonummer
     */
    public long sparbuchErstellen(Kunde inhaber) {
        Random rand = new Random();
        long neueKontonummer;
        boolean gespeichert = false;

        do {
            //erstellt neue zufällige Kontonummer
            neueKontonummer = Math.abs(rand.nextLong()) % 10000000000L;

            boolean kontonummerExistiertBereits = false;

            //prüft ob Kontonummer bereits bei der Bank existiert
            for (Konto k : konten) {
                if (k.getKontonummer() == neueKontonummer) {
                    kontonummerExistiertBereits = true;
                    break; //falls die Kontonummer bereits verwendet wird
                }
            }

            //wenn die Kontonummer noch nicht verwendet wird
            if (!kontonummerExistiertBereits) {
                try {

                    //neues Sparbuch mit inhaber und der neuen Kontonummer erstellt
                    Sparbuch neuesSparbuch = new  Sparbuch(inhaber, neueKontonummer);

                    //wird zur Liste der Konten bei dieser Bank hinzugefügt
                    this.konten.add(neuesSparbuch);
                    gespeichert = true;
                } catch (UngueltigeKontonummerException e) {
                    //Schleife läuft weiter und würfelt neue Nummer
                }
            }
        } while (!gespeichert);

        return neueKontonummer;
    }

    /**
     * Liefert eine Auflistung von Kontonummern und Kontostand zu jedem
     * Konto in jeweils einer Zeile zurück.
     * @return Auflistung von Kontonummern mit jeweiligem Kontostand
     */
    public String getAlleKonten() {

    }

    /**
     * Liefert eine Menge aller gültigen Kontonummern in der Bank.
     * @return Menge aller gültigen Kontonummern in der Bank als String
     */
    public Set<Long> getAlleKontonummern() {

    }

    /**
     * Liefert eine Menge aller Kunden, die bei dieser Bank mindestens ein Konto haben.
     * Kunden sind absteigend nach ihrem Geburtstag sortiert.
     * @return sortiertes Set von Kunden bei dieser Bank
     */
    public SortedSet<Kunde> getAlleKunden() {

    }

    /**
     * Löscht das Konto mit der angegebenen Nummer.
     * @param number Konto mit dieser Nummer, welches gelöscht werden soll
     * @return true, wenn das Konto gelöscht wurde, false, wenn die Kontonummer nicht existiert
     */
    public boolean kontoLoeschen(long number) {

    }

    /**
     * Liefert den Kontostand des Kontos mit der angegebenen Nummer zurück.
     * @param nummer Konto mit dieser Nummer, dessen Kontostand zurückgegeben werden soll
     * @return Kontostand des Kontos mit der angegebenen Nummer
     */
    public Geldbetrag getKontostand(long nummer) {

    }

    /**
     * Hebt den Betrag vom Konto mit der Nummer von ab und gibt zurück, ob
     * die Abhebung funktioniert hat.
     * @param von Nummer vom Konto von dem die Abhebung stattfinden soll
     * @param betrag Betrag der vom Konto mit der Nummer von abgehoben werden soll
     * @return true, wenn das Abheben funktioniert hat, false, wenn das Abheben nicht funktioniert hat
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) {

    }

    /**
     * Überweist den genannten Betrag vom überweisungsfähigen Konto mit der Nummer vonKontonr
     * zum überweisungsfähigen Konto mit der Nummer nochKontonr und gibt zurück, ob die
     * Überweisung funktioniert hat. Es funktionieren nur bankinterne Überweisungen.
     * @param vonKontonr Nummer des Kontos von dem der Geldbetrag überwiesen werden soll
     * @param nachKontonr Nummer des Kontos das den überwiesenen Betrag erhalten soll
     * @param betrag Betrag der überwiesen soll
     * @param verwendungszweck Zweck wozu das Geld überwiesen wird
     * @return true, wenn die Überweisung funktioniert hat, false, wenn die Überweisung nicht funktioniert hat
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, Geldbetrag betrag, String verwendungszweck){

    }

    /**
     * Liefert eine Map zurück, in der für jeden Kunden der Bank die Summe der Kontostände
     * aller seiner Konten angegeben ist.
     * @return Map mit Summe aller Kontostände des Kunden
     */
    public Map<Kunde, Geldbetrag> getGesamtKontostaende() {

    }

    /**
     * Löscht alle Konten die inhaber gehören und gibt die Anzahl der gelöschten Konten zurück.
     * @param inhaber Inhaber dessen Konten gelöscht werden sollen
     * @return Anzahl der gelöschten Konten
     */
    public int kontenEInesKundenLoeschen(Kunde inhaber) {

    }
}
