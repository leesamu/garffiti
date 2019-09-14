package com.example.hackmitwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.*;
import android.view.*;
import android.provider.Settings;
import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Dialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
//import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;


import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.MultiplePermissionsReport;

import java.security.Permissions;

public class MainActivity extends AppCompatActivity {

    private Button btn_grant;
    private static final String TAG = "Main Activity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && isServicesValid()
                && ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {
            startActivity(new Intent(MainActivity.this,MapActivity.class));
            finish();
        }


        btn_grant = findViewById(R.id.btn_grant);


        btn_grant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Dexter.withActivity(MainActivity.this) .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        startActivity(new Intent(MainActivity.this,MapActivity.class));
                        finish();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

            }
        });
    }
    public boolean isServicesValid(){
        Log.d(TAG,"checking google service version");
        int avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(avaliable == ConnectionResult.SUCCESS){
            //everything is ok
            Log.d(TAG,"Google play services are working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)){
            Log.d(TAG, "An error occured that can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,avaliable,ERROR_DIALOG_REQUEST);
            dialog.show();

        }
        else {
            Toast.makeText(this,"You can't make requests, ", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
