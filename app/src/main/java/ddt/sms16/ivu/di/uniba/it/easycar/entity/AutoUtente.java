package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente implements Comparable<AutoUtente> {

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
    public AutoUtente(String targa, int km, String annoImmatricolazione, Utente utente, Modello modello) {
        this.targa = targa;
        this.km = km;
        this.annoImmatricolazione = annoImmatricolazione;
        this.utente = utente;
        this.modello = modello;


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
        return modello.getMarca().getNome()+" "+modello.getNome()+" - "+targa;
    }

    @Override
    public int compareTo(AutoUtente another) {
        if(this.km == another.km &&
                this.annoImmatricolazione.equalsIgnoreCase(another.annoImmatricolazione) &&
                this.utente.getEmail().equalsIgnoreCase(another.utente.getEmail()) &&
                this.modello.getIDModello() == another.modello.getIDModello() &&
                this.modello.compareTo(another.modello) == 0 &&
                this.selected == another.selected)
            return 0;
        else
            return -1;
    }
}
