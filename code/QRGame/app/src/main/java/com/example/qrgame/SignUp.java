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
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
public class SignUp extends AppCompatActivity {
    private Button Finish;
    ArrayList<User> dataList;
    public boolean isHave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button Finish = findViewById(R.id.Finish_button);
        EditText UserName = findViewById(R.id.Username);
        EditText PhoneNumber = findViewById(R.id.PhoneNumber);

        //firebase initialize
        FirebaseApp.initializeApp(this);
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

        //store the data
        Map<String, Object> curUser = new HashMap<>();
        dataList = new ArrayList<>();
        Intent sign_page = new Intent(this, MainPageActivity.class);
        //click Finish bottom to save data to firebase
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = UserName.getText().toString();
                String phoneNumber = PhoneNumber.getText().toString();
                String androidId = getUdid();
                User checkUser= new User(userName, phoneNumber, androidId);
                findUser(checkUser);
                if (userName.isEmpty()) {
                    Toast warningToast = Toast.makeText(SignUp.this, "Username is empty", Toast.LENGTH_LONG);
                    warningToast.show();
                } else if (phoneNumber.isEmpty()) {
                    Toast warningToast = Toast.makeText(SignUp.this, "PhoneNumber is empty", Toast.LENGTH_LONG);
                    warningToast.show();
                } else if (isHave==true) {
                    Toast warningToast = Toast.makeText(SignUp.this, "Username was already register", Toast.LENGTH_LONG);
                    warningToast.show();
                }else {
                    dataList.add((new User(userName, phoneNumber, androidId)));
                    CollectionReference user = fireStore.collection("UserCollection");
                    curUser.put("UserNameKey", dataList.get(0).getUsername());
                    curUser.put("PhoneKey", dataList.get(0).getPhonenumber());
                    curUser.put("AndroidKey", dataList.get(0).getAndroidId());

                    user.document(dataList.get(0).getUsername()).set(curUser);
                    /**
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

                     **/
                    startActivity(sign_page);

                }


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
    public void checkExist(){
        isHave=true;
    }

    /**
     * This function will check the username is exist or not in database
     * if exist, bollean isHave will change to true,otherwise, will change to false
     *
     * @param user
     */
    public  void findUser(User user) {
        //final boolean[] result = new boolean[1];
        final boolean[] isExist = {false};
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("UserCollection").document(user.getUsername());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        isExist[0] = true;
                        Log.d("RRG", "a true");

                    } else {
                        isExist[0] = false;

                        Log.d("RRG", "EXIST false");
                    }
                } else {
                    isExist[0] = false;
                    Log.d("RRG", "Get failure");
                }

            }
        });


        if (isExist[0] == true) {
            checkExist();
        } else {

            isHave=false;
        }

    }


}