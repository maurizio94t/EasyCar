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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Auto;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ScadenzeFragment;

public class AggiuntaScadenza extends AppCompatActivity {
    private Calendar myCalendar = Calendar.getInstance();

    private EditText editTextDate;

    private String dataN;
    private RadioButton tipoScadenzaRadioGroupSelected;
    private Spinner spinnerTarghe;
    private EditText mDataScadenza;
    int anno, mese, giorno = 0;
    public static final String TAG_UTENTE_EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_scadenza);
        final MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(AggiuntaScadenza.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDataScadenza = (EditText)findViewById(R.id.dataScadenza);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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


        mDataScadenza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AggiuntaScadenza.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        final RadioGroup tipoScadenzaRadioGroup = (RadioGroup) findViewById(R.id.tipoScadenzaRadioGroup);

        spinnerTarghe = (Spinner) findViewById(R.id.spinner_targa);

        List<AutoUtente> auto = mySQLiteHelper.getAllMieAutoUtente();


        AutoUtente[] automobili = new AutoUtente[auto.size()];
        int i = 0;
        for (AutoUtente a : auto) {
            automobili[i] = a;
            i++;
        }

        ArrayAdapter<AutoUtente> adapter = new ArrayAdapter<AutoUtente>(this,
                android.R.layout.simple_spinner_item, automobili);
        spinnerTarghe.setAdapter(adapter);


        tipoScadenzaRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tipoScadenzaRadioGroupSelected = (RadioButton) findViewById(checkedId);


            }
        });



        editTextDate = (EditText) findViewById(R.id.dataScadenza);
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        Calendar today = Calendar.getInstance();

        int anni = today.get(Calendar.YEAR) - myCalendar.get(Calendar.YEAR);
        if (myCalendar.get(Calendar.MONTH) > today.get(Calendar.MONTH) ||
                (myCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && myCalendar.get(Calendar.DATE) > today.get(Calendar.DATE))) {

        }

        dataN = sdf.format(myCalendar.getTime());
        editTextDate.setText(dataN);
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
            if(tipoScadenzaRadioGroupSelected==null || mDataScadenza.getText().toString().equalsIgnoreCase("")) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Compila tutti i campi...", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                String tipoScadenza  = tipoScadenzaRadioGroupSelected.getText().toString();
                String dataScadenza = Utility.convertStringDateToString(mDataScadenza.getText().toString());
                AutoUtente autoSelezionata = (AutoUtente) spinnerTarghe.getSelectedItem();
                String targa= autoSelezionata.getTarga();
                String email =MainActivity.sharedpreferences.getString(TAG_UTENTE_EMAIL,"");

                boolean aggiunto = aggiungiScadenza(tipoScadenza, dataScadenza, targa, email);
                Log.d("Response",Boolean.toString(aggiunto));
                Snackbar snackbar;
                if(aggiunto) {
                    snackbar = Snackbar.make(findViewById(android.R.id.content), "Errore nell'aggiunta della scadenza, controlla la connessione!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    finish();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


private boolean aggiungiScadenza(final String descrizione, final String dataScadenza, final String targa, final String email){
    final boolean[] aggiunto = new boolean[1];
    StringRequest myReq = new StringRequest(Request.Method.POST,
            MainActivity.urlOperations,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response", "> OK Req");
                    Log.d("Response", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONObject dati = jsonObj.getJSONObject("dati");
                        String idScadenza= dati.getString(MainActivity.TAG_SCADENZE_IDSCADENZA);
                        String descrizione= dati.getString( MainActivity.TAG_SCADENZE_DESCRIZIONE);
                        String dataScadenza= dati.getString( MainActivity.TAG_SCADENZE_DATASCADENZA);
                        String targaVeicolo = dati.getString(MainActivity.TAG_SCADENZE_VEICOLO);

                        MainActivity.mySQLiteHelper.aggiungiScadenza(new Scadenza(Integer.parseInt(idScadenza), descrizione, dataScadenza, new AutoUtente(targaVeicolo)));
                        //aggiorno la listview
                        ScadenzeFragment.customAdapter.clear();
                        ScadenzeFragment.customAdapter.addAll(MainActivity.mySQLiteHelper.getAllScadenzeOrdinate());
                        ScadenzeFragment.customAdapter.notifyDataSetChanged();
                        aggiunto[0] = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Response", "> That didn't work!");
                   aggiunto[0] = false;
                }
            }) {

        protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
            Log.d("SCADENZAAGG >", descrizione);
            Log.d("SCADENZAAGG >", dataScadenza);
            Log.d("SCADENZAAGG >", targa);
            Log.d("SCADENZAAGG >", email);

            Map<String, String> params = new HashMap<String, String>();
            params.put("operation", "c");
            params.put("email", email);
            params.put("table", MainActivity.TAG_SCADENZE);
            params.put("descrizione", descrizione);
            params.put("data", dataScadenza);
            params.put("targa", targa);

            return params;
        };
    };
    if (Utility.checkInternetConnection(getApplicationContext())) {
        MainActivity.queue.add(myReq);
    } else {
        UpdateService.requests.add(myReq);
        finish();
        Toast.makeText(getApplicationContext(), "Quando sarà presente la connessione, aggiorneremo i tuoi dati!", Toast.LENGTH_LONG).show();
    }

return aggiunto[0];
}
}
