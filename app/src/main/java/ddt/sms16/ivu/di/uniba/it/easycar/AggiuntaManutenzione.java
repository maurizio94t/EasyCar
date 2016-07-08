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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

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


public class AggiuntaManutenzione extends AppCompatActivity {

    private EditText mDescrizioneManutenzione;
    private EditText mData;
    private EditText mChilometraggio;
    private Spinner mSpinnerVeicolo;
    int anno, mese, giorno = 0;
    String dataN;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_manutenzioni);

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

          mDescrizioneManutenzione = (EditText)findViewById(R.id.descrizioneManutenzione);
          mData = (EditText)findViewById(R.id.dataManutenzione);
          mChilometraggio = (EditText)findViewById(R.id.chilometraggio_auto);
          mSpinnerVeicolo = (Spinner)findViewById(R.id.spinner_veicolo);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                anno = year;
                mese = monthOfYear;
                giorno = dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(AggiuntaManutenzione.this, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        mData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AggiuntaManutenzione.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        List<AutoUtente> auto = MainActivity.mySQLiteHelper.getAllAutoUtente();


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

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataN = sdf.format(myCalendar.getTime());
        mData.setText(dataN);

    }
    public boolean campiValidi(){

        if(mDescrizioneManutenzione.getText().toString().compareTo("")==0 || mData.getText().toString().compareTo("")==0
                || mChilometraggio.toString().compareTo("")==0 || mSpinnerVeicolo.toString().compareTo("")==0
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


            if (!campiValidi()) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "Compila tutti i campi...", Snackbar.LENGTH_LONG);

                snackbar.show();
            } else {


                String descrizioneManutenzione = mDescrizioneManutenzione.getText().toString();
                String datamanutenzione = mData.getText().toString();
                String chilometraggio = mChilometraggio.getText().toString();
                String targaVeicolo = mSpinnerVeicolo.getSelectedItem().toString();
                String targa = Utility.estraiTarga(targaVeicolo);
                int ordinaria =0;

                Log.d("Response Manutenzione",datamanutenzione);
                Log.d("Response Manutenzione",chilometraggio);
                Log.d("Response Manutenzione",String.valueOf(ordinaria));
                Log.d("Response Manutenzione",targa);


                String email = MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "");

                //  boolean aggiunto = aggiungiManutenzione(dettaglioProblema, targa, email);
                boolean aggiunto = true;
                aggiungiManutenzione(descrizioneManutenzione,datamanutenzione,ordinaria,chilometraggio,targa,email);
                Log.d("Response", Boolean.toString(aggiunto));
                if (aggiunto) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "problema aggiunto con successo!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "errore nell'aggiunta del problema, controlla la connessione!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
            return true;



        }
        return super.onOptionsItemSelected(item);
    }


    private boolean aggiungiManutenzione(final String descrizione, final String data,final int ordinaria,final String kmManutezione,  final String targa, final String email){
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("Response Manutenzione",response);
                        try {

                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            int idManutenzione= dati.getInt(MainActivity.TAG_MANUTENZIONI_ID_MANUTENZIONE);
                            String descrizione= dati.getString(MainActivity.TAG_MANUTENZIONI_DESCRIZIONE );
                            String data= dati.getString(MainActivity.TAG_MANUTENZIONI_DATA );
                            int ordinaria= dati.getInt(MainActivity.TAG_MANUTENZIONI_ORDINARIA );
                            String kmManutenzione= dati.getString(MainActivity.TAG_MANUTENZIONI_KM_MANUTENZIONE );
                            String targaVeicolo = dati.getString(MainActivity.TAG_MANUTENZIONI_VEICOLO);
                            String targa= Utility.estraiTarga(targaVeicolo);
                            Log.d("Response Manutenzione",String.valueOf(idManutenzione));
                            Log.d("Response Manutenzione",descrizione);
                            Log.d("Response Manutenzione",data);
                            Log.d("Response Manutenzione",String.valueOf(ordinaria));
                            Log.d("Response Manutenzione",kmManutenzione);
                            Log.d("Response Manutenzione",targa);

                            //    MainActivity.mySQLiteHelper.aggiungiManutenzione();
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

               //s final String descrizione, final String data,final int ordinaria,final int kmManutezione,  final String targa, final String email
                params.put("operation", "c");
                params.put("email", email);
                params.put("table", MainActivity.TAG_MANUTENZIONI);
                params.put("descrizione", descrizione );
               /* params.put("descrizione", descrizione );
                params.put("descrizione", descrizione );
                params.put("descrizione", descrizione );
                params.put("descrizione", descrizione );
               */
                params.put("targa",Utility.estraiTarga(targa) );

                return params;
            };
        };
        MainActivity.queue.add(myReq);

        return aggiunto[0];
    }
}
