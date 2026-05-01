package bankprojekt.basisdaten;

/**
 * Währungen die im Bankprojekt genutzt werden können.
 * @author Amelie Sophie Dzierzawa, 599428
 */
public enum Waehrung {

    /**
     * Währung: Euro, genutzt in Europa.
     */
    EURO("EUR",1.0),

    /**
     * Währung: Denar, genutzt in Nordmazedonien.
     */
    DENAR("MKD",61.5),

    /**
     * Währung: Franc, genutzt in den Komoren.
     */
    FRANC("KMF",491.96775),

    /**
     * Währung: Dobra, genutzt in Sâo Tomé und Príncipe.
     */
    DOBRA("STN",24.5);


    //Attribute
    private final String kuerzel;
    private final double  umrechnungskursZuEuro;

    //Konstruktor
    Waehrung(String kuerzel,double umrechnungskursZuEuro) {
        this.kuerzel = kuerzel;
        this.umrechnungskursZuEuro = umrechnungskursZuEuro;
    }

    /**
     * Getter für das Kürzel der jeweiligen Währung.
     * @return Kürzel der Währung als String
     */
    public String getKuerzel() {
        return kuerzel;
    }


    /**
     * Getter für den Umrechnungskurs zu Euro der jeweiligen Währung.
     * @return Umrechnungskurs zu Euro als double
     */
    public double getUmrechnungskursZuEuro() {
        return umrechnungskursZuEuro;
    }

    /**
     * Überschriebene toString Methode, die das Kürzel der jeweiligen Währung ausgibt.
     * @return das Kürzel der Wärhung in einem String
     */
    @Override
    public String toString() {
        return this.kuerzel;
    }

}
