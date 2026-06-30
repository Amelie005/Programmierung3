package bankprojekt.aktienhandel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class AktienkontoView implements PropertyChangeListener {

    private AktienkontoController controller;

    public AktienkontoView(AktienkontoController controller) {
        this.controller = controller;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("aktiendepot".equals(evt.getPropertyName())) {
            Map<Aktie, Integer> neuesDepot = (Map<Aktie, Integer>) evt.getNewValue();
            System.out.println("Das Depot hat sich geändert!");
            neuesDepot.forEach((aktie, anzahl) ->
                    System.out.println(aktie.getWkn() + ": " + anzahl + " Stück"));
        }
    }
}
