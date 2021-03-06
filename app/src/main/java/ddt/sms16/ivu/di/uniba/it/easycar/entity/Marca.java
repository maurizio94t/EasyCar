package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Enrico on 19/06/16.
 */
public class Marca implements Comparable<Marca> {

    private int IDMarca;
    private String nome;


    public Marca(String nome) {
        this.nome = nome;
    }

    public Marca(int IDMarca, String nome) {
        this.IDMarca = IDMarca;
        this.nome = nome;
    }
    public Marca(int IDMarca) {
        this.IDMarca = IDMarca;

    }
    public int getIDMarca() {
        return IDMarca;
    }

    public String getNome() {
        return nome;
    }

    public void setIDMarca(int IDMarca) {
        this.IDMarca = IDMarca;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return  nome;
    }

    @Override
    public int compareTo(Marca another) {
        if(this.nome.equalsIgnoreCase(another.nome))
            return 0;
        else
            return -1;
    }
}
