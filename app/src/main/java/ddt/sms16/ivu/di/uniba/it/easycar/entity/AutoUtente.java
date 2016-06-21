package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 17/06/16.
 */
public class AutoUtente {

    private String targa;
    private int km;

    private String annoImmatricolazione;
    private String Utenti_Email;
    private int Modello_id;

    public AutoUtente(String targa, int km, String cilindrata, String alimentazione, String annoImmatricolazione, String Utenti_Email, int Model_id){
        this.targa=targa;
        this.km=km;

        this.annoImmatricolazione=annoImmatricolazione;
        this.Utenti_Email=Utenti_Email;
        this.Modello_id=Model_id;

    }

    @Override
    public String toString() {
        return "AutoUtente [targa=" + targa + ", km=" + km +", anno imm= "+annoImmatricolazione+", Utente_Email= "+Utenti_Email+", Model_id= "+ Modello_id
                + "]";
    }
}
