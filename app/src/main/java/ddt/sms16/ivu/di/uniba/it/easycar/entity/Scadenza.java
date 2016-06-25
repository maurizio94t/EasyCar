package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Scadenza {

    private int IDScadenza;
    private String descrizione;
    private String dataScadenza;
    private AutoUtente auto;


    public Scadenza(int IDScadenza, String descrizione, String dataScadenza, AutoUtente auto) {
        this.IDScadenza = IDScadenza;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.auto = auto;

    }
    public int getIDScadenza() {
        return IDScadenza;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public AutoUtente getAuto() {
        return auto;
    }

}
