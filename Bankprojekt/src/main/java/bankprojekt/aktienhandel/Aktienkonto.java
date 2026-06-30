package bankprojekt.aktienhandel;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Konto;
import bankprojekt.exceptions.GesperrtException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Stellt ein Aktienkonto dar.
 * In einem Aktienkonto kann man Geld lagern, also auch abheben und einzahlen,
 * aber auch Aktien in einem Aktiendepot verwalten, also kaufen und verkaufen.
 * @author Amelie Dzierzawa, 599428
 */
public class Aktienkonto extends Konto {

    private Map<Aktie, Integer> aktiendepot = new ConcurrentHashMap<Aktie, Integer>();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);


    /**
     * Wartet asynchron, bis der Preis der Aktie mit der gewünschten Wertpapierkennnummer unter den angegebenen Höchstpreis gefallen ist.
     * Dann kauft sie anzahl Stück davon und speichert sie im Depot des Kontos.
     * Ist der Kontostand zu diesem Zeitpunkt allerdings
     * nicht ausreichend oder gibt es die Wertpapierkennnummer überhaupt nicht, wird
     * nichts gekauft.
     * @param wkn Wertpapierkennnummer der Aktie
     * @param anzahl Anzahl die an Aktien mit der wkn gekauft werden sollen
     * @param hoechstpreis Preis unter den die Aktie fallen soll damit sie gekauft wird
     * @return den ingesamt gezahlten Kaufpreis
     */
    public Future<Geldbetrag> kaufauftrag(String wkn, int anzahl, Geldbetrag hoechstpreis) {
        Aktie aktie = Aktie.getAktie(wkn);

        //wenn Aktie mit dieser wkn nicht existiert
        if (aktie == null || anzahl <= 0) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<Geldbetrag> future = new CompletableFuture<>();
        ScheduledExecutorService[] scheduler = new ScheduledExecutorService[1];
        scheduler[0] = Executors.newSingleThreadScheduledExecutor();

        scheduler[0].scheduleAtFixedRate(() -> {
            //prüfen, ob der Preis der Aktie(n) unter dem Höchstpreis liegt
            if (aktie.getKurs().compareTo(hoechstpreis) < 0) {
                Geldbetrag gesamtpreis = aktie.getKurs().mal(anzahl);

                //prüfen, ob der Kontostand ausreicht
                if (getKontostand().compareTo(gesamtpreis) >= 0) {

                    setKontostand(getKontostand().minus(gesamtpreis));

                    aktiendepot.merge(aktie, anzahl, Integer::sum); //wenn erfolgreich, Aktie(n) zum Depot zufügen
                    support.firePropertyChange("aktiendepot", null, aktiendepot); //observer "bescheid sagen"
                    future.complete(gesamtpreis);
                } else {
                    System.out.println("Das Konto ist nicht ausreichend gedeckt!"); //falls immer noch nicht erfolgreich, war Konto nicht gedeckt
                    future.complete(new Geldbetrag(0.0));
                }

                scheduler[0].shutdown(); //aufräumen
            }
        }, 0, 1, TimeUnit.SECONDS); //prüft jede Sekunde

        return future; //Gesamtpreis zurückgeben
    }

    /**
     * Wenn es keine Aktie mit der gewünschten Wertpapierkennnummer im Depot gibt,
     * findet kein Verkauf statt. Ansonsten wartet die Methode asynchron, bis der Kurs der Aktie den
     * gewünschten Minimalpreis erreicht hat und verkauft dann den kompletten Depotbestand dieser Aktie.
     * @param wkn Aktien mit dieser wkn die verkauft werden sollen
     * @param minimalpreis Preis für den die Aktien mindestens verkauft werden sollen
     * @return den Gesamterlös durch die verkauften Aktien oder 0, wenn die Aktie nicht im Depot vorhanden ist
     */
    public Future<Geldbetrag> verkaufauftrag(String wkn, Geldbetrag minimalpreis) {
        Aktie aktie = Aktie.getAktie(wkn);

        //falls Aktie nicht existiert oder nicht im Aktiendepot ist
        if (aktie == null || !aktiendepot.containsKey(aktie)) {
            return CompletableFuture.completedFuture(new Geldbetrag(0.0));
        }

        CompletableFuture<Geldbetrag> future = new CompletableFuture<>();
        ScheduledExecutorService[] scheduler = new ScheduledExecutorService[1];
        scheduler[0] = Executors.newSingleThreadScheduledExecutor();

        scheduler[0].scheduleAtFixedRate(() -> {
            if (aktie.getKurs().compareTo(minimalpreis) >= 0) { //wenn Minimalpreis erreicht/überschritten
                int anzahl = aktiendepot.get(aktie);
                Geldbetrag gesamterloes = aktie.getKurs().mal(anzahl); //Gesamterlös errechnen

                aktiendepot.remove(aktie); //Aktie aus Depot entfernen
                support.firePropertyChange("aktiendepot", null, aktiendepot); //observer wieder "bescheid sagen"
                einzahlen(gesamterloes); //Geld auf Konto einzahlen

                future.complete(gesamterloes);
                scheduler[0].shutdown(); //aufräumen
            }
        },0,1, TimeUnit.SECONDS); //prüft auch jede Sekunde
        return future;
    }

    @Override
    protected boolean pruefeDeckungUndRegeln(Geldbetrag betrag) {
        //abheben nur bis 0 möglich
        return !getKontostand().minus(betrag).isNegativ();
    }

    /**
     * Methode zum anmelden von Observer-Objekten.
     * @param pcl Observer der im PropertyChangeSupport registriert werden soll
     */
    public void anmelden(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }


}
