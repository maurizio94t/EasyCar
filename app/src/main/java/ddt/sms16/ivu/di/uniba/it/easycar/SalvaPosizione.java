package ddt.sms16.ivu.di.uniba.it.easycar;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Giuseppe-PC on 07/07/2016.
 */
public class SalvaPosizione extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    public static SharedPreferences sharedpreferences;
    protected static final String TAG = "Posizione Auto";
    public static final String MyPREFERENCES = "MyPreferences";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        buildGoogleApiClient();

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
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor prefEditor = sharedpreferences.edit();
        Toast.makeText(SalvaPosizione.this, "Latitude"+mLastLocation.getLatitude(), Toast.LENGTH_SHORT)
                .show();
        prefEditor.putString("Latitude", String.valueOf(mLastLocation.getLatitude()));
        prefEditor.putString("Longitude", String.valueOf(mLastLocation.getLongitude()));
        prefEditor.commit();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(SalvaPosizione.this, "Connetion failed", Toast.LENGTH_SHORT)
                .show();
    }
}