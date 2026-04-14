package Uhrzeit;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        //Erste Uhrzeit t1
        System.out.println("Bitte gebe eine beliebige Uhrzeit ein!");
        System.out.println("Stunden: ");
        int st1 = scanner.nextInt();
        System.out.println("Minuten: ");
        int mi1 = scanner.nextInt();
        System.out.println("Sekunden: ");
        int se1 = scanner.nextInt();
        System.out.println();

        //Zweite Uhrzeit t2
        System.out.println("Bitte gebe nochmal eine beliebige Uhrzeit ein!");
        System.out.println("Stunden: ");
        int st2 = scanner.nextInt();
        System.out.println("Minuten: ");
        int mi2 = scanner.nextInt();
        System.out.println("Sekunden: ");
        int se2 = scanner.nextInt();
        System.out.println();

        Zeit t1 = new Zeit(st1, mi1, se1);
        Zeit t2 = new Zeit(st2, mi2, se2);

        //Ausgabe t1
        System.out.println("Ausgabe der ersten Uhrzeit:");
        System.out.print("Deutsche Formatierung: ");
        t1.ausgebenDeutsch();
        System.out.println("Englische Formatierung: ");
        t1.ausgebenEnglish();
        System.out.println();

        //Ausgabe t2
        System.out.println("Ausgabe der zweiten Uhrzeit:");
        System.out.print("Deutsche Formatierung: ");
        t2.ausgebenDeutsch();
        System.out.println("Englische Formatierung: ");
        t2.ausgebenEnglish();
        System.out.println();

        //Differenz der Sekunden zwischen t1 und t2
        System.out.println("Differenz in Sekunden zwischen der ersten und der zweiten Uhrzeit: ");
        System.out.println(t1.differenz(t2));
        System.out.println();

        //Ausgabe der aktuellen Systemzeit
        Zeit aktuell = new Zeit();
        System.out.println("Aktuelle Systemzeit: ");
        System.out.print("Deutsche Formatierung: ");
        aktuell.ausgebenDeutsch();
        System.out.println("Englische Formatierung: ");
        aktuell.ausgebenEnglish();
        System.out.println();

        //Differenz zwischen den Sekunden der aktuellen Uhrzeit und t1
        System.out.println("Differenz in Sekunden zwischen der aktuellen Systemzeit und der ersten Uhrzeit:");
        System.out.println(aktuell.differenz(t1));
    }
}
