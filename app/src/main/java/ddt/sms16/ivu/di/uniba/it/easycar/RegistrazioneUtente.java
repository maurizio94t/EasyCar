package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrazioneUtente extends Activity {
    private EditText mNome;
    private EditText mCognome;
    private EditText mData;
    private EditText mEmail;
    private Button cancellaNome;
    private Button cancellaCognome;
    private Button cancellaData;
    private Button cancellaMail;
    private Button registraUtente;
    private TextView testString;
    int anno, mese, giorno = 0;
    private String dataN;
    private Calendar myCalendar = Calendar.getInstance();
    private String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_utente);
         mNome = (EditText)findViewById(R.id.nomeUtente);
         mCognome = (EditText)findViewById(R.id.cognomUtente);
         mData = (EditText)findViewById(R.id.dataUtente);
         mEmail = (EditText)findViewById(R.id.email);
        registraUtente = (Button) findViewById(R.id.registraUtente);
        cancellaNome = (Button)findViewById(R.id.cancella_nome);
        cancellaCognome = (Button)findViewById(R.id.cancella_cognome);
        cancellaData = (Button)findViewById(R.id.cancella_data);
        cancellaMail = (Button)findViewById(R.id.cancella_mail);
        testString = (TextView) findViewById(R.id.testString);

        mNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaNome.setVisibility(View.VISIBLE);
            }
        });

        mCognome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaCognome.setVisibility(View.VISIBLE);
            }
        });

        mData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaData.setVisibility(View.VISIBLE);
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

        mData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(RegistrazioneUtente.this, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });
        mData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getApplicationContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellaMail.setVisibility(View.VISIBLE);
            }
        });
        cancellaNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNome.setText("");
            }
        });
        cancellaCognome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCognome.setText("");
            }
        });
        cancellaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.setText("");
            }
        });
        cancellaMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail.setText("");
            }
        });

        registraUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campiValidi()){
                    testString.setText("Campi validi");
                }else{
                    testString.setText("Campi non validi");
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
        mData.setText(dataN);

    }
    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mNome.getText().toString().compareTo("")==0 || mCognome.getText().toString().compareTo("")==0
                || mData.toString().compareTo("")==0 && mEmail.toString().compareTo(emailPattern)==0
                ){
            return false;
        }
        return true;
    }
}
