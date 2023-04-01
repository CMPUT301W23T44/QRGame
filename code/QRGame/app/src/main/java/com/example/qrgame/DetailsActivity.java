package com.example.qrgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class DetailsActivity extends AppCompatActivity {

    private  QRCode qrcode;
    private String userName;
    private ArrayList<QRCode>  qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        TextView qrname=findViewById(R.id.details_name);
        TextView qrScore=findViewById(R.id.details_score);
        TextView qrFace=findViewById(R.id.details_qr_face);
        ImageView image=findViewById(R.id.details_surrounding_image);


        Button back=findViewById(R.id.details_back_button);
        Button delete=findViewById(R.id.detail_delete_button);

        //userQR= new ArrayList<>();
        qrcode = (QRCode) getIntent().getSerializableExtra("qrCode");
        userName = (String) getIntent().getStringExtra("Username");

        qrname.setText(qrcode.getName());
        qrScore.setText("Score:"+qrcode.getScore());
        qrFace.setText(qrcode.getFace());
        image.setImageBitmap(qrcode.getLocationImageBitmap());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DetailsActivity.this,Inventory_activity.class);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteQR();
                qrcode.removeUsers(userName);
                qrcode.removeComments(userName);
                QRDatabaseController dbAdapter=QRDatabaseController.getInstance();
                dbAdapter.pushQR(qrcode);

                Intent intent =new Intent(DetailsActivity.this,Inventory_activity.class);
                startActivity(intent);
            }
        });


    }








    public void deleteQR(){
        //qrcode.toString();
        //qr=new QRCode(qrcode.getScore(), qrcode.getHash(),qrcode.getName(),qrcode.getFace(),qrcode.getLatLng().latitude,qrcode.getLatLng().longitude,qrcode.getUsers(),qrcode.getComments(),qrcode.getLocation_image());
        //Log.d("RRG", "checkqrcode111"+qrcode.toString());
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
                                        qr = (ArrayList<QRCode>) document2.get("QRCode");
                                        qr.get(0).getHash();
                                        if(qr.size()>1) {
                                            for (int i = 1; i < qr.size(); i++) {
                                                Log.d("RRG", "checkqrcode111" + qr.get(i).getHash());
                                                docRef2.update("QRCode", FieldValue.arrayRemove(qr.get(i)));

                                            }

                                        }else{
                                            Log.d("RRG", "checkqrcode111" + qr.get(0).getHash());
                                            docRef2.update("QRCode", FieldValue.arrayRemove(qr.get(0)));
                                        }
                                    }

                                }
                            }
                        });

                    }
                }
                //docRef2.update("QRCode", FieldValue.arrayRemove());
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