package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaScadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Scadenze;
import ddt.sms16.ivu.di.uniba.it.easycar.DettaglioAutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;


/**
 * Created by Maurizio on 01/06/16.
 */
public class ScadenzeFragment extends Fragment {
    private Context thisContext;
    private View view;
    private CustomAdapter_Scadenze customAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_scadenze, container, false);

        customAdapter = new CustomAdapter_Scadenze(
                thisContext.getApplicationContext(),
                R.layout.row_scadenza,
                MainActivity.mySQLiteHelper.getAllScadenze());

        //utilizzo dell'adapter
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);


        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.aggingiScadenza);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent aggiuntaScadenza = new Intent(getActivity(), AggiuntaScadenza.class);
                startActivity(aggiuntaScadenza);
            }
        });
        return view;
    }

}
