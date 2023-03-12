package com.example.qrgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * This class allows a user to log in to the application using a username and a phone number
 */
public class LogIn extends AppCompatActivity {
    Button login_button;
    Button signup_button;
    TextView title_txt;
    TextView noAccount_txt;
    EditText username;
    EditText phone_number;
    public boolean isHave;
    ArrayList<User> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        title_txt = findViewById(R.id.login_title);
        username = findViewById(R.id.username);
        phone_number = findViewById(R.id.number);
        login_button = findViewById(R.id.login_button);
        noAccount_txt = findViewById(R.id.noAccount);
        signup_button = findViewById(R.id.signup_button);


        FirebaseApp.initializeApp(this);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        Intent main_page = new Intent(this, MainPageActivity.class);
        Map<String, Object> currUser = new HashMap<>();
        CollectionReference logUser = fireStore.collection("LoginUser");


        /**
         * Create a a reference to LogInUser collection and gets the data
         */
        CollectionReference docRef = fireStore.collection("LoginUser");
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            /**
             * Checks if there is a user has logged in without logging out. If so go directly to main page
             * @param task
             */
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.isEmpty()) {

                        Log.d("RRG", "check" + isHave);

                    } else {
                        startActivity(main_page);

                        Log.d("RRG", "check");
                    }
                }else{
                    Log.d("RRG", "check");
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Check if username and phone number are in the database if so, log in
             * @param view
             */
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                String userName = username.getText().toString();
                String phoneNumber = phone_number.getText().toString();
                String androidId = getUdid();
                fireStore.collection("UserCollection")
                        .whereEqualTo("UserNameKey", username.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String phone= (String) document.getData().get("PhoneKey");
                                        if(!Objects.equals(phone, phone_number.getText().toString())){
                                            Toast warning = Toast.makeText(LogIn.this, "Wrong phone number", Toast.LENGTH_SHORT);
                                            warning.show();
                                        }else{
                                            dataList = new ArrayList<>();
                                            dataList.add(new User(userName,phoneNumber,androidId));

                                            currUser.put("UserNameKey", dataList.get(0).getUsername());
                                            currUser.put("PhoneKey", dataList.get(0).getPhonenumber());
                                            currUser.put("AndroidKey", dataList.get(0).getAndroidId());
                                            logUser.document(dataList.get(0).getAndroidId()).set(currUser);
                                            startActivity(main_page);

                                        }
                                        Log.d("RRG", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Toast warningToast = Toast.makeText(LogIn.this, "Username doesn't find", Toast.LENGTH_SHORT);
                                    warningToast.show();
                                    Log.d("RRG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });





        Intent sign_page = new Intent(this, SignUp.class);
        signup_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Starts SignUp activity when clicked
             * @param view
             */
            @Override
            public void onClick(View view) {
                startActivity(sign_page);
                finish();

            }
        });




    }


    /**
     * Gets Android Id
     * @return
     * Android Id as a String
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    /**
     * Gets device Id
     * @return
     * Device Id as a String
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }
}
