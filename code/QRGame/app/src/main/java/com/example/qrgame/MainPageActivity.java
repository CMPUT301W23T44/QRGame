package com.example.qrgame;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;


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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.firestore.QuerySnapshot;


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
    FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_CODE = 101;
    private ArrayList<LatLng> locationArrayList;
    private ArrayList<String> locationnameArrayList;

    private ArrayList<Float> distanceList;
    private ArrayList<Float> distanceList10;

    private FloatingActionButton addQr_button;
    private Button inventory_button;
    private Context mContext;
    private Button search_button;

    private Button social_button;
    private Button logout_button;
    FirebaseFirestore firebaseDatabase;

    private final int QR_SCANNER_REQUEST = 0;
    private final int INVENTORY_REQUEST = 1;
    private final int SOCIAL_REQUEST = 2;

    // Name and face of MissingNo
    private final String MISSINGNONAME = "MissingNo";
    private final String MISSINGNOFACE = "   |-------|\n   |       |\n   |       |\n   |       |\n" +
            "|          |\n|          |\n|          |\n|----------|";

    private String currUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        locationArrayList = new ArrayList<>();
        locationnameArrayList = new ArrayList<>();




//        locationArrayList.add(sydney);
//        locationArrayList.add(TamWorth);
//        locationArrayList.add(NewCastle);
//        locationArrayList.add(Brisbane);
//        locationArrayList.add(Near1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        logout_button = findViewById(R.id.logout_button);
        currUser = (String) getIntent().getStringExtra("Username");

        search_button = findViewById(R.id.search);
        search_button.setOnClickListener(v -> {
            new Map().show(getSupportFragmentManager(), "Search QRCode");
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);
        Intent login_page = new Intent(this, LogIn.class);
        String deviceId = getUdid();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

        logout_button.setOnClickListener(view -> {
            FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

            fireStore.collection("LoginUser").document(deviceId).delete();
            startActivity(login_page);
        });


        addQr_button = findViewById(R.id.add_qr);
        Intent qr_scanner = new Intent(this, QRScannerActivity.class);
        // Player chose to add a QR code
        addQr_button.setOnClickListener(view -> startActivityForResult(qr_scanner, QR_SCANNER_REQUEST));

        inventory_button = findViewById(R.id.inventory_button);
        Intent inventory = new Intent(MainPageActivity.this, Inventory_activity.class);
        inventory_button.setOnClickListener(view -> startActivity(inventory));

        social_button = findViewById(R.id.social_button);

        social_button.setOnClickListener(view -> {
            Intent social_page = new Intent(MainPageActivity.this, Social.class);
            startActivity(social_page);
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
                Current = new LatLng(0,0);
            }
        }
        return Current;
    }

    // Handles activities that return with a result
    private String hash;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        // A scanner activity was called and finished here
        if (requestCode == QR_SCANNER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (result != null) {
                    hash = result.getStringExtra("result");
                } else {
                    // If the QR code did not have any data, a generic QR code is displayed
                    MISSINGNO();
                }

                // Get instance of the database
                QRDatabaseController dbAdapter = QRDatabaseController.getInstance();

                /*
                 Find the QR code and perform actions based on if it exists
                 LOOK HERE FOR EXAMPLE OF DATABASE
                */
                dbAdapter.findQR(hash, qrExists -> {
                    Log.d("TestQR", String.valueOf(qrExists));
                    if (qrExists) {
                        // If the QR code exists, the code is retrieved from the database and
                        // updated

                        dbAdapter.getQRCode(hash, qrCode -> {
                            Intent qrInfoIntent = new Intent(MainPageActivity.this, ExistingQRInfoActivity.class);
                            boolean alreadyScanned = true;
                            // If the user has not scanned the QR code yet, they are added to the
                            // user list
                            if (!(qrCode.getUsers()).contains(currUser)) {
                                qrCode.addUsers(currUser);
                                addQR(qrCode);
                                alreadyScanned = false;
                            }

                            qrInfoIntent.putExtra("qrCode", qrCode);
                            qrInfoIntent.putExtra("Username", currUser);
                            qrInfoIntent.putExtra("scanned", alreadyScanned);
                            // Display the QR code information
                            startActivity(qrInfoIntent);
                        });

                    } else {
                        // If the QR code does not exist yet, a new one is created
                        Intent newQRCodeInfoIntent = new Intent(MainPageActivity.this, NewQRInfoActivity.class);
                        newQRCodeInfoIntent.putExtra("Username", currUser);

                        // Get current location for if the user wants to save it with the QR code
                        LatLng currLocation = getLocation();
                        newQRCodeInfoIntent.putExtra("latitude", (Double) currLocation.latitude);
                        newQRCodeInfoIntent.putExtra("longitude", (Double) currLocation.longitude);

                        // New QR code is created and the user is added to the user list
                        QRCode qrCode = new QRCode(hash);
                        qrCode.addUsers(currUser);
                        newQRCodeInfoIntent.putExtra("qrCode", qrCode);

                        // Display the QR code information
                        startActivity(newQRCodeInfoIntent);
                    }
                });
            } else if (resultCode == RESULT_CANCELED) {
                // If the user cancelled the scanner, the default QR code is displayed
                MISSINGNO();
            }
        }
    }

    /**
     * Used for when a QR code does not exist. Generates a QR code worth 0 points
     * called MissingNo
     */
    private void MISSINGNO() {
        // MissingNo has a hash of '0'
        hash = "0";
        QRCode qrCode = new QRCode(0, hash, MISSINGNONAME, MISSINGNOFACE,
                0, 0, new ArrayList<>(), new HashMap<String, String>(), "");

        Intent qrInfoIntent = new Intent(MainPageActivity.this,
                ExistingQRInfoActivity.class);
        qrInfoIntent.putExtra("qrCode", qrCode);
        qrInfoIntent.putExtra("scanned", true);

        startActivity(qrInfoIntent);
    }

    /**
     * return the AndroidID
     *
     * @return
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * return the deviceId
     *
     * @return
     */
    public String getUdid() {
        String androidID = AndroidID();
        return "2" + UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-", "");
    }
    /**
     * add QRcode in UserCollection
     * @param qrCode
     */
    public void addQR(QRCode qrCode){
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("UserNameKey");
                        DocumentReference docRef2 = fireStore.collection("UserCollection").document(Username);
                        docRef2.update("QRCode", FieldValue.arrayUnion(qrCode));

                    }
                }
            }

        });
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        firebaseDatabase = FirebaseFirestore.getInstance();
        CollectionReference test_value = firebaseDatabase.collection("qrCodes");
        mGoogleMap = googleMap;
        locationnameArrayList.add("current");

        //String deviceId = getUdid();
        //mContext = getContext();

