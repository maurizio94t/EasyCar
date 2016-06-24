package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

/**
 * Created by Maurizio on 01/06/16.
 */
public class HomeFragment extends Fragment {
    Context thisContext;
    View view;
    CustomAdapter_AutoUtente carsAdapter;
    ListView carsListView;
    ArrayList<AutoUtente> arrayAutoUtente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_home, container, false);
        arrayAutoUtente = MainActivity.listaAutoUtente;
        //arrayAutoUtente = new ArrayList<AutoUtente>();
        //arrayAutoUtente.add(new AutoUtente("BZ907PF", 12500, "2016", R.drawable.ic_menu_gallery, "maur_izzio@live.it", new Modello(1,"Panda", "A", "Benzina", "900", "13.3", new Marca(1, "FIAT")), false));
        //arrayAutoUtente.add(new AutoUtente("DX008JM", 15000, "2016", R.drawable.ic_menu_gallery, "maur_izzio@live.it", new Modello(2,"Punto", "A", "Benzina/GPL", "900", "13.3", new Marca(2, "FIAT")), false));

        /*
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        ListAdapter adapter = new SimpleAdapter(
                container.getContext(), MainActivity.listaAutoUtente,
                R.layout.list_item,
                new String[]{MainActivity.TAG_AUTOUTENTE_TARGA, MainActivity.TAG_AUTOUTENTE_MODELLI_ID, MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE},
                new int[]{R.id.nome, R.id.cognome, R.id.email});

        listView.setAdapter(adapter);
        */

        //INIZIO

        carsAdapter = new CustomAdapter_AutoUtente(
                thisContext.getApplicationContext(),
                R.layout.row_auto,
                arrayAutoUtente);

        //utilizzo dell'adapter
        carsListView = (ListView)view.findViewById(R.id.listView);
        carsListView.setAdapter(carsAdapter);


        //click lungo su item
        registerForContextMenu(carsListView);
        //FINE


        return view;
    }

}
