package com.example.qrgame;

import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class SearchUser extends AppCompatActivity {
    ArrayList<QRCode> qrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchuser);

        TextView usern = findViewById(R.id.searchuser_username);
        TextView totalsearchuserscore = findViewById(R.id.searchuser_totalscore);
        TextView totalsearchuseramount = findViewById(R.id.searchuser_totalamount);
        EditText searchuser = findViewById(R.id.searchuser);
        Button search = findViewById(R.id.searchuser_search);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                String username = searchuser.getText().toString();

                // Check for empty or null input
                if (username.isEmpty()) {
                    Toast.makeText(SearchUser.this, "Invalid input! Please enter a Username.", Toast.LENGTH_SHORT).show();
                    return;
                }

                User checkUser = new User(username, null, null, qrcode);
                DocumentReference docRef = fireStore.collection("UserCollection").document(checkUser.getUsername());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                usern.setText("Username:  " + username);

                                Map map = document.getData();
                                ArrayList<QRCode> qrcode = (ArrayList<QRCode>) map.get("QRCode");
                                int total = 0;
                                for (int i = 0; i < qrcode.size(); i++) {
                                    Map map1 = (Map) qrcode.get(i);
                                    int score = ((Long) map1.get("score")).intValue();
                                    total += score;
                                }
                                totalsearchuserscore.setText("Total Score:  " + total);

                                totalsearchuseramount.setText("Total Amount:  " + qrcode.size());

                            } else {
                                Toast.makeText(SearchUser.this, "Invalid user! User not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        Button Return = findViewById(R.id.searchuser_return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUser.super.onBackPressed();
            }
        });

    }
}
