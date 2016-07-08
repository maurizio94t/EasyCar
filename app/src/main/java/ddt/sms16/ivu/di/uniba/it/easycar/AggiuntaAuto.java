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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiuntaAuto extends AppCompatActivity {
    private EditText mTarga ;
    private EditText mEmail ;
    private EditText mAnnoimmatricolazione ;
    private EditText mChilometraggio;
    private Spinner mSpinnerMarca ;
    private Spinner mSpinnerModello;
    private Button inviaAuto;
    int anno, mese, giorno = 0;
    String dataN;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_auto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTarga = (EditText) findViewById(R.id.autoTarga);
        mEmail = (EditText) findViewById(R.id.editTxtEmail);
        mAnnoimmatricolazione = (EditText) findViewById(R.id.anno_immatricolazione);
        mChilometraggio = (EditText) findViewById(R.id.chilometraggio_auto);
        mSpinnerMarca = (Spinner)findViewById(R.id.spinner_marca);
        mSpinnerModello = (Spinner)findViewById(R.id.spinner_modello);



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


        mAnnoimmatricolazione.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(AggiuntaAuto.this, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        mAnnoimmatricolazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AggiuntaAuto.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        List<Modello> modello = MainActivity.mySQLiteHelper.getAllModelli();


        String[] modelli = new String[modello.size()];
        int i = 0;
        for (Modello a : modello
                ) {
            modelli[i] = a.getNome();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modelli);
        mSpinnerModello.setAdapter(adapter);


        List<Marca> marca = MainActivity.mySQLiteHelper.getAllMarche();
        String[] marche = new String[marca.size()];

        int j = 0;
        for (Marca a : marca
                ) {
            marche[j] = a.getNome();
            j++;
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, marche);
        mSpinnerMarca.setAdapter(adapter1);
    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataN = sdf.format(myCalendar.getTime());
        mAnnoimmatricolazione.setText(dataN);

    }
    public boolean campiValidi(){
        if(mTarga.getText().toString().compareTo("")==0 || mEmail.getText().toString().compareTo("")==0
                || mAnnoimmatricolazione.toString().compareTo("")==0 || mChilometraggio.toString().compareTo("")==0
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
                Toast.makeText(getApplicationContext(), "Auto aggiunta", Toast.LENGTH_LONG).show();
                Log.d("done", "done");
                return true;
            }
        }else {
            Toast.makeText(getApplicationContext(), "Completa l'inserimemnto dei dati", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


}
