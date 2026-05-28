package bankprojekt.verwaltung;

import bankprojekt.basisdaten.*;
import bankprojekt.exceptions.GesperrtException;
import bankprojekt.exceptions.UngueltigeKontonummerException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * Stellt ein Bank dar.
 * @author Amelie Dzierzawa, 599428
 */
public class Bank {
    private final long bankleitzahl;
    private final Map<Long, Konto> konten = new HashMap<>();

    /**
     * Zähler für die nächste zu vergebende Kontonummer. Startet bei 1,
     * wird nie zurück gesetzt.
     */
    private long naechsteKontonummer = 1;

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
            throw new IllegalArgumentException("Die Bankleitzahl ist zu lang! Sie kann höchstens 12 Stellen betragen.");
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
     * Hilfsmethode: Sucht Konto mit der angegebenen Nummer und gibt es zurück, soweit
     * es keine Exception auslöst.
     * @param nummer Kontonummer des gesuchten Kontos
     * @param ueberweisungPruefen wenn true, wird zusätzlich geprüft ob das Konto überweisungsfähig ist
     * @return Konto mit der angegebenen Kontonummer
     * @throws UngueltigeKontonummerException wenn die Kontonummer ungültig ist, also <=0
     * @throws UngueltigeKontonummerException wenn die Kontonummer nicht bei diesre Bank vergeben ist
     * @throws IllegalArgumentException wenn das Konto nicht überweisungsfähig ist (weil es bspw. ein Sparbuch ist)
     */
    private Konto kontoGefundenOderException(long nummer, boolean ueberweisungPruefen)
            throws UngueltigeKontonummerException
    {
        if (nummer <= 0) {
            throw new UngueltigeKontonummerException(); //Kontonummer hat ungültiges Format, sollte aber
            //eigentlich nie passieren
        }

        Konto k = konten.get(nummer);

        if (k == null) {
            throw new UngueltigeKontonummerException(); //Konto existiert nicht bei dieser Bank
        }

        if (ueberweisungPruefen && !(k instanceof UeberweisungsfaehigesKonto)) {
            throw new IllegalArgumentException(
                    "Konto mit Kontonummer: " + nummer + " ist nicht überweisungsfähig!"
            ); //Konto ist nicht überweisungsfähig
        }

        return k;
    }

    /**
     * Hilfsmethode: Sucht Konto mit der angegebenen Nummer und gibt es zurück, soweit
     * es keine Exception auslöst.
     * @param nummer Kontonummer des gesuchten Kontos
     * @param gesperrtPruefen wenn true, wird zusätzlich geprüft ob das Konto gesperrt ist
     * @param ueberweisungPruefen wenn true, wird zusätzlich geprüft ob das Konto überweisungsfähig ist
     * @return Konto mit der angegebenen Kontonummer
     * @throws UngueltigeKontonummerException wenn die Kontonummer ungültig ist, also <=0
     * @throws UngueltigeKontonummerException wenn die Kontonummer nicht bei diesre Bank vergeben ist
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn das Konto nicht überweisungsfähig ist (weil es bspw. ein Sparbuch ist)
     */
    private Konto kontoGefundenOderException(long nummer, boolean gesperrtPruefen, boolean ueberweisungPruefen)
        throws UngueltigeKontonummerException, GesperrtException
        {
        if (nummer <= 0) {
            throw new UngueltigeKontonummerException(); //Kontonummer hat ungültiges Format, sollte aber
            //eigentlich nie passieren
        }

        Konto k = konten.get(nummer);

        if (k == null) {
            throw new UngueltigeKontonummerException(); //Konto existiert nicht bei dieser Bank
        }
        if (gesperrtPruefen && k.isGesperrt()) {
            throw new GesperrtException(nummer); //Konto ist gesperrt
        }

        if (ueberweisungPruefen && !(k instanceof UeberweisungsfaehigesKonto)) {
            throw new IllegalArgumentException(
                    "Konto mit Kontonummer: " + nummer + " ist nicht überweisungsfähig!"
            ); //Konto ist nicht überweisungsfähig
        }

        return k;
    }


