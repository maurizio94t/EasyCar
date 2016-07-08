package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;


public class AggiuntaManutenzione extends AppCompatActivity {

    private EditText mDescrizioneManutenzione;
    private EditText mDataScadenza;
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
          mDataScadenza = (EditText)findViewById(R.id.dataManutenzione);
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

        mDataScadenza.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        mDataScadenza.setOnClickListener(new View.OnClickListener() {

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
            automobili[i] = a.getTarga();
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
        mDataScadenza.setText(dataN);

    }
    public boolean campiValidi(){

        if(mDescrizioneManutenzione.getText().toString().compareTo("")==0 || mDataScadenza.getText().toString().compareTo("")==0
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

        if(campiValidi()) {
            if (id == R.id.done) {
                Toast.makeText(getApplicationContext(), "Manutenzione aggiunta", Toast.LENGTH_LONG).show();
                Log.d("done", "done");
                return true;
            }
        }else{
            Toast.makeText(getApplicationContext(), "Completa l'inserimemnto dei dati", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
