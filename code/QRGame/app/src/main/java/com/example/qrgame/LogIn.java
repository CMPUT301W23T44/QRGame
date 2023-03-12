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



public class LogIn extends AppCompatActivity {
    Button login_button;
    Button signup_button;
    TextView title_txt;
    TextView noAccount_txt;
    EditText username;
    EditText phone_number;
    ArrayList<User> dataList;
    String UserCollection;
    String LoginCollection;

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
        UserCollection="UserCollection";
        LoginCollection="LoginUser";
        Map<String, Object> currUser = new HashMap<>();

        FirebaseApp.initializeApp(this);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        Intent main_page = new Intent(this, MainPageActivity.class);
        String lo= getUdid();
        CollectionReference logUser = fireStore.collection(LoginCollection);

        /**
         * chech the current user ,if exist, auto login
         *
         */
        CollectionReference docRef = fireStore.collection(LoginCollection);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.isEmpty()) {

                        Log.d("RRG", "check" );

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
            @Override
            public void onClick(View view) {
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                String userName = username.getText().toString();
                String phoneNumber = phone_number.getText().toString();
                String androidId = getUdid();
                fireStore.collection(UserCollection)
                        .whereEqualTo("UserNameKey", username.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String user= (String) document.getData().get("UserNameKey");
                                        String phone= (String) document.getData().get("PhoneKey");
                                        if(!Objects.equals(phone, phone_number.getText().toString())){
                                            Toast warning = Toast.makeText(LogIn.this, "Phone incorrect", Toast.LENGTH_SHORT);
                                            warning.show();
                                        }else if(!Objects.equals(user, username.getText().toString())) {
                                            Toast warningToast = Toast.makeText(LogIn.this, "Username doesn't find", Toast.LENGTH_SHORT);
                                            warningToast.show();
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
            @Override

            public void onClick(View view) {
                startActivity(sign_page);
                finish();


            }
        });
    }

    /**
     * return the AndroidID
     * @return
     */

    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }
    /**
     * return the deviceId
     * @return
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }
}