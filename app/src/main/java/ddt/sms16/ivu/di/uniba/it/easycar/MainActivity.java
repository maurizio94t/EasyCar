package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bottone = (Button) findViewById(R.id.button);


  bottone.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {




                URL url = null;

                try {
                    url = new URL("http://t2j.no-ip.org/AAA/WebService.php");

                    HttpURLConnection client = null;

                    client = (HttpURLConnection) url.openConnection();
                    client.setRequestMethod("POST");
                    client.setRequestProperty("nome", "Alfredo");
                    client.setRequestProperty("cognome", "Alfredo");
                    client.setDoOutput(true);

                    Toast.makeText(MainActivity.this, "mi sono collegato bastà", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });



        //Questo è per TTTJJJ
    }
}
