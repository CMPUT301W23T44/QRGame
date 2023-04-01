package com.example.qrgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SearchQR extends AppCompatActivity {

    ArrayList<QRCode> qrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchqr);

        TextView searchqroutputname = findViewById(R.id.searchqr_qroutput);
        TextView searchqrtotalamount = findViewById(R.id.searchqr_amount);
        EditText searchqrinputname = findViewById(R.id.searchqr_searchqr);
        Button search = findViewById(R.id.searchqr_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                String searchqrcodename = searchqrinputname.getText().toString().toLowerCase().replaceAll("\\s+", "");

                if (searchqrcodename.isEmpty()) {
                    Toast.makeText(SearchQR.this, "Invalid input! Please enter a QR Code name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                CollectionReference collection_name = fireStore.collection("qrCodes");
                fireStore.collection("qrCodes")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean qrCodeFound = false;
                                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                        String databasecodename = document.getString("name").toLowerCase().replaceAll("\\s+", "");
                                        if (searchqrcodename.equals(databasecodename)) {
                                            qrCodeFound = true;
                                            searchqroutputname.setText(document.getString("name"));

                                            Map map = document.getData();
                                            ArrayList<String> users = (ArrayList<String>) map.get("users");
                                            searchqrtotalamount.setText("Total Amount Scanned:  " + users.size());
                                            break;
                                        }
                                    }

                                    if (!qrCodeFound) {
                                        Toast.makeText(SearchQR.this, "Invalid QR Code name.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        Button Return = findViewById(R.id.searchqr_return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchQR.super.onBackPressed();
            }
        });

    }
}
