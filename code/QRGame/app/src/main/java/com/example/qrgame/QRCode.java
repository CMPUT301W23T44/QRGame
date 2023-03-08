package com.example.qrgame;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Represents the QR code object
 */
public class QRCode implements Comparable, Serializable {

    private int score = 0;
    private String hash;
    private String name;
    private String face;

    private int RADIX = 16;
    private int ZERO_VALUE = 20;

    QRCode(String data) throws NoSuchAlgorithmException {
        hash = QRCodeHasher.hash(data);
        calcScore();
        generate();
    }

    /**
     * Calculates the score of a QR code based off the hash provided
     */
    private void calcScore() {
        String previousChar = String.valueOf(hash.charAt(0));
        String currentChar;
        int currentScoreValue = 1;

        // Finds consecutive instances of a character in the hash and bases the score off the
        // power of these consecutive values
        for (int i = 1; i < hash.length(); i++) {
            currentChar = String.valueOf(hash.charAt(i));
            if (currentChar.equals(previousChar)) {
                if (currentChar == "0") {
                    currentScoreValue *= ZERO_VALUE;
                } else {
                    currentScoreValue *= (Integer.parseInt(currentChar, RADIX));
                }
            } else {
                score += currentScoreValue;
                currentScoreValue = 1;
            }
            previousChar = currentChar;
        }
    }

    public int getScore() {
        return score;
    }

    /**
     * Generates a unique name and face for a QR code based
     * on a provided hash
     */
    private void generate() {
        name = NameFaceScheme.generateName(hash);
        face = NameFaceScheme.generateFace(hash);
    }

    public String getName() {
        return name;
    }

    public String getFace() { return face;}

    @Override
    public int compareTo(Object o) {
        if (this.score > ((QRCode) o).getScore()) {
            return 1;
        }
        return 0;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "name='" + name + '\'' +
                '}';
    }
}
