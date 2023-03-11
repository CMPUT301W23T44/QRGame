//package com.example.qrgame;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.location.LocationRequest;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import android.Manifest;
//import android.content.Context;
//import android.location.Location;
//import android.widget.Toast;
//
//
//public class Map extends AppCompatActivity {
//    LatLng sydney = new LatLng(-34, 151);
//    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
//    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
//    LatLng Brisbane = new LatLng(-27.470125, 153.021072);
//    LatLng Near1 = new LatLng(54, -114);
//    LatLng Near2 = new LatLng(55, -115);
//    LatLng Near3 = new LatLng(56, -114);
//    LatLng Near4 = new LatLng(57, -115);
//    GoogleMap mGoogleMap;
//    private static final int REQUEST_CODE = 101;
//
//    private ArrayList<LatLng> locationArrayList;
//    private ArrayList<Float> distanceList;
//    private ArrayList<Float> distanceList10;
//
//
//    private static final int REQUEST_LOCATION = 1;
//    LocationManager locationManager;
//    String latitude, longitude;
//
//    protected void showLocation() {
//
//        locationArrayList = new ArrayList<>();
//
//        locationArrayList.add(sydney);
//        locationArrayList.add(TamWorth);
//        locationArrayList.add(NewCastle);
//        locationArrayList.add(Brisbane);
//        locationArrayList.add(Near1);
//        locationArrayList.add(Near2);
//        locationArrayList.add(Near3);
//        locationArrayList.add(Near4);
//        ActivityCompat.requestPermissions(MainPageActivity,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }
//
//    }
//
//    private void OnGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private String getLocation() {
//        LatLng Current = null;
//        if (ActivityCompat.checkSelfPermission(
//                Map.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                Map.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        } else {
//            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (locationGPS != null) {
//                double lat = locationGPS.getLatitude();
//                double longi = locationGPS.getLongitude();
//                latitude = String.valueOf(lat);
//                longitude = String.valueOf(longi);
////                showLocation.setText("Your Location: "
////                        +  latitude +
////                        longitude);
//                Current = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                locationArrayList.add(Current);
//
//            } else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return String.valueOf(Current);
//    }
//
//
//
//    public void onMap(@NonNull GoogleMap googleMap) {
//        //mGoogleMap = googleMap;
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        distanceList = new ArrayList<>();
//        distanceList10 = new ArrayList<>();
//        for (int i = 0; i < locationArrayList.size() - 1; i++) {
//            LatLng loc1 = locationArrayList.get(i);
//            LatLng loc2 = locationArrayList.get(locationArrayList.size());
//            float[] distanceResult = new float[1];
//            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
//            float distanceInMeters = distanceResult[0];
//            distanceList.add(distanceInMeters);
//            Collections.sort(distanceList);
//            distanceList10 = (ArrayList<Float>) distanceList.subList(0,9);
//        }
//        for (int i = 0; i < locationArrayList.size() - 1; i++) {
//            LatLng loc1 = locationArrayList.get(i);
//            LatLng loc2 = locationArrayList.get(locationArrayList.size());
//            float[] distanceResult = new float[1];
//            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
//            float distanceInMeters = distanceResult[0];
//            if (distanceList10.contains(distanceInMeters)  ){
//                googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
//                // below line is use to move our camera to the specific location.
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
//
//    }
//
//        }
//
//
////        for (int i = 0; i < locationArrayList.size(); i++) {
////            // below line is use to add marker to each location of our array list.
////            if (i < locationArrayList.size()-1){
////                if(){
////
////                    googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));}}
////            else{
////                googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Current"));
////            }
////            // below line is use to zoom our camera on map.
////            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
////            // below line is use to move our camera to the specific location.
////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
////        }
//    }
//
//}
//
