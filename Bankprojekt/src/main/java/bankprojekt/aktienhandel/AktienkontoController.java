package bankprojekt.aktienhandel;

public class AktienkontoController {

    private Aktienkonto model;
    private AktienkontoView view;

    /**
     * Erstellt einen neuen Controller und initialisiert die View für das gegebene Model.
     * Meldet die View zudem als Beobachter am Aktienkonto an.
     * * @param model Das zu steuernde Aktienkonto
     */
    public AktienkontoController(Aktienkonto model) {
        this.model = model;
        this.view = new AktienkontoView(this);
        model.anmelden(view);
    }

    /**
     * Beendet die Verbindung zwischen Controller, View und Model.
     * Meldet die View als Beobachter vom Aktienkonto ab, um Memory Leaks zu vermeiden.
     */
    public void beenden() {
        model.abmelden(view);
    }

}
