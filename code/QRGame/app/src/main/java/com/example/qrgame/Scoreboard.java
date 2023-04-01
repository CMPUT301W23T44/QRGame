package com.example.qrgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

// Made by Alex Huo.
// This is the Scoardboard activity.
// It shows the all users scorting by their total mark by descending order.
// It also shows the current user with the mark and rank.
// By the buttons, user can return back to the mainpage or search other users or qrcodes in the database.


public class Scoreboard extends AppCompatActivity {
    private ArrayList<ScoreRank> ScList;

    private ListView ScoreboardList;

    private ScoreboardAdapter ScAdapter;

    private TextView currentUserRank;

    private TextView currentUserScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);

        // Initialize TextViews for displaying user's rank and score
        currentUserRank = findViewById(R.id.cur_rank);
        currentUserScore = findViewById(R.id.cur_score);

        // Initialize ArrayList and ListView for the scoreboard
        ScList = new ArrayList<>();
        ScoreboardList = findViewById(R.id.scoreboard_list);
        ScAdapter = new ScoreboardAdapter(this,ScList);
        ScoreboardList.setAdapter(ScAdapter);

        // Get the current user's username from the intent
        String currentUsername = (String) getIntent().getStringExtra("Username");

        // Connect to Firestore and get the "UserCollection" collection
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection = fireStore.collection("UserCollection");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Iterate through each document in the collection
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        Map map = document.getData();
                        String usern = (String) map.get("UserNameKey");
                        ArrayList<QRCode> qrcode = (ArrayList<QRCode>) map.get("QRCode");
                        if (qrcode !=null && qrcode.size()!=0) {

                            // Calculate the total score for the user
                            int temp = 0;
                            for (int i = 0; i < qrcode.size(); i++) {
                                Map map1 = (Map) qrcode.get(i);
                                int score = ((Long) map1.get("score")).intValue();
                                temp += score;
                            }
                            // Add user to scoreboard with their total score
                            ScAdapter.add(new ScoreRank(usern, temp));
                        }

                        // Sort the scoreboard in descending order of score
                        Collections.sort(ScList, new Comparator<ScoreRank>() {
                            @Override
                            public int compare(ScoreRank s1, ScoreRank s2) {
                                return s2.GetScoreInt() - s1.GetScoreInt();
                            }
                        });

                        // Set the rank for each user in the sorted list
                        for(int index = 0; index < ScAdapter.getCount(); index++)
                        {
                            ScList.get(index).SetRank(index+1);
                        }

                        // Find the current user's rank and score, and display them
                        for (int index = 0; index < ScAdapter.getCount(); index++) {
                            if (ScList.get(index).GetName().equals(currentUsername)) {
                                currentUserRank.setText("Your Rank: " + ScList.get(index).GetRankString());
                                currentUserScore.setText("Your Score: " + ScList.get(index).GetScoreInt());
                            }
                        }

                        // Update the ListView with the new data
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


