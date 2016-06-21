package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Maurizio on 01/06/16.
 */
public class HomeFragment extends Fragment {

    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        ListAdapter adapter = new SimpleAdapter(
                container.getContext(), MainActivity.listaAutoUtente,
                R.layout.list_item,
                new String[]{MainActivity.TAG_AUTOUTENTE_TARGA, MainActivity.TAG_AUTOUTENTE_MODELLI_ID, MainActivity.TAG_AUTOUTENTE_ANNO_IMMATRICOLAZIONE},
                new int[]{R.id.nome, R.id.cognome, R.id.email});

        listView.setAdapter(adapter);

        return view;
    }

}
