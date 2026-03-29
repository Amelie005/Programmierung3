
public class Oberklasse {
	private int x;
	
	public Oberklasse()
	{
		this.x = 7;
	}
	
	public void thisXAusgeben()
	{
		System.out.println("x in Oberklasse: " + this.x);
	}
	
	public void textAusgeben()
	{
		System.out.println("Oberklasse - textAusgeben");
	}
	
	public void thisXAusgeben2()
	{
		System.out.println("x in Oberklasse: " + this.x);
	}
	
	public void andereMethodenAufrufen()
	{
		System.out.println("------------------");
		System.out.println("getX() in Oberklasse aufgerufen: " + this.getX());
		System.out.print("thisXAusgeben in Oberklasse aufgerufen: ");
		this.thisXAusgeben();
		System.out.print("thisXAusgeben2 in Oberklasse aufgerufen: ");
		this.thisXAusgeben2();
		System.out.println("------------------");
	}
	
	public void eineLetzteMethode()
	{
		System.out.println("this.x in Oberklasse: " + this.x);
	}
	
	public int getX()
	{
		return this.x;
	}

}
