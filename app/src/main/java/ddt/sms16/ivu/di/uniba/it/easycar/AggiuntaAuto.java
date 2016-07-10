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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;

import static android.R.layout.simple_spinner_item;


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




        final List<Marca> marca = MainActivity.mySQLiteHelper.getAllMarche();
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
                Log.d("marcaSelezionata",marcaSelezionata.getIDMarca() +" "+marcaSelezionata.getNome());

                List<Modello> listaModelli = MainActivity.mySQLiteHelper.getAllModelliDiMarca(marcaSelezionata);
                for (Modello m : listaModelli
                     ) {
                    Log.d("marcaSelezionata",m.getNome());

                }

Modello[] modelli = new Modello[listaModelli.size()];
                int i = 0;
              for (Modello a : listaModelli) {
                    modelli[i] = a;
                    i++;
                }

                ArrayAdapter<Modello> adapter = new ArrayAdapter<Modello>(AggiuntaAuto.super.getApplicationContext(),
                        android.R.layout.simple_spinner_item, modelli);

                mSpinnerModello.setAdapter(adapter);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)
        /*
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              // Marca marcaSelezionata= (Marca) mSpinnerMarca.getSelectedItem();
              //  List<Modello> modello = MainActivity.mySQLiteHelper.getAllModelliDiMarca(marcaSelezionata);


           //     String[] modelli = new String[modello.size()];
                int i = 0;
          /*      for (Modello a : modello) {
                    modelli[i] = a.getNome();
                    i++;
                }*/

              /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(AggiuntaAuto.super.getApplicationContext(),
                        android.R.layout.simple_spinner_item, modelli);

                mSpinnerModello.setAdapter(adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

/*
        mSpinnerMarca.setOnItemClickListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             List<Modello> modello = MainActivity.mySQLiteHelper.getAllModelli();


                String[] modelli = new String[modello.size()];
                int i = 0;
                for (Modello a : modello) {
                    modelli[i] = a.getNome();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AggiuntaAuto.super.getApplicationContext(),
                        android.R.layout.simple_spinner_item, modelli);
                mSpinnerModello.setAdapter(adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));
        */
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataN = sdf.format(myCalendar.getTime());
        mAnnoimmatricolazione.setText(dataN);

    }
    public boolean campiValidi(){
        if(mTarga.getText().toString().compareTo("")==0 || mEmail.getText().toString().compareTo("")==0
                || mAnnoimmatricolazione.getText().toString().compareTo("")==0 || mChilometraggio.getText().toString().compareTo("")==0
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
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "Auto aggiunta", Snackbar.LENGTH_LONG);
                snackbar.show();
                Log.d("done", "done");
                return true;
            }
        }else {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Campi non completi", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return super.onOptionsItemSelected(item);
    }


}
