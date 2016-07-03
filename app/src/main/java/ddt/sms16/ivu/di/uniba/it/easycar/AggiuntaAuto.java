package ddt.sms16.ivu.di.uniba.it.easycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Giuseppe-PC on 29/06/2016.
 */
public class AggiuntaAuto extends AppCompatActivity {
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






        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        if (id == R.id.done) {
            Log.d("done","done");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
