package com.example.karans.mapdemotrial;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.backendless.Backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected LocationManager locationManager;
    List<LatLng> latLngList = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        user u1 = new user();
        u1.setFname("karan");
        u1.setLname("shaha");
        u1.setMobilenumber(777777);

        Backendless.initApp(getApplicationContext(),"BABB30B0-852A-B795-FFDB-EEE621EE6D00","42BC4DEC-A747-D477-FF3F-09AB8A331100","v1");
        Toast.makeText(getApplicationContext(),"Success b4",Toast.LENGTH_LONG).show();

        Backendless.Persistence.save( u1, new AsyncCallback<user>() {
            public void handleResponse( user response )
            {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }

            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpMapIfNeeded() {
        // check if we have got the googleMap already
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            /*if (mMap != null) {
                addLines();
            }*/

        }
    }

    private void addLines(List<LatLng> latLngs) {


        // move camera to zoom on map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0),
                13));
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(getApplicationContext(),"in location changed",Toast.LENGTH_LONG);
            /*if (ActivityCompat.checkSelfPermission(, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);*/
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude,longitude);
            latLngList.add(latLng);

            mMap
                    .addPolyline((new PolylineOptions())
                            .addAll(latLngList).width(5).color(Color.BLUE)
                            .geodesic(true));

            mMap.addMarker(new MarkerOptions().position(latLng).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngList.get(0)));
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
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Add a marker in Sydney and move the camera
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Toast.makeText(getApplicationContext(),"before requestLocationUpdates",Toast.LENGTH_LONG);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000,
                2, locationListener);


    }


}
