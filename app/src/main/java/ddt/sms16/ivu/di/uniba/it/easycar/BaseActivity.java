package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ddt.sms16.ivu.di.uniba.it.easycar.entity.Marca;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.MieAutoFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.OneFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ProblemiFragment;
import ddt.sms16.ivu.di.uniba.it.easycar.fragments.ScadenzeFragment;

/**
 * Created by Maurizio on 01/06/16.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //aggiornamente del db locare se c'è connessione
        //aggiornaDataBaseLocale();

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
        ft.replace(R.id.fragment_container, new OneFragment());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;

        boolean ok = false;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new OneFragment();
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
       Intent aggiuntaManutenzione = new Intent(this, AggiungiManutenzione.class);
            startActivity(aggiuntaManutenzione);

        } else if (id == R.id.nav_info) {

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

    public void aggiornaDataBaseLocale() {
        MainActivity.listMarcaLocal = MainActivity.mySQLiteHelper.getAllMarche();
        boolean trovato = false;
        for(Marca marcaE : MainActivity.listaMarche) {
            for(Marca marcaL : MainActivity.listMarcaLocal) {
                if(marcaE.getIDMarca() == marcaL.getIDMarca()) {
                    trovato = true;
                    break;
                }
            }
            if(!trovato) {
                MainActivity.mySQLiteHelper.aggiungiMarca(marcaE);
            }
        }

        // get tutte le AutoUtente
        MainActivity.listMarcaLocal = MainActivity.mySQLiteHelper.getAllMarche();
    }

}
