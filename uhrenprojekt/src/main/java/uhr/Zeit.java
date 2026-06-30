package uhr;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
* eine Uhr mit Sekundenzählung
*/
public class Zeit 
{
	private Future<?> laufen;
	private ScheduledExecutorService service;
	private int stunde, minute, sekunde;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	/**
	 * erstellt die Uhr
	 */
    public Zeit() {
		//Thread starten, um die Uhrzeit laufen zu lassen:
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

	/**
	 * Aktualisiert die interne Uhrzeit und benachrichtigt alle registrierten
	 * Observer, falls sich die Zeit seit dem letzten Aufruf geändert hat.
	 */
	private void tick() {
		LocalTime jetzt = LocalTime.now();
		int s = jetzt.getHour(), m = jetzt.getMinute(), sek = jetzt.getSecond();

		//prüfung, ob eine Änderung vorliegt, um unnötige Benachrichtigungen zu vermeiden
		if (s != stunde || m != minute || sek != sekunde) {
			stunde = s; minute = m; sekunde = sek;
			//informiere alle Observer über das Update
			support.firePropertyChange("zeit", null, this);
		}
	}

    /**
     * liefert die aktuelle Stunde
     * @return Stunde
     */
    public int getStunde() { return stunde; }

    /**
     * liefert die aktuelle Minute
     * @return Minute
     */
    public int getMinute() { return minute; }

    /**
     * liefert die aktuelle Sekunde
     * @return Sekunde
     */
    public int getSekunde() { return sekunde; }

	/**
	 * Methode zum anmelden eines Observers.
	 * @param pcl Observer Objekt das angemeldet werden soll.
	 */
	public void anmelden(PropertyChangeListener pcl) { support.addPropertyChangeListener(pcl); }

	/**
	 * Methode zum abmelden eines Observers.
	 * @param pcl Observer Objekt das abgemeldet werden soll.
	 */
	public void abmelden(PropertyChangeListener pcl) { support.removePropertyChangeListener(pcl); }
	
	/**
	 * stoppt die laufende Uhr
	 */
	public void uhrStoppen()
	{
		service.shutdown();
	}

}
