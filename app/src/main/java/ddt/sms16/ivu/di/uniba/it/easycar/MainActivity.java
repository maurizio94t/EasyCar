package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;

/**
 * Created by Maurizio on 01/06/16.
 */
public class MainActivity extends AppCompatActivity {

    // URL to get data JSON
    private static final String url = "http://t2j.no-ip.org/ddt/WebService.php";
    private static final String urlOperations = "http://t2j.no-ip.org/ddt/WebServiceOperations.php";
    // JSON Node - Campo stato login
    public static final String TAG_STATO = "Stato";

    // JSON Node - Campi tabella AutoUtente
    public static final String TAG_AUTOUTENTE = "AutoUtente";
    public static final String TAG_AUTOUTENTE_TARGA = "Targa";
    public static final String TAG_AUTOUTENTE_KM = "KM";
    public static final String TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE = "AnnoImmatricolazione";
    public static final String TAG_AUTOUTENTE_FOTO_AUTO = "FotoAuto";
    public static final String TAG_AUTOUTENTE_MODELLO = "Modello";
    public static final String TAG_AUTOUTENTE_SELECTED = "Selected";

    // JSON Node - Campi tabella Manutenzioni
    public static final String TAG_MANUTENZIONI = "Manutenzioni";
    public static final String TAG_MANUTENZIONI_ID_MANUTENZIONE = "IDManutenzione";
    public static final String TAG_MANUTENZIONI_DESCRIZIONE = "Descrizione";
    public static final String TAG_MANUTENZIONI_DATA = "Data";
    public static final String TAG_MANUTENZIONI_ORDINARIA = "Ordinaria";
    public static final String TAG_MANUTENZIONI_KM_MANUTENZIONE = "KmManutenzione";
    public static final String TAG_MANUTENZIONI_TARGA = "Targa";
    public static final String TAG_MANUTENZIONI_VEICOLO = "Veicolo";

    // JSON Node - Campi tabella Modelli
    public static final String TAG_MODELLI = "Modelli";
    public static final String TAG_MODELLI_IDMODELLO = "IDModello";
    public static final String TAG_MODELLI_NOME = "Nome";
    public static final String TAG_MODELLI_SEGMENTO = "Segmento";
    public static final String TAG_MODELLI_ALIMENTAZIONE = "Alimentazione";
    public static final String TAG_MODELLI_CILINDRATA = "Cilindrata";
    public static final String TAG_MODELLI_KW = "KW";
    public static final String TAG_MODELLI_MARCA = "Marca";

    // JSON Node - Campi tabella Marca
    public static final String TAG_MARCHE = "Marche";
    public static final String TAG_MARCHE_IDMARCA = "IDMarca";
    public static final String TAG_MARCHE_NOME = "Nome";

    // JSON Node - Campi tabella Problemi
    public static final String TAG_PROBLEMI = "Problemi";
    public static final String TAG_PROBLEMI_IDPROBLEMA = "IDProblemi";
    public static final String TAG_PROBLEMI_DESCRIZIONE = "Descrizione";
    public static final String TAG_PROBLEMI_VEICOLO = "Veicolo";

    // JSON Node - Campi tabella Scadenze
    public static final String TAG_SCADENZE = "Scadenze";
    public static final String TAG_SCADENZE_IDSCADENZA = "IDScadenza";
    public static final String TAG_SCADENZE_DESCRIZIONE = "Descrizione";
    public static final String TAG_SCADENZE_DATASCADENZA = "DataScadenza";
    public static final String TAG_SCADENZE_VEICOLO = "Veicolo";

    // JSON Node - Campi tabella Utenti
    public static final String TAG_UTENTE = "Utente";
    public static final String TAG_UTENTE_NOME = "Nome";
    public static final String TAG_UTENTE_COGNOME = "Cognome";
    public static final String TAG_UTENTE_DATANASCITA = "DataDiNascita";
    public static final String TAG_UTENTE_FOTO = "Foto";
    public static final String TAG_UTENTE_EMAIL = "Email";

    // Hashmap per la ListView
    public static ArrayList<AutoUtente> listaAutoUtente;
    public static ArrayList<Manutenzione> listaManutenzioni;
    public static ArrayList<Modello> listaModelli;
    public static ArrayList<Marca> listaMarche;
    public static ArrayList<Problema> listaProblemi;
    public static ArrayList<Scadenza> listaScadenze;
    public static Utente utente;

