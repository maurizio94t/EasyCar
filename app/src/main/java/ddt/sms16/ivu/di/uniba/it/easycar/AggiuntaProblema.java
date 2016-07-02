package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiuntaProblema extends Activity {
    EditText dettagliProblema;
    Button bottoneInvia;
    Spinner spinnerVeicolo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrizioneproblema);
        dettagliProblema =(EditText) findViewById(R.id.Problema);
        spinnerVeicolo = (Spinner) findViewById(R.id.spinner_veicolo);
        final EditText problema = (EditText)findViewById(R.id.Problema);
        bottoneInvia = (Button)findViewById(R.id.inviaProblema);
        final TextView prova = (TextView)findViewById(R.id.prova);

        bottoneInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaCampi())
                prova.setText("Dati corretti");
                prova.setText("Dati non corretti");
            }
        });
    }


    public boolean controllaCampi(){

        if(dettagliProblema.getText().toString().compareTo("")==0 || spinnerVeicolo.getSelectedItem().toString().compareTo("")==0) return false;
        else return true;
    }

}
