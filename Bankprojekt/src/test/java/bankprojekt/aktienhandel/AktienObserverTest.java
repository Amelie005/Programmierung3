package bankprojekt.aktienhandel;

import bankprojekt.basisdaten.Geldbetrag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import static org.mockito.Mockito.*;

public class AktienObserverTest {

    private Aktienkonto aktienkonto;
    private PropertyChangeListener mockObserver;
    private final String TEST_WKN = "TEST";

    @BeforeEach
    void setup() {
        aktienkonto = new Aktienkonto();
        mockObserver = mock(PropertyChangeListener.class); //Mock für den Observer
        aktienkonto.anmelden(mockObserver); //Observer anmelden

        //prüfen ob Aktie schon existiert, sonst erstellen
        if (Aktie.getAktie(TEST_WKN) == null) {
            new Aktie(TEST_WKN, new Geldbetrag(50.0));
        }
    }

    @AfterEach
    void tearDown() {
        aktienkonto.abmelden(mockObserver);
    }

    @Test
    void observerWirdBeiKaufBenachrichtigt() throws InterruptedException {
        aktienkonto.einzahlen(new Geldbetrag(1000.0));

        aktienkonto.kaufauftrag(TEST_WKN, 1, new Geldbetrag(100.0));
        Thread.sleep(1500); //kurz für ExecutorService warten
        //verifizieren, dass propertyChange aufgerufen wurde
        verify(mockObserver, atLeastOnce()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void observerWirdBeiVerkaufBenachrichtigt()  throws InterruptedException {
        Aktie testAktie = Aktie.getAktie(TEST_WKN);
        aktienkonto.einzahlen(new Geldbetrag(1000.0));

        aktienkonto.kaufauftrag(TEST_WKN, 1, testAktie.getKurs().plus(new Geldbetrag(10.0)));
        Thread.sleep(2000);

        clearInvocations(mockObserver); //vorherigen AUfruf ignorieren
        Geldbetrag minimalpreis = testAktie.getKurs().minus(new Geldbetrag(1.0));
        aktienkonto.verkaufauftrag(TEST_WKN, minimalpreis);

        Thread.sleep(2000);

        verify(mockObserver, atLeastOnce()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void observerWirdBeiVerkaufEinerNichtVorhandenenAktieNichtBenachrichtigt() throws InterruptedException {
        aktienkonto.verkaufauftrag(TEST_WKN, new Geldbetrag(1.0));
        Thread.sleep(1500);
        verify(mockObserver, never()).propertyChange(any(PropertyChangeEvent.class));
    }
}
