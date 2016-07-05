package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
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
    private static final String TABELLA_UTENTE = "Utente";
    private static final String TABELLA_MODELLI = "Modelli";
    private static final String TABELLA_PROBLEMI = "Problemi";
    private static final String TABELLA_MANUTENZIONI = "Manutenzioni";


    private static final String KEY_NOME = "Nome";


    String CREA_TABELLA_UTENTE = "CREATE TABLE Utente (NomeU TEXT , CognomeU TEXT, DataDiNascita TEXT , Email TEXT PRIMARY KEY  )";
    String CREA_TABELLA_MARCHE = "CREATE TABLE Marche (IDMarca INTEGER   PRIMARY KEY  , Nome TEXT)";
    String CREA_TABELLA_MODELLI = "CREATE TABLE  Modelli ( IDModello INTEGER  PRIMARY KEY      , Nome TEXT  , Segmento TEXT  , Alimentazione TEXT  , Cilindrata TEXT , KW TEXT  , Marca_id INTEGER  , FOREIGN KEY (`Marca_id`) REFERENCES  `Marche` (`IDMarca`) ON DELETE NO ACTION ON UPDATE NO ACTION);";
    String CREA_TABELLA_AUTOUTENTE = "CREATE TABLE AutoUtente ( Targa TEXT  PRIMARY KEY, KM INTEGER , AnnoImmatricolazione TEXT  , FotoAuto BLOB , Utenti_Email TEXT  , Modelli_id INTEGER  , FOREIGN KEY (`Utenti_Email`) REFERENCES  `Utenti` (`Email`) ON DELETE NO ACTION ON UPDATE NO ACTION, FOREIGN KEY (`Modelli_id`) REFERENCES  `Modelli` (`IDModello`) ON DELETE NO ACTION ON UPDATE NO ACTION);";
    String CREA_TABELLA_PROBLEMI = "CREATE TABLE Problemi ( IDProblemi INTEGER   PRIMARY KEY     , Descrizione TEXT, Targa TEXT, FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`) ON DELETE NO ACTION ON UPDATE NO ACTION);";
    String CREA_TABELLA_MANUTENZIONI = "CREATE TABLE Manutenzioni ( IDManutenzione INTEGER  PRIMARY KEY   , Descrizione TEXT  ,Data TEXT  ,Ordinaria INTEGER,  KmManutenzione TEXT , Targa TEXT , FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`)ON DELETE NO ACTION ON UPDATE NO ACTION);";
    String CREA_TABELLA_SCADENZE = "CREATE TABLE Scadenze ( IDScadenza INTEGER PRIMARY KEY     , Descrizione TEXT, DataScadenza TEXT, Targa TEXT, FOREIGN KEY (`Targa`) REFERENCES `AutoUtente` (`Targa`) ON DELETE NO ACTION ON UPDATE NO ACTION);";


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
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS Marche");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */


    public void aggiungiAutoUtente(AutoUtente auto) {
        Log.d("aggiungiAutoUtente", auto.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("Targa", auto.getTarga()); // get title
        values.put("KM", auto.getKm()); // get title
        values.put("AnnoImmatricolazione", auto.getAnnoImmatricolazione()); // get title
        values.put("FotoAuto",  auto.getFotoAuto());
        values.put("Utenti_Email", auto.getUtente().getEmail()); // get title
        values.put("Modelli_id", auto.getModello().getIDModello()); // get title


        // 3. insert
        db.insert(TABELLA_AUTO_UTENTE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    public void aggiungiUtente(Utente utente) {

        Log.d("aggiungiUtente", utente.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();


        values.put("NomeU", utente.getNome()); // get title
        values.put("CognomeU", utente.getCognome()); // get title
        values.put("DataDiNascita", utente.getDataN()); // get title
        values.put("Email", utente.getEmail()); // get title
     //   values.put("Psw", utente.getPsw()); // get title



        // 3. insert
        db.insert(TABELLA_UTENTE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

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


    public void aggiungiModello(Modello modello) {

        Log.d("aggiungiModello", modello.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();


        values.put("Nome", modello.getNome()); // get title
        values.put("Segmento", modello.getSegmento()); // get title
        values.put("Alimentazione", modello.getAlimentazione()); // get title
        values.put("KW", modello.getKw());
        values.put("Marca_id", modello.getMarca().getIDMarca());


        // 3. insert
        db.insert(TABELLA_MODELLI, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();


    }

    public void aggiungiMarca(Marca marca) {
        Log.d("aggiungiMarca", marca.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("IDMarca", marca.getIDMarca()); // get title
        values.put(KEY_NOME, marca.getNome()); // get title


        // 3. insert
        db.insert(TABELLA_MARCHE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void aggiungiProblemi(Problema problema) {
        Log.d("aggiungiproblema", problema.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("IDProblemi", problema.getIDProblemi());

        values.put("Descrizione", problema.getDescrizione()); // get title
        values.put("Targa", problema.getAuto().getTarga()); // get title


        // 3. insert
        db.insert(TABELLA_PROBLEMI, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    public void aggiungiManutenzione(Manutenzione manutenzione) {
        Log.d("aggiungiManutenzione", manutenzione.toString());


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("IDManutenzione", manutenzione.getIDManutenzione()); // get title

        values.put("Descrizione", manutenzione.getDescrizione()); // get title
        values.put("Data", manutenzione.getData()); // get title
        values.put("Ordinaria", manutenzione.getOrdinaria()); // get title
        values.put("KmManutenzione", manutenzione.getKmManutenzione()); // get title
        values.put("Targa", manutenzione.getAuto().getTarga()); // get title


        // 3. insert
        db.insert(TABELLA_MANUTENZIONI, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void aggiungiFotoUtente(Bitmap foto) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("FotoAuto", foto.toString()); // get title


        // 3. insert
        db.insert(TABELLA_AUTO_UTENTE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void aggiungiScadenza(Scadenza scadenza) {
        Log.d("aggiungiScadenza", scadenza.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("IDScadenza", scadenza.getIDScadenza());

        values.put("DataScadenza", scadenza.getDataScadenza());
        values.put("Descrizione", scadenza.getDescrizione()); // get title
        values.put("Targa", scadenza.getAuto().getTarga()); // get title


        // 3. insert
        db.insert(TABELLA_SCADENZE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    public List<AutoUtente> getAllAutoUtente() {
        List<AutoUtente> auto = new LinkedList<AutoUtente>();


        String query = "SELECT  * FROM " + TABELLA_AUTO_UTENTE;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        AutoUtente autoUtente = null;
        if (cursor.moveToFirst()) {
            do {
          

                autoUtente = new AutoUtente(cursor.getString(0), cursor.getInt(1), cursor.getString(2),cursor.getBlob(3) , new Utente(cursor.getString(3)), new Modello(cursor.getInt(4)), 0);

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

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_UTENTE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Utente utente = null;
        if (cursor.moveToFirst()) {
            do {
                utente = new Utente(cursor.getString(0), cursor.getString(1), cursor.getString(3), 0, cursor.getString(3));


                // Add book to books
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

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_MODELLI;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Modello modello = null;
        if (cursor.moveToFirst()) {
            do {
                modello = new Modello(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), new Marca(cursor.getInt(6)));

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

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_SCADENZE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Scadenza scadenza = null;
        if (cursor.moveToFirst()) {
            do {
                scadenza = new Scadenza(cursor.getInt(0), cursor.getString(1), cursor.getString(2), new AutoUtente(cursor.getString(3)));

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

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_PROBLEMI;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Problema problema = null;
        if (cursor.moveToFirst()) {
            do {
                problema = new Problema(cursor.getInt(0), cursor.getString(1), new AutoUtente(cursor.getString(2)));

                problemi.add(problema);
            } while (cursor.moveToNext());
        }
        for (Problema p : problemi
                ) {
            Log.d("getAllProblemi()", p.toString());
        }


        return problemi;
    }

    public List<Marca> getAllMarche() {
        List<Marca> marche = new LinkedList<Marca>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_MARCHE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Marca marca = null;
        if (cursor.moveToFirst()) {
            do {
                marca = new Marca(cursor.getString(1));
                marca.setIDMarca(cursor.getInt(0));


                // Add book to books
                marche.add(marca);
            } while (cursor.moveToNext());
        }
        for (Marca m : marche
                ) {
            Log.d("getAllMarche()", m.toString());
        }


        // return books
        return marche;
    }


    public List<AutoUtente> getAllTarghe() {
        List<AutoUtente> auto = new LinkedList<AutoUtente>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABELLA_AUTO_UTENTE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        AutoUtente autoUtente = null;
        if (cursor.moveToFirst()) {
            do {
                //autoUtente = new AutoUtente(cursor.getString(0),cursor.getInt(1),cursor.getString(2),0,cursor.getString(4),new Modello(0,null,null,null,null,null,null),false);
                //  int km, String annoImmatricolazione, int fotoAuto, String utente_email, Modello modello, boolean selected


                // Add book to books
                auto.add(autoUtente);
            } while (cursor.moveToNext());
        }

        Log.d("getAllMarche()", auto.toString());

        // return books
        return auto;
    }


}