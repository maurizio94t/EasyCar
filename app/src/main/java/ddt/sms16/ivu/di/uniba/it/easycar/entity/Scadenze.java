package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Scadenze {
    private int IDScadenza;
    private String descrizione;
    private String dataScadenza;
    private String targa;


    public Scadenze(int IDScadenza, String descrizione, String dataScadenza, String targa){
        this.IDScadenza=IDScadenza;
        this.descrizione=descrizione;
        this.dataScadenza=dataScadenza;
        this.targa=targa;

    }
}
