package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Problema implements Comparable<Problema> {

    private int IDProblema;
    private String descrizione;
    private AutoUtente auto;

    public Problema(int IDProblema){
        this.IDProblema=IDProblema;
    }
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
    public int getIDProblema() {
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


    @Override
    public int compareTo(Problema another) {
        if(this.descrizione.equals(another.descrizione) &&
                this.auto.getTarga().equalsIgnoreCase(another.auto.getTarga()))
            return 0;
        else
            return -1;
    }
}
