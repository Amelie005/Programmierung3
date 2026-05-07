package bankprojekt.verwaltung;

import bankprojekt.basisdaten.*;
import bankprojekt.exceptions.GesperrtException;
import bankprojekt.exceptions.UngueltigeKontonummerException;

import java.util.*;

public class Bank {
    private final long bankleitzahl;
    private final List<Konto> konten = new ArrayList<>();

    /**
     * Erstellt eine Bank mit der angegebenen Bankleitzahl.
     * @param bankleitzahl Bankleitzahl die der erstellten Bank zugeordnet werden soll
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
     * @return Bankleitzahl der Bank
     */
    public long getBankleitzahl() {
        return bankleitzahl;
    }

    /**
     * Erstellt ein Girokonto für den angegebenen Kunden. Es wird eine beliebige neue
     * nicht vergebene Kontonummer erzeugt. Das Girokonto wird mit dieser Nummer in der Kontoliste
     * gespeichert und die vergebene Kontonummer wird zurückgegeben.
     *
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

                    //neues Girokonto mit inhaber und der neuen Kontonummer erstellt
                    Girokonto neusGirokonto = new Girokonto(inhaber, neueKontonummer, new Geldbetrag(0.0));

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
     *
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
                    Sparbuch neuesSparbuch = new Sparbuch(inhaber, neueKontonummer);

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
     *
     * @return Auflistung von Kontonummern mit jeweiligem Kontostand
     */
    public String getAlleKonten() {
        StringBuilder ausgabe = new StringBuilder();

        for (Konto k : konten) {
            //fügt den Text einfach hinten an, ohne neue Objekte zu erzeugen
            ausgabe.append("Kontonummer: ")
                    .append(k.getKontonummer())
                    .append(", aktueller Kontostand: ")
                    .append(k.getKontostand())
                    .append("\n");
        }

        return ausgabe.toString();
    }

    /**
     * Liefert eine Menge aller gültigen Kontonummern in der Bank.
     *
     * @return Menge aller gültigen Kontonummern in der Bank
     */
    public Set<Long> getAlleKontonummern() {
        Set<Long> ausgabe = new HashSet<>();

        for (Konto k : konten) {
            long kontonummer = k.getKontonummer();
            ausgabe.add(kontonummer);
        }

        return ausgabe;
    }

    /**
     * Liefert eine Menge aller Kunden, die bei dieser Bank mindestens ein Konto haben.
     * Kunden sind absteigend nach ihrem Geburtstag sortiert.
     *
     * @return sortiertes Set von Kunden bei dieser Bank
     */
    public SortedSet<Kunde> getAlleKunden() {
        SortedSet<Kunde> ausgabe = new TreeSet<>(new Comparator<>() {
            public int compare(Kunde k1, Kunde k2) {
                int vergleich = k1.getGeburtstag().compareTo(k2.getGeburtstag());

                //wenn die Geburtstage nicht gleich sind, sortiere danach
                if (vergleich != 0) {
                    return vergleich;
                }
                //wenn sie gleich sind, vergleiche den Namen,
                //sonst würden Kunden mit dem gleichen Geburtstag einfach nicht eingefügt werden
                return k1.getName().compareTo(k2.getName());
            }
        });

        for (Konto k : konten) {
            Kunde kunde = k.getInhaber();
            ausgabe.add(kunde);
        }

        return ausgabe;
    }

    /**
     * Löscht das Konto mit der angegebenen Nummer.
     *
     * @param number Konto mit dieser Nummer, welches gelöscht werden soll
     * @return true, wenn das Konto gelöscht wurde, false, wenn die Kontonummer nicht existiert
     */
    public boolean kontoLoeschen(long number) {
        //expliziter Iterator für die Liste konten
        Iterator<Konto> it = konten.iterator();

        while (it.hasNext()) { //solange noch ein nächstes Element existiert
            Konto k = it.next();

            //wenn Kontonummer mit der gesuchten Kontonummer übereinstimmt
            if (k.getKontonummer() == number) {
                it.remove(); //Konto wird gelöscht
                return true;
            }
        }
        //falls nicht existent, nichts gelöscht und false zurückgegeben
        return false;
    }

    /**
     * Liefert den Kontostand des Kontos mit der angegebenen Nummer zurück.
     *
     * @param nummer Konto mit dieser Nummer, dessen Kontostand zurückgegeben werden soll
     * @return Kontostand des Kontos mit der angegebenen Nummer
     * @throws IllegalArgumentException wenn kein Konto mit dieser Kontonummer existiert
     */
    public Geldbetrag getKontostand(long nummer) {

        for (Konto k : konten) {
            if (k.getKontonummer() == nummer) {
                return k.getKontostand();
            }
        }
        throw new IllegalArgumentException("Konto mit der Kontonummer: " + nummer + " existiert nicht!");
    }

