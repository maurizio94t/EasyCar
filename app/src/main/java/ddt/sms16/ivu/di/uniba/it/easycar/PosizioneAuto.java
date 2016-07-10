package ddt.sms16.ivu.di.uniba.it.easycar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected TextView mLocation;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private Activity activity;
    private LocationManager mlocManager;
    private LocationListener gpsListener;
    public static SharedPreferences sharedpreferences;
    protected static final String TAG = "Posizione Auto";
    public static final String MyPREFERENCES = "MyPreferences";
    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posizione_auto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String latitude = sharedpreferences.getString("Latitude","");
        String longitude = sharedpreferences.getString("Longitude","");
        double latitudeP = Double.parseDouble(latitude);
        double longitudeP = Double.parseDouble(longitude);
        try {
            addresses = geocoder.getFromLocation(latitudeP,longitudeP, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(latitudeP == 0 && longitudeP ==0){
            Toast.makeText(activity, "Sono a 0", Toast.LENGTH_SHORT)
                    .show();
        }
        POSIZIONE_AUTO = new LatLng(latitudeP,longitudeP);
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(POSIZIONE_AUTO)
                .title("La tua auto")
                );


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(POSIZIONE_AUTO, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        mLocation.setText(addresses.get(0).getAddressLine(0));
    }



    @Override
    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }





    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(activity, "Connetion failed", Toast.LENGTH_SHORT)
                .show();
    }
}
