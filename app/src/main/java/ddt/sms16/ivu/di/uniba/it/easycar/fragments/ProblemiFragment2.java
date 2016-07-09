package ddt.sms16.ivu.di.uniba.it.easycar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import ddt.sms16.ivu.di.uniba.it.easycar.Child;
import ddt.sms16.ivu.di.uniba.it.easycar.ExpandListAdapter;
import ddt.sms16.ivu.di.uniba.it.easycar.Group;
import ddt.sms16.ivu.di.uniba.it.easycar.MainActivity;
import ddt.sms16.ivu.di.uniba.it.easycar.R;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.AutoUtente;
import ddt.sms16.ivu.di.uniba.it.easycar.entity.Problema;

/**
 * Created by Maurizio on 01/06/16.
 */
public class ProblemiFragment2 extends Fragment {
    private Context thisContext;
    View view;

    private ExpandListAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thisContext = container.getContext();
        view = inflater.inflate(R.layout.fragment_problemi, container, false);

        ExpandList = (ExpandableListView) view.findViewById(R.id.exp_list);
        // inizio prova

        // get the listview
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        ExpandList.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

        //fine prova
        ExpListItems = SetStandardGroups();
        ExpAdapter = new ExpandListAdapter(thisContext, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);

        return view;
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public ArrayList<Group> SetStandardGroups() {

        List<AutoUtente> listaAutoUtente = MainActivity.mySQLiteHelper.getAllMieAutoUtente();

        String[] group_names = new String[listaAutoUtente.size()];
        int i = 0;
        for(AutoUtente a : listaAutoUtente) {
            group_names[i++] = a.getModello().getMarca().getNome() + " " + a.getModello().getNome();
        }

        ArrayList<Group> list = new ArrayList<Group>();
        ArrayList<Child> ch_list;

        for (AutoUtente a : listaAutoUtente) {
            Group gru = new Group();
            gru.setName(a.getModello().getMarca().getNome() + " " + a.getModello().getNome());

            ch_list = new ArrayList<Child>();
            List<Problema> listaProblemiAuto = MainActivity.mySQLiteHelper.getAllProblemiByAuto(a);
            for (Problema p : listaProblemiAuto) {
                Child ch = new Child();
                ch.setName(p.getDescrizione());
                ch.setImage(R.drawable.ic_map);
                if(isYourProblema(p))
                    ch.setYour(true);
                else
                    ch.setYour(false);
                ch_list.add(ch);
            }
            gru.setItems(ch_list);
            list.add(gru);
        }
        return list;
    }

    private boolean isYourProblema(Problema p) {
        Log.e("EMAIL SHARED_P >", MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, ""));
        Log.e("EMAIL PROBLEMA>", p.getAuto().getUtente().getEmail());
        if(MainActivity.sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, "").equalsIgnoreCase(p.getAuto().getUtente().getEmail())) {
            return true;
        }
        return  false;
        /*
        List<AutoUtente> listaAuto = MainActivity.mySQLiteHelper.getAllMieAutoUtente();
        for(AutoUtente a : listaAuto) {
            if(a.getTarga().equalsIgnoreCase(p.getAuto().getTarga())) {
                return true;
            }
        }
        return false;
        */
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