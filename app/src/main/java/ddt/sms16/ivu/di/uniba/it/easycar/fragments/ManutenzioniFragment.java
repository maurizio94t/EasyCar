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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaManutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_Manutenzione;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.UpdateService;
import ddt.sms16.ivu.di.uniba.it.easycar.Utility;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Manutenzione;

public class ManutenzioniFragment extends Fragment {
    private Context thisContext;
    private View view;
    private CustomAdapter_Manutenzione customAdapter;
    private ListView listView;
    private boolean alert;
    Manutenzione manutezione;

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


        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.aggingiManutenzione);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent aggiuntaManutenzione = new Intent(getActivity(), AggiuntaManutenzione.class);
                startActivity(aggiuntaManutenzione);
            }
        });
        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View myView, int myItemInt, long mylng) {
                Manutenzione selectedFromList = (Manutenzione) (listView.getItemAtPosition(myItemInt));
                manutezione = selectedFromList;
                return false;
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View myView, int myItemInt, long mylng) {
                Manutenzione selectedFromList = (Manutenzione) (listView.getItemAtPosition(myItemInt));
                manutezione = selectedFromList;
                return false;
            }

        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    //    menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Elimina");//groupId, itemId, order, title
     //   menu.add(0, v.getId(), 0, "Modifica");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Elimina") {
            controlloAlert();
            Toast.makeText(getContext(),"Elimina",Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean controlloAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set title
        alertDialogBuilder.setTitle("Sei sicuro di voler eliminare?");

        alertDialogBuilder
                .setMessage("Click su si per confermare")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       // eliminaManutenzione(manutezione.getIDManutenzione());
                        customAdapter = new CustomAdapter_Manutenzione(
                                thisContext.getApplicationContext(),
                                R.layout.row_manutenzione,
                                MainActivity.mySQLiteHelper.getAllManutenzioni());
                        listView = (ListView) view.findViewById(R.id.listView);
                        listView.setAdapter(customAdapter);

                        eliminaManutenzione(manutezione.getIDManutenzione());
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       alert = false;
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        return alert;
    }


    private boolean eliminaManutenzione(final int idManutenzione) {
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("ResponseEliminaManutenzione  ", String.valueOf(idManutenzione));
                        Log.d("ResponseEliminaManutenzione", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            String effettuato =dati.getString("Delete");
                            Log.d("ResponseEliminaManutenzione ", effettuato);
                            if(effettuato.equalsIgnoreCase("true")){
                                Log.d("ResponseEliminaManutenzione ",effettuato);
                                MainActivity.mySQLiteHelper.deleteManutezione(new Manutenzione(idManutenzione));
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

                params.put("table", MainActivity.TAG_MANUTENZIONI);
                params.put("id", String.valueOf(idManutenzione));


                return params;
            }

            ;
        };
        if (Utility.checkInternetConnection(getActivity().getApplicationContext())) {
            MainActivity.queue.add(myReq);
        } else {
            UpdateService.requests.add(myReq);
        }

        return aggiunto[0];
    }
}