    /**
     * Erstellt ein Girokonto für den angegebenen Kunden.
     * Die Kontonummer wird automatisch als nächste freie Nummer aufsteigende vergeben,
     * sodass keine Kollisionen entstehen können.
     * @param inhaber Kunde für den das Girokonto erstellt werden soll
     * @return neue vergebene Kontonummer
     * @throws UngueltigeKontonummerException wenn die vergebene Kontonummer ulgültig ist (tritt eigetnlich nicht auf)
     */
    public long girokontoErstellen(Kunde inhaber) throws UngueltigeKontonummerException {
        long neueKontonummer = naechsteKontonummer++;
        Girokonto neuesGirokonto = new Girokonto(inhaber, neueKontonummer, new Geldbetrag(0.0));
        konten.put(neueKontonummer, neuesGirokonto);
        return neueKontonummer;
    }

    /**
     * Erstellt ein Sparbuch für den angegebenen Kunden.
     * Die Kontonummer wird automatisch als nächste freie Nummer aufsteigend vergeben,
     * sodass keine Kollisionen entstehen können.
     * @param inhaber Kunde für den das Sparbuch erstellt werden soll
     * @return neu vergebene Kontonummer
     * @throws UngueltigeKontonummerException wenn die vergebene Kontonummer ungültig ist (sollte eigentlich nie auftreten)
     */
    public long sparbuchErstellen(Kunde inhaber) throws UngueltigeKontonummerException {
        long neueKontonummer = naechsteKontonummer++;
        Sparbuch neuesSparbuch = new Sparbuch(inhaber, neueKontonummer);
        konten.put(neueKontonummer, neuesSparbuch);
        return neueKontonummer;
    }

    /**
     * Liefert eine Auflistung von Kontonummern und Kontostand zu jedem
     * Konto in jeweils einer Zeile zurück.
     * @return Auflistung von Kontonummern mit jeweiligem Kontostand
     */
    public String getAlleKonten() {
        StringBuilder ausgabe = new StringBuilder();

        for (Konto k : konten.values()) {
            //fügt den Text einfach hinten an, ohne neue Objekte zu erzeugen
            ausgabe.append("Kontonummer: ")
                    .append(k.getKontonummer())
                    .append(", aktueller Kontostand: ")
                    .append(k.getKontostand())
                    .append(System.lineSeparator());
        }

        return ausgabe.toString();
    }

    /**
     * Liefert eine Menge aller gültigen Kontonummern in der Bank.
     * @return Menge aller gültigen Kontonummern in der Bank
     */
    public Set<Long> getAlleKontonummern() {
        return new HashSet<>(konten.keySet()); //Kopie, keySet direkt zurückgeben würde interne Map exponieren
    }

    /**
     * Liefert eine Menge aller Kunden, die bei dieser Bank mindestens ein Konto haben.
     * Kunden sind absteigend nach ihrem Geburtstag sortiert.
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
                //wenn sie gleich sind, vergleiche den Namen (siehe 'compareTo()' in Klasse 'Kunde'),
                //sonst würden Kunden mit dem gleichen Geburtstag einfach nicht eingefügt werden
                return k1.compareTo(k2);
            }
        });

        for (Konto k : konten.values()) {
            ausgabe.add(k.getInhaber());
        }

        return ausgabe;
    }

    /**
     * Löscht das Konto mit der angegebenen Nummer.
     * @param number Konto mit dieser Nummer, welches gelöscht werden soll
     * @return true, wenn das Konto gelöscht wurde, false, wenn die Kontonummer nicht existiert
     */
    public boolean kontoLoeschen(long number) {
        return konten.remove(number) != null; //null bedeutet Schlüssel war nicht vorhanden
    }

    /**
     * Liefert den Kontostand des Kontos mit der angegebenen Nummer zurück.
     * Gesperrte konten dürfen auch abgefragt werden.
     * @param nummer Konto mit dieser Nummer, dessen Kontostand zurückgegeben werden soll
     * @return Kontostand des Kontos mit der angegebenen Nummer
     * @throws UngueltigeKontonummerException wenn kein Konto mit dieser Kontonummer existiert oder die
     * Kontonummer ungültig ist
     */
    public Geldbetrag getKontostand(long nummer) throws UngueltigeKontonummerException{
        return kontoGefundenOderException(nummer, false).getKontostand();
    }

