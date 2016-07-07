package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 20/06/16.
 */
public class Modello implements Comparable<Modello> {


    private int IDModello;


    private String nome;
    private String segmento;
    private String alimentazione;
    private String cilindrata;
    private String kw;
    private Marca marca;


    public Modello(int IDModello, String nome, String segmento, String alimentazione, String cilindrata, String kw, Marca marca) {
        this.IDModello = IDModello;
        this.nome = nome;
        this.segmento = segmento;
        this.alimentazione = alimentazione;
        this.cilindrata = cilindrata;
        this.kw = kw;
        this.marca = marca;
    }
    public Modello(String nome, String segmento, String alimentazione, String cilindrata, String kw, Marca marca) {

        this.nome = nome;
        this.segmento = segmento;
        this.alimentazione = alimentazione;
        this.cilindrata = cilindrata;
        this.kw = kw;
        this.marca = marca;
    }
    public Modello(int IDModello){
        this.IDModello = IDModello;
    }

    public Marca getMarca() {
        return marca;
    }

    public int getIDModello() {
        return IDModello;
    }

    public String getNome() {
        return nome;
    }

    public String getSegmento() {
        return segmento;
    }

    public String getAlimentazione() {
        return alimentazione;
    }

    public String getCilindrata() {
        return cilindrata;
    }

    public String getKw() {
        return kw;
    }

    @Override
    public String toString() {
        return "Modelli [IDModello=" + IDModello + ", Nome=" + nome + ", segmento=" + segmento + ", alimentazione=" + alimentazione + ", cilindrata=" + cilindrata + ", kw=" + kw + ", marca=" + marca.toString()+ "]";
    }

    @Override
    public int compareTo(Modello another) {
        if(this.nome.equalsIgnoreCase(another.nome) &&
                this.segmento.equalsIgnoreCase(another.segmento) &&
                this.alimentazione.equalsIgnoreCase(another.alimentazione) &&
                this.cilindrata.equalsIgnoreCase(another.cilindrata) &&
                this.kw.equalsIgnoreCase(another.kw) &&
                this.marca.getIDMarca() == another.marca.getIDMarca())
            return 0;
        else
            return -1;
    }
}
