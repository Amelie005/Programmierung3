package uhr;

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

	/**
	 * erstellt die Uhr
	 */
    public Zeit() {
		//Thread starten, um die Uhrzeit laufen zu lassen:
    	service = Executors.newSingleThreadScheduledExecutor();
		laufen = service.scheduleAtFixedRate(() -> laufen(), 0, 1, TimeUnit.SECONDS);
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
	
	private void laufen()
	{
		LocalTime jetzt = LocalTime.now();
		stunde = jetzt.getHour();
		minute = jetzt.getMinute();
		sekunde = jetzt.getSecond();
	}
	
	/**
	 * stoppt die laufende Uhr
	 */
	public void uhrStoppen()
	{
		laufen.cancel(false);
		service.shutdown();
	}

}