    /**
     * Hebt den angegebenen Betrag vom Konto mit der angegebenen Kontonummer von ab.
     * Gibt false zurück, wenn die Abhebung erlaubt aber nicht durchführbar ist (bspw.
     * Kontostand überzogen etc.)
     * @param von Kontonummer des Kontos von dem abgehoben werden soll
     * @param betrag Betrag der abgehoben werden soll
     * @return true, wenn die Abhebung durchgeführt wurde, false, wenn sie nicht möglich war
     * @throws UngueltigeKontonummerException wenn die Kontonummer ungültig oder nicht vorhanden ist
     * @throws GesperrtException wenn das Konto gesperrt ist
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) throws UngueltigeKontonummerException, GesperrtException {
        return kontoGefundenOderException(von, true, false).abheben(betrag);
    }

    /**
     * Zahlt den angegebenen Betrag auf das Konto mit der angegebenen Kontonummer ein.
     * Auf gesperrte Konten darf auch eingezahlt werden.
     * @param auf Kontonummer des Kontos auf das eingezahlt werden soll
     * @param betrag Betrag der eingezahlt werden soll
     * @return true, wenn das Einzahlen erfolgreich war
     * @throws UngueltigeKontonummerException wenn die Kontonummer ungültig oder nicht vorhanden ist
     */
    public boolean geldEinzahlen(long auf, Geldbetrag betrag) throws UngueltigeKontonummerException {
        kontoGefundenOderException(auf, false).einzahlen(betrag);
        return true; //wenn bis hier keine Exception geworfen wird, hat Einzahlung geklappt
    }

    /**
     * Überweist den angegebenen Betrag vom konto mit Nummer vonKontonr
     * zum Konto mit Nummer nachKontonr. Es sind nur bankinterne Überweisungen
     * möglich und beide Konten müssen überweisungsfähig sein.
     * Gibt false zurück, wenn das Absenden des Betrags nicht möglich war (bspw.
     * weil der Kontostand überzogen wurde).
     * @param vonKontonr Kontonummer des sendenden Kontos
     * @param nachKontonr Kontonummer des empfangenden Kontos
     * @param betrag zu überweisender Betrag
     * @param verwendungszweck Verwendungszweck der Überweisung
     * @return true, wenn die Überweisung durchgeführt wurde, false wenn sie nicht möglich war
     * @throws UngueltigeKontonummerException wenn eine der Kontonummern ungültig oder nicht vorhanden ist
     * @throws GesperrtException wenn das sendende Konto gesperrt ist
     * @throws IllegalArgumentException wenn eines der Konten nicht überweisungsfähig ist
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, Geldbetrag betrag, String verwendungszweck)
        throws UngueltigeKontonummerException, GesperrtException
    {
        /* ALTE VERSION
        UeberweisungsfaehigesKonto vonKonto = null;
        UeberweisungsfaehigesKonto nachKonto = null;

        //nach den Konten suchen und gleichzeitig prüfen, ob sie überweisungsfähig sind
        for (Konto k : konten) {
            if (k.getKontonummer() == vonKontonr && k instanceof UeberweisungsfaehigesKonto) { //KLAUSURRELEVANT
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
        */

        Konto vonRoh = kontoGefundenOderException(vonKontonr, true, true);
        Konto nachRoh = kontoGefundenOderException(nachKontonr, true);

        if (vonRoh instanceof UeberweisungsfaehigesKonto vonKonto && nachRoh instanceof UeberweisungsfaehigesKonto nachKonto) {
            boolean erfolgreich = vonKonto.ueberweisungAbsenden(
                    betrag,
                    nachKonto.getInhaber().getName(),
                    nachKontonr,
                    this.bankleitzahl,
                    verwendungszweck
            );

            if (erfolgreich) {
                nachKonto.ueberweisungEmpfangen(
                        betrag,
                        vonKonto.getInhaber().getName(),
                        vonKontonr,
                        this.bankleitzahl,
                        verwendungszweck
                );
            }

            return erfolgreich;
        }

