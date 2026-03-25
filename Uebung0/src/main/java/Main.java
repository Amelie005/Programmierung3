import mathematik.Vektor;
import trigonometrie.Winkel;


public class Main {
    public static void main (String[] args) {

        //Vektor mit Konstruktor 1 erstellt
        Vektor v1 = new Vektor(3.0,4.0);
        //Vektor mit Konstruktor 2 erstellt
        Vektor v2 = new Vektor(5.0, new Winkel(90.0));

        //Ausgabe v1
        System.out.println("Vektor 1:");
        System.out.println("Kartesische Koordinaten: " + v1); //toString automatisch aufgerufen
        System.out.println("Polar: Betrag = " + v1.getBetrag() + ", Winkel = " + v1.getWinkel());

        System.out.println();

        //Ausgabe v2
        System.out.println("Vektor 2:");
        System.out.println("Kartesische Koordinaten: " + v2);
        System.out.println("Polar: Betrag = " + v2.getBetrag() + ", Winkel = " + v2.getWinkel());

        System.out.println();

        //Addition von v1 und v2
        System.out.println("Addition v1 + v2:");
        System.out.println("Vektor 1 + Vektor 2 = " + v1.addieren(v2));

        System.out.println();

        //Berechnung des orthogonalen Einheitsvektors von v1
        Vektor v3 = v1.orthogonalerEinheitsvektor();
        System.out.println("Orthogonaler Einheitsvektor von Vektor 1:");
        System.out.println("Vektor 3 = " + v3.toString());

        System.out.println();

        //Betrag von v3
        System.out.println("Betrag von Vektor 3:");
        System.out.println("Betrag = " + v3.getBetrag());

        System.out.println();

        //Skalarprodukt des Ausgangsvektors v1 mit v2
        System.out.println("Skalarprodukt von Vektor 1 mit Vektor 2:");
        System.out.println(("Skalarprodukt = " + v1.skalarprodukt(v2)));


    }
}
