package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button bottoneIvia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descrizioneproblema);
        dettagliProblema =(EditText) findViewById(R.id.editTextProblema);
        bottoneIvia = (Button) findViewById(R.id.buttonIviaProblema);

    }
}
