package ballspiel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Startet ein kleines Ballspiel als Übung für Threads
 *
 */
public class BallSpielerei extends Application {
	private BallOberflaeche view;
	private Farbtopf[] farben = {new Farbtopf(Color.BLUE), new Farbtopf(Color.YELLOW), new Farbtopf(Color.RED)};
	//Liste um die gestarteten Threads zu verwalten
	private final java.util.List<Thread> aktiveThreads = new java.util.ArrayList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hüpfende Bälle");
		view = new BallOberflaeche(this);
		Scene scene = new Scene(view, 500, 400, false);		
	    primaryStage.setScene(scene);
	    primaryStage.setOnCloseRequest(e -> {alleBeenden();});
	    primaryStage.show();
		//Uhr starten, nachdem die View sichtbar ist
		starteUhr();
	}
	
	/**
	 * erzeugt einen neuen Ball und macht ihn in der Oberfläche sichtbar
	 */
	public void neuerBall()
	{
		Random r = new Random();
		int dauer = r.nextInt(500) + 1000; // Zufallszahl zwischen 1000 und 1500
		int farbe = r.nextInt(3);
		int dx = r.nextInt(5) + 1;
		int dy = r.nextInt(5) + 1;

		//Ball-Objekt erstellen
		Ball b = new Ball(view.getVerfuegbareBreite(), view.getVerfuegbareHoehe(), dx, dy, farben[farbe]);

		//Ball vor huepfen-Logik sichtbar machen
		view.ballEintragen(b);

		//Thread erstellen
		Thread t = new Thread((() -> {
			try {
				b.huepfen(dauer); //Thread blockiert, solange der Ball hüpft
			} finally {
				//Block wird immer ausgeführt, wenn hüpfen vorbei ist (egal ob fertig gehüpft oder durch t.interrupt())
				//Thread aus Liste entfernen
				synchronized (aktiveThreads) {
					aktiveThreads.remove(Thread.currentThread());
				}

				javafx.application.Platform.runLater(() -> {
					view.ballEntfernen(b);
				});
			}
		}));

		//Thread der Liste zufügen
		synchronized(aktiveThreads) {
			aktiveThreads.add(t);
		}
		t.start();
	}
	
	/**
	 * farben
	 * @return farben
	 */
	public Farbtopf[] getFarben() {
		return farben;
	}
	
	public void auffuellen(Farbtopf topf)
	{
		Random r = new Random();
		int menge = r.nextInt(5000) + 1000; 
		topf.fuellstandErhoehen(menge);
	}

	/**
	 * beendet das Hüpfen aller Bälle
	 */
	public void alleBeenden()
	{
		synchronized(aktiveThreads) {
			for (Thread t : aktiveThreads) {
				if (t != null && t.isAlive()) {
					t.interrupt(); //Signalisiert Thread, dass er stoppen soll
				}
			}
			aktiveThreads.clear(); //Liste leeren
		}
	}

	/**
	 * Startet einen Hintergrund-Thread, der jede Sekunde
	 * die Uhrzeit im Fenster aktualisiert.
	 */
	private void starteUhr() {
		Thread uhrenThread = new Thread(() -> {
			//Formatter für Stunden und minuten
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

			while (true) {
				//Aktuelle Uhrzeit holen
				String aktuelleUhrzeit = LocalTime.now().format(formatter);

				Platform.runLater(() -> {
					if (view != null) {
						view.setUhrzeitText(aktuelleUhrzeit);
					}
				});

				try {
					//Warten, bevor neu geprüft wird
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//Falls Thread unterbrochen wird
					break;
				}
			}
		});

		//Thread zu Deamon Thread machen
		uhrenThread.setDaemon(true);
		uhrenThread.start();
	}


}
