package com.example.hackmitwork;

import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.*;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap nMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final String TAG = "Google Maps";
    private static final float DEFAULT_ZOOM = 15f;

    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        nMap = googleMap;
    }
    private void getDeviceLocation(){
      mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

      try {
        final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
               if(task.isSuccessful()){
                   Log.d("Sucess","Task was sucesfull");
                   Location currentLocation = (Location) task.getResult();
                   moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM);
               }
               else {
                   Log.d("Failure","Current location not found");
                   Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
               }
            }
        });
      }
      catch (SecurityException e) {
          Log.e("Error", e.getMessage());
      }
    }
    private void moveCamera(LatLng latLng,float zoom){
        Log.d(TAG,"Camera moved to lat: " + latLng.latitude + " and long: " + latLng.longitude);
        nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();
        getDeviceLocation();
        nMap.setMyLocationEnabled(true);


    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

}
