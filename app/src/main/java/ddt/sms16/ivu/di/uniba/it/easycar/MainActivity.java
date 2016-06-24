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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;

/**
 * Created by Maurizio on 01/06/16.
 */
public class MainActivity extends AppCompatActivity {

    // URL to get data JSON
    private static final String url = "http://t2j.no-ip.org/ddt/WebService.php?email=%s&psw=%s";

    // JSON Node - Campo stato login
    public static final String TAG_STATO = "Stato";

    // JSON Node - Campi tabella AutoUtente
    public static final String TAG_AUTOUTENTE = "AutoUtente";
    public static final String TAG_AUTOUTENTE_TARGA = "Targa";
    public static final String TAG_AUTOUTENTE_KM = "KM";
    public static final String TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE = "AnnoImmatricolazione";
    public static final String TAG_AUTOUTENTE_FOTO_AUTO = "FotoAuto";
    public static final String TAG_AUTOUTENTE_UTENTI_EMAIL = "Utenti_Email";
    public static final String TAG_AUTOUTENTE_MODELLO= "Modello";

    // JSON Node - Campi tabella Manutenzioni
    public static final String TAG_MANUTENZIONI = "Manutenzioni";
    public static final String TAG_MANUTENZIONI_ID_MANUTENZIONE = "IDManutenzione";
    public static final String TAG_MANUTENZIONI_DESCRIZIONE = "Descrizione";
    public static final String TAG_MANUTENZIONI_DATA = "Data";
    public static final String TAG_MANUTENZIONI_ORDINARIA = "Ordinaria";
    public static final String TAG_MANUTENZIONI_KM_MANUTENZIONE = "KmManutenzione";
    public static final String TAG_MANUTENZIONI_TARGA = "Targa";
    public static final String TAG_MANUTENZIONI_VEICOLO = "Veicolo";

    // JSON Node - Campi tabella Marca
    public static final String TAG_MARCHE = "Marche";
    public static final String TAG_MARCHE_IDMARCA = "IDMarca";
    public static final String TAG_MARCHE_NOME = "Nome";

    // JSON Node - Campi tabella Modelli
    public static final String TAG_MODELLI = "Modelli";
    public static final String TAG_MODELLI_IDMODELLO = "IDModello";
    public static final String TAG_MODELLI_NOME = "Nome";
    public static final String TAG_MODELLI_SEGMENTO = "Segmento";
    public static final String TAG_MODELLI_ALIMENTAZIONE = "Alimentazione";
    public static final String TAG_MODELLI_CILINDRATA = "Cilindrata";
    public static final String TAG_MODELLI_KW = "KW";
    public static final String TAG_MODELLI_MARCA = "Marca";



    // JSON Node - Campi tabella Utenti
    public static final String TAG_UTENTI = "Utenti";
    public static final String TAG_UTENTI_NOME = "Nome";
    public static final String TAG_UTENTI_COGNOME = "Cognome";
    public static final String TAG_UTENTI_DATANASCITA = "DataDiNascita";
    public static final String TAG_AUTOUTENTE_FOTO = "Foto";
    public static final String TAG_UTENTI_EMAIL = "Email";
    public static final String TAG_AUTOUTENTE_SELECTED = "Selected";

    // Hashmap per la ListView
    public static ArrayList<AutoUtente> listaAutoUtente;
    public static ArrayList<Manutenzione> listaManutenzioni;
    public static ArrayList<HashMap<String, String>> listaMarche;
    public static ArrayList<HashMap<String, String>> listaModelli;
    public static ArrayList<HashMap<String, String>> listaProblemi;
    public static ArrayList<HashMap<String, String>> listaScadenze;
    public static Utente utente;

    public static boolean stato;
    private EditText mEditTxtEmail;
    private EditText mEditTxtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaAutoUtente = new ArrayList<AutoUtente>();
        listaManutenzioni = new ArrayList<Manutenzione>();

        mEditTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        mEditTxtPsw = (EditText) findViewById(R.id.editTxtPsw);

        Button btnAccedi = (Button) findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new GetData(mEditTxtEmail.getText().toString(), mEditTxtPsw.getText().toString()).execute();
                new GetData("maur_izzio@live.it", "prova").execute();
            }
        });

        Button bottonePrendiFoto = (Button) findViewById(R.id.bottonPrendiFoto);

        bottonePrendiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent prendiFoto = new Intent(MainActivity.this, PrendiFoto.class);
                startActivity(prendiFoto);
            }
        });


    }

    // AsynkTask
    private class GetData extends AsyncTask<Void, Void, Void> {
        ProgressDialog proDialog;
        private String email;
        private String psw;

        public GetData(String email, String psw) {
            this.email = email;
            this.psw = psw;
        }

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
            List<String> args = new ArrayList<String>();
            args.add(this.email);
            args.add(this.psw);
            String urlFormatted = String.format(url, args.toArray());
            // Creo un'istanza di WebRequest per effettuare una richiesta al server
            WebRequest webreq = new WebRequest();

            // Faccio una richiesta all'url dichiarato come variabile di classe e prendo la risposta
            String jsonStr = webreq.makeWebServiceCall(urlFormatted, WebRequest.GETRequest);

            Log.d("Response: ", "> " + jsonStr);

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
            if(stato) {
                Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
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

                if(!stato) {
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
                    String email = autoUtentiObj.getString(TAG_AUTOUTENTE_UTENTI_EMAIL);
                    //boolean selected = veicoloObj.getBoolean(TAG_AUTOUTENTE_SELECTED);

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
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, email, modello, false);

                    // aggiungo la singola auto alla lista di auto dell'utente
                    listaAutoUtente.add(autoutente);
                }

                // Prelevo JSON Array node (AutoUtente)
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
                    String email = veicoloObj.getString(TAG_AUTOUTENTE_UTENTI_EMAIL);
                    //boolean selected = veicoloObj.getBoolean(TAG_AUTOUTENTE_SELECTED);

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
                    AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, R.drawable.ic_menu_gallery, email, modello, false);
                    Manutenzione manutenzione = new Manutenzione(id, desc, dataS, ord, kmManut, autoutente);

                    // aggiungo la singola manutenzione alla lista di manutenzioni
                    listaManutenzioni.add(manutenzione);
                }

                // Prelevo JSON Array node (Utenti)
                JSONArray utenti = jsonObj.getJSONArray(TAG_UTENTI);
                // Ciclo tutti gli utenti
                for (int i = 0; i < utenti.length(); i++) {
                    JSONObject u = utenti.getJSONObject(i);

                    String nome = u.getString(TAG_UTENTI_NOME);
                    String cognome = u.getString(TAG_UTENTI_COGNOME);
                    String dataN = u.getString(TAG_UTENTI_DATANASCITA);
                    //String foto = u.getString(TAG_AUTOUTENTE_FOTO);
                    String email = u.getString(TAG_UTENTI_EMAIL);

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
