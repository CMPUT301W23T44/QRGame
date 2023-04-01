package com.example.qrgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import java.util.Objects;
import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    private  QRCode qrcode;
    private String userName;

    private ArrayList<QRCode>  qr;

    private CommentAdapter adapter;
    private ListView commentList;
    private HashMap<String ,String> commentMap;
    private ArrayList<Comment> commentData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        commentList=findViewById(R.id.details_comment_list);
        commentMap=new HashMap<>();
        commentData=new ArrayList<>();
        adapter=new CommentAdapter(this,commentData);
        commentList.setAdapter(adapter);



        TextView qrname=findViewById(R.id.details_name);
        TextView qrScore=findViewById(R.id.details_score);
        TextView qrFace=findViewById(R.id.details_qr_face);
        ImageView image=findViewById(R.id.details_surrounding_image);


        Button back=findViewById(R.id.finish_button);
        Button delete=findViewById(R.id.detail_delete_button);

        //userQR= new ArrayList<>();
        qrcode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = (String) getIntent().getStringExtra("Username");

        commentMap=qrcode.getComments();
        if (commentMap!=null) {
            for (Map.Entry<String, String> entry : commentMap.entrySet()) {
                Comment com = new Comment(entry.getKey(), entry.getValue());
                adapter.add(com);
            }
        }


        qrname.setText(qrcode.getName());
        qrScore.setText("Score:"+qrcode.getScore());
        qrFace.setText(qrcode.getFace());
        image.setImageBitmap(qrcode.getLocationImageBitmap());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DetailsActivity.this,Inventory_activity.class);
                intent.putExtra("Username", userName);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQR(qrcode.getHash());
                qrcode.removeUsers(userName);
                qrcode.removeComments(userName);
                QRDatabaseController dbAdapter=QRDatabaseController.getInstance();
                dbAdapter.pushQR(qrcode);

                Intent intent =new Intent();
                intent.putExtra("qrcode",qrcode);
                setResult(Activity.RESULT_OK,intent);
                finish();
//                startActivity(intent);
            }
        });


    }








    public void deleteQR(String hash){

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("UserNameKey");
                        DocumentReference docRef2 = fireStore.collection("UserCollection").document(Username);
                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document2 = task.getResult();
                                    if (document2.exists()) {
                                        Map map = document2.getData();
                                        ArrayList<QRCode> qr = (ArrayList<QRCode>) map.get("QRCode");
                                            for (int i = 0; i < qr.size(); i++) {
                                                Map map1 = (Map) qr.get(i);
                                                String hash2=map1.get("hash").toString();
                                                if (Objects.equals(hash, hash2)) {
                                                    docRef2.update("QRCode", FieldValue.arrayRemove(qr.get(i)));
                                                }

                                            }



                                        }

                                    }
                                }
                            });

                        }
                    }

                    }
                });
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