//        locationArrayList.add(Near2);
//        locationArrayList.add(Near3);
//        locationArrayList.add(Near4);

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


        test_value
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            List<Float> lists = new ArrayList<Float>();
                            //locationArrayList = new ArrayList<>();
                            List<Float> lists1 = new ArrayList<Float>();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
//                                value = task.getResult().getDocuments().get(0).getGeoPoint("latLng");
//                                value = ((HashMap<String, Object>) document.getData().get("latLng"));

                                String test = "test";//"(String) document.get("latLng");""
                                test += document.get("latLng");
                                String[] temp;
                                String[] temp1;
                                String[] lat;
                                String lot;
                                String delimeter = "=";
                                String delimeter1 = ",";
                                temp = test.split(delimeter);
                                temp1 = temp[1].split(delimeter1);
                                lat = temp1[0].split(",");
                                String lata = lat[0];
                                lot = temp[2].substring(0,temp[2].length()-1);
                                LatLng qrcode = new LatLng(Double.valueOf(lata),Double.valueOf(lot));
                                locationnameArrayList.add(document.getString("name"));
                                //Toast.makeText(MainPageActivity.this, document.getString("name") , Toast.LENGTH_SHORT).show();
                                locationArrayList.add(qrcode);
                                //value += " location: ";
//                                        GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));


                                //value += document.getString("location_test_string");Hallowed Bleismita
                                //Toast.makeText(Map.this.getContext(), value, Toast.LENGTH_SHORT).show();Industrious Tryedeght
                            }
/*                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                OnGPS();
                            } else {
                                getLocation();*/
                            //locationArrayList.add(getLocation());

                            distanceList = new ArrayList<>();
                            distanceList10 = new ArrayList<>();
                            for (int i = 1; i < locationArrayList.size() - 1; i++) {
                                LatLng loc1 = locationArrayList.get(i);
                                LatLng loc2 = locationArrayList.get(0);
                                float[] distanceResult = new float[1];
                                Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
                                float distanceInMeters = distanceResult[0];
                                lists.add(distanceInMeters);

                            }
                            Collections.sort(lists);
//                            if (lists.size() > 9) {
//                                lists1 = lists.subList(0, 10);
//                            } else {
//                                lists1 = lists;
//                            }
                            lists1 = lists;


                            for (int i = 1; i < locationArrayList.size() - 1; i++) {
                                LatLng loc1 = locationArrayList.get(i);
                                LatLng loc2 = locationArrayList.get(0);
                                float[] distanceResult = new float[1];
                                Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
                                float distanceInMeters = distanceResult[0];
                                if (lists1.contains(distanceInMeters)) {
                                    googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(locationnameArrayList.get(i)));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                                    // below line is use to move our camera to the specific location.
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));

                                }
                            }
                            ActivityCompat.requestPermissions(MainPageActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


//                            Intent intent = new Intent(Map.this.getContext(), Activity.class);
//                            intent.putExtra("name",task.getResult().getDocuments().get(0).getString("name"));
//                            startActivity(intent);


                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
//                            });


                });
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }
//
//
//        distanceList = new ArrayList<>();
//        distanceList10 = new ArrayList<>();
//        for (int i = 0; i < locationArrayList.size() - 2; i++) {
//            LatLng loc1 = locationArrayList.get(i);
//            LatLng loc2 = locationArrayList.get(locationArrayList.size() - 1);
//            float[] distanceResult = new float[1];
//            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
//            float distanceInMeters = distanceResult[0];
//            lists.add(distanceInMeters);
//
//        }
//        Collections.sort(lists);
//        if (lists.size() > 9) {
//            lists1 = lists.subList(0, 10);
//        } else {
//            lists1 = lists;
//        }
//
//        for (int i = 0; i < locationArrayList.size() - 2; i++) {
//            LatLng loc1 = locationArrayList.get(i);
//            LatLng loc2 = locationArrayList.get(locationArrayList.size() - 1);
//            float[] distanceResult = new float[1];
//            Location.distanceBetween(loc1.latitude, loc1.longitude, loc2.latitude, loc2.longitude, distanceResult);
//            float distanceInMeters = distanceResult[0];
//            if (lists1.contains(distanceInMeters)) {
//                googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
//                // below line is use to move our camera to the specific location.
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
//
//            }
//        }
    }
}