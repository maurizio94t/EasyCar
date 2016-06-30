package ddt.sms16.ivu.di.uniba.it.easycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiungiManutenzione extends AppCompatActivity {
    Button mInviaManutenzione;
    EditText mDescrizioneManutenzione;
    EditText mDataScadenza;
    EditText mChilometraggio;
    Spinner mSpinnerVeicolo;


    WebRequest webRequest;
    URL paginaURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manutenzioni);
          mInviaManutenzione = (Button) findViewById(R.id.bottoneAggiungiManutenzione);
          mDescrizioneManutenzione = (EditText)findViewById(R.id.descrizioneManutenzione);
          mDataScadenza = (EditText)findViewById(R.id.data_scadenza);
          mChilometraggio = (EditText)findViewById(R.id.chilometraggio_auto);
          mSpinnerVeicolo = (Spinner)findViewById(R.id.spinner_veicolo);
// Set a toolbar to replace the action bar.
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setLogo(R.drawable.ic_calendar_clock_grey600_18dp);
            toolbar.setTitle("Manutenzioni");
        mInviaManutenzione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    paginaURL = new URL("http://t2j.no-ip.org/phpmyadmin");
                }catch (IOException e){
                    e.getMessage();
                }

                if(campiValidi()){
                    String descrizione = mDescrizioneManutenzione.getText().toString();
                    String data = mDataScadenza.getText().toString();
                    String chilometraggio = mChilometraggio.getText().toString();
                    String spinnerVeicolo = mSpinnerVeicolo.getSelectedItem().toString();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Descrizione",descrizione);
                    params.put("Data",data);
                    params.put("Chilometraggio",chilometraggio);
                    params.put("Veicolo",spinnerVeicolo);
                    webRequest.makeWebServiceCall(paginaURL.toString(),WebRequest.GETRequest,params);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
