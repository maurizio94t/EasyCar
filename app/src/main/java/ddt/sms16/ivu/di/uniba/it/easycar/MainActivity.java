package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // URL to get data JSON
    private static String url = "http://t2j.no-ip.org/ddt/WebService.php";

    // JSON Node - Campi tabella AutoUtente
    public static final String TAG_AUTOUTENTE = "AutoUtente";
    public static final String TAG_AUTOUTENTE_TARGA = "Targa";
    public static final String TAG_AUTOUTENTE_KW = "KW";
    public static final String TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE = "AnnoImmatricolazione";
    public static final String TAG_AUTOUTENTE_FOTO_AUTO = "FotoAuto";
    public static final String TAG_AUTOUTENTE_UTENTI_EMAIL = "Utenti_Email";
    public static final String TAG_AUTOUTENTE_MODELLI_ID = "Modelli_id";

    // JSON Node - Campi tabella Manutenzione
    public static final String TAG_MANUTENZIONI = "Manutenzioni";
    public static final String TAG_MANUTENZIONI_ID_MANUTENZIONE = "IDManutenzione";
    public static final String TAG_MANUTENZIONI_DESCRIZIONE = "Descrizione";
    public static final String TAG_MANUTENZIONI_DATA = "Data";
    public static final String TAG_MANUTENZIONI_ORDINARIA = "Ordinaria";
    public static final String TAG_MANUTENZIONI_KM_MANUTENZIONE = "KmManutenzione";
    public static final String TAG_MANUTENZIONI_TARGA = "Targa";

    // JSON Node - Campi tabella Utenti
    public static final String TAG_UTENTI = "Utenti";
    public static final String TAG_UTENTI_NOME = "Nome";
    public static final String TAG_UTENTI_COGNOME = "Cognome";
    public static final String TAG_UTENTI_DATANASCITA = "DataDiNascita";
    public static final String TAG_UTENTI_EMAIL = "Email";

    // Hashmap per la ListView
    public static ArrayList<HashMap<String, String>> listaUtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetUsers().execute();
    }

    // AsynkTask
    private class GetUsers extends AsyncTask<Void, Void, Void> {
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
            // Creo un'istanza di WebRequest per effettuare una richiesta al server
            WebRequest webreq = new WebRequest();

            // Faccio una richiesta all'url dichiarato come variabile di classe e prendo la risposta
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POSTRequest);

            Log.d("Response: ", "> " + jsonStr);

            listaUtenti = ParseJSON(jsonStr);

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

            Intent intent = new Intent(MainActivity.this, BaseActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);

                ArrayList<HashMap<String, String>> listaAutoUtente = new ArrayList<HashMap<String, String>>();
                // Prelevo JSON Array node (AutoUtente)
                JSONArray autoUtenti = jsonObj.getJSONArray(TAG_AUTOUTENTE);
                // Ciclo tutte le auto degli utenti
                for (int i = 0; i < autoUtenti.length(); i++) {
                    JSONObject u = autoUtenti.getJSONObject(i);

                    String targa = u.getString(TAG_AUTOUTENTE_TARGA);
                    String kw = u.getString(TAG_AUTOUTENTE_KW);
                    String annoImmatricolazione = u.getString(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                    String foto = u.getString(TAG_AUTOUTENTE_FOTO_AUTO);
                    String email = u.getString(TAG_AUTOUTENTE_UTENTI_EMAIL);
                    String idModelli = u.getString(TAG_AUTOUTENTE_MODELLI_ID);


                    // hashmap per il singolo utente
                    HashMap<String, String> autoutente = new HashMap<String, String>();

                    // aggiungo tutti i campi dell'utente all'HashMap
                    autoutente.put(TAG_AUTOUTENTE_TARGA, targa);
                    autoutente.put(TAG_AUTOUTENTE_KW, kw);
                    autoutente.put(TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE, annoImmatricolazione);
                    autoutente.put(TAG_AUTOUTENTE_FOTO_AUTO, foto);
                    autoutente.put(TAG_AUTOUTENTE_UTENTI_EMAIL, email);
                    autoutente.put(TAG_AUTOUTENTE_MODELLI_ID, idModelli);


                    // aggiungo il singolo studente alla lista di studenti
                    listaAutoUtente.add(autoutente);
                }

                ArrayList<HashMap<String, String>> listaUtenti = new ArrayList<HashMap<String, String>>();
                // Prelevo JSON Array node (Utenti)
                JSONArray utenti = jsonObj.getJSONArray(TAG_UTENTI);
                // Ciclo tutti gli utenti
                for (int i = 0; i < utenti.length(); i++) {
                    JSONObject u = utenti.getJSONObject(i);

                    String nome = u.getString(TAG_UTENTI_NOME);
                    String cognome = u.getString(TAG_UTENTI_COGNOME);
                    String dataN = u.getString(TAG_UTENTI_DATANASCITA);
                    String email = u.getString(TAG_UTENTI_EMAIL);

                    // hashmap per il singolo utente
                    HashMap<String, String> utente = new HashMap<String, String>();

                    // aggiungo tutti i campi dell'utente all'HashMap
                    utente.put(TAG_UTENTI_NOME, nome);
                    utente.put(TAG_UTENTI_COGNOME, cognome);
                    utente.put(TAG_UTENTI_DATANASCITA, dataN);
                    utente.put(TAG_UTENTI_EMAIL, email);

                    // aggiungo il singolo studente alla lista di studenti
                    listaUtenti.add(utente);
                }
                return listaUtenti;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }
}
