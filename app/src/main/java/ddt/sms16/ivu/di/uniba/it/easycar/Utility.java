package ddt.sms16.ivu.di.uniba.it.easycar;

/**
 * Created by Enrico on 22/06/16.
 */
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility{
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static int [] getData(String str){
        String[] data = str.split("-");
        int [] dataConvert = new int[3];
        dataConvert[0]=Integer.parseInt(data[0]);
        dataConvert[1]=Integer.parseInt(data[1]);
        dataConvert[2]=Integer.parseInt(data[2]);
        return dataConvert;
    }
    public static boolean checkInternetConnection(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }


        public static String controllaMese(int m){
            String mese = "";

            switch (m){
                case 1: return "GEN";
                case 2: return "FEB";
                case 3: return "MAR";
                case 4: return "APR";
                case 5: return "MAG";
                case 6: return "GIU";
                case 7: return "LUG";
                case 8: return "AGO";
                case 9: return "SET";
                case 10: return "OTT";
                case 11: return "NOV";
                case 12: return "DIC";
            }
            return mese;
        }

    public static Date convertStringToDate(String data) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.ITALIAN);
            Date date = null;
            date = format.parse(data);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertDateToString(Date data) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String s = df.format(data);
        return s;
    }

    public static String convertStringDateToString(String data) {
        String gg= data.substring(0,2);
        String mm= data.substring(3,5);
        String aa= data.substring(6,10);
       return aa+mm+gg;
    }

    public static String convertStringDateToStringIt(String data) {
        String aaaa = data.substring(0,4);
        String mm = data.substring(5,7);
        String gg= data.substring(8,10);
        return gg + "/" + mm + "/" + aaaa;
    }
    public static String estraiTarga(String auto){
        int lunghezza = auto.length();
        int numTrattini=0;
        for (int i=0;i<lunghezza;i++){
            if(auto.charAt(i)=='-'){
                numTrattini++;
                if(numTrattini==1){
                    return auto.substring(lunghezza-i);
                }
            }
        }
        return "";
    }
}
