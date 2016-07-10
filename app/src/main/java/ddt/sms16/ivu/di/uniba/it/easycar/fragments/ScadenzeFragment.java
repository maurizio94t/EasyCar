package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaScadenza;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Scadenze;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.UpdateService;
import ddt.sms16.ivu.di.uniba.it.easycar.Utility;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Scadenza;

public class ScadenzeFragment extends Fragment {
    private Context thisContext;
    private View view;
    public static CustomAdapter_Scadenze customAdapter;
    private ListView listView;
    public Scadenza scadenza;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_scadenze, container, false);

        customAdapter = new CustomAdapter_Scadenze(
                thisContext.getApplicationContext(),
                R.layout.row_scadenza,
                MainActivity.mySQLiteHelper.getAllScadenzeOrdinate());

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View myView, int myItemInt, long mylng) {
                Scadenza selectedFromList = (Scadenza) (listView.getItemAtPosition(myItemInt));
                scadenza = selectedFromList;
                return false;
            }

        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Elimina");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Elimina"){
            controlloAlert();
        } else {
            return false;
        }
        return true;
    }

    private boolean controlloAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder.setTitle("Sei sicuro di voler eliminare?");

        alertDialogBuilder
                .setMessage("Click su si per confermare")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        eliminaScadenza(scadenza.getIDScadenza());
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        return true;
    }

    private boolean eliminaScadenza(final int idScadenza){
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("ResponseEliminaScadenza  ",String.valueOf(idScadenza));
                        Log.d("ResponseEliminaScadenza", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            boolean delete =dati.getBoolean("Delete");
                            if(delete) {
                                MainActivity.mySQLiteHelper.deleteScadenza(new Scadenza(idScadenza));
                                //aggiorno la listview
                                customAdapter.clear();
                                customAdapter.addAll(MainActivity.mySQLiteHelper.getAllScadenzeOrdinate());
                                customAdapter.notifyDataSetChanged();
                            }
                            aggiunto[0] = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", "> That didn't work!");
                        aggiunto[0] = false;
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("operation", "d");

                params.put("table", MainActivity.TAG_SCADENZE);
                params.put("id", String.valueOf(idScadenza));


                return params;
            };
        };
        if(Utility.checkInternetConnection(getActivity().getApplicationContext())) {
            MainActivity.queue.add(myReq);
        } else {
            UpdateService.requests.add(myReq);
        }

        return aggiunto[0];
    }

}
