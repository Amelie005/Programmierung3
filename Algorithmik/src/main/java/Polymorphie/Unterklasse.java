package Polymorphie;

public class Unterklasse extends Oberklasse {
	private int x;
	
	public Unterklasse()
	{
		this.x = 11;
	}
	
	@Override
	public void textAusgeben()
	{
		System.out.println("Polymorphie.Unterklasse - textAusgeben");
	}
	
	@Override
	public void thisXAusgeben2()
	{
		System.out.println("x in Polymorphie.Unterklasse: " + this.x);
	}
	
	@Override
	public void eineLetzteMethode()
	{
		System.out.println("------------------");
		System.out.print("super-Methode aufgerufen: ");
		super.eineLetzteMethode();
		System.out.println("this.x in Polymorphie.Unterklasse: " + this.x);
		System.out.println("------------------");
	}
	
	@Override
	public int getX()
	{
		return this.x;
	}
}
