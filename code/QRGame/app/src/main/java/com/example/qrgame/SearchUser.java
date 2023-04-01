package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
        Button ViewDetail = findViewById(R.id.searchuser_viewdetail);
        ViewDetail.setEnabled(false);

        searchuser.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        searchuser.setInputType(InputType.TYPE_CLASS_TEXT);
        searchuser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search.performClick();
                    return true;
                }
                return false;
            }
        });

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
                                ViewDetail.setEnabled(true);

                            } else {
                                Toast.makeText(SearchUser.this, "Invalid user! User not found.", Toast.LENGTH_SHORT).show();
                                ViewDetail.setEnabled(false);
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


        ViewDetail.setOnClickListener(view -> {
            Intent userdetail_page = new Intent(SearchUser.this, SearchUserDetail.class);
            startActivity(userdetail_page);
        });

        ViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SearchUser.this, SearchUserDetail.class);

                String searchusername = searchuser.getText().toString();

                intent.putExtra("username", searchusername);
                startActivityForResult(intent,10);
            }
        });

    }
}
