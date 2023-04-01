package com.example.qrgame;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Base64;
import java.util.Map;

public class SearchQRDetail extends AppCompatActivity {
    private ArrayList<String> users;
    private ListView userlist;
    private SearchUserDetailAdapater SQRDAdapter;
    private String qrcodename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchqrdetail);

        userlist = findViewById(R.id.searchqrdetail_userlist);

        TextView qrname = findViewById(R.id.searchqrdetail_qrname);
        TextView qrscore = findViewById(R.id.searchqrdetail_qrscore);
        TextView qrFace=findViewById(R.id.searchqrdetail_qr_face);
        ImageView image=findViewById(R.id.searchqrdetail_surrounding_image);

        qrcodename = (String) getIntent().getStringExtra("qrcodename");

        users = new ArrayList<>();
        SQRDAdapter = new SearchUserDetailAdapater(this,users);
        userlist.setAdapter(SQRDAdapter);

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference collection_name = fireStore.collection("qrCodes");
        fireStore.collection("qrCodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                String databasecodename = document.getString("name").toLowerCase().replaceAll("\\s+", "");
                                if (qrcodename.equals(databasecodename)) {
                                    qrname.setText(document.getString("name"));

                                    Map map = document.getData();
                                    qrscore.setText("Score:  " + ((Long)map.get("score")).intValue());

                                    qrFace.setText((String) map.get("face"));

                                    image.setImageBitmap(getLocationImageBitmap((String) map.get("location_image")));

                                    ArrayList<String> allUsers = (ArrayList<String>) map.get("users");

                                    for(int i =0 ; i<allUsers.size();i++)
                                    {
                                        SQRDAdapter.add(allUsers.get(i));
                                    }


                                    SQRDAdapter.notifyDataSetChanged();

                                    break;
                                }
                            }
                        }
                    }
                });

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent =new Intent(SearchQRDetail.this, SearchUserDetail.class);

                String searchqrcodename = users.get(i);

                intent.putExtra("username", searchqrcodename);


                startActivityForResult(intent,10);



            }
        });

        Button Return = findViewById(R.id.searchqrdetail_return);
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchQRDetail.super.onBackPressed();
            }
        });

    }
    public Bitmap getLocationImageBitmap(String image) {
        byte[] bytes = Base64.getDecoder().decode(image);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
