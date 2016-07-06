package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Problema {

    private int IDProblema;
    private String descrizione;
    private AutoUtente auto;


    public Problema(int IDProblema, String descrizione, AutoUtente auto) {
        this.IDProblema = IDProblema;
        this.descrizione = descrizione;
        this.auto = auto;

    }
    public Problema( String descrizione, AutoUtente auto) {
        this.IDProblema = IDProblema;
        this.descrizione = descrizione;
        this.auto = auto;

    }
    public int getIDProblemi() {
        return IDProblema;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public AutoUtente getAuto() {
        return auto;
    }

    public String toString() {
        return "Problema[IDProblema=" + IDProblema + ", Descrizione=" + descrizione + ", auto="+auto.toString()+"]";
    }
}
