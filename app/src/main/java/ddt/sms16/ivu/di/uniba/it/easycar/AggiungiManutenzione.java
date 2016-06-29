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
    Button mInviaManutenzione = (Button) findViewById(R.id.bottoneAggiungiManutenzione);
    EditText mDescrizioneManutenzione = (EditText)findViewById(R.id.descrizioneManutenzione);
    EditText mDataScadenza = (EditText)findViewById(R.id.data_scadenza);
    EditText mChilometraggio = (EditText)findViewById(R.id.chilometraggio_auto);
    Spinner mSpinnerVeicolo = (Spinner)findViewById(R.id.spinner_veicolo);
    WebRequest webRequest;
    URL paginaURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungimanutenzione);


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
