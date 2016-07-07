package ddt.sms16.ivu.di.uniba.it.easycar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import ddt.sms16.ivu.di.uniba.it.easycar.fragments.MieAutoFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.HomeFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ProblemiFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ScadenzeFragment;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // inizio

        StringRequest myReq1 = new StringRequest(Request.Method.POST,
                MainActivity.urlOperations,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RespOperations", "> " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RespOperations", "> NON FUNZIONA!");
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                        /*
                        //insert into AutoUtente
                        params.put("operation", "c");
                        params.put("table", "AutoUtente");

                        params.put("targa", "AA000BA");
                        params.put("km", "12099");
                        params.put("anno", "1900");
                        params.put("foto", "");
                        params.put("utente", "enrico@gmail.com");
                        params.put("modello", "1315");
                        */

                        /*
                        //insert into Manutenzioni
                        params.put("operation", "c");
                        params.put("table", "Manutenzioni");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVE");
                        params.put("data", "20160629");
                        params.put("ordinaria", "false");
                        params.put("km", "5000");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Problemi
                        params.put("operation", "c");
                        params.put("table", "Problemi");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Problema GRAVISSIMO");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Scedenze
                        params.put("operation", "c");
                        params.put("table", "Scadenze");
                        params.put("email", "enrico@gmail.com");

                        params.put("descrizione", "Scadenza AAAA");
                        params.put("data", "20151225");
                        params.put("targa", "AA000BA");
                        */

                        /*
                        //insert into Utenti
                        params.put("operation", "c");
                        params.put("table", "Utenti");

                        params.put("nome", "Giorgio");
                        params.put("cognome", "DeMarzo");
                        params.put("data", "19940614");
                        params.put("email", "giorgione@gmail.com");
                        params.put("psw", "gino");
                        */

                        /*
                        //delete
                        params.put("operation", "d");
                        params.put("table", "Manutenzioni");

                        params.put("id", "7");
                        */


                /*
                //update AutoUtente
                params.put("operation", "u");
                params.put("table", "AutoUtente");

                params.put("targa", "ZZZZZZZ");
                params.put("km", "10");
                params.put("anno", "2010");
                params.put("modello", "1316");
                */

                /*
                //update Manutenzioni
                params.put("operation", "u");
                params.put("table", "Manutenzioni");

                params.put("id", "2");
                params.put("descrizione", "Cambio gommeee");
                params.put("data", "2010-12-29");
                params.put("ordinaria", "1");
                params.put("km", "1000");
                params.put("targa", "BF564TGA");
                */

                /*
                //update Problemi
                params.put("operation", "u");
                params.put("table", "Problemi");

                params.put("id", "4");
                params.put("descrizione", "Casse rotte!");
                params.put("targa", "BF564TG");
                */


                /*
                //update Scadenza
                params.put("operation", "u");
                params.put("table", "Scadenze");

                params.put("id", "4");
                params.put("descrizione", "Bollo forrt!");
                params.put("data", "2013-02-11!");
                params.put("targa", "AB678BN");
                */

                /*
                params.put("operation", "u");
                params.put("table", "AutoUtente");

                params.put("selected", "1");
                params.put("targa", "BF564TG");
                params.put("email", "maur_izzio@live.it");
                */

                return params;
            }

            ;
        };
        MainActivity.queue.add(myReq1);

        // fine

        //aggiornamento del db locale se c'è connessione
        if(Utility.checkInternetConnection(getApplicationContext())) {
            UpdateService.aggiornaDataBaseLocale();
        } else {
            Toast.makeText(getApplicationContext(), "Connessione non presente!", Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Faccio partire il primo Fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment());
        ft.commit();

        navigationView.getMenu().getItem(0).setChecked(true);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
        } else if (id == R.id.nav_mie_auto) {
            fragment = new MieAutoFragment();
            ok = true;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_scadenze) {
            fragment = new ScadenzeFragment();
            ok = true;

        } else if (id == R.id.nav_problemi) {
            fragment = new ProblemiFragment();
            ok = true;
        } else if (id == R.id.manutenzioni) {

        } else if (id == R.id.nav_info) {
            Intent gpsTest = new Intent(this, PosizioneAuto.class);
            startActivity(gpsTest);
        }

        if (ok) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //ft.addToBackStack(null);
            ft.commit();
        }

        item.setChecked(true);
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
