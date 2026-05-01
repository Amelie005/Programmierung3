package bankprojekt.basisdaten;

import exceptions.GesperrtException;
import exceptions.UngueltigeKontonummerException;

/**
 * Ein Girokonto, d.h. ein Konto mit einem Dispo und der Fähigkeit,
 * Überweisungen zu senden und zu empfangen.
 * Grundsätzlich sind Überweisungen und Abhebungen möglich bis
 * zu einem Kontostand von -this.dispo
 * @author Doro
 *
 */
public class Girokonto extends UeberweisungsfaehigesKonto{
	/**
	 * Wert, bis zu dem das Konto überzogen werden darf
	 */
	private Geldbetrag dispo;
	
	/**
	 * erzeugt ein Girokonto mit den angegebenen Werten
	 * @param inhaber Kontoinhaber
	 * @param kontonummer Kontonummer
	 * @param dispo Dispo
	 * @throws IllegalArgumentException wenn der inhaber null ist 
	 * 				    oder der angegebene dispo negativ bzw. NaN ist 
	 * @throws UngueltigeKontonummerException wenn kontonummer ungültig ist
	 */
	public Girokonto(Kunde inhaber, long kontonummer, Geldbetrag dispo)
	{
		super(inhaber, kontonummer);
		if(dispo == null || dispo.isNegativ())
			throw new IllegalArgumentException("Der Dispo ist nicht gültig!");
		this.dispo = dispo;
	}
	
	/**
	 * erzeugt ein leeres, nicht gesperrtes Standard-Girokonto
	 * von Max MUSTERMANN
	 */
	public Girokonto()
	{
		this(Kunde.MUSTERMANN, 99887766, new Geldbetrag(500));
	}
	
	/**
	 * liefert den Dispo
	 * @return Dispo von this
	 */
	public Geldbetrag getDispo() {
		return dispo;
	}

	/**
	 * setzt den Dispo neu
	 * @param dispo muss größer sein als 0
	 * @throws IllegalArgumentException wenn dispo negativ bzw. NaN ist
	 */
	public void setDispo(Geldbetrag dispo) {
		if(dispo == null || dispo.isNegativ())
			throw new IllegalArgumentException("Der Dispo ist nicht gültig!");
		this.dispo = dispo;
	}
	
	@Override
    public boolean ueberweisungAbsenden(Geldbetrag betrag, 
    		String empfaenger, long nachKontonr, 
    		long nachBlz, String verwendungszweck) 
    				throws GesperrtException 
    {
      if (this.isGesperrt())
            throw new GesperrtException(this.getKontonummer());
        if (betrag == null || betrag.isNegativ()|| empfaenger == null || verwendungszweck == null)
            throw new IllegalArgumentException("Parameter fehlerhaft");
        if (!getKontostand().plus(dispo).minus(betrag).isNegativ())
        {
            setKontostand(getKontostand().minus(betrag));
            return true;
        }
        else
        	return false;
    }
	
	@Override
	public boolean abheben(Geldbetrag betrag) throws GesperrtException{
		if (betrag == null || betrag.isNegativ()) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if(this.isGesperrt())
			throw new GesperrtException(this.getKontonummer());
		if (!getKontostand().plus(dispo).minus(betrag).isNegativ())
		{
			setKontostand(getKontostand().minus(betrag));
			return true;
		}
		else
			return false;
	}

    @Override
    public void ueberweisungEmpfangen(Geldbetrag betrag, String vonName, long vonKontonr, long vonBlz, String verwendungszweck)
    {
        if (betrag == null || betrag.isNegativ()|| vonName == null || verwendungszweck == null)
            throw new IllegalArgumentException("Parameter fehlerhaft");
        setKontostand(getKontostand().plus(betrag));
    }
    
    @Override
    public String toString()
    {
    	String ausgabe = "-- GIROKONTO --" + System.lineSeparator() +
    	super.toString() + System.lineSeparator()
    	+ "Dispo: " + this.dispo;
    	return ausgabe;
    }

	/**
	 * Wechselt die Währung, in der das Konto geführt wird.
	 * Dabei wird der Kontostand und auch der Dispo in die neue Währung umgerechnet.
	 * @param neu die neue Zielwährung
	 * @throws IllegalArgumentException  wenn die Währung null ist
	 */
	@Override
	public void waehrungswechsel(Waehrung neu) {
		super.waehrungswechsel(neu); //Rechnet den Kontostand um
		this.dispo = this.dispo.umrechnen(neu); //Rechnet den Dispo um
	}

	/**
	 * {@inheritDoc}
	 * @throws IllegalArgumentException wenn der Betrag null oder negativ ist
	 */
	@Override
	public void einzahlen(Geldbetrag betrag) {
		super.einzahlen(betrag);
	}
}
