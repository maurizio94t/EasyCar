package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
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
public class AggiuntaManutenzione extends Activity {
    Button mInviaManutenzione;
    private EditText mDescrizioneManutenzione;
    private EditText mDataScadenza;
    private EditText mChilometraggio;
    private Spinner mSpinnerVeicolo;
    int anno, mese, giorno = 0;
    String dataN;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Button cancellaDescrizione;
        final Button cancellaChilometraggio;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutenzioni);

          mDescrizioneManutenzione = (EditText)findViewById(R.id.descrizioneManutenzione);
          mDataScadenza = (EditText)findViewById(R.id.dataManutenzione);
          mChilometraggio = (EditText)findViewById(R.id.chilometraggio_auto);
          mSpinnerVeicolo = (Spinner)findViewById(R.id.spinner_veicolo);
          Button invia = (Button)findViewById(R.id.inviaManutenzione);
        cancellaDescrizione = (Button)findViewById(R.id.delete_descrizione);
        cancellaChilometraggio = (Button)findViewById(R.id.delete_chilometri);


        mDescrizioneManutenzione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaDescrizione.setVisibility(View.VISIBLE);
            }
        });

        cancellaDescrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDescrizioneManutenzione.setText("");
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
                new DatePickerDialog(getApplicationContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });


        mChilometraggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaChilometraggio.setVisibility(View.VISIBLE);
            }
        });

        cancellaChilometraggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChilometraggio.setText("");
            }
        });

        invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campiValidi()){
                    TextView t = (TextView)findViewById(R.id.controllo);
                    t.setText("Campi validi");
                }else{
                    TextView t = (TextView)findViewById(R.id.controllo);
                    t.setText("Campi NON validi");
                }
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
        mDataScadenza.setText(dataN);

    }
    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mDescrizioneManutenzione.getText().toString().compareTo("")==0 && mDataScadenza.getText().toString().compareTo("")==0
                && mChilometraggio.toString().compareTo("")==0 && mSpinnerVeicolo.toString().compareTo("")==0
                ){
            return true;
        }
        return false;
    }
}
