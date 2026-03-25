package mathematik;
import trigonometrie.Winkel;

public class Vektor {
    private int x;
    private int y;
    private double laenge;
    private Winkel winkel;

    public Vektor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vektor(double laenge, Winkel winkel) {
        this.laenge = laenge;
        this.winkel = new Winkel(winkel.getWinkelInGrad());
    }


}
