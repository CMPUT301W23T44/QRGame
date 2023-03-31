
package com.example.qrgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
    private Context mContext;
    FirebaseFirestore firebaseDatabase;
    private String currUser;
    private String value;
    private GeoPoint loc;
    private int temp;
//    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        temp = 0;
        mContext = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        firebaseDatabase = FirebaseFirestore.getInstance();
        CollectionReference test_value = firebaseDatabase.collection("qrCodes");
        //currUser = (String) getIntent().getStringExtra("Username");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.map, null);
        EditText la = view.findViewById(R.id.latitude);

        return builder
                .setView(view)
                .setTitle("Search QRCode by name")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Search", (dialog, which) -> {
                    String lat = la.getText().toString();
                    //String lot = lo.getText().toString();
                    //GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                    //Toast.makeText(Map.this.getContext(), lat, Toast.LENGTH_SHORT).show();

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
                                            //value += document.get("latLng");
                                            //Toast.makeText(mContext, value , Toast.LENGTH_SHORT).show();
                                            //value += " location: ";
//                                        GeoPoint loc1 = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lot));
                                            if (value.equals(lat)) {
                                                //Intent qrInfoIntent = new Intent(mContext, ExistingQRInfoActivity.class);
//                                                qrInfoIntent.putExtra("qrCode", qrCode);
//                                                qrInfoIntent.putExtra("Username", currUser);
//                                                qrInfoIntent.putExtra("scanned", alreadyScanned);
                                                // Display the QR code information
                                                //Intent qrInfoIntent = new Intent(mContext, ExistingQRInfoActivity.class);
                                                //requireActivity().startActivity(qrInfoIntent);
                                                Toast.makeText(mContext, lat + " is found , its score is" + document.getDouble("score"), Toast.LENGTH_SHORT).show();
                                                temp = 1;

//                                                if (Map.this.getContext() != null) {
//                                                    Toast.makeText(Map.this.getContext(), lat + "finded", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Log.e("Map", "Context is null");
//                                                }

                                            }

                                            //value += document.getString("location_test_string");Hallowed Bleismita
                                            //Toast.makeText(Map.this.getContext(), value, Toast.LENGTH_SHORT).show();Industrious Tryedeght
                                        }
                                        if (temp == 0){
                                            Toast.makeText(mContext, lat + " is not found ", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(mContext, value + "finded", Toast.LENGTH_SHORT).show();

                })
                .create();
    }
}
