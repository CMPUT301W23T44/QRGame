package com.example.qrgame;
//made by ZhengPang
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//This is the activity about sign up page, when user type Username and PhoneNumber, it will
//turn the page to mainPage
public class SignUp extends AppCompatActivity {
    private Button Finish;
    ArrayList<User> dataList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button Finish = findViewById(R.id.Finish_button);
        EditText UserName=findViewById(R.id.Username);
        EditText PhoneNumber=findViewById(R.id.PhoneNumber);

        //firebase initialize
        FirebaseApp.initializeApp(this);
        FirebaseFirestore fireStore=FirebaseFirestore.getInstance();

        //store the data
        Map<String,Object> curUser=new HashMap<>();
        dataList=new ArrayList<>();
        Intent sign_page = new Intent(this, MainPageActivity.class);
        //click Finish bottom to save data to firebase
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=UserName.getText().toString();
                String phoneNumber=PhoneNumber.getText().toString();


                if (userName.isEmpty()){
                    Toast warningToast=Toast.makeText(SignUp.this,"Username is empty",Toast.LENGTH_LONG);
                    warningToast.show();
                }else if(phoneNumber.isEmpty()){
                    Toast warningToast=Toast.makeText(SignUp.this,"PhoneNumber is empty",Toast.LENGTH_LONG);
                    warningToast.show();
                }else {
                    dataList.add((new User(userName,phoneNumber)));
                    curUser.put("UserNameKey", dataList.get(0).getUsername());
                    curUser.put("PhoneKey", dataList.get(0).getPhonenumber());

                    fireStore.collection("UserCollection")
                            .add(curUser)

                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RRG", "Document is success");
                                    } else {
                                        Log.e("RRG", "something wrong", task.getException());
                                    }
                                }

                            });


                    startActivity(sign_page);

                }


            }
        });


    }
}