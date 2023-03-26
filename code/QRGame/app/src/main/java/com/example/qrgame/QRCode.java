package com.example.qrgame;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;
import com.google.android.gms.maps.model.LatLng;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a QR code object
 */
@IgnoreExtraProperties
public class QRCode implements Comparable, Serializable {

    @PropertyName("score")
    private int score;
    @PropertyName("hash")
    private String hash;
    @PropertyName("name")
    private String name;
    @PropertyName("face")
    private String face;

    @PropertyName("longitude")
    private double longitude;
    @PropertyName("latitude")
    private double latitude;

    @PropertyName("users")
    private ArrayList<String> users;
    @PropertyName("comments")
    private HashMap<String, String> comments;
    @PropertyName("location_image")
    private String location_image;

    private LatLng location;

    public QRCode() {
    }

    public QRCode(String hash){

        this.hash = hash;
        score = calcScore();
        face = NameFaceScheme.generateFace(hash);
        name = NameFaceScheme.generateName(hash);
        //LocationGetter loc = new LocationGetter();
        //location = getLocation();
        this.latitude = 0;
        this.longitude = 0;
        users = new ArrayList<>();
        comments = new HashMap<>();
        location_image = "";
    }



    public QRCode(int score, String hash, String name, String face, double latitude, double longitude,
                  ArrayList<String> users, HashMap comments, String location_image) {
        this.score = score;
        this.hash = hash;
        this.name = name;
        this.face = face;
        setLatLong(latitude, longitude);
        this.users = users;
        this.comments = comments;
        this.location_image = location_image;
    }

    public void addUsers(String uid) {
        users.add(uid);
    }

    public void addComments(String userName, String comment) {
        comments.put(userName, comment);
    }

    public void setLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLocation_image(String bytes) {
        location_image = bytes;
    }

//    private LatLng getLocation() {
//        LatLng Current = null;
////        if (ActivityCompat.checkSelfPermission(
////                MainPageActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                MainPageActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
////        } else {
//        @SuppressLint("MissingPermission") Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (locationGPS != null) {
//            double lat = locationGPS.getLatitude();
//            double longi = locationGPS.getLongitude();
//            latitude = Double.parseDouble(String.valueOf(lat));
//            longitude = Double.parseDouble(String.valueOf(longi));
//            Current = new LatLng(latitude, longitude);
//            //locationArrayList.add(Current);
//
//        }else {
//            //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
//        }
//
//        return Current;
//    }

    /**
     * Calculates the score of a QR code based off the hash provided
     */
    private int calcScore() {
        int RADIX = 16;
        int ZERO_VALUE = 20;
        String previousChar = String.valueOf(hash.charAt(0));
        String currentChar;
        int currentScoreValue = 0;
        boolean firstInstance = true;
        int score = 0;

        // Finds consecutive instances of a character in the hash and bases the score off the
        // power of these consecutive values
        for (int i = 1; i < hash.length(); i++) {
            currentChar = String.valueOf(hash.charAt(i));
            if (previousChar.equals("0") && firstInstance) {
                currentScoreValue = 1;
                firstInstance = false;
            }
            if (currentChar.equals(previousChar)) {
                if (firstInstance) {
                    currentScoreValue = Integer.parseInt(currentChar, RADIX);
                    firstInstance = false;
                } else {
                    // Zeroes are worth the most points
                    if (currentChar.equals("0")) {
                        currentScoreValue *= ZERO_VALUE;
                    } else {
                        currentScoreValue *= (Integer.parseInt(currentChar, RADIX));
                    }
                }
            } else {
                score += currentScoreValue;
                currentScoreValue = 0;
                firstInstance = true;
            }
            previousChar = currentChar;
        }
        return score;
    }

    @PropertyName("score")
    public int getScore() {
        return score;
    }

    @PropertyName("hash")
    public String getHash() {
        return hash;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("face")
    public String getFace() {
        return face;
    }

    public ArrayList<String> getUsers() {
        return users;
    }
    public LatLng getLatLng() {
        return new com.google.android.gms.maps.model.LatLng(latitude, longitude);
    }

    public HashMap<String, String> getComments() {
        return comments;
    }

    public String getLocation_image() {
        return location_image;
    }

    @Exclude
    public Bitmap getLocationImageBitmap() {
        byte[] bytes = location_image.getBytes();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public int compareTo(Object o) {
        QRCode qrCode = (QRCode) o;
        return ((Integer) this.getScore())
                .compareTo((Integer) qrCode.getScore());
    }
}


