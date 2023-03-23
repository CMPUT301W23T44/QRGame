package com.example.qrgame;

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
}

