package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Scoreboard extends AppCompatActivity {
    private ArrayList<ScoreRank> ScList;

    private ListView ScoreboardList;

    private ScoreboardAdapter ScAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);

        ScList = new ArrayList<>();
        ScoreboardList = findViewById(R.id.scoreboard_list);
        ScAdapter = new ScoreboardAdapter(this,ScList);
        ScoreboardList.setAdapter(ScAdapter);


        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection = fireStore.collection("UserCollection");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        Map map = document.getData();
                        String usern = (String) map.get("UserNameKey");
                        ArrayList<QRCode> qrcode = (ArrayList<QRCode>) map.get("QRCode");
                        if (qrcode !=null && qrcode.size()!=0) {
                            int temp = 0;

                            for (int i = 0; i < qrcode.size(); i++) {
                                Map map1 = (Map) qrcode.get(i);
                                int score = ((Long) map1.get("score")).intValue();
                                temp += score;
                            }
                            ScAdapter.add(new ScoreRank(usern, temp));
                        }

                        Collections.sort(ScList, new Comparator<ScoreRank>() {
                            @Override
                            public int compare(ScoreRank s1, ScoreRank s2) {
                                return s2.GetScoreInt()-s1.GetScoreInt();
                            }
                        });

                        for(int index = 0; index < ScAdapter.getCount();index++)
                        {
                            ScList.get(index).SetRank(index+1);
                        }

                        ScAdapter.notifyDataSetChanged();
                    }

                }
            }
        });

        Button Return = findViewById(R.id.button2);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Scoreboard.super.onBackPressed();
            }
        });

        Button SearchUser = findViewById(R.id.button4);
        SearchUser.setOnClickListener(view -> {
            Intent searchuser_page = new Intent(Scoreboard.this, SearchUser.class);
            startActivity(searchuser_page);
        });

        Button SearchQR = findViewById(R.id.button3);
        SearchQR.setOnClickListener(view -> {
            Intent searchqr_page = new Intent(Scoreboard.this, SearchQR.class);
            startActivity(searchqr_page);
        });


    }
}


