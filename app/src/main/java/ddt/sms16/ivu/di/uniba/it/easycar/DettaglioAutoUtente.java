package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.MieAutoFragment;

public class DettaglioAutoUtente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_auto_utente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TextView txtViewMarca = (TextView) findViewById(R.id.txtViewMarca);
        TextView txtViewModello = (TextView) findViewById(R.id.txtViewModello);
        TextView txtViewAnno = (TextView) findViewById(R.id.txtViewAnno);

        TextView txtViewCilindrata = (TextView) findViewById(R.id.txtViewCilindrata);
        TextView txtViewKm = (TextView) findViewById(R.id.txtViewKm);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int posizione = bundle.getInt(MieAutoFragment.EXTRA_POSIZIONE);
            AutoUtente auto = MainActivity.listAutoUtenteLocal.get(posizione);

            toolbar.setTitle(auto.getModello().getMarca().getNome() + " " + auto.getModello().getNome());
            setSupportActionBar(toolbar);

            txtViewMarca.setText(auto.getModello().getMarca().getNome());
            txtViewModello.setText(auto.getModello().getNome());
            txtViewAnno.setText(auto.getAnnoImmatricolazione());

            txtViewCilindrata.setText(auto.getModello().getCilindrata());
            txtViewKm.setText(String.valueOf(auto.getKm()));
        }

    }

}
