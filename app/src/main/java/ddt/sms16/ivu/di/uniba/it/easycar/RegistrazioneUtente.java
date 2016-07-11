package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;

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
    private String nome;
    private String cognome;
    private String data;
    private String email;
    private String password;
    private String ripetiPassword;
    private boolean emailNonEsistente = true;
    String passwordCriptata = "";

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
        mNome = (EditText) findViewById(R.id.nomeUtente);
        mCognome = (EditText) findViewById(R.id.cognomUtente);
        mData = (EditText) findViewById(R.id.dataUtente);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mRipetiPassword = (EditText) findViewById(R.id.repeatPassword);
        mRegistra = (Button) findViewById(R.id.btnRegistraUtente);

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
                new DatePickerDialog(RegistrazioneUtente.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        mRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                if (controlloCampi()) {
                    snackbar = Snackbar.make(findViewById(android.R.id.content), "Campi non validi", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    if (password.equalsIgnoreCase(ripetiPassword)) {
                        AeSimpleSHA1 sha1 = new AeSimpleSHA1();
                        try {
                            passwordCriptata = sha1.SHA1(password);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        StringRequest myReq = new StringRequest(Request.Method.POST,
                                MainActivity.urlOperations,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObj = new JSONObject(response);
                                            JSONObject dati = jsonObj.getJSONObject("dati");
                                            String emailE = dati.getString("EmailNotExist");
                                            emailNonEsistente = Boolean.parseBoolean(emailE);
                                            boolean added = dati.getBoolean("Added");
                                            if (emailNonEsistente && added) {
                                                MainActivity.mySQLiteHelper.aggiungiUtente(new Utente(nome, cognome, data, email, passwordCriptata));

                                                Intent intent = new Intent(getApplication(), LoginActivity.class);
                                                intent.putExtra("registrato", "true");
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Snackbar snackbar;
                                                if (!emailNonEsistente && !added) {
                                                    snackbar = Snackbar.make(findViewById(android.R.id.content), "Errore: e-mail già esistente!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                } else if (!emailNonEsistente && !added) {
                                                    snackbar = Snackbar.make(findViewById(android.R.id.content), "Errore: registrazione non avvenuta!", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {

                            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("operation", "c");
                                params.put("table", MainActivity.TAG_UTENTI);
                                params.put("nome", nome);
                                params.put("cognome", cognome);
                                params.put("data", Utility.convertStringDateToString(data));
                                params.put("email", email);
                                params.put("psw", password);

                                return params;
                            }

                            ;
                        };
                        if (Utility.checkInternetConnection(getApplicationContext())) {
                            MainActivity.queue.add(myReq);
                        } else {
                            Toast.makeText(getApplicationContext(), "Impossibile effettuare la registrazione senza internet!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password non uguali!", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });
        /*
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
        */
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataN = sdf.format(myCalendar.getTime());
        mData.setText(dataN);

    }

    /*
    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ITALY);

        dataN = sdf.format(myCalendar.getTime());
        mData.setText(dataN);

    }
    */

    public boolean controlloCampi() {

        if (mNome.getText().toString().equalsIgnoreCase("") || mCognome.getText().toString().equalsIgnoreCase("") ||
                mData.getText().toString().equalsIgnoreCase("") || mEmail.getText().toString().equalsIgnoreCase("") ||
                mPassword.getText().toString().equalsIgnoreCase("") || mRipetiPassword.getText().toString().equalsIgnoreCase("")) {
            return true;
        } else {
            nome = mNome.getText().toString();
            cognome = mCognome.getText().toString();
            data = mData.getText().toString();
            email = mEmail.getText().toString();
            password = mPassword.getText().toString();
            ripetiPassword = mRipetiPassword.getText().toString();
            return false;
        }
    }

}





