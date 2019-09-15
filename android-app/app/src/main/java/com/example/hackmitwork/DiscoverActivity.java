package com.example.hackmitwork;

import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DiscoverActivity extends AppCompatActivity {

    private CameraPreview preview = null;
    private Camera mCamera = null;
    public byte[] reusedBuffer = null;
    private ImageView annotatedImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        annotatedImageView = (ImageView) findViewById(R.id.annotated_image);
        preview = (CameraPreview) findViewById(R.id.camera_preview);
        mCamera = preview.checkCamera();
        preview.start();
        mCamera.setPreviewCallbackWithBuffer(previewCallback);
        reusedBuffer = new byte[1920 * 1080 * 3 / 2]; // 1.5 bytes per pixel
        mCamera.addCallbackBuffer(reusedBuffer);
        setContentView(R.layout.activity_discover);
    }

    private PreviewCallback previewCallback = new PreviewCallback() {
        // called whenever a new frame is captured
        public void onPreviewFrame(byte[] frame, Camera mCamera) {
            Camera.Parameters parameters = mCamera.getParameters();
            //TODO: Push to server
            //if (videoStreamingThread != null){
            //    videoStreamingThread.push(frame, parameters);
            //}

            mCamera.addCallbackBuffer(frame);
        }
    };

    private void sendPost2(LatLng latLng, String encoded_image){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://35.194.84.11:5000/gps";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("", );
            jsonBody.put("lng",latLng.longitude);
            jsonBody.put("lat",latLng.latitude);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

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


}
