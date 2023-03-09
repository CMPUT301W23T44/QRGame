package com.example.qrgame;
//made by Abdirahman
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.nfc.Tag;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//This is the activity about sign up page, when user type Username and PhoneNumber, it will
//turn the page to mainPage
public class LogIn extends AppCompatActivity {
    Button login_button;
    Button signup_button;
    TextView title_txt;
    TextView noAccount_txt;
    EditText user_name;
    EditText phone_number;
    ArrayList<User> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        title_txt = findViewById(R.id.login_title);
        user_name = findViewById(R.id.username);
        phone_number = findViewById(R.id.number);
        login_button = findViewById(R.id.login_button);
        noAccount_txt = findViewById(R.id.noAccount);
        signup_button = findViewById(R.id.signup_button);


        //firebase initialize
        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollectionRef = db.collection("UserCollection");



        //store the data
        Map<String, Object> curUser = new HashMap<>();
        dataList = new ArrayList<>();
        Intent signUp = new Intent(this, SignUp.class);
        //click Finish bottom to save data to firebase
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = user_name.getText().toString();
                String phoneNumber = phone_number.getText().toString();
                String androidId = getUdid();
                User user = new User(userName,phoneNumber,androidId);

               // Query query = userCollectionRef.whereEqualTo("AndroidKey", androidId);


                if (userName.isEmpty()) {
                    Toast warningToast = Toast.makeText(LogIn.this, "Username is empty", Toast.LENGTH_LONG);
                    warningToast.show();
                } else if (phoneNumber.isEmpty()) {
                    Toast warningToast = Toast.makeText(LogIn.this, "PhoneNumber is empty", Toast.LENGTH_LONG);
                    warningToast.show();
                } else {

                    DocumentReference docRef = db.collection("UserCollection").document(user.getAndroidId());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document= task.getResult();
                                if (document.exists()) {
                                    Toast warningToast = Toast.makeText(LogIn.this,
                                            "Successful Log In!", Toast.LENGTH_LONG);
                                    warningToast.show();
                                } else {
                                    Toast warningToast = Toast.makeText(LogIn.this,
                                            "Unsuccessful Log In!", Toast.LENGTH_LONG);
                                    warningToast.show();
                                }

                            }
                        }

                    });




                }


            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signUp);
                finish();
            }
        });




    }

    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }

    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }
}
