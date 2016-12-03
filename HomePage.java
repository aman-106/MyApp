package com.example.aj047884.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePage extends AppCompatActivity {

    private static final String tag = "HomePage";
    private static Logger logger;
    Location location;
    double latitude;
    double longitude;
    TextView latitudeval;
    TextView longitudeval;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean isGpsEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        final Context context = getApplicationContext();
        this.getInputFieldValue();


        Log.e(tag, "home page activity started:");

//         using the location listener to get gps locaton ..................

        Log.d(tag, "setting the location listener class");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Log.d(tag, "calling the function to enable the gps ");
        this.enableGps();

    }


    //         GETTING THE GPS COORDINATES
    public void getGpsUpdates(View View) {
        Log.d(tag, "gps enable: " + isGpsEnable);
        int someint = 0;
        if (this.isGpsEnable == true) {
            Log.d(tag, "granted permission: " + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
            Log.d("permession granted :", ""+ PackageManager.PERMISSION_GRANTED );
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.d(tag, "Should show rationale");
                    // TODO: Display a fragment or something here.


                } else {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, someint);
                    Log.d(tag, "else loop");
                }


            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
            Log.d(tag, "location should ne null:" + location);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d(tag, "location: " + location);
                if (location != null) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d(tag, "values: " + latitude + " : " + longitude);
                    this.setFieldValues();
                }
            }



        } else {
            this.enableGps();
        }


    }

    private void setFieldValues() {
        Log.d(tag, "setting the values");
        latitudeval.setText(String.valueOf(latitude));
        longitudeval.setText(String.valueOf(longitude));
    }


    protected void enableGps() {
        //        MAKING THE GPS ENABLE ..............................
        Log.d(tag, "gps enabled through intent");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Enable Location");
            dialog.setMessage("Your Locations Settings is set to 'Off'.\n Please Enable Location ");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }

                    }
            );
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int paramInt) {
//                do nothing
                }
            });

            AlertDialog alert = dialog.create();
            alert.show();


        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.isGpsEnable = true;
        }


    }


    protected void getInputFieldValue() {

        this.latitudeval = (TextView) findViewById(R.id.latvalue);
        this.longitudeval = (TextView) findViewById(R.id.longval);
    }


}
