package ddt.sms16.ivu.di.uniba.it.easycar.entity;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public int getInviata() {
        return inviata;
    }

    public void setInviata(int inviata) {
        this.inviata = inviata;
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

    public boolean lastDay() {
        Date date = Utility.convertStringToDate(dataScadenza);
        Calendar dataScadenza = Calendar.getInstance();
        dataScadenza.setTime(date);
        Calendar today =  Calendar.getInstance();

        //portare tutto in calendar e poi continuare
        //Log.d("DataCalendar scadenza>", dataScadenza.get(Calendar.YEAR) + " - " + dataScadenza.get(Calendar.MONTH) + " - " +  dataScadenza.get(Calendar.DAY_OF_MONTH));
        //Log.d("DataCalendar oggi >", today.get(Calendar.YEAR) + " - " + today.get(Calendar.MONTH) + " - " +  today.get(Calendar.DAY_OF_MONTH));

        if(dataScadenza.get(Calendar.YEAR) == today.get(Calendar.YEAR) && dataScadenza.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            if(dataScadenza.get(Calendar.DAY_OF_MONTH)+1 == today.get(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }
        return false;
    }
}
