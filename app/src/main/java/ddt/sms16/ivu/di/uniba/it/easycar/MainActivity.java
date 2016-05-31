package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ddt.sms16.ivu.di.uniba.it.easycar.fragments.HomeFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.OneFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // URL to get data JSON
    private static String url = "http://t2j.no-ip.org/ddt/WebService.php";

    // JSON Node names
    public static final String TAG_UTENTI = "utenti";
    public static final String TAG_UTENTE_NOME = "nome";
    public static final String TAG_UTENTE_COGNOME = "cognome";
    public static final String TAG_UTENTE_DATANASCITA = "dataN";
    public static final String TAG_UTENTE_EMAIL = "email";

    // Hashmap per la ListView
    public static ArrayList<HashMap<String, String>> listaUtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetUsers().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Faccio partire il primo Fragment
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.fragment_container, new HomeFragment());
        //ft.commit();

        //navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        boolean ok = false;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            ok = true;
        } else if (id == R.id.nav_one) {
            fragment = new OneFragment();
            ok = true;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if(ok) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        }

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // AsynkTask
    private class GetUsers extends AsyncTask<Void, Void, Void> {
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Mostro la progress loading Dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Caricamento in corso...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creo un'istanza di WebRequest per effettuare una richiesta al server
            WebRequest webreq = new WebRequest();

            // Faccio una richiesta all'url dichiarato come variabile di classe e prendo la risposta
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POSTRequest);

            Log.d("Response: ", "> " + jsonStr);

            listaUtenti = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            if (proDialog.isShowing())
                proDialog.dismiss();
            /**
             * Updating received data from JSON into ListView
             * */

            /*  AL MOMENTO NON ABBIAMO QUI LA LISTA
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, listaUtenti,
                    R.layout.list_item,
                    new String[]{TAG_UTENTE_NOME, TAG_UTENTE_COGNOME, TAG_UTENTE_EMAIL},
                    new int[]{R.id.nome, R.id.cognome, R.id.email});

            setListAdapter(adapter);
            */
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap per la ListView
                ArrayList<HashMap<String, String>> listaUtenti = new ArrayList<HashMap<String, String>>();
                JSONObject jsonObj = new JSONObject(json);

                // Prelevo JSON Array node
                JSONArray utenti = jsonObj.getJSONArray(TAG_UTENTI);

                // Ciclo tutti gli utenti
                for (int i = 0; i < utenti.length(); i++) {
                    JSONObject u = utenti.getJSONObject(i);

                    String nome = u.getString(TAG_UTENTE_NOME);
                    String cognome = u.getString(TAG_UTENTE_COGNOME);
                    String dataN = u.getString(TAG_UTENTE_DATANASCITA);
                    String email = u.getString(TAG_UTENTE_EMAIL);

                    // hashmap per il singolo utente
                    HashMap<String, String> utente = new HashMap<String, String>();

                    // aggiungo tutti i campi dell'utente all'HashMap
                    utente.put(TAG_UTENTE_NOME, nome);
                    utente.put(TAG_UTENTE_COGNOME, cognome);
                    utente.put(TAG_UTENTE_DATANASCITA, dataN);
                    utente.put(TAG_UTENTE_EMAIL, email);

                    // aggiungo il singolo studente alla lista di studenti
                    listaUtenti.add(utente);
                }
                return listaUtenti;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }
}
