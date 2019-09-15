package com.example.hackmitwork;

import java.io.IOException;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.io.UnsupportedEncodingException;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import java.util.HashMap;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import android.os.Bundle;
import android.content.Intent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.android.volley.Request.Method;
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
import java.util.Map;
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
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.android.volley.AuthFailureError;


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
private void sendPost2(LatLng latLng, String tag){

    try {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://35.194.84.11:5000/gps";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("tag", tag);
        jsonBody.put("lng",latLng.longitude);
        jsonBody.put("lat",latLng.latitude);
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


        };

        requestQueue.add(stringRequest);
    } catch (JSONException e) {
        e.printStackTrace();
    }


}



private void sendPost(LatLng latLng, String tag)
    {
    try {
        String url = "http://35.194.84.11:5000/gps";
        final double latitude = latLng.latitude;
        final double longitude = latLng.longitude;
        RequestQueue requestQueue = Volley.newRequestQueue(MapActivity.this);
        HashMap<String,String> params = new HashMap<>();
        params.put("tag",tag);
        params.put("lat",Double.toString(latitude));
        params.put("lng",Double.toString(longitude));
        Log.d("Hello","This was hit");
        CustomRequest jsObjRequest = new CustomRequest(Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               Log.d("ooh","What");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.d("error", "Volley");
            }
        });
        requestQueue.add(jsObjRequest);

    } catch (Exception e) {
        Log.d("Error","fail 2");
    }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

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
                       sendPost2(latLng,tagName);
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
