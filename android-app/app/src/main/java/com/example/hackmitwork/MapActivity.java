package com.example.hackmitwork;

import java.io.IOException;
import android.location.Location;
import android.nfc.Tag;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.Marker;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;
import org.json.JSONArray;
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
    // Initialize a new StringRequest
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    String url = "http://35.194.84.11:5000/gps";
    JsonObjectRequest stringRequest = new JsonObjectRequest(
            Request.Method.GET,
            url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                     Log.d("Hello",response.toString());
                   double lng, lat;
                   String tag;
                  try {
                      JSONArray the_json_array = response.getJSONArray("markers");
                      System.out.println(the_json_array);
                      for(int i = 0; i < the_json_array.length();++i){
                          JSONArray val = the_json_array.getJSONArray(i);
                         JSONArray doubleVals = val.getJSONArray(0);
                         lat = doubleVals.getDouble(0);
                         lng =  doubleVals.getDouble(1);
                         tag = val.getString(1);
                         LatLng latLng = new LatLng(lat,lng);
                          Marker marker =  nMap.addMarker(new MarkerOptions()
                                  .position(latLng).title(tag));




                      }
                  }
                  catch (JSONException j){
                      j.getStackTrace();
                  }





                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Do something when get error
                    Log.d("LMAO","What");

                }
            }
    );

    // Add StringRequest to the RequestQueue
    requestQueue.add(stringRequest);
    return  null;
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







    @Override
    public void onMapReady(GoogleMap googleMap){
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        nMap = googleMap;
        getDeviceLocation(false);
        nMap.setMyLocationEnabled(true);
        getMarkers();

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
                   //  MarkerOptions marker = new MarkerOptions().position(latLng).title(tag);
                   //  nMap.addMarker(marker);
                   nMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                       @Override
                       public boolean onMarkerClick(Marker marker) {
                           LatLng latLng = marker.getPosition();
                           Intent i = new Intent(MapActivity.this, DiscoverActivity.class);
                           double lat = latLng.latitude;
                           double lng = latLng.longitude;
                           String concat = Double.toString(lat) + "," + Double.toString(lng);
                           i.putExtra("latLng", concat);
                           startActivity(i);

                           //Using position get Value from arraylist
                           return false;
                       }
                   });

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
        finish();
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
