package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;


public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "EasyCar";



    String CREA_TABELLA_MARCHE ="CREATE TABLE Marche (IDMarca INTEGER   PRIMARY KEY AUTOINCREMENT, Nome TEXT)";





String CREA_TABELLA_MODELLI="CREATE TABLE  Modelli (\n" +
        "  `IDModello` INTEGER  PRIMARY KEY  AUTOINCREMENT   ,\n" +
        "  `Nome` TEXT  ,\n" +
        "  `Segmento` TEXT  ,\n" +
        "  `Alimentazione` TEXT  ,\n" +
        "  `Cilindrata` TEXT ,\n" +
        "  `KW` TEXT  ,\n" +
        "  `Marca_id` INTEGER  ,\n" +
        "    FOREIGN KEY (`Marca_id`)\n" +
        "    REFERENCES  `Marche` (`IDMarca`)\n" +
        "    ON DELETE NO ACTION\n" +
        "    ON UPDATE NO ACTION);";










    String CREA_TABELLA_AUTOUTENTE="CREATE TABLE `AutoUtente` (\n" +
            "  `Targa` TEXT  PRIMARY KEY ,\n" +
            "  `KM` INTEGER  ,\n" +
            "  `AnnoImmatricolazione` TEXT  ,\n" +
            "  `FotoAuto` BLOB  ,\n" +
            "  `Utenti_Email` TEXT  ,\n" +
            "  `Modelli_id` INTEGER  ,\n" +

             "    FOREIGN KEY (`Utenti_Email`)\n" +
            "    REFERENCES  `Utenti` (`Email`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "    FOREIGN KEY (`Modelli_id`)\n" +
            "    REFERENCES  `Modelli` (`IDModello`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";






    String CREA_TABELLA_MANUTENZIONI="CREATE TABLE `Manutenzioni` (\n" +
            "  `IDManutenzione` INTEGER  PRIMARY KEY AUTOINCREMENT ,\n" +
            "  `Descrizione` TEXT  ,\n" +
            "  `Data` DATETIME  ,\n" +
            "  `Ordinaria` INTEGER ,\n" +
            "  `KmManutenzione` TEXT ,\n" +
            "  `Targa` TEXT  ,\n" +
              "    FOREIGN KEY (`Targa`)\n" +
            "    REFERENCES `AutoUtente` (`Targa`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";






    String CREA_TABELLA_SCADENZE="CREATE TABLE `Scadenze` (\n" +
            "  `IDScadenza` INTEGER PRIMARY KEY  AUTOINCREMENT  ,\n" +
            "  `Descrizione` TEXT,\n" +
            "  `DataScadenza` TEXT,\n" +
            "  `Targa` TEXT,\n" +

             "    FOREIGN KEY (`Targa`)\n" +
            "    REFERENCES `AutoUtente` (`Targa`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";








    String CREA_TABELLA_PROBLEMI="\n" +
            "CREATE TABLE `Problemi` (\n" +
            "  `IDProblemi` INTEGER   PRIMARY KEY  AUTOINCREMENT  ,\n" +
            "  `Descrizione`TEXT ,\n" +
            "  `Targa` TEXT,\n" +
             "    FOREIGN KEY (`Targa`)\n" +
            "    REFERENCES `AutoUtente` (`Targa`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";







    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


   db.execSQL(CREA_TABELLA_MARCHE);
      db.execSQL(CREA_TABELLA_MODELLI);
        db.execSQL(CREA_TABELLA_AUTOUTENTE);
        db.execSQL(CREA_TABELLA_MANUTENZIONI);
        db.execSQL(CREA_TABELLA_SCADENZE);
        db.execSQL(CREA_TABELLA_PROBLEMI);


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


    private static final String TABELLA_MARCHE = "Marche";


    private static final String KEY_NOME = "Nome";






    public void aggiungiMarca(Marca marca){
        Log.d("aggiungiMarca", marca.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(KEY_NOME, marca.getNome()); // get title



        // 3. insert
        db.insert(TABELLA_MARCHE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }


    // Get All Books
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

        Log.d("getAllMarche()", marche.toString());

        // return books
        return marche;
    }




}