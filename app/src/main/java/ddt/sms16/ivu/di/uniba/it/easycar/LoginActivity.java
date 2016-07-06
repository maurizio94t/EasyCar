package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private String pswEncrypted;

    private String jsonStr;
    private EditText mEditTxtEmail;
    private EditText mEditTxtPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        mEditTxtPsw = (EditText) findViewById(R.id.editTxtPsw);

        Button btnAccedi = (Button) findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cifro la password
                AeSimpleSHA1 aeSimpleSHA1 = new AeSimpleSHA1();
                try {
                    // sostituire con TextEdit
                    pswEncrypted = aeSimpleSHA1.SHA1("prova");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                StringRequest myReq = new StringRequest(Request.Method.POST,
                        MainActivity.url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                jsonStr = response;
                                Log.d("Response", "> OK FETCH DB");
                                //new GetData(mEditTxtEmail.getText().toString(), mEditTxtPsw.getText().toString()).execute();
                                new GetData().execute();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Response", "> That didn't work!");
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        // sostituire con TextEdit
                        params.put("email", "maur_izzio@live.it");
                        params.put("psw", pswEncrypted);
                        return params;
                    }

                    ;
                };
                MainActivity.queue.add(myReq);


                StringRequest myReq1 = new StringRequest(Request.Method.POST,
                        MainActivity.urlOperations,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("RespOperations", "> " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("RespOperations", "> NON FUNZIONA!");
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        /*
                        //insert into AutoUtente
                        params.put("operation", "c");
                        params.put("table", "AutoUtente");

                        params.put("targa", "AA000BA");
                        params.put("km", "12099");
                        params.put("anno", "1900");
                        params.put("foto", "");
                        params.put("utente", "enrico@gmail.com");
                        params.put("modello", "1315");
                        */

                        /*
                        //insert into Manutenzioni
                        params.put("operation", "c");
                        params.put("table", "Manutenzioni");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVE");
                        params.put("data", "20160629");
                        params.put("ordinaria", "false");
                        params.put("km", "5000");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Problemi
                        params.put("operation", "c");
                        params.put("table", "Problemi");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVISSIMO");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Scedenze
                        params.put("operation", "c");
                        params.put("table", "Scadenze");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Scadenza AAAA");
                        params.put("data", "20151225");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Utenti
                        params.put("operation", "c");
                        params.put("table", "Utenti");

                        params.put("nome", "Giorgio");
                        params.put("cognome", "DeMarzo");
                        params.put("data", "19940614");
                        params.put("email", "giorgione@gmail.com");
                        params.put("psw", "gino");
                        */

                        /*
                        //delete
                        params.put("operation", "d");
                        params.put("table", "Manutenzioni");

                        params.put("id", "7");
                        */

                        return params;
                    }

                    ;
                };
                MainActivity.queue.add(myReq1);
            }
        });
        /*
        Button bottonePrendiFoto = (Button) findViewById(R.id.bottonPrendiFoto);
        bottonePrendiFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent prendiFoto = new Intent(MainActivity.this, PrendiFoto.class);
                startActivity(prendiFoto);
            }
        });
        */
    }

    public boolean ParseJSON(String json) {
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);

                MainActivity.utenteVerificato = jsonObj.getBoolean(MainActivity.TAG_UTENTE_VERIFICATO);

                if (!MainActivity.utenteVerificato) {
                    return false;
                }
                UpdateService.parse(jsonObj);

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return false;
        }
    }

    // AsynkTask
    public class GetData extends AsyncTask<Void, Void, Void> {
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Mostro la progress loading Dialog
            proDialog = new ProgressDialog(LoginActivity.this);
            proDialog.setMessage("Caricamento in corso...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Response", "> " + jsonStr);

            ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
            /**
             * Updating received data from JSON into ListView
             * */
            if (MainActivity.utenteVerificato) {
                SharedPreferences.Editor editor = MainActivity.sharedpreferences.edit();
                editor.putBoolean(MainActivity.TAG_UTENTE_VERIFICATO, true);
                editor.putString(MainActivity.TAG_UTENTE_NOME, MainActivity.utenteLoggato.getNome());
                editor.putString(MainActivity.TAG_UTENTE_COGNOME, MainActivity.utenteLoggato.getCognome());
                editor.putString(MainActivity.TAG_UTENTE_DATANASCITA, MainActivity.utenteLoggato.getDataN());
                editor.putString(MainActivity.TAG_UTENTE_EMAIL, MainActivity.utenteLoggato.getEmail());
                editor.putString(MainActivity.TAG_UTENTE_PSW, MainActivity.utenteLoggato.getPsw());
                editor.commit();

                Intent intentBaseActivity = new Intent(LoginActivity.this, BaseActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intentBaseActivity);
                finish();

                // Start a service for update Local DB
                //Intent intentService = new Intent(LoginActivity.this, UpdateService.class);
                //startService(intentService);
            } else {
                View parentLayout = findViewById(R.id.root_view);
                Snackbar snackbar = Snackbar.make(parentLayout, "Dati non corretti! Riprova..", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}
