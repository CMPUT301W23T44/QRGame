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

/**
 * Interface between the program app and the online Firebase database
 */
public class QRDatabaseController {

    private FirebaseFirestore qrFireStore;
    private FirebaseDatabase qrDatabase;
    private static final String COLLECTION_NAME = "qrCodes";
    private CollectionReference QR_CODE_COLLECTION;

    private final static String TAG = "QRDataBaseAction";
    private String hash;

    QRDatabaseController() {
        qrFireStore = FirebaseFirestore.getInstance();
        QR_CODE_COLLECTION = qrFireStore.collection(COLLECTION_NAME);
        qrDatabase = FirebaseDatabase.getInstance();

    }

    /**
     * Adds a QR code to the Firebase database
     * @param qrCode - QR code to be added to the database
     */
    public void addQR(QRCode qrCode) {
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
     * @param hash - QR code hash to be deleted
     */
    public void deleteQR(String hash) {
        QR_CODE_COLLECTION.document(hash)
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
    public boolean findQR(String hash) { // TODO - Fix find function
        final boolean[] result = new boolean[1];
        DocumentReference docRef = QR_CODE_COLLECTION.document(hash);
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
                    Log.d(TAG, "Find failure");
                }
            }
        });
        return result[0];
    }

    /**
     * NOT WORKING
     * Gets the provided QR code from the firebase database
     * @param hash - QR code hash to be retrieved
     * @return
     *      Desired QR code object
     */
    public QRCode getQRCode(String hash) {   // TODO - Fix get function
        final QRCode[] qrCode1 = new QRCode[1];
        qrCode1[0] = null;
        this.hash = hash;
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(QRCode qrCode) {
                qrCode1[0] = qrCode;
            }
        });
        return qrCode1[0];
    }

    private void readData(FirebaseCallback firebaseCallback) {
        DocumentReference docRef = QR_CODE_COLLECTION.document(hash);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                QRCode qrCode = task.getResult().toObject(QRCode.class);
                firebaseCallback.onCallback(qrCode);
            }
        });
    }
    private interface FirebaseCallback{
        void onCallback(QRCode qrCode);
    }

}
