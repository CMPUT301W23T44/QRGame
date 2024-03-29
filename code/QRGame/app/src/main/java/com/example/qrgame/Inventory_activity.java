package com.example.qrgame;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import com.example.qrgame.QRDatabaseController.deleteQR;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    int totalPoints  ;

    private String currUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        QrDataList=new ArrayList<>();
        currUser = (String) getIntent().getStringExtra("Username");

        QrCodeList=findViewById(R.id.inventory_qr_list);
        QrAdapter=new QRCodeArrayAdapter(this,QrDataList);
        QrCodeList.setAdapter(QrAdapter);


        TextView totalPoint=findViewById(R.id.inventory_total_score);
        TextView totalQr=findViewById(R.id.inventory_total_amount);

        TextView username=findViewById(R.id.inventory_username_text);

        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();

        //
        /**
         * get username and qrcode scanned by this specific user
         * */
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                               @Override
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
                                                                           String androidKey = (String) map.get("AndroidKey");
                                                                           String phone = (String) map.get("PhoneKey");
                                                                           String usern = (String) map.get("UserNameKey");
                                                                           ArrayList<QRCode> qrcode = (ArrayList<QRCode>) map.get("QRCode");


                                                                           if (qrcode !=null && qrcode.size()!=0) {
                                                                               for (int i = 0; i < qrcode.size(); i++) {
                                                                                   Map map1 = (Map) qrcode.get(i);
                                                                                   int score = ((Long) map1.get("score")).intValue();
                                                                                   String hash = (String) map1.get("hash");
//

                                                                                   dbAdapter.getQRCode(hash, qrCode -> {
                                                                                       if (qrCode != null) {
                                                                                           QrAdapter.add(qrCode);
                                                                                           totalPoints = GetTotalPoints();
                                                                                           totalPoint.setText("Score: " + totalPoints);
                                                                                           totalQr.setText("Total QR codes: " + QrDataList.size());
                                                                                       }
                                                                                   });


                                                                               }
                                                                           }else{
                                                                               totalPoint.setText("Score: " + 0);
                                                                               totalQr.setText("Total QR codes: " +0);
                                                                           }





                                                                            QrAdapter.notifyDataSetChanged();
                                                                           User user = new User(usern, phone, androidKey, qrcode);
                                                                           Log.d("RRG", "check1"+user.getQrcode());
                                                                           username.setText("Username: " + user.getUsername());

                                                                       }
                                                                   }
                                                               }
                                                           });
                                                       }
                                                   }
                                               }

        });


//






//        FirebaseFirestore qrDB = FirebaseFirestore.getInstance();
//        String COLLECTION_NAME = "qrCodes";
//        CollectionReference QR_CODE_COLLECTION = qrDB.collection(COLLECTION_NAME);
//
//        qrDB.collection(COLLECTION_NAME)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
//                                Map latLng = (Map) map.get("latLng");
//                                double lat = (Double) latLng.get("latitude");
//                                double lng = (Double) latLng.get("longitude");
//
//                                ArrayList<String> users = (ArrayList<String>) map.get("users");
//                                HashMap<String, String> comments = (HashMap<String, String>) map.get("comments");
//                                String location_image = (String) map.get("location_image");
//
//                                QRCode qrCode = new QRCode(score, hash, name, face, lat, lng, users, comments, location_image);
//
//                                QrAdapter.add(qrCode);
//                                totalPoints[0] =GetTotalPoints();
//                                totalPoint.setText("Total score: "+totalPoints[0]);
//                                totalQr.setText("Total QR codes: "+QrDataList.size());
//
//
//
//                                QrAdapter.notifyDataSetChanged();
//
//
//                            }}else{
//                            Log.d("QRread","error on getting doc",task.getException());
//                            }
//                        }
//
//                });










//
        Button sortButton=findViewById(R.id.inventory_sort_button);
        Button backButton=findViewById(R.id.inventory_back_button);

        /**
         * list listener to view/delete one item on selection
         * */

        QrCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent =new Intent(Inventory_activity.this, DetailsActivity.class);
                intent.putExtra("qrCode",QrDataList.get(i));
                Log.d("RRG", "checkqrcodesss"+QrDataList.get(i).toString());
                intent.putExtra("scanned", true);
                intent.putExtra("Username", currUser);


                startActivityForResult(intent,10);



            }
        });


        /**
         * return back to main activity
         * */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Inventory_activity.this,MainPageActivity.class);
                intent.putExtra("Username",currUser);
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
        int score=0;
        for (int i=0;i<QrDataList.size();i++){
            score = QrDataList.get(i).getScore();
            total+=score;
        }
        return total;
    }
//
//    /**
//     * delete one qrcode in usercollection
//     * @param qrCode
//     */
//    public void deleteQR(QRCode qrCode){
//        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
//        DocumentReference docRef = fireStore.collection("LoginUser").document(getUdid());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String Username = document.getString("UserNameKey");
//                        DocumentReference docRef2 = fireStore.collection("UserCollection").document(Username);
//                        docRef2.update("QRCode", FieldValue.arrayRemove(qrCode));
//
//                    }
//                }
//            }
//
//        });
//    }

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

    /**
     *
     * @param requestCode Activity result
     * @param resultCode Activity result
     * @param result  Activity result
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==10){
                if (result!=null){
                    QRCode qrcode;
                    qrcode = (QRCode) result.getSerializableExtra("qrcode");
                    int index = 0;
                    for (int i=0; i < QrDataList.size(); ++i){
                        if (QrDataList.get(i).getHash().equals(qrcode.getHash())){
                            QrDataList.remove(i);
                            break;
                        }
                        index++;

                    }
                    System.out.println("iciiiiiiiiiiiiiiiiiiiiii index:"+index);
                    //QrDataList.remove(index - 1);
                    totalPoints = GetTotalPoints();
                    TextView totalPoint=findViewById(R.id.inventory_total_score);
                    TextView totalQr=findViewById(R.id.inventory_total_amount);

                    totalPoint.setText("Total score: " + totalPoints);
                    totalQr.setText("Total QR codes: " + QrDataList.size());
                    QrAdapter.notifyDataSetChanged();


                }
            }
        }
    }


    }