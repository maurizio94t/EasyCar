package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ImageView giuseppe = (ImageView)findViewById(R.id.giuseppe);
        ImageView maurizio = (ImageView)findViewById(R.id.maurizio);
        ImageView enrico = (ImageView)findViewById(R.id.enrico);

        giuseppe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/gdemarzo2?fref=ts"));
                startActivity(browserIntent);
            }
        });
        enrico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/enricodelia03?fref=ts"));
                startActivity(browserIntent);
            }
        });
        maurizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mau.troiani?fref=ts"));
                startActivity(browserIntent);
            }
        });
    }
}
