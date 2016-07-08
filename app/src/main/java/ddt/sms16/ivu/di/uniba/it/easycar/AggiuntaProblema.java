package ddt.sms16.ivu.di.uniba.it.easycar;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;

public class AggiuntaProblema extends AppCompatActivity {
    private EditText dettagliProblema;

    private Spinner mSpinnerVeicolo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_problema);
        dettagliProblema =(EditText) findViewById(R.id.Problema);
        mSpinnerVeicolo = (Spinner) findViewById(R.id.spinner_veicolo);
        final EditText problema = (EditText)findViewById(R.id.Problema);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<AutoUtente> auto = MainActivity.mySQLiteHelper.getAllMieAutoUtente();

        String[] automobili = new String[auto.size()];
        int i = 0;
        for (AutoUtente a : auto
                ) {
            automobili[i] = a.getModello().getMarca().getNome()+" "+a.getModello().getNome()+"-"+a.getTarga();
            i++;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, automobili);
        mSpinnerVeicolo.setAdapter(adapter);
    }


    public boolean controllaCampi(){

        if(dettagliProblema.getText().toString()==null || mSpinnerVeicolo.getSelectedItem().toString()==null) return false;
        else return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(controllaCampi()) {
            if (id == R.id.done) {

                if(!controllaCampi()){
                    Snackbar snackbar = Snackbar
                            .make( findViewById(android.R.id.content),"Compila tutti i campi...", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }else
                {
                    String dettaglioProblema  = dettagliProblema.getText().toString();

                     String targa = mSpinnerVeicolo.getSelectedItem().toString();
                 String email =MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL,"");

                   boolean aggiunto = aggiungiProblema(dettaglioProblema, targa,email);

                    Log.d("Response",Boolean.toString(aggiunto));
                    if(aggiunto){
                        Snackbar snackbar = Snackbar
                                .make( findViewById(android.R.id.content),"problema aggiunto con successo!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{
                        Snackbar snackbar = Snackbar
                                .make( findViewById(android.R.id.content),"errore nell'aggiunta del problema, controlla la connessione!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                return true;
            }
            }

        return super.onOptionsItemSelected(item);
    }


    private boolean aggiungiProblema(final String descrizione, final String targa, final String email){
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("Response Problema",response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            String idProblema= dati.getString(MainActivity.TAG_PROBLEMI_IDPROBLEMA);
                            String descrizione= dati.getString(MainActivity.TAG_PROBLEMI_DESCRIZIONE);
                            String targaVeicolo = dati.getString(MainActivity.TAG_PROBLEMI_VEICOLO);
                            MainActivity.mySQLiteHelper.aggiungiProblema(new Problema(Integer.parseInt(idProblema),descrizione, new AutoUtente(targaVeicolo)));
                            aggiunto[0] =true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", "> That didn't work!");
                        aggiunto[0] =false;
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                Log.d("Response","getParams");
                Log.d("Response",descrizione);
                Log.d("Response",Utility.estraiTarga(targa));

                params.put("operation", "c");
                params.put("email", email);
                params.put("table", MainActivity.TAG_PROBLEMI);
                params.put("descrizione", descrizione );
                params.put("targa",Utility.estraiTarga(targa) );

                return params;
            };
        };
        MainActivity.queue.add(myReq);

        return aggiunto[0];
    }
}
