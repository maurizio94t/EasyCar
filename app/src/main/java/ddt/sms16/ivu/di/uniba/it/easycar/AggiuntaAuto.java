package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
        setContentView(R.layout.activity_inserimentoauto);
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
        inviaAuto = (Button)findViewById(R.id.inviaAuto);


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
        mAnnoimmatricolazione.setText(dataN);

    }
    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mTarga.getText().toString().compareTo("")==0 || mEmail.getText().toString().compareTo("")==0
                || mAnnoimmatricolazione.toString().compareTo("")==0 || mChilometraggio.toString().compareTo("")==0
                || mSpinnerMarca.isSelected() || mSpinnerModello.isSelected()  ){
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            Log.d("done","done");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
