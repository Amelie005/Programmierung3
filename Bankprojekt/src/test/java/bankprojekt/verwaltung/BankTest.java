package bankprojekt.verwaltung;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Girokonto;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.basisdaten.Sparbuch;
import bankprojekt.exceptions.GesperrtException;
import bankprojekt.exceptions.UngueltigeKontonummerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void setUp() {

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
        nrVon = bank.mockEinfuegen(mockGiroVon);
        nrNach = bank.mockEinfuegen(mockGiroNach);
        nrSparbuch = bank.mockEinfuegen(mockSparbuch);
    }


    @Test
    void getKontostandKontonummerVorhandenLiefertKontostand() throws UngueltigeKontonummerException {
        Geldbetrag erwartet = new Geldbetrag(250.0);
        when(mockGiroVon.getKontostand()).thenReturn(erwartet);
        Geldbetrag ergebnis = bank.getKontostand(nrVon);

        assertEquals(erwartet, ergebnis);
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
        try {
            bank.getKontostand(9999L);
            fail("Es müsste eine UngültigeKontonummerException geworfen werden!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        }
    }

    @Test
    void getKontostandUngueltigeKontonummerWirftException() {
        try {
            bank.getKontostand(-1L);
            fail("Es müsste eine UngueltigeKontonummerException geworfen werden!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        }
    }

    @Test
    void geldUeberweisenErfolgreichGibtTrueUndRuftEmpfangenAuf() throws UngueltigeKontonummerException, GesperrtException {
        Geldbetrag betrag = new Geldbetrag(100.0);
        when(mockGiroVon.ueberweisungAbsenden(
                eq(betrag), anyString(), eq(nrNach), eq(bank.getBankleitzahl()), anyString())
        ).thenReturn(true);

        boolean ergebnis = bank.geldUeberweisen(nrVon, nrNach, betrag, "Miete");

        assertTrue(ergebnis);

        //nach erfolgreicher Absendung muss der Empfänger die Überweisung empfangen haben
        verify(mockGiroNach).ueberweisungEmpfangen(
                eq(betrag), anyString(), eq(nrVon), eq(bank.getBankleitzahl()), anyString());

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
    void geldUeberweisenVonKontoGesperrtWirftGesperrtException() {
        when(mockGiroVon.isGesperrt()).thenReturn(true);

        try {
            bank.geldUeberweisen(nrVon, nrNach, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine GesperrtException geworfen werden!");
        } catch (UngueltigeKontonummerException e) {
            fail("UngueltigeKontonummerExcpetion statt GesperrtException wurde geworfen!");
        } catch (GesperrtException e) {
            //Test bestanden
        }
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
        try {
            bank.geldUeberweisen(9999L, nrNach, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine UngueltigeKontonummerException geworfen werden!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        } catch (GesperrtException e) {
            fail("GesperrtException statt UngueltigeKontonummerException geworfen!");
        }
    }

    @Test
    void geldUeberweisenNachKontoNichtVorhandenWirftUngueltigeKontonummerException() {
        try {
            bank.geldUeberweisen(nrVon, 9999L, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine UngueltigeKontonummerException geworfen werden!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        } catch (GesperrtException e) {
            fail("GesperrtException statt UngueltigeKontonummerException geworfen!");
        }
    }

    @Test
    void geldUeberweisenVonKontoNichtUeberweisungsfaehigWirftIllegalArgumentException() {
        when(mockSparbuch.isGesperrt()).thenReturn(false);

        try {
            bank.geldUeberweisen(nrSparbuch, nrNach, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine IllegalArgumentException geworfen werden!");
        } catch (IllegalArgumentException e) {
            //Test bestanden
        } catch (UngueltigeKontonummerException | GesperrtException e) {
            fail(e.getClass().getSimpleName() + " statt IllegalArgumentException geworfen!");
        }
    }

    @Test
    void geldUeberweisenNachKontoNichtUeberweisungsfaehigWirftIllegalArgumentException() {
        try {
            bank.geldUeberweisen(nrVon, nrSparbuch, new Geldbetrag(100.0), "Miete");
            fail("Es müsste IllegalArgumentException geworfen werden!");
        } catch (IllegalArgumentException e) {
            //Test bestanden
        } catch (UngueltigeKontonummerException | GesperrtException e) {
            fail(e.getClass().getSimpleName() + " statt IllegalArgumentException geworfen!");
        }
    }

    @Test
    void geldUeberweisenAnSichSelbstWirdDruchgefuehrt() throws UngueltigeKontonummerException, GesperrtException {
        when(mockGiroVon.ueberweisungAbsenden(
                any(), anyString(), eq(nrVon), anyLong(), anyString())
        ).thenReturn(true);

        boolean ergebnis = bank.geldUeberweisen(nrVon, nrVon, new Geldbetrag(100.0), "Selbstüberweisung");
        assertTrue(ergebnis);
    }

    @Test
    void geldUeberweisenUngueltigeVonKontonummerWirftUngueltigeKontonummerException() {
        try {
            bank.geldUeberweisen(-1L, nrNach, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine UngueltigeKontonummerException geworfen!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        } catch (GesperrtException e) {
            fail("GesperrtException statt UngueltigeKontonummerException!");
        }
    }

    @Test
    void geldUeberweisenUngueltigeNachKontonummerWirftUngueltigeKontonummerException() {
        try {
            bank.geldUeberweisen(nrVon, -1L, new Geldbetrag(100.0), "Miete");
            fail("Es müsste eine UngueltigeKontonummerException geworfen!");
        } catch (UngueltigeKontonummerException e) {
            //Test bestanden
        } catch (GesperrtException e) {
            fail("GesperrtException statt UngueltigeKontonummerException!");
        }
    }

}