package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.MieAutoFragment;

import static android.R.layout.simple_spinner_item;


public class AggiuntaAuto extends AppCompatActivity {
    private EditText mTarga ;
    private EditText mEmail ;
    private EditText mAnno ;
    private EditText mChilometraggio;
    private Spinner mSpinnerMarca ;
    private Spinner mSpinnerModello;
    private int idMarca;
    private int idModello;

    int anno, mese, giorno = 0;
    String dataN;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_auto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTarga = (EditText) findViewById(R.id.autoTarga);
        mEmail = (EditText) findViewById(R.id.editTxtEmail);
        mAnno = (EditText) findViewById(R.id.anno_imm);
        mChilometraggio = (EditText) findViewById(R.id.chilometraggio_auto);
        mSpinnerMarca = (Spinner)findViewById(R.id.spinner_marca);
        mSpinnerModello = (Spinner)findViewById(R.id.spinner_modello);


        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        List<Marca> marca = MainActivity.mySQLiteHelper.getAllMarche();
        Marca[] marche = new Marca[marca.size()];

        int j = 0;
        for (Marca a : marca ) {
            marche[j] =a;
            j++;
        }
        ArrayAdapter<Marca> adapter1 = new ArrayAdapter<Marca>(this,
                simple_spinner_item, marche);
        mSpinnerMarca.setAdapter(adapter1);
        mSpinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                 Marca marcaSelezionata= (Marca) mSpinnerMarca.getSelectedItem();
                idMarca=marcaSelezionata.getIDMarca();

                List<Modello> listaModelli = MainActivity.mySQLiteHelper.getAllModelliDiMarca(marcaSelezionata);


                Modello[] modelli = new Modello[listaModelli.size()];
                int i = 0;
              for (Modello a : listaModelli) {
                    modelli[i] = a;
                    i++;
                }

                ArrayAdapter<Modello> adapter = new ArrayAdapter<Modello>(AggiuntaAuto.this,
                        android.R.layout.simple_spinner_item, modelli);

                mSpinnerModello.setAdapter(adapter);


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mSpinnerModello.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Modello modelloSelezionato= (Modello) mSpinnerModello.getSelectedItem();
                idModello=modelloSelezionato.getIDModello();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean campiValidi(){
        if(mTarga.getText().toString().equalsIgnoreCase("")|| mEmail.getText().toString().equalsIgnoreCase("")
                || mAnno.toString().equalsIgnoreCase("")|| mChilometraggio.toString().equalsIgnoreCase("")
                 ){
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            if(mTarga.getText().toString().equalsIgnoreCase("") || mAnno.getText().toString().equalsIgnoreCase("") || mChilometraggio.getText().toString().equalsIgnoreCase("")) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Compila tutti i campi...", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                final String email = MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL,"");
                final String targa = mTarga.getText().toString();
                final String annoImm = mAnno.getText().toString();
                final String km = mChilometraggio.getText().toString();

                StringRequest myReq = new StringRequest(Request.Method.POST,
                        MainActivity.urlOperations,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("ResponseAggiuntaAuto ",response);
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if(jsonObj.getString("dati").equalsIgnoreCase("null")) {
                                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Errore: targa già presente!", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    } else {

                                        JSONObject dati = jsonObj.getJSONObject("dati");
                                        Log.d("ResponseAggiuntaAuto ",response);


                                        String targa = dati.getString("targa");
                                        String km = dati.getString("km");
                                        String anno= dati.getString("anno");
                                        String email= dati.getString("utente");
                                        String modello= dati.getString("modello");

                                        MainActivity.mySQLiteHelper.aggiungiAutoUtente(new AutoUtente(targa,Integer.parseInt(km),anno,new Utente(email),new Modello(Integer.parseInt(modello))));
                                        //aggiorno la listview
                                        MieAutoFragment.customAdapter.clear();
                                        MieAutoFragment.customAdapter.addAll(MainActivity.mySQLiteHelper.getAllMieAutoUtente());
                                        MieAutoFragment.customAdapter.notifyDataSetChanged();
                                        finish();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ResponseAggiuntaAuto","errore");
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("operation", "c");
                        params.put("table", MainActivity.TAG_AUTOUTENTE);

                        params.put("targa",targa  );
                        params.put("km", km );
                        params.put("anno",annoImm );
                        params.put("utente", email );
                        params.put("modello", String.valueOf(idModello) );

                        Log.d("ResponseAggiuntaAuto ", targa);
                        Log.d("ResponseAggiuntaAuto ", km);
                        Log.d("ResponseAggiuntaAuto ", annoImm);
                        Log.d("ResponseAggiuntaAuto ", email);

                        return params;
                    }

                    ;
                };
                if (Utility.checkInternetConnection(getApplicationContext())) {
                    MainActivity.queue.add(myReq);
                } else {
                    UpdateService.requests.add(myReq);
                    finish();
                    Toast.makeText(getApplicationContext(), "Quando sarà presente la connessione, aggiorneremo i tuoi dati!", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
