package bankprojekt.basisdaten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GirokontoTest {

    private Girokonto konto;
    private Kunde testKunde;

    @BeforeEach
    void setUp() {
        testKunde = new Kunde("Max", "Mustermann", "Musterstraße 1", LocalDate.of(2000, 1, 1));
        //Girokonto mit 500 Euro Dispo erstellen
        konto = new Girokonto(testKunde, 12345, new Geldbetrag(500, Waehrung.EURO));
    }

    @Test
    void testEinzahlenErhoehtKontostand() {
        //Ausgangszustand: 0 Euro
        Geldbetrag einzahlung = new Geldbetrag(100, Waehrung.EURO);
        konto.einzahlen(einzahlung);

        assertEquals(100.0, konto.getKontostand().getBetrag(), 0.001);
        assertEquals(Waehrung.EURO, konto.getKontostand().waehrung);
    }

    @Test
    void testEinzahlenInFremdwaehrung() {
        //Kontostand ist EURO (0,00)
        //61.5 MKD einzahlen (entspricht 1.00 EURO)
        Geldbetrag fremdgeld = new Geldbetrag(61.5, Waehrung.DENAR);

        konto.einzahlen(fremdgeld);

        //Kontostand muss danach 1.00 EURO sein, da das Konto in EURO geführt wird
        //und die plus-Methode in Geldbetrag automatisch umrechnet
        assertEquals(1.0, konto.getKontostand().getBetrag(), 0.001);
        assertEquals(Waehrung.EURO, konto.getKontostand().waehrung);
    }

    @Test
    void testEinzahlenNullWirftException() {
        assertThrows(IllegalArgumentException.class, () -> konto.einzahlen(null));
    }

    @Test
    void testEinzahlenNegativWirftException() {
        //ein Geldbetrag kann negativ sein
        //Die Methode einzahlen muss das abfangen
        Geldbetrag negativ = new Geldbetrag(-50, Waehrung.EURO);
        assertThrows(IllegalArgumentException.class, () -> konto.einzahlen(negativ));
    }
}