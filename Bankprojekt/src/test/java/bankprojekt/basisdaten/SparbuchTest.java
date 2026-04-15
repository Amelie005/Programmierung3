package bankprojekt.basisdaten;

import bankprojekt.nuetzliches.Kalender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SparbuchTest {

    private Sparbuch sparbuch;
    private Kunde testInhaber;

    @BeforeEach
    void setUp() {
        testInhaber = new Kunde("Max", "Mustermann", "Musterstr. 1", LocalDate.of(2000, 1, 1));
        sparbuch = new Sparbuch(testInhaber, 12345678L);
        sparbuch.einzahlen(new Geldbetrag(5000)); //Startguthaben für Tests
    }

    //Test 1: erfolgreiches abheben ohne Grenzfall
    @Test
    void testAbhebenErfolgreich() throws exceptions.GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(100));
        assertTrue(result, "Abheben erfolgreich.");
        assertEquals(new Geldbetrag(4900), sparbuch.getKontostand());
    }

    //Test 2: erfolgreiches abheben, aber mit Grenzfall (genau 2000€ abgehoben)
    @Test
    void testAbhebenUntereGrenze() throws exceptions.GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(2000));
        assertTrue(result);
        assertEquals(new Geldbetrag(3000), sparbuch.getKontostand());
    }

    //Test 3: Monatslimit von 2000€ überschritten -> muss false liefern
    @Test
    void testAbhebenMonatslimitUeberschritten() throws exceptions.GesperrtException {
        sparbuch.abheben(new Geldbetrag(2000));
        boolean result = sparbuch.abheben(new Geldbetrag(1));
        assertFalse(result);
    }

    //Test 4: Grenzfall, denn Kontostand würde unter 0.50€ fallen -> muss false liefern
    @Test
    void testAbhebenUnterMinimum() throws exceptions.GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(4999.51));
        assertFalse(result);
    }

    //Test 5: Fehlerfall; ein gesperrtes Konto muss eine GesperrtException werfen
    @Test
    void testAbhebenGesperrt() {
        sparbuch.sperren();
        assertThrows(exceptions.GesperrtException.class, () -> sparbuch.abheben(new Geldbetrag(100)));
    }

    //Test 6: Fehlerfall; null-Betrag muss IllegalArgumentException werfen
    @Test
    void testAbhebenNull() {
        assertThrows(IllegalArgumentException.class, () -> sparbuch.abheben(null));
    }

    //Test 7: Fehlerfall; negativer Betrag muss IllegalArgumentException werfen
    @Test
    void testAbhebenNegativ() {
        assertThrows(IllegalArgumentException.class, () -> sparbuch.abheben(new Geldbetrag(-50)));
    }

    //Dieser Test schlägt mit dem Original-Code fehl!!!
    //Test 8: Abhebung in einem neuen Monat muss korrekt gezählt werden
    @Test
    void testAbhebenNeuerMonatWirdGezaehlt() throws exceptions.GesperrtException {

        //erstellen eines veränderbaren Kalenders
        class VerstellbarerKalender extends Kalender {
            LocalDate datum = LocalDate.of(2099, 5, 1); // Wir starten im Mai
            @Override
            public LocalDate getHeutigesDatum() {
                return datum;
            }
        }
        VerstellbarerKalender meinKalender = new VerstellbarerKalender();

        //Sparbuch erstellen und füllen
        Sparbuch neuesSparbuch = new Sparbuch(testInhaber, 87654321L, meinKalender);
        neuesSparbuch.einzahlen(new Geldbetrag(5000));

        // Initialisierung: Einmal im Mai abheben, damit "zeitpunkt" im Objekt auf Mai steht
        neuesSparbuch.abheben(new Geldbetrag(10));

        //Jetzt stellen wir den Kalender auf Juni um
        meinKalender.datum = LocalDate.of(2099, 6, 1);

        //Erstes Abheben im JUNI (Maximum ausschöpfen)
        //ALTER CODE: Prüft Limit -> OK -> Setzt bereitsAbgehoben auf 2000 -> Reset auf 0 (weil neuer Monat)!
        //NEUER CODE: Reset auf 0 (wegen neuem Monat) -> Prüft Limit -> OK -> Setzt bereitsAbgehoben auf 2000.
        neuesSparbuch.abheben(new Geldbetrag(2000));

        //zweites Abheben im JUNI (darf nicht funktionieren!)
        boolean result = neuesSparbuch.abheben(new Geldbetrag(1));

        //Auswertung:
        // Der alte Code liefert hier TRUE (Fehler!), weil der Zähler oben resettet wurde
        // Der neue Code liefert hier FALSE (Korrekt!), weil der Zähler bei 2000 steht
        assertFalse(result, "Nachdem im neuen Monat bereits 2000€ abgehoben wurden, darf kein weiterer Euro abgehoben werden.");
    }
}

