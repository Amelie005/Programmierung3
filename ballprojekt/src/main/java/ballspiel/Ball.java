package ballspiel;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * ein hüpfender Ball für eine JavaFx-Oberfläche
 *
 */
public class Ball extends Circle {
	private static final int RADIUS = 8;
	private static final int BENOETIGTE_MENGE = 2;
	private final double breite;
	private final double hoehe;
	private int dx = 2;
	private int dy = 2;
	private double x;
	private double y;
	private Farbtopf topf;

	

	public Ball(double breite, double hoehe, int dx, int dy, Farbtopf topf) {
		this.breite = breite;
		this.hoehe = hoehe;
		this.setFill(topf.getFarbe());
		this.setVisible(true);
		this.setRadius(RADIUS);
		x = RADIUS;
		y = RADIUS;
		zeichnen(true);
		this.topf = topf;
		this.dx = Math.max(Math.min(dx, 5), 1);
		this.dy = Math.max(Math.min(dy, 5), 1);
	}

	/**
	 * setzt den Ball an die angegebene Position
	 * @param grau true, wenn der Ball in grau gezeichnet werden soll, 
	 *             false, wenn er in der richtigen Farbe gezeichnet werden soll
	 */
	private void zeichnen(boolean grau) {
		Platform.runLater(() ->
		{
			if(grau)
				this.setFill(Color.GREY);
			else
				this.setFill(topf.getFarbe());	
			this.setLayoutX(x);
			this.setLayoutY(y);
		});
	}
	

	/**
	 * bewegt den Ball einen Schritt weiter
	 * @throws ZuWenigFarbeException wenn zu wenig Farbe im Topf ist
	 */
	private void einHuepfer() throws ZuWenigFarbeException {
		topf.fuellstandVerringern(BENOETIGTE_MENGE);
		x += dx;
		y += dy;
		if (x - RADIUS <= 0) {
			x = RADIUS;
			dx = -dx;
		}
		if (x + RADIUS >= breite) {
			x = breite - RADIUS;
			dx = -dx;
		}
		if (y - RADIUS <= 0) {
			y = RADIUS;
			dy = -dy;
		}
		if (y + RADIUS >= hoehe) {
			y = hoehe - RADIUS;
			dy = -dy;
		}
		zeichnen(false);
	}
	
	/**
	 * bewegt den Ball dauer viele Schritte weiter in der Oberfläche. Um eine angenehme Animation
	 * zu erhalten, wird nach jedem Schritt eine Pause eingelegt.
	 * @param anzahlHuepfer Anzahl der Schritte
	 */
	public void huepfen(int anzahlHuepfer) {
		for (int i = 1; i <= anzahlHuepfer; i++) {

			//vor jedem Schritt prüfen, ob von außen abgebrochen wird
			if (Thread.currentThread().isInterrupted()) {
				return; //Schleife abbrechen, Thread stirbt
			}

			//Synchronisierter Zugriff auf den Topf, um passiv zu warten
			synchronized (topf) {
				while (topf.getFuellstand() < BENOETIGTE_MENGE) {
					zeichnen(true);
					try {
						//Thread gibt Schloss ab und legt sich schlafen
						topf.wait(); //damit es aufgerufen werden darf, Methode mit Farbtopf.java synchronisiert
					} catch (InterruptedException e) {
						//Wenn Thread während des wartens unterbrochen wird
						return;
					}
				}
			}

			try {
				this.einHuepfer();
			} catch (ZuWenigFarbeException e) {
				//kann nicht auftreten
			}
			try {
				Thread.sleep(5); //Pause für die Animation
			} catch (InterruptedException e) {
				//wenn Thread während des Schlafens unterbrochen wird
				return;
			}
		}
	}
}
