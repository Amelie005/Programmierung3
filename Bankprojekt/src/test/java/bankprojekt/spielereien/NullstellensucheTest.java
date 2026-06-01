package bankprojekt.spielereien;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class NullstellensucheTest {

    private final double BREITE = 0.01;

    @Test
    void funktionF() {
        //f(x) = x^2 -25: Nullstellen sollten bei -5, 5 liegen
        DoubleUnaryOperator f = x -> Math.pow(x,2) - 25;

        //positiver Bereich
        double ergebnisPositiv = Nullstellensuche.findeNullstelleInIntervall(f, 0, 6);
        assertEquals(5.0, ergebnisPositiv, BREITE);

        //negativer Bereich
        double ergebnisNegativ = Nullstellensuche.findeNullstelleInIntervall(f, 0, -6);
        assertEquals(-5.0, ergebnisNegativ, BREITE);
    }

    @Test
    void funktionG() {
        //g(x) = e^(3x) - 7: Nullstelle bei c.a 0.65
        DoubleUnaryOperator g = x -> Math.exp(3 * x) - 7;
        double erwartet = Math.log(7) / 3.0;

        double ergebnis = Nullstellensuche.findeNullstelleInIntervall(g,0, 2);
        assertEquals(erwartet, ergebnis, BREITE);
    }

    @Test
    void funktionH() {
        //h(x) = sin(x^2) - 0.5: Nullstelle bei ca. 0.72
        DoubleUnaryOperator h = x -> Math.sin(Math.pow(x,2)) - 0.5;
        double erwartet = Math.sqrt(Math.PI / 6.0);

        double ergebnis = Nullstellensuche.findeNullstelleInIntervall(h,0, 1.5);
        assertEquals(erwartet, ergebnis, BREITE);
    }

    @Test
    void funktionKWirftIllegalArgumentExcpetion() {
        //k(x) = x^2 + 1: hat keine Nullstellen, da immer positiv
        DoubleUnaryOperator k = x -> Math.pow(x,2) + 1;

        //muss IllegalArgumentException werfen, da keim Vorzeichenwechsel stattfindet
        assertThrows(IllegalArgumentException.class, () -> {
            Nullstellensuche.findeNullstelleInIntervall(k, -2, 2);
        }, "Test schlägt fehl, wenn doch eine Nullstelle gefunden wurde (hier falsch).");
    }

}