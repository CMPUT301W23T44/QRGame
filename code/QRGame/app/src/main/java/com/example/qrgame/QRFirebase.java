package com.example.qrgame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface between the program app and the online Firebase database
 */
public class QRFirebase {


    private static FirebaseFirestore qrDB = FirebaseFirestore.getInstance();
    private static String COLLECTION_NAME = "qrCodes";
    private final static CollectionReference QR_CODE_COLLECTION = qrDB.collection(COLLECTION_NAME);

    private final static String TAG = "QR_adder";

    /**
     * Adds a QR code to the Firebase database
     * @param qrCode - QR code to be added to the database
     */
    public static void addQR(QRCode qrCode) {
        QR_CODE_COLLECTION
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
     * @param qrCode - QR code to be deleted
     */
    public static void deleteQR(QRCode qrCode) {
        if (findQR(qrCode)) {
            QR_CODE_COLLECTION.document(COLLECTION_NAME)
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
    }

    /**
     * Finds a provided QR code in the database
     * @param qrCode - QR code to be found
     * @return
     *      True if the QR code exists in the database
     */
    public static boolean findQR(QRCode qrCode) {
        final boolean[] result = new boolean[1];
        DocumentReference docRef = qrDB.collection(COLLECTION_NAME).document(qrCode.getHash());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        result[0] = true;
                    } else {
                        result[0] = false;
                    }
                } else {
                    Log.d(TAG, "Get failure");
                }
            }
        });
        return result[0];
    }

}
