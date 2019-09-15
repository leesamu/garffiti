package com.example.hackmitwork;

import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.content.Intent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.io.OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap nMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final String TAG = "Google Maps";
    private static final float DEFAULT_ZOOM = 15f;
    private Button mGps;
    private String tagName = "David";

private LatLng getMarkers(){
    try {
        String url = "172.16.143.201:5000";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject myResponse = new JSONObject(response.toString());
        Log.d(TAG,myResponse.toString());
        System.out.println("Loaded");
        //myResponse.

    }
    catch (MalformedURLException e){
        e.getStackTrace();
    }
    catch (IOException i){
        i.getStackTrace();
    }
    catch(JSONException j){
        j.getStackTrace();
    }
    return null;
}
private void sendPost(LatLng latLng, String tag){
    try {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        URL url = new URL("35.194.84.11:5000/gps");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonString = new JSONObject()
                .put("lat", latitude)
                .put("long", longitude)
                .put("tag", tag).toString();

        OutputStream os = con.getOutputStream();
        byte[] input = jsonString.getBytes("utf-8");
        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        System.out.println(response.toString());

    }
    catch (MalformedURLException m){
        m.getStackTrace();
    }
    catch (IOException i){
        i.getStackTrace();
    }
    catch (JSONException j){
        j.getStackTrace();
    }
}

    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        nMap = googleMap;
        getDeviceLocation(false);
        nMap.setMyLocationEnabled(true);

    }

    private void getDeviceLocation(final boolean setTag){
      mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

      try {
        final Task location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
               if(task.isSuccessful()){
                   Log.d("Sucess","Task was sucesfull");
                   Location currentLocation = (Location) task.getResult();
                   LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                   moveCamera(latLng,DEFAULT_ZOOM);
                   if(setTag){
                       sendPost(latLng,tagName);
                   }

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
    private void renderMarker(){
        getDeviceLocation(true);
        startActivity(new Intent(MapActivity.this,ExploreActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();
        mGps = (Button) findViewById(R.id.markerbtn);
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderMarker();
            }
        });


    }


    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

}
