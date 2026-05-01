package bankprojekt.basisdaten;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GeldbetragTest {

    //Hilfsattribut aufgrund von Rundungsfehlern bei double-Rechnungen
    private static final double DELTA = 0.001;

    @Test
    void testUmrechnenEuroInFremd() {
        //1 Euro = 61.5 MKD laut deiner Enum
        Geldbetrag euro = new Geldbetrag(1, Waehrung.EURO);
        Geldbetrag inDenar = euro.umrechnen(Waehrung.DENAR);

        assertEquals(61.5, inDenar.getBetrag(), DELTA);
        assertEquals(Waehrung.DENAR, inDenar.waehrung);
    }

    @Test
    void testUmrechnenFremdInFremd() {
        //61.5 MKD -> 1 Euro -> 24.5 STN
        Geldbetrag denar = new Geldbetrag(61.5, Waehrung.DENAR);
        Geldbetrag inDobra = denar.umrechnen(Waehrung.DOBRA);

        assertEquals(24.5, inDobra.getBetrag(), DELTA);
        assertEquals(Waehrung.DOBRA, inDobra.waehrung);
    }

    @Test
    void testPlusBeachtetWaehrung() {
        //1 Euro + 61.5 MKD (entspricht 1 Euro) = 2 Euro
        Geldbetrag euro = new Geldbetrag(1, Waehrung.EURO);
        Geldbetrag denar = new Geldbetrag(61.5, Waehrung.DENAR);

        Geldbetrag ergebnis = euro.plus(denar);

        assertEquals(2.0, ergebnis.getBetrag(), DELTA);
        assertEquals(Waehrung.EURO, ergebnis.waehrung);
    }

    @Test
    void testMinusBeachtetWaehrung() {
        //100 MKD - 1.0 Euro (entspricht 61.5 MKD) = 38.5 MKD
        Geldbetrag denar = new Geldbetrag(100, Waehrung.DENAR);
        Geldbetrag euro = new Geldbetrag(1, Waehrung.EURO);

        Geldbetrag ergebnis = denar.minus(euro);

        assertEquals(38.5, ergebnis.getBetrag(), DELTA);
        assertEquals(Waehrung.DENAR, ergebnis.waehrung);
    }

    @Test
    void testCompareToBeachtetWaehrung() {
        Geldbetrag einEuro = new Geldbetrag(1, Waehrung.EURO);
        Geldbetrag vielDenar = new Geldbetrag(100, Waehrung.DENAR); //entspricht ca. 1.62 Euro

        assertTrue(einEuro.compareTo(vielDenar) < 0);
        assertTrue(vielDenar.compareTo(einEuro) > 0);
    }

    @Test
    void testEqualsUndHashCode() {
        Geldbetrag g1 = new Geldbetrag(10, Waehrung.EURO);
        Geldbetrag g2 = new Geldbetrag(10, Waehrung.EURO);
        Geldbetrag g3 = new Geldbetrag(10, Waehrung.DENAR);

        assertEquals(g1, g2);
        assertNotEquals(g1, g3);
        assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    void testMalBeachtetWaehrung() {
        Geldbetrag dobra = new Geldbetrag(10, Waehrung.DOBRA);
        Geldbetrag ergebnis = dobra.mal(2.5);

        assertEquals(25.0, ergebnis.getBetrag(), DELTA);
        assertEquals(Waehrung.DOBRA, ergebnis.waehrung);
    }

    @Test
    void testIsNegativ() {
        assertTrue(new Geldbetrag(-0.01).isNegativ());
        assertFalse(new Geldbetrag(0).isNegativ());
    }

    @Test
    void testKonstruktorExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new Geldbetrag(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new Geldbetrag(10, null));
    }
}