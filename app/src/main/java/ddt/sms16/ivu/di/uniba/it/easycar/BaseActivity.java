package ddt.sms16.ivu.di.uniba.it.easycar;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import ddt.sms16.ivu.di.uniba.it.easycar.fragments.HomeFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ManutenzioniFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.MieAutoFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ProblemiFragment2;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ScadenzeFragment;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Intent intentService;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPreferences";
    public boolean GPSenabled = false;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        // Start a service for update Local DB
        intentService = new Intent(this, UpdateService.class);
        startService(intentService);

        // controllo se il GPS è attivo
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GPSenabled = true;
        } else {
            showGPSDisabledAlertToUser();
        }

        // inizializzo la coda di request da fare in caso di connessione assente
        UpdateService.requests = new ArrayList<StringRequest>();

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


        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView textNavName = (TextView) header.findViewById(R.id.textNavName);
        TextView textNavEmail = (TextView) header.findViewById(R.id.textNavEmail);
        ImageButton btnLogout = (ImageButton) header.findViewById(R.id.btnLogout);

        textNavName.setText(sharedpreferences.getString(MainActivity.TAG_UTENTE_NOME, "") + " " + sharedpreferences.getString(MainActivity.TAG_UTENTE_COGNOME, ""));
        textNavEmail.setText(sharedpreferences.getString(MainActivity.TAG_UTENTE_EMAIL, ""));

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove(MainActivity.TAG_UTENTE_VERIFICATO);
                editor.remove(MainActivity.TAG_UTENTE_NOME);
                editor.remove(MainActivity.TAG_UTENTE_COGNOME);
                editor.remove(MainActivity.TAG_UTENTE_DATANASCITA);
                editor.remove(MainActivity.TAG_UTENTE_EMAIL);
                editor.remove(MainActivity.TAG_UTENTE_PSW);
                editor.commit();

                stopService(intentService);

                Intent intentLogin = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        });

        // Faccio partire il primo Fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
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



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        boolean ok = false;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            ok = true;
        } else if (id == R.id.nav_mie_auto) {
            fragment = new MieAutoFragment();
            ok = true;
        } else if (id == R.id.manutenzioni) {
            fragment = new ManutenzioniFragment();
            ok = true;
        } else if (id == R.id.nav_scadenze) {
            fragment = new ScadenzeFragment();
            ok = true;
        } else if (id == R.id.nav_problemi) {
            fragment = new ProblemiFragment2();
            ok = true;
        } else if (id == R.id.nav_posizione_auto) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String salvata = preferences.getString("Salvata", "DEFAULT");
         /* sharedpreferences.getString("Salvata","").toString()
            SharedPreferences.Editor prefEditor = sharedpreferences.edit();
         */
            Log.d("fanculo","fanculoooo");

            Log.d("fanculo",salvata);
            if(salvata.equalsIgnoreCase("DEFAULT")){
            Intent posizioneAuto = new Intent(this, PosizioneAuto.class);
            startActivity(posizioneAuto);
            }else{
                Toast.makeText(getApplicationContext(),"Posizione non salvata",Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_info) {
            Intent info = new Intent(this, Info.class);
            startActivity(info);
        }

        if (ok) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            //ft.addToBackStack(null);
            ft.commit();
            item.setChecked(true);
        }
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.MessaggioGps)
                .setCancelable(false)
                .setPositiveButton(R.string.MessaggioGps1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.MessaggioGps2,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
