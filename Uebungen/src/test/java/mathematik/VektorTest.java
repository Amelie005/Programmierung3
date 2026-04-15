package mathematik;

import org.junit.jupiter.api.Test;
import trigonometrie.Winkel;

import static org.junit.jupiter.api.Assertions.*;

class VektorTest {

    //Konstruktor 1

    @Test
    void konstruktor_normfall_positiveKoordinaten() {
        Vektor v = new Vektor(3.0, 4.0);
        assertEquals(3.0, v.getX());
        assertEquals(4.0, v.getY());
    }

    @Test
    void konstruktor_grenzfall_nullvektor() {
        Vektor v = new Vektor(0.0, 0.0);
        assertEquals(0.0, v.getX());
        assertEquals(0.0, v.getY());
    }

    @Test
    void konstruktor_grenzfall_negativKoordinaten() {
        Vektor v = new Vektor(-3.0, -4.0);
        assertEquals(-3.0, v.getX());
        assertEquals(-4.0, v.getY());
    }

    @Test
    void konstruktor_fehlerfall_NaN() {
        assertThrows(IllegalArgumentException.class, () -> new Vektor(Double.NaN, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new Vektor(1.0, Double.NaN));
    }

    @Test
    void konstruktor_fehlerfall_unendlich() {
        assertThrows(IllegalArgumentException.class, () -> new Vektor(Double.POSITIVE_INFINITY, 1.0));
        assertThrows(IllegalArgumentException.class, () -> new Vektor(1.0, Double.NEGATIVE_INFINITY));
    }

    //Konstruktor 2

    @Test
    void konstruktor2_normfall_90Grad() {
        Vektor v = new Vektor(5.0, new Winkel(90.0));
        assertEquals(0.0, v.getX(), 1e-9);
        assertEquals(5.0, v.getY(), 1e-9);
    }

    @Test
    void konstruktor2_grenzfall_0Grad() {
        Vektor v = new Vektor(5.0, new Winkel(0.0));
        assertEquals(5.0, v.getX(), 1e-9);
        assertEquals(0.0, v.getY(), 1e-9);
    }

    @Test
    void konstruktor2_fehlerfall_winkelNull() {
        assertThrows(NullPointerException.class, () -> new Vektor(5.0, null));
    }

    @Test
    void konstruktor2_fehlerfall_negativeLaenge() {
        assertThrows(IllegalArgumentException.class, () -> new Vektor(-5.0, new Winkel(90.0)));
    }

    // getLaenge()

    @Test
    void getLaenge_normfall_pythagoras() {
        Vektor v = new Vektor(3.0, 4.0);
        assertEquals(5.0, v.getBetrag(), 1e-9);
    }

    @Test
    void getLaenge_grenzfall_nullvektor() {
        assertEquals(0.0, Vektor.NULLVEKTOR.getBetrag(), 1e-9);
    }

    //addieren()

    @Test
    void addieren_normfall() {
        Vektor v1 = new Vektor(1.0, 2.0);
        Vektor v2 = new Vektor(3.0, 4.0);
        Vektor ergebnis = v1.addieren(v2);
        assertEquals(4.0, ergebnis.getX(), 1e-9);
        assertEquals(6.0, ergebnis.getY(), 1e-9);
    }

    @Test
    void addieren_grenzfall_mitNullvektor() {
        Vektor v = new Vektor(3.0, 4.0);
        Vektor ergebnis = v.addieren(Vektor.NULLVEKTOR);
        assertEquals(v.getX(), ergebnis.getX(), 1e-9);
        assertEquals(v.getY(), ergebnis.getY(), 1e-9);
    }

    @Test
    void addieren_fehlerfall_null() {
        Vektor v = new Vektor(1.0, 2.0);
        assertThrows(NullPointerException.class, () -> v.addieren(null));
    }

    //skalarprodukt()

    @Test
    void skalarprodukt_normfall() {
        Vektor v1 = new Vektor(3.0, 4.0);
        Vektor v2 = new Vektor(1.0, 2.0);
        assertEquals(11.0, v1.skalarprodukt(v2), 1e-9); // 3*1 + 4*2
    }

    @Test
    void skalarprodukt_grenzfall_orthogonaleVektoren() {
        Vektor v1 = new Vektor(1.0, 0.0);
        Vektor v2 = new Vektor(0.0, 1.0);
        assertEquals(0.0, v1.skalarprodukt(v2), 1e-9); // orthogonal → 0
    }

    @Test
    void skalarprodukt_fehlerfall_null() {
        Vektor v = new Vektor(1.0, 2.0);
        assertThrows(NullPointerException.class, () -> v.skalarprodukt(null));
    }

    //orthogonalerEinheitsvektor()

    @Test
    void einheitsvektor_normfall_laengeIst1() {
        Vektor v = new Vektor(3.0, 4.0);
        assertEquals(1.0, v.orthogonalerEinheitsvektor().getBetrag(), 1e-9);
    }

    @Test
    void einheitsvektor_normfall_istOrtogonal() {
        Vektor v = new Vektor(3.0, 4.0);
        // Skalarprodukt mit Original muss 0 sein
        assertEquals(0.0, v.skalarprodukt(v.orthogonalerEinheitsvektor()), 1e-9);
    }

    @Test
    void einheitsvektor_fehlerfall_nullvektor() {
        assertThrows(ArithmeticException.class,
                () -> Vektor.NULLVEKTOR.orthogonalerEinheitsvektor());
    }

}