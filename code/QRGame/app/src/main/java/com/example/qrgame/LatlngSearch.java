
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

public class LatlngSearch extends DialogFragment {


    private Context mContext;
    FirebaseFirestore firebaseDatabase;
    private String value;
    private int count;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mContext = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        firebaseDatabase = FirebaseFirestore.getInstance();
        CollectionReference test_value = firebaseDatabase.collection("qrCodes");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.latlngsearch, null);
        EditText la = view.findViewById(R.id.latitude);
        EditText lo = view.findViewById(R.id.longitude);
        return builder
                .setView(view)
                .setTitle("Search QRCode by Latlng")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Search", (dialog, which) -> {
                    Double lat1 = Double.valueOf(la.getText().toString());
                    Double lot1 = Double.valueOf(lo.getText().toString());
                    count = 0;
                    test_value
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
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
                                            if((Double.valueOf(lata) - lat1) < 1 ){
                                                if((Double.valueOf(lot) - lot1) < 1){
                                                    count += 1;
                                                }

                                            }
                                            value = document.getString("name");
                                            value += " location: ";
                                        }
                                        //value += document.getString("location_test_string");
                                        Toast.makeText(mContext, "There are "+String.valueOf(count)+" Qrcodes near this location", Toast.LENGTH_SHORT).show();
                                    }
                                }

//                            });

                            });

                })
                .create();
    }
}
