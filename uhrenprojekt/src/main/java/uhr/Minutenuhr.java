package uhr;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Stellt eine Minutenuhr dar, die man anhalten und weiterlaufen lassen kann
 */
public class Minutenuhr {
	@FXML private Label lblStunde;
	@FXML private Label lblMinute;
	@FXML private Label lblDoppelpunkt;
	@FXML private RadioButton radEin;
	@FXML private RadioButton radAus;
	private final ToggleGroup gruppe = new ToggleGroup();
	private Stage stage;
	private Zeit zeit;
	
	/**
	 * erstellt das Fenster für die Minutenuhr und bringt es auf den
	 * Bildschirm; zu Beginn läuft die Uhr
	 */
	public Minutenuhr() {
		stage = new Stage();
		FXMLLoader loader = 
				new FXMLLoader(getClass().getResource("minutenuhr.fxml"));
		loader.setController(this);
		Parent lc = null;
		try {
			lc = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Scene scene = new Scene(lc, 400, 100);
        stage.setTitle("Minutenuhr");
        stage.setScene(scene);
        stage.show();
	} 
	
	@FXML private void initialize()
	{
		setzeZeit();
		radEin.setToggleGroup(gruppe);
		radAus.setToggleGroup(gruppe);
		radEin.setOnAction( e -> ein());
		radAus.setOnAction( e -> aus());
	}
	
	/**
	 * wird beim Klick auf den Ein-Button aufgerufen
	 */
	private void ein()
	{
	}
	
	/**
	 * wird beim Klick auf den Aus-Button aufgerufen
	 */
	private void aus()
	{
	}
	
	/**
	 * Holen der aktuellen Uhrzeit und Anzeige, wenn die Uhr angestellt ist
	 */
	private void setzeZeit() 
	{
		Platform.runLater( () -> {
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
		lblMinute.setVisible((sichtbar));
	}
}
