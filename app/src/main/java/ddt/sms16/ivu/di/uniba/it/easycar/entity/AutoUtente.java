package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente {

    private String targa;
    private int km;
    private String annoImmatricolazione;
    private int fotoAuto;
    private Utente utente;
    private Modello modello;
    private int selected;

    public AutoUtente(String targa, int km, String annoImmatricolazione, int fotoAuto, Utente utente, Modello modello, int selected) {
        this.targa = targa;
        this.km = km;
        this.annoImmatricolazione = annoImmatricolazione;
        this.fotoAuto = fotoAuto;
        this.utente = utente;
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

    public Utente getUtenti_Email() {
        return utente;
    }

    public Modello getModello() {
        return modello;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int sel) {
        this.selected = sel;
    }

    @Override
    public String toString() {
        return "AutoUtente [targa=" + targa + ", km=" + km + ", anno imm= " + annoImmatricolazione + ", FotoAuto= " + fotoAuto + ", Utente_Email= " + utente + ", Modello_id= " + modello.getIDModello()
                + "]";
    }
}
