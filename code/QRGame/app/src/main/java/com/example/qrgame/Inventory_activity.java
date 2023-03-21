package com.example.qrgame;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.qrgame.QRDatabaseController.deleteQR;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory_activity extends AppCompatActivity {

    private ArrayList<QRCode> QrDataList;
    private ListView QrCodeList;
    private QRCodeArrayAdapter QrAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        QrDataList=new ArrayList<>();

        QrCodeList=findViewById(R.id.inventory_qr_list);
        QrAdapter=new QRCodeArrayAdapter(this,QrDataList);
        QrCodeList.setAdapter(QrAdapter);

//

        TextView username=findViewById(R.id.inventory_username_text);
        TextView totalPoint=findViewById(R.id.inventory_total_score);
        TextView totalQr=findViewById(R.id.inventory_total_amount);

        FirebaseFirestore qrDB = FirebaseFirestore.getInstance();
        String COLLECTION_NAME = "qrCodes";
        CollectionReference QR_CODE_COLLECTION = qrDB.collection(COLLECTION_NAME);

        qrDB.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getData());
                                Map map=document.getData();
                                int score=( (Long) map.get("score")).intValue();
                                String hash= (String) map.get("hash");
                                String name= (String) map.get("name");
                                String face= (String) map.get("face");
                                Map latLng = (Map) map.get("latLng");
                                double lat = (Double) latLng.get("latitude");
                                double lng = (Double) latLng.get("longitude");

                                ArrayList<String> users = (ArrayList<String>) map.get("users");
                                ArrayList<String> comments = (ArrayList<String>) map.get("comments");

                                QRCode qrCode = new QRCode(score, hash, name, face, lat, lng, users, comments);

                                QrAdapter.add(qrCode);
                                QrAdapter.notifyDataSetChanged();


                            }}else{
                            Log.d("QRread","error on getting doc",task.getException());
                            }
                        }

                });










//
        Button sortButton=findViewById(R.id.inventory_sort_button);
        Button backButton=findViewById(R.id.inventory_back_button);



        QrCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Button deleteButton=findViewById(R.id.inventory_delete_button);
                Button detailsButton=findViewById(R.id.inventory_qr_details);


                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (i >=0) {
                            //
                            QRCode qrCode=QrDataList.get(i);
//                            deleteQR(qrCode);

                            //
                            QrDataList.remove(i);
                            QrAdapter.notifyDataSetChanged();
                        }
                    }
                });

                detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent =new Intent(Inventory_activity.this,QRInfoActivity.class);
                        intent.putExtra("qrCode",QrDataList.get(i));
                        startActivity(intent);
                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Inventory_activity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });










    }
}