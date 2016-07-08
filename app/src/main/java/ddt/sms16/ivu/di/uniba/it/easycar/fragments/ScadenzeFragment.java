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
import android.widget.ListView;
import android.widget.Toast;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaScadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Scadenze;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

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

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.aggingiScadenza);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent aggiuntaScadenza = new Intent(getActivity(), AggiuntaScadenza.class);
                startActivity(aggiuntaScadenza);
            }
        });
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Elimina");
        menu.add(0, v.getId(), 0, "Modifica");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Elimina"){
            Toast.makeText(getContext(),"Codice elimina",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Modifica"){
            Toast.makeText(getContext(),"Codice modifica", Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

}
