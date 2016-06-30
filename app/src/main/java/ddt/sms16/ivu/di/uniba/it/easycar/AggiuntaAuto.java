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
public class AggiuntaAuto extends Activity {
    private EditText mTarga = (EditText) findViewById(R.id.autoTarga);
    private EditText mEmail = (EditText) findViewById(R.id.email);
    private EditText mAnnoimmatricolazione = (EditText) findViewById(R.id.anno_immatricolazione);
    private EditText mChilometraggio = (EditText) findViewById(R.id.chilometraggio_auto);
    private Spinner mSpinnerMarca = (Spinner)findViewById(R.id.spinner_marca);
    private Spinner mSpinnerModello = (Spinner)findViewById(R.id.spinner_modello);
    private Button inviaAggiunta = (Button)findViewById(R.id.inviaAuto);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimentoauto);
    }

    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mTarga.getText().toString().compareTo("")==0 && mEmail.getText().toString().compareTo("")==0
                && mAnnoimmatricolazione.toString().compareTo("")==0 && mChilometraggio.toString().compareTo("")==0
                && mSpinnerMarca.isSelected() && mSpinnerModello.isSelected()  ){
            return true;
        }
        return false;
    }
}
