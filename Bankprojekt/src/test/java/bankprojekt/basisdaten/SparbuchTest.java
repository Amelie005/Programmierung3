package bankprojekt.basisdaten;

import bankprojekt.exceptions.GesperrtException;
import bankprojekt.nuetzliches.Kalender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SparbuchTest {

    private Sparbuch sparbuch;
    private Kunde testInhaber;

    @Mock
    private Kalender kalender;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this); //Mocks zuerst, sonst ist Kalender noch null, wenn Konstruktor ihn braucht

        testInhaber = new Kunde("Max", "Mustermann", "Musterstr. 1", LocalDate.of(2000, 1, 1));

        //Kalender gibt standardmäßig festes Datum zurück
        when(kalender.getHeutigesDatum()).thenReturn(LocalDate.of(2024, 1, 1));

        //Sparbuch mit gemocktem Kalender erstellen
        sparbuch = new Sparbuch(testInhaber, 12345678L, kalender);
        sparbuch.einzahlen(new Geldbetrag(5000)); //Startguthaben für Tests

    }

    @Test
    void abhebenErfolgreich() throws GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(100));
        assertTrue(result, "Abheben erfolgreich.");
        assertEquals(new Geldbetrag(4900), sparbuch.getKontostand());
    }

    @Test
    void abhebenUntereGrenzeErreicht() throws GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(2000));
        assertTrue(result);
        assertEquals(new Geldbetrag(3000), sparbuch.getKontostand());
    }

    @Test
    void abhebenMonatslimitWurdeUeberschrittenFalseZurueckliefern() throws GesperrtException {
        sparbuch.abheben(new Geldbetrag(2000));
        boolean result = sparbuch.abheben(new Geldbetrag(1));
        assertFalse(result);
    }

    @Test
    void abhebenUnterMinimumGefallenFalseZurueckliefern() throws GesperrtException {
        boolean result = sparbuch.abheben(new Geldbetrag(4999.51));
        assertFalse(result);
    }

    @Test
    void abhebenKontoGesperrtWirftGesperrtException() {
        sparbuch.sperren();
        assertThrows(GesperrtException.class, () -> sparbuch.abheben(new Geldbetrag(100)));
    }

    @Test
    void abhebenVonNullBetragWirftIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> sparbuch.abheben(null));
    }

    @Test
    void abhebenBetragIstNegativWirftIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> sparbuch.abheben(new Geldbetrag(-50)));
    }

    //Dieser Test schlägt mit dem Original-Code fehl!!!
    @Test
    void abhebenNeuerMonatWirdGezaehlt() throws GesperrtException {
        //Abhebung damit Monat auf Januar gesetzt wird
        sparbuch.abheben(new Geldbetrag(100));

        //Kalender auf Februar umstellen
        when(kalender.getHeutigesDatum()).thenReturn(LocalDate.of(2024, 2, 1));

        //Erste Abhebung im Februar sollte funktionieren
        boolean februarErste = sparbuch.abheben(new Geldbetrag(2000));
        assertTrue(februarErste, "Erste Abhebung im Februar sollte funktionieren.");

        //Zweite Abhebung im Februar darf nicht mehr funktionieren, da
        //Monatslimit bereits erreicht wurde
        boolean februarZweite = sparbuch.abheben(new Geldbetrag(1));
        assertFalse(februarZweite, "Monatslimit im Februar wurde bereits erreicht!");
    }

    @Test
    void abhebenJahresWechselSetztLimitZurueck() throws GesperrtException {
        when(kalender.getHeutigesDatum()).thenReturn(LocalDate.of(2024, 12, 1));

        //Limit im Dezember ausnutzen
        sparbuch.abheben(new Geldbetrag(2000));

        //auf Januar umstellen
        when(kalender.getHeutigesDatum()).thenReturn(LocalDate.of(2025, 1, 1));

        //Ersze Abhebung im neuen Jahr sollte funktionieren
        boolean result = sparbuch.abheben(new Geldbetrag(2000));
        assertTrue(result, "Nach Jahreswechsel muss Monatslimit zurückgesetzt sein!");
    }
}

