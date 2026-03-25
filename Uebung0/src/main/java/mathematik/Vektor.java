package mathematik;

import trigonometrie.Winkel;

/**
 * Klasse zur Speicherung eines Vektors im zweidimensionalen Raum.
 * @author Amelie Sophie Dzierzawa, Matrnr.: 599428 (HTW Berlin)
 */
public class Vektor {

    //Konstante, die den Nullvektor repräsentiert
    public static final Vektor NULLVEKTOR = new Vektor(0.0, 0.0);
    private final double x;
    private final double y;

    /**
     * Konstruktor 1: KArtesische Koordinaten
     * @param x x-Koordinate des Vektors
     * @param y y-Koordinate des Vektors
     */
    public Vektor(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) //prüft ob Zahlen unendlich sind
            throw new IllegalArgumentException("Koordinaten müssen endliche Zahlen sein");

        this.x = x;
        this.y = y;
    }

    /**
     * Konstruktor 2: Polare Koordinaten
     * @param betrag Länge des Vektors
     * @param winkel Winkel zwischen dem Vektor und der x-Achse
     */
    public Vektor(double betrag, Winkel winkel) {
        if (winkel == null) //prüft ob Winkel null ist
            throw new NullPointerException("Winkel darf nicht null sein");
        if (!Double.isFinite(betrag) || betrag < 0) //prüft ob Länge endlich und größer als 0 ist
            throw new IllegalArgumentException("Länge muss eine positive endliche Zahl sein");

        this.x = betrag * Math.cos(winkel.getWinkelImBogenmass());
        this.y = betrag * Math.sin(winkel.getWinkelImBogenmass());
    }

    /**
     * Getter für den Wert der x-Koordinate
     * @return x-Koordinate
     */
    public double getX() {return x;}

    /**
     * Getter für den Wert der y-Koordinate
     * @return y-Koordinate
     */
    public double getY() {return y;}

    /**
     * Getter für den Betrag
     * @return Betrag des Vektors
     */
    public double getBetrag() {
        return Math.sqrt(x*x+y*y);
    }

    /**
     * Getter für den Winkel des Vektors zur x-Achse
     * @return Winkel des Vektors zur x-Achse
     */
    public Winkel getWinkel() {
        return new Winkel(Math.toDegrees(Math.atan2(y,x)));
    }

    /**
     * Addiert aktuellen Vekto mit neuem Vektor
     * @param v neuer Vektor v
     * @return Vektor als Ergebnis der Addition
     */
    public Vektor addieren(Vektor v) {
        if (v == null) //prüft ob Summand null ist
            throw new NullPointerException("Summand darf nicht null sein");
        return new Vektor(this.x + v.x, this.y + v.y);
    }

    /**
     * Berechnet das Skalarprodukt vom aktuellen Vektor und einem neuen Vektor v
     * @param v neuer Vektor v
     * @return Ergebnis der Rechnung als double
     */
    public double skalarprodukt(Vektor v) {
        if (v == null) //prüft ob Vektor null ist
            throw new NullPointerException("Vektor darf nicht null sein");
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Berechnet einen Vektor der Länge 1, der orthogonal zum aktuellen Vektor ist
     * @return zum aktuellen Vektor orthogonalen Vektor
     */
    public Vektor orthogonalerEinheitsvektor() {
        double betrag = this.getBetrag();
        if (betrag == 0.0) //prüft ob Länge 0 ist
            throw new ArithmeticException("Nullvektor hat keinen orthogonalen Einheitsvektor!");

        return new Vektor(-this.y / betrag, this.x / betrag);
    }

    /**
     * Liefert eine lesbare String-Darstellung des Vektors
     * @return Vektor als String-Darstellung
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y); //% - Beginn eines Platzhalter
                                                    //.2 - zwei Nachkommastellen
                                                    //f - Zahl als float
    }

}

    /*
    Setter machen meiner Meinung nach keinen Sinn, denn ein Vektor repräsentiert als Wertobjekt einen
    unveränderlichen Wert.
    Es macht keinen Sinn die Werte eines Vektors zu verändern, stattdessen sollte man
    lieber einen neuen Vektor initiieren. Die Klasse Winkel folgt demselben Prinzip, denn es gibt
    Sicherheit aber auch mehr Übersichtlichkeit. Deshalb sind die Variablen x,y auch konstant.
     */