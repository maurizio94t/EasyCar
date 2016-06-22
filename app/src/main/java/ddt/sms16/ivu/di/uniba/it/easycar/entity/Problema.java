package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Problema {

    private int IDProblemi;
    private String descrizione;
    private AutoUtente auto;


    public Problema(int IDProblemi, String descrizione, AutoUtente auto) {
        this.IDProblemi = IDProblemi;
        this.descrizione = descrizione;
        this.auto = auto;

    }
}
