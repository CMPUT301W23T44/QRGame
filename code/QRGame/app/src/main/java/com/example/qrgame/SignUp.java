package com.example.qrgame;
//made by ZhengPang
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.Nullable;

//This is the activity about sign up page, when user type Username and PhoneNumber, it will
//turn the page to mainPage
public class SignUp extends AppCompatActivity {
    private Button Finish;
    ArrayList<User> dataList;
    String UserCollection;
    String LoginCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button Finish = findViewById(R.id.Finish_button);
        EditText UserName = findViewById(R.id.Username);
        EditText PhoneNumber = findViewById(R.id.PhoneNumber);
        UserCollection="UserCollection";
        LoginCollection="LoginUser";
        //firebase initialize

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        //store the data
        Map<String, Object> curUser = new HashMap<>();

        dataList = new ArrayList<>();
        Intent sign_page = new Intent(this, MainPageActivity.class);
        //click Finish bottom to save data to firebase

        Finish.setOnClickListener(new View.OnClickListener() {
            /**
             * Check the username and phonenumber format
             * @param view
             */
            @Override
            public void onClick(View view) {
                String userName = UserName.getText().toString();
                String phoneNumber = PhoneNumber.getText().toString();
                String androidId = getUdid();
                User checkUser= new User(userName, phoneNumber, androidId);
                if (userName.isEmpty()) {
                    Toast warningToast = Toast.makeText(SignUp.this, "Username is empty", Toast.LENGTH_SHORT);
                    warningToast.show();
                } else if (phoneNumber.isEmpty()) {
                    Toast warningToast = Toast.makeText(SignUp.this, "PhoneNumber is empty", Toast.LENGTH_SHORT);
                    warningToast.show();
                } else if (userName.length()>8) {
                    Toast warningToast = Toast.makeText(SignUp.this, "UserName too long", Toast.LENGTH_SHORT);
                    warningToast.show();


                }else if (PhoneNumber.length()!=10) {
                    Toast warningToast = Toast.makeText(SignUp.this, "Phonenumber ivalid", Toast.LENGTH_SHORT);
                    warningToast.show();
                }else {
                    DocumentReference docRef = fireStore.collection(UserCollection).document(checkUser.getUsername());
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
                                    Toast.makeText(getBaseContext(), "Username exist " , Toast.LENGTH_SHORT).show();
                                }else{
                                    dataList.add((new User(userName, phoneNumber, androidId)));
                                    CollectionReference user = fireStore.collection( UserCollection);
                                    CollectionReference logUser = fireStore.collection(LoginCollection);
                                    curUser.put("UserNameKey", dataList.get(0).getUsername());
                                    curUser.put("PhoneKey", dataList.get(0).getPhonenumber());
                                    curUser.put("AndroidKey", dataList.get(0).getAndroidId());

                                    user.document(dataList.get(0).getUsername()).set(curUser);
                                    logUser.document(dataList.get(0).getAndroidId()).set(curUser);
                                    startActivity(sign_page);

                                }

                            }else {
                                Log.d("RRG", "get failed with ", task.getException());
                            }
                        }
                    });

                }


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