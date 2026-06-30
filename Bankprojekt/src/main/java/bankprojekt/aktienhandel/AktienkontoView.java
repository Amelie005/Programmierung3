package bankprojekt.aktienhandel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class AktienkontoView implements PropertyChangeListener {

    private AktienkontoController controller;

    /**
     * Erstellt eine neue View für ein Aktienkonto, gesteuert durch den gegebenen Controller.
     * * @param controller der Controller, der die Kommunikation mit dem Aktienkonto verwaltet
     */
    public AktienkontoView(AktienkontoController controller) {
        this.controller = controller;
    }

    /**
     * Reagiert auf Änderungen am Aktienkonto.
     * Wird vom Aktienkonto aufgerufen, wenn sich das Aktiendepot verändert hat,
     * und gibt den neuen Stand des Depots auf der Konsole aus.
     * * @param evt das Ereignis, das die Änderung beschreibt
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("aktiendepot".equals(evt.getPropertyName())) {
            Map<Aktie, Integer> neuesDepot = (Map<Aktie, Integer>) evt.getNewValue();
            System.out.println("Das Depot hat sich geändert!");
            neuesDepot.forEach((aktie, anzahl) ->
                    System.out.println(aktie.getWkn() + ": " + anzahl + " Stück"));
        }
    }
}
