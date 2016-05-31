package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    // URL to get data JSON
    private static String url = "http://t2j.no-ip.org/ddt/WebService.php";

    // JSON Node names
    private static final String TAG_UTENTI = "utenti";
    private static final String TAG_UTENTE_NOME = "nome";
    private static final String TAG_UTENTE_COGNOME = "cognome";
    private static final String TAG_UTENTE_DATANASCITA = "dataN";
    private static final String TAG_UTENTE_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetUsers().execute();

    }

    private class GetUsers extends AsyncTask<Void, Void, Void> {

        // Hashmap per la ListView
        ArrayList<HashMap<String, String>> listaUtenti;
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Mostro la progress loading Dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Please wait...");
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
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, listaUtenti,
                    R.layout.list_item,
                    new String[]{TAG_UTENTE_NOME, TAG_UTENTE_COGNOME, TAG_UTENTE_EMAIL},
                    new int[]{R.id.nome, R.id.cognome, R.id.email});

            setListAdapter(adapter);

        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap per la ListView
                ArrayList<HashMap<String, String>> listaUtenti = new ArrayList<HashMap<String, String>>();
                JSONObject jsonObj = new JSONObject(json);

                // Prelevo JSON Array node
                JSONArray utenti = jsonObj.getJSONArray(TAG_UTENTI);

                // Ciclo tutti gli utenti
                for (int i = 0; i < utenti.length(); i++) {
                    JSONObject u = utenti.getJSONObject(i);

                    String nome = u.getString(TAG_UTENTE_NOME);
                    String cognome = u.getString(TAG_UTENTE_COGNOME);
                    String dataN = u.getString(TAG_UTENTE_DATANASCITA);
                    String email = u.getString(TAG_UTENTE_EMAIL);

                    // hashmap per il singolo utente
                    HashMap<String, String> utente = new HashMap<String, String>();

                    System.out.println("----------------- " + nome + " " + cognome + " -----------------");
                    // aggiungo tutti i campi dell'utente all'HashMap
                    utente.put(TAG_UTENTE_NOME, nome);
                    utente.put(TAG_UTENTE_COGNOME, cognome);
                    utente.put(TAG_UTENTE_DATANASCITA, dataN);
                    utente.put(TAG_UTENTE_EMAIL, email);

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
