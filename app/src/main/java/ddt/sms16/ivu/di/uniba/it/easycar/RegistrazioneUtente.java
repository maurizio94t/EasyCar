package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrazioneUtente extends Activity {
    EditText mNome;
    EditText mCognome;
    EditText mData;
    EditText mEmail;
    Button cancellaNome;
    Button cancellaCognome;
    Button cancellaData;
    Button cancellaMail;
    Button registraUtente;
    TextView testString;

    String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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

    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mNome.getText().toString().compareTo("")==0 || mCognome.getText().toString().compareTo("")==0
                || mData.toString().compareTo("")==0 && mEmail.toString().compareTo(emailPattern)!=0
                ){
            return false;
        }
        return true;
    }
}
