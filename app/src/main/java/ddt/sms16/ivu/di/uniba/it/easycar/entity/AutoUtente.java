package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente {

    private String targa;
    private int km;
    private String annoImmatricolazione;
    private int fotoAuto;
    private String utente_email;
    private Modello modello;
    private boolean selected;

    public AutoUtente(String targa, int km, String annoImmatricolazione, int fotoAuto, String utente_email, Modello modello, boolean selected) {
        this.targa = targa;
        this.km = km;
        this.annoImmatricolazione = annoImmatricolazione;
        this.fotoAuto = fotoAuto;
        this.utente_email = utente_email;
        this.modello = modello;
        this.selected = selected;

    }

    public String getTarga() {
        return targa;
    }

    public int getKm() {
        return km;
    }

    public String getAnnoImmatricolazione() {
        return annoImmatricolazione;
    }

    public int getFotoAutoId() {
        return fotoAuto;
    }

    public String getUtenti_Email() {
        return utente_email;
    }

    public Modello getModello() {
        return modello;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
    }

    @Override
    public String toString() {
        return "AutoUtente [targa=" + targa + ", km=" + km + ", anno imm= " + annoImmatricolazione + ", FotoAuto= " + fotoAuto + ", Utente_Email= " + utente_email + ", Modello_id= " + modello.getIDModello()
                + "]";
    }
}
