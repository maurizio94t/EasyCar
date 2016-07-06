package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Scadenza implements Comparable<Scadenza> {

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

    @Override
    public String toString() {
        return "Scadenza [IDScadenza=" + IDScadenza + ", descrizione=" + descrizione + ", dataScadenza=" + dataScadenza + ", auto=" + auto.toString() + "]";
    }

    @Override
    public int compareTo(Scadenza s) {
        if(this.descrizione.equals(s.descrizione) &&
                this.dataScadenza.equalsIgnoreCase(s.dataScadenza) &&
                this.auto.getTarga().equalsIgnoreCase(s.auto.getTarga()))
            return 0;
        else
            return -1;
    }
}
