package com.example.hackmitwork;

import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

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
            if (isRunning) {
                Camera.Parameters parameters = mCamera.getParameters();
                //TODO: Push to server
                //if (videoStreamingThread != null){
                //    videoStreamingThread.push(frame, parameters);
                //}
            }
            mCamera.addCallbackBuffer(frame);
        }
    };



}
