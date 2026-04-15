package trigonometrie;

/**
 * Stellt einen trigonometrie.Winkel dar, der immer zwischen 0° (inklusive)
 * und 360° (exklusive) liegt
 */
public final class Winkel {
	private double winkelInGrad;
	
	/**
	 * erstellt einen trigonometrie.Winkel zwischen 0° (inklusive) und 360°
	 * @param winkelInGrad der trigonometrie.Winkel in Grad
	 * @throws IllegalArgumentException wenn winkelInGrad keine Zahl ist
	 */
	public Winkel(double winkelInGrad) {
		if(!Double.isFinite(winkelInGrad))
			throw new IllegalArgumentException();
		double winkel = winkelInGrad % 360;
		if (winkel < 0)
			winkel = 360 + winkel;
		this.winkelInGrad = winkel;
	}

	/**
	 * trigonometrie.Winkel in Grad
	 * @return trigonometrie.Winkel in Grad
	 */
	public double getWinkelInGrad() {
		return winkelInGrad;
	}
	
	/**
	 * trigonometrie.Winkel im Bogenmaß
	 * @return trigonometrie.Winkel im Bogenmaß
	 */
	public double getWinkelImBogenmass() {
		return Math.toRadians(this.winkelInGrad);
	}
	
	@Override
	public String toString()
	{
		return String.format("%.2f°", this.winkelInGrad);
	}
	
	/** 
	 * Summe aus this und summand
	 * @param summand zu addierender trigonometrie.Winkel
	 * @return this + summand
	 * @throws NullPointerException wenn summand null ist
	 */
	public Winkel addieren(Winkel summand)
	{
		return new Winkel(winkelInGrad + summand.winkelInGrad);
	}
	/** 
	 * Differenz von this und subtrahend
	 * @param subrahent zu subtrahierender trigonometrie.Winkel
	 * @return this - subtrahend
	 * @throws NullPointerException wenn subtrahent null ist
	 */
	public Winkel subtrahieren(Winkel subrahent)
	{
		return new Winkel(winkelInGrad - subrahent.winkelInGrad);
	}
	/** 
	 * Produkt aus this und zahl
	 * @param zahl Faktor
	 * @return this * zahl
	 * @throws IllegalArgumentException wenn zahl keine Zahl ist
	 */
	public Winkel multiplizieren(double zahl)
	{
		return new Winkel(zahl * winkelInGrad);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Winkel w)
			return w.winkelInGrad == this.winkelInGrad;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(this.winkelInGrad);
	}
	
}
