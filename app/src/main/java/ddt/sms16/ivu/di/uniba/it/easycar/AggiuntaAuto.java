package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiuntaAuto extends Activity {
    private EditText mTarga ;
    private EditText mEmail ;
    private EditText mAnnoimmatricolazione ;
    private EditText mChilometraggio;
    private Spinner mSpinnerMarca ;
    private Spinner mSpinnerModello;
    private Button inviaAuto;
    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserimentoauto);
        mTarga = (EditText) findViewById(R.id.autoTarga);
        mEmail = (EditText) findViewById(R.id.editTxtEmail);
        mAnnoimmatricolazione = (EditText) findViewById(R.id.anno_immatricolazione);
        mChilometraggio = (EditText) findViewById(R.id.chilometraggio_auto);
        mSpinnerMarca = (Spinner)findViewById(R.id.spinner_marca);
        mSpinnerModello = (Spinner)findViewById(R.id.spinner_modello);
        inviaAuto = (Button)findViewById(R.id.inviaAuto);
        final Button deleteTarga = (Button)findViewById(R.id.delete_targa);
        final Button deleteImmatricolazione = (Button)findViewById(R.id.delete_immatricolazione);
        final Button deleteChilometraggio = (Button)findViewById(R.id.delete_chilometraggio);
        test = (TextView)findViewById(R.id.testString);

        mTarga.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                deleteTarga.setVisibility(View.VISIBLE);
            }
        });

        deleteTarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTarga.setText("");
            }
        });
        mChilometraggio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                deleteChilometraggio.setVisibility(View.VISIBLE);
            }
        });

        deleteChilometraggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChilometraggio.setText("");
            }
        });
        mAnnoimmatricolazione.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                deleteImmatricolazione.setVisibility(View.VISIBLE);
            }
        });

        deleteImmatricolazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnnoimmatricolazione.setText("");
            }
        });

        inviaAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campiValidi()){
                    test.setText("Dati corretti");
                }else{

                    test.setText("Dati non corretti");
                }
            }
        });
    }

    public boolean campiValidi(){
        //CONTROLLO FUNZIONI SPINNER
        if(mTarga.getText().toString().compareTo("")==0 || mEmail.getText().toString().compareTo("")==0
                || mAnnoimmatricolazione.toString().compareTo("")==0 || mChilometraggio.toString().compareTo("")==0
                || mSpinnerMarca.isSelected() || mSpinnerModello.isSelected()  ){
            return false;
        }
        return true;
    }


}
