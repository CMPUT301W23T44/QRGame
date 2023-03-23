
package com.example.qrgame;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;
import static androidx.camera.core.impl.utils.ContextUtil.getBaseContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Map extends DialogFragment {
    FirebaseFirestore firebaseDatabase;
    private String value;
    private Button search;

    private GeoPoint loc;
//    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        firebaseDatabase = FirebaseFirestore.getInstance();
        CollectionReference test_value = firebaseDatabase.collection("qrCodes");



        View view = LayoutInflater.from(getContext()).inflate(R.layout.map, null);
        EditText la = view.findViewById(R.id.latitude);
        EditText lo = view.findViewById(R.id.longitude);
        search = view.findViewById(R.id.search);
        if(search != null) {
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String lat = la.getText().toString();
                    String lot = lo.getText().toString();
                    GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                    Toast.makeText(Map.this.getContext(), loc1.toString(), Toast.LENGTH_SHORT).show();

                    //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    test_value
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                            //value = String.valueOf(task.getResult().getDocuments().get(0).getString("name"));

                                            value = document.getString("name");
                                            value += " location: ";
//                                        GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                                            if (document.getGeoPoint("location_test") == loc1) {
                                                Toast.makeText(Map.this.getContext(), loc1.toString(), Toast.LENGTH_SHORT).show();


                                            }
                                            value += document.getString("location_test_string");
                                            Toast.makeText(Map.this.getContext(), value, Toast.LENGTH_SHORT).show();
                                        }
//                            Intent intent = new Intent(Map.this.getContext(), Activity.class);
//                            intent.putExtra("name",task.getResult().getDocuments().get(0).getString("name"));
//                            startActivity(intent);


                                    } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
//                            });


                            });
                }
            });
        }
//        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        //firebaseDatabase = FirebaseFirestore.getInstance();
//        //CollectionReference test_value = firebaseDatabase.collection("qrCodes");
//        test_value
//
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @SuppressLint("RestrictedApi")
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
//                                //value = String.valueOf(task.getResult().getDocuments().get(0).getString("name"));
//
//                                value = document.getString("name");
//                                value += " location: ";
//                                String lat = la.getText().toString();
//                                String lot = lo.getText().toString();
////                                GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat),Double.parseDouble(lot));
////                                if (document.getGeoPoint("location_test") == loc1){
////                                    Toast.makeText(Map.this.getContext(), loc1.toString(), Toast.LENGTH_SHORT).show();
////
////
////                                }
//                                value += document.getGeoPoint("location_test");
//                                Toast.makeText(Map.this.getContext(), value, Toast.LENGTH_SHORT).show();
//                            }
////                            Intent intent = new Intent(Map.this.getContext(), Activity.class);
////                            intent.putExtra("name",task.getResult().getDocuments().get(0).getString("name"));
////                            startActivity(intent);
//
//
//                        } else {
////                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
////        Intent intent=Intent.parseUri();
////        String name=intent.getStringExtra("name");



        return builder
                .setView(view)
                .setTitle("test" + value)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Search", (dialog, which) -> {
                    String lat = la.getText().toString();
                    String lot = lo.getText().toString();
                    GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                    Toast.makeText(Map.this.getContext(), loc1.toString(), Toast.LENGTH_SHORT).show();

                    //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    test_value
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                            //value = String.valueOf(task.getResult().getDocuments().get(0).getString("name"));

                                            value = document.getString("name");
                                            value += " location: ";
//                                        GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                                            if (document.getGeoPoint("location_test") == loc1) {
                                                Toast.makeText(Map.this.getContext(), loc1.toString(), Toast.LENGTH_SHORT).show();


                                            }
                                            //value += document.getString("location_test_string");
                                            //Toast.makeText(Map.this.getContext(), value, Toast.LENGTH_SHORT).show();
                                        }
//                            Intent intent = new Intent(Map.this.getContext(), Activity.class);
//                            intent.putExtra("name",task.getResult().getDocuments().get(0).getString("name"));
//                            startActivity(intent);


                                    } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
//                            });


                            });

                })
                .create();
//        return null;
    }
}
