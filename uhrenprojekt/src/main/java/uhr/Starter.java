package uhr;

import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
 
/**
 * Startet eine Uhrenoberfläche
 * @author Doro
 *
 */
public class Starter extends Application {
	@FXML private Button btnAnalog;
	@FXML private Button btnDigital;
	@FXML private Button btnEntfernen;
	@FXML private Button btnMinute;

	private Stage primaryStage;

	private final Zeit zeitModell = new Zeit();

	private List<DigitalUhr> dUhren = new LinkedList<>();
	private List<KreisUhr> kUhren = new LinkedList<>();
	private List<Minutenuhr> mUhren = new LinkedList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("starter.fxml"));
		loader.setController(this);
		Parent lc = loader.load();
		Scene scene = new Scene(lc, 400, 100);
		primaryStage.setTitle("MVC Uhren-Steuerung");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML private void initialize()
	{
		btnAnalog.setOnAction(e -> neueKreisUhr());
		btnDigital.setOnAction(e -> neueDigitalUhr());
		btnMinute.setOnAction(e -> neueMinutenUhr());
		btnEntfernen.setOnAction(e -> alleEntfernen());
		primaryStage.setOnCloseRequest(e -> starterSchliessen());
	}
	
	/**
	 * wird beim Klick auf Digital-Button aufgerufen
	 */
	private void neueDigitalUhr()
	{

		dUhren.add(new DigitalUhr(zeitModell));
	}
	
	/**
	 * wird beim Klick auf Analog-Button aufgerufen
	 */
	private void neueKreisUhr()
	{
		KreisUhr uhr = new KreisUhr(this.zeitModell);
		kUhren.add(uhr);
	}
	
	/**
	 * wird beim Klick auf Konsolen-Button aufgerufen
	 */
	private void neueMinutenUhr() {
		mUhren.add(new Minutenuhr(zeitModell));
	}
	
	private void alleEntfernen()
	{
		for(KreisUhr k : kUhren) k.dispose();
		for(DigitalUhr d : dUhren) d.beenden();
		for(Minutenuhr m : mUhren) m.beenden(); // Falls du in Minutenuhr beenden() ergänzt

		kUhren.clear();
		dUhren.clear();
		mUhren.clear();
	}
	
	private void starterSchliessen()
	{
		zeitModell.uhrStoppen();
		alleEntfernen();
	}
}
