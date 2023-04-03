package com.example.qrgame;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Interface between the program app and the online Firebase database
 */
public class QRDatabaseController {

    // Interfaces to handle callbacks and process data received from the database
    public interface QRCodeExistsCallback {
        void onQRCodeCallback(boolean qrExists);
    }

    public interface GetQRCodeCallback {
        void onGetQRCodeCallback(QRCode qrCode);
    }

    // Interfaces end point

    private final FirebaseFirestore db;
    private static QRDatabaseController instance = null;

    private static final String COLLECTION_NAME = "qrCodes";
    private final static String TAG = "QRDataBaseAction";


    private QRDatabaseController() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Retrieve a singleton instance of the database
     *
     * @return Instance of the database
     */
    public static QRDatabaseController getInstance() {
        if (instance == null) {
            instance = new QRDatabaseController();
        }
        return instance;
    }


    /**
     * Adds a QR code to the Firebase database
     *
     * @param qrCode - QR code to be added to the database
     */
    public void pushQR(QRCode qrCode) {
        db.collection(COLLECTION_NAME)
                .document(qrCode.getHash())
                .set(qrCode)
                .addOnSuccessListener(unused -> Log.d(TAG, "Add successful"))
                .addOnFailureListener(e -> Log.d(TAG, "Add failure"));
    }

    /**
     * Deletes a provided QR code from the database, if it exists
     *
     * @param hash - QR code hash to be deleted
     */
    public void deleteQR(String hash) { // TODO - Maybe delete if not needed
        db.collection(COLLECTION_NAME).document(hash)
                .delete()
                .addOnSuccessListener(unused -> Log.d(TAG, "Delete successful"))
                .addOnFailureListener(e -> Log.d(TAG, "Delete failure"));

    }

    /**
     * Finds a provided QR code in the database
     *
     * @param hash     - QR code hash to be found
     * @param callback - Callback that handles the result
     */
    public void findQR(String hash, QRDatabaseController.QRCodeExistsCallback callback) { // TODO - Fix find function
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query conflicts = db.collection(COLLECTION_NAME)
                .whereEqualTo("hash", hash);
        conflicts.get().addOnSuccessListener(queryDocumentSnapshots -> callback.onQRCodeCallback(!queryDocumentSnapshots.isEmpty()));
    }

    /**
     * Gets the provided QR code from the firebase database
     *
     * @param hash     - QR code hash to be retrieved
     * @param callback - Callback that handles the result
     */
    public void getQRCode(String hash, QRDatabaseController.GetQRCodeCallback callback) {   // TODO - Fix get function
        FirebaseFirestore qrFireStore = FirebaseFirestore.getInstance();
        qrFireStore.collection(COLLECTION_NAME)
                .document(hash)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    QRCode qrCode = documentSnapshot.toObject(QRCode.class);
                    callback.onGetQRCodeCallback(qrCode);
                });
    }
}
