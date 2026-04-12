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

        //gestellter Kalender der immer einen anderen Monat zurückgibt
        Kalender kalenderNeuerMonat = new Kalender() {
            @Override
            public LocalDate getHeutigesDatum() {
                return LocalDate.of(2099, 6, 1);
            }
        };

        Sparbuch neuesSparbuch = new Sparbuch(testInhaber, 87654321L, kalenderNeuerMonat);
        neuesSparbuch.einzahlen(new Geldbetrag(5000));

        neuesSparbuch.abheben(new Geldbetrag(2000)); //erstes mal Abheben im "neuen" Monat
        boolean result = neuesSparbuch.abheben(new Geldbetrag(1)); //sollte fehlschlagen

        //Fehler: mit Original-Code wird false erwartet aber true zurückgegeben, weil bereitsAbgehoben nach der ersten Abhebung auf 0 zurückgesetzt wird
        assertFalse(result, "Nach 2000€ Abhebung darf kein weiterer Betrag abgehoben werden");
    }
}

