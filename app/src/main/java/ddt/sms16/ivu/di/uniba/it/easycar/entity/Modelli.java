package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Modelli {



private int IDModello;
    private String nome;
    private String segmento;
    private String alimentazione;
    private String cilindrata;
    private String kw;
    private int marca_id;


    public Modelli(int IDModello, String nome, String segmento, String alimentazione, String cilindrata, String kw, int marca_id){
        this.IDModello=IDModello;
        this.nome=nome;
        this.segmento=segmento;
        this.alimentazione=alimentazione;
        this.cilindrata=cilindrata;
        this.kw=kw;
        this.marca_id=marca_id;
    }
    @Override
    public String toString() {
        return "Modelli [IDModello=" + IDModello + ", Nome=" + nome +  ", segmento=" + segmento+ ", alimentazione=" + alimentazione+ ", cilindrata=" + cilindrata+ ", kw=" + kw+ ", marca_id=" + marca_id+"]";
    }


}
