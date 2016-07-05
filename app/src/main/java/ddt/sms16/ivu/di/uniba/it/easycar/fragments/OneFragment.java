package ddt.sms16.ivu.di.uniba.it.easycar.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ddt.sms16.ivu.di.uniba.it.easycar.MySQLiteHelper;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;


public class OneFragment extends Fragment {

    Context thisContext;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_one, container, false);

        Button b = (Button) view.findViewById(R.id.btnIntend);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(getActivity().getApplicationContext());
                mySQLiteHelper.aggiungiModello(new Modello(23,"Enrico","","","","",new Marca("ciao")));
                mySQLiteHelper.getAllModelli();
                // mySQLiteHelper.getAllAutoUtente();
                // Intent intent0 = new Intent(getActivity(), MyCameraActivity.class);
                //startActivity(intent0);

                /*

                -----> !!! Enrico ti ho commentato questa roba xk ho tolto il commento della password nel costruttore !!! <-----

                mySQLiteHelper.aggiungiUtente(new Utente("Enrico", "d'Elia", "1996-04-04", "enrico96@gmail.com"));
                Utente utente = mySQLiteHelper.prendiUtente("enrico96@gmail.com");
                if (utente != null)
                    Log.d("utente preso: ", utente.toString());
                else
                    Log.d("utente ", "null");
                */


            }
        });


        return view;
    }

}