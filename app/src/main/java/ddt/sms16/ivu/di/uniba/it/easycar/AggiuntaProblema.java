package ddt.sms16.ivu.di.uniba.it.easycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiuntaProblema extends AppCompatActivity {
    private EditText dettagliProblema;
    private Button bottoneInvia;
    private Spinner mSpinnerVeicolo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_problema);
        dettagliProblema =(EditText) findViewById(R.id.Problema);
        mSpinnerVeicolo = (Spinner) findViewById(R.id.spinner_veicolo);
        final EditText problema = (EditText)findViewById(R.id.Problema);
        bottoneInvia = (Button)findViewById(R.id.inviaProblema);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // final TextView prova = (TextView)findViewById(R.id.prova);

     /*   bottoneInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controllaCampi())
                prova.setText("Dati corretti");
                prova.setText("Dati non corretti");
            }
        });*/

        List<AutoUtente> auto = MainActivity.mySQLiteHelper.getAllAutoUtente();


        String[] automobili = new String[auto.size()];
        int i = 0;
        for (AutoUtente a : auto
                ) {
            automobili[i] = a.getTarga();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, automobili);
        mSpinnerVeicolo.setAdapter(adapter);
    }


    public boolean controllaCampi(){

        if(dettagliProblema.getText().toString().compareTo("")==0 || mSpinnerVeicolo.getSelectedItem().toString().compareTo("")==0) return false;
        else return true;
    }

}
