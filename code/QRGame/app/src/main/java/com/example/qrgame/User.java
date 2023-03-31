package com.example.qrgame;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 *
 * The user class contain the username,phonenumber,and android device id
 */

public class User {
    private String username;
    private String phonenumber;
    private String androidId;
    ArrayList<QRCode> qrcode;



    public User(String username, String phonenumber, String androidId, ArrayList<QRCode> qrcode) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.androidId = androidId;
        this.qrcode=qrcode;
    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAndroidId() {
        return androidId;
    }

    public ArrayList<QRCode> getQrcode() {
        return qrcode;
    }

    public static void addQRCode(QRCode qrCode, String userName) {
        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = fireStore.collection("UserCollection").document(userName);
        docRef2.update("QRCode", FieldValue.arrayUnion(qrCode));
    }
}


