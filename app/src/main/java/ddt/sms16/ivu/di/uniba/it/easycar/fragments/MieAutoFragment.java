package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Maurizio on 01/06/16.
 */
public class MieAutoFragment extends Fragment {
    Context thisContext;
    View view;
    CustomAdapter_AutoUtente customAdapter;
    ListView carsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_mie_auto, container, false);

        //INIZIO OK

        customAdapter = new CustomAdapter_AutoUtente(
                thisContext.getApplicationContext(),
                R.layout.row_auto,
                MainActivity.listaAutoUtente);

        //utilizzo dell'adapter
        carsListView = (ListView)view.findViewById(R.id.listView);
        carsListView.setAdapter(customAdapter);

        //FINE OK

        return view;
    }

}
