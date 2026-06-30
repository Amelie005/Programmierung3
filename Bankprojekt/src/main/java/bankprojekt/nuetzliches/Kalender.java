package bankprojekt.nuetzliches;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Bereitstellung des heutigen Datums
 */
public class Kalender implements Serializable {
	/**
	 * liefert das heutige Datum
	 * @return das heutige Datum
	 */
	public LocalDate getHeutigesDatum() {
		return LocalDate.now();
	}
}
