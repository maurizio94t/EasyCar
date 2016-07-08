package ddt.sms16.ivu.di.uniba.it.easycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<AutoUtente> auto = MainActivity.mySQLiteHelper.getAllAutoUtente();

        String[] automobili = new String[auto.size()];
        int i = 0;
        for (AutoUtente a : auto
                ) {
            automobili[i] = a.getModello().getMarca().getNome()+" "+a.getModello().getNome()+"-"+a.getTarga();
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
        if(controllaCampi()) {
            if (id == R.id.done) {
                Toast.makeText(getApplicationContext(), "Problema aggiunto", Toast.LENGTH_LONG).show();
                Log.d("done", "done");
                return true;
            }
        }else {
            Toast.makeText(getApplicationContext(), "Completa l'inserimemnto dei dati", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
