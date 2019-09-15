package com.example.hackmitwork;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class CustomRequest {
    private LatLng latLng;
    private String tag;

    public CustomRequest(LatLng latLng, String tag) {
        this.latLng = latLng;
        this.tag = tag;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getTag() {
        return tag;
    }
}