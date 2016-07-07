package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Giuseppe-PC on 07/07/2016.
 */
public class PosizioneAuto extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap map;
    private LatLng POSIZIONE_AUTO;
    protected static final String TAG = "Posizione Auto";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLocation;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private Activity activity;
    private LocationManager mlocManager;
    private LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posizione_auto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        buildGoogleApiClient();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mLocation = (TextView)findViewById(R.id.location);


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            Geocoder geocoder;
            List<Address> addresses = new ArrayList<>();
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mLocation.setText(addresses.get(0).getAddressLine(0));
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }

        POSIZIONE_AUTO = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(POSIZIONE_AUTO)
                .title("La tua auto")
                );


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(POSIZIONE_AUTO, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }



    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done) {
            Log.d("done","done");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start() {
        activity = PosizioneAuto.this;
            mlocManager = (LocationManager) activity
                    .getSystemService(Context.LOCATION_SERVICE);

            if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                findLoc();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    activity);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activity.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }
    }

    public void findLoc() {
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1,
                (android.location.LocationListener) gpsListener);

        if (mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null)
            Toast.makeText(activity, "LAST Location null", Toast.LENGTH_SHORT)
                    .show();
        else {
            gpsListener.onLocationChanged(mlocManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(activity, "Connetion failed", Toast.LENGTH_SHORT)
                .show();
    }
}
