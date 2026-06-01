package bankprojekt.spielereien;


import java.util.function.DoubleUnaryOperator;

/**
 * Klasse zum Finden einer Nullstelle.
 *
 * @author Amelie Dzierzawa, 599428
 */
public class Nullstellensuche {

    /**
     * Sucht eine Nullstelle im Intervall [a, b].
     *
     * @param f mathematische Funktion als Lambda-Ausdruck
     * @param a linke Intervallgrenze
     * @param b rechte Intervallgrenze
     * @return Nullstelle
     * @throws IllegalArgumentException wenn das Intervall ungültig ist oder kein Vorzeichenwechsel stattfindet
     */
    public static double findeNullstelleInIntervall(DoubleUnaryOperator f, double a, double b) { //DoubelUnaryOperator spart eigenes Interface und Autoboxing (also auch Performance)
        //Sortiert die Intervallgrenzen, falls a > b übergeben wurde
        if (a > b) {
            double temp = a;
            a = b;
            b = temp;
        }

        double fa = f.applyAsDouble(a);
        double fb = f.applyAsDouble(b);

        //Überprüfung, ob eine der Grenzen bereits die Nullstelle ist
        if (fa == 0.0) return a;
        if (fb == 0.0) return b;

        //Fall 2: kein Vorzeichenwechsel, also keine Garantie für eine Nullstelle
        if (fa * fb > 0) {
            throw new IllegalArgumentException("Kein Vorzeichenwechsel im Intervall [" + a + ", " + b + "]. " +
                    "Es kann keine Nullstelle garantiert werden.");
        }

        //Intervallbreite
        //Abbruch, sobald die Intervallgröße kleiner als die Toleranz ist
        double toleranz = 0.01;
        double m = a;

        while ((b - a) >= toleranz) {
            //Mitte m des Intervalls berechnen
            m = a + (b - a) / 2.0;
            double fm = f.applyAsDouble(m);

            //wenn NMitte die Nullstelle ist, abbrechen
            if (fm == 0.0) {
                return m;
            }

            //Intervall auf Teilintervall mit Vorzeichenwechsel einschränken
            if (fa * fm < 0) {
                //Vorzeichenwechsel in linker Hälfte
                b = m;
                fb = fm;
            } else {
                //Vorzeichenwechsel in rechter Hälfte
                a = m;
                fa = fm;
            }
        }

        //Mitte des verbleibenden Intervalls
        return a + (b - a) / 2.0;
    }

}
