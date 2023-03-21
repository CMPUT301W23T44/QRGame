package com.example.qrgame;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a QR code object
 */
@IgnoreExtraProperties
public class QRCode implements Comparable, Serializable {

    @PropertyName("score")
    private int score;
    @PropertyName("hash")
    private String hash;
    @PropertyName("name")
    private String name;
    @PropertyName("face")
    private String face;

    @PropertyName("longitude")
    private double longitude;
    @PropertyName("latitude")
    private double latitude;

    @PropertyName("users")
    private ArrayList<String> users;
    @PropertyName("comments")
    private ArrayList<String> comments;

    public QRCode() {
    }

    public QRCode(String hash){

        this.hash = hash;
        score = calcScore();
        face = NameFaceScheme.generateFace(hash);
        name = NameFaceScheme.generateName(hash);
        this.latitude = 0;
        this.longitude = 0;
        users = new ArrayList<>();
        comments = new ArrayList<>();
    }



    public QRCode(int score, String hash, String name, String face, double latitude, double longitude,
                  ArrayList<String> users, ArrayList<String> comments) {
        this.score = score;
        this.hash = hash;
        this.name = name;
        this.face = face;
        setLatLong(latitude, longitude);
        this.users = users;
        this.comments = comments;
    }

//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    public void setHash(String hash) {
//        this.hash = hash;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setFace(String face) {
//        this.face = face;
//    }

    public void addUsers(String uid) {
        users.add(uid);
    }

    public void addComments(String comment) {
        comments.add(comment);
    }

    public void setLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Calculates the score of a QR code based off the hash provided
     */
    private int calcScore() {
        int RADIX = 16;
        int ZERO_VALUE = 20;
        String previousChar = String.valueOf(hash.charAt(0));
        String currentChar;
        int currentScoreValue = 0;
        boolean firstInstance = true;
        int score = 0;

        // Finds consecutive instances of a character in the hash and bases the score off the
        // power of these consecutive values
        for (int i = 1; i < hash.length(); i++) {
            currentChar = String.valueOf(hash.charAt(i));
            if (previousChar.equals("0") && firstInstance) {
                currentScoreValue = 1;
                firstInstance = false;
            }
            if (currentChar.equals(previousChar)) {
                if (firstInstance) {
                    currentScoreValue = Integer.parseInt(currentChar, RADIX);
                    firstInstance = false;
                } else {
                    // Zeroes are worth the most points
                    if (currentChar.equals("0")) {
                        currentScoreValue *= ZERO_VALUE;
                    } else {
                        currentScoreValue *= (Integer.parseInt(currentChar, RADIX));
                    }
                }
            } else {
                score += currentScoreValue;
                currentScoreValue = 0;
                firstInstance = true;
            }
            previousChar = currentChar;
        }
        return score;
    }

    @PropertyName("score")
    public int getScore() {
        return score;
    }

    @PropertyName("hash")
    public String getHash() {
        return hash;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("face")
    public String getFace() {
        return face;
    }

    public ArrayList<String> getUsers() {
        return users;
    }
    public LatLng getLatLng() {
        return new com.google.android.gms.maps.model.LatLng(latitude, longitude);
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    @Override
    public int compareTo(Object o) {
        QRCode qrCode = (QRCode) o;
        return ((Integer) this.getScore())
                .compareTo((Integer) qrCode.getScore());
    }
}


