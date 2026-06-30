package bankprojekt.verwaltung;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Girokonto;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.basisdaten.Sparbuch;
import bankprojekt.exceptions.GesperrtException;
import bankprojekt.exceptions.UngueltigeKontonummerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BankTest {

    private Bank bank;

    //Mocks von Girokonten zum Überweisen
    @Mock
    private Girokonto mockGiroVon;
    @Mock
    private Girokonto mockGiroNach;
    //Sparbuch Mock für Tests von nicht überweisungsfähigen Konten
    @Mock
    private Sparbuch mockSparbuch;

    //Kontonummern die an die Mocks vergeben werden
    private long nrVon;
    private long nrNach;
    private long nrSparbuch;

    //nicht gemockt, da sie für die Funktionalität der Bank relativ unwichtig sind
    private Kunde kundeVon;
    private Kunde kundeNach;


    @BeforeEach
    void setUp() throws GesperrtException {

        bank = new Bank(12345L);
        MockitoAnnotations.openMocks(this);

        kundeVon = new Kunde("Amelie", "Dzierzawa", "Berliner Straße 1", LocalDate.of(2005, 12, 12));
        kundeNach = new Kunde("Jemand", "Irgendwer", "Berliner Chaussee 1", LocalDate.of(2000, 1, 1));

        //getInhaber() wird von Bank.geldUeberweisen() für den Verwendungszweck gebraucht
        when(mockGiroVon.getInhaber()).thenReturn(kundeVon);
        when(mockGiroNach.getInhaber()).thenReturn(kundeNach);

        //Konten sollten nicht gesperrt sein
        when(mockGiroVon.isGesperrt()).thenReturn(false);
        when(mockGiroNach.isGesperrt()).thenReturn(false);

        //Mocks in die Bank einfügen, Kontonummern werden automatisch vergeben
        nrVon = bank.kontoErstellen((inhaber, nummer) -> mockGiroVon, kundeVon);
        nrNach = bank.kontoErstellen((inhaber, nummer) -> mockGiroNach, kundeNach);
        nrSparbuch = bank.kontoErstellen((inhaber, nummer) -> mockSparbuch, kundeVon);

    }


    @Test
    void getKontostandKontonummerVorhandenLiefertKontostand() throws UngueltigeKontonummerException, GesperrtException {
        Geldbetrag erwartet = new Geldbetrag(250.0);
        when(mockGiroVon.getKontostand()).thenReturn(erwartet);
        Geldbetrag ergebnis = bank.getKontostand(nrVon);

        assertEquals(erwartet, ergebnis);
        verify(mockGiroVon,times(0)).abheben(ArgumentMatchers.any(Geldbetrag.class));
    }


    @Test
    void getKontostandGesperrtesKontoLiefertKontostand() throws UngueltigeKontonummerException {
        when(mockGiroVon.isGesperrt()).thenReturn(true);
        Geldbetrag erwartet = new Geldbetrag(100.0);
        when(mockGiroVon.getKontostand()).thenReturn(erwartet);

        //sollte keine GesperrtException werfen, weil man von gesperrten Konten trotzdem den Kontostand abfragen darf
        assertEquals(erwartet, bank.getKontostand(nrVon));
    }


    @Test
    void getKontostandKontonummerNichtVorhandenWirftException() throws UngueltigeKontonummerException {
        assertThrows(UngueltigeKontonummerException.class, () -> bank.getKontostand(9999L));
    }

    @Test
    void getKontostandUngueltigeKontonummerWirftException() {
        assertThrows(UngueltigeKontonummerException.class, () -> bank.getKontostand(-1L));
    }

    @Test
    void geldUeberweisenErfolgreichGibtTrueUndRuftEmpfangenAuf() throws UngueltigeKontonummerException, GesperrtException {
        Geldbetrag betrag = new Geldbetrag(100.0);
        when(mockGiroVon.ueberweisungAbsenden(
                eq(betrag),
                eq(kundeNach.getName()),
                eq(nrNach),
                eq(bank.getBankleitzahl()),
                eq("Miete"))
        ).thenReturn(true);

        boolean ergebnis = bank.geldUeberweisen(nrVon, nrNach, betrag, "Miete");

        assertTrue(ergebnis);

        //prüfen, das ueberweisungAbsenden mit korrekten Parametern aufgerufen wurde
        verify(mockGiroVon).ueberweisungAbsenden(
                eq(betrag),
                eq(kundeNach.getName()),
                eq(nrNach),
                eq(bank.getBankleitzahl()),
                eq("Miete"));

        //nach erfolgreicher Absendung muss der Empfänger die Überweisung empfangen haben
        verify(mockGiroNach).ueberweisungEmpfangen(
                eq(betrag),
                eq(kundeVon.getName()),
                eq(nrVon),
                eq(bank.getBankleitzahl()),
                eq("Miete"));

    }

    @Test
    void geldUeberweisenAbsendenSchlaegtFehlGibtFalseUndWirdNichtEmpfangen() throws UngueltigeKontonummerException, GesperrtException {
        Geldbetrag betrag = new Geldbetrag(1_000_000.00); //Dispo wird überschritten
        when(mockGiroVon.ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString())).thenReturn(false);

        boolean ergebnis = bank.geldUeberweisen(nrVon, nrNach, betrag, "Miete");

        assertFalse(ergebnis);

        //Geld sollte nie empfangen werden
        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }


    @Test
    void geldUeberweisenVonKontoGesperrtWirftGesperrtException() throws GesperrtException {
        when(mockGiroVon.isGesperrt()).thenReturn(true);
        when(mockGiroVon.ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString()))
                .thenThrow(new GesperrtException(nrVon));

        assertThrows(GesperrtException.class,
                () -> bank.geldUeberweisen(nrVon, nrNach, new Geldbetrag(100.0), "Miete" ));

        //Geld darf nie empfangen werden
        verify(mockGiroNach, never()).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
    }


    @Test
    void geldUeberweisenNachKontoGesperrtWirftKeineException() throws UngueltigeKontonummerException, GesperrtException {
        when(mockGiroNach.isGesperrt()).thenReturn(true);
        when(mockGiroVon.ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString())).thenReturn(true);

        //wenn Exception fliegt, schlägt der Test fehl
        bank.geldUeberweisen(nrVon, nrNach, new Geldbetrag(100.0), "Miete");
    }

    @Test
    void geldUeberweisenVonKontoNichtVorhandenWirftUngueltigeKontonummerException() {
        assertThrows(UngueltigeKontonummerException.class,
                () -> bank.geldUeberweisen(999L, nrNach, new Geldbetrag(100.0), "Miete"));

        //Überweisung soll nicht empfangen werden können
        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void geldUeberweisenNachKontoNichtVorhandenWirftUngueltigeKontonummerException() {
        assertThrows(UngueltigeKontonummerException.class,
                () -> bank.geldUeberweisen(nrVon, 999L, new Geldbetrag(100.0), "Miete"));

        //Überweisung soll nicht empfangen werden können
        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void geldUeberweisenVonKontoNichtUeberweisungsfaehigWirftIllegalArgumentException() {
        when(mockSparbuch.isGesperrt()).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> bank.geldUeberweisen(nrSparbuch, nrNach, new Geldbetrag(100.0), "Miete"));

        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void geldUeberweisenNachKontoNichtUeberweisungsfaehigWirftIllegalArgumentException() throws GesperrtException {
        assertThrows(IllegalArgumentException.class,
                () -> bank.geldUeberweisen(nrVon, nrSparbuch, new Geldbetrag(100.0), "Miete"));
    }

    @Test
    void geldUeberweisenAnSichSelbstWirdDurchgefuehrt() throws UngueltigeKontonummerException, GesperrtException {
        when(mockGiroVon.ueberweisungAbsenden(
                any(), anyString(), eq(nrVon), anyLong(), anyString())
        ).thenReturn(true);

        boolean ergebnis = bank.geldUeberweisen(nrVon, nrVon, new Geldbetrag(100.0), "Selbstüberweisung");
        assertTrue(ergebnis);
    }

    @Test
    void geldUeberweisenUngueltigeVonKontonummerWirftUngueltigeKontonummerException() {
        assertThrows(UngueltigeKontonummerException.class,
                () -> bank.geldUeberweisen(-1L, nrNach, new Geldbetrag(100.0), "Miete"));

        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void geldUeberweisenUngueltigeNachKontonummerWirftUngueltigeKontonummerException() {
        assertThrows(UngueltigeKontonummerException.class,
                () -> bank.geldUeberweisen(nrVon, -1L, new Geldbetrag(100.0), "Miete"));

        verify(mockGiroNach, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

}