package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrazioneUtente extends AppCompatActivity {
    private EditText mNome;
    private EditText mCognome;
    private EditText mData;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRipetiPassword;
    private Button mRegistra;
    int anno, mese, giorno = 0;
    private String dataN;
    private Calendar myCalendar = Calendar.getInstance();
    private String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_utente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
         mNome = (EditText)findViewById(R.id.nomeUtente);
         mCognome = (EditText)findViewById(R.id.cognomUtente);
         mData = (EditText)findViewById(R.id.dataUtente);
         mEmail = (EditText)findViewById(R.id.email);
         mPassword = (EditText)findViewById(R.id.password);
         mRipetiPassword = (EditText)findViewById(R.id.repeatPassword);
        mRegistra = (Button)findViewById(R.id.btnRegistraUtente);

        mRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!campiValidi()){
                    Toast.makeText(getApplicationContext(), "Campi non validi", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Registrato", Toast.LENGTH_LONG).show();
                }
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



    }
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        Calendar today = Calendar.getInstance();

        dataN = sdf.format(myCalendar.getTime());
        mData.setText(dataN);

    }
    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mNome.getText().toString().compareTo("")==0 || mCognome.getText().toString().compareTo("")==0
                || mData.toString().compareTo("")==0 && mEmail.toString().compareTo(emailPattern)==0
                ){
            if(mPassword.getText().toString().compareTo(mRipetiPassword.getText().toString())!=0){
                Toast.makeText(getApplication(),"La password non corrisponde",Toast.LENGTH_LONG);
            }
            return false;
        }
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
