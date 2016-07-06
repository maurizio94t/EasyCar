package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

    public static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPreferences";

    // URL to get data JSON
    public static final String url = "http://t2j.no-ip.org/ddt/WebService.php";
    public static final String urlOperations = "http://t2j.no-ip.org/ddt/WebServiceOperations.php";
    // JSON Node - Campo stato login
    public static final String TAG_UTENTE_VERIFICATO = "UtenteVerificato";

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
    public static final String TAG_UTENTE_LOGGATO = "UtenteLoggato";
    public static final String TAG_UTENTI = "Utenti";
    public static final String TAG_UTENTE = "Utente";
    public static final String TAG_UTENTE_NOME = "Nome";
    public static final String TAG_UTENTE_COGNOME = "Cognome";
    public static final String TAG_UTENTE_DATANASCITA = "DataDiNascita";
    public static final String TAG_UTENTE_FOTO = "Foto";
    public static final String TAG_UTENTE_EMAIL = "Email";
    public static final String TAG_UTENTE_PSW = "Psw";

    // Hashmap per la ListView
    public static ArrayList<AutoUtente> listaAutoUtente;
    public static ArrayList<Manutenzione> listaManutenzioni;
    public static ArrayList<Modello> listaModelli;
    public static ArrayList<Marca> listaMarche;
    public static ArrayList<Problema> listaProblemi;
    public static ArrayList<Scadenza> listaScadenze;
    public static ArrayList<Utente> listaUtenti;
    public static Utente utenteLoggato;

    public static MySQLiteHelper mySQLiteHelper;

    public static boolean utenteVerificato;

    public static List<Marca> listMarcheLocal;
    public static List<Modello> listModelliLocal;
    public static List<Utente> listUtentiLocal;
    public static List<AutoUtente> listAutoUtenteLocal;
    public static List<Manutenzione> listManutenzioniLocal;
    public static List<Problema> listProblemiLocal;
    public static List<Scadenza> listScadenzeLocal;

    private String jsonStr;
    public static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getApplicationContext());

        listaAutoUtente = new ArrayList<AutoUtente>();
        listaManutenzioni = new ArrayList<Manutenzione>();
        listaModelli = new ArrayList<Modello>();
        listaMarche = new ArrayList<Marca>();
        listaProblemi = new ArrayList<Problema>();
        listaScadenze = new ArrayList<Scadenza>();
        listaUtenti = new ArrayList<Utente>();

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());

        boolean loginSalvato = sharedpreferences.getBoolean(TAG_UTENTE_VERIFICATO, false);
        if (loginSalvato) {
            if (Utility.checkInternetConnection(getApplicationContext())) {
                StringRequest myReq = new StringRequest(Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                jsonStr = response;
                                Log.d("Response", "> OK FETCH DB");
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
                        String user = sharedpreferences.getString(TAG_UTENTE_EMAIL, "");
                        String psw = sharedpreferences.getString(TAG_UTENTE_PSW, "");

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", user);
                        params.put("psw", psw);
                        return params;
                    }

                    ;
                };
                queue.add(myReq);
            } else {
                Intent intentBaseActivity = new Intent(MainActivity.this, BaseActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intentBaseActivity);
                finish();
            }
        } else {
            Intent intentLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intentLoginActivity);
            finish();
        }
    }

    // AsynkTask
    public class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Response", "> " + jsonStr);
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(jsonStr);
                UpdateService.parse(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            Intent intentBaseActivity = new Intent(MainActivity.this, BaseActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intentBaseActivity);
            finish();
        }
    }

}
