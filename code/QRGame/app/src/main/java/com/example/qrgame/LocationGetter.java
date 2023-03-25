package com.example.qrgame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import org.checkerframework.checker.units.qual.Current;

public class LocationGetter extends MainPageActivity{


    private LatLng getLocation() {
        LatLng Current = null;
//        if (ActivityCompat.checkSelfPermission(
//                MainPageActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                MainPageActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
            @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Current = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                //locationArrayList.add(Current);

            }else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }

        return Current;
    }
}
