package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SearchUserDetail extends AppCompatActivity {

    ArrayList<String> qrnamelist = new ArrayList<>();
    private ListView qrcodelist;

    private SearchUserDetailAdapater SRUSERAdapater;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchuserdetail);

        qrcodelist = findViewById(R.id.searchuserdetail_qrcodelist);
        TextView usern = findViewById(R.id.searchuserdetail_username);
        TextView totalscore = findViewById(R.id.searchuserdetail_totalscore);
        TextView totalamount = findViewById(R.id.searchuserdetail_totalamount);
        SRUSERAdapater = new SearchUserDetailAdapater(this, qrnamelist);
        qrcodelist.setAdapter(SRUSERAdapater);


        username = (String) getIntent().getStringExtra("username");

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection_name = fireStore.collection("UserCollection");
        fireStore.collection("UserCollection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                String databaseusername = document.getString("UserNameKey");
                                if (username.equals(databaseusername)) {
                                    usern.setText("Username:  " + username);

                                    Map map = document.getData();
                                    ArrayList<QRCode> qrcode = (ArrayList<QRCode>) map.get("QRCode");
                                    int total = 0;
                                    for (int i = 0; i < qrcode.size(); i++) {
                                        Map map1 = (Map) qrcode.get(i);
                                        int score = ((Long) map1.get("score")).intValue();
                                        total += score;

                                        SRUSERAdapater.add((String) map1.get("name"));
                                    }
                                    totalscore.setText("Total Score:  " + total);
                                    totalamount.setText("Total Amount:  " + qrcode.size());

                                    break;
                                }
                            }
                        }
                    }
                });

        qrcodelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent =new Intent(SearchUserDetail.this, SearchQRDetail.class);

                String searchqrcodename = qrnamelist.get(i).toLowerCase().replaceAll("\\s+", "");

                intent.putExtra("qrcodename", searchqrcodename);


                startActivityForResult(intent,10);



            }
        });

        Button Return = findViewById(R.id.searchuserdetail_return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUserDetail.super.onBackPressed();
            }
        });
    }


}
