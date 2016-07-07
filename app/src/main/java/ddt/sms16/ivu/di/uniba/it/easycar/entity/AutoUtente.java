package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente {

    private String targa;
    private int km;
    private String annoImmatricolazione;

    private Utente utente;
    private Modello modello;
    private int selected;

    public AutoUtente(String targa, int km, String annoImmatricolazione, Utente utente, Modello modello, int selected) {
        this.targa = targa;
        this.km = km;
        this.annoImmatricolazione = annoImmatricolazione;

        this.utente = utente;
        this.modello = modello;
        this.selected = selected;

    }
    public AutoUtente(String targa) {
        this.targa = targa;

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


    public Utente getUtente() {
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
        return "AutoUtente [targa=" + targa + ", km=" + km + ", anno imm= " + annoImmatricolazione + ", Utente_Email= " + utente.toString() + ", Modello_id= " + modello.toString()
                + ", selected= " + selected + "]";
    }
}
