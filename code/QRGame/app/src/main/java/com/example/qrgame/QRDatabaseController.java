package com.example.qrgame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.Map;

/**
 * Interface between the program app and the online Firebase database
 */
public class QRDatabaseController {

    public interface QRCodeExistsCallback{
        void onQRCodeCallback(boolean qrExists);
    }

    private FirebaseFirestore db;
    private static QRDatabaseController instance = null;


    private static final String COLLECTION_NAME = "qrCodes";

    private final static String TAG = "QRDataBaseAction";

    private QRDatabaseController() {
        db = FirebaseFirestore.getInstance();
    }

    public static QRDatabaseController getInstance() {
        if (instance == null) {
            instance = new QRDatabaseController();
        }
        return instance;
    }


        /**
         * Adds a QR code to the Firebase database
         * @param qrCode - QR code to be added to the database
         */
    public void pushQR (QRCode qrCode) {
        db.collection(COLLECTION_NAME)
                .document(qrCode.getHash())
                .set(qrCode)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Add successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Add failure");
                    }
                });
    }

    /**
     * Deletes a provided QR code from the database, if it exists
     * @param hash - QR code hash to be deleted
     */
    public void deleteQR(String hash) {
        db.collection(COLLECTION_NAME).document(hash)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Delete successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Delete failure");
                    }
                });

    }

    /**
     * NOT WORKING
     * Finds a provided QR code in the database
     * @param hash - QR code hash to be found
     * @return
     *      True if the QR code exists in the database
     */
    public static void findQR(String hash, QRDatabaseController.QRCodeExistsCallback callback) { // TODO - Fix find function
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query conflicts = db.collection(COLLECTION_NAME)
                .whereEqualTo("hash", hash);
        conflicts.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    callback.onQRCodeCallback(false);
                } else {
                    callback.onQRCodeCallback(true);
                }
            }
        });
    }

    /**
     * NOT WORKING
     * Gets the provided QR code from the firebase database
     * @param hash - QR code hash to be retrieved
     * @return
     *      Desired QR code object
     */
    public static QRCode getQRCode(String hash) {   // TODO - Fix get function
        FirebaseFirestore qrFireStore = FirebaseFirestore.getInstance();
        qrFireStore.collection("qrCodes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData());
                                Map map = document.getData();
                                int score = ((Long) map.get("score")).intValue();
                                String hash = (String) map.get("hash");
                                String name = (String) map.get("name");
                                String face = (String) map.get("face");

                                QRCode qrCode = new QRCode(score, hash, name, face);

                            }
                        } else {
                            Log.d("QRread", "error on getting doc", task.getException());
                        }
                    }

                });
        return null;
    }

}
