package com.example.qrgame;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
* keep track of all Qrcodes scanned by user and allow to view details and delete Qrcode
 */
public class Inventory_activity extends AppCompatActivity {

    private ArrayList<QRCode> QrDataList;
    private ListView QrCodeList;
    private QRCodeArrayAdapter QrAdapter;
    private ArrayList<QRCode> qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        QrDataList=new ArrayList<>();

        QrCodeList=findViewById(R.id.inventory_qr_list);
        QrAdapter=new QRCodeArrayAdapter(this,QrDataList);
        QrCodeList.setAdapter(QrAdapter);
        final int[] totalPoints = new int[1];

        TextView username=findViewById(R.id.inventory_username_text);

        //
        /**
         * get username and qrcode scanned by this specific user
         * */
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document=task.getResult();
                    if (document.exists()){
                        Map map=document.getData();
                        String androidKey=(String) map.get("AndroidKey");
                        String phone=(String)map.get("PhoneKey");
                        String usern=(String) map.get("UserNameKey");
                        User user=new User(usern,phone,androidKey,qrcode);
                        username.setText("username:"+user.getUsername());
                    }
                }
            }
        });
        //



//


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
                                String location_image = (String) map.get("location_image");

                                QRCode qrCode = new QRCode(score, hash, name, face, lat, lng, users, comments, location_image);

                                QrAdapter.add(qrCode);
                                totalPoints[0] =GetTotalPoints();
                                totalPoint.setText("Total score: "+totalPoints[0]);
                                totalQr.setText("Total QR codes: "+QrDataList.size());



                                QrAdapter.notifyDataSetChanged();


                            }}else{
                            Log.d("QRread","error on getting doc",task.getException());
                            }
                        }

                });










//
        Button sortButton=findViewById(R.id.inventory_sort_button);
        Button backButton=findViewById(R.id.inventory_back_button);

        /**
         * list listener to view/delete one item on selection
         * */

        QrCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Button deleteButton=findViewById(R.id.inventory_delete_button);
                Button detailsButton=findViewById(R.id.inventory_qr_details);


                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (i >=0 && QrDataList.size()>0) {
                            //
                            QRCode qrCode=QrDataList.get(i);
//                            deleteQR(qrCode);

                            //
                            QrDataList.remove(i);

                            totalPoints[0] =GetTotalPoints();
                            totalPoint.setText("total score: "+totalPoints[0]);
                            totalQr.setText("Total QR codes: "+QrDataList.size());
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


        /**
         * return back to main activity
         * */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Inventory_activity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });

        /**
         * sort all scanned qrcodes in descending order
         * */
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // QrDataList.sort(((o1, o2) -> o1.getScore().compareTo(o2.getScore())));
                Collections.sort(QrDataList,Comparator.comparing(QRCode::getScore));
                Collections.reverse(QrDataList);
                QrAdapter.notifyDataSetChanged();
            }
        });


    }

    /**
     * @return total number of points from all scanned qrCodes
     * */
    public int GetTotalPoints(){
        int total=0;
        for (int i=0;i<QrDataList.size();i++){
            int score = QrDataList.get(i).getScore();
            total+=score;
        }
        return total;
    }

    /**
     * Gets device Id
     * @return
     * Device Id as a String
     */
    public String getUdid() {
        String androidID=AndroidID();
        return"2"+ UUID.nameUUIDFromBytes(androidID.getBytes()).toString().replace("-","");
    }


    /**
     * Gets Android Id
     * @return
     * Android Id as a String
     */
    public String AndroidID() {
        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return id == null ? "" : id;
    }
}