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

/**
 * Interface between the program app and the online Firebase database
 */
public class QRDatabaseController {


    private static final FirebaseFirestore qrDB = FirebaseFirestore.getInstance();
    private static final String COLLECTION_NAME = "qrCodes";
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
     * @param hash - QR code hash to be deleted
     */
    public static void deleteQR(String hash) {
        if (findQR(hash)) {
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
    }

    /**
     * Finds a provided QR code in the database
     * @param hash - QR code hash to be found
     * @return
     *      True if the QR code exists in the database
     */
    public static boolean findQR(String hash) {
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
     * Gets the provided QR code from the firebase database
     * @param hash - QR code hash to be retrieved
     * @return
     *      Desired QR code object
     */
    public static QRCode getQRCode(String hash) {
        final QRCode[] qrCode = new QRCode[1];
        DocumentReference docRef = QR_CODE_COLLECTION.document(hash);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        qrCode[0] = documentSnapshot.toObject(QRCode.class);
                    } else {
                        qrCode[0] = null;
                    }
                } else {
                    Log.d(TAG, "Get failure");
                }
            }
        });
        return qrCode[0];
    }

}
