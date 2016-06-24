package ddt.sms16.ivu.di.uniba.it.easycar.entity;

/**
 * Created by Maurizio on 24/06/16.
 */
public class Utente {
    private String nome;
    private String cognome;
    private String dataN;
    private int foto;
    private String email;

    public Utente(String nome, String cognome, String dataN, int foto, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataN = dataN;
        this.foto = foto;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataN() {
        return dataN;
    }

    public void setDataN(String dataN) {
        this.dataN = dataN;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utente[nome='" + nome + ", cognome='" + cognome + ", dataN='" + dataN + ", foto=" + foto + ", email='" + email + "]";
    }
}
