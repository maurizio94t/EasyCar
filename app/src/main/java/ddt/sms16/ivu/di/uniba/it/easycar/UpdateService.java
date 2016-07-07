package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
    private int mTime ;
    private String json;
    private RequestQueue queue;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Create Service",Toast.LENGTH_LONG).show();
        mTime = 0;
        queue = Volley.newRequestQueue(getApplicationContext());
        context = getApplicationContext();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("SERVICE >","onStart" + mTime) ;
        //Toast.makeText(getApplicationContext(),"Start Service",Toast.LENGTH_LONG).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
            Log.e("SERVICE >","NullPointerException()");
        }

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every hour
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 5, pintent);//1000*60 = minute
        mTime++;

        if(Utility.checkInternetConnection(getApplicationContext())) {
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
                    String user = MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "");
                    String psw = MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_PSW, "");

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", user);
                    params.put("psw", psw);
                    return params;
                };
            };
            queue.add(myReq);
        }

        Log.e("SERVICE >","onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Destroy Service",Toast.LENGTH_LONG).show();

    }

    protected class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e("SERVICE >", "doInBackground");
            try {
                Log.e("SERVICE-RESP >", params[0]);
                JSONObject jsonObj = new JSONObject(params[0]);
                parse(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            Log.e("SERVICE >", "onPostExecute");
            aggiornaDataBaseLocale(context);
        }

    }

    public static void parse(JSONObject jsonObj) throws JSONException {
        MainActivity.listaAutoUtente = new ArrayList<AutoUtente>();
        MainActivity.listaManutenzioni = new ArrayList<Manutenzione>();
        MainActivity.listaModelli = new ArrayList<Modello>();
        MainActivity.listaMarche = new ArrayList<Marca>();
        MainActivity.listaProblemi = new ArrayList<Problema>();
        MainActivity.listaScadenze = new ArrayList<Scadenza>();

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

        // Prelevo JSON Array node (Scadenze)
        JSONArray scadenzeJSON = jsonObj.getJSONArray(MainActivity.TAG_SCADENZE);
        // Ciclo tutte le auto degli utenti
        for (int i = 0; i < scadenzeJSON.length(); i++) {
            JSONObject scadenzaObj = scadenzeJSON.getJSONObject(i);

            int idScadenza = scadenzaObj.getInt(MainActivity.TAG_SCADENZE_IDSCADENZA);
            String descrizioneScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DESCRIZIONE);
            String dataScadenza = scadenzaObj.getString(MainActivity.TAG_SCADENZE_DATASCADENZA);


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
            Scadenza scadenza = new Scadenza(idScadenza, descrizioneScadenza, dataScadenza, autoutente);

            // aggiungo il singolo problema alla lista dei problemi
            MainActivity.listaScadenze.add(scadenza);
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
        MainActivity.utenteLoggato = new Utente(nomeUtLog, cognomeUtLog, dataNUtLog, emailUtLog ,pswUtLog);


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

    public static void aggiornaDataBaseLocale(Context context) {
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
                MainActivity.mySQLiteHelper.aggiungiProblemi(problemaE);
                // ---------------> mostra la notifica <---------------
                /*
                android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_done_white_24dp)
                        .setContentTitle("Nuova segnalazione!")
                        .setContentText("Nuovo problema riguardate la tua " + problemaE.getAuto().getModello().getMarca().getNome() + " " + problemaE.getAuto().getModello().getNome());
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(problemaE.getIDProblema(), mBuilder.build());
                */
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

    }

}
