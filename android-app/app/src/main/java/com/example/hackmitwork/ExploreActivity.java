package com.example.hackmitwork;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.net.Uri;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.util.Log;

public class ExploreActivity extends AppCompatActivity {

    String LOG_TAG = "Main-tag";
    String tag = "Josh Debug Msg";
    static final int REQUEST_IMAGE_CAPTURE= 1;

    // views
    private ImageView capturedImgView = null;
    private TextView annotatedTextView = null;
    private ImageView annotatedImageView = null;
    private LinearLayout exploreView = null;
    private LinearLayout annotateView = null;
    private Button startAnnotateButton = null;
    private Button exitAnnotateButton = null;
    private DrawingView drawingView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);            //Connect to XML file
        Log.e(tag, "Content view has been set!");
        dispatchTakePictureIntent();


//        startAnnotateButton = (Button) findViewById(R.id.startAnnotationButton);
//        exitAnnotateButton = (Button) findViewById(R.id.exitAnnotationButton);
//
//        startAnnotateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(LOG_TAG, "button (startAnnotation) clicked");
//                byte[] byteImage = videoStreamingThread.getCurrentFrame();
//                currentCapturedBm = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
//                exploreView.setVisibility(view.GONE);
//                annotateView.setVisibility(view.VISIBLE);
//                capturedImgView.setImageBitmap(currentCapturedBm);
//                pauseStream();
//            }
//        });
//
//
//        exitAnnotateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(LOG_TAG, "button (exitAnnotation) clicked");
//                annotateView.setVisibility(view.GONE);
//                exploreView.setVisibility(view.VISIBLE);
//                //drawingView.saveDrawing(currentCapturedBm);
//                drawingView.clearDrawing();
//                //resumeStream();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            ImageView image = (ImageView) findViewById(R.id.imageView);

            currentPhotoPath = currentPhotoPath.replace("/storage/emulated/0", "");

            String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + currentPhotoPath;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);


//            ViewGroup layout = (ViewGroup) findViewById(R.id.drawingLayout);
            drawingView = (DrawingView) findViewById(R.id.drawing_area);
//            drawingView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            layout.addView(drawingView);
        }
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.hackmitwork.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_Hack";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }





}


