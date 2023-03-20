package com.example.qrgame;
// Made by Abdirahman
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;



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



        /**
         * Create a a reference to LogInUser collection and gets the data
         */
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            /**
             * Checks if there is a user has logged in without logging out. If so go directly to main page
             * @param task
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document=task.getResult();
                    if (document.exists()) {
                        startActivity(main_page);
                        Log.d("RRG", "check" + isHave);

                    } else {


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

                if (userName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please enter username and phone number" , Toast.LENGTH_SHORT).show();
                    return;
                }
                User checkUser= new User(userName, phoneNumber, androidId);
                DocumentReference docRef = fireStore.collection("UserCollection").document(checkUser.getUsername());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    /**
                     * check user exist or not
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document=task.getResult();
                            if (document.exists()){
                                Log.d("RRG", document.getString("PhoneKey"));
                                Log.d("RRG", phoneNumber);
                                if (Objects.equals(document.getString("PhoneKey"), phoneNumber)) {
                                    addCurrUser(userName, phoneNumber, androidId);
                                    goToMainPage(view);
                                } else {
                                    Toast.makeText(getBaseContext(), "Wrong phone number" , Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(getBaseContext(), "Wrong username. Account not found" , Toast.LENGTH_SHORT).show();
                            }

                        } else {
                                    Log.d("RRG", "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });



        signup_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Starts SignUp activity when clicked
             * @param view
             */
            @Override
            public void onClick(View view) {
               gotToSignUp(view);
            }
        });




    }


    /**
     * Adds the current user to the LoginUser collection
     * @param userName
     * @param phoneNumber
     * @param androidId
     */
    public void addCurrUser(String userName, String phoneNumber, String androidId){
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        Map<String, Object> currUser = new HashMap<>();
        CollectionReference logUser = fireStore.collection("LoginUser");
        dataList = new ArrayList<>();
        dataList.add(new User(userName,phoneNumber,androidId));

        currUser.put("UserNameKey", dataList.get(0).getUsername());
        currUser.put("PhoneKey", dataList.get(0).getPhonenumber());
        currUser.put("AndroidKey", dataList.get(0).getAndroidId());
        logUser.document(dataList.get(0).getAndroidId()).set(currUser);
    }

    /**
     * Starts MainPageActivity
     * @param view
     */
    public void goToMainPage(View view) {
        Intent main_page = new Intent(this, MainPageActivity.class);
        startActivity(main_page);
    }

    /**
     * Starts SignUp activity
     * @param view
     */
    public boolean gotToSignUp(View view) {
        Intent sign_page = new Intent(this, SignUp.class);
        startActivity(sign_page);
        finish();
        return true;
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

