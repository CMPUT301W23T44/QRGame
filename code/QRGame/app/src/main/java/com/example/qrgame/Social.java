package com.example.qrgame;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.FirebaseOptions;

import org.checkerframework.checker.units.qual.A;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Social extends AppCompatActivity {

    private ArrayList<QRCode> QrDataList;

    private ListView QrCodeList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social);


//        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
//        fireStore.collection("UserCollection")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    private ArrayList<String> userlist = new ArrayList<>();
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()){
//                                System.out.println(document.getData());
//                                Map map=document.getData();
//                                int score=( (Long) map.get("score")).intValue();
//                                String hash= (String) map.get("hash");
//                                String name= (String) map.get("name");
//                                String face= (String) map.get("face");
//
//                                QRCode qrCode=new QRCode(score,hash,name,face);
//
//                            }}else{
//                            Log.d("QRread","error on getting doc",task.getException());
//                        }
//                    }
//
//                });

// Here should use the data base to get the name and score into the leaderboard , didnt figure it out, do it in the future


        Button Return = findViewById(R.id.return_button);
        Button Search_QR_Codes = findViewById(R.id.searchqr_button);
        Button Search_Users = findViewById(R.id.searchuser_button);

        //return button: return back to the MainPageActivity
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Social.this, MainPageActivity.class);
                startActivity(intent);
            }
        });


    }
}