package com.example.qrgame;



public class User {
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
