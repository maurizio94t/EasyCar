package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.AggiuntaProblema;
import ddt.sms16.ivu.di.uniba.it.easycar.ExpandListAdapter;
import ddt.sms16.ivu.di.uniba.it.easycar.Group;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.UpdateService;
import ddt.sms16.ivu.di.uniba.it.easycar.Utility;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;

/**
 * Created by Maurizio on 01/06/16.
 */
public class ProblemiFragment2 extends Fragment {
    public static Context thisContext;
    View view;
    public static ExpandListAdapter ExpAdapter;
    public static ArrayList<Group> ExpListItems;
    public static ExpandableListView ExpandList;
    private Problema problema;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_problemi, container, false);

        Log.d("ProblemiFragment2 >", "ENTRA");

        ExpandList = (ExpandableListView) view.findViewById(R.id.exp_list);

        // get the listview
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        ExpandList.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

        ExpListItems = SetStandardGroups();
        ExpAdapter = new ExpandListAdapter(thisContext, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);


        ExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Problema selectedFromList = (Problema) ExpAdapter.getChild(groupPosition, childPosition);
                problema=selectedFromList;
                if(MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "").equalsIgnoreCase(problema.getAuto().getUtente().getEmail())) {
                    controlloAlert();
                }else{
                    Toast.makeText(getContext(),"Problema segnalato da altro utente",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });



        /*
        ExpandList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,View myView, int myItemInt, long mylng) {
                Problema selectedFromList =(Problema) (ExpandList.getItemAtPosition(myItemInt));
                problema=selectedFromList;
                return false;
            }

        });*/
        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.aggingiProblema);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent aggiuntaProblema = new Intent(getActivity(), AggiuntaProblema.class);
                startActivity(aggiuntaProblema);
            }
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Elimina");
        // menu.add(0, v.getId(), 0, "Modifica");
    }

    private boolean controlloAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder.setTitle(R.string.TitoloDialog);

        alertDialogBuilder
                .setMessage(R.string.MessageDialog)
                .setCancelable(false)
                .setPositiveButton(R.string.PositiveButton,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                      eliminaProblema(problema.getIDProblema());
                    }
                })
                .setNegativeButton(R.string.NegtiveButton,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        return true;
    }

    private boolean eliminaProblema(final int idProblema){
        final boolean[] aggiunto = new boolean[1];
        StringRequest myReq = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", "> OK Req");
                        Log.d("ResponseEliminaProblema  ",String.valueOf(idProblema));
                        Log.d("ResponseEliminaProblema", response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject dati = jsonObj.getJSONObject("dati");
                            boolean delete = dati.getBoolean("Delete");
                            if(delete) {
                                MainActivity.mySQLiteHelper.deleteProblema(new Problema(idProblema));

                                //aggiorno la listview
                                Log.d("AGGIORNAMENTO", "OKK");
                                ExpListItems = SetStandardGroups();
                                ExpAdapter = new ExpandListAdapter(thisContext, ExpListItems);
                                ExpandList.setAdapter(ExpAdapter);
                                ExpAdapter.notifyDataSetChanged();
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

                params.put("table", MainActivity.TAG_PROBLEMI);
                params.put("id", String.valueOf(idProblema));


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
    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public static ArrayList<Group> SetStandardGroups() {

        List<AutoUtente> listaAutoUtente = MainActivity.mySQLiteHelper.getAllMieAutoUtente();

        String[] group_names = new String[listaAutoUtente.size()];
        int i = 0;
        for(AutoUtente a : listaAutoUtente) {
            group_names[i++] = a.getModello().getMarca().getNome() + " " + a.getModello().getNome();
        }

        ArrayList<Group> list = new ArrayList<Group>();

        for (AutoUtente a : listaAutoUtente) {
            Group gru = new Group();
            gru.setName(a.getModello().getMarca().getNome() + " " + a.getModello().getNome());
            List<Problema> ch_listaProblemiAuto = MainActivity.mySQLiteHelper.getAllProblemiByAuto(a);

            gru.setItems(ch_listaProblemiAuto);
            list.add(gru);
        }
        return list;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_problemi, container, false);

        // get the listview
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        expListView = (ExpandableListView) view.findViewById(R.id.exp_list);
        expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });





        final FloatingActionButton aggiuntaProblemi = (FloatingActionButton)  view.findViewById(R.id.aggingiProblema);
        aggiuntaProblemi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentAggiuntaProblema = new Intent(getActivity(), AggiuntaProblema.class);
                startActivity(intentAggiuntaProblema);
            }
        });

        registerForContextMenu(expListView);
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
    public boolean onContextItemSelected(MenuItem item) {
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

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    //Preparing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
    */
}
