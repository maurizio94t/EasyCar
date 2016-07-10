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

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaAuto;
import ddt.sms16.ivu.di.uniba.it.easycar.CustomAdapter_AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.DettaglioAutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.UpdateService;
import ddt.sms16.ivu.di.uniba.it.easycar.Utility;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;

public class MieAutoFragment extends Fragment {

    public static final String EXTRA_POSIZIONE = "posizione";

    private Context thisContext;
    private View view;
    private CustomAdapter_AutoUtente customAdapter;
    private ListView listView;
    private AutoUtente auto;

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View myView, int myItemInt, long mylng) {
                Log.e("LONG >", "OK");
                auto = (AutoUtente) (listView.getItemAtPosition(myItemInt));
                return false;
            }

        });

        registerForContextMenu(listView);
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Seleziona preferita");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Elimina");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Seleziona preferita") {
            StringRequest myReq = new StringRequest(Request.Method.POST,
                    MainActivity.urlOperations,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Resp", "> " + response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                JSONObject dati = jsonObj.getJSONObject("dati");
                                boolean update = dati.getBoolean("Update");
                                if(update) {
                                    MainActivity.mySQLiteHelper.setSelected(auto);
                                    customAdapter.clear();
                                    customAdapter = new CustomAdapter_AutoUtente(
                                            thisContext.getApplicationContext(),
                                            R.layout.row_auto,
                                            MainActivity.mySQLiteHelper.getAllMieAutoUtente());
                                    listView.setAdapter(customAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Response", "> NON FUNZIONA!");
                        }
                    }) {

                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    //insert into AutoUtente
                    params.put("operation", "u");
                    params.put("table", "AutoUtente");

                    params.put("selected", "1");
                    params.put("targa", auto.getTarga());
                    params.put("email", auto.getUtente().getEmail());

                    return params;
                }
                ;
            };
            MainActivity.queue.add(myReq);
        } else if (item.getTitle() == "Elimina") {
            
            controlloAlert();
        } else {
            return false;
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
                        eliminaAuto(auto.getTarga());

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


    private boolean eliminaAuto(final String Targa) {
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("ResponseEliminaAuto  ", String.valueOf(Targa));
                        Log.d("ResponseEliminaManutenzione", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            boolean effettuato = dati.getBoolean("Delete");
                            //Log.d("ResponseEliminaManutenzione ", effettuato);
                            if(effettuato){
                                //Log.d("ResponseEliminaManutenzione ",effettuato);
                                MainActivity.mySQLiteHelper.deleteAutoUtente(new AutoUtente(Targa));
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

                params.put("table", MainActivity.TAG_AUTOUTENTE);
                params.put("targa", String.valueOf(Targa));


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
