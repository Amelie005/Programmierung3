package Polymorphie;

public class Polymorphietest {

	public static void main(String[] args) {
		Oberklasse ober = new Oberklasse();
		ober.textAusgeben();
		ober.thisXAusgeben();
		ober.thisXAusgeben2();
		ober.andereMethodenAufrufen();
		ober.eineLetzteMethode();
		System.out.println();
		
		Unterklasse unter = new Unterklasse();
		unter.textAusgeben();
		unter.thisXAusgeben();
		unter.thisXAusgeben2();
		unter.andereMethodenAufrufen();
		unter.eineLetzteMethode();
		System.out.println();
		
		Oberklasse ober2 = new Unterklasse();
		ober2.textAusgeben();
		ober2.thisXAusgeben();
		ober2.thisXAusgeben2();
		ober2.andereMethodenAufrufen();
		ober2.eineLetzteMethode();
		

	}

}
