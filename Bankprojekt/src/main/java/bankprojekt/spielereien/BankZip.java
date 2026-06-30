package bankprojekt.spielereien;

import bankprojekt.basisdaten.Geldbetrag;
import bankprojekt.basisdaten.Girokonto;
import bankprojekt.basisdaten.Konto;
import bankprojekt.basisdaten.Kunde;
import bankprojekt.fabriken.GirokontoFabrik;
import bankprojekt.fabriken.SparbuchFabrik;
import bankprojekt.verwaltung.Bank;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BankZip {
    public static void main(String[] args) throws IOException {

        int aktuellesJahr = LocalDate.now().getYear();

        Bank bank1 = new Bank(12345);

        Kunde opa = new Kunde("Opa", "Otto", "Altersheim", LocalDate.of(aktuellesJahr - 70, 3, 1));
        Kunde oma = new Kunde("Oma", "Emma", "Altersheim", LocalDate.of(aktuellesJahr - 67, 12, 5));
        Kunde kind = new Kunde("Kind", "Klein", "zuhause", LocalDate.of(aktuellesJahr - 8, 2, 28));
        Kunde teenager = new Kunde("Teenager", "Mittel", "zuhause", LocalDate.of(aktuellesJahr - 15, 6, 18));
        Kunde geradeErwachsen = new Kunde("Erwachsen", "Neu", "zuhause", LocalDate.of(aktuellesJahr - 18, 1, 1));

        bank1.kontoErstellen(new GirokontoFabrik(), opa);
        bank1.kontoErstellen(new GirokontoFabrik(), oma);
        bank1.kontoErstellen(new GirokontoFabrik(), teenager);
        bank1.kontoErstellen(new SparbuchFabrik(), geradeErwachsen);
        bank1.kontoErstellen(new GirokontoFabrik(), kind);


        Bank bank2 = new Bank(67890);
        Kunde nochNichtGanzErwachsen = new Kunde("Erwachsen", "Fast", "zuhause", LocalDate.of(aktuellesJahr - 18, 12, 31));
        Kunde mama = new Kunde("Mama", "Erna", "zuhause", LocalDate.of(aktuellesJahr - 42, 3, 5));
        Kunde papa = new Kunde("Papa", "Hugo", "zuhause", LocalDate.of(aktuellesJahr - 43, 7, 15));
        Kunde senior = new Kunde("Uropa", "Heinz", "Neben dem Friedhof", LocalDate.of(aktuellesJahr - 95, 2, 28));

        bank2.kontoErstellen(new GirokontoFabrik(), nochNichtGanzErwachsen);
        bank2.kontoErstellen(new GirokontoFabrik(), mama);
        bank2.kontoErstellen(new SparbuchFabrik(), papa);
        bank2.kontoErstellen(new GirokontoFabrik(), senior);

        System.out.println("Bank 1 und Bank 2 angelegt.");

        //zipOutputStream vorbereiten + Daten speichern
        //try with resources damit die Streams am Ende automatisch geschlossen werden
        try(FileOutputStream fos = new FileOutputStream("bankdatei.zip");
            ZipOutputStream zos = new ZipOutputStream(fos)) {

            //erstes Bankobjekt in zip anlegen
            ZipEntry eintrag1 = new ZipEntry("bank1.dat");
            zos.putNextEntry(eintrag1);

            //speichern, zos ist schon ein OutputStream, also kann man den direkt übergeben
            bank1.speichern(zos);

            //Eintrag wieder schließen
            zos.closeEntry();
            System.out.println("bank1.dat zu bankdatei.zip zugefügt!");

            //alles nochmal mit bank2
            ZipEntry eintrag2 =  new ZipEntry("bank2.dat");
            zos.putNextEntry(eintrag2);
            bank2.speichern(zos);
            zos.closeEntry();
            System.out.println("bank2.dat zu bankdatei.zip zugefügt!");

            System.out.println("Bankdatei.zip wurde vollständig erstellt!");
        } catch(IOException e) {
            System.err.println("Fehler beim Speichern der zip-Datei: " + e.getMessage());
            e.printStackTrace();
        }

        //zip-Datei einlesen und wieder ausgeben
        try(FileInputStream fis = new FileInputStream("bankdatei.zip");
            ZipInputStream zipIn = new ZipInputStream(fis)) {

            ZipEntry aktuellerEintrag;
            int bankZaehler = 1;

            //Schliefe läuft bis getNextEntry() einen Eintrag im zip findet
            while((aktuellerEintrag = zipIn.getNextEntry()) != null) {
                System.out.println();
                System.out.println("Aktueller Eintrag: " + aktuellerEintrag.getName());

                //Bankobjekt aus dem Stream laden
                Bank eingeleseneBank = Bank.einlesen(zipIn);

                //Übersicht zum überprüfen
                System.out.println("Eingelesene Bank: " + bankZaehler + " mit der BLZ: " + eingeleseneBank.getBankleitzahl());

                //alle Konten ausgeben, wieder um zu prüfen
                String konten = eingeleseneBank.getAlleKonten();

                if(konten != null && !konten.trim().isEmpty()) {
                    System.out.println(konten);
                } else {
                    System.out.println("Keine Konten vorhanden! Leere Bank.");
                }


                bankZaehler++;

                zipIn.closeEntry();
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der zip-Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }
}