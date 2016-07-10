package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
import java.util.Calendar;
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
 * Created by Maurizio on 03/07/16.
 */
public class UpdateService extends Service  {
    private static SharedPreferences sharedpreferences;

    private int mTime ;
    private String json;
    private RequestQueue queue;
    private Context context;

    private MySQLiteHelper mySQLiteHelper;

    private ArrayList<AutoUtente> listaAutoUtente;
    private ArrayList<Manutenzione> listaManutenzioni;
    private ArrayList<Modello> listaModelli;
    private  ArrayList<Marca> listaMarche;
    private ArrayList<Problema> listaProblemi;
    private ArrayList<Scadenza> listaScadenze;
    private ArrayList<Utente> listaUtenti;

    public static ArrayList<StringRequest> requests;

    private Scadenza scadenzaNot;

    @Override
    public void onCreate() {
        super.onCreate();
        mTime = 0;
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getApplicationContext());
        context = getApplicationContext();

        mySQLiteHelper = new MySQLiteHelper(getApplicationContext());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("SERVICE >","onStart" + mTime) ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE >","onStartCommand");
        /*
        Intent mIntent = intent ;
        String msg = mIntent.getStringExtra("msg")
        */
        // Toast.makeText(getApplicationContext(),"onStart Command Service",Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"Message is = "+intent.getStringExtra("message"),Toast.LENGTH_LONG).show();
        Calendar cal = Calendar.getInstance();
        Intent mIntent = new Intent(this, UpdateService.class);
        PendingIntent pintent = null;
        try {
            pintent = PendingIntent.getService(this, 0, intent, 0);
        } catch (NullPointerException e) {
            Log.e("SERVICE >","NullPointerException() --> Reset del Service");
        }

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every hour
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60, pintent); //1000*60 = minute
        mTime++;

        if(Utility.checkInternetConnection(getApplicationContext())) {
            try {
                for(int i = 0; i < requests.size(); i++) {
                    queue.add(requests.get(i));
                    Log.e("QUEUE >", String.valueOf(i));
                    break;
                }
                if(!requests.isEmpty()) {
                    requests.remove(0);
                }
            } catch (Exception e) {

            }

            Log.e("SERVICE >","CONNECTED");
            // Scarito tutti i dati dal db esterno
            StringRequest myReq = new StringRequest(Request.Method.POST,
                    MainActivity.url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", "> OK FETCH DB");
                            new GetData().execute(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Response", "> That didn't work!");
                        }
                    }) {

                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    String user = sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "");
                    String psw = sharedpreferences.getString(MainActivity.TAG_UTENTE_PSW, "");

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", user);
                    params.put("psw", psw);
                    return params;
                };
            };
            queue.add(myReq);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("SERVICE >", "doInBackground");
            try {
                JSONObject jsonObj = new JSONObject(params[0]);
                parseService(jsonObj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            Log.e("SERVICE >", "onPostExecute");
            try {
                aggiornaDataBaseLocaleService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void parse(JSONObject jsonObj) throws JSONException {
        MainActivity.listaAutoUtente = new ArrayList<AutoUtente>();
        MainActivity.listaManutenzioni = new ArrayList<Manutenzione>();
        MainActivity.listaModelli = new ArrayList<Modello>();
        MainActivity.listaMarche = new ArrayList<Marca>();
        MainActivity.listaProblemi = new ArrayList<Problema>();
        MainActivity.listaScadenze = new ArrayList<Scadenza>();
        MainActivity.listaUtenti = new ArrayList<Utente>();


        if(!jsonObj.getString(MainActivity.TAG_AUTOUTENTE).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (AutoUtente)
            JSONArray autoUtentiJSON = jsonObj.getJSONArray(MainActivity.TAG_AUTOUTENTE);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < autoUtentiJSON.length(); i++) {
                JSONObject autoUtentiObj = autoUtentiJSON.getJSONObject(i);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione,  utenteAuto, modello, selected);

                // aggiungo la singola auto alla lista di auto dell'utente
                MainActivity.listaAutoUtente.add(autoutente);
            }
        }

        if(!jsonObj.getString(MainActivity.TAG_MANUTENZIONI).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Manutenzioni)
            JSONArray manutenzioni = jsonObj.getJSONArray(MainActivity.TAG_MANUTENZIONI);
            // Ciclo tutte le manutenzioni dell'utente
            for (int i = 0; i < manutenzioni.length(); i++) {
                JSONObject manutenzioniObj = manutenzioni.getJSONObject(i);

                int id = manutenzioniObj.getInt(MainActivity.TAG_MANUTENZIONI_ID_MANUTENZIONE);
                String desc = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_DESCRIZIONE);
                String dataS = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_DATA);
                int ord = manutenzioniObj.getInt(MainActivity.TAG_MANUTENZIONI_ORDINARIA);
                String kmManut = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_KM_MANUTENZIONE);

                //get Veicolo
                JSONObject veicoloObj = manutenzioniObj.getJSONObject(MainActivity.TAG_MANUTENZIONI_VEICOLO);

                String targa = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = veicoloObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = veicoloObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = veicoloObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = veicoloObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, /*R.drawable.ic_menu_gallery*/ utenteAuto, modello, selected);
                Manutenzione manutenzione = new Manutenzione(id, desc, dataS, ord, kmManut, autoutente);

                // aggiungo la singola manutenzione alla lista di manutenzioni
                MainActivity.listaManutenzioni.add(manutenzione);
            }
        }

        // Prelevo JSON Array node (Modelli)
        JSONArray modelli = jsonObj.getJSONArray(MainActivity.TAG_MODELLI);
        // Ciclo tutti i modelli delle auto
        for (int i = 0; i < modelli.length(); i++) {
            JSONObject modelloObj = modelli.getJSONObject(i);

            int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
            String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
            String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
            String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
            String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
            String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

            //get Marca
            JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

            int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
            String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

            //costruisco gli oggetti
            Marca marca = new Marca(idMarca, nomeMarca);
            Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);

            // aggiungo la singola manutenzione alla lista di manutenzioni
            MainActivity.listaModelli.add(modello);
        }

        // Prelevo JSON Array node (Marche)
        JSONArray marche = jsonObj.getJSONArray(MainActivity.TAG_MARCHE);
        // Ciclo tutte le marche delle auto
        for (int i = 0; i < marche.length(); i++) {
            JSONObject marcaObj = marche.getJSONObject(i);

            int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
            String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

            //costruisco gli oggetti
            Marca marca = new Marca(idMarca, nomeMarca);

            // aggiungo la singola manutenzione alla lista di manutenzioni
            MainActivity.listaMarche.add(marca);
        }

        if(!jsonObj.getString(MainActivity.TAG_PROBLEMI).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Problemi)
            JSONArray problemiJSON = jsonObj.getJSONArray(MainActivity.TAG_PROBLEMI);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < problemiJSON.length(); i++) {
                JSONObject problemaObj = problemiJSON.getJSONObject(i);

                int idProblema = problemaObj.getInt(MainActivity.TAG_PROBLEMI_IDPROBLEMA);
                String descrizioneProblema = problemaObj.getString(MainActivity.TAG_PROBLEMI_DESCRIZIONE);


                JSONObject autoUtentiObj = problemaObj.getJSONObject(MainActivity.TAG_PROBLEMI_VEICOLO);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione,/* R.drawable.ic_menu_gallery*/  utenteAuto, modello, selected);
                Problema problema = new Problema(idProblema, descrizioneProblema, autoutente);

                // aggiungo il singolo problema alla lista dei problemi
                MainActivity.listaProblemi.add(problema);
            }
        }

        if(!jsonObj.getString(MainActivity.TAG_SCADENZE).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Scadenze)
            JSONArray scadenzeJSON = jsonObj.getJSONArray(MainActivity.TAG_SCADENZE);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < scadenzeJSON.length(); i++) {
                JSONObject scadenzaObj = scadenzeJSON.getJSONObject(i);

                int idScadenza = scadenzaObj.getInt(MainActivity.TAG_SCADENZE_IDSCADENZA);
                String descrizioneScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DESCRIZIONE);
                String dataScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DATASCADENZA);
                int inviata = scadenzaObj.getInt(MainActivity.TAG_SCADENZE_INVIATA);


                JSONObject autoUtentiObj = scadenzaObj.getJSONObject(MainActivity.TAG_PROBLEMI_VEICOLO);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, /*R.drawable.ic_menu_gallery*/ utenteAuto, modello, selected);
                Scadenza scadenza = new Scadenza(idScadenza, descrizioneScadenza, dataScadenza, inviata, autoutente);

                // aggiungo il singolo problema alla lista dei problemi
                MainActivity.listaScadenze.add(scadenza);
            }
        }

        // Prelevo JSON Array node (UtenteLoggato)
        JSONObject utenteL = jsonObj.getJSONObject(MainActivity.TAG_UTENTE_LOGGATO);

        String nomeUtLog = utenteL.getString(MainActivity.TAG_UTENTE_NOME);
        String cognomeUtLog = utenteL.getString(MainActivity.TAG_UTENTE_COGNOME);
        String dataNUtLog = utenteL.getString(MainActivity.TAG_UTENTE_DATANASCITA);
        //String foto = utenteL.getString(MainActivity.TAG_UTENTE_FOTO);
        String emailUtLog = utenteL.getString(MainActivity.TAG_UTENTE_EMAIL);
        String pswUtLog = utenteL.getString(MainActivity.TAG_UTENTE_PSW);

        // creo l'oggetto dell' Utente Loggato
        Log.d("UTENTELOGGATO > ", nomeUtLog);
        Log.d("UTENTELOGGATO > ", cognomeUtLog);
        Log.d("UTENTELOGGATO > ", dataNUtLog);
        Log.d("UTENTELOGGATO > ", emailUtLog);
        Log.d("UTENTELOGGATO > ", pswUtLog);

        MainActivity.utenteLoggato = new Utente(nomeUtLog, cognomeUtLog, dataNUtLog, emailUtLog ,pswUtLog);

        if(!jsonObj.getString(MainActivity.TAG_UTENTE).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Utenti)
            JSONArray utentiJSON = jsonObj.getJSONArray(MainActivity.TAG_UTENTI);
            // Ciclo tutti gli utenti
            for (int i = 0; i < utentiJSON.length(); i++) {
                JSONObject u = utentiJSON.getJSONObject(i);

                String nome = u.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = u.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = u.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = u.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utente = new Utente(nome, cognome, dataN , email);
                MainActivity.listaUtenti.add(utente);
            }
        }
    }

    private void parseService(JSONObject jsonObj) throws JSONException {
        listaAutoUtente = new ArrayList<AutoUtente>();
        listaManutenzioni = new ArrayList<Manutenzione>();
        listaModelli = new ArrayList<Modello>();
        listaMarche = new ArrayList<Marca>();
        listaProblemi = new ArrayList<Problema>();
        listaScadenze = new ArrayList<Scadenza>();
        listaUtenti = new ArrayList<Utente>();

        if(!jsonObj.getString(MainActivity.TAG_AUTOUTENTE).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (AutoUtente)
            JSONArray autoUtentiJSON = jsonObj.getJSONArray(MainActivity.TAG_AUTOUTENTE);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < autoUtentiJSON.length(); i++) {
                JSONObject autoUtentiObj = autoUtentiJSON.getJSONObject(i);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione,  utenteAuto, modello, selected);

                // aggiungo la singola auto alla lista di auto dell'utente
                listaAutoUtente.add(autoutente);
            }
        }

        if(!jsonObj.getString(MainActivity.TAG_MANUTENZIONI).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Manutenzioni)
            JSONArray manutenzioni = jsonObj.getJSONArray(MainActivity.TAG_MANUTENZIONI);
            // Ciclo tutte le manutenzioni dell'utente
            for (int i = 0; i < manutenzioni.length(); i++) {
                JSONObject manutenzioniObj = manutenzioni.getJSONObject(i);

                int id = manutenzioniObj.getInt(MainActivity.TAG_MANUTENZIONI_ID_MANUTENZIONE);
                String desc = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_DESCRIZIONE);
                String dataS = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_DATA);
                int ord = manutenzioniObj.getInt(MainActivity.TAG_MANUTENZIONI_ORDINARIA);
                String kmManut = manutenzioniObj.getString(MainActivity.TAG_MANUTENZIONI_KM_MANUTENZIONE);

                //get Veicolo
                JSONObject veicoloObj = manutenzioniObj.getJSONObject(MainActivity.TAG_MANUTENZIONI_VEICOLO);

                String targa = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = veicoloObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = veicoloObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = veicoloObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = veicoloObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = veicoloObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, /*R.drawable.ic_menu_gallery*/ utenteAuto, modello, selected);
                Manutenzione manutenzione = new Manutenzione(id, desc, dataS, ord, kmManut, autoutente);

                // aggiungo la singola manutenzione alla lista di manutenzioni
                listaManutenzioni.add(manutenzione);
            }
        }

        // Prelevo JSON Array node (Modelli)
        JSONArray modelli = jsonObj.getJSONArray(MainActivity.TAG_MODELLI);
        // Ciclo tutti i modelli delle auto
        for (int i = 0; i < modelli.length(); i++) {
            JSONObject modelloObj = modelli.getJSONObject(i);

            int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
            String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
            String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
            String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
            String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
            String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

            //get Marca
            JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

            int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
            String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

            //costruisco gli oggetti
            Marca marca = new Marca(idMarca, nomeMarca);
            Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);

            // aggiungo la singola manutenzione alla lista di manutenzioni
            listaModelli.add(modello);
        }

        // Prelevo JSON Array node (Marche)
        JSONArray marche = jsonObj.getJSONArray(MainActivity.TAG_MARCHE);
        // Ciclo tutte le marche delle auto
        for (int i = 0; i < marche.length(); i++) {
            JSONObject marcaObj = marche.getJSONObject(i);

            int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
            String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

            //costruisco gli oggetti
            Marca marca = new Marca(idMarca, nomeMarca);

            // aggiungo la singola manutenzione alla lista di manutenzioni
            listaMarche.add(marca);
        }

        if(!jsonObj.getString(MainActivity.TAG_PROBLEMI).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Problemi)
            JSONArray problemiJSON = jsonObj.getJSONArray(MainActivity.TAG_PROBLEMI);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < problemiJSON.length(); i++) {
                JSONObject problemaObj = problemiJSON.getJSONObject(i);

                int idProblema = problemaObj.getInt(MainActivity.TAG_PROBLEMI_IDPROBLEMA);
                String descrizioneProblema = problemaObj.getString(MainActivity.TAG_PROBLEMI_DESCRIZIONE);


                JSONObject autoUtentiObj = problemaObj.getJSONObject(MainActivity.TAG_PROBLEMI_VEICOLO);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione,/* R.drawable.ic_menu_gallery*/  utenteAuto, modello, selected);
                Problema problema = new Problema(idProblema, descrizioneProblema, autoutente);

                // aggiungo il singolo problema alla lista dei problemi
                listaProblemi.add(problema);
            }
        }

        if(!jsonObj.getString(MainActivity.TAG_SCADENZE).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Scadenze)
            JSONArray scadenzeJSON = jsonObj.getJSONArray(MainActivity.TAG_SCADENZE);
            // Ciclo tutte le auto degli utenti
            for (int i = 0; i < scadenzeJSON.length(); i++) {
                JSONObject scadenzaObj = scadenzeJSON.getJSONObject(i);

                int idScadenza = scadenzaObj.getInt(MainActivity.TAG_SCADENZE_IDSCADENZA);
                String descrizioneScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DESCRIZIONE);
                String dataScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DATASCADENZA);
                int inviata = scadenzaObj.getInt(MainActivity.TAG_SCADENZE_INVIATA);

                JSONObject autoUtentiObj = scadenzaObj.getJSONObject(MainActivity.TAG_PROBLEMI_VEICOLO);

                String targa = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_TARGA);
                int km = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_KM);
                String annoImmatricolazione = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE);
                //String foto = autoUtentiObj.getString(MainActivity.TAG_AUTOUTENTE_FOTO_AUTO);

                //get Utente
                JSONObject utenteObj = autoUtentiObj.getJSONObject(MainActivity.TAG_UTENTE);

                String nome = utenteObj.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = utenteObj.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = utenteObj.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = utenteObj.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utenteAuto = new Utente(nome, cognome, dataN, email);

                int selected = autoUtentiObj.getInt(MainActivity.TAG_AUTOUTENTE_SELECTED);

                //get Modello
                JSONObject modelloObj = autoUtentiObj.getJSONObject(MainActivity.TAG_AUTOUTENTE_MODELLO);

                int idModello = modelloObj.getInt(MainActivity.TAG_MODELLI_IDMODELLO);
                String nomeModello = modelloObj.getString(MainActivity.TAG_MODELLI_NOME);
                String segmento = modelloObj.getString(MainActivity.TAG_MODELLI_SEGMENTO);
                String alimentazione = modelloObj.getString(MainActivity.TAG_MODELLI_ALIMENTAZIONE);
                String cilindrata = modelloObj.getString(MainActivity.TAG_MODELLI_CILINDRATA);
                String kw = modelloObj.getString(MainActivity.TAG_MODELLI_KW);

                //get Marca
                JSONObject marcaObj = modelloObj.getJSONObject(MainActivity.TAG_MODELLI_MARCA);

                int idMarca = marcaObj.getInt(MainActivity.TAG_MARCHE_IDMARCA);
                String nomeMarca = marcaObj.getString(MainActivity.TAG_MARCHE_NOME);

                //costruisco gli oggetti
                Marca marca = new Marca(idMarca, nomeMarca);
                Modello modello = new Modello(idModello, nomeModello, segmento, alimentazione, cilindrata, kw, marca);
                AutoUtente autoutente = new AutoUtente(targa, km, annoImmatricolazione, /*R.drawable.ic_menu_gallery*/ utenteAuto, modello, selected);
                Scadenza scadenza = new Scadenza(idScadenza, descrizioneScadenza, dataScadenza, inviata, autoutente);

                // aggiungo il singolo problema alla lista dei problemi
                listaScadenze.add(scadenza);
            }
        }

        if(!jsonObj.getString(MainActivity.TAG_UTENTI).equalsIgnoreCase("null")) {
            // Prelevo JSON Array node (Utenti)
            JSONArray utentiJSON = jsonObj.getJSONArray(MainActivity.TAG_UTENTI);
            // Ciclo tutti gli utenti
            for (int i = 0; i < utentiJSON.length(); i++) {
                JSONObject u = utentiJSON.getJSONObject(i);

                String nome = u.getString(MainActivity.TAG_UTENTE_NOME);
                String cognome = u.getString(MainActivity.TAG_UTENTE_COGNOME);
                String dataN = u.getString(MainActivity.TAG_UTENTE_DATANASCITA);
                //String foto = u.getString(MainActivity.TAG_UTENTE_FOTO);
                String email = u.getString(MainActivity.TAG_UTENTE_EMAIL);

                // creo l'oggetto del singolo Utente
                Utente utente = new Utente(nome, cognome, dataN , email);
                listaUtenti.add(utente);
            }
        }
    }

    public static void aggiornaDataBaseLocale() {
        //aggiungo ai dati in locale i nuovi dati presenti sul DB

        boolean trovato;
        //update Marche - insert
        MainActivity.listMarcheLocal = MainActivity.mySQLiteHelper.getAllMarche();
        for(Marca marcaE : MainActivity.listaMarche) {
            trovato = false;
            for(Marca marcaL : MainActivity.listMarcheLocal) {
                if(marcaE.getIDMarca() == marcaL.getIDMarca()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiMarca(marcaE);
            }
        }
        MainActivity.listMarcheLocal = MainActivity.mySQLiteHelper.getAllMarche();

        //update Marche - update
        for(Marca marcaE : MainActivity.listaMarche) {
            for(Marca marcaL : MainActivity.listMarcheLocal) {
                if(marcaE.getIDMarca() == marcaL.getIDMarca()) {
                    if(marcaE.compareTo(marcaL) != 0) {
                        MainActivity.mySQLiteHelper.updateMarca(marcaE);
                    }
                    break;
                }
            }
        }
        MainActivity.listMarcheLocal = MainActivity.mySQLiteHelper.getAllMarche();

        //update Modelli - insert
        MainActivity.listModelliLocal = MainActivity.mySQLiteHelper.getAllModelli();
        for(Modello modelloE : MainActivity.listaModelli) {
            trovato = false;
            for(Modello modelloL : MainActivity.listModelliLocal) {
                if(modelloE.getIDModello() == modelloL.getIDModello()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiModello(modelloE);
            }
        }
        MainActivity.listModelliLocal = MainActivity.mySQLiteHelper.getAllModelli();

        //update Modelli - update
        for(Modello modelloE : MainActivity.listaModelli) {
            for(Modello modelloL : MainActivity.listModelliLocal) {
                if(modelloE.getIDModello() == modelloL.getIDModello()) {
                    if(modelloE.compareTo(modelloL) != 0) {
                        MainActivity.mySQLiteHelper.updateModello(modelloE);
                    }
                    break;
                }
            }
        }
        MainActivity.listModelliLocal = MainActivity.mySQLiteHelper.getAllModelli();

        //update Utenti - insert
        MainActivity.listUtentiLocal = MainActivity.mySQLiteHelper.getAllUtenti();
        for(Utente utenteE : MainActivity.listaUtenti) {
            trovato = false;
            for(Utente utenteL : MainActivity.listUtentiLocal) {
                if(utenteE.getEmail().equalsIgnoreCase(utenteL.getEmail())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiUtente(utenteE);
            }
        }
        MainActivity.listUtentiLocal = MainActivity.mySQLiteHelper.getAllUtenti();

        //update Utenti - update
        for(Utente utenteE : MainActivity.listaUtenti) {
            for(Utente utenteL : MainActivity.listUtentiLocal) {
                if(utenteE.getEmail().equalsIgnoreCase(utenteL.getEmail())) {
                    if(utenteE.compareTo(utenteL) != 0) {
                        MainActivity.mySQLiteHelper.updateUtente(utenteE);
                    }
                    break;
                }
            }
        }
        MainActivity.listUtentiLocal = MainActivity.mySQLiteHelper.getAllUtenti();

        //update AutoUtente - insert
        MainActivity.listAutoUtenteLocal = MainActivity.mySQLiteHelper.getAllAutoUtente();
        for(AutoUtente autoE : MainActivity.listaAutoUtente) {
            trovato = false;
            for(AutoUtente autoL : MainActivity.listAutoUtenteLocal) {
                if(autoE.getTarga().equalsIgnoreCase(autoL.getTarga())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiAutoUtente(autoE);
            }
        }
        MainActivity.listAutoUtenteLocal = MainActivity.mySQLiteHelper.getAllAutoUtente();

        //update AutoUtente - update
        for(AutoUtente autoE : MainActivity.listaAutoUtente) {
            for(AutoUtente autoL : MainActivity.listAutoUtenteLocal) {
                if(autoE.getTarga().equalsIgnoreCase(autoL.getTarga())) {
                    if(autoE.compareTo(autoL) != 0) {
                        MainActivity.mySQLiteHelper.updateAutoUtente(autoE);
                    }
                    break;
                }
            }
        }
        MainActivity.listAutoUtenteLocal = MainActivity.mySQLiteHelper.getAllAutoUtente();

        //update AutoUtente - delete
        for(AutoUtente autoUtenteL : MainActivity.listAutoUtenteLocal) {
            trovato = false;
            for(AutoUtente autoUtenteE : MainActivity.listaAutoUtente) {
                if(autoUtenteL.getTarga().equalsIgnoreCase(autoUtenteE.getTarga())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.deleteAutoUtente(autoUtenteL);
            }
        }
        MainActivity.listAutoUtenteLocal = MainActivity.mySQLiteHelper.getAllAutoUtente();

        //update Manutenzioni - insert
        MainActivity.listManutenzioniLocal = MainActivity.mySQLiteHelper.getAllManutenzioni();
        for(Manutenzione manutenzioneE : MainActivity.listaManutenzioni) {
            trovato = false;
            for(Manutenzione manutenzioneL : MainActivity.listManutenzioniLocal) {
                if(manutenzioneE.getIDManutenzione() == manutenzioneL.getIDManutenzione()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiManutenzione(manutenzioneE);
            }
        }
        MainActivity.listManutenzioniLocal = MainActivity.mySQLiteHelper.getAllManutenzioni();

        //update Manutenzioni - update
        for(Manutenzione manutenzioneE : MainActivity.listaManutenzioni) {
            for(Manutenzione manutenzioneL : MainActivity.listManutenzioniLocal) {
                if(manutenzioneE.getIDManutenzione()== manutenzioneL.getIDManutenzione()) {
                    if(manutenzioneE.compareTo(manutenzioneL) != 0) {
                        MainActivity.mySQLiteHelper.updateMantenzione(manutenzioneE);
                    }
                    break;
                }
            }
        }
        MainActivity.listManutenzioniLocal = MainActivity.mySQLiteHelper.getAllManutenzioni();

        //update Manutenzioni - delete
        for(Manutenzione manutenzioneL : MainActivity.listManutenzioniLocal) {
            trovato = false;
            for(Manutenzione manutenzioneE : MainActivity.listaManutenzioni) {
                if(manutenzioneL.getIDManutenzione() == manutenzioneE.getIDManutenzione()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.deleteManutezione(manutenzioneL);
            }
        }
        MainActivity.listManutenzioniLocal = MainActivity.mySQLiteHelper.getAllManutenzioni();

        //update Problemi - insert
        MainActivity.listProblemiLocal = MainActivity.mySQLiteHelper.getAllProblemi();
        for(Problema problemaE : MainActivity.listaProblemi) {
            trovato = false;
            for(Problema problemaL : MainActivity.listProblemiLocal) {
                if(problemaE.getIDProblema() == problemaL.getIDProblema()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiProblema(problemaE);
            }
        }
        MainActivity.listProblemiLocal = MainActivity.mySQLiteHelper.getAllProblemi();

        //update Problemi - update
        for(Problema problemaE : MainActivity.listaProblemi) {
            for(Problema problemaL : MainActivity.listProblemiLocal) {
                if(problemaE.getIDProblema() == problemaL.getIDProblema()) {
                    if(problemaE.compareTo(problemaL) != 0) {
                        MainActivity.mySQLiteHelper.updateProblema(problemaE);
                    }
                    break;
                }
            }
        }
        MainActivity.listProblemiLocal = MainActivity.mySQLiteHelper.getAllProblemi();

        //update Problemi - delete
        for(Problema problemaL : MainActivity.listProblemiLocal) {
            trovato = false;
            for(Problema problemaE : MainActivity.listaProblemi) {
                if(problemaL.getIDProblema() == problemaE.getIDProblema()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.deleteProblema(problemaL);
            }
        }
        MainActivity.listProblemiLocal = MainActivity.mySQLiteHelper.getAllProblemi();

        //update Scadenze - insert
        MainActivity.listScadenzeLocal = MainActivity.mySQLiteHelper.getAllScadenze();
        for(Scadenza scadenzaE : MainActivity.listaScadenze) {
            trovato = false;
            for(Scadenza scadenzaL : MainActivity.listScadenzeLocal) {
                if(scadenzaE.getIDScadenza() == scadenzaL.getIDScadenza()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiScadenza(scadenzaE);
            }
        }
        MainActivity.listScadenzeLocal = MainActivity.mySQLiteHelper.getAllScadenze();

        //update Scadenze - update
        for(Scadenza scadenzaE : MainActivity.listaScadenze) {
            for(Scadenza scadenzaL : MainActivity.listScadenzeLocal) {
                if(scadenzaE.getIDScadenza() == scadenzaL.getIDScadenza()) {
                    if(scadenzaE.compareTo(scadenzaL) != 0) {
                        MainActivity.mySQLiteHelper.updateScadenza(scadenzaE);
                    }
                    break;
                }
            }
        }
        MainActivity.listScadenzeLocal = MainActivity.mySQLiteHelper.getAllScadenze();

        //update Scadenze - delete
        for(Scadenza scadenzaL : MainActivity.listScadenzeLocal) {
            trovato = false;
            for(Scadenza scadenzaE : MainActivity.listaScadenze) {
                if(scadenzaL.getIDScadenza() == scadenzaE.getIDScadenza()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.deleteScadenza(scadenzaL);
            }
        }
        MainActivity.listScadenzeLocal = MainActivity.mySQLiteHelper.getAllScadenze();
    }

    private void aggiornaDataBaseLocaleService() throws Exception {
        //aggiungo ai dati in locale i nuovi dati presenti sul DB
        boolean trovato;
        //update Marche - insert
        List<Marca> listMarcheLocal = mySQLiteHelper.getAllMarche();
        for(Marca marcaE : listaMarche) {
            trovato = false;
            for(Marca marcaL : listMarcheLocal) {
                if(marcaE.getIDMarca() == marcaL.getIDMarca()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiMarca(marcaE);
            }
        }
        listMarcheLocal = mySQLiteHelper.getAllMarche();

        //update Marche - update
        for(Marca marcaE : listaMarche) {
            for(Marca marcaL : listMarcheLocal) {
                if(marcaE.getIDMarca() == marcaL.getIDMarca()) {
                    if(marcaE.compareTo(marcaL) != 0) {
                        mySQLiteHelper.updateMarca(marcaE);
                    }
                    break;
                }
            }
        }
        listMarcheLocal = mySQLiteHelper.getAllMarche();

        //update Modelli - insert
        List<Modello> listModelliLocal = mySQLiteHelper.getAllModelli();
        for(Modello modelloE : listaModelli) {
            trovato = false;
            for(Modello modelloL : listModelliLocal) {
                if(modelloE.getIDModello() == modelloL.getIDModello()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiModello(modelloE);
            }
        }
        listModelliLocal = mySQLiteHelper.getAllModelli();

        //update Modelli - update
        for(Modello modelloE : listaModelli) {
            for(Modello modelloL : listModelliLocal) {
                if(modelloE.getIDModello() == modelloL.getIDModello()) {
                    if(modelloE.compareTo(modelloL) != 0) {
                        mySQLiteHelper.updateModello(modelloE);
                    }
                    break;
                }
            }
        }
        listModelliLocal = mySQLiteHelper.getAllModelli();

        //update Utenti - insert
        List<Utente> listUtentiLocal = mySQLiteHelper.getAllUtenti();
        for(Utente utenteE : listaUtenti) {
            trovato = false;
            for(Utente utenteL : listUtentiLocal) {
                if(utenteE.getEmail().equalsIgnoreCase(utenteL.getEmail())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiUtente(utenteE);
            }
        }
        listUtentiLocal = mySQLiteHelper.getAllUtenti();

        //update Utenti - update
        for(Utente utenteE : listaUtenti) {
            for(Utente utenteL : listUtentiLocal) {
                if(utenteE.getEmail().equalsIgnoreCase(utenteL.getEmail())) {
                    if(utenteE.compareTo(utenteL) != 0) {
                        mySQLiteHelper.updateUtente(utenteE);
                    }
                    break;
                }
            }
        }
        listUtentiLocal = mySQLiteHelper.getAllUtenti();

        //update AutoUtente - insert
        List<AutoUtente> listAutoUtenteLocal = mySQLiteHelper.getAllAutoUtente();
        for(AutoUtente autoE : listaAutoUtente) {
            trovato = false;
            for(AutoUtente autoL : listAutoUtenteLocal) {
                if(autoE.getTarga().equalsIgnoreCase(autoL.getTarga())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiAutoUtente(autoE);
            }
        }
        listAutoUtenteLocal = mySQLiteHelper.getAllAutoUtente();

        //update AutoUtente - update
        for(AutoUtente autoE : listaAutoUtente) {
            for(AutoUtente autoL : listAutoUtenteLocal) {
                if(autoE.getTarga().equalsIgnoreCase(autoL.getTarga())) {
                    if(autoE.compareTo(autoL) != 0) {
                        mySQLiteHelper.updateAutoUtente(autoE);
                    }
                    break;
                }
            }
        }
        listAutoUtenteLocal = mySQLiteHelper.getAllAutoUtente();

        //update AutoUtente - delete
        for(AutoUtente autoUtenteL : listAutoUtenteLocal) {
            trovato = false;
            for(AutoUtente autoUtenteE : listaAutoUtente) {
                if(autoUtenteL.getTarga().equalsIgnoreCase(autoUtenteE.getTarga())) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.deleteAutoUtente(autoUtenteL);
            }
        }
        listAutoUtenteLocal = mySQLiteHelper.getAllAutoUtente();

        //update Manutenzioni - insert
        List<Manutenzione> listManutenzioniLocal = mySQLiteHelper.getAllManutenzioni();
        for(Manutenzione manutenzioneE : listaManutenzioni) {
            trovato = false;
            for(Manutenzione manutenzioneL : listManutenzioniLocal) {
                if(manutenzioneE.getIDManutenzione() == manutenzioneL.getIDManutenzione()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiManutenzione(manutenzioneE);
            }
        }
        listManutenzioniLocal = mySQLiteHelper.getAllManutenzioni();

        //update Manutenzioni - update
        for(Manutenzione manutenzioneE : listaManutenzioni) {
            for(Manutenzione manutenzioneL : listManutenzioniLocal) {
                if(manutenzioneE.getIDManutenzione() == manutenzioneL.getIDManutenzione()) {
                    if(manutenzioneE.compareTo(manutenzioneL) != 0) {
                        mySQLiteHelper.updateMantenzione(manutenzioneE);
                    }
                    break;
                }
            }
        }
        listManutenzioniLocal = mySQLiteHelper.getAllManutenzioni();

        //update Manutenzioni - delete
        for(Manutenzione manutenzioneL : listManutenzioniLocal) {
            trovato = false;
            for(Manutenzione manutenzioneE : listaManutenzioni) {
                if(manutenzioneL.getIDManutenzione() == manutenzioneE.getIDManutenzione()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.deleteManutezione(manutenzioneL);
            }
        }
        listManutenzioniLocal = mySQLiteHelper.getAllManutenzioni();

        //update Problemi - insert
        List<Problema> listProblemiLocal = mySQLiteHelper.getAllProblemi();
        for(Problema problemaE : listaProblemi) {
            trovato = false;
            for(Problema problemaL : listProblemiLocal) {
                if(problemaE.getIDProblema() == problemaL.getIDProblema()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiProblema(problemaE);
                NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_alert_octagon_grey600_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("Nuova segnalazione!")
                        .setContentText("Nuovo problema riguardate la tua " + problemaE.getAuto().getModello().getMarca().getNome() + " " + problemaE.getAuto().getModello().getNome());

                Intent resultIntent = new Intent(this, MainActivity.class);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(problemaE.getIDProblema(), mBuilder.build());
            }
        }
        listProblemiLocal = mySQLiteHelper.getAllProblemi();

        //update Problemi - update
        for(Problema problemaE : listaProblemi) {
            for(Problema problemaL : listProblemiLocal) {
                if(problemaE.getIDProblema() == problemaL.getIDProblema()) {
                    if(problemaE.compareTo(problemaL) != 0) {
                        mySQLiteHelper.updateProblema(problemaE);
                    }
                    break;
                }
            }
        }
        listProblemiLocal = mySQLiteHelper.getAllProblemi();

        //update Problemi - delete
        for(Problema problemaL : listProblemiLocal) {
            trovato = false;
            for(Problema problemaE : listaProblemi) {
                if(problemaL.getIDProblema() == problemaE.getIDProblema()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.deleteProblema(problemaL);
            }
        }
        listProblemiLocal = mySQLiteHelper.getAllProblemi();

        //update Scadenze - insert
        List<Scadenza> listScadenzeLocal = mySQLiteHelper.getAllScadenze();
        for(Scadenza scadenzaE : listaScadenze) {
            trovato = false;
            for(Scadenza scadenzaL : listScadenzeLocal) {
                if(scadenzaE.getIDScadenza() == scadenzaL.getIDScadenza()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.aggiungiScadenza(scadenzaE);
            }
        }
        listScadenzeLocal = mySQLiteHelper.getAllScadenze();

        //update Scadenze - update
        for(Scadenza scadenzaE : listaScadenze) {
            for(Scadenza scadenzaL : listScadenzeLocal) {
                if(scadenzaE.getIDScadenza() == scadenzaL.getIDScadenza()) {
                    if(scadenzaE.compareTo(scadenzaL) != 0) {
                        mySQLiteHelper.updateScadenza(scadenzaE);
                    }
                    break;
                }
            }
        }
        listScadenzeLocal = mySQLiteHelper.getAllScadenze();

        //update Scadenze - delete
        for(Scadenza scadenzaL : listScadenzeLocal) {
            trovato = false;
            for(Scadenza scadenzaE : listaScadenze) {
                if(scadenzaL.getIDScadenza() == scadenzaE.getIDScadenza()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                mySQLiteHelper.deleteScadenza(scadenzaL);
            }
        }
        listScadenzeLocal = mySQLiteHelper.getAllScadenze();

        for(Scadenza scadenza : listScadenzeLocal) {
            if(scadenza.lastDay() && scadenza.getInviata() == 0) {
                NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_calendar_clock_grey600_24dp)
                        .setAutoCancel(true)
                        .setContentTitle("Scadenza imminente!")
                        .setContentText(scadenza.getDescrizione() + " " + scadenza.getAuto().getModello().getMarca().getNome() + " " + scadenza.getAuto().getModello().getNome() + " scade domani.");

                Intent resultIntent = new Intent(this, MainActivity.class);
                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(scadenza.getIDScadenza(), mBuilder.build());

                //setto la notifica inviata
                scadenza.setInviata(1);
                // inizio request
                scadenzaNot = scadenza;
                StringRequest myReq = new StringRequest(Request.Method.POST,
                        MainActivity.urlOperations,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("ResponseInviata >", response);
                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(response);
                                    JSONObject dati = jsonObj.getJSONObject("dati");
                                    boolean update = dati.getBoolean("Update");
                                    if(update) {
                                        mySQLiteHelper.updateScadenza(scadenzaNot);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                        //update into Scadenze
                        params.put("operation", "u");
                        params.put("table", "Scadenze");

                        params.put("id", String.valueOf(scadenzaNot.getIDScadenza()));
                        params.put("inviata", String.valueOf(1));
                        return params;
                    }

                    ;
                };
                queue.add(myReq);

                // fine request


            }
        }

    }
}
