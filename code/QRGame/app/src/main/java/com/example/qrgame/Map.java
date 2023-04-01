
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
import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Map extends DialogFragment {
    private Context mContext;
    FirebaseFirestore firebaseDatabase;
    private String value;
    private int ctemp;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ctemp = 0;
        mContext = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        firebaseDatabase = FirebaseFirestore.getInstance();
        CollectionReference test_value = firebaseDatabase.collection("qrCodes");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.map, null);
        EditText la = view.findViewById(R.id.latitude);
        return builder
                .setView(view)
                .setTitle("Search QRCode by name")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Search", (dialog, which) -> {//Having the search button
                    String name = la.getText().toString();
                    test_value
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                            value = document.getString("name");
                                            if (value.equals(name)) {
                                                //get the QRCode location and convert to Latlng
                                                String test = "test";
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
                                                //make a toast of the search result
                                                Toast.makeText(mContext, name + " is found , it is located at " + qrcode.toString(), Toast.LENGTH_SHORT).show();
                                                ctemp = 1;
                                            }
                                        }
                                        if (ctemp == 0){
                                            //make a toast of the search if it is not found
                                            Toast.makeText(mContext, name + " is not found ", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        //make a toast if database cannot load
                                        Toast.makeText(mContext, "Database load error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                })
                .create();
    }
}
