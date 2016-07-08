package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 08/07/16.
 */

public class Auto {

    private String nomeMarca;
    private  String nomeModello;
    private  String targa;
    public Auto(String nomeMarca, String nomeModello, String targa){
        this.nomeMarca=nomeMarca;
        this.nomeModello= nomeModello;
        this.targa=targa;
    }
    public String getNomeMarca() {
        return nomeMarca;
    }

    public String getNomeModello() {
        return nomeModello;
    }

    public String getTarga() {
        return targa;
    }
}
