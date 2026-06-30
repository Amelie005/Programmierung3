package uhr;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Stellt eine Digitale Uhr dar, die man anhalten und weiterlaufen lassen kann
 *
 */
public class DigitalUhr implements PropertyChangeListener
{
	@FXML private Label anzeige;
	@FXML private Button btnEin;
	@FXML private Button btnAus;
	private Stage stage;
	
	private ScheduledExecutorService service;
	private Future<?> laufen;
	
	private Zeit zeit;
	private boolean uhrAn;

	/**
	 * erstellt das Fenster für die digitale Uhr.
	 * @param zeit das Model, bei dem sich die Uhr anmeldet
	 */
	public DigitalUhr(Zeit zeit) {
		this.zeit = zeit;
		this.zeit.anmelden(this); //als Observer anmelden

		stage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("digitaluhr.fxml"));
		loader.setController(this);
		try {
			Parent lc = loader.load();
			Scene scene = new Scene(lc, 400, 100);
			stage.setTitle("Digitaluhr");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wird vom Modell aufgerufen, wenn sich die Zeit geändert hat.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Platform.runLater(() -> {
			anzeige.setText(String.format("%02d:%02d:%02d",
					zeit.getStunde(), zeit.getMinute(), zeit.getSekunde()));
		});
	}
	
	@FXML private void initialize()
	{
		//initiale Anzeige
		anzeige.setText(String.format("%02d:%02d:%02d",
				zeit.getStunde(), zeit.getMinute(), zeit.getSekunde()));
		btnEin.setOnAction( e -> einschalten());
		btnAus.setOnAction( e -> ausschalten());
		stage.setOnCloseRequest(e -> fensterSchliessen());
	}
	
	/**
	 * wird beim Klick auf den Ein-Button aufgerufen
	 */
	private void einschalten()
	{
		uhrAn = true;
		btnEin.setDisable(true);
		btnAus.setDisable(false);
	}
	
	/**
	 * wird beim Klick auf den Aus-Button aufgerufen
	 */
	private void ausschalten()
	{
		uhrAn = false;
		btnEin.setDisable(false);
		btnAus.setDisable(true);
	}
	
	/**
	 * wird beim Schließen dieses Fenster aufgerufen
	 */
	private void fensterSchliessen()
	{
		zeit.abmelden(this);
		stage.close();
	}

	/**
	 * schließt das Fenster
	 */
	public void beenden() {
		stage.close();
	}

}
