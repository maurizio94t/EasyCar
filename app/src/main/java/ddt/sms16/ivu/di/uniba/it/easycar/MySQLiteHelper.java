package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;


public class MySQLiteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EasyCar";

    private static final String TABELLA_MARCHE = "Marche";
    private static final String TABELLA_AUTO_UTENTE = "AutoUtente";
    private static final String TABELLA_SCADENZE = "Scadenze";
    private static final String TABELLA_UTENTI = "Utenti";
    private static final String TABELLA_MODELLI = "Modelli";
    private static final String TABELLA_PROBLEMI = "Problemi";
    private static final String TABELLA_MANUTENZIONI = "Manutenzioni";



    String CREA_TABELLA_UTENTE = "CREATE TABLE Utenti (NomeU TEXT , CognomeU TEXT, DataDiNascita TEXT , Email TEXT PRIMARY KEY  )";
    String CREA_TABELLA_MARCHE = "CREATE TABLE Marche (IDMarca INTEGER   PRIMARY KEY  , Nome TEXT)";
    String CREA_TABELLA_MODELLI = "CREATE TABLE  Modelli ( IDModello INTEGER  PRIMARY KEY      , Nome TEXT  , Segmento TEXT  , Alimentazione TEXT  , Cilindrata TEXT , KW TEXT  , Marca_id INTEGER  , FOREIGN KEY (`Marca_id`) REFERENCES  `Marche` (`IDMarca`) ON DELETE CASCADE ON UPDATE CASCADE );";
    String CREA_TABELLA_AUTOUTENTE = "CREATE TABLE AutoUtente ( Targa TEXT  PRIMARY KEY, KM INTEGER , AnnoImmatricolazione TEXT   , Selected INTEGER   , Utenti_Email TEXT  , Modelli_id INTEGER  , FOREIGN KEY (`Utenti_Email`) REFERENCES  `Utenti` (`Email`) ON DELETE CASCADE ON UPDATE CASCADE , FOREIGN KEY (`Modelli_id`) REFERENCES  `Modelli` (`IDModello`) ON DELETE CASCADE ON UPDATE CASCADE );";
    String CREA_TABELLA_PROBLEMI = "CREATE TABLE Problemi ( IDProblema INTEGER   PRIMARY KEY     , Descrizione TEXT, Targa TEXT, FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`) ON DELETE CASCADE ON UPDATE CASCADE );";
    String CREA_TABELLA_MANUTENZIONI = "CREATE TABLE Manutenzioni ( IDManutenzione INTEGER  PRIMARY KEY   , Descrizione TEXT  ,Data TEXT  ,Ordinaria INTEGER,  KmManutenzione TEXT , Targa TEXT , FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`) ON DELETE CASCADE ON UPDATE CASCADE );";
    String CREA_TABELLA_SCADENZE = "CREATE TABLE Scadenze ( IDScadenza INTEGER PRIMARY KEY     , Descrizione TEXT, DataScadenza TEXT, Targa TEXT, FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`) ON DELETE CASCADE ON UPDATE CASCADE );";



            public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREA_TABELLA_UTENTE);
        db.execSQL(CREA_TABELLA_MARCHE);
        db.execSQL(CREA_TABELLA_MODELLI);
        db.execSQL(CREA_TABELLA_AUTOUTENTE);
        db.execSQL(CREA_TABELLA_PROBLEMI);


        db.execSQL(CREA_TABELLA_MANUTENZIONI);
        db.execSQL(CREA_TABELLA_SCADENZE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Marche");


        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */


    public void aggiungiAutoUtente(AutoUtente auto) {
        Log.d("aggiungiAutoUtente", auto.toString());
         SQLiteDatabase db = this.getWritableDatabase();

         ContentValues values = new ContentValues();

        values.put("Targa", auto.getTarga());
        values.put("KM", auto.getKm());
        values.put("AnnoImmatricolazione", auto.getAnnoImmatricolazione());
        values.put("Utenti_Email", auto.getUtente().getEmail());
        values.put("Selected",auto.getSelected());
        values.put("Modelli_id", auto.getModello().getIDModello());



        db.insert(TABELLA_AUTO_UTENTE,
                null,
                values);

         db.close();
    }


    public void aggiungiUtente(Utente utente) {

        Log.d("aggiungiUtente", utente.toString());
         SQLiteDatabase db = this.getWritableDatabase();

         ContentValues values = new ContentValues();


        values.put("NomeU", utente.getNome());
        values.put("CognomeU", utente.getCognome());
        values.put("DataDiNascita", utente.getDataN());
        values.put("Email", utente.getEmail());
        //   values.put("Psw", utente.getPsw());



        db.insert(TABELLA_UTENTI,
                null,
                values);


        db.close();
    }

    /*
    public Utente prendiUtente(String email) {
        Utente utente = null;

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_UTENTE+ " WHERE Email= '" + email+"' ";;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list

        if (cursor.moveToFirst()) {
         do {
                utente = new Utente(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));


            } while (cursor.moveToNext());
     }


        return utente;
    }
    */


    public void aggiungiModello(Modello modello) {

        Log.d("aggiungiModello", modello.toString());
          SQLiteDatabase db = this.getWritableDatabase();

          ContentValues values = new ContentValues();

        values.put("IDModello", modello.getIDModello());
        values.put("Nome", modello.getNome());
        values.put("Segmento", modello.getSegmento());
        values.put("Alimentazione", modello.getAlimentazione());

        values.put("Cilindrata", modello.getCilindrata());
        values.put("KW", modello.getKw());
        values.put("Marca_id", modello.getMarca().getIDMarca());


        // 3. insert
        db.insert(TABELLA_MODELLI,
                null,
                values);
        db.close();


    }

    public void aggiungiMarca(Marca marca) {
        Log.d("aggiungiMarca", marca.toString());

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("IDMarca", marca.getIDMarca());
        values.put("Nome", marca.getNome());


        db.insert(TABELLA_MARCHE,
                null,
                values);


        db.close();
    }

    public void aggiungiProblemi(Problema problema) {
        Log.d("aggiungiproblema", problema.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("IDProblema", problema.getIDProblema());
        values.put("Descrizione", problema.getDescrizione());
        values.put("Targa", problema.getAuto().getTarga());

        db.insert(TABELLA_PROBLEMI,
                null,
                values);

        db.close();
    }


    public void aggiungiManutenzione(Manutenzione manutenzione) {
        Log.d("aggiungiManutenzione", manutenzione.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("IDManutenzione", manutenzione.getIDManutenzione());
        values.put("Descrizione", manutenzione.getDescrizione());
        values.put("Data", manutenzione.getData());
        values.put("Ordinaria", manutenzione.getOrdinaria());
        values.put("KmManutenzione", manutenzione.getKmManutenzione());
        values.put("Targa", manutenzione.getAuto().getTarga());

        db.insert(TABELLA_MANUTENZIONI,
                null,
                values);

        db.close();
    }



    public void aggiungiScadenza(Scadenza scadenza) {
        Log.d("aggiungiScadenza", scadenza.toString());

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("IDScadenza", scadenza.getIDScadenza());

        values.put("DataScadenza", scadenza.getDataScadenza());
        values.put("Descrizione", scadenza.getDescrizione());
        values.put("Targa", scadenza.getAuto().getTarga());



        db.insert(TABELLA_SCADENZE, // table
                null,
                values);


        db.close();
    }




    public List<AutoUtente> getAllAutoUtente() {
        List<AutoUtente> auto = new LinkedList<AutoUtente>();

        String email="'"+MainActivity.utenteLoggato.getEmail()+"'";
        String query =

        "SELECT * FROM "+ TABELLA_AUTO_UTENTE +" JOIN "+ TABELLA_MODELLI+ " ON Modelli_id=IDModello JOIN "+ TABELLA_MARCHE+ " ON Marca_id=IDMarca JOIN " + TABELLA_UTENTI+" ON Utenti_Email=Email WHERE AutoUtente.Utenti_Email="+email;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        AutoUtente autoUtente = null;
        if (cursor.moveToFirst()) {
            do {



                autoUtente = new AutoUtente(cursor.getString(0), cursor.getInt(1), cursor.getString(2), new Utente(cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18)), new Modello( cursor.getInt(5),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),new Marca( cursor.getInt(12),cursor.getString(14))),cursor.getInt(3));
                auto.add(autoUtente);
            } while (cursor.moveToNext());
        }
        for (AutoUtente a : auto
                ) {
            Log.d("getAllAutoUtente()", a.toString());
        }


        return auto;
    }


    public List<Utente> getAllUtenti() {
        List<Utente> utenti = new LinkedList<Utente>();


        String query = "SELECT  * FROM " + TABELLA_UTENTI;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        Utente utente = null;
        if (cursor.moveToFirst()) {
            do {
                utente = new Utente(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));



                utenti.add(utente);
            } while (cursor.moveToNext());
        }
        for (Utente u : utenti
                ) {
            Log.d("getAllUtenti()", u.toString());
        }

        return utenti;
    }



    public List<Modello> getAllModelli() {
        List<Modello> modelli = new LinkedList<Modello>();


        String query = "SELECT * FROM " + TABELLA_MODELLI  +" JOIN "+ TABELLA_MARCHE +" ON Marca_id = IDMarca ;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

         Modello modello = null;
        if (cursor.moveToFirst()) {

            do {

                modello = new Modello(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), new Marca(cursor.getInt(7), cursor.getString(8)));

                modelli.add(modello);
            } while (cursor.moveToNext());
        }
        for (Modello m : modelli
                ) {
            Log.d("getAllModelli()", m.toString());
        }


        return modelli;
    }

    public List<Scadenza> getAllScadenze() {
        List<Scadenza> scadenze = new LinkedList<Scadenza>();


        String query =  "SELECT * FROM "+TABELLA_SCADENZE+" NATURAL JOIN "+TABELLA_AUTO_UTENTE+" JOIN "+TABELLA_MODELLI +" ON Modelli_id=IDModello JOIN "+TABELLA_MARCHE +" ON Marca_id=IDMarca JOIN "+TABELLA_UTENTI +" ON Utenti_Email=Email ";




        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        Scadenza scadenza = null;
        if (cursor.moveToFirst()) {
            do {

                scadenza = new Scadenza(cursor.getInt(0), cursor.getString(1), cursor.getString(2), new AutoUtente(cursor.getString(3),cursor.getInt(4),cursor.getString(5),new Utente(cursor.getString(18),cursor.getString(19),cursor.getString(20),cursor.getString(21)),new Modello(cursor.getInt(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),new Marca(cursor.getInt(16),cursor.getString(17))),cursor.getInt(6)));

                scadenze.add(scadenza);
            } while (cursor.moveToNext());
        }
        for (Scadenza s : scadenze
                ) {
            Log.d("getAllScadenze()", s.toString());
        }


        return scadenze;
    }

    public List<Problema> getAllProblemi() {
        List<Problema> problemi = new LinkedList<Problema>();


        String query = "SELECT * FROM "+TABELLA_PROBLEMI+" NATURAL JOIN "+TABELLA_AUTO_UTENTE+" JOIN "+TABELLA_MODELLI+" ON Modelli_id=IDModello JOIN "+ TABELLA_MARCHE+" ON Marca_id=IDMarca JOIN "+TABELLA_UTENTI+" ON Utenti_Email=Email";


    //    String query = "SELECT * FROM "+TABELLA_PROBLEMI+"  JOIN "+TABELLA_AUTO_UTENTE+ " ON Problemi.Targa=AutoUtente.Targa ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        Problema problema = null;
        if (cursor.moveToFirst()) {
            do {
                problema = new Problema(cursor.getInt(0), cursor.getString(1), new AutoUtente(cursor.getString(2),cursor.getInt(3),cursor.getString(4),new Utente(cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19)), new Modello(cursor.getInt(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),new Marca(cursor.getInt(13),cursor.getString(15)) ),0));
                problemi.add(problema);

            } while (cursor.moveToNext());
        }
        for (Problema p : problemi
                ) {
            Log.d("getAllProblemi()",p.toString());
        }


        return problemi;
    }

    public List<Marca> getAllMarche() {
        List<Marca> marche = new LinkedList<Marca>();


        String query = "SELECT  * FROM " + TABELLA_MARCHE;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        Marca marca = null;
        if (cursor.moveToFirst()) {
            do {
                marca = new Marca(cursor.getString(1));
                marca.setIDMarca(cursor.getInt(0));



                marche.add(marca);
            } while (cursor.moveToNext());
        }
        for (Marca m : marche
                ) {
            Log.d("getAllMarche()", m.toString());
        }



        return marche;
    }


    public List<AutoUtente> getAllTarghe() {
        List<AutoUtente> auto = new LinkedList<AutoUtente>();

        String query = "SELECT  * FROM " + TABELLA_AUTO_UTENTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        AutoUtente autoUtente = null;
        if (cursor.moveToFirst()) {
            do {
                //autoUtente = new AutoUtente(cursor.getString(0),cursor.getInt(1),cursor.getString(2),0,cursor.getString(4),new Modello(0,null,null,null,null,null,null),false);
                //  int km, String annoImmatricolazione, int fotoAuto, String utente_email, Modello modello, boolean selected

            auto.add(autoUtente);
            } while (cursor.moveToNext());
        }

        Log.d("getAllMarche()", auto.toString());


        return auto;
    }


    public List<Manutenzione> getAllManutenzioni() {
        List<Manutenzione> manutenzioni = new LinkedList<Manutenzione>();

        String query =   "SELECT * FROM "+TABELLA_MANUTENZIONI +" NATURAL JOIN "+ TABELLA_AUTO_UTENTE+" JOIN "+TABELLA_MODELLI+ " ON Modelli_id=IDModello JOIN "+TABELLA_MARCHE +" ON Marca_id=IDMarca JOIN "+ TABELLA_UTENTI +" ON Utenti_Email=Email";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

         Manutenzione manutenzione = null;
        if (cursor.moveToFirst()) {

            do {

                    manutenzione = new Manutenzione(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4),new AutoUtente(cursor.getString(5),cursor.getInt(6),cursor.getString(7),new Utente(cursor.getString(20),cursor.getString(21),cursor.getString(22),cursor.getString(23)),new Modello(cursor.getInt(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16), new Marca(cursor.getInt(18),cursor.getString(19))),cursor.getInt(3) )) ;

                manutenzioni.add(manutenzione);
            } while (cursor.moveToNext());
        }
        for (Manutenzione m : manutenzioni
                ) {
            Log.d("getAllManutenzione()", m.toString());
        }

         return manutenzioni;
    }


    public void updateUtente(Utente utente){

            SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("NomeU",utente.getNome());
        cv.put("CognomeU",utente.getCognome());
        cv.put("DataDiNascita",utente.getDataN());

String email="'"+utente.getEmail()+"'";
            db.update(TABELLA_UTENTI, cv, "Email="+email, null);

    }


    //da testare
    public void updateAutoUtente(AutoUtente auto){

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put("KM",auto.getKm());
        cv.put("AnnoImmatricolazione",auto.getAnnoImmatricolazione());
        cv.put("Modelli_id",auto.getModello().getIDModello());


        String targa="'"+auto.getTarga()+"'";
        db.update(TABELLA_AUTO_UTENTE, cv, "Targa="+targa, null);

    }

    //da testare

    public void updateMantenzione(Manutenzione manutenzione){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Descrizione", manutenzione.getDescrizione());
        cv.put("Data", manutenzione.getData());
        cv.put("Ordinaria", manutenzione.getOrdinaria());
        cv.put("KmManutenzione", manutenzione.getKmManutenzione());
        cv.put("Targa", manutenzione.getAuto().getTarga());

         String id="'"+manutenzione.getIDManutenzione()+"'";
        db.update(TABELLA_MANUTENZIONI, cv, "IDManutenzione="+id, null);

    }




    //da testare

    public void updateProblema(Problema  problema){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Descrizione", problema.getDescrizione());
        cv.put("Targa", problema.getAuto().getTarga());

        String id="'"+problema.getIDProblema()+"'";
        db.update(TABELLA_PROBLEMI, cv, "IDProblema="+id, null);

    }

    //da testare

    public void updateScadenza(Scadenza  scadenza){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("DataScadenza", scadenza.getDataScadenza());
        cv.put("Descrizione", scadenza.getDescrizione());
        cv.put("Targa", scadenza.getAuto().getTarga());

        String id="'"+scadenza.getIDScadenza()+"'";
        db.update(TABELLA_SCADENZE, cv, "IDScadenza="+id, null);

    }
}