package bankprojekt.vorlesungscode.sortiererei;

/**
 * Träger einer Methode zum Vergleich zweier Objekte
 */
public interface Vergleicher {
	/**
	 * vergleicht a und b
	 * @param a zu vergleichendes Objekt
	 * @param b zu vergleichendes Objekt
	 * @return positiver Wert, wenn a größer als b, 
	 *         negativer Wert, wenn a kleiner als b
	 *         und 0, wenn a == b 
	 * @throws IllegalArgumentException wenn a und b nicht verglichen werden können
	 */
	 int vergleichen(Object a, Object b);
	 
		/**
		 * prüft, ob a größer b
		 * @param a erstes zu vergleichendes Objekt
		 * @param b zweites zu vergleichendes Objekt
		 * @return a kleiner b
		 * @throws IllegalArgumentException wenn a und b nicht verglichen werden können
		 */
		default boolean isGroesser(Object a, Object b) {
			return this.vergleichen(a, b) > 0;
		}
		
		/**
		 * prüft, ob a kleiner b
		 * @param a erstes zu vergleichendes Objekt
		 * @param b zweites zu vergleichendes Objekt
		 * @return a kleiner b
		 * @throws IllegalArgumentException wenn a und b nicht verglichen werden können
		 */
		default public boolean isKleiner(Object a, Object b){
			return this.vergleichen(a,  b) < 0;
		}
		
		/**
		 * prüft, ob a == b
		 * @param a erstes zu vergleichendes Objekt
		 * @param b zweites zu vergleichendes Objekt
		 * @return a == b
		 * @throws IllegalArgumentException wenn a und b nicht verglichen werden können
		 */
		default public boolean isGleich(Object a, Object b){
			return this.vergleichen(a,  b) == 0;
		}
		
		/**
		 * sortiert das Array x aufsteigend
		 * @param x das zu sortierende Array
		 * @throws NullPointerException wenn x == null
		 */
		default void sortiere(Object[] x) {
			boolean unsortiert = true;
			Object temp;
			while (unsortiert) 
			{
				unsortiert = false;          
				for (int i = 0; i < x.length - 1; i++) 
				{
					if (this.isGroesser(x[i], x[i+1])) 
					{
						temp = x[i];
						x[i] = x[i + 1];
						x[i + 1] = temp;
						unsortiert = true; 
					}
				}
			}
		}

	 

}
