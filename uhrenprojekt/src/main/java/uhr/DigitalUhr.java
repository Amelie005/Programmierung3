package uhr;

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
public class DigitalUhr
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
	 * erstellt das Fenster für die digitale Uhr und bringt es auf den
	 * Bildschirm; zu Beginn läuft die Uhr im 1-Sekunden-Takt
	 */
	public DigitalUhr() {
		this.zeit = new Zeit();
		
		stage = new Stage();
		FXMLLoader loader = 
				new FXMLLoader(getClass().getResource("digitaluhr.fxml"));
		loader.setController(this);
		Parent lc = null;
		try {
			lc = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Scene scene = new Scene(lc, 400, 100);
        stage.setTitle("Digitaluhr");
        stage.setScene(scene);
        stage.show();
        
      //Thread starten, um die Uhrzeit laufen zu lassen:
		service = Executors.newSingleThreadScheduledExecutor();
		laufen = service.scheduleAtFixedRate(() -> tick(), 0, 1, TimeUnit.SECONDS);
		einschalten();
	} 
	
	@FXML private void initialize()
	{
		tick();
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
		laufen.cancel(false);
		service.shutdown();
	}

	/**
	 * Holen der aktuellen Uhrzeit und Anzeige, wenn die Uhr angestellt ist
	 */
	private void tick() 
	{
		if(uhrAn)
		{
			Platform.runLater( () ->
				anzeige.setText(String.format("%02d:%02d:%02d", 
						zeit.getStunde(), zeit.getMinute(),zeit.getSekunde())));
		}
	}

	/**
	 * schließt das Fenster
	 */
	public void beenden() {
		stage.close();
	}


}
