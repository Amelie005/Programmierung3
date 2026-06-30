package bankprojekt.aktienhandel;

public class AktienkontoController {

    private Aktienkonto model;
    private AktienkontoView view;

    public AktienkontoController(Aktienkonto model) {
        this.model = model;
        this.view = new AktienkontoView(this);
        model.anmelden(view);
    }


}