        return false; //wird nei erreicht, weil Hilfsmethode schon auf überweisungsfähigkeit prüft
    }

    /**
     * Liefert eine Map zurück, in der für jeden Kunden der Bank die Summe der Kontostände
     * aller seiner Konten angegeben ist.
     * @return Map von kunde zu Gesamtkontostand über alle seine Konten bei dieser Bank
     */
    public Map<Kunde, Geldbetrag> getGesamtKontostaende() {
        Map<Kunde, Geldbetrag> ergebnis = new HashMap<>();

        for (Konto k : konten.values()) {
            ergebnis.merge(k.getInhaber(), k.getKontostand(), (alt, neu) -> alt.plus(neu));
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
        Iterator<Konto> it = konten.values().iterator();

        while (it.hasNext()) {
            if (it.next().getInhaber().equals(inhaber)) {
                it.remove();
                anzahlGeloeschterKonten++;
            }
        }
        return anzahlGeloeschterKonten;
    }

    /**
     * Fügt das gegebene Konto k, bei dem es sich um ein Mock-Objekt
     * handeln sollte, in die Kontenliste der Bank ein und liefert die dabei von der Bank vergebene
     * Kontonummer zurück.
     * @param k Konto das in die Kontoliste der Bank eingefügt werden soll
     * @return für das Konto k vergebene Kontonummer
     */
    public long mockEinfuegen(Konto k) {
        long neueNummer = this.naechsteKontonummer; //neue Kontonummer bestimmen und hochzählen
        this.naechsteKontonummer++;

        this.konten.put(neueNummer, k);

        return neueNummer;
    }

    /**
     * Liefert eine Liste aller Kunden, die ein Konto mit negativem Kontostand haben.
     * Wer mehrere überzogene Konten hat, kommt nicht doppelt vor.
     * @return Liste mit allen Kunden die ein Konto mit negativem Kontostand haben
     */
    public List<Kunde> kundenMitLeeremKonto () {
        Stream<Kunde> kundenMitLeeremKonto = konten.values().stream()
                .filter(k -> k.getKontostand().isNegativ())
                .map(Konto::getInhaber)
                .distinct(); //stellt sicher, dass Kunden mit mehreren überzogenen Konten nicht doppelt vorkommen

        return kundenMitLeeremKonto.toList();
    }

    /**
     * Liefert die Namen und Geburtstage aller Kunden der Bank in einer sortierten Liste,
     * jeweils in einer Zeile.
     * Kunden kommen nicht doppelt vor.
     * @return Liste mit Namen und Gebrustagen aller Kunden der Bank
     */
    public String getKundengeburtstage() {
        StringBuilder sb = new StringBuilder();

        Stream<Kunde> kundenGeburtstage = konten.values().stream()
                .map(Konto::getInhaber)
                .distinct()
                .sorted(Comparator.comparing(Kunde::getGeburtstag).thenComparing(Kunde::getName)); //sortieren nach Geburtstag, bei Gleichheit nach Name

        kundenGeburtstage.forEach(k -> sb.append(k.getName())
                .append(", Geburtstag: ")
                .append(k.getGeburtstag())
                .append(System.lineSeparator()));

        return sb.toString();
    }

    /**
     * Liefert die Anzahl der Kunden zurück, die mindestens 67 Jahre alt sind.
     * @return Anzahl der Kunden die mindestens 67 Jahre alt sind
     */
    public long getAnzahlSenioren() {

        //Kunden, die vor mehr als 67 Jahren geboren wurden, also mindestens 67 Jahre alt sind
        return konten.values().stream()
                .map(Konto::getInhaber)
                .map(Kunde::getGeburtstag)
                .distinct()
                .filter(g -> LocalDate.now().minusYears(67).isAfter(g)) //Period Klasse: minusYears
                .count();

    }

    /**
     * Zahlt auf ein Konto jedes Kunden, der dieses Jahr 18 wird, den Betrag ein.
     * Wenn einer dieser Kunden mehrere Konten hat, wird eines davon beliebig ausgewählt.
     * @param betrag einzuzahlender Betrag
     */
    public void schenkungAnNeueErwachsene (Geldbetrag betrag) {
        //Kunden, die dieses Jahr 18 werden herausfiltern
        Stream<Kunde> kundenDie18Werden = konten.values().stream()
                .map(Konto::getInhaber)
                .distinct()
                .sorted(Comparator.comparing(Kunde::getGeburtstag))
                .filter(g -> LocalDate.now().minusYears(18).isAfter(g.getGeburtstag()));

        //für jeden dieser Kunden ein Konto auswählen und den Betrag einzahlen
        kundenDie18Werden.forEach(k -> {
            //Konto eines Kunden auswählen, hier einfach das erste Konto, das gefunden wird
            Optional<Konto> kontoOptional = konten.values().stream()
                    .filter(konto -> konto.getInhaber().equals(k))
                    .findFirst();
            kontoOptional.ifPresent(konto -> geldEinzahlen(konto.getKontonummer(), betrag));
        });

    }


}
