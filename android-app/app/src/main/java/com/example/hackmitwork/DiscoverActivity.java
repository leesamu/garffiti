package com.example.hackmitwork;

import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.android.gms.maps.model.LatLng;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity {

    private CameraPreview preview = null;
    private Camera mCamera = null;
    public byte[] reusedBuffer = null;
    private ImageView annotatedImageView = null;
    private String latLng = null;

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latLng = extras.getString("latLng");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private PreviewCallback previewCallback = new PreviewCallback() {
        // called whenever a new frame is captured
        public void onPreviewFrame(byte[] frame, Camera mCamera) {
            Camera.Parameters parameters = mCamera.getParameters();
            //TODO: Push to server
            //if (videoStreamingThread != null){
            //    videoStreamingThread.push(frame, parameters);
            //}
            String encodedImage = Base64.encodeToString(frame, Base64.DEFAULT);
            sendPost2(encodedImage);
            mCamera.addCallbackBuffer(frame);
        }
    };

    private void sendPost2(String encoded_image){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://35.194.84.11:5000/explore";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("image", encoded_image);
            jsonBody.put("latlng", latLng);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    annotatedImageView.setImageBitmap(decodedByte);
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
