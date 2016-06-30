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

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaScadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.R;


/**
 * Created by Maurizio on 01/06/16.
 */
public class ScadenzeFragment extends Fragment {
    Context thisContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scadenze, container, false);



        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.aggingiScadenza);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("fragment","funzionaaa");
                /*

                Fragment fragment = new AggiuntaScadenzaFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                */
                Intent aggiuntaScadenza = new Intent(getActivity(), AggiuntaScadenza.class);
                startActivity(aggiuntaScadenza);
            }
        });





        return view;
    }

}