    /**
     * Hebt den Betrag vom Konto mit der Nummer von ab und gibt zurück, ob
     * die Abhebung funktioniert hat.
     *
     * @param von    Nummer vom Konto von dem die Abhebung stattfinden soll
     * @param betrag Betrag der vom Konto mit der Nummer von abgehoben werden soll
     * @return true, wenn das Abheben funktioniert hat, false, wenn das Abheben nicht funktioniert hat
     * @throws GesperrtException wenn das Konto gesperrt ist
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) throws GesperrtException {
        for (Konto k : konten) {
            if (k.getKontonummer() == von) {
                return k.abheben(betrag);
            }
        }
        return false;
    }

    /**
     * Zahlt den Betrag auf das angegebene Konto mit der Kontonummer auf ein.
     *
     * @param auf    Konto mit dieser Kontonummer auf das eingezahlt werden soll
     * @param betrag Betrag der auf Konto mit der Kontonummer auf eingezahlt werden soll
     * @return true, wenn das Einzahlen erfolgreich war, false, wenn das Einzahlen nicht erfolgreich war
     */
    public boolean geldEinzahlen(long auf, Geldbetrag betrag) {
        for (Konto k : konten) {
            if (k.getKontonummer() == auf) {
                k.einzahlen(betrag);
                return true;
            }
        }
        return false;
    }

    /**
     * Überweist den genannten Betrag vom überweisungsfähigen Konto mit der Nummer vonKontonr
     * zum überweisungsfähigen Konto mit der Nummer nachKontonr und gibt zurück, ob die
     * Überweisung funktioniert hat. Es funktionieren nur bankinterne Überweisungen.
     *
     * @param vonKontonr       Nummer des Kontos von dem der Geldbetrag überwiesen werden soll
     * @param nachKontonr      Nummer des Kontos das den überwiesenen Betrag erhalten soll
     * @param betrag           Betrag der überwiesen soll
     * @param verwendungszweck Zweck wozu das Geld überwiesen wird
     * @return true, wenn die Überweisung funktioniert hat, false, wenn die Überweisung nicht funktioniert hat
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, Geldbetrag betrag, String verwendungszweck) {
        UeberweisungsfaehigesKonto vonKonto = null;
        UeberweisungsfaehigesKonto nachKonto = null;

        //nach den Konten suchen und gleichzeitig prüfen ib sie überweisungsfähig sind
        for (Konto k : konten) {
            if (k.getKontonummer() == vonKontonr && k instanceof UeberweisungsfaehigesKonto) {
                vonKonto = (UeberweisungsfaehigesKonto) k;
            }
            if (k.getKontonummer() == nachKontonr && k instanceof UeberweisungsfaehigesKonto) {
                nachKonto = (UeberweisungsfaehigesKonto) k;
            }
        }

        //prüfen, ob beide Konten gefunden worden sind
        if (vonKonto == null || nachKonto == null) {
            return false;
        }

        try {
            boolean erfolgreich = vonKonto.ueberweisungAbsenden(
                    betrag,
                    nachKonto.getInhaber().getName(), //man braucht den Namen des Empfängers
                    nachKontonr,
                    this.bankleitzahl,
                    verwendungszweck
            );

            //wenn absenden funktioniert hat, muss es noch empfangen werden
            if (erfolgreich) {
                nachKonto.ueberweisungEmpfangen(
                        betrag,
                        vonKonto.getInhaber().getName(), //auch wieder Name nötig, aber hier vom Sender
                        vonKontonr,
                        this.bankleitzahl,
                        verwendungszweck
                );
                return true;
            }

        } catch (GesperrtException e) {
            //falls eines der Konten gesperrt ist
            return false;
        }
        return false;
    }

    /**
     * Liefert eine Map zurück, in der für jeden Kunden der Bank die Summe der Kontostände
     * aller seiner Konten angegeben ist.
     *
     * @return Map mit Summe aller Kontostände des Kunden
     */
    public Map<Kunde, Geldbetrag> getGesamtKontostaende() {
        //man könnte hier alternativ auch einfach merge() nutzen, indem man die for-Schleife ersetzt:
        //for (Konto k: konten){
        //        ergebnis.merge(k.getInhaber(), k.getKontostand(), (alterStand, neuerStand) -> alterStand.plus(neuerStand));
        //    }

        Map<Kunde, Geldbetrag> ergebnis = new HashMap<>();

        //über alle Konten in der Bank iterieren
        for (Konto k : konten) {
            //für jedes Konto den Inhaber und den Kontostand holen
            Kunde inhaber = k.getInhaber();
            Geldbetrag kontostand = k.getKontostand();

            //prüfen ob Kunde schon in der Map ist, weil er mehr als ein Konto hat
            if (ergebnis.containsKey(inhaber)) {
                //falls ja, Kontostand des neu gefundenen Kontos mit dem schon vorhandenen Gesamtkontostand addieren
                Geldbetrag bisherigeSumme = ergebnis.get(inhaber);
                //könnte man mit Map.compute ersetzen
                ergebnis.put(inhaber, bisherigeSumme.plus(kontostand));
            } else {
                //wenn noch nicht in der Map vorhanden, einfach neu hinzufügen
                ergebnis.put(inhaber, kontostand);
            }
        }
        return ergebnis;
    }

    /**
     * Löscht alle Konten die inhaber gehören und gibt die Anzahl der gelöschten Konten zurück.
     *
     * @param inhaber Inhaber dessen Konten gelöscht werden sollen
     * @return Anzahl der gelöschten Konten
     */
    public int kontenEinesKundenLoeschen(Kunde inhaber) {
        int anzahlGeloeschterKonten = 0;
        Iterator<Konto> it = konten.iterator();

        while (it.hasNext()) {
            Konto konto = it.next();

            if (konto.getInhaber().equals(inhaber)) {
                it.remove();
                anzahlGeloeschterKonten++;
            }
        }
        return anzahlGeloeschterKonten;
    }
}
