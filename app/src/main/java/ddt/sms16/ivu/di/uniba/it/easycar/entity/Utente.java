package ddt.sms16.ivu.di.uniba.it.easycar.entity;


public class Utente implements Comparable<Utente> {
    private String nome;
    private String cognome;
    private String dataN;

    private String email;
    private String psw;

    public Utente(String email){
        this.email=email;
    }
    public Utente(String nome, String cognome, String dataN, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataN = dataN;

        this.email = email;
        this.psw = "";
    }

    public Utente(String nome, String cognome, String dataN, String email , String psw) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataN = dataN;

        this.email = email;
        this.psw=psw;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsw() {
        return psw;
    }

    @Override
    public String toString() {
        return "Utente[nome= " + nome + ", cognome= " + cognome + ", dataN= " + dataN +  ", email= " + email + ", psw= " + psw + "]";
    }

    @Override
    public int compareTo(Utente another) {
        if(this.nome.equals(another.nome) &&
                this.cognome.equals(another.cognome) &&
                this.dataN.equalsIgnoreCase(another.dataN))
            return 0;
        else
            return -1;
    }
}
