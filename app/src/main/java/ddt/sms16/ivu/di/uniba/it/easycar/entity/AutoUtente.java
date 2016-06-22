package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente {

    private String targa;
    private int km;
    private String annoImmatricolazione;
    private String Utenti_Email;
    private Modello Modello_id;

    public AutoUtente(String targa, int km, String cilindrata, String alimentazione, String annoImmatricolazione, String Utenti_Email, Modello Model_id) {
        this.targa = targa;
        this.km = km;
        this.annoImmatricolazione = annoImmatricolazione;
        this.Utenti_Email = Utenti_Email;
        this.Modello_id = Model_id;

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

    public String getUtenti_Email() {
        return Utenti_Email;
    }

    public Modello getModello_id() {
        return Modello_id;
    }

    @Override
    public String toString() {
        return "AutoUtente [targa=" + targa + ", km=" + km + ", anno imm= " + annoImmatricolazione + ", Utente_Email= " + Utenti_Email + ", Model_id= " + Modello_id.getIDModello()
                + "]";
    }
}
