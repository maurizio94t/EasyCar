package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaAuto;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.DettaglioAutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

public class MieAutoFragment extends Fragment {

    public static final String EXTRA_POSIZIONE = "posizione";

    private Context thisContext;
    private View view;
    private CustomAdapter_AutoUtente customAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_mie_auto, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAggiuntaAuto = new Intent(getActivity(), AggiuntaAuto.class);
                startActivity(intentAggiuntaAuto);
            }
        });

        customAdapter = new CustomAdapter_AutoUtente(
                thisContext.getApplicationContext(),
                R.layout.row_auto,
                MainActivity.mySQLiteHelper.getAllMieAutoUtente());

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







        registerForContextMenu(listView);
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Seleziona preferita");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Elimina");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Seleziona preferita"){
            Toast.makeText(getContext(),"Codice elimina",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Elimina"){
            Toast.makeText(getContext(),"Codice elimina",Toast.LENGTH_LONG).show();
        }else {
            return false;
        }
        return true;
    }
}
