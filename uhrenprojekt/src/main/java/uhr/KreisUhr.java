package uhr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * stellt eine analoge Uhr dar 
 * 
 * @author Doro
 *
 */
public class KreisUhr extends JFrame implements PropertyChangeListener
{
	private static final long serialVersionUID = 1L;
	private static final String TITEL = "Kreisuhr";
	private static final String INFO = "Tasten: A(usschalten), E(inschalten)";
	private static final int BREITE = 400;
	private static final int HOEHE = 300;
	private static final int ZENTRUM_X = BREITE / 2;
	private static final int ZENTRUM_Y = HOEHE / 2;
	private static final int RADIUS = 100;
	private static final int DURCHMESSER = 2 * RADIUS;
	private static final int POS_INFO_X = 20;
	private static final int POS_INFO_Y = 40;
	private static final int POS_UHRZEIT = 60;
	private static final int[] ZEIGERLAENGE = new int[] { 5 * RADIUS / 10, 6 * RADIUS / 10, 7 * RADIUS / 10 }; 
	private static final Color[] ZEIGERFARBE = new Color[] { Color.red, Color.green, Color.blue }; 
	private static final Color[] GRAUE_FARBEN = new Color[] { Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY };
	private static final Color HINTERGRUND_FARBE = Color.white;
	private static final Color KREIS_FARBE = Color.black;
	private static final int[][] END_X = new int[3][60];
	private static final int[][] END_Y = new int[3][60];
	private static final double ZWEI_PI = 2 * Math.PI;
	private static final double[] KONST = new double[] { ZWEI_PI / 12, ZWEI_PI / 60, ZWEI_PI / 60 };

	private final Zeit zeitModell;
	private boolean uhrAn;
	private Color[] farben = ZEIGERFARBE;

	/**
	 * erstellt die analoge Uhr und meldet sie beim Model an.
	 * @param zeit das  Zeit-Model
	 */
	public KreisUhr(Zeit zeit) {
		this.zeitModell = zeit;
		this.zeitModell.anmelden(this); //anmelden als Observer
		this.uhrAn = true;

		setTitle(TITEL);
		setSize(BREITE, HOEHE);
		setVisible(true);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (Character.toUpperCase(e.getKeyChar())) {
					case 'E' -> { uhrAn = true; farben = ZEIGERFARBE; }
					case 'A' -> { uhrAn = false; farben = GRAUE_FARBEN; }
				}
				repaint();
			}
		});

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				zeitModell.abmelden(KreisUhr.this); //beim schließen abmelden
				dispose();
			}
		});
	}

	/**
	 * Holen der aktuellen Uhrzeit und Anzeige, wenn die Uhr angestellt ist
	 */
	private void tick() {
		if (!uhrAn)
		{
			return;
		}
		repaint();
	}

	/**
	 * Wird vom Modell aufgerufen, wenn sich die Zeit geändert hat.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (uhrAn) {
			//Swing Updates müssen im Event dispatch thread erfolgen
			SwingUtilities.invokeLater(this::repaint);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, BREITE, HOEHE);
		g.setColor(HINTERGRUND_FARBE);
		g.fillRect(0, 0, BREITE, HOEHE);
		g.setColor(KREIS_FARBE);
		g.drawOval((BREITE - DURCHMESSER) / 2, (HOEHE - DURCHMESSER) / 2, DURCHMESSER, DURCHMESSER);

		final int[] zeiten = new int[] { zeitModell.getStunde(), zeitModell.getMinute(), zeitModell.getSekunde() };
		g.drawString(INFO, POS_INFO_X, POS_INFO_Y);
		g.drawString(String.format("%02d:%02d:%02d", zeiten[0], zeiten[1], zeiten[2]), POS_INFO_X, POS_UHRZEIT);

		for (int i = 0; i < END_X.length; i++) {
			final int z = zeiten[i];
			if (END_X[i][z] == 0) {
				final double grad = z * KONST[i];
				END_X[i][z] = (int) (ZENTRUM_X + ZEIGERLAENGE[i] * Math.sin(grad));
				END_Y[i][z] = (int) (ZENTRUM_Y - ZEIGERLAENGE[i] * Math.cos(grad));
			}
			g.setColor(farben[i]);
			g.drawLine(ZENTRUM_X, ZENTRUM_Y, END_X[i][zeiten[i]], END_Y[i][zeiten[i]]);
		}
	}
}