package com.example.hackmitwork;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

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

import android.util.Log;

public class ExploreActivity extends AppCompatActivity {

    String tag = "Josh Debug Msg";
    static final int REQUEST_IMAGE_CAPTURE= 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);            //Connect to XML file
        Log.e(tag, "Content view has been set!");
        dispatchTakePictureIntent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            ImageView image = (ImageView) findViewById(R.id.imageView);

            currentPhotoPath = currentPhotoPath.replace("/storage/emulated/0", "");

            String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + currentPhotoPath;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            image.setImageBitmap(bitmap);
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


