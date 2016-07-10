package ddt.sms16.ivu.di.uniba.it.easycar.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Storico;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.SalvaPosizione;
import ddt.sms16.ivu.di.uniba.it.easycar.Utility;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;



public class HomeFragment extends Fragment {

    private Context thisContext;
    private View view;
    private CustomAdapter_Storico customAdapter;
    private ListView listView;
    public static boolean save = false;
    private List<Manutenzione> listaManutenzioni;
    private List<Scadenza> listaScadenze;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView auto = (TextView)view.findViewById(R.id.textAuto);

        Button btnSalvaPosizione = (Button) view.findViewById(R.id.btnPosizione);
        btnSalvaPosizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.checkInternetConnection(thisContext)) {
                    Intent salvaPos = new Intent(getActivity().getApplicationContext(), SalvaPosizione.class);//getContext()
                    startActivity(salvaPos);
                    save = true;
                }
            }
        });

        listaManutenzioni = MainActivity.mySQLiteHelper.getAllManutenzioniOrdinate();
        listaScadenze = MainActivity.mySQLiteHelper.getAllScadenzeOrdinate();

        selectLists();

        List<Object> listaStorico = merge();
        customAdapter = new CustomAdapter_Storico(
                thisContext.getApplicationContext(),
                R.layout.row_storico,
                listaStorico);


        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);
        //AutoUtente a = MainActivity.mySQLiteHelper.getAutoPreferita();
        //auto.setText(a.getModello().getMarca().getNome()+" "+a.getModello().getNome());
        return view;
    }

    private void selectLists() {
        List<Manutenzione> man = new LinkedList<Manutenzione>();
        for (Manutenzione m : listaManutenzioni) {
            if (m.getAuto().getSelected() == 1) {
                man.add(m);
            }
        }

        List<Scadenza> sca = new LinkedList<Scadenza>();
        for (Scadenza s : listaScadenze) {
            if (s.getAuto().getSelected() == 1) {
                sca.add(s);
            }
        }

        listaManutenzioni = man;
        listaScadenze = sca;
    }

    private List<Object> merge() {
        List<Object> answer = new LinkedList<Object>();
        int i = 0, j = 0, k = 0;

        while (i < listaManutenzioni.size() && j < listaScadenze.size()) {
            if (listaManutenzioni.get(i).afterScadenza(listaScadenze.get(j)))
                answer.add(k++, listaManutenzioni.get(i++));
            else
                answer.add(k++, listaScadenze.get(j++));
        }

        while (i < listaManutenzioni.size())
            answer.add(k++, listaManutenzioni.get(i++));


        while (j < listaScadenze.size())
            answer.add(k++, listaScadenze.get(j++));

        return answer;
    }

}