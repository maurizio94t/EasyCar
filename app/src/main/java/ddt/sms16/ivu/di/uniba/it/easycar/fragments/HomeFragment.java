package ddt.sms16.ivu.di.uniba.it.easycar.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Storico;
import ddt.sms16.ivu.di.uniba.it.easycar.DettaglioAutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.MySQLiteHelper;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.SalvaPosizione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Modello;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Utente;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HomeFragment extends Fragment {

    private Context thisContext;
    private View view;
    private CustomAdapter_Storico customAdapter;
    private ListView listView;

    private List<Manutenzione> listaManutenzioni;
    private List<Scadenza> listaScadenze;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Button btnSalvaPosizione = (Button) view.findViewById(R.id.btnPosizione);
        btnSalvaPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent salvaPos = new Intent(getContext(), SalvaPosizione.class);
                startActivity(salvaPos);
            }
        });

        listaManutenzioni = MainActivity.mySQLiteHelper.getAllManutenzioni();
        listaScadenze = MainActivity.mySQLiteHelper.getAllScadenze();

        selectLists();

        List<Object> listaStorico = merge();
        customAdapter = new CustomAdapter_Storico(
                thisContext.getApplicationContext(),
                R.layout.row_storico,
                listaStorico);

        //utilizzo dell'adapter
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        return view;
    }

    private void selectLists() {
        //String email = MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "");

        List<Manutenzione> man = new LinkedList<Manutenzione>();
        for(Manutenzione m : listaManutenzioni) {
            if(m.getAuto().getSelected() == 1) {
                man.add(m);
            }
        }

        List<Scadenza> sca = new LinkedList<Scadenza>();
        for(Scadenza s : listaScadenze) {
            if (s.getAuto().getSelected() == 1) {
                sca.add(s);
            }
        }

        listaManutenzioni = man;
        listaScadenze = sca;
    }

    private List<Object> merge() {
        //int[] answer = new int[listaManutenzioni.size() + listaScadenze.size()];
        List<Object> answer = new LinkedList<Object>();
        int i = 0, j = 0, k = 0;

        while (i < listaManutenzioni.size() && j < listaScadenze.size()) {
            if (listaManutenzioni.get(i).beforeScadenza(listaScadenze.get(j)))
                answer.add(k++,listaManutenzioni.get(i++)); //[k++] = a[i++];
            else
                answer.add(k++, listaScadenze.get(j++));    //answer[k++] = b[j++];
        }

        while (i < listaManutenzioni.size())
            answer.add(k++, listaManutenzioni.get(i++));    //answer[k++] = a[i++];


        while (j < listaScadenze.size())
            answer.add(k++, listaScadenze.get(j++));        //answer[k++] = b[j++];

        return answer;
    }

}