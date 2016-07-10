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
import android.widget.Toast;

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
    private Button btnRegistrati;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTxtEmail = (EditText) findViewById(R.id.editTxtEmail);
        mEditTxtPsw = (EditText) findViewById(R.id.editTxtPsw);

        Button btnAccedi = (Button) findViewById(R.id.btnAccedi);
        Button btnRegistrati = (Button) findViewById(R.id.btnRegistati);

        Intent intent = getIntent();
        if(intent != null){
        String registrato = intent.getStringExtra("registrato");

        if(registrato !=null ){
            if(registrato.equalsIgnoreCase("true")){

                Toast.makeText(getApplicationContext(), "Registazione avvenuta con successo!", Toast.LENGTH_LONG).show();

            }

        }}
        btnRegistrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registraUtente = new Intent(getApplication(),RegistrazioneUtente.class);
                startActivity(registraUtente);
            }
        });
        btnAccedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cifro la password
                AeSimpleSHA1 aeSimpleSHA1 = new AeSimpleSHA1();
                try {
                    // sostituire con TextEdit
                    pswEncrypted = aeSimpleSHA1.SHA1(mEditTxtPsw.getText().toString());
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
                                new GetData().execute();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Response", "> That didn't work!");
                                View parentLayout = findViewById(R.id.root_view);
                                Snackbar snackbar = Snackbar.make(parentLayout, "Connessione non presente!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }) {

                    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        // sostituire con TextEdit
                        params.put("email", mEditTxtEmail.getText().toString());
                        params.put("psw", pswEncrypted);
                        return params;
                    }

                    ;
                };
                MainActivity.queue.add(myReq);
            }
        });
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
