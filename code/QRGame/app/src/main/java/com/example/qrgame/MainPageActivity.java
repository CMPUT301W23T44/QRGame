package com.example.qrgame;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;




public class MainPageActivity extends AppCompatActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    String latitude, longitude;
    private static final int REQUEST_LOCATION = 1;

    LatLng sydney = new LatLng(-34, 151);
    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);
    LatLng Near1 = new LatLng(54, -114);
    LatLng Near2 = new LatLng(55, -115);
    LatLng Near3 = new LatLng(56, -114);
    LatLng Near4 = new LatLng(57, -115);
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;
    Marker mCurrLocationMarker;
    private static final int REQUEST_CODE = 101;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<Float> distanceList;
    private ArrayList<Float> distanceList10;

    private FloatingActionButton addQr_button;
    private Button inventory_button;
    private Button search_button;

    private Button social_button;
    private Button logout_button;

    private final int QR_SCANNER_REQUEST = 0;
    private final int INVENTORY_REQUEST = 1;
    private final int SOCIAL_REQUEST = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        locationArrayList = new ArrayList<>();

        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);
        locationArrayList.add(Near1);
        locationArrayList.add(Near2);
        locationArrayList.add(Near3);
        locationArrayList.add(Near4);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        logout_button = findViewById(R.id.logout_button);
//        search_button = findViewById(R.id.search);
//        search_button.setOnClickListener(v -> {
//            new Map().show(getSupportFragmentManager(), "Search QRCode");
//        });
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap);
//        mapFragment.getMapAsync(this);
        Intent login_page = new Intent(this, LogIn.class);
        String deviceId= getUdid();
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

                fireStore.collection("LoginUser").document(deviceId).delete();
                startActivity(login_page);
            }
        });
        addQr_button = findViewById(R.id.add_qr);

        Intent qr_scanner = new Intent(this, QRScannerActivity.class);
        // Player chose to add a QR code
        addQr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(qr_scanner, QR_SCANNER_REQUEST);
            }
        });

        inventory_button = findViewById(R.id.inventory_button);
        Intent inventory =new Intent(MainPageActivity.this,Inventory_activity.class);
        inventory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(MainPageActivity.this,Inventory_activity.class);
                //startActivity(intent);
                startActivity(inventory);
            }
        });

        social_button = findViewById(R.id.social_button);

        social_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent social_page  = new Intent(MainPageActivity.this, Social.class);
                startActivity(social_page);
            }
        });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private LatLng getLocation() {
        LatLng Current = null;
        if (ActivityCompat.checkSelfPermission(
                MainPageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainPageActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Current = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                locationArrayList.add(Current);

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
        return Current;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == QR_SCANNER_REQUEST) {
                try {
                    String result = null;
                    if (data != null) {
                        result = data.getStringExtra("result");
                    } else {
                        result = "00000000";    // TODO - Change with a zero score QR code
                    }
                    // Create a new QR code and add it to the database
                    QRCode qrCode = new QRCode(result);

                    QRDatabaseController qrDB = new QRDatabaseController();
                    qrDB.addQR(qrCode);
                    // Display the QR codes info
                    Intent qrInfoIntent = new Intent(this, QRInfoActivity.class);
                    qrInfoIntent.putExtra("qrCode", qrCode);
                    startActivity(qrInfoIntent);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    /**
     * return the AndroidID
     * @return
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * return the deviceId
     * @return
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        Location location = new Location();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        List<Float> lists = new ArrayList<Float>();
        List<Float> lists1 = new ArrayList<Float>();


        distanceList = new ArrayList<>();
        distanceList10 = new ArrayList<>();
        for (int i = 0; i < locationArrayList.size() - 2; i++) {
            LatLng loc1 = locationArrayList.get(i);
            LatLng loc2 = locationArrayList.get(locationArrayList.size()-1);
            float[] distanceResult = new float[1];
            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
            float distanceInMeters = distanceResult[0];
            lists.add(distanceInMeters);

        }
        Collections.sort(lists);
        if (lists.size() > 9){
            lists1 = lists.subList(0,10);
        }
        else{
            lists1 = lists;
        }

        for (int i = 0; i < locationArrayList.size() - 2; i++) {
            LatLng loc1 = locationArrayList.get(i);
            LatLng loc2 = locationArrayList.get(locationArrayList.size()-1);
            float[] distanceResult = new float[1];
            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
            float distanceInMeters = distanceResult[0];
            if (lists1.contains(distanceInMeters)) {
                googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                // below line is use to move our camera to the specific location.
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));

            }
        }
    }
}