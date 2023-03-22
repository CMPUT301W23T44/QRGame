package com.example.qrgame;

import java.io.Serializable;

/**
 *
 * The user class contain the username,phonenumber,and android device id
 */

public class User implements Serializable {
    private String username;
    private String phonenumber;
    private String androidId;

    public User(String username, String phonenumber,String androidId) {
        this.username = username;
        this.phonenumber = phonenumber;
        this.androidId = androidId;
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
}
