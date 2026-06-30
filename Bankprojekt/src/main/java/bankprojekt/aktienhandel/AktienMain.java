package bankprojekt.aktienhandel;

import bankprojekt.basisdaten.Geldbetrag;

import java.util.concurrent.*;

/**
 * Hauptklasse zum Testen und Ausprobieren der
 * Funktionalität von den Klassen {@link Aktie} und {@link Aktienkonto}.
 * @author Amelie Dzierzawa, 599428
 */
public class AktienMain {
    /**
     * Erstellt einige Aktien und ein Aktienkonto,
     * führt mehrere Kauf- und Verkaufaufträge aus und gibt fortlaufend die Aktienkurse,
     * abgeschlossene Käufe und Verkäufe und den Kontostand des Aktienkontos an.
     * @param args wird nicht verwendet
     * @throws InterruptedException wenn der wartende Thread von außen unterbrochen wird
     * @throws ExecutionException wenn innerhalb eines Future Objekts eine Exception geworfen wird
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Aktie apple  = new Aktie("APPLE", new Geldbetrag(150.00));
        Aktie tesla  = new Aktie("TESLA", new Geldbetrag(200.00));
        Aktie nvidia = new Aktie("NVIDIA", new Geldbetrag(500.00));

        Aktienkonto aktienkonto = new Aktienkonto();
        aktienkonto.einzahlen(new Geldbetrag(5000.0));

        //Aktienkurse werden alle 3 Sekunden angezeigt
        ScheduledExecutorService anzeige = Executors.newSingleThreadScheduledExecutor();
        anzeige.scheduleAtFixedRate(() -> {
            System.out.println(System.lineSeparator() + "Aktuelle Aktienkurse:");
            System.out.printf("APPLE: %.2f%n", apple.getKurs().getBetrag());
            System.out.printf("TESLA: %.2f%n", tesla.getKurs().getBetrag());
            System.out.printf("NVIDIA: %.2f%n", nvidia.getKurs().getBetrag());
        }, 0, 3, TimeUnit.SECONDS);


        //mehrere Kaufaufträge
        CompletableFuture<Geldbetrag> kauf1 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("APPLE", 5, new Geldbetrag(152.00));
        CompletableFuture<Geldbetrag> kauf2 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("TESLA", 3, new Geldbetrag(202.00));
        CompletableFuture<Geldbetrag> kauf3 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("NVIDIA", 2, new Geldbetrag(505.00));

        //Wartet bis alle Käufe abgeschlossen sind
        CompletableFuture.allOf(kauf1, kauf2, kauf3).get();

        //Ergebnisse der Käufe ausgeben
        System.out.println(System.lineSeparator() + "Abgeschlossene Käufe:");
        System.out.printf("APPLE Kaufpreis: %.2f%n", kauf1.get().getBetrag());
        System.out.printf("TESLA Kaufpreis: %.2f%n", kauf2.get().getBetrag());
        System.out.printf("NVIDIA Kaufpreis: %.2f%n", kauf3.get().getBetrag());
        System.out.printf("Gesamtkontostand: %.2f%n", aktienkonto.getKontostand().getBetrag());

        //mehrere Verkaufaufträge
        CompletableFuture<Geldbetrag> verkauf1 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("APPLE", new Geldbetrag(148.00));
        CompletableFuture<Geldbetrag> verkauf2 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("TESLA", new Geldbetrag(198.00));
        CompletableFuture<Geldbetrag> verkauf3 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("NVIDIA", new Geldbetrag(495.00));

        CompletableFuture.allOf(verkauf1, verkauf2, verkauf3).get();

        //Ergebnisse wieder ausgeben lassen
        System.out.println(System.lineSeparator() + "Abgeschlossene Verkäufe:");
        System.out.printf("APPLE Erlös: %.2f%n", verkauf1.get().getBetrag());
        System.out.printf("TESLA Erlös: %.2f%n", verkauf2.get().getBetrag());
        System.out.printf("NVIDIA Erlös: %.2f%n", verkauf3.get().getBetrag());
        System.out.printf("Gesamtkontostand: %.2f%n", aktienkonto.getKontostand().getBetrag());


        //wieder neue Kaufaufträge
        CompletableFuture<Geldbetrag> kauf12 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("APPLE", 2, new Geldbetrag(149.00));
        CompletableFuture<Geldbetrag> kauf22 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("TESLA", 3, new Geldbetrag(208.00));
        CompletableFuture<Geldbetrag> kauf32 = (CompletableFuture<Geldbetrag>) aktienkonto.kaufauftrag("NVIDIA", 2, new Geldbetrag(510.00));

        CompletableFuture.allOf(kauf12, kauf22, kauf32).get();

        //ausgeben
        System.out.println(System.lineSeparator() + "Abgeschlossene Käufe:");
        System.out.printf("APPLE Kaufpreis: %.2f%n", kauf12.get().getBetrag());
        System.out.printf("TESLA Kaufpreis: %.2f%n", kauf22.get().getBetrag());
        System.out.printf("NVIDIA Kaufpreis: %.2f%n", kauf32.get().getBetrag());
        System.out.printf("Gesamtkontostand: %.2f%n", aktienkonto.getKontostand().getBetrag());

        //neue Verkaufaufträge
        CompletableFuture<Geldbetrag> verkauf12 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("APPLE", new Geldbetrag(148.00));
        CompletableFuture<Geldbetrag> verkauf22 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("TESLA", new Geldbetrag(198.00));
        CompletableFuture<Geldbetrag> verkauf32 = (CompletableFuture<Geldbetrag>) aktienkonto.verkaufauftrag("NVIDIA", new Geldbetrag(495.00));

        CompletableFuture.allOf(verkauf12, verkauf22, verkauf32).get();

        //ausgeben
        System.out.println(System.lineSeparator() + "Abgeschlossene Verkäufe:");
        System.out.printf("APPLE Erlös: %.2f%n", verkauf12.get().getBetrag());
        System.out.printf("TESLA Erlös: %.2f%n", verkauf22.get().getBetrag());
        System.out.printf("NVIDIA Erlös: %.2f%n", verkauf32.get().getBetrag());
        System.out.printf("Gesamtkontostand: %.2f%n", aktienkonto.getKontostand().getBetrag());


        //abschließend anzeige herunterfahren, also aufräumen
        anzeige.shutdown();
        //Aktien auch aufräumen
        apple.aktieShutdown();
        tesla.aktieShutdown();
        nvidia.aktieShutdown();
    }
}