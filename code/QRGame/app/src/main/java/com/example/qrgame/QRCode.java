package com.example.qrgame;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

/**
 * Represents the QR code object
 */
@IgnoreExtraProperties
public class QRCode implements Comparable, Serializable {

    private int score;
    private String hash;

    @PropertyName("name")
    private String name;

    @PropertyName("face")
    private String face;

    public QRCode() {
    }

    public QRCode(String data) throws NoSuchAlgorithmException {
        hash = QRCodeHasher.hash(data);
        score = calcScore();
        face = NameFaceScheme.generateFace(hash);
        name = NameFaceScheme.generateName(hash);
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

    @Override
    public int compareTo(Object o) {
        if (this.score > ((QRCode) o).getScore()) {
            return 1;
        }
        return 0;
    }

    //    private void setLocation(int latitude, int longitude) {
//        location.put("Latitude", latitude);
//        location.put("Longitude", longitude);
//    }
//
//    public HashMap<String, Integer> getLocation() {
//        return location;
//    }

//    public void addPlayer(Player player) {
//        playersList.add(player);
//    }

//    public ArrayList<Player> getPlayers() {
//        return playerList;
//    }
}



