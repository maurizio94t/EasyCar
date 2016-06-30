package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.os.Bundle;
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
public class AggiungiManutenzione extends Activity {
    Button mInviaManutenzione;
    EditText mDescrizioneManutenzione;
    EditText mDataScadenza;
    EditText mChilometraggio;
    Spinner mSpinnerVeicolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manutenzioni);
          mInviaManutenzione = (Button) findViewById(R.id.bottoneAggiungiManutenzione);
          mDescrizioneManutenzione = (EditText)findViewById(R.id.descrizioneManutenzione);
          mDataScadenza = (EditText)findViewById(R.id.data_scadenza);
          mChilometraggio = (EditText)findViewById(R.id.chilometraggio_auto);
          mSpinnerVeicolo = (Spinner)findViewById(R.id.spinner_veicolo);
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
