package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Manutenzione {


    private int IDManutenzione;
    private String descrizione;
    private String data;
    private int ordinaria;
    private String kmManutenzione;
    private AutoUtente auto;

    public Manutenzione(String descrizione, String data, int ordinaria, String kmManutenzione, AutoUtente auto) {

        this.descrizione = descrizione;
        this.data = data;
        this.ordinaria = ordinaria;
        this.kmManutenzione = kmManutenzione;
        this.auto = auto;
    }

    public Manutenzione(int IDManutenzione, String descrizione, String data, int ordinaria, String kmManutenzione, AutoUtente auto) {
        this.IDManutenzione = IDManutenzione;
        this.descrizione = descrizione;
        this.data = data;
        this.ordinaria = ordinaria;
        this.kmManutenzione = kmManutenzione;
        this.auto = auto;
    }

    public int getIDManutenzione() {
        return IDManutenzione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getData() {
        return data;
    }

    public int getOrdinaria() {
        return ordinaria;
    }

    public String getKmManutenzione() {
        return kmManutenzione;
    }

    public AutoUtente getAuto() {
        return auto;
    }

    @Override
    public String toString() {
        return "Manutenzione [IDManutenzione=" + IDManutenzione + ", descrizione=" + descrizione + ", data=" + data + ", ordinaria=" + ordinaria + ", kmManutenzione=" + kmManutenzione + ", targa auto=" + auto.getTarga() + "]";
    }
}
