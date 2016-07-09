package ddt.sms16.ivu.di.uniba.it.easycar.entity;

import ddt.sms16.ivu.di.uniba.it.easycar.Utility;

/**
 * Created by Enrico on 20/06/16.
 */
public class Scadenza implements Comparable<Scadenza> {

    private int IDScadenza;
    private String descrizione;
    private String dataScadenza;
    private AutoUtente auto;
    private int inviata;


    public int getInviata() {
        return inviata;
    }

    public Scadenza(int IDScadenza, String descrizione, String dataScadenza, int inviata, AutoUtente auto) {
        this.IDScadenza = IDScadenza;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.auto = auto;
        this.inviata=inviata;

    }
    public Scadenza(int IDScadenza, String descrizione, String dataScadenza, AutoUtente auto) {
        this.IDScadenza = IDScadenza;
        this.descrizione = descrizione;
        this.dataScadenza = dataScadenza;
        this.auto = auto;

    }
    public Scadenza(int IDScadenza) {
        this.IDScadenza = IDScadenza;

    }
    public Scadenza(int anInt, String string, int IDScadenza, AutoUtente autoUtente){
        this.IDScadenza=IDScadenza;
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
        return "Scadenza [IDScadenza=" + IDScadenza + ", descrizione=" + descrizione + ", dataScadenza=" + dataScadenza +" , inviata="+inviata+ ", auto=" + auto.toString() + "]";
    }

    @Override
    public int compareTo(Scadenza another) {
        if(this.descrizione.equals(another.descrizione) &&
                this.dataScadenza.equalsIgnoreCase(another.dataScadenza) &&
                this.inviata == another.inviata &&
                this.auto.getTarga().equalsIgnoreCase(another.auto.getTarga()) &&
                this.auto.compareTo(another.auto) == 0)
            return 0;
        else
            return -1;
    }

    public boolean before(Scadenza s) {
        return Utility.convertStringToDate(dataScadenza).before(Utility.convertStringToDate(s.dataScadenza));
    }
}
