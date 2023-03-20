package com.example.qrgame;

import android.content.Intent;
import android.util.Log;

public class QRPreprocess {

    private static boolean exists = false;
    private static QRCode gottenQRCode;

    public static boolean exists(String hash) {
        // Create a new QR code and add it to the database
        QRDatabaseController dbAdapter = QRDatabaseController.getInstance();
        dbAdapter.findQR(hash, new QRDatabaseController.QRCodeExistsCallback() {
            @Override
            public void onQRCodeCallback(boolean qrExists) {
                Log.d("TestQR", String.valueOf(qrExists));
                exists = qrExists;
            }
        });
        return exists;
    }


}
