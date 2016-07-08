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

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaManutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;

/**
 * Created by Giuseppe-PC on 08/07/2016.
 */
public class ManutenzioniFragment extends Fragment {
    private Context thisContext;
    private View view;
    private CustomAdapter_Manutenzione customAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_manutenzioni, container, false);

        customAdapter = new CustomAdapter_Manutenzione(
                thisContext.getApplicationContext(),
                R.layout.row_manutenzione,
                MainActivity.mySQLiteHelper.getAllManutenzioni());

        //utilizzo dell'adapter
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.aggingiManutenzione);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent aggiuntaManutenzione = new Intent(getActivity(), AggiuntaManutenzione.class);
                startActivity(aggiuntaManutenzione);
            }
        });
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Elimina");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Modifica");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Elimina"){
            Toast.makeText(getContext(),"Codice elimina",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="Modifica"){

        }else{
            return false;
        }
        return true;
    }


}