    public static MySQLiteHelper mySQLiteHelper;
    public static List<AutoUtente> listAutoUtenteLocal;
    public static List<Marca> listMarcaLocal;

    public static boolean stato;
    private String jsonStr;
    private EditText mEditTxtEmail;
    private EditText mEditTxtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaAutoUtente = new ArrayList<AutoUtente>();
        listaManutenzioni = new ArrayList<Manutenzione>();
        listaModelli = new ArrayList<Modello>();
        listaMarche = new ArrayList<Marca>();
        listaProblemi = new ArrayList<Problema>();
        listaScadenze = new ArrayList<Scadenza>();

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());

        mEditTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        mEditTxtPsw = (EditText) findViewById(R.id.editTxtPsw);

        Button btnAccedi = (Button) findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest myReq = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                jsonStr = response;
                                Log.d("Response", "> OK FETCH DB");
                                //new GetData(mEditTxtEmail.getText().toString(), mEditTxtPsw.getText().toString()).execute();
                                new GetData().execute();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Response", "> That didn't work!");
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", "maur_izzio@live.it");
                        params.put("psw", "prova");
                        return params;
                    };
                };
                queue.add(myReq);


                // prova
                StringRequest myReq1 = new StringRequest(Request.Method.POST,
                        urlOperations,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("RespOperations", "> " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("RespOperations", "> NON FUNZIONA!");
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        /*
                        //insert into AutoUtente
                        params.put("operation", "c");
                        params.put("table", "AutoUtente");

                        params.put("targa", "AA000BA");
                        params.put("km", "12099");
                        params.put("anno", "1900");
                        params.put("foto", "");
                        params.put("utente", "enrico@gmail.com");
                        params.put("modello", "1315");
                        */

                        /*
                        //insert into Manutenzioni
                        params.put("operation", "c");
                        params.put("table", "Manutenzioni");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVE");
                        params.put("data", "20160629");
                        params.put("ordinaria", "false");
                        params.put("km", "5000");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Problemi
                        params.put("operation", "c");
                        params.put("table", "Problemi");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVISSIMO");
                        params.put("targa", "AA000BA");
                        */



                        return params;
                    }

                    ;
                };
                queue.add(myReq1);
            }
        });


    /*    Button bottonePrendiFoto = (Button) findViewById(R.id.bottonPrendiFoto);
        bottonePrendiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent prendiFoto = new Intent(MainActivity.this, PrendiFoto.class);
                startActivity(prendiFoto);
            }
        });*/


    }

    // AsynkTask
    private class GetData extends AsyncTask<Void, Void, Void> {
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Mostro la progress loading Dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Caricamento in corso...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Response", "> " + jsonStr);

            ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
            /**
             * Updating received data from JSON into ListView
             * */
            if (stato) {
                Intent intentBaseActivity = new Intent(MainActivity.this, BaseActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intentBaseActivity);
                finish();
            } else {
                View parentLayout = findViewById(R.id.root_view);
                Snackbar snackbar = Snackbar.make(parentLayout, "Dati non corretti! Riprova..", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    private boolean ParseJSON(String json) {
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);

                stato = jsonObj.getBoolean(TAG_STATO);

                if (!stato) {
                    return false;
                }

                // Prelevo JSON Array node (AutoUtente)
                JSONArray autoUtentiJSON = jsonObj.getJSONArray(TAG_AUTOUTENTE);
                // Ciclo tutte le auto degli utenti
                for (int i = 0; i < autoUtentiJSON.length(); i++) {
                    JSONObject autoUtentiObj = autoUtentiJSON.getJSONObject(i);

                    String targa = autoUtentiObj.getString(TAG_AUTOUTENTE_TARGA);
                    int km = autoUtentiObj.getInt(TAG_AUTOUTENTE_KM);
                    String annoImmatricolazione = autoUtentiObj.getString(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                    //String foto = autoUtentiObj.getString(TAG_AUTOUTENTE_FOTO_AUTO);

                    //get Utente
                    JSONObject utenteObj = autoUtentiObj.getJSONObject(TAG_UTENTE);

                    String nome = utenteObj.getString(TAG_UTENTE_NOME);
                    String cognome = utenteObj.getString(TAG_UTENTE_COGNOME);
                    String dataN = utenteObj.getString(TAG_UTENTE_DATANASCITA);
                    //String foto = u.getString(TAG_UTENTE_FOTO);
                    String email = utenteObj.getString(TAG_UTENTE_EMAIL);

                    // creo l'oggetto del singolo Utente
                    Utente utenteAuto = new Utente(nome, cognome, dataN, R.drawable.ic_menu_gallery, email);

                    int selected = autoUtentiObj.getInt(TAG_AUTOUTENTE_SELECTED);

                    //get Modello
                    JSONObject modelloObj = autoUtentiObj.getJSONObject(TAG_AUTOUTENTE_MODELLO);

                    int idModello = modelloObj.getInt(TAG_MODELLI_IDMODELLO);
                    String nomeModello = modelloObj.getString(TAG_MODELLI_NOME);
                    String segmento = modelloObj.getString(TAG_MODELLI_SEGMENTO);
                    String alimentazione = modelloObj.getString(TAG_MODELLI_ALIMENTAZIONE);
                    String cilindrata = modelloObj.getString(TAG_MODELLI_CILINDRATA);
                    String kw = modelloObj.getString(TAG_MODELLI_KW);

                    //get Marca
                    JSONObject marcaObj = modelloObj.getJSONObject(TAG_MODELLI_MARCA);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);
                    Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, utenteAuto, modello, selected);

                    // aggiungo la singola auto alla lista di auto dell'utente
                    listaAutoUtente.add(autoutente);
                }

                // Prelevo JSON Array node (Manutenzioni)
                JSONArray manutenzioni = jsonObj.getJSONArray(TAG_MANUTENZIONI);
                // Ciclo tutte le manutenzioni dell'utente
                for (int i = 0; i < manutenzioni.length(); i++) {
                    JSONObject manutenzioniObj = manutenzioni.getJSONObject(i);

                    int id = manutenzioniObj.getInt(TAG_MANUTENZIONI_ID_MANUTENZIONE);
                    String desc = manutenzioniObj.getString(TAG_MANUTENZIONI_DESCRIZIONE);
                    String dataS = manutenzioniObj.getString(TAG_MANUTENZIONI_DATA);
                    int ord = manutenzioniObj.getInt(TAG_MANUTENZIONI_ORDINARIA);
                    String kmManut = manutenzioniObj.getString(TAG_MANUTENZIONI_KM_MANUTENZIONE);

                    //get Veicolo
                    JSONObject veicoloObj = manutenzioniObj.getJSONObject(TAG_MANUTENZIONI_VEICOLO);

                    String targa = veicoloObj.getString(TAG_AUTOUTENTE_TARGA);
                    int km = veicoloObj.getInt(TAG_AUTOUTENTE_KM);
                    String annoImmatricolazione = veicoloObj.getString(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                    //String foto = veicoloObj.getString(TAG_AUTOUTENTE_FOTO_AUTO);

                    //get Utente
                    JSONObject utenteObj = veicoloObj.getJSONObject(TAG_UTENTE);

                    String nome = utenteObj.getString(TAG_UTENTE_NOME);
                    String cognome = utenteObj.getString(TAG_UTENTE_COGNOME);
                    String dataN = utenteObj.getString(TAG_UTENTE_DATANASCITA);
                    //String foto = u.getString(TAG_UTENTE_FOTO);
                    String email = utenteObj.getString(TAG_UTENTE_EMAIL);

                    // creo l'oggetto del singolo Utente
                    Utente utenteAuto = new Utente(nome, cognome, dataN, R.drawable.ic_menu_gallery, email);

                    int selected = veicoloObj.getInt(TAG_AUTOUTENTE_SELECTED);

                    //get Modello
                    JSONObject modelloObj = veicoloObj.getJSONObject(TAG_AUTOUTENTE_MODELLO);

                    int idModello = modelloObj.getInt(TAG_MODELLI_IDMODELLO);
                    String nomeModello = modelloObj.getString(TAG_MODELLI_NOME);
                    String segmento = modelloObj.getString(TAG_MODELLI_SEGMENTO);
                    String alimentazione = modelloObj.getString(TAG_MODELLI_ALIMENTAZIONE);
                    String cilindrata = modelloObj.getString(TAG_MODELLI_CILINDRATA);
                    String kw = modelloObj.getString(TAG_MODELLI_KW);

                    //get Marca
                    JSONObject marcaObj = modelloObj.getJSONObject(TAG_MODELLI_MARCA);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);
                    Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, utenteAuto, modello, selected);
                    Manutenzione manutenzione = new Manutenzione(id, desc, dataS, ord, kmManut, autoutente);

                    // aggiungo la singola manutenzione alla lista di manutenzioni
                    listaManutenzioni.add(manutenzione);
                }

                // Prelevo JSON Array node (Modelli)
                JSONArray modelli = jsonObj.getJSONArray(TAG_MODELLI);
                // Ciclo tutti i modelli delle auto
                for (int i = 0; i < modelli.length(); i++) {
                    JSONObject modelloObj = modelli.getJSONObject(i);

                    int idModello = modelloObj.getInt(TAG_MODELLI_IDMODELLO);
                    String nomeModello = modelloObj.getString(TAG_MODELLI_NOME);
                    String segmento = modelloObj.getString(TAG_MODELLI_SEGMENTO);
                    String alimentazione = modelloObj.getString(TAG_MODELLI_ALIMENTAZIONE);
                    String cilindrata = modelloObj.getString(TAG_MODELLI_CILINDRATA);
                    String kw = modelloObj.getString(TAG_MODELLI_KW);

                    //get Marca
                    JSONObject marcaObj = modelloObj.getJSONObject(TAG_MODELLI_MARCA);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);
                    Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);

                    // aggiungo la singola manutenzione alla lista di manutenzioni
                    listaModelli.add(modello);
                }

                // Prelevo JSON Array node (Marche)
                JSONArray marche = jsonObj.getJSONArray(TAG_MARCHE);
                // Ciclo tutte le marche delle auto
                for (int i = 0; i < marche.length(); i++) {
                    JSONObject marcaObj = marche.getJSONObject(i);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);

                    // aggiungo la singola manutenzione alla lista di manutenzioni
                    listaMarche.add(marca);
                }

                // Prelevo JSON Array node (Problemi)
                JSONArray problemiJSON = jsonObj.getJSONArray(TAG_PROBLEMI);
                // Ciclo tutte le auto degli utenti
                for (int i = 0; i < problemiJSON.length(); i++) {
                    JSONObject problemaObj = problemiJSON.getJSONObject(i);

                    int idProblema = problemaObj.getInt(TAG_PROBLEMI_IDPROBLEMA);
                    String descrizioneProblema = problemaObj.getString(TAG_PROBLEMI_DESCRIZIONE);


                    JSONObject autoUtentiObj = problemaObj.getJSONObject(TAG_PROBLEMI_VEICOLO);

                    String targa = autoUtentiObj.getString(TAG_AUTOUTENTE_TARGA);
                    int km = autoUtentiObj.getInt(TAG_AUTOUTENTE_KM);
                    String annoImmatricolazione = autoUtentiObj.getString(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                    //String foto = autoUtentiObj.getString(TAG_AUTOUTENTE_FOTO_AUTO);

                    //get Utente
                    JSONObject utenteObj = autoUtentiObj.getJSONObject(TAG_UTENTE);

                    String nome = utenteObj.getString(TAG_UTENTE_NOME);
                    String cognome = utenteObj.getString(TAG_UTENTE_COGNOME);
                    String dataN = utenteObj.getString(TAG_UTENTE_DATANASCITA);
                    //String foto = u.getString(TAG_UTENTE_FOTO);
                    String email = utenteObj.getString(TAG_UTENTE_EMAIL);

                    // creo l'oggetto del singolo Utente
                    Utente utenteAuto = new Utente(nome, cognome, dataN, R.drawable.ic_menu_gallery, email);

                    int selected = autoUtentiObj.getInt(TAG_AUTOUTENTE_SELECTED);

                    //get Modello
                    JSONObject modelloObj = autoUtentiObj.getJSONObject(TAG_AUTOUTENTE_MODELLO);

                    int idModello = modelloObj.getInt(TAG_MODELLI_IDMODELLO);
                    String nomeModello = modelloObj.getString(TAG_MODELLI_NOME);
                    String segmento = modelloObj.getString(TAG_MODELLI_SEGMENTO);
                    String alimentazione = modelloObj.getString(TAG_MODELLI_ALIMENTAZIONE);
                    String cilindrata = modelloObj.getString(TAG_MODELLI_CILINDRATA);
                    String kw = modelloObj.getString(TAG_MODELLI_KW);

                    //get Marca
                    JSONObject marcaObj = modelloObj.getJSONObject(TAG_MODELLI_MARCA);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);
                    Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, utenteAuto, modello, selected);
                    Problema problema = new Problema(idProblema, descrizioneProblema, autoutente);

                    // aggiungo il singolo problema alla lista dei problemi
                    listaProblemi.add(problema);
                }

                // Prelevo JSON Array node (Scadenze)
                JSONArray scadenzeJSON = jsonObj.getJSONArray(TAG_SCADENZE);
                // Ciclo tutte le auto degli utenti
                for (int i = 0; i < scadenzeJSON.length(); i++) {
                    JSONObject scadenzaObj = scadenzeJSON.getJSONObject(i);

                    int idScadenza = scadenzaObj.getInt(TAG_SCADENZE_IDSCADENZA);
                    String descrizioneScadenza = scadenzaObj.getString(TAG_SCADENZE_DESCRIZIONE);
                    String dataScadenza = scadenzaObj.getString(TAG_SCADENZE_DATASCADENZA);


                    JSONObject autoUtentiObj = scadenzaObj.getJSONObject(TAG_PROBLEMI_VEICOLO);

                    String targa = autoUtentiObj.getString(TAG_AUTOUTENTE_TARGA);
                    int km = autoUtentiObj.getInt(TAG_AUTOUTENTE_KM);
                    String annoImmatricolazione = autoUtentiObj.getString(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                    //String foto = autoUtentiObj.getString(TAG_AUTOUTENTE_FOTO_AUTO);

                    //get Utente
                    JSONObject utenteObj = autoUtentiObj.getJSONObject(TAG_UTENTE);

                    String nome = utenteObj.getString(TAG_UTENTE_NOME);
                    String cognome = utenteObj.getString(TAG_UTENTE_COGNOME);
                    String dataN = utenteObj.getString(TAG_UTENTE_DATANASCITA);
                    //String foto = u.getString(TAG_UTENTE_FOTO);
                    String email = utenteObj.getString(TAG_UTENTE_EMAIL);

                    // creo l'oggetto del singolo Utente
                    Utente utenteAuto = new Utente(nome, cognome, dataN, R.drawable.ic_menu_gallery, email);

                    int selected = autoUtentiObj.getInt(TAG_AUTOUTENTE_SELECTED);

                    //get Modello
                    JSONObject modelloObj = autoUtentiObj.getJSONObject(TAG_AUTOUTENTE_MODELLO);

                    int idModello = modelloObj.getInt(TAG_MODELLI_IDMODELLO);
                    String nomeModello = modelloObj.getString(TAG_MODELLI_NOME);
                    String segmento = modelloObj.getString(TAG_MODELLI_SEGMENTO);
                    String alimentazione = modelloObj.getString(TAG_MODELLI_ALIMENTAZIONE);
                    String cilindrata = modelloObj.getString(TAG_MODELLI_CILINDRATA);
                    String kw = modelloObj.getString(TAG_MODELLI_KW);

                    //get Marca
                    JSONObject marcaObj = modelloObj.getJSONObject(TAG_MODELLI_MARCA);

                    int idMarca = marcaObj.getInt(TAG_MARCHE_IDMARCA);
                    String nomeMarca = marcaObj.getString(TAG_MARCHE_NOME);

                    //costruisco gli oggetti
                    Marca marca = new Marca(idMarca, nomeMarca);
                    Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, utenteAuto, modello, selected);
                    Scadenza scadenza = new Scadenza(idScadenza, descrizioneScadenza, dataScadenza, autoutente);

                    // aggiungo il singolo problema alla lista dei problemi
                    listaScadenze.add(scadenza);
                }

                // Prelevo JSON Array node (Utenti)
                JSONArray ut = jsonObj.getJSONArray(TAG_UTENTE);
                // Ciclo tutti gli utenti
                for (int i = 0; i < ut.length(); i++) {
                    JSONObject u = ut.getJSONObject(i);

                    String nome = u.getString(TAG_UTENTE_NOME);
                    String cognome = u.getString(TAG_UTENTE_COGNOME);
                    String dataN = u.getString(TAG_UTENTE_DATANASCITA);
                    //String foto = u.getString(TAG_UTENTE_FOTO);
                    String email = u.getString(TAG_UTENTE_EMAIL);

                    // creo l'oggetto del singolo Utente
                    utente = new Utente(nome, cognome, dataN, R.drawable.ic_menu_gallery, email);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return false;
        }
    }

}
