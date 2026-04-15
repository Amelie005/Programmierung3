package bankprojekt.spielereien;

import java.util.Arrays;

/**
 * Eine kleine Spielerei mit Arrays, um den Umgang mit Interfaces 
 * zu üben
 */
public class EinigeArrays {

	/**
	 * erzeugt zwei Arrays mit Hilfe von Arrays.setAll
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		int[] eins = new int[20];
		//in das Array eins die Zahlen von 1 bis 20 hineinschreiben mit Hilfe von setAll:

		System.out.println("Die Zahlen von 1 bis 20: ");
		System.out.println(Arrays.toString(eins));

		//in das Array eins immer abwechselnd 0 und 1 hineinschreiben mit Hilfe von setAll:

		System.out.println("Immer abwechselnd 0 und 1: ");
		System.out.println(Arrays.toString(eins));
		
		double[] zwei = new double[20];
		//in das Array zwei die Zweierpotenzen von 2^0 bis 2^19 hineinschreiben mit Hilfe von setAll:

		System.out.println("Die Zweierpotenzen von 2 hoch 0 bis 2 hoch 19:");
		System.out.println(Arrays.toString(zwei));
	}

}
