package uhr;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Stellt eine Minutenuhr dar, die man anhalten und weiterlaufen lassen kann
 */
public class Minutenuhr implements PropertyChangeListener {
	@FXML private Label lblStunde;
	@FXML private Label lblMinute;
	@FXML private Label lblDoppelpunkt;
	@FXML private RadioButton radEin;
	@FXML private RadioButton radAus;

	private final ToggleGroup gruppe = new ToggleGroup();
	private Stage stage;
	private final Zeit zeit;
	private boolean uhrAn = true;

	/**
	 * erstellt das Fenster für die Minutenuhr.
	 * @param zeit das Model, bei dem sich die Uhr anmeldet
	 */
	public Minutenuhr(Zeit zeit) {
		this.zeit = zeit;
		this.zeit.anmelden(this); //als Observer anmelden

		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("minutenuhr.fxml"));
		loader.setController(this);
		try {
			Parent lc = loader.load();
			Scene scene = new Scene(lc, 400, 100);
			stage.setTitle("Minutenuhr");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML private void initialize()
	{
		radEin.setToggleGroup(gruppe);
		radAus.setToggleGroup(gruppe);
		radEin.setSelected(true);
		radEin.setOnAction(e -> ein());
		radAus.setOnAction(e -> aus());
		stage.setOnCloseRequest(e -> zeit.abmelden(this));
		aktualisiereAnzeige();
	}

	/**
	 * Reagiert auf Änderungen des Modells.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (uhrAn) {
			aktualisiereAnzeige();
		}
	}
	/**
	 * wird beim Klick auf den Ein-Button aufgerufen
	 */
	private void ein()
	{
		uhrAn = true;
		sichtbarMachen(true);
		aktualisiereAnzeige();
	}
	
	/**
	 * wird beim Klick auf den Aus-Button aufgerufen
	 */
	private void aus()
	{
		uhrAn = false;
		sichtbarMachen(false);
	}

	/**
	 * Aktualisiert die Anzeige der Uhr
	 */
	private void aktualisiereAnzeige() {
		Platform.runLater(() -> {
			lblStunde.setText(String.format("%02d", zeit.getStunde()));
			lblMinute.setText(String.format("%02d", zeit.getMinute()));
		});
	}
	
	/**
	 * Macht die Anzeige der Uhrzeit sichtbar oder unsichtbar
	 * @param sichtbar true, wenn die Uhrzeit sichtbar werden soll
	 */
	private void sichtbarMachen(boolean sichtbar) {
		lblStunde.setVisible(sichtbar);
		lblDoppelpunkt.setVisible(sichtbar);
		lblMinute.setVisible(sichtbar);
	}

	/**
	 * Schließt das Fenster und meldet die Uhr als Observer vom Model ab.
	 */
	public void beenden() {
		zeit.abmelden(this);
		stage.close();
	}
}
