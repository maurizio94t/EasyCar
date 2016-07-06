package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ddt.sms16.ivu.di.uniba.it.easycar.DettaglioAutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

public class MieAutoFragment extends Fragment {

    public static final String EXTRA_POSIZIONE = "posizione";

    Context thisContext;
    View view;
    CustomAdapter_AutoUtente customAdapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_mie_auto, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        //INIZIO OK

        customAdapter = new CustomAdapter_AutoUtente(
                thisContext.getApplicationContext(),
                R.layout.row_auto,
                MainActivity.listAutoUtenteLocal);

        //utilizzo dell'adapter
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDettaglioAuto = new Intent(thisContext, DettaglioAutoUtente.class);
                intentDettaglioAuto.putExtra(EXTRA_POSIZIONE, position);
                startActivity(intentDettaglioAuto);
            }
        });

        //FINE OK

        return view;
    }

}